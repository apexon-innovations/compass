package com.apexon.compass.onboardservice.service.impl;

import static com.apexon.compass.constants.PsrServiceConstants.*;
import static com.apexon.compass.constants.PsrServiceConstants.PATH_SEPERATER;
import static com.apexon.compass.onboardservice.constants.CommonConstants.*;

import com.amazonaws.SdkClientException;
import com.apexon.compass.entities.*;
import com.apexon.compass.entities.ManagerDetails;
import com.apexon.compass.entities.Resources;
import com.apexon.compass.exception.custom.RecordNotFoundException;
import com.apexon.compass.exception.custom.ServiceException;
import com.apexon.compass.fileupload.dto.BlobObjectMetadata;
import com.apexon.compass.fileupload.dto.FileDownloadDto;
import com.apexon.compass.fileupload.dto.FileUploadDto;
import com.apexon.compass.fileupload.dto.PreSignedURLRequestDto;
import com.apexon.compass.fileupload.service.UploadService;
import com.apexon.compass.onboardservice.constants.CommonConstants;
import com.apexon.compass.onboardservice.constants.EncryptionProperties;
import com.apexon.compass.onboardservice.dao.*;
import com.apexon.compass.onboardservice.dto.*;
import com.apexon.compass.onboardservice.service.OnboardService;
import com.apexon.compass.onboardservice.util.EncryptionUtils;
import java.awt.image.BufferedImage;
import java.io.*;
import java.time.Instant;
import java.util.Collections;
import java.util.stream.Collectors;
import javax.imageio.ImageIO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.batik.transcoder.TranscoderException;
import org.apache.batik.transcoder.TranscoderInput;
import org.apache.batik.transcoder.TranscoderOutput;
import org.apache.batik.transcoder.image.PNGTranscoder;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;
import org.bson.types.ObjectId;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class OnboardServiceImpl implements OnboardService {

    private final JiraConfigurationDao jiraConfigurationDao;

    private final JiraRulesDao jiraRulesDao;

    private final ScmConfigurationDao scmConfigurationDao;

    private final SonarConfigurationDao sonarConfigurationDao;

    private final EncryptionProperties encryptionProperties;

    private final ProjectDao projectDao;

    private final UploadService uploadService;

    @Override
    public JiraConfigResponse addJiraConfig(JiraConfigRequest jiraConfigRequest) {
        JiraConfiguration jiraConfiguration = jiraConfigurationDao
            .saveJiraConfiguration(buildJiraConfigurationEntity(jiraConfigRequest));
        log.info("Stored jira config successfully for id {}", jiraConfigRequest.getProjectId());
        return buildJiraConfigResponse(jiraConfigRequest, jiraConfiguration);
    }

    @Override
    public JiraRulesResponse addJiraRule(JiraRulesRequest jiraRulesRequest) {
        JiraRules jiraRules = jiraRulesDao.addJiraRules(buildJiraRulesEntity(jiraRulesRequest));
        log.info("Stored jira rules config successfully for id {}", jiraRulesRequest.getProjectId());
        return buildJiraRulesResponse(jiraRules);
    }

    @Override
    public ScmConfigResponse addScmConfig(ScmConfigRequest scmConfigRequest) {
        ScmConfiguration scmConfiguration = scmConfigurationDao
            .addScmConfiguration(buildScmConfigEntity(scmConfigRequest));
        log.info("Stored scm config successfully for id {}", scmConfigRequest.getProjectId());
        return buildScmConfigResponse(scmConfiguration);
    }

    @Override
    public SonarConfigResponse addSonarConfig(SonarConfigRequest sonarConfigRequest) {
        SonarConfigurations sonarConfigurations = sonarConfigurationDao
            .addSonarConfig(buildSonarConfigEntity(sonarConfigRequest));
        log.info("Stored sonar config successfully for id {}", sonarConfigRequest.getProjectId());
        return buildSonarConfigResponse(sonarConfigurations);
    }

    @Override
    public ProjectResponse addProject(ProjectRequest projectRequest) {
        Project addedProject = projectDao.addProject(buildProjectEntity(projectRequest));
        return buildProjectResponse(addedProject);
    }

    private ProjectResponse buildProjectResponse(Project addedProject) {
        return ProjectResponse.builder()
            .id(addedProject.getId())
            .jiraProjectId(addedProject.getJiraProjectId())
            .name(addedProject.getName())
            .category(addedProject.getCategory())
            .billingType(addedProject.getBillingType())
            .startDate(String.valueOf(addedProject.getStartDate()))
            .endDate(String.valueOf(addedProject.getEndDate()))
            .pm(addedProject.getProjectManager()
                .stream()
                .map(manager -> com.apexon.compass.onboardservice.dto.ManagerDetails.builder()
                    .email(manager.getEmail())
                    .image(getPresignedURL(manager.getImageName()))
                    .name(manager.getName())
                    .build())
                .collect(Collectors.toList()))
            .dm(addedProject.getDeliveryManager()
                .stream()
                .map(manager -> com.apexon.compass.onboardservice.dto.ManagerDetails.builder()
                    .email(manager.getEmail())
                    .image(getPresignedURL(manager.getImageName()))
                    .name(manager.getName())
                    .build())
                .collect(Collectors.toList()))
            .clientPm(addedProject.getClientProjectManager()
                .stream()
                .map(manager -> ClientManager.builder().email(manager.getEmail()).name(manager.getName()).build())
                .collect(Collectors.toList()))
            .resources(addedProject.getResources()
                .stream()
                .map(resource -> com.apexon.compass.onboardservice.dto.Resources.builder()
                    .actual(resource.getActual())
                    .sow(resource.getSow())
                    .build())
                .collect(Collectors.toList()))
            .isDeleted(addedProject.isDeleted())
            .logoUrl(getPresignedURL(addedProject.getLogoName()))
            .createdAt(addedProject.getCreatedDate())
            .updatedAt(addedProject.getUpdatedDate())
            .createdBy(addedProject.getCreatedBy())
            .updatedBy(addedProject.getUpdatedBy())
            .build();
    }

    private Project buildProjectEntity(ProjectRequest projectRequest) {
        long epochMilli = Instant.now().toEpochMilli();
        IconUploadDto logo = buildIconUploadDto(projectRequest.getLogoImage(),
                String.valueOf(Instant.now().toEpochMilli()));
        return Project.builder()
            .name(projectRequest.getName())
            .clientName(projectRequest.getClientName())
            .category(projectRequest.getCategory())
            .billingType(projectRequest.getBillingType())
            .startDate(projectRequest.getStartDate())
            .endDate(projectRequest.getEndDate())
            .projectManager(projectRequest.getPm().stream().map(manager -> {
                IconUploadDto managerImage = buildIconUploadDto(manager.getImage(),
                        String.valueOf(Instant.now().toEpochMilli()));
                return ProjectManager.builder()
                    .email(manager.getEmail())
                    .name(manager.getName())
                    .image(uploadImageFileToS3(managerImage).getUri())
                    .imageName(managerImage.getFileName())
                    .build();
            }).collect(Collectors.toList()))
            .deliveryManager(projectRequest.getDm().stream().map(manager -> {
                IconUploadDto managerImage = buildIconUploadDto(manager.getImage(),
                        String.valueOf(Instant.now().toEpochMilli()));
                return ManagerDetails.builder()
                    .email(manager.getEmail())
                    .name(manager.getName())
                    .image(uploadImageFileToS3(managerImage).getUri())
                    .imageName(managerImage.getFileName())
                    .build();
            }).collect(Collectors.toList()))
            .clientProjectManager(projectRequest.getClientPm()
                .stream()
                .map(manager -> ManagerDetails.builder().email(manager.getEmail()).name(manager.getName()).build())
                .collect(Collectors.toList()))
            .resources(projectRequest.getResources()
                .stream()
                .map(resource -> Resources.builder().sow(resource.getSow()).actual(resource.getActual()).build())
                .collect(Collectors.toList()))
            .jiraProjectId(projectRequest.getJiraProjectId())
            .isDeleted(projectRequest.isDeleted())
            .logoUrl(uploadImageFileToS3(logo).getUri())
            .logoName(logo.getFileName())
            .createdDate(epochMilli)
            .createdBy(CREATOR_NAME)
            .updatedDate(epochMilli)
            .updatedBy(CREATOR_NAME)
            .build();
    }

    private IconUploadDto buildIconUploadDto(String file, String fileName) {
        String fileType = file.substring(file.indexOf(PATH_SEPERATER) + 1, file.indexOf(SEMICOLON_PUNCTUATION));
        log.info("FileType:::" + fileType);
        return IconUploadDto.builder()
            .fileName(fileName + FULLSTOP_PUNCTUATION + fileType)
            .file(file)
            .fileType(fileType)
            .build();
    }

    private SonarConfigResponse buildSonarConfigResponse(SonarConfigurations sonarConfigurations) {
        return SonarConfigResponse.builder()
            .id(String.valueOf(sonarConfigurations.getId()))
            .projectId(String.valueOf(sonarConfigurations.getProjectId()))
            .userName(sonarConfigurations.getUserName())
            .updatedAt(sonarConfigurations.getUpdatedDate())
            .updatedBy(sonarConfigurations.getUpdatedBy())
            .createdAt(sonarConfigurations.getCreatedDate())
            .createdBy(sonarConfigurations.getCreatedBy())
            .credentials(sonarConfigurations.getCredentials())
            .projects(sonarConfigurations.getProjects()
                .stream()
                .map(project -> ProjectsMetaData.builder()
                    .url(project.getUrl())
                    .key(project.getKey())
                    .language(project.getLanguage())
                    .build())
                .toList())
            .metricsFields(sonarConfigurations.getMetricsFields())
            .build();
    }

    private SonarConfigurations buildSonarConfigEntity(SonarConfigRequest sonarConfigRequest) {
        String encryptionKey = EncryptionUtils.generateEncryptionKey(32, encryptionProperties);
        long currentTimeStamp = Instant.now().toEpochMilli();
        return SonarConfigurations.builder()
            .projects(sonarConfigRequest.getProjects()
                .stream()
                .map(project -> Projects.builder()
                    .key(project.getKey())
                    .url(project.getUrl())
                    .language(project.getLanguage())
                    .build())
                .toList())
            .metricsFields(METRIC_NAMES_LIST)
            .userName(sonarConfigRequest.getUserName())
            .credentials(
                    EncryptionUtils.encryptString(sonarConfigRequest.getCredentials() + encryptionProperties.getSalt(),
                            encryptionKey, encryptionProperties.getSalt()))
            .encryptionKey(encryptionKey)
            .createdBy(CREATOR_NAME)
            .updatedBy(CREATOR_NAME)
            .createdDate(currentTimeStamp)
            .updatedDate(currentTimeStamp)
            .build();
    }

    private ScmConfigResponse buildScmConfigResponse(ScmConfiguration scmConfiguration) {
        return ScmConfigResponse.builder()
            .id(scmConfiguration.getId())
            .projectId(String.valueOf(scmConfiguration.getProjectId()))
            .codeChurnDelta(scmConfiguration.getCodeChurnDelta())
            .legacyRefactorDelta(scmConfiguration.getLegacyRefactorDelta())
            .source(CommonConstants.Source.valueOf(scmConfiguration.getSource()))
            .product(CommonConstants.Product.valueOf(scmConfiguration.getProduct()))
            .createdAt(scmConfiguration.getCreatedDate())
            .createdBy(scmConfiguration.getCreatedBy())
            .updatedAt(scmConfiguration.getUpdatedDate())
            .updatedBy(scmConfiguration.getUpdatedBy())
            .userName(scmConfiguration.getUserName())
            .credentials(EncryptionUtils.decryptString(scmConfiguration.getCredentials(),
                    scmConfiguration.getEncryptionKey(), encryptionProperties.getSalt()))
            .scm(scmConfiguration.getScm()
                .stream()
                .map(scm -> ScmRepoDetails.builder()
                    .repoUrl(scm.getRepoUrl())
                    .repoName(scm.getRepoName())
                    .projectLanguage(scm.getProjectLanguage())
                    .defaultBranchToScan(scm.getDefaultBranchToScan())
                    .build())
                .toList())
            .build();
    }

    private ScmConfiguration buildScmConfigEntity(ScmConfigRequest scmConfigRequest) {
        String encryptionKey = EncryptionUtils.generateEncryptionKey(32, encryptionProperties);
        long currentTimeStamp = Instant.now().toEpochMilli();
        return ScmConfiguration.builder()
            .projectId(new ObjectId(scmConfigRequest.getProjectId()))
            .scm(scmConfigRequest.getScm()
                .stream()
                .map(scm -> Scm.builder()
                    .repoUrl(scm.getRepoUrl())
                    .projectLanguage(scm.getProjectLanguage())
                    .repoName(scm.getRepoName())
                    .defaultBranchToScan(scm.getDefaultBranchToScan())
                    .build())
                .toList())
            .userName(scmConfigRequest.getUserName())
            .credentials(EncryptionUtils.encryptString(scmConfigRequest.getCredentials(), encryptionKey,
                    encryptionProperties.getSalt()))
            .encryptionKey(encryptionKey)
            .product(scmConfigRequest.getProduct().toString())
            .source(scmConfigRequest.getSource().toString())
            .codeChurnDelta(scmConfigRequest.getCodeChurnDelta())
            .legacyRefactorDelta(scmConfigRequest.getLegacyRefactorDelta())
            .createdBy(CREATOR_NAME)
            .createdDate(currentTimeStamp)
            .updatedBy(CREATOR_NAME)
            .updatedDate(currentTimeStamp)
            .build();
    }

    private JiraRulesResponse buildJiraRulesResponse(JiraRules jiraRules) {
        return JiraRulesResponse.builder()
            .id(jiraRules.getId())
            .projectId(String.valueOf(jiraRules.getProjectId()))
            .jiraProjectId(Integer.parseInt(jiraRules.getJiraProjectId()))
            .definitionOfTodo(jiraRules.getDefinitionOfTodo())
            .definitionOfInProgress(jiraRules.getDefinitionOfInProgress())
            .definitionOfDevComplete(jiraRules.getDefinitionOfDevComplete())
            .definitionOfQaComplete(jiraRules.getDefinitionOfQaComplete())
            .definitionOfDone(jiraRules.getDefinitionOfDone())
            .definitionOfAccepted(jiraRules.getDefinitionOfAccepted())
            .storyTypes(jiraRules.getStoryTypes())
            .defaultPriority(jiraRules.getDefaultPriority())
            .bugTypes(jiraRules.getBugTypes())
            .taskTypes(jiraRules.getTaskTypes())
            .velocityFormula(jiraRules.getVelocityFormula())
            .blockerDefinition(jiraRules.getBlockerDefinition())
            .flagDefinition(jiraRules.getFlagDefinition())
            .createdAt(jiraRules.getCreatedDate())
            .updatedAt(jiraRules.getUpdatedDate())
            .createdBy(jiraRules.getCreatedBy())
            .updatedBy(jiraRules.getUpdatedBy())
            .build();
    }

    private JiraRules buildJiraRulesEntity(JiraRulesRequest jiraRulesRequest) {
        long currentTimeStamp = Instant.now().toEpochMilli();
        return JiraRules.builder()
            .jiraProjectId(String.valueOf(jiraRulesRequest.getJiraProjectId()))
            .projectId(new ObjectId(jiraRulesRequest.getProjectId()))
            .storyTypes(jiraRulesRequest.getStoryTypes())
            .bugTypes(jiraRulesRequest.getBugTypes())
            .taskTypes(jiraRulesRequest.getTaskTypes())
            .definitionOfTodo(jiraRulesRequest.getDefinitionOfTodo())
            .definitionOfInProgress(jiraRulesRequest.getDefinitionOfInProgress())
            .definitionOfDone(jiraRulesRequest.getDefinitionOfDone())
            .definitionOfAccepted(jiraRulesRequest.getDefinitionOfAccepted())
            .definitionOfDevComplete(jiraRulesRequest.getDefinitionOfDevComplete())
            .definitionOfQaComplete(jiraRulesRequest.getDefinitionOfQaComplete())
            .velocityFormula(StringUtils.EMPTY)
            .blockerDefinition(jiraRulesRequest.getBlockerDefinition())
            .flagDefinition(jiraRulesRequest.getFlagDefinition())
            .rangeCategory(Collections.emptyList())
            .defaultPriority(jiraRulesRequest.getDefaultPriority())
            // .estimationType()
            .createdBy(CREATOR_NAME)
            .updatedBy(CREATOR_NAME)
            .createdDate(currentTimeStamp)
            .updatedDate(currentTimeStamp)
            .build();
    }

    private JiraConfigResponse buildJiraConfigResponse(JiraConfigRequest jiraConfigRequest,
            JiraConfiguration jiraConfiguration) {
        return JiraConfigResponse.builder()
            .id(jiraConfiguration.getId())
            .projectId(String.valueOf(jiraConfiguration.getProjectId()))
            .jiraProjectId(jiraConfiguration.getJiraProjectId())
            .jiraBoardId(jiraConfigRequest.getJiraBoardId())
            .jiraUrl(jiraConfiguration.getUrl())
            .queryEndPoint(jiraConfiguration.getQueryEndPoint())
            .projectStartDate(jiraConfiguration.getProjectStartDate())
            .jiraApiKey(jiraConfiguration.getJiraApiKey())
            .issueTypes(jiraConfiguration.getIssueTypes())
            .sprintDataFieldName(jiraConfiguration.getSprintDataFieldName())
            .jiraEpicIdFieldName(jiraConfiguration.getJiraEpicIdFieldName())
            .jiraStoryPointsFieldName(jiraConfiguration.getJiraStoryPointsFieldName())
            .jiraBoardAsTeam(jiraConfiguration.isJiraBoardAsTeam())
            .issueTypesId(jiraConfiguration.getIssueTypesId())
            .createdBy(jiraConfiguration.getCreatedBy())
            .createdAt(jiraConfiguration.getCreatedDate())
            .updatedAt(jiraConfiguration.getUpdatedDate())
            .updatedBy(jiraConfiguration.getUpdatedBy())
            .build();
    }

    private JiraConfiguration buildJiraConfigurationEntity(JiraConfigRequest jiraConfigRequest) {
        long currentTimeStamp = Instant.now().toEpochMilli();
        return JiraConfiguration.builder()
            .projectId(new ObjectId(jiraConfigRequest.getProjectId()))
            .jiraProjectId(jiraConfigRequest.getJiraProjectId())
            .jiraBoardId(jiraConfigRequest.getJiraBoardId())
            .url(jiraConfigRequest.getJiraUrl())
            .queryEndPoint(JIRA_QUERY_ENDPOINT)
            .projectStartDate(Instant.now().toEpochMilli())
            .jiraApiKey(jiraConfigRequest.getJiraApiKey())
            .issueTypes(jiraConfigRequest.getIssueTypes())
            .sprintDataFieldName(jiraConfigRequest.getSprintDataFieldName())
            .jiraEpicIdFieldName(jiraConfigRequest.getJiraEpicIdFieldName())
            .jiraStoryPointsFieldName(jiraConfigRequest.getJiraStoryPointsFieldName())
            .jiraBoardAsTeam(true)
            .issueTypesId(jiraConfigRequest.getIssueTypesId())
            .createdBy(CREATOR_NAME)
            .createdDate(currentTimeStamp)
            .updatedBy(CREATOR_NAME)
            .updatedDate(currentTimeStamp)
            .build();
    }

    private BlobObjectMetadata uploadImageFileToS3(IconUploadDto iconUploadDto) throws ServiceException {
        String file = iconUploadDto.getFile().substring(iconUploadDto.getFile().indexOf(COMMA_PUNCTUATION) + 1);
        validateImageSize(file, iconUploadDto.getFileType());
        StringBuilder path = new StringBuilder();
        path.append(COMPASS_IMAGES).append(PATH_SEPERATER).append(iconUploadDto.getFileName());
        try {
            FileUploadDto fileUploadDto = new FileUploadDto(getAwsRegion(), COMPASS_IMAGES, String.valueOf(path), file);
            uploadService.uploadBase64ImageToS3(fileUploadDto);
            FileDownloadDto fileDownloadDto = new FileDownloadDto(getAwsRegion(), COMPASS_IMAGES,
                    fileUploadDto.getPath());
            return uploadService.downloadFromStorage(fileDownloadDto);
        }
        catch (SdkClientException e) {
            log.error("Error while uploading image to s3", e);
            throw new ServiceException("Connection error with Amazon" + e.getMessage());
        }
    }

    private String getPresignedURL(String fileName) throws SdkClientException {
        if (StringUtils.isNotEmpty(fileName)) {
            String path = COMPASS_IMAGES + PATH_SEPERATER + fileName;
            FileDownloadDto fileDownloadDto = new FileDownloadDto(getAwsRegion(), COMPASS_IMAGES, path);
            BlobObjectMetadata s3Object = uploadService.downloadFromStorage(fileDownloadDto);
            PreSignedURLRequestDto requestDto = new PreSignedURLRequestDto(s3Object.getBlobName(), s3Object.getKey());
            return uploadService.generatePresignedUrl(requestDto);
        }
        return org.apache.commons.lang3.StringUtils.EMPTY;
    }

    private String getAwsRegion() {
        return uploadService.getBlobRegion();
    }

    public void validateImageSize(String base64, String fileType) {
        byte[] imageByte = Base64.decodeBase64(base64.getBytes());
        InputStream byteArrayInputStream = new ByteArrayInputStream(imageByte);
        try {
            BufferedImage bufferedImage = ImageIO.read(byteArrayInputStream);
            log.info("width-->" + bufferedImage.getWidth() + "Height-->" + bufferedImage.getHeight());
            if (bufferedImage.getHeight() > 1024 || bufferedImage.getWidth() > 1024) {
                throw new ServiceException("File size too large, Please upload 1024x1024 images.");
            }
            if (fileType.contains(SVG_TYPE)) {
                TranscoderInput transcoderInput = new TranscoderInput(byteArrayInputStream);
                ByteArrayOutputStream pngOstream = new ByteArrayOutputStream();
                TranscoderOutput outputPngImage = new TranscoderOutput(pngOstream);
                PNGTranscoder myConverter = new PNGTranscoder();
                myConverter.transcode(transcoderInput, outputPngImage);
                byte[] pngByte = pngOstream.toByteArray();
                bufferedImage = ImageIO.read(new ByteArrayInputStream(pngByte));
                pngOstream.flush();
                pngOstream.close();
            }
            if (bufferedImage == null) {
                throw new ServiceException("Image is not available");
            }
            byteArrayInputStream.close();
        }
        catch (IllegalArgumentException e) {
            throw new ServiceException("No base64 String");
        }
        catch (IOException e) {
            throw new RecordNotFoundException("File not found");
        }
        catch (TranscoderException e) {
            throw new ServiceException("Error while reading svg File");
        }
    }

}
