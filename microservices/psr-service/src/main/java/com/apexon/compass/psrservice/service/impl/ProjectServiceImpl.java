package com.apexon.compass.psrservice.service.impl;

import com.apexon.compass.aggregation.vo.*;
import com.apexon.compass.entities.*;
import com.apexon.compass.exception.custom.RecordNotFoundException;
import com.apexon.compass.exception.custom.ServiceException;
import com.apexon.compass.exception.custom.TypeConversionException;
import com.apexon.compass.psrservice.dao.NestProjectsDao;
import com.apexon.compass.psrservice.dao.ProjectDetailsDao;
import com.apexon.compass.psrservice.dao.ScmCodeAnalysisMonthDao;
import com.apexon.compass.psrservice.dao.SprintDao;
import com.apexon.compass.psrservice.service.impl.helper.ProjectHelper;
import com.apexon.compass.psrservice.dto.*;
import com.apexon.compass.psrservice.repository.*;
import com.apexon.compass.psrservice.service.ProjectService;
import com.apexon.compass.psrservice.utils.DateTimeUtils;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.bson.types.ObjectId;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

import static com.apexon.compass.constants.PsrServiceConstants.*;
import static com.apexon.compass.utilities.ArithmeticUtils.getMatcherCount;
import static com.apexon.compass.utilities.ArithmeticUtils.replaceNonNumericsWithZero;
import static com.apexon.compass.utilities.DateTimeUtils.convertDateToMonth;
import static com.apexon.compass.utilities.DateTimeUtils.convertDateToWeek;
import static org.springframework.data.domain.Sort.Direction.DESC;

@Service
@Slf4j
@AllArgsConstructor
public class ProjectServiceImpl implements ProjectService {

    public static final String NO_PROJECT_DATA_AVAILABLE = "No project data available";

    public static final String NO_RECORD_FOUND = "No record found";

    private ProjectMonthlyRepository projectMonthlyRepository;

    private ProjectWeeklyRepository projectWeeklyRepository;

    private ProjectOneTimeRepository projectOneTimeRepository;

    private ProjectRepository projectRepository;

    private ProjectDetailsDao projectDetailsDao;

    private ProjectHelper projectHelper;

    private ProjectComplianceRepository projectComplianceRepository;

    private SprintDao sprintDao;

    private NestProjectsDao nestProjectsDao;

    private ScmCodeAnalysisMonthDao scmCodeAnalysisMonthDao;

    @Override
    public List<OverallHealthDto> getAllProjectDetails(String emailAddress) {
        List<Project> projectDetails = projectRepository
            .findByProjectManagerEmailAndIsDeletedInOrDeliveryManagerEmailAndIsDeletedIn(emailAddress,
                    Arrays.asList(null, Boolean.FALSE), emailAddress, Arrays.asList(null, Boolean.FALSE));
        if (projectDetails.isEmpty()) {
            throw new RecordNotFoundException("Invalid email id");
        }
        List<OverallHealthDto> proejctHealthList = projectDetails.stream().map(this::convertToOverallHealth).toList();
        proejctHealthList.forEach(projectHealth -> {
            List<ProjectWeekly> weeklyData = projectWeeklyRepository
                .findByProjectID(new ObjectId(projectHealth.getProjectID()), PageRequest.of(0, 1, Sort.by(DESC, DATE)));
            ProjectWeekly weekly = null;
            if (CollectionUtils.isNotEmpty(weeklyData)) {
                weekly = weeklyData.get(0);
            }
            ProjectOneTime oneTime = projectOneTimeRepository
                .findByProjectIDAndIsArchive(new ObjectId(projectHealth.getProjectID()), false);
            projectHealth.setConsolidatedMetrics(
                    configureConsolidatedMetrics(oneTime == null ? ProjectOneTime.builder().build() : oneTime,
                            Objects.isNull(weekly) ? ProjectWeekly.builder().build() : weekly));
            ProjectWeekly projectWeekly = Objects.isNull(weekly) ? ProjectWeekly.builder().build() : weekly;
            projectHealth.setOverallHealth(projectWeekly.getOverallHealth());
            projectHealth.setLastUpdatedTime(projectWeekly.getOverallStatusUpdateDate());
        });
        return proejctHealthList;
    }

    @Override
    public List<OverallHealthDto> getClientProjectDetails(String emailAddress) {
        List<Project> projectDetails = projectRepository.findByClientProjectManagerEmailAndIsDeletedIn(emailAddress,
                Arrays.asList(null, Boolean.FALSE));
        if (projectDetails.isEmpty()) {
            throw new RecordNotFoundException("Invalid email id");
        }
        List<OverallHealthDto> proejctHealthList = projectDetails.stream().map(this::convertToOverallHealth).toList();
        proejctHealthList.forEach(projectHealth -> {
            List<ProjectWeekly> weeklyList = projectWeeklyRepository
                .findByProjectID(new ObjectId(projectHealth.getProjectID()), PageRequest.of(0, 2, Sort.by(DESC, DATE)));
            ProjectOneTime oneTime = projectOneTimeRepository
                .findByProjectIDAndIsArchive(new ObjectId(projectHealth.getProjectID()), false);
            projectHealth.setConsolidatedMetrics(
                    configureConsolidatedMetrics(oneTime == null ? ProjectOneTime.builder().build() : oneTime,
                            weeklyList.isEmpty() ? ProjectWeekly.builder().build() : weeklyList.get(0)));
            ProjectWeekly projectWeekly = weeklyList.isEmpty() ? ProjectWeekly.builder().build() : weeklyList.get(0);
            projectHealth.setOverallHealth(projectWeekly.getOverallHealth());
        });
        return proejctHealthList;
    }

    @Override
    public OverallHealthDto getProjectDetails(String projectId) {
        Project projectDetail = projectRepository.findByIdAndIsDeletedIn(projectId, Arrays.asList(null, Boolean.FALSE))
            .orElseThrow(() -> new RecordNotFoundException("Project Id is invalid"));
        OverallHealthDto overallHealthDto = convertToOverallHealth(projectDetail);
        List<ProjectMonthly> monthlyList = projectMonthlyRepository.findByProjectID(new ObjectId(projectId),
                Sort.by(DESC, MONTH));
        if (!monthlyList.isEmpty()) {
            overallHealthDto.setGraphData(convertMonthlyData(monthlyList));
        }
        List<ProjectWeekly> weeklyList = projectWeeklyRepository.findByProjectID(new ObjectId(projectId),
                PageRequest.of(0, 10, Sort.by(DESC, DATE)));
        ProjectOneTime oneTime = projectOneTimeRepository.findByProjectIDAndIsArchive(new ObjectId(projectId), false);
        overallHealthDto.setConsolidatedMetrics(
                configureConsolidatedMetrics(oneTime == null ? ProjectOneTime.builder().build() : oneTime,
                        weeklyList.isEmpty() ? ProjectWeekly.builder().build() : weeklyList.get(0)));
        overallHealthDto.setWeekly(configureWeeklyStatus(weeklyList));
        overallHealthDto.setOverallHealth(weeklyList.get(0).getOverallHealth());
        return overallHealthDto;
    }

    @Override
    public ManagerWrapperDto getStrategicDetailsOfManager(String propertyName) {
        List<ManagerDto> projectList = new ArrayList<>();
        List<Project> projectDetails = projectRepository.findByIsDeletedNullOrIsDeletedFalse();
        if (projectDetails.isEmpty()) {
            throw new RecordNotFoundException(NO_PROJECT_DATA_AVAILABLE);
        }
        try {
            for (Project projectDetail : projectDetails) {
                List<ManagerDetails> managerList = (List<ManagerDetails>) PropertyUtils.getProperty(projectDetail,
                        propertyName);
                String key;
                key = CollectionUtils.isNotEmpty(managerList) ? managerList.get(0).getName() : NA;
                String finalKey = key;
                Optional<ManagerDto> dataOptional = projectList.stream()
                    .filter(managerDto -> managerDto.getName().equalsIgnoreCase(finalKey))
                    .findAny();
                List<ProjectWeekly> weeklyList = projectWeeklyRepository
                    .findByProjectID(new ObjectId(projectDetail.getId()), PageRequest.of(0, 2, Sort.by(DESC, DATE)));
                String overAllHealth;
                if (CollectionUtils.isNotEmpty(weeklyList)) {
                    overAllHealth = projectWeeklyRepository
                        .findByProjectID(new ObjectId(projectDetail.getId()), PageRequest.of(0, 2, Sort.by(DESC, DATE)))
                        .get(0)
                        .getOverallHealth();
                }
                else {
                    overAllHealth = NA;
                }
                ManagerDto managerDto;
                if (dataOptional.isPresent()) {
                    managerDto = dataOptional.get();
                    projectList.remove(managerDto);
                    managerDto.setOnTrack(managerDto.getOnTrack() + getMatcherCount(GINITIAL, overAllHealth));
                    managerDto.setDelay(managerDto.getDelay() + getMatcherCount(AINITIAL, overAllHealth));
                    managerDto.setCritical(managerDto.getCritical() + getMatcherCount(RINITIAL, overAllHealth));
                    managerDto.setNa(managerDto.getNa() + getMatcherCount(NA, overAllHealth));
                }
                else {
                    if (StringUtils.isBlank(key)) {
                        key = NA;
                    }
                    managerDto = ManagerDto.builder()
                        .name(key)
                        .onTrack(getMatcherCount(GINITIAL, overAllHealth))
                        .delay(getMatcherCount(AINITIAL, overAllHealth))
                        .critical(getMatcherCount(RINITIAL, overAllHealth))
                        .na(getMatcherCount(NA, overAllHealth))
                        .build();
                }
                projectList.add(managerDto);
            }
        }
        catch (Exception ex) {
            log.error("Error in getStrategicDetailsOfPm : " + ex.getMessage(), ex);
        }
        return ManagerWrapperDto.builder().manager(projectList).build();
    }

    @Override
    public OverallProjectHealthWrapperDto getOverallProjectHealth() {
        List<Project> projectDetails = projectRepository.findByIsDeletedNullOrIsDeletedFalse();
        if (projectDetails.isEmpty()) {
            throw new RecordNotFoundException(NO_PROJECT_DATA_AVAILABLE);
        }
        OverallProjectHealthDto overallHealthDto = OverallProjectHealthDto.builder().amber(0).green(0).red(0).build();
        projectDetails.forEach(detail -> {
            List<ProjectWeekly> weeklyList = projectWeeklyRepository.findByProjectID(new ObjectId(detail.getId()),
                    PageRequest.of(0, 2, Sort.by(DESC, DATE)));
            if (CollectionUtils.isNotEmpty(weeklyList)) {
                if (weeklyList.get(0).getOverallHealth().equalsIgnoreCase(GINITIAL)) {
                    overallHealthDto.setGreen(overallHealthDto.getGreen() + 1);
                }
                if (weeklyList.get(0).getOverallHealth().equalsIgnoreCase(RINITIAL)) {
                    overallHealthDto.setRed(overallHealthDto.getRed() + 1);
                }
                if (weeklyList.get(0).getOverallHealth().equalsIgnoreCase(AINITIAL)) {
                    overallHealthDto.setAmber(overallHealthDto.getAmber() + 1);
                }
                overallHealthDto.setNa(0);
            }
        });
        int availableProject = overallHealthDto.getAmber() + overallHealthDto.getGreen() + overallHealthDto.getRed();
        if (availableProject < projectDetails.size()) {
            overallHealthDto.setNa(projectDetails.size() - availableProject);
        }
        return OverallProjectHealthWrapperDto.builder().overAllHealth(overallHealthDto).build();
    }

    @Override
    public WeeklyCriteriaWrapperDto getCriteriaWiseHelath() {
        List<Project> projectDetails = projectRepository.findByIsDeletedNullOrIsDeletedFalse();
        if (projectDetails.isEmpty()) {
            throw new RecordNotFoundException(NO_PROJECT_DATA_AVAILABLE);
        }
        List<WeeklyCriteriaDto> dataList = new ArrayList<>();
        projectDetails.forEach(detail -> {
            List<ProjectWeekly> weeklyList = projectWeeklyRepository.findByProjectID(new ObjectId(detail.getId()),
                    PageRequest.of(0, 2, Sort.by(DESC, DATE)));
            if (CollectionUtils.isNotEmpty(weeklyList)) {
                weeklyList.get(0).getCriteria().forEach(criteria -> {
                    WeeklyCriteriaDto criteriaDto;
                    Optional<WeeklyCriteriaDto> optionalMap = dataList.stream()
                        .filter(weeklyCriteriaDto -> weeklyCriteriaDto.getName()
                            .equalsIgnoreCase(criteria.getParameter()))
                        .findAny();
                    if (NAINITIAL.equals(criteria.getValue())) {
                        criteria.setValue(NA);
                    }
                    if (optionalMap.isPresent()) {
                        criteriaDto = optionalMap.get();
                        criteriaDto.setRed(criteriaDto.getRed() + getMatcherCount(RINITIAL, criteria.getValue()));
                        criteriaDto.setGreen(criteriaDto.getGreen() + getMatcherCount(GINITIAL, criteria.getValue()));
                        criteriaDto.setAmber(criteriaDto.getAmber() + getMatcherCount(AINITIAL, criteria.getValue()));
                        criteriaDto.setNa(criteriaDto.getNa() + getMatcherCount(NA, criteria.getValue()));
                    }
                    else {
                        criteriaDto = WeeklyCriteriaDto.builder()
                            .name(criteria.getParameter())
                            .red(getMatcherCount(RINITIAL, criteria.getValue()))
                            .green(getMatcherCount(GINITIAL, criteria.getValue()))
                            .amber(getMatcherCount(AINITIAL, criteria.getValue()))
                            .na(getMatcherCount(NA, criteria.getValue()))
                            .build();
                        dataList.add(criteriaDto);
                    }
                });
            }
            List<ProjectOneTime> projectOneTime = projectOneTimeRepository
                .findByProjectIDAndIsArchiveFalse(new ObjectId(detail.getId()));
            if (!projectOneTime.isEmpty()) {
                projectOneTime.forEach(oneTimeData -> oneTimeData.getCriteria().forEach(criteria -> {
                    WeeklyCriteriaDto criteriaDto;
                    Optional<WeeklyCriteriaDto> optionalMap = dataList.stream()
                        .filter(weeklyCriteriaDto -> weeklyCriteriaDto.getName().equals(criteria.getParameter()))
                        .findAny();
                    if (NAINITIAL.equals(criteria.getValue())) {
                        criteria.setValue(NA);
                    }
                    if (optionalMap.isPresent()) {
                        criteriaDto = optionalMap.get();
                        dataList.remove(criteriaDto);
                        criteriaDto.setRed(criteriaDto.getRed() + getMatcherCount(RINITIAL, criteria.getValue()));
                        criteriaDto.setGreen(criteriaDto.getGreen() + getMatcherCount(GINITIAL, criteria.getValue()));
                        criteriaDto.setAmber(criteriaDto.getAmber() + getMatcherCount(AINITIAL, criteria.getValue()));
                        criteriaDto.setNa(criteriaDto.getNa() + getMatcherCount(NA, criteria.getValue()));
                    }
                    else {
                        criteriaDto = WeeklyCriteriaDto.builder()
                            .name(criteria.getParameter())
                            .red(getMatcherCount(RINITIAL, criteria.getValue()))
                            .amber(getMatcherCount(AINITIAL, criteria.getValue()))
                            .green(getMatcherCount(GINITIAL, criteria.getValue()))
                            .na(getMatcherCount(NA, criteria.getValue()))
                            .build();
                    }
                    dataList.add(criteriaDto);
                }));
            }
        });
        dataList.forEach(weeklyCriteriaDto -> {
            int available = weeklyCriteriaDto.getAmber() + weeklyCriteriaDto.getGreen() + weeklyCriteriaDto.getNa()
                    + weeklyCriteriaDto.getRed();
            if (available < projectDetails.size()) {
                weeklyCriteriaDto.setNa(weeklyCriteriaDto.getNa() + (projectDetails.size() - available));
            }
        });
        return WeeklyCriteriaWrapperDto.builder().weeklyProjectStatus(dataList).build();
    }

    @Override
    public NetPromoterWrapperDto getNpmScore() {
        try {
            List<NetpromoterDto> npmList = new ArrayList<>();
            List<Project> projects = projectRepository.findByIsDeletedNullOrIsDeletedFalse();
            if (projects.isEmpty()) {
                throw new RecordNotFoundException(NO_PROJECT_DATA_AVAILABLE);
            }
            for (Project projectDetail : projects) {
                String npmScore = StringUtils.EMPTY;
                if (CollectionUtils.isNotEmpty(projectDetail.getNpsScore())) {
                    for (NpsScore npmScoreObj : projectDetail.getNpsScore()) {
                        npmScore = String.valueOf(npmScoreObj.getValue());
                        if (npmScore.equalsIgnoreCase(NAINITIAL)) {
                            npmScore = npmScore.replace(NAINITIAL, NA);
                        }
                        if (npmScore.trim().equalsIgnoreCase("Not received")) {
                            npmScore = npmScore.replace(npmScore, NA);
                        }
                        if (!npmScore.equalsIgnoreCase(NA)) {
                            npmScore = String.valueOf((int) BigDecimal.valueOf((Double.parseDouble(npmScore)))
                                .setScale(0, RoundingMode.HALF_DOWN)
                                .doubleValue());
                        }
                    }
                }
                String key = npmScore;
                if (StringUtils.isNotBlank(key)) {
                    Optional<NetpromoterDto> dataOptional = npmList.stream()
                        .filter(netpromoterDto -> netpromoterDto.getScore().equals(key))
                        .findAny();
                    NetpromoterDto npmDto;
                    if (dataOptional.isPresent()) {
                        npmDto = dataOptional.get();
                        npmDto.setNumberOfProjects(npmDto.getNumberOfProjects() + 1);
                    }
                    else {
                        npmDto = NetpromoterDto.builder().score(key).numberOfProjects(1).build();
                        npmList.add(npmDto);
                    }
                }
            }
            Optional<NetpromoterDto> netpromoterDto = npmList.stream()
                .filter(netpromoter -> netpromoter.getScore().equalsIgnoreCase(NA))
                .findFirst();
            if (netpromoterDto.isPresent()) {
                int count = npmList.stream().map(NetpromoterDto::getNumberOfProjects).reduce(0, Integer::sum);
                if (count < projects.size()) {
                    netpromoterDto.get()
                        .setNumberOfProjects(netpromoterDto.get().getNumberOfProjects() + (projects.size() - count));
                }
            }
            return NetPromoterWrapperDto.builder().npmScore(npmList).build();
        }
        catch (NumberFormatException ex) {
            throw new TypeConversionException("Type conversion error " + ex.getMessage());
        }
    }

    private Map<String, Object> configureConsolidatedMetrics(ProjectOneTime oneTime, ProjectWeekly weekly) {
        Map<String, Object> consolidatedMetrics = new HashMap<>();
        if (CollectionUtils.isEmpty(weekly.getCriteria())) {
            weekly.setCriteria(buildDummyCriteriaList());
        }
        weekly.getCriteria()
            .forEach(criteria -> consolidatedMetrics.put(criteria.getParameter(),
                    CriteriaCommentsDto.builder()
                        .health(criteria.getValue().replace(NAINITIAL, NA))
                        .comments(buildCommentsDtoList(criteria))
                        .build()));

        if (oneTime != null && oneTime.getCriteria() != null) {
            oneTime.getCriteria()
                .forEach(criteria -> consolidatedMetrics.put(criteria.getParameter(),
                        CriteriaCommentsDto.builder()
                            .health(criteria.getValue().replace(NAINITIAL, NA))
                            .comments(buildCommentsDtoList(criteria))
                            .build()));
        }
        return consolidatedMetrics;
    }

    private List<CommentsDto> buildCommentsDtoList(Criteria criteria) {
        if (CollectionUtils.isEmpty(criteria.getCommentModels())) {
            return Collections.emptyList();
        }
        return criteria.getCommentModels()
            .stream()
            .map(commentModel -> CommentsDto.builder()
                .comment(commentModel.getComment())
                .commentedBy(commentModel.getCommentedBy())
                .commentedDate(commentModel.getCommentedDate())
                .build())
            .toList();
    }

    private List<Criteria> buildDummyCriteriaList() {
        return Arrays.asList(Criteria.builder().parameter(SCOPE).value(NA).build(),
                Criteria.builder().parameter(PLANNING).value(NA).build(),
                Criteria.builder().parameter(SCHEDULE).value(NA).build(),
                Criteria.builder().parameter(PEOPLE).value(NA).build(),
                Criteria.builder().parameter(DELIVERABLES).value(NA).build(),
                Criteria.builder().parameter(FINANCIALS).value(NA).build(),
                Criteria.builder().parameter(CUSTOMER).value(NA).build(),
                Criteria.builder().parameter(METRICS_AND_COMPLIANCE).value(NA).build(),
                Criteria.builder().parameter(PROCESS).value(NA).build());
    }

    private OverallHealthDto convertToOverallHealth(Project project) {
        if (project != null) {
            try {
                return OverallHealthDto.builder()
                    .projectID(project.getId())
                    .jiraProjectID(project.getJiraProjectId())
                    .projectName(project.getName())
                    .projectCategory(project.getCategory())
                    .projectType(project.getBillingType())
                    .projectStartDate(String.valueOf(project.getStartDate()))
                    .projectEndDate(String.valueOf(project.getEndDate()))
                    .projectManager(CollectionUtils.isNotEmpty(project.getProjectManager())
                            ? project.getProjectManager().get(0).getName() : null)
                    .projectManagerEmail(CollectionUtils.isNotEmpty(project.getProjectManager())
                            ? project.getProjectManager().get(0).getEmail() : null)
                    .deliveryManager(CollectionUtils.isNotEmpty(project.getDeliveryManager())
                            ? project.getDeliveryManager().get(0).getName() : null)
                    .deliveryManagerEmail(CollectionUtils.isNotEmpty(project.getDeliveryManager())
                            ? project.getDeliveryManager().get(0).getEmail() : null)
                    .deliveryDirector(project.getDeliveryDirector())
                    .projectLogo(project.getProjectLogo())
                    .useImage(StringUtils.isNotEmpty(project.getProjectLogo()))
                    .startDate(project.getStartDate())
                    .endDate(project.getEndDate())
                    .projectNameInitials(createProjectInitials(project.getName()))
                    .actualTeamSize(CollectionUtils.isNotEmpty(project.getResources())
                            ? project.getResources().get(0).getActual() : null)
                    .pmBandwidth(CollectionUtils.isNotEmpty(project.getProjectManager())
                            ? project.getProjectManager().get(0).getBandwidth() : null)
                    .build();
            }
            catch (NumberFormatException ex) {
                throw new TypeConversionException("Type conversion error " + ex.getMessage());
            }
        }
        return OverallHealthDto.builder().build();
    }

    private String createProjectInitials(String projectName) {
        if (StringUtils.isEmpty(projectName)) {
            return StringUtils.EMPTY;
        }
        StringBuilder initials = new StringBuilder(StringUtils.EMPTY + Character.toUpperCase(projectName.charAt(0)));
        for (int i = 1; i < projectName.length() - 1; i++)
            if (projectName.charAt(i) == ' ')
                initials.append(StringUtils.SPACE + Character.toUpperCase(projectName.charAt(i + 1)));
        return initials.toString();
    }

    private Map<String, List<Map<String, Object>>> convertMonthlyData(List<ProjectMonthly> monthlyList) {
        Map<String, List<Map<String, Object>>> graphData = new HashMap<>();
        monthlyList.forEach(projectMonhtly -> {
            projectMonhtly.setMonth(convertDateToMonth(projectMonhtly.getMonthDate()));
            projectMonhtly.getMetrics().forEach(parameter -> {
                if (graphData.get(parameter.getParameter()) == null) {
                    List<Map<String, Object>> dataList = new ArrayList<>();
                    dataList.add(createMonthlyDataNode(projectMonhtly, parameter));
                    graphData.put(parameter.getParameter(), dataList);
                }
                else {
                    List<Map<String, Object>> dataList = graphData.get(parameter.getParameter());
                    dataList.add(createMonthlyDataNode(projectMonhtly, parameter));
                    graphData.put(parameter.getParameter(), dataList);
                }
            });
        });
        return graphData;
    }

    private Map<String, Object> createMonthlyDataNode(ProjectMonthly projectMonthly, Parameter parameter) {
        Map<String, Object> dataNode = new HashMap<>();
        dataNode.put(NAME, projectMonthly.getMonth());
        dataNode.put(parameter.getParameter(), Double.parseDouble(replaceNonNumericsWithZero(parameter.getValue())));
        return dataNode;
    }

    private Map<String, String[]> configureWeeklyStatus(List<ProjectWeekly> weeklyList) {
        Map<String, String[]> weeklyData = new HashMap<>();
        String[] headers = new String[weeklyList.size() + 1];
        String[] data = new String[weeklyList.size() + 1];
        headers[0] = StringUtils.EMPTY;
        data[0] = HEALTH;
        for (int i = 0; i < weeklyList.size(); i++) {
            ProjectWeekly projectWeekly = weeklyList.get(i);
            headers[i + 1] = convertDateToWeek(new Date(projectWeekly.getDate()));
            data[i + 1] = projectWeekly.getOverallHealth().replace(NAINITIAL, GINITIAL);
        }
        weeklyData.put(HEADER, headers);
        weeklyData.put(DATA, data);
        return weeklyData;
    }

    @Override
    public ProjectDataDto getProject(String id) {
        Project project = projectHelper.findAndValidateProjectByIdAndUser(id);
        ProjectDetailsWithWeeklyVo projectDetailsWithWeekly = projectDetailsDao.getProjectDetailsWithWeekly(id);
        ProjectDetailsWithOneTimeVo projectDetailsWithOneTime = projectDetailsDao.getProjectDetailsWithOneTime(id);
        if (projectDetailsWithWeekly == null) {
            throw new RecordNotFoundException(NO_RECORD_FOUND);
        }
        if (CollectionUtils.isEmpty(projectDetailsWithWeekly.getWeeklyData())) {
            projectDetailsWithWeekly.setWeeklyData(new ArrayList<>());
        }
        if (CollectionUtils.isEmpty(projectDetailsWithOneTime.getOneTimeData())) {
            projectDetailsWithOneTime.setOneTimeData(new ArrayList<>());
        }
        List<NestProjectWiseProjectMembersVo> nestProjectWiseProjectMembersVo = nestProjectsDao
            .getProjectMembers(project.getId());

        List<ProjectMembersDetailsVo> projectMembersDetailsVo = new ArrayList<>();
        nestProjectWiseProjectMembersVo.forEach(nest -> {
            if (CollectionUtils.isNotEmpty(nest.getProjectMembersDetailsVo())) {
                projectMembersDetailsVo.addAll(nest.getProjectMembersDetailsVo());
            }
        });

        Map<String, String> managersMap = new HashMap<>();
        if (!CollectionUtils.isEmpty(projectMembersDetailsVo)) {
            projectMembersDetailsVo.stream()
                .filter(member -> member.getDesignation().contains(MANAGER))
                .forEach(projectMember -> managersMap.put(projectMember.getEmail(), projectMember.getDp()));
        }
        List<ManageresVo> accountManager = new ArrayList<>();
        List<ManageresVo> projectManager = new ArrayList<>();
        List<ManageresVo> deliveryManager = new ArrayList<>();
        List<ProjectMembersDetailsVo> teamMembers = new ArrayList<>();

        if (CollectionUtils.isNotEmpty(nestProjectWiseProjectMembersVo)) {
            nestProjectWiseProjectMembersVo.forEach(nest -> {
                accountManager.addAll(nest.getAccountManagersVo());
                projectManager.addAll(nest.getProjectManagersVo());
                deliveryManager.addAll(nest.getDeliveryManagersVo());
                teamMembers.addAll(nest.getProjectMembersDetailsVo());
            });
        }
        return ProjectDataDto.builder()
            .id(projectDetailsWithWeekly.getId())
            .name(projectDetailsWithWeekly.getName())
            .billingType(projectDetailsWithWeekly.getBillingType())
            .category(projectDetailsWithWeekly.getCategory())
            .clientName(projectDetailsWithWeekly.getClientName())
            .endDate(projectDetailsWithWeekly.getEndDate().toString())
            .iconLocation(projectHelper
                .getPresignedURL(project.getFileName() != null ? project.getFileName() : StringUtils.EMPTY))
            .initials(createProjectInitials(projectDetailsWithWeekly.getName()))
            .startDate(projectDetailsWithWeekly.getStartDate().toString())
            .accountManager(CollectionUtils.isNotEmpty(nestProjectWiseProjectMembersVo)
                    ? getManagerDetail(managersMap, nestProjectWiseProjectMembersVo, ACCOUNT_MANAGER_VO) : null)
            .deliveryManager(CollectionUtils.isNotEmpty(nestProjectWiseProjectMembersVo)
                    ? getManagerDetail(managersMap, nestProjectWiseProjectMembersVo, DELIVERY_MANAGER_VO) : null)
            .projectManager(CollectionUtils.isNotEmpty(nestProjectWiseProjectMembersVo)
                    ? getManagerDetail(managersMap, nestProjectWiseProjectMembersVo, PROJECT_MANAGER_VO) : null)
            .resources(ResourcesDto.builder()
                .actual(projectDetailsWithWeekly.getResources().get(0).getActual().intValue())
                .sow(projectDetailsWithWeekly.getResources().get(0).getSow().intValue())
                .build())
            .teamMembers(CollectionUtils.isNotEmpty(teamMembers) ? teamMembers.stream()
                .map(member -> PersonDetailsDto.builder()
                    .email(member.getEmail())
                    .name(member.getName())
                    .dp(member.getDp())
                    .build())
                .toList() : new ArrayList<>())
            .healthMetrics(getHealthMetrics(
                    CollectionUtils.isEmpty(projectDetailsWithWeekly.getWeeklyData()) ? ProjectWeekly.builder().build()
                            : projectDetailsWithWeekly.getWeeklyData().get(0),
                    CollectionUtils.isEmpty(projectDetailsWithOneTime.getOneTimeData())
                            ? ProjectOneTime.builder().build() : projectDetailsWithOneTime.getOneTimeData().get(0)))
            .build();
    }

    private List<PersonDetailsDto> getManagerDetail(Map<String, String> managersMap,
            List<NestProjectWiseProjectMembersVo> nestProjectWiseProjectMembersVos, String property) {
        List<PersonDetailsDto> personDetails = new ArrayList<>();
        nestProjectWiseProjectMembersVos.forEach(nest -> {
            try {
                List<ManageresVo> managersVos = (List<ManageresVo>) PropertyUtils.getProperty(nest, property);
                if (CollectionUtils.isNotEmpty(managersVos)) {
                    managersVos.forEach(manager -> {
                        Optional<PersonDetailsDto> managerOptional = personDetails.stream()
                            .filter(person -> person.getEmail().equalsIgnoreCase(manager.getEmail()))
                            .findAny();
                        if (managerOptional.isPresent()) {
                            managerOptional.get().getNestProjects().add(nest.getName());
                        }
                        else {
                            personDetails.add(PersonDetailsDto.builder()
                                .email(manager.getEmail())
                                .name(manager.getName())
                                .dp(StringUtils.isBlank(managersMap.get(manager.getEmail())) ? null
                                        : managersMap.get(manager.getEmail()))
                                .nestProjects(new ArrayList<>(Collections.singletonList(nest.getName())))
                                .build());
                        }
                    });
                }
            }
            catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                throw new ServiceException("Something went wrong...");
            }
        });
        return personDetails;
    }

    private HealthMetricsDto getHealthMetrics(ProjectWeekly weekly, ProjectOneTime oneTime) {
        HealthMetricsDto healthMetricsDto = null;
        List<Criteria> weeklyCriteria = weekly.getCriteria();
        List<Criteria> oneTimeCriteria = oneTime.getCriteria();
        if (weeklyCriteria != null) {
            weeklyCriteria.forEach(criteria -> {
                if (criteria.getValue().equalsIgnoreCase(NAINITIAL)) {
                    criteria.setValue(NA);
                }
            });
            healthMetricsDto = HealthMetricsDto.builder()
                .scope(weeklyCriteria.get(0).getValue())
                .planning(weeklyCriteria.get(1).getValue())
                .schedule(weeklyCriteria.get(2).getValue())
                .people(weeklyCriteria.get(3).getValue())
                .deliverables(weeklyCriteria.get(4).getValue())
                .financials(weeklyCriteria.get(5).getValue())
                .customer(weeklyCriteria.get(6).getValue())
                .compliance(weeklyCriteria.get(7).getValue())
                .process(weeklyCriteria.get(8).getValue())
                .overall(weekly.getOverallHealth())
                .build();
        }
        if (oneTimeCriteria != null) {
            oneTimeCriteria.forEach(criteria -> {
                if (criteria.getValue().equalsIgnoreCase(NAINITIAL)) {
                    criteria.setValue(NA);
                }
            });
            if (Objects.isNull(healthMetricsDto)) {
                healthMetricsDto = HealthMetricsDto.builder().build();
            }
            healthMetricsDto.setStartUp(oneTimeCriteria.get(0).getValue());
            healthMetricsDto.setPeriodicReview(oneTimeCriteria.get(1).getValue());
        }
        return healthMetricsDto;
    }

    @Override
    public AllProjectsDto getAssignedProjects(String email) throws RecordNotFoundException {

        List<AllActiveProjectWiseOverAllHealthVo> allActiveProjectWiseOverAllHealth = projectDetailsDao
            .getProjectDetailsWithoverallHealth(email);
        if (CollectionUtils.isEmpty(allActiveProjectWiseOverAllHealth)) {
            throw new RecordNotFoundException(NO_RECORD_FOUND);
        }

        return AllProjectsDto.builder()
            .data(allActiveProjectWiseOverAllHealth.parallelStream().map(iscProjectDetail -> {
                Optional<JiraConfigurationWiseCurrentSprintVo> sprintByJiraConfiguration = Optional
                    .ofNullable(sprintDao.getSprintByJiraConfiguration(iscProjectDetail.getId()));
                return ProjectDto.builder()
                    .id(iscProjectDetail.getId())
                    .name(iscProjectDetail.getName())
                    .category(iscProjectDetail.getCategory())
                    .jiraProjectId(sprintByJiraConfiguration.map(JiraConfigurationWiseCurrentSprintVo::getJiraProjectId)
                        .orElse(null))
                    .iconLocation(projectHelper.getPresignedURL(iscProjectDetail.getFileName() != null
                            ? iscProjectDetail.getFileName() : StringUtils.EMPTY))
                    .initials(createProjectInitials(iscProjectDetail.getName()))
                    .overallHealth(iscProjectDetail.getOverallHealth().isEmpty() ? null
                            : iscProjectDetail.getOverallHealth().get(0).getOverallHealth())
                    .boards(sprintByJiraConfiguration.isPresent()
                            && CollectionUtils.isNotEmpty(sprintByJiraConfiguration.get().getBoards())
                                    ? sprintByJiraConfiguration.get()
                                        .getBoards()
                                        .stream()
                                        .map(board -> BoardDetailsDto.builder()
                                            .boardId(String.valueOf(board.getBoardId()))
                                            .name(board.getName())
                                            .sprintId(getSprintId(sprintByJiraConfiguration, board))
                                            .sprintName(getSprintName(sprintByJiraConfiguration, board))
                                            .build())
                                        .toList()
                                    : null)
                    .repos(buildRepositoryDataDtoList(iscProjectDetail))
                    .build();
            }).toList())
            .build();
    }

    private List<RepositoryDataDto> buildRepositoryDataDtoList(AllActiveProjectWiseOverAllHealthVo iscProjectDetail) {
        if (CollectionUtils.isNotEmpty(iscProjectDetail.getRepos())) {
            Long date = DateTimeUtils.getFirstDayOfMonth(1);
            List<ScmCodeAnalysisMonthRepoSortingVo> scmCodeAnalysisMonth = scmCodeAnalysisMonthDao
                .getScmCodeAnalysisMonthSumOfAddedRemovedLines(new ObjectId(iscProjectDetail.getId()), date);
            if (CollectionUtils.isNotEmpty(scmCodeAnalysisMonth)) {
                ScmCodeAnalysisMonthRepoSortingVo scmCodeAnalysisMonthRepoSorting = scmCodeAnalysisMonth.get(0);
                return scmCodeAnalysisMonthRepoSorting.getRepos()
                    .stream()
                    .map(repo -> RepositoryDataDto.builder()
                        .repoId(repo.getRepoId())
                        .repoName(repo.getRepoName())
                        .totalLineOfCode(CollectionUtils.isEmpty(repo.getChangedLoc()) ? 0
                                : repo.getChangedLoc().get(0).longValue())
                        .isSonarAvailable(repo.getSonar())
                        .isScmAvailable(repo.getScm())
                        .build())
                    .toList();
            }
            else {
                return null;
            }
        }
        return null;
    }

    private String getSprintId(Optional<JiraConfigurationWiseCurrentSprintVo> sprintByJiraConfiguration,
            BoardsVo board) {
        if (sprintByJiraConfiguration.isPresent()
                && CollectionUtils.isNotEmpty(sprintByJiraConfiguration.get().getCurrent_sprint())) {

            Optional<CurrentSprintVo> currentSprintVo = sprintByJiraConfiguration.get()
                .getCurrent_sprint()
                .stream()
                .filter(sprint -> sprint.getBoardId().equals(board.getBoardId()))
                .findAny();
            return currentSprintVo.map(CurrentSprintVo::getSprintId).map(String::valueOf).orElse(null);
        }
        return null;
    }

    private String getSprintName(Optional<JiraConfigurationWiseCurrentSprintVo> sprintByJiraConfiguration,
            BoardsVo board) {
        if (sprintByJiraConfiguration.isPresent()
                && CollectionUtils.isNotEmpty(sprintByJiraConfiguration.get().getCurrent_sprint())) {

            Optional<CurrentSprintVo> anyCurrentSrpingVO = sprintByJiraConfiguration.get()
                .getCurrent_sprint()
                .stream()
                .filter(sprint -> sprint.getBoardId().equals(board.getBoardId()))
                .findAny();
            return anyCurrentSrpingVO.map(CurrentSprintVo::getName).orElse(null);
        }
        return null;
    }

    @Override
    public ComplianceDto getProjectCompliance(String id) {
        Project project = projectHelper.findAndValidateProjectByIdAndUser(id);
        List<ProjectCompliance> projectCompliances = projectComplianceRepository.findByProjectId(new ObjectId(id));
        List<TicketObservationDto> tickets = new ArrayList<>();
        List<TicketObservationDto> observations = new ArrayList<>();
        projectCompliances.forEach(projectCompliance -> {
            if (MINOR_NC.equalsIgnoreCase(projectCompliance.getNonConfirmationType())
                    || MAJOR_NC.equalsIgnoreCase(projectCompliance.getNonConfirmationType())
                    || COMPLIANCE_ISSUES.equalsIgnoreCase(projectCompliance.getNonConfirmationType())
                    || COMPLIANCE_GAPS.equalsIgnoreCase(projectCompliance.getNonConfirmationType())) {
                tickets.add(buildTicketObservationResponse(projectCompliance));
            }
            else {
                observations.add(buildTicketObservationResponse(projectCompliance));
            }
        });
        return ComplianceDto.builder()
            .id(CollectionUtils.isNotEmpty(projectCompliances) ? projectCompliances.get(0).getId() : StringUtils.EMPTY)
            .nestId(CollectionUtils.isNotEmpty(projectCompliances)
                    ? String.valueOf(projectCompliances.get(0).getNestProjectId()) : StringUtils.EMPTY)
            .projectId(project.getId())
            .complianceSummary(ComplianceSummaryDto.builder()
                .isPending(CollectionUtils.isEmpty(tickets) && CollectionUtils.isEmpty(observations))
                .openTickets((int) tickets.stream()
                    .filter(ticket -> OPEN.equalsIgnoreCase(ticket.getStatus())
                            || REOPENED.equalsIgnoreCase(ticket.getStatus()))
                    .count())
                .closedTickets((int) tickets.stream()
                    .filter(ticket -> CLOSED.equalsIgnoreCase(ticket.getStatus())
                            || RESOLVED.equalsIgnoreCase(ticket.getStatus()))
                    .count())
                .openObservations((int) observations.stream()
                    .filter(observation -> !(CLOSED.equalsIgnoreCase(observation.getStatus()))
                            && !(RESOLVED.equalsIgnoreCase(observation.getStatus())))
                    .count())
                .closedObservations((int) observations.stream()
                    .filter(observation -> CLOSED.equalsIgnoreCase(observation.getStatus())
                            || RESOLVED.equalsIgnoreCase(observation.getStatus()))
                    .count())
                .build())
            .tickets(tickets)
            .observations(observations)
            .build();
    }

    private TicketObservationDto buildTicketObservationResponse(ProjectCompliance projectCompliance) {
        return TicketObservationDto.builder()
            .category(projectCompliance.getNonConfirmationType())
            .status(projectCompliance.getStatus() != null ? projectCompliance.getStatus().getName() : null)
            .number(projectCompliance.getNumber())
            .type(projectCompliance.getIssueType() != null ? projectCompliance.getIssueType().getName() : null)
            .url(String.join(FORWARD_SLASH, JIRA_BASE_URL, projectCompliance.getNumber()))
            .ncRaisedDate(projectCompliance.getNcRaisedDate())
            .ncDueDate(projectCompliance.getNcDueDate())
            .id(projectCompliance.getIssueId())
            .name(projectCompliance.getSummary())
            .build();
    }

    private List<RepositoryDataDto> buildRepositoryDataDtoListForClient(AllActiveClientProjectVo iscProjectDetail) {
        if (CollectionUtils.isNotEmpty(iscProjectDetail.getRepos())) {
            Long date = DateTimeUtils.getFirstDayOfMonth(1);
            List<ScmCodeAnalysisMonthRepoSortingVo> scmCodeAnalysisMonth = scmCodeAnalysisMonthDao
                .getScmCodeAnalysisMonthSumOfAddedRemovedLines(new ObjectId(iscProjectDetail.getId()), date);
            if (CollectionUtils.isNotEmpty(scmCodeAnalysisMonth)) {
                ScmCodeAnalysisMonthRepoSortingVo scmCodeAnalysisMonthRepoSorting = scmCodeAnalysisMonth.get(0);
                return scmCodeAnalysisMonthRepoSorting.getRepos()
                    .stream()
                    .map(repo -> RepositoryDataDto.builder()
                        .repoId(repo.getRepoId())
                        .repoName(repo.getRepoName())
                        .totalLineOfCode(CollectionUtils.isEmpty(repo.getChangedLoc()) ? 0
                                : repo.getChangedLoc().get(0).longValue())
                        .isSonarAvailable(repo.getSonar())
                        .isScmAvailable(repo.getScm())
                        .build())
                    .toList();
            }
            else {
                return null;
            }
        }
        return null;
    }

    @Override
    public AllClientProjectDto getClientProjects(String email) throws RecordNotFoundException {
        List<AllActiveClientProjectVo> allActiveClientProjectVos = projectDetailsDao.getClientProjectDetails(email);
        if (CollectionUtils.isEmpty(allActiveClientProjectVos)) {
            throw new RecordNotFoundException(NO_RECORD_FOUND);
        }

        return AllClientProjectDto.builder().data(allActiveClientProjectVos.parallelStream().map(iscProjectDetail -> {
            Optional<JiraConfigurationWiseCurrentSprintVo> sprintByJiraConfiguration = Optional
                .ofNullable(sprintDao.getSprintByJiraConfiguration(iscProjectDetail.getId()));
            return ClientProjectDto.builder()
                .id(iscProjectDetail.getId())
                .name(iscProjectDetail.getName())
                .category(iscProjectDetail.getCategory())
                .jiraProjectId(sprintByJiraConfiguration.map(JiraConfigurationWiseCurrentSprintVo::getJiraProjectId)
                    .orElse(null))
                .iconLocation(projectHelper.getPresignedURL(
                        iscProjectDetail.getFileName() != null ? iscProjectDetail.getFileName() : StringUtils.EMPTY))
                .initials(createProjectInitials(iscProjectDetail.getName()))
                .boards(sprintByJiraConfiguration.isPresent()
                        && CollectionUtils.isNotEmpty(sprintByJiraConfiguration.get().getBoards())
                                ? sprintByJiraConfiguration.get()
                                    .getBoards()
                                    .stream()
                                    .map(board -> BoardDetailsDto.builder()
                                        .boardId(String.valueOf(board.getBoardId()))
                                        .name(board.getName())
                                        .sprintId(getSprintId(sprintByJiraConfiguration, board))
                                        .sprintName(getSprintName(sprintByJiraConfiguration, board))
                                        .build())
                                    .toList()
                                : null)
                .repos(buildRepositoryDataDtoListForClient(iscProjectDetail))
                .build();
        }).toList()).build();
    }

}
