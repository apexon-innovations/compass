package com.apexon.compass.psrservice.service.impl;

import com.amazonaws.SdkClientException;
import com.amazonaws.services.s3.model.S3Object;
import com.apexon.compass.aggregation.vo.NpsReportsByProjectIdVo;
import com.apexon.compass.entities.*;
import com.apexon.compass.exception.custom.BadRequestException;
import com.apexon.compass.exception.custom.RecordNotFoundException;
import com.apexon.compass.exception.custom.ServiceException;
import com.apexon.compass.exception.custom.UnauthorizedException;
import com.apexon.compass.fileupload.dto.BlobObjectMetadata;
import com.apexon.compass.fileupload.dto.FileDownloadDto;
import com.apexon.compass.fileupload.dto.FileUploadDto;
import com.apexon.compass.fileupload.service.UploadService;
import com.apexon.compass.psrservice.dao.ProjectDetailsDao;
import com.apexon.compass.psrservice.dto.*;
import com.apexon.compass.psrservice.enums.AcceptedExcelTypes;
import com.apexon.compass.psrservice.repository.ProjectRepository;
import com.apexon.compass.psrservice.repository.IscProjectsNpsRepository;
import com.apexon.compass.psrservice.service.IscService;
import com.apexon.compass.psrservice.service.impl.helper.AsyncUtil;
import com.apexon.compass.psrservice.service.impl.helper.ProjectHelper;
import com.apexon.compass.utilities.DateTimeUtils;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.xml.bind.DatatypeConverter;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.ParseException;
import java.time.ZonedDateTime;
import java.util.*;

import static com.apexon.compass.constants.CompassUtilityConstants.*;
import static com.apexon.compass.constants.PsrServiceConstants.*;
import static com.apexon.compass.constants.WidgetsNameConstants.GET_STORE_NPS_DATA;
import static com.apexon.compass.constants.WidgetsNameConstants.WIDGET_VALIDATION_EXCEPTION_MESSAGE;

@Slf4j
@Service
@AllArgsConstructor(onConstructor = @__({ @Autowired }))
public class IscServiceImpl implements IscService {

    private ProjectRepository projectRepository;

    private IscProjectsNpsRepository iscProjectsNpsRepository;

    private ProjectHelper projectHelper;

    private ProjectDetailsDao projectDetailsDao;

    private UploadService uploadService;

    private AsyncUtil asyncUtil;

    private Environment environment;

    @Override
    public OnboardingProjectResponseDto onboardProject(OnboardingProjectRequestDto onboardingProjectRequestDto) {
        List<Jira> jiraList = new ArrayList<>();
        List<Sonar> sonarList = new ArrayList<>();
        List<BitBucket> bitBucketList = new ArrayList<>();
        if (StringUtils.isBlank(onboardingProjectRequestDto.getNestId())) {
            onboardingProjectRequestDto.setNestId(DEFAULT_ID);
        }
        List<Project> project = projectRepository.findByNameAndIsDeletedFalseOrNestIdsInAndIsDeletedFalse(
                onboardingProjectRequestDto.getName(),
                Arrays.asList(onboardingProjectRequestDto.getNestId().split(",")));
        if (CollectionUtils.isNotEmpty(project)) {
            throw new BadRequestException("Project already exists with same name or nestId");
        }
        if (onboardingProjectRequestDto.getJira() != null) {
            onboardingProjectRequestDto.getJira().forEach(jiraDto -> {
                if (StringUtils.isBlank(jiraDto.getId())) {
                    jiraDto.setId(DEFAULT_ID);
                }
                jiraList.add(Jira.builder().id(jiraDto.getId()).url(jiraDto.getUrl()).build());
            });
        }
        if (onboardingProjectRequestDto.getBitbucket() != null) {
            onboardingProjectRequestDto.getBitbucket()
                .forEach(bitbucketDto -> bitBucketList
                    .add(BitBucket.builder().name(bitbucketDto.getName()).url(bitbucketDto.getUrl()).build()));
        }
        if (onboardingProjectRequestDto.getSonar() != null) {
            onboardingProjectRequestDto.getSonar()
                .forEach(sonarDto -> sonarList.add(Sonar.builder()
                    .key(sonarDto.getProjectKey())
                    .language(sonarDto.getLanguage())
                    .url(sonarDto.getUrl())
                    .build()));
        }

        IconUploadDto iconUploadDto = IconUploadDto.builder()
            .file(onboardingProjectRequestDto.getFile())
            .fileName(onboardingProjectRequestDto.getFileName())
            .build();
        String svnUrl = uploadPsrExcel(onboardingProjectRequestDto.getPsrFile().getFileName(),
                onboardingProjectRequestDto.getPsrFile().getFile());
        Project iscProject = projectRepository.save(Project.builder()
            .name(onboardingProjectRequestDto.getName())
            .nestIds(Arrays.asList(onboardingProjectRequestDto.getNestId().split(",")))
            .jira(jiraList)
            .sonar(sonarList)
            .bitbucket(bitBucketList)
            .fileName(iconUploadDto.getFileName())
            .psrLocation(svnUrl)
            .logoUrl(uploadImageFileToS3(iconUploadDto).getUri())
            .build());

        log.info("New Project Created" + iscProject.getId());

        return OnboardingProjectResponseDto.builder()
            .id(iscProject.getId())
            .name(iscProject.getName())
            .psrFile(PsrFileResponseDto.builder()
                .fileName(onboardingProjectRequestDto.getPsrFile().getFileName())
                .fileUrl(iscProject.getPsrLocation())
                .build())
            .nestIds(iscProject.getNestIds())
            .logo(projectHelper.getPresignedURL(iconUploadDto.getFileName()))
            .bitbucket(iscProject.getBitbucket()
                .stream()
                .map(bitBucket -> BitBucketDto.builder().name(bitBucket.getName()).url(bitBucket.getUrl()).build())
                .toList())
            .sonar(iscProject.getSonar()
                .stream()
                .map(sonar -> SonarDto.builder()
                    .projectKey(sonar.getKey())
                    .language(sonar.getLanguage())
                    .url(sonar.getUrl())
                    .build())
                .toList())
            .jira(iscProject.getJira()
                .stream()
                .map(jira -> JiraDto.builder().id(jira.getId()).url(jira.getUrl()).build())
                .toList())
            .build();
    }

    private String uploadPsrExcel(String fileName, String psrFile) {
        String data = psrFile.substring(psrFile.indexOf(COMMA_PUNCTUATION) + 1);
        MultipartFile file = new MockMultipartFile(fileName, fileName, MediaType.MULTIPART_FORM_DATA_VALUE,
                DatatypeConverter.parseBase64Binary(data));
        if (file.getSize() == NumberUtils.LONG_ZERO) {
            throw new BadRequestException(EMPTY_FILE_ERROR_MESSAGE);
        }
        if (!AcceptedExcelTypes.contains(FilenameUtils.getExtension(file.getName()))) {
            AcceptedExcelTypes[] acceptedValues = AcceptedExcelTypes.values();
            StringBuilder errorMessage = new StringBuilder(FILE_TYPE_ERROR);
            for (AcceptedExcelTypes value : acceptedValues)
                errorMessage.append(value.getValue()).append(COMMA);
            throw new BadRequestException(errorMessage.substring(0, errorMessage.length() - 1));
        }
        return uploadFileToS3(file);
    }

    private String uploadFileToS3(MultipartFile multipartFile) {
        File currentWorkingRepository = createTempDirectory();
        Path directory = Paths.get(currentWorkingRepository + PATH_SEPERATER + multipartFile.getName());
        try {
            Files.write(directory, multipartFile.getBytes());
        }
        catch (IOException e) {
            throw new ServiceException(FILE_CREATION_ERROR);
        }
        StringBuilder path = new StringBuilder(ACTIVE_DIRECTORY);
        StringBuilder bucket = getPsrBucketName();
        path.append(PATH_SEPERATER).append(multipartFile.getOriginalFilename());
        try {
            if (uploadService.checkIfFileExist(String.valueOf(bucket), String.valueOf(path))) {
                throw new BadRequestException("File already exists");
            }
            FileUploadDto fileUploadDto = new FileUploadDto(getAwsRegion(), String.valueOf(bucket),
                    String.valueOf(path), new File(directory.toString()));
            uploadService.uploadPsrFile(fileUploadDto);
            asyncUtil.deleteTempFolder(currentWorkingRepository);
        }
        catch (SdkClientException e) {
            throw new ServiceException("Connection error with Amazon" + e.getMessage());
        }
        return String.valueOf(path);
    }

    private StringBuilder getPsrBucketName() {
        StringBuilder bucketName = new StringBuilder();
        if (Arrays.stream(environment.getActiveProfiles()).count() != 0
                || Arrays.stream(environment.getDefaultProfiles()).count() == 0) {
            return bucketName.append(
                    String.format(COMPASS_PSR, Arrays.stream(environment.getActiveProfiles()).findAny().orElse(null)));
        }
        else {
            throw new ServiceException("No profile found");
        }
    }

    private File createTempDirectory() {
        try {
            Path tempDirectory = Files.createTempDirectory(null);
            return Files.createTempDirectory(tempDirectory, null).toFile();
        }
        catch (IOException e) {
            throw new ServiceException(FILE_CREATION_ERROR);
        }
    }

    @Override
    public OffboardingProjectResponseDto offboardProject(String iscProjectId) {
        Project project = projectRepository.findById(iscProjectId)
            .orElseThrow(() -> new RecordNotFoundException("Project Id is invalid"));
        project.setDeleted(true);
        project = projectRepository.save(project);
        return OffboardingProjectResponseDto.builder()
            .id(project.getId())
            .name(project.getName())
            .psrLocation(project.getPsrLocation())
            .bitbucket(project.getBitbucket()
                .stream()
                .map(bitBucket -> BitBucketDto.builder().name(bitBucket.getName()).url(bitBucket.getUrl()).build())
                .toList())
            .nestIds(project.getNestIds())
            .jira(project.getJira()
                .stream()
                .map(jira -> JiraDto.builder().id(jira.getId()).url(jira.getUrl()).build())
                .toList())
            .sonar(project.getSonar()
                .stream()
                .map(sonar -> SonarDto.builder()
                    .language(sonar.getKey())
                    .url(sonar.getUrl())
                    .language(sonar.getLanguage())
                    .build())
                .toList())
            .isDeleted(true)
            .build();
    }

    @Override
    public NpsDataResponseDto storeNpsData(String id, NpsDataRequestDto npsDataRequestDto)
            throws UnauthorizedException, ParseException {
        List<Survey> surveyList = new ArrayList<>();
        List<OverallSatisfaction> overallSatisfactionList = new ArrayList<>();
        projectHelper.findAndValidateProjectByIdAndUser(id);
        List<String> assingedWidgets = projectHelper.getWidget();
        if (!assingedWidgets.contains(GET_STORE_NPS_DATA)) {
            throw new RecordNotFoundException(WIDGET_VALIDATION_EXCEPTION_MESSAGE);
        }
        if (npsDataRequestDto.getSurvey() != null) {
            npsDataRequestDto.getSurvey()
                .forEach(survey -> surveyList.add(Survey.builder()
                    .type(survey.getKey())
                    .rating(Integer.parseInt(survey.getRating()))
                    .response(survey.getResponse())
                    .build()));
        }
        if (npsDataRequestDto.getOverallSatisfaction() != null) {
            npsDataRequestDto.getOverallSatisfaction()
                .forEach(overallSatisfaction -> overallSatisfactionList.add(OverallSatisfaction.builder()
                    .key(overallSatisfaction.getKey())
                    .rating(overallSatisfaction.getRating())
                    .response(overallSatisfaction.getResponse())
                    .build()));
        }
        if (ZonedDateTime.now().toInstant().toEpochMilli() <= DateTimeUtils
            .dateTimeToEpoch(npsDataRequestDto.getSubmissionDate())) {
            throw new BadRequestException("Invalid SubmissionDate");
        }
        IscProjectsNps iscProjectsNps = iscProjectsNpsRepository.save(IscProjectsNps.builder()
            .fileName(npsDataRequestDto.getFileName())
            .iscProjectId(new ObjectId(id))
            .submissionDate(DateTimeUtils.dateTimeToEpoch(npsDataRequestDto.getSubmissionDate()))
            .submissionPeriod(DateTimeUtils.getLastDateOfMonth(npsDataRequestDto.getSubmissionPeriod()))
            .teamSize(npsDataRequestDto.getTeamSize())
            .duration(npsDataRequestDto.getDuration())
            .conclusionRemarks(npsDataRequestDto.getConclusionRemarks())
            .customer(Customer.builder()
                .name(npsDataRequestDto.getCustomer().getName())
                .designation(npsDataRequestDto.getCustomer().getRole())
                .build())
            .survey(surveyList)
            .overallSatisfaction(overallSatisfactionList)
            .build());

        return NpsDataResponseDto.builder()
            .id(iscProjectsNps.getId())
            .submissionDate(iscProjectsNps.getSubmissionDate())
            .submissionPeriod(npsDataRequestDto.getSubmissionPeriod())
            .teamSize(iscProjectsNps.getTeamSize())
            .duration(iscProjectsNps.getDuration())
            .conclusionRemarks(iscProjectsNps.getTeamSize())
            .customer(CustomerDto.builder()
                .name(iscProjectsNps.getCustomer().getName())
                .role(iscProjectsNps.getCustomer().getDesignation())
                .build())
            .survey(iscProjectsNps.getSurvey()
                .stream()
                .map(survey -> SurveyDto.builder()
                    .key(survey.getType())
                    .rating(Integer.toString(survey.getRating()))
                    .response(survey.getResponse())
                    .build())
                .toList())
            .overallSatisfaction(iscProjectsNps.getOverallSatisfaction()
                .stream()
                .map(overallSatisfaction -> OverallSatisfactionDto.builder()
                    .key(overallSatisfaction.getKey())
                    .rating(overallSatisfaction.getRating())
                    .response(overallSatisfaction.getResponse())
                    .build())
                .toList())

            .build();
    }

    @Override
    public NpsReportsDto getNpsReport(String id, String reportType) throws UnauthorizedException, ParseException {
        projectHelper.findAndValidateProjectByIdAndUser(id);
        NpsReportsByProjectIdVo npsReportsByProjectId = projectDetailsDao.getNpsReport(id,
                ((reportType.equalsIgnoreCase(HISTORICAL)) ? 5 : 1));
        if (CollectionUtils.isEmpty(npsReportsByProjectId.getNps_score())) {
            throw new RecordNotFoundException("No data available");
        }
        return HISTORICAL.equalsIgnoreCase(reportType) ? buildHistoricalResponse(npsReportsByProjectId)
                : buildRecentResponse(npsReportsByProjectId);
    }

    private NpsReportsDto buildHistoricalResponse(NpsReportsByProjectIdVo npsReportsByProjectId) {

        return NpsReportshistoricalDto.builder()
            .iscProjectId(npsReportsByProjectId.getId())
            .averageNps(Math.round(((npsReportsByProjectId.getNps_score()
                .stream()
                .mapToDouble(nps -> nps.getSurvey().stream().mapToDouble(Survey::getRating).average().orElse(0))
                .reduce(0, Double::sum)) / npsReportsByProjectId.getNps_score().size()) * 100.0) / 100.0)
            .survey(npsReportsByProjectId.getNps_score()
                .stream()
                .map(npsReportsValue -> HistoricalSurveyDto.builder()
                    .duration(DateTimeUtils.getLocalDateTimeToString(npsReportsValue.getSubmissionPeriod()))
                    .average((double) npsReportsValue.getSurvey().stream().mapToInt(Survey::getRating).sum() / 10)
                    .value(npsReportsValue.getSurvey()
                        .stream()
                        .map(survey -> NpsReportsValueDto.builder()
                            .key(survey.getType())
                            .rating(Integer.toString(survey.getRating()))
                            .build())
                        .toList())
                    .build())
                .toList())
            .build();
    }

    private NpsReportsDto buildRecentResponse(NpsReportsByProjectIdVo npsReportsByProjectId) {

        return NpsReportsRecentDto.builder()
            .iscProjectId(npsReportsByProjectId.getId())
            .averageNps((double) npsReportsByProjectId.getNps_score()
                .get(0)
                .getSurvey()
                .stream()
                .mapToInt(Survey::getRating)
                .sum() / 10)
            .survey(RecentSurveyDto.builder()
                .duration(DateTimeUtils
                    .getLocalDateTimeToString(npsReportsByProjectId.getNps_score().get(0).getSubmissionPeriod()))
                .value(npsReportsByProjectId.getNps_score()
                    .get(0)
                    .getSurvey()
                    .stream()
                    .map(survey -> NpsReportsValueDto.builder()
                        .key(survey.getType())
                        .rating(Integer.toString(survey.getRating()))
                        .build())
                    .toList())
                .build())
            .build();
    }

    @Override
    public IconDto updateIcon(String id, IconUploadDto iconUploadDto) throws RecordNotFoundException, ServiceException {
        Optional<Project> iscProjects = projectRepository.findById(id);
        if (!iscProjects.isPresent()) {
            throw new RecordNotFoundException("Project does not Exist");
        }
        BlobObjectMetadata s3Object = uploadImageFileToS3(iconUploadDto);
        iscProjects.get().setLogoUrl(String.valueOf(s3Object.getUri()));
        iscProjects.get().setFileName(iconUploadDto.getFileName());
        projectRepository.save(iscProjects.get());
        return IconDto.builder()
            .id(iscProjects.get().getId())
            .file(projectHelper.getPresignedURL(iconUploadDto.getFileName()))
            .fileName(s3Object.getKey())
            .build();
    }

    private BlobObjectMetadata uploadImageFileToS3(IconUploadDto iconUploadDto) throws ServiceException {
        String fileType = iconUploadDto.getFile()
            .substring(iconUploadDto.getFile().indexOf(PATH_SEPERATER) + 1,
                    iconUploadDto.getFile().indexOf(SEMICOLON_PUNCTUATION));
        log.info("FileType:::" + fileType);
        String file = iconUploadDto.getFile().substring(iconUploadDto.getFile().indexOf(COMMA_PUNCTUATION) + 1);
        projectHelper.covertBase64ToFile(file, iconUploadDto.getFileName(), fileType);
        StringBuilder bucketName = projectHelper.getBucketName();
        StringBuilder path = new StringBuilder();
        path.append(bucketName).append(PATH_SEPERATER).append(iconUploadDto.getFileName());
        try {
            FileUploadDto fileUploadDto = new FileUploadDto(getAwsRegion(), String.valueOf(bucketName),
                    String.valueOf(path), file);
            uploadService.uploadBase64ImageToS3(fileUploadDto);
            FileDownloadDto fileDownloadDto = new FileDownloadDto(getAwsRegion(), String.valueOf(bucketName),
                    fileUploadDto.getPath());
            return uploadService.downloadFromStorage(fileDownloadDto);
        }
        catch (SdkClientException e) {
            throw new ServiceException("Connection error with Amazon" + e.getMessage());
        }
    }

    private String getAwsRegion() {
        return uploadService.getBlobRegion();
    }

}
