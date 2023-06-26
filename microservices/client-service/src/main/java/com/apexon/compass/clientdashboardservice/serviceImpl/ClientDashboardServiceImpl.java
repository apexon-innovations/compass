package com.apexon.compass.clientdashboardservice.serviceImpl;

import static com.apexon.compass.clientdashboardservice.utils.ClientDashboardServiceUtils.distinctByKey;
import static com.apexon.compass.clientdashboardservice.utils.DateTimeUtils.*;
import static com.apexon.compass.constants.ClientDashboardServiceConstants.*;
import static com.apexon.compass.constants.QueryConstants.COMMA_PUNCTUATION;
import static com.apexon.compass.constants.QueryConstants.RECORD_NOT_FOUND;
import static com.apexon.compass.utilities.DateTimeUtils.*;

import com.apexon.compass.aggregation.vo.*;
import com.apexon.compass.clientdashboardservice.dao.ClientDao;
import com.apexon.compass.clientdashboardservice.dao.ProjectDetailsDao;
import com.apexon.compass.clientdashboardservice.dto.*;
import com.apexon.compass.clientdashboardservice.repository.DefectAgeingTrendsRepository;
import com.apexon.compass.clientdashboardservice.repository.DefectTrendsRepository;
import com.apexon.compass.clientdashboardservice.service.ClientDashboardService;
import com.apexon.compass.clientdashboardservice.serviceImpl.helper.ProjectHelper;
import com.apexon.compass.clientdashboardservice.utils.DateTimeUtils;
import com.apexon.compass.entities.*;
import com.apexon.compass.exception.custom.BadRequestException;
import com.apexon.compass.exception.custom.ForbiddenException;
import com.apexon.compass.exception.custom.RecordNotFoundException;
import com.apexon.compass.exception.custom.ServiceException;
import com.apexon.compass.utilities.ArithmeticUtils;
import java.text.ParseException;
import java.time.DayOfWeek;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor(onConstructor = @__({ @Autowired }))
@Slf4j
public class ClientDashboardServiceImpl implements ClientDashboardService {

    public static final String ZERO = "0";

    private ProjectHelper projectHelper;

    private ClientDao clientDao;

    private ProjectDetailsDao projectDetailsDao;

    private DefectAgeingTrendsRepository defectAgeingTrendsRepository;

    private DefectTrendsRepository defectTrendsRepository;

    @Override
    public ProjectIdAndSprintDtos getVelocityTrends(String projectIds, Integer sprintCount, String emailAddress) {
        List<ProjectIdAndSprintDto> projectIdAndSprints;
        if (projectIds.equalsIgnoreCase(ALL)) {
            List<ProjectsWithJiraConfigurationVo> iscProjectsWithJiraConfigurations = projectDetailsDao
                .findAllProjectsOfUser(emailAddress);
            if (CollectionUtils.isEmpty(iscProjectsWithJiraConfigurations)) {
                log.info("No Record is available for this user");
                throw new ForbiddenException("Data is unavailable for this user");
            }
            ArrayList<String> projectIdsList = new ArrayList<>();
            for (ProjectsWithJiraConfigurationVo iscProjectsWithJiraConfiguration : iscProjectsWithJiraConfigurations) {
                projectIdsList.add(iscProjectsWithJiraConfiguration.getId());
            }
            projectIdAndSprints = getVelocityTrends(projectIdsList, sprintCount);
            return ProjectIdAndSprintDtos.builder().data(projectIdAndSprints).build();
        }
        projectIdAndSprints = getVelocityTrends(List.of(projectIds.split(COMMA_PUNCTUATION)), sprintCount);
        return ProjectIdAndSprintDtos.builder().data(projectIdAndSprints).build();
    }

    private List<ProjectIdAndSprintDto> getVelocityTrends(List<String> projectIds, Integer sprintCount) {
        List<ProjectIdAndSprintDto> projectIdAndSprints = new ArrayList<>();
        ProjectIdAndSprintDto projectIdAndSprint;
        List<VelocityTrendsVo> velocityTrends = clientDao.getVelocityTrends(projectIds, sprintCount);
        if (CollectionUtils.isEmpty(velocityTrends)) {
            log.info("Record not found for this project Id");
            throw new RecordNotFoundException("Record not found for this project Id");
        }
        velocityTrends.stream().map(velocity -> {
            List<VelocityTrendsSprintDataVo> data = velocity.getData();
            Collections.reverse(data);
            velocity.setData(data);
            return velocity;
        }).toList();
        List<SprintsDto> sprints = new ArrayList<>();
        for (VelocityTrendsVo velocityTrendsTrend : velocityTrends) {
            sprints = velocityTrendsTrend.getData()
                .stream()
                .map(velocity -> SprintsDto.builder()
                    .sprintId(velocity.getSprintDetails().getSprintId())
                    .committedPoints(String.valueOf(velocity.getData().get(0).getPlannedPoints()))
                    .completedPoints(String.valueOf(velocity.getData().get(0).getCompletedPoints()))
                    .committedCount(String.valueOf(velocity.getData().get(0).getPlannedCounts()))
                    .completedCount(String.valueOf(velocity.getData().get(0).getCompletedCounts()))
                    .build())
                .toList();
            Project project = projectHelper
                .findAndValidateProjectByIdAndUser(velocityTrendsTrend.get_id().getProjectId().toString());
            projectIdAndSprint = ProjectIdAndSprintDto.builder()
                .projectId(velocityTrendsTrend.get_id().getProjectId().toString())
                .projectName(project.getName())
                .jiraProjectId(velocityTrendsTrend.get_id().getJiraProjectId())
                .sprints(sprints)
                .build();
            projectIdAndSprints.add(projectIdAndSprint);
        }
        projectIdAndSprints.add(getSprintsAvgCalculation(projectIdAndSprints, sprints));
        return projectIdAndSprints;
    }

    private ProjectIdAndSprintDto getSprintsAvgCalculation(List<ProjectIdAndSprintDto> projectIdAndSprint,
            List<SprintsDto> sprints) {
        int divider = projectIdAndSprint.size();
        List<SprintsDto> sprintsAvgCalculated = sprints.stream().map(sprint -> {
            double committedCount = projectIdAndSprint.stream()
                .mapToDouble(project -> Double
                    .parseDouble(project.getSprints().get(sprints.indexOf(sprint)).getCommittedCount()))
                .sum();
            double completedCount = projectIdAndSprint.stream()
                .mapToDouble(project -> Double
                    .parseDouble(project.getSprints().get(sprints.indexOf(sprint)).getCompletedCount()))
                .sum();
            double committedPoints = projectIdAndSprint.stream()
                .mapToDouble(project -> Double
                    .parseDouble(project.getSprints().get(sprints.indexOf(sprint)).getCommittedPoints()))
                .sum();
            double completedPoints = projectIdAndSprint.stream()
                .mapToDouble(project -> Double
                    .parseDouble(project.getSprints().get(sprints.indexOf(sprint)).getCompletedPoints()))
                .sum();
            return SprintsDto.builder()
                .committedCount(String.valueOf(committedCount))
                .completedCount(String.valueOf(completedCount))
                .committedPoints(String.valueOf(committedPoints))
                .completedPoints(String.valueOf(completedPoints))
                .build();
        }).map(sprintToUpdate -> {
            sprintToUpdate
                .setCommittedCount(String.valueOf(Double.parseDouble(sprintToUpdate.getCommittedCount()) / divider));
            sprintToUpdate
                .setCompletedCount(String.valueOf(Double.parseDouble(sprintToUpdate.getCompletedCount()) / divider));
            sprintToUpdate
                .setCommittedPoints(String.valueOf(Double.parseDouble(sprintToUpdate.getCommittedPoints()) / divider));
            sprintToUpdate
                .setCompletedPoints(String.valueOf(Double.parseDouble(sprintToUpdate.getCompletedPoints()) / divider));
            return sprintToUpdate;
        }).toList();
        return ProjectIdAndSprintDto.builder()
            .projectId(ZERO)
            .projectName(AVERAGE)
            .sprints(sprintsAvgCalculated)
            .build();
    }

    @Override
    public StoryPointsTrendsDto getStoryPointsDeliveryTrendForProject(String emailAddress, String projectId,
            String sprintNumbers) {
        List<ProjectsWithJiraConfigurationVo> projects;
        projects = projectDetailsDao.findAllProjectsOfUserWithProjectAndEmail(projectId,
                projectHelper.getUserNameFromToken());
        if (CollectionUtils.isEmpty(projects)) {
            log.info(NO_PROJECTS_AVAILABLE_FOR_USER_FOR_THIS_ISC_PROJECT_ID);
            throw new ForbiddenException(NO_RECORD_AVAILABLE);
        }
        ProjectsWithJiraConfigurationVo project = projects.get(0);
        List<String> sprintIds = Arrays.stream(sprintNumbers.split(COMMA_PUNCTUATION)).toList();
        List<SprintDayWiseDataDto> sprintsData = sprintIds.stream().map(id -> {
            Sprints currentSprint = getSprint(project.getSprints(), Integer.parseInt(id));
            if (Objects.nonNull(currentSprint)) {
                List<StoryPointsDeliveryVo> storyPoints = clientDao.getStoryPointsDeliveryTrendForProject(projectId,
                        currentSprint.getSprintId());
                if (CollectionUtils.isNotEmpty(storyPoints)) {
                    return buildSprintDataDto(currentSprint, storyPoints);
                }
            }
            return null;
        }).filter(Objects::nonNull).toList();
        if (CollectionUtils.isEmpty(sprintsData)) {
            log.info("Empty list sprintsData");
            throw new RecordNotFoundException(RECORD_NOT_FOUND);
        }
        return StoryPointsTrendsDto.builder()
            .projectId(projectId)
            .projectName(Objects.nonNull(project.getProject()) ? project.getProject().getName() : project.getName())
            .sprintData(sprintsData)
            .build();
    }

    private SprintDayWiseDataDto buildSprintDataDto(Sprints currentSprint, List<StoryPointsDeliveryVo> storyPoints) {
        AtomicInteger atomicInteger = new AtomicInteger(1);
        return SprintDayWiseDataDto.builder()
            .name(currentSprint.getName())
            .sprintId(currentSprint.getSprintId())
            .dayWiseData(storyPoints.stream()
                .map(story -> DayWiseDataDto.builder()
                    .openTillNowCount(story.getOpenTillNowCounts())
                    .completedCount(story.getCompletedCounts())
                    .reopenCount(story.getReopenCounts())
                    .newlyAddedCount(story.getNewlyAddedCounts())
                    .completedPoints(story.getCompletedPoints())
                    .openTillNowPoints(story.getOpenTillNowPoints())
                    .newlyAddedPoints(story.getNewlyAddedPoints())
                    .reopenPoints(story.getReopenPoints())
                    .day(atomicInteger.getAndIncrement())
                    .build())
                .toList())
            .build();
    }

    @Override
    public ProjectIdAndSprintDataDtos getConsolidatedDefectRatio(String projectIds, String emailAddress,
            String sprintNumbers) {
        SprintDataDto sprintData;
        List<ProjectIdAndSprintDataDto> data = new ArrayList<>();
        List<String> distinctIscProjectIds;
        if (projectIds.equalsIgnoreCase(ALL)) {
            List<ProjectsWithJiraConfigurationVo> iscProjectsWithJiraConfigurations = projectDetailsDao
                .findAllProjectsOfUser(emailAddress);
            if (CollectionUtils.isEmpty(iscProjectsWithJiraConfigurations)) {
                log.info("Record is not available for this user");
                throw new ForbiddenException("No Data is available for this user");
            }
            distinctIscProjectIds = iscProjectsWithJiraConfigurations.stream()
                .map(ProjectsWithJiraConfigurationVo::getId)
                .toList();
        }
        else {
            distinctIscProjectIds = new ArrayList<>(Arrays.asList(projectIds.split(COMMA_PUNCTUATION)));
        }

        int sprintCount = Integer.parseInt(sprintNumbers);
        List<ConsolidatedDefectRatioVo> consolidatedDefectRatio = clientDao
            .getConsolidatedDefectRatio(distinctIscProjectIds, sprintCount);
        if (CollectionUtils.isEmpty(consolidatedDefectRatio)) {
            throw new RecordNotFoundException(DATA_IS_UNAVAILABLE);
        }
        int initial = 0;
        for (String iscProjectId : distinctIscProjectIds) {
            // TODO : Need to cache below query/find workaround
            Project project = projectHelper.findAndValidateProjectByIdAndUser(iscProjectId);
            if (consolidatedDefectRatio.size() < initial || consolidatedDefectRatio.size() < sprintCount) {
                throw new RecordNotFoundException(DATA_IS_UNAVAILABLE);
            }
            sprintData = getConsolidatedSprintData(consolidatedDefectRatio.subList(initial, sprintCount));
            data.add(ProjectIdAndSprintDataDto.builder()
                .jiraProjectId(consolidatedDefectRatio.get(initial).get_id().getJiraProjectId())
                .projectId(iscProjectId)
                .projectName(project.getName())
                .sprintData(sprintData)
                .build());
            initial = sprintCount;
            sprintCount += sprintCount;
        }
        return ProjectIdAndSprintDataDtos.builder().data(data).build();
    }

    private SprintDataDto getConsolidatedSprintData(List<ConsolidatedDefectRatioVo> consolidatedDefectRatio) {
        double delivered = 0.0;
        double opened = 0.0;
        for (ConsolidatedDefectRatioVo defectRatio : consolidatedDefectRatio) {
            delivered += defectRatio.getData().get(0).getDelivered();
            opened += defectRatio.getData().get(0).getOpened();
        }
        return SprintDataDto.builder()
            .deliveredStories((int) delivered)
            .defectOpened((int) opened)
            .ratio(ratioCalculation(opened, delivered))
            .build();
    }

    private Double ratioCalculation(Double defectOpened, Double deliveredStories) {
        if (deliveredStories == 0) {
            if (defectOpened == 0) {
                return 0.0;
            }
            else {
                return 100.00;
            }
        }
        else {
            return Double.parseDouble(DECIMAL_FORMATTER.format(((defectOpened * 100) / deliveredStories)));
        }
    }

    @Override
    public ProjectStoryPointDtos getProjectStoryPointDefectRatio(String projectId, String sprintCount) {
        double sumOfAllDefectOpen;
        double sumOfAllDeliveredStories;
        Project project = projectHelper.findAndValidateProjectByIdAndUser(projectId);
        List<StoryPointDefectRatioVo> storyPointDefectRatio = clientDao.getStoryPointDefectRatio(projectId,
                Integer.parseInt(sprintCount));
        if (storyPointDefectRatio.isEmpty()) {
            throw new RecordNotFoundException(DATA_IS_UNAVAILABLE);
        }
        sumOfAllDefectOpen = storyPointDefectRatio.stream()
            .mapToDouble(ratio -> ratio.getData().get(0).getOpened())
            .sum();
        sumOfAllDeliveredStories = storyPointDefectRatio.stream()
            .mapToDouble(ratio -> ratio.getData().get(0).getDelivered())
            .sum();
        return ProjectStoryPointDtos.builder()
            .data(ProjectStoryPointDto.builder()
                .averageRatio(Double.parseDouble(DECIMAL_FORMATTER.format(
                        (sumOfAllDefectOpen * 100) / (sumOfAllDeliveredStories == 0 ? 1 : sumOfAllDeliveredStories))))
                .projectName(project.getName())
                .totalStoryPoints((int) sumOfAllDeliveredStories)
                .projectId(projectId)
                .jiraProjectId(storyPointDefectRatio.get(0).get_id().getJiraProjectId())
                .sprintData((storyPointDefectRatio.stream()
                    .map(storyDefectRatio -> SprintDataDto.builder()
                        .sprintId(storyDefectRatio.getSprintId())
                        .name(storyDefectRatio.getName())
                        .deliveredStories(storyDefectRatio.getData().get(0).getDelivered().intValue())
                        .defectOpened(storyDefectRatio.getData().get(0).getOpened().intValue())
                        .ratio(ratioCalculation(storyDefectRatio.getData().get(0).getOpened(),
                                storyDefectRatio.getData().get(0).getDelivered()))
                        .build())
                    .toList()))
                .build())
            .build();
    }

    @Override
    public ProductCompletionTrendsDto getProductCompletionTrend(String email, String projectId, Integer sprintCount) {
        List<String> projectIds;
        List<ProjectsWithJiraConfigurationVo> projects;
        if (ALL.equalsIgnoreCase(projectId)) {
            projects = projectDetailsDao.findAllProjectsOfLoggedInUser(projectHelper.getUserNameFromToken());
            if (CollectionUtils.isEmpty(projects)) {
                log.info("No projects available for this user");
                throw new ForbiddenException("No record available for this user");
            }
            projectIds = projects.stream().map(ProjectsWithJiraConfigurationVo::getId).toList();
        }
        else {
            projectIds = Arrays.stream(projectId.split(COMMA_PUNCTUATION)).toList();
            projects = projectDetailsDao.findAllProjectsOfUserBasedOnProjectId(
                    projectIds.stream().map(ObjectId::new).toList(), projectHelper.getUserNameFromToken());
            if (CollectionUtils.isEmpty(projects)) {
                log.info("No projects available for the user for this iscProjectId");
                throw new ForbiddenException(NO_RECORD_AVAILABLE);
            }
        }
        sprintCount = Objects.isNull(sprintCount) ? 5 : sprintCount;
        List<ProductCompletionTrendsVo> productCompletionTrends = clientDao.getProductCompletionTrends(projectIds,
                sprintCount);
        if (CollectionUtils.isEmpty(productCompletionTrends) || productCompletionTrends.stream()
            .allMatch(product -> CollectionUtils.isEmpty(product.getSprintData()))) {
            log.error("No data available for {}", projectIds);
            throw new RecordNotFoundException(RECORD_NOT_FOUND);
        }
        return ProductCompletionTrendsDto.builder()
            .data(buildProductCompletionTrendDto(productCompletionTrends, projects))
            .build();
    }

    private List<ProductCompletionTrendDto> buildProductCompletionTrendDto(
            List<ProductCompletionTrendsVo> productCompletionTrends, List<ProjectsWithJiraConfigurationVo> projects) {
        return productCompletionTrends.stream().map(product -> {
            if (CollectionUtils.isEmpty(product.getSprintData())) {
                return null;
            }
            ProjectsWithJiraConfigurationVo iscProject = projects.stream()
                .filter(project -> project.getJiraId().equals(product.get_id().getProjectId()))
                .findFirst()
                .orElse(null);
            List<ProductCompletionSprintDetails> sprintsDetail = product.getSprintData()
                .stream()
                .map(sprint -> ProductCompletionSprintDetails.builder()
                    .id(sprint.getSprintId())
                    .name(sprint.getName())
                    .completedCount(sprint.getCompletedCounts())
                    .completedPoints(sprint.getCompletedPoints())
                    .squads(Objects.isNull(sprint.getTotalSquads()) ? null : sprint.getTotalSquads().intValue())
                    .build())
                .toList();
            return ProductCompletionTrendDto.builder()
                .name(Objects.nonNull(iscProject) && Objects.nonNull(iscProject.getName()) ? iscProject.getName()
                        : iscProject.getProject().getName())
                .id(Objects.nonNull(iscProject.getProject()) ? iscProject.getProject().getId() : iscProject.getId())
                .jiraProjectId(product.get_id().getJiraProjectId())
                .projectId(product.get_id().getProjectId())
                .sprints(sprintsDetail)
                .build();
        }).toList();
    }

    public StoryPointsDeliversDto getStoryPointsDelivered(String email, String projectId, Integer sprintId) {
        List<String> projectIds;
        List<ProjectsWithJiraConfigurationVo> projects;
        boolean isAll = false;
        if (ALL.equalsIgnoreCase(projectId)) {
            projects = projectDetailsDao.findAllProjectsOfUser(projectHelper.getUserNameFromToken());
            isAll = true;
            if (CollectionUtils.isEmpty(projects)) {
                log.info(NO_PROJECTS_AVAILABLE_UNDER_USER);
                throw new ForbiddenException(NO_RECORD_AVAILABLE);
            }
        }
        else {
            projectIds = Arrays.stream(projectId.split(COMMA_PUNCTUATION)).toList();
            projects = projectDetailsDao.findAllProjectsOfUserWithProjects(
                    projectIds.stream().map(ObjectId::new).toList(), projectHelper.getUserNameFromToken());
            if (CollectionUtils.isEmpty(projects)) {
                log.info("No projects available for user for this jiraProjectId");
                throw new ForbiddenException(NO_RECORD_AVAILABLE);
            }
        }
        List<String> projectIdList = new ArrayList<>();
        HashMap<String, Integer> iscIdSprintId = new HashMap<>();
        HashMap<String, String> iscIdProjectName = new HashMap<>();
        setIscIdProjectName(sprintId, projects, isAll, projectIdList, iscIdSprintId, iscIdProjectName);
        List<StoryPointsVo> storyPoints = clientDao.getStoryPointsDelivered(projectIdList, iscIdSprintId);

        List<ProjectDetailsDto> data = storyPoints.stream()
            .map(storyPoint -> buildProjectDetails(storyPoint, iscIdProjectName))
            .toList();

        boolean allNonEmptyOrNull = data.stream()
            .filter(Objects::nonNull)
            .allMatch(dayWiseData -> CollectionUtils.isEmpty(dayWiseData.getDayWiseData()));
        if (CollectionUtils.isEmpty(data) || allNonEmptyOrNull) {
            log.info("data[] is empty");
            throw new RecordNotFoundException(NO_RECORD_AVAILABLE);
        }
        return StoryPointsDeliversDto.builder().data(data).build();
    }

    private void setIscIdProjectName(Integer sprintId, List<ProjectsWithJiraConfigurationVo> iscProjects, boolean isAll,
            List<String> projectIdList, HashMap<String, Integer> iscIdSprintId,
            HashMap<String, String> iscIdProjectName) {
        for (ProjectsWithJiraConfigurationVo project : iscProjects) {
            Sprints currentSprint = getSprint(project.getSprints(), sprintId);
            if (isAll) {
                iscIdProjectName.put(project.getId(), project.getName());
                projectIdList.add(project.getId());
                iscIdSprintId.put(project.getId(),
                        Objects.nonNull(currentSprint.getSprintId()) ? currentSprint.getSprintId() : null);
            }
            else {
                iscIdProjectName.put(project.getProject().getId(), project.getProject().getName());
                projectIdList.add(project.getProject().getId());
                iscIdSprintId.put(project.getProject().getId(),
                        Objects.nonNull(currentSprint.getSprintId()) ? currentSprint.getSprintId() : null);
            }
        }
    }

    private ProjectDetailsDto buildProjectDetails(StoryPointsVo storyPoints, HashMap<String, String> iscIdProjectName) {
        AtomicInteger atomicInteger = new AtomicInteger(1);
        LocalDate startDates = Instant.ofEpochMilli(storyPoints.getStartDate())
            .atZone(ZoneOffset.UTC)
            .minusDays(1)
            .toLocalDate();
        LocalDate endDates = Instant.ofEpochMilli(storyPoints.getEndDate()).atZone(ZoneOffset.UTC).toLocalDate();
        long workingDays = getWorkingDays(startDates, endDates);
        List<DayWiseDataDto> dayWiseData = new ArrayList<>();
        double perDayIdealCompletedPoints = ((double) storyPoints.getTotalEfforts() / workingDays);
        double idealCompletedPoints = storyPoints.getTotalEfforts();
        double perDayIdealCompletedCount = ((double) storyPoints.getTotalTasks() / workingDays);
        double idealCompletedCount = storyPoints.getTotalTasks();
        DayOfWeek dayOfWeekFromLongDate;
        for (StatusDataVo story : storyPoints.getStatusData()) {
            dayOfWeekFromLongDate = getDayOfWeekFromLongDate(story.getDate());
            if (dayOfWeekFromLongDate.getValue() != 6 && dayOfWeekFromLongDate.getValue() != 7) {
                idealCompletedCount -= perDayIdealCompletedCount;
                idealCompletedPoints -= perDayIdealCompletedPoints;
            }
            dayWiseData.add(DayWiseDataDto.builder()
                .reopenCount(story.getReopenCounts())
                .newlyAddedCount(story.getNewlyAddedCounts())
                .day(atomicInteger.getAndIncrement())
                .completedCount(story.getCompletedCounts())
                .openTillNowCount(story.getOpenTillNowCounts())
                .reopenPoints(story.getReopenPoints())
                .newlyAddedPoints(story.getNewlyAddedPoints())
                .openTillNowPoints(story.getOpenTillNowPoints())
                .completedPoints(story.getCompletedPoints())
                .idealCompletedCount(Math.max((int) Math.round(idealCompletedCount), 0))
                .idealCompletedPoints(Math.max((int) (Math.round(idealCompletedPoints)), 0))
                .build());
        }
        return ProjectDetailsDto.builder()
            .dayWiseData(dayWiseData)
            .name(storyPoints.getName())
            .sprintId(storyPoints.getSprintId())
            .jiraProjectId(storyPoints.getJiraProjectId())
            .projectId(storyPoints.getProjectId())
            .projectName(iscIdProjectName.get(storyPoints.get_id().getProjectId()))
            .build();
    }

    private Sprints getSprint(List<Sprints> sprints, int sprintNumber) {
        List<Sprints> sortedSprints = sprints.stream()
            .filter(sprint -> Objects.nonNull(sprint.getStartDate()))
            .filter(distinctByKey(Sprints::getSprintId))
            .sorted(Comparator.comparingLong(Sprints::getStartDate).reversed())
            .limit(5)
            .toList();
        return (sortedSprints.size() >= sprintNumber + 1) ? sortedSprints.get(sprintNumber) : null;
    }

    @Override
    public DefectSnapshotDto getConsolidatedDefectSnapshot(String emailAddress, String projectId) {
        List<String> projectIds;
        List<ProjectsWithJiraConfigurationVo> iscProjects;
        if (ALL.equalsIgnoreCase(projectId)) {
            iscProjects = projectDetailsDao.findAllProjectsOfLoggedInUser(projectHelper.getUserNameFromToken());
            if (CollectionUtils.isEmpty(iscProjects)) {
                log.info(NO_PROJECTS_AVAILABLE_UNDER_USER);
                throw new ForbiddenException(NO_RECORD_AVAILABLE);
            }
            projectIds = iscProjects.stream().map(ProjectsWithJiraConfigurationVo::getId).toList();
        }
        else {
            projectIds = Arrays.stream(projectId.split(COMMA_PUNCTUATION)).toList();
            iscProjects = projectDetailsDao.findAllProjectsOfUserBasedOnProjectId(
                    projectIds.stream().map(ObjectId::new).toList(), projectHelper.getUserNameFromToken());
            if (CollectionUtils.isEmpty(iscProjects)) {
                log.info(NO_PROJECTS_AVAILABLE_FOR_USER_FOR_THIS_ISC_PROJECT_ID);
                throw new ForbiddenException(NO_RECORD_AVAILABLE);
            }
        }

        List<ConsolidatedDefectSanpshotVo> consolidatedDefectSnapshot = clientDao
            .getConsolidatedDefectSnapshot(projectIds);
        if (CollectionUtils.isEmpty(consolidatedDefectSnapshot)) {
            log.info(DATA_IS_UNAVAILABLE);
            throw new RecordNotFoundException("No Data is available for this JiraIds");
        }
        return DefectSnapshotDto.builder()
            .data(buildDefectSnapshotProjectWise(consolidatedDefectSnapshot, iscProjects))
            .build();
    }

    @Override
    public ProductHealthOverviewDto getProductHealthOverview(String emailAddress, String projectId) {
        List<ProjectsWithJiraConfigurationVo> iscProjects;
        List<String> projectIds;
        if (ALL.equalsIgnoreCase(projectId)) {
            iscProjects = projectDetailsDao.findAllProjectsOfLoggedInUser(projectHelper.getUserNameFromToken());
            if (CollectionUtils.isEmpty(iscProjects)) {
                log.info(NO_PROJECTS_AVAILABLE_UNDER_USER);
                throw new ForbiddenException(NO_RECORD_AVAILABLE);
            }
            projectIds = iscProjects.stream().map(ProjectsWithJiraConfigurationVo::getId).toList();
        }
        else {
            projectIds = Arrays.stream(projectId.split(COMMA_PUNCTUATION)).toList();
            iscProjects = projectDetailsDao.findAllProjectsOfUserBasedOnProjectId(
                    projectIds.stream().map(ObjectId::new).toList(), projectHelper.getUserNameFromToken());
            if (CollectionUtils.isEmpty(iscProjects)) {
                log.info(NO_PROJECTS_AVAILABLE_FOR_USER_FOR_THIS_ISC_PROJECT_ID);
                throw new ForbiddenException(NO_RECORD_AVAILABLE);
            }
        }

        List<ProductHealthOverviewVo> productHealthOverview = clientDao.getProductHealthOverview(projectIds);
        if (CollectionUtils.isEmpty(productHealthOverview)) {
            log.info(DATA_IS_UNAVAILABLE);
            throw new RecordNotFoundException("Data is unavailable for this project Ids");
        }

        return ProductHealthOverviewDto.builder()
            .data(buildProductHealthOverview(productHealthOverview, iscProjects))
            .build();
    }

    @Override
    public CodeScoreDataDto getCodeScore(String emailAddress, String projectId, String repoId, Integer monthCount) {
        projectHelper.findAndValidateProjectByIdAndUser(projectId);
        Long startDate = DateTimeUtils.getFirstDayOfMonth(monthCount);
        Long endDate = DateTimeUtils.getLastDayOfMonth(Instant.ofEpochMilli(startDate)
            .atZone(ZoneOffset.UTC)
            .toLocalDate()
            .plusMonths(monthCount - NumberUtils.LONG_ONE));
        boolean isAllRepo = Objects.isNull(repoId);
        List<CodeScoreVo> codeScores = clientDao.getCodeScore(projectId, isAllRepo, repoId, startDate, endDate);
        if (CollectionUtils.isEmpty(codeScores)) {
            log.info("No Record Found for project id : {} ", projectId);
            throw new RecordNotFoundException(NO_RECORD_AVAILABLE);
        }
        return CodeScoreDataDto.builder().data(buildCodeScoreDto(codeScores)).build();
    }

    private List<CodeScoreDto> buildCodeScoreDto(List<CodeScoreVo> codeScores) {
        if (codeScores.stream().allMatch(metric -> CollectionUtils.isEmpty(metric.getMonthData()))) {
            log.info(NO_MONTHLY_DATA_AVAILABLE_FOR_PROJECT_ID);
            throw new RecordNotFoundException(RECORD_NOT_FOUND);
        }
        return codeScores.stream().map(codeScore -> {
            if (CollectionUtils.isNotEmpty(codeScore.getMonthData())) {
                return CodeScoreDto.builder()
                    .repoId(codeScore.getRepoId())
                    .repoName(codeScore.getRepoName())
                    .monthData(codeScore.getMonthData().stream().map(month -> {
                        try {
                            return MonthDataDto.builder()
                                .security(month.getSecurity())
                                .robustness(month.getRobustness())
                                .efficiency(month.getEfficiency())
                                .month(DateTimeUtils.formatMonth(month.getMonth()))
                                .build();
                        }
                        catch (ParseException parseException) {
                            throw new ServiceException(
                                    SOMETHING_WENT_WRONG_WHILE_PARSING_THE_DATE + parseException.getMessage());
                        }
                    }).toList())
                    .build();
            }
            return null;
        }).toList();
    }

    @Override
    public CodeMetricsDataDto getCodeMetrics(String emailAddress, String projectId, String repoId, int monthCount) {
        projectHelper.findAndValidateProjectByIdAndUser(projectId);
        Long startDate = DateTimeUtils.getFirstDayOfMonth(monthCount);
        Long endDate = DateTimeUtils.getLastDayOfMonth(Instant.ofEpochMilli(startDate)
            .atZone(ZoneOffset.UTC)
            .toLocalDate()
            .plusMonths(monthCount - NumberUtils.LONG_ONE));
        boolean isAllRepo = Objects.isNull(repoId);
        List<ClientCodeMetricsVo> clientCodeMetrics = clientDao.getCodeMetrics(projectId, isAllRepo, repoId, startDate,
                endDate);
        if (CollectionUtils.isEmpty(clientCodeMetrics)) {
            log.info("No record found for projectId : {} ", projectId);
            throw new RecordNotFoundException(NO_RECORD_AVAILABLE);
        }
        return CodeMetricsDataDto.builder().data(buildCodeMetricsDto(clientCodeMetrics)).build();
    }

    private List<CodeMetricsDto> buildCodeMetricsDto(List<ClientCodeMetricsVo> clientCodeMetrics) {
        if (clientCodeMetrics.stream().allMatch(metric -> CollectionUtils.isEmpty(metric.getMonthData()))) {
            log.info(NO_MONTHLY_DATA_AVAILABLE_FOR_PROJECT_ID);
            throw new RecordNotFoundException(RECORD_NOT_FOUND);
        }
        return clientCodeMetrics.stream().map(metric -> {
            if (CollectionUtils.isNotEmpty(metric.getMonthData())) {
                return CodeMetricsDto.builder()
                    .codeChurn(metric.getCodeChurn())
                    .legacyRefactor(metric.getLegacyRefactor())
                    .repoId(metric.getRepoId())
                    .repoName(metric.getRepoName())
                    .monthData(metric.getMonthData().stream().map(month -> {
                        try {
                            return CodeMetricsMonthlyDataDto.builder()
                                .addedLineOfCode(month.getAddedLineOfCode())
                                .month(DateTimeUtils.formatMonth(month.getMonth()))
                                .removedLineOfCode(month.getRemovedLineOfCode())
                                .build();
                        }
                        catch (ParseException e) {
                            throw new ServiceException(SOMETHING_WENT_WRONG_WHILE_PARSING_THE_DATE);
                        }
                    }).toList())
                    .build();
            }
            return null;
        }).toList();
    }

    private List<ProductHealthOverviewDetailsDto> buildProductHealthOverview(
            List<ProductHealthOverviewVo> productHealthOverviews, List<ProjectsWithJiraConfigurationVo> iscProjects) {
        return productHealthOverviews.stream().map(productHealthOverview -> {
            Optional<ProjectsWithJiraConfigurationVo> iscProject = iscProjects.stream()
                .filter(project -> project.getJiraId().equals(productHealthOverview.getJiraProjectId()))
                .findFirst();
            return ProductHealthOverviewDetailsDto.builder()
                .projectId(productHealthOverview.getJiraProjectId())
                .projectName(iscProject.isPresent() && Objects.nonNull(iscProject.get().getName())
                        ? iscProject.get().getName() : iscProject.get().getProject().getName())
                .totalStories(productHealthOverview.getData().getStories().getTotalStories().intValue())
                .completedStories(productHealthOverview.getData().getStories().getCompletedStories().intValue())
                .pendingStories(productHealthOverview.getData().getStories().getInProgressStories().intValue())
                .backlogStories(productHealthOverview.getData().getStories().getBacklogStories().intValue())
                .totalDefects(productHealthOverview.getData().getDefects().getTotalDefects().intValue())
                .resolvedDefects(productHealthOverview.getData().getDefects().getResolvedDefects().intValue())
                .pendingDefects(productHealthOverview.getData().getDefects().getInProgressDefects().intValue())
                .backlogDefects(productHealthOverview.getData().getDefects().getBacklogDefects().intValue())
                .build();
        }).toList();
    }

    private List<DefectSnapshotProjectwiseDto> buildDefectSnapshotProjectWise(
            List<ConsolidatedDefectSanpshotVo> consolidatedDefectSnapshots,
            List<ProjectsWithJiraConfigurationVo> iscProjects) {
        return consolidatedDefectSnapshots.stream().map(consolidatedDefectSnapshot -> {
            Optional<ProjectsWithJiraConfigurationVo> iscProject = iscProjects.stream()
                .filter(project -> project.getJiraId().equals(consolidatedDefectSnapshot.getProjectId()))
                .findFirst();
            return DefectSnapshotProjectwiseDto.builder()
                .projectId(consolidatedDefectSnapshot.getJiraProjectId())
                .projectName(iscProject.isPresent() && Objects.nonNull(iscProject.get().getName())
                        ? iscProject.get().getName() : iscProject.get().getProject().getName())
                .backlogOpen(PlannedBacklogOpenDefectsDto.builder()
                    .blocker(consolidatedDefectSnapshot.getData().getBacklogOpen().getBlocker().intValue())
                    .critical(consolidatedDefectSnapshot.getData().getBacklogOpen().getCritical().intValue())
                    .major(consolidatedDefectSnapshot.getData().getBacklogOpen().getMajor().intValue())
                    .minor(consolidatedDefectSnapshot.getData().getBacklogOpen().getMinor().intValue())
                    .total(consolidatedDefectSnapshot.getData().getBacklogOpen().getTotalOpened().intValue())
                    .build())
                .plannedOpen(PlannedBacklogOpenDefectsDto.builder()
                    .blocker(consolidatedDefectSnapshot.getData().getPlannedOpen().getBlocker().intValue())
                    .critical(consolidatedDefectSnapshot.getData().getPlannedOpen().getCritical().intValue())
                    .major(consolidatedDefectSnapshot.getData().getPlannedOpen().getMajor().intValue())
                    .minor(consolidatedDefectSnapshot.getData().getPlannedOpen().getMinor().intValue())
                    .total(consolidatedDefectSnapshot.getData().getPlannedOpen().getTotalOpened().intValue())
                    .build())
                .totalDefects(consolidatedDefectSnapshot.getTotalDefects().intValue())
                .build();
        }).toList();
    }

    @Override
    public CodeViolationsDataDto getCodeViolations(String emailAddress, String projectId, String repoId,
            int monthCount) {
        projectHelper.findAndValidateProjectByIdAndUser(projectId);
        Long startDate = DateTimeUtils.getFirstDayOfMonth(monthCount);
        Long endDate = DateTimeUtils.getLastDayOfMonth(Instant.ofEpochMilli(startDate)
            .atZone(ZoneOffset.UTC)
            .toLocalDate()
            .plusMonths(monthCount - NumberUtils.LONG_ONE));
        List<CodeViolationsVo> codeViolations = clientDao.getCodeViolations(projectId, Objects.isNull(repoId), repoId,
                startDate, endDate);
        if (CollectionUtils.isEmpty(codeViolations)) {
            throw new RecordNotFoundException(NO_RECORD_AVAILABLE);
        }
        List<CodeViolationsDto> data = codeViolations.stream()
            .filter(violation -> CollectionUtils.isNotEmpty(violation.getMonthData()))
            .map(violation -> CodeViolationsDto.builder()
                .repoName(violation.getRepoName())
                .repoId(violation.getRepoId())
                .monthData(buildMonthlyDataList(violation.getMonthData()))
                .build())
            .toList();
        if (CollectionUtils.isEmpty(data)) {
            throw new RecordNotFoundException(NO_RECORD_AVAILABLE);
        }
        return CodeViolationsDataDto.builder().data(data).build();
    }

    private List<CodeViolationsMonthlyDataDto> buildMonthlyDataList(List<CodeViolationsMonthlyDataVo> monthlyData) {
        AtomicInteger previousCriticalViolations = new AtomicInteger(0);
        return monthlyData.stream().filter(month -> Objects.nonNull(month.getViolations())).map(month -> {
            int criticalViolations = month.getViolations().getCritical();
            int added = Math.max(criticalViolations - previousCriticalViolations.get(), 0);
            int removed = Math.max(previousCriticalViolations.get() - criticalViolations, 0);
            previousCriticalViolations.set(criticalViolations);
            String formattedMonth;
            try {
                formattedMonth = formatMonth(month.getMonth());
            }
            catch (ParseException e) {
                throw new ServiceException("Something went wrong while parsing date");
            }
            ViolationsDto violations = ViolationsDto.builder()
                .total(month.getViolations().getTotal())
                .criticalAdded(added)
                .criticalRemoved(removed)
                .build();
            return CodeViolationsMonthlyDataDto.builder().month(formattedMonth).violations(violations).build();
        }).toList();
    }

    @Override
    public CodeHealthDataDto getCodeHealthSnapShot(String emailAddress) {
        List<Project> projects = projectDetailsDao.findAllProjectsOfClient(emailAddress);
        if (CollectionUtils.isEmpty(projects)) {
            log.error("Could not found associated projects for :: {}", emailAddress);
            throw new RecordNotFoundException(RECORD_NOT_FOUND);
        }
        List<ObjectId> iscProjectIds = projects.stream().map(project -> new ObjectId(project.getId())).toList();
        List<CodeSnapshotVo> codeSnapshots = clientDao.getCodeHealthSnapshot(iscProjectIds, getFirstDayOfMonth(1));
        if (CollectionUtils.isEmpty(codeSnapshots)
                || codeSnapshots.stream().allMatch(snapShot -> CollectionUtils.isEmpty(snapShot.getData()))) {
            log.error("No snapshot record found for :: {}", iscProjectIds);
            throw new RecordNotFoundException(RECORD_NOT_FOUND);
        }
        return CodeHealthDataDto.builder()
            .data(codeSnapshots.stream()
                .map(codeSnapshot -> CodeHealthDto.builder()
                    .id(codeSnapshot.get_id())
                    .name(codeSnapshot.getProjectName())
                    .repos(buildRepoDetailsDtoList(codeSnapshot.getData()))
                    .build())
                .toList())
            .build();
    }

    private List<RepoDetailsDto> buildRepoDetailsDtoList(List<CodeSnapshotDataVo> data) {
        List<RepoDetailsDto> repoDetailsList = new ArrayList<>();
        List<CodeSnapshotDataVo> tempList = new ArrayList<>();
        long aggregatedTotalLineOfCode = data.stream()
            .filter(repo -> Objects.nonNull(repo.getTotalLineOfCode()))
            .mapToLong(CodeSnapshotDataVo::getTotalLineOfCode)
            .sum();
        long threshHoldValue = (70 * aggregatedTotalLineOfCode) / 100;
        long parallelAggregationOfTotalLoc = 0;
        boolean isOtherRepo = false;
        for (CodeSnapshotDataVo snapshotData : data) {
            RepoDetailsDto repoDetails = RepoDetailsDto.builder()
                .repoId(snapshotData.getRepoId())
                .repoName(snapshotData.getRepoName())
                .build();
            setViolationsAndTechnicalDebt(repoDetails, snapshotData);
            if (CollectionUtils.isNotEmpty(snapshotData.getScmData())) {
                if (parallelAggregationOfTotalLoc < threshHoldValue) {
                    setScmData(repoDetails, snapshotData);
                }
                else {
                    tempList.add(snapshotData);
                    isOtherRepo = true;
                }
                parallelAggregationOfTotalLoc += (Objects.isNull(snapshotData.getTotalLineOfCode()) ? 0
                        : snapshotData.getTotalLineOfCode());
            }
            if (!isOtherRepo) {
                repoDetailsList.add(repoDetails);
            }
        }
        if (CollectionUtils.isNotEmpty(tempList)) {
            int totalLineOfCodeOfOtherRepos = tempList.stream()
                .filter(temp -> Objects.nonNull(temp.getTotalLineOfCode()))
                .mapToInt(CodeSnapshotDataVo::getTotalLineOfCode)
                .sum();
            int totalViolationsOfOtherRepos = tempList.stream()
                .filter(temp -> CollectionUtils.isNotEmpty(temp.getViolationsAndTechnicalDebt()))
                .filter(temp -> Objects.nonNull(temp.getViolationsAndTechnicalDebt().get(0).getTotalViolation()))
                .mapToInt(temp -> temp.getViolationsAndTechnicalDebt().get(0).getTotalViolation())
                .sum();
            int codeChurnOfOtherRepos = tempList.stream()
                .filter(temp -> CollectionUtils.isNotEmpty(temp.getScmData()))
                .filter(temp -> Objects.nonNull(temp.getScmData().get(0).getCodeChurn()))
                .mapToInt(temp -> temp.getScmData().get(0).getCodeChurn())
                .sum();
            int legacyRefactorOfOtherRepos = tempList.stream()
                .filter(temp -> CollectionUtils.isNotEmpty(temp.getScmData()))
                .filter(temp -> Objects.nonNull(temp.getScmData().get(0).getLegacyRefactor()))
                .mapToInt(temp -> temp.getScmData().get(0).getLegacyRefactor())
                .sum();
            repoDetailsList.add(RepoDetailsDto.builder()
                .repoName("Other " + tempList.size() + " repos")
                .totalLineOfCode(totalLineOfCodeOfOtherRepos)
                .totalViolations(totalViolationsOfOtherRepos)
                .codeChurn(codeChurnOfOtherRepos)
                .legacyRefactor(legacyRefactorOfOtherRepos)
                .build());
        }
        return repoDetailsList;
    }

    private void setScmData(RepoDetailsDto repoDetails, CodeSnapshotDataVo tempObj) {
        repoDetails.setCodeChurn(Objects.isNull(tempObj.getScmData().get(0).getCodeChurn()) ? null
                : tempObj.getScmData().get(0).getCodeChurn());
        repoDetails.setLegacyRefactor(Objects.isNull(tempObj.getScmData().get(0).getLegacyRefactor()) ? null
                : tempObj.getScmData().get(0).getLegacyRefactor());
        repoDetails
            .setTotalLineOfCode(Objects.isNull(tempObj.getTotalLineOfCode()) ? null : tempObj.getTotalLineOfCode());
    }

    private void setViolationsAndTechnicalDebt(RepoDetailsDto repoDetails, CodeSnapshotDataVo tempObj) {
        if (CollectionUtils.isNotEmpty(tempObj.getViolationsAndTechnicalDebt())) {
            repoDetails
                .setTotalViolations(Objects.isNull(tempObj.getViolationsAndTechnicalDebt().get(0).getTotalViolation())
                        ? null : tempObj.getViolationsAndTechnicalDebt().get(0).getTotalViolation());
            repoDetails.setCompliance(Objects.isNull(tempObj.getViolationsAndTechnicalDebt().get(0).getCompliance())
                    ? null : ArithmeticUtils.round(tempObj.getViolationsAndTechnicalDebt().get(0).getCompliance(), 2));
            repoDetails
                .setTechnicalDebt(Objects.isNull(tempObj.getViolationsAndTechnicalDebt().get(0).getTechnicalDebt())
                        ? null : convertMinutesToDays(
                                tempObj.getViolationsAndTechnicalDebt().get(0).getTechnicalDebt().longValue()));
        }
    }

    @Override
    public TechDebtDto getTechnicalDebt(String emailAddress, String projectId, String repoId, Integer monthCount) {
        projectHelper.findAndValidateProjectByIdAndUser(projectId);
        Long startDate = DateTimeUtils.getFirstDayOfMonth(monthCount);
        Long endDate = DateTimeUtils.getLastDayOfMonth(Instant.ofEpochMilli(startDate)
            .atZone(ZoneOffset.UTC)
            .toLocalDate()
            .plusMonths(monthCount - NumberUtils.LONG_ONE));
        boolean isAllRepo = Objects.isNull(repoId);
        List<CodeTechDebtVo> techDebt = clientDao.getTechnicalDebt(projectId, isAllRepo, repoId, startDate, endDate);
        if (CollectionUtils.isEmpty(techDebt)) {
            log.info("No Record Found for project id : {} ", projectId);
            throw new RecordNotFoundException(NO_RECORD_AVAILABLE);
        }
        return TechDebtDto.builder().data(buildTechDebtRepoDataDtoList(techDebt)).build();
    }

    private List<TechDebtRepoDataDto> buildTechDebtRepoDataDtoList(List<CodeTechDebtVo> techDebt) {
        if (techDebt.stream().allMatch(metric -> CollectionUtils.isEmpty(metric.getMonthData()))) {
            log.info(NO_MONTHLY_DATA_AVAILABLE_FOR_PROJECT_ID);
            throw new RecordNotFoundException(RECORD_NOT_FOUND);
        }
        return techDebt.stream().map(repo -> {
            if (CollectionUtils.isNotEmpty(repo.getMonthData())) {
                return TechDebtRepoDataDto.builder()
                    .repoId(repo.getRepoId())
                    .repoName(repo.getRepoName())
                    .monthData(repo.getMonthData().stream().map(month -> {
                        try {
                            return TechDebtMonthlyDataDto.builder()
                                .complexity(month.getComplexity().doubleValue())
                                .coverage(month.getCoverage())
                                .duplication(ArithmeticUtils.round(month.getDuplication(), 2))
                                .issues(month.getIssues().doubleValue())
                                .maintainability(convertToDays(month.getMaintainability().doubleValue()))
                                .month(formatMonth(month.getMonth()))
                                .reliability(convertToDays(month.getReliability().doubleValue()))
                                .security(convertToDays(month.getSecurity()))
                                .size(month.getSize().doubleValue())
                                .totalDays(convertToDays(month.getTotal()))
                                .build();
                        }
                        catch (ParseException e) {
                            throw new ServiceException(SOMETHING_WENT_WRONG_WHILE_PARSING_THE_DATE + e.getMessage());
                        }
                    }).toList())
                    .build();
            }
            return null;
        }).toList();
    }

    @Override
    public DefectAgeingTrendsDataDto getDefectAgeingTrends(String emailAddress, String projectIds) {
        List<ObjectId> ids;
        if (ALL.equalsIgnoreCase(projectIds)) {
            ids = getIdsForAll(emailAddress);
        }
        else {
            ids = Arrays.stream(projectIds.split(COMMA_PUNCTUATION)).map(ObjectId::new).toList();
        }
        List<Project> projects = projectDetailsDao.findAssociatedUsersWithProjects(ids,
                projectHelper.getUserNameFromToken());
        if (CollectionUtils.isEmpty(projects)) {
            throw new ForbiddenException("Access denied");
        }
        List<DefectAgeingTrends> defectAgeingTrends = defectAgeingTrendsRepository
            .findByProjectIdIn(projects.stream().map(project -> new ObjectId(project.getId())).toList());
        if (CollectionUtils.isEmpty(defectAgeingTrends)) {
            log.info(NO_RECORD_AVAILABLE_FOR_PROJECTS, projectIds);
            throw new RecordNotFoundException(RECORD_NOT_FOUND);
        }
        return DefectAgeingTrendsDataDto.builder().data(defectAgeingTrends.stream().map(trend -> {
            if (CollectionUtils.isNotEmpty(trend.getData())) {
                return DefectAgeingTrendsDto.builder()
                    .id(trend.getId())
                    .name(trend.getProjectName())
                    .jiraProjectId(trend.getJiraProjectId())
                    .range(trend.getData()
                        .stream()
                        .map(data -> DefectAgeingRangeDto.builder()
                            .value(createRange(data.getMin(), data.getMax()))
                            .count(data.getCount())
                            .build())
                        .toList())
                    .build();
            }
            return null;
        }).toList()).build();
    }

    @Override
    public DefectTrendsTypeDataDto getDefectTrendsDefectType(String emailAddress, String projectIds) {
        List<ObjectId> ids;
        Long lastDateOfPreviousMonth = getLastDayOfMonthAtStart(
                LocalDate.now(ZoneOffset.UTC).minusMonths(NumberUtils.LONG_ONE));
        if (projectIds.equals(ALL)) {
            ids = getIdsForAll(emailAddress);
        }
        else {
            ids = Arrays.stream(projectIds.split(",")).map(ObjectId::new).toList();
        }
        List<DefectTrendsType> defectTrendsType = ids.stream().map(id -> {
            projectHelper.findAndValidateProjectByIdAndUser(id.toString());
            return clientDao.getDefectTrendsType(lastDateOfPreviousMonth, id);
        }).toList();
        if (CollectionUtils.isEmpty(defectTrendsType)) {
            log.info(NO_RECORD_AVAILABLE_FOR_PROJECTS, projectIds);
            throw new RecordNotFoundException(RECORD_NOT_FOUND);
        }
        return DefectTrendsTypeDataDto.builder().data(buildDefectTrendsType(defectTrendsType)).build();
    }

    private List<ObjectId> getIdsForAll(String emailAddress) {
        List<ProjectsWithJiraConfigurationVo> iscProjectsWithJiraConfigurations = projectDetailsDao
            .findAllProjectsOfUser(emailAddress);
        if (CollectionUtils.isEmpty(iscProjectsWithJiraConfigurations)) {
            log.info("Record is not available for this user");
            throw new ForbiddenException("No Data is available for this user");
        }
        return iscProjectsWithJiraConfigurations.stream().map(jiraConfig -> new ObjectId(jiraConfig.getId())).toList();
    }

    @Override
    public DefectTrendsSeverityDataDto getDefectTrendsDefectSeverity(String emailAddress, String projectIds) {
        Long lastDateOfPreviousMonth = getLastDayOfMonthAtStart(
                LocalDate.now(ZoneOffset.UTC).minusMonths(NumberUtils.LONG_ONE));
        List<ObjectId> ids = Arrays.stream(projectIds.split(",")).map(ObjectId::new).toList();
        ids.forEach(id -> projectHelper.findAndValidateProjectByIdAndUser(id.toString()));
        List<DefectTrendsType> defectTrendsSeverity = defectTrendsRepository.findByProjectIdInAndDateIs(ids,
                lastDateOfPreviousMonth);
        if (CollectionUtils.isEmpty(defectTrendsSeverity)) {
            log.info(NO_RECORD_AVAILABLE_FOR_PROJECTS, projectIds);
            throw new RecordNotFoundException(RECORD_NOT_FOUND);
        }
        List<DefectTrendsSeverityDto> data = buildDefectTrendsSeverity(defectTrendsSeverity);
        return DefectTrendsSeverityDataDto.builder().data(data).build();
    }

    @Override
    public DefectTrendsAcceptanceDataDto getDefectTrendsDefectAcceptance(String emailAddress, String projectIds) {
        Long lastDateOfPreviousMonth = getLastDayOfMonthAtStart(
                LocalDate.now(ZoneOffset.UTC).minusMonths(NumberUtils.LONG_ONE));
        log.info("Date is " + lastDateOfPreviousMonth);
        List<ObjectId> ids = Arrays.stream(projectIds.split(",")).map(ObjectId::new).toList();
        ids.forEach(id -> projectHelper.findAndValidateProjectByIdAndUser(id.toString()));
        List<DefectTrendsType> defectTrendsAcceptance = defectTrendsRepository.findByProjectIdInAndDateIs(ids,
                lastDateOfPreviousMonth);
        if (CollectionUtils.isEmpty(defectTrendsAcceptance)) {
            log.info(NO_RECORD_AVAILABLE_FOR_PROJECTS, projectIds);
            throw new RecordNotFoundException(RECORD_NOT_FOUND);
        }

        return DefectTrendsAcceptanceDataDto.builder()
            .data(buildDefectTrendsAcceptance(defectTrendsAcceptance))
            .build();
    }

    @Override
    public DefectTrendAcceptanceStatsDataDto getDefectTrendDefectAcceptanceStats(String emailAddress, String projectId,
            Integer month) {
        projectHelper.findAndValidateProjectByIdAndUser(projectId);
        Long date;
        Integer monthCount = month;
        List<DefectTrendsType> defectTrendAcceptance = new ArrayList<>();
        DefectTrendsType defectTrendType;
        for (int count = 0; count < month; count++) {
            date = getLastDayOfMonthAtStart(LocalDate.now(ZoneOffset.UTC).minusMonths(monthCount));
            defectTrendType = clientDao.getDefectTrendsType(date, new ObjectId(projectId));
            if (defectTrendType != null) {
                defectTrendAcceptance.add(defectTrendType);
            }
            --monthCount;
        }
        if (CollectionUtils.isEmpty(defectTrendAcceptance)) {
            throw new RecordNotFoundException("Data is not available");
        }
        return DefectTrendAcceptanceStatsDataDto.builder()
            .projectId(defectTrendAcceptance.get(0).getProjectId().toString())
            .jiraProjectId(defectTrendAcceptance.get(0).getJiraProjectId())
            .projectName(defectTrendAcceptance.get(0).getProjectName())
            .monthData(buildDefectTrendAcceptanceStatsMonthDataDto(defectTrendAcceptance))
            .build();
    }

    private List<DefectTrendAcceptanceStatsMonthDataDto> buildDefectTrendAcceptanceStatsMonthDataDto(
            List<DefectTrendsType> defectTrendAcceptance) {
        return defectTrendAcceptance.stream().map(defectTrendType -> {
            Integer accepted = defectTrendType.getOpenBug() + defectTrendType.getCloseBug()
                    + defectTrendType.getReopenBug();
            int actedUpon = accepted + defectTrendType.getRejectedBug();
            Double ratio = Double.parseDouble(DECIMAL_FORMATTER.format((accepted.doubleValue() / actedUpon) * 100));
            return DefectTrendAcceptanceStatsMonthDataDto.builder()
                .month(getMonthAndYearFromLongDate(defectTrendType.getDate()))
                .accepted(accepted)
                .actedUpon(actedUpon)
                .ratio(ratio)
                .build();
        }).toList();
    }

    private List<DefectTrendsAcceptanceDto> buildDefectTrendsAcceptance(List<DefectTrendsType> defectTrendsAcceptance) {
        return defectTrendsAcceptance.stream().map(defectTrendsType -> {
            Integer accepted = defectTrendsType.getOpenBug() + defectTrendsType.getCloseBug()
                    + defectTrendsType.getReopenBug();
            int actedUpon = accepted + defectTrendsType.getRejectedBug();
            Double ratio = Double.parseDouble(DECIMAL_FORMATTER.format((accepted.doubleValue() / actedUpon) * 100));
            return DefectTrendsAcceptanceDto.builder()
                .id(defectTrendsType.getProjectId().toString())
                .jiraProjectId(defectTrendsType.getJiraProjectId())
                .projectName(defectTrendsType.getProjectName())
                .actedUpon(actedUpon)
                .accepted(accepted)
                .ratio(ratio)
                .build();
        }).toList();
    }

    private List<DefectTrendsSeverityDto> buildDefectTrendsSeverity(List<DefectTrendsType> defectTrendsSeverity) {
        return defectTrendsSeverity.stream().map(severity -> {
            Integer total = severity.getCloseBug() + severity.getUnattendedBug() + severity.getOpenBug();
            return DefectTrendsSeverityDto.builder()
                .id(severity.getProjectId().toString())
                .jiraProjectId(severity.getJiraProjectId())
                .name(severity.getProjectName())
                .total(total)
                .planned(PlannedDefectSeverityDto.builder()
                    .totalPlanned(severity.getOpenBug())
                    .openBlocker(severity.getOpenBlocker())
                    .openCritical(severity.getOpenCritical())
                    .openMajor(severity.getOpenMajor())
                    .openMinor(severity.getOpenMinor())
                    .build())
                .closed(ClosedDefectSeverityDto.builder()
                    .totalClosed(severity.getCloseBug())
                    .closeMajor(severity.getCloseMajor())
                    .closeMinor(severity.getCloseMinor())
                    .closeBlocker(severity.getCloseBlocker())
                    .closeCritical(severity.getCloseCritical())
                    .build())
                .backlog(BacklogDefectSeverityDto.builder()
                    .totalBacklog(severity.getUnattendedBug())
                    .unattendedBlocker(severity.getUnattendedBlocker())
                    .unattendedCritical(severity.getUnattendedCritical())
                    .unattendedMajor(severity.getUnattendedMajor())
                    .unattendedMinor(severity.getUnattendedMinor())
                    .build())
                .build();
        }).toList();
    }

    @Override
    public DefectTrendsTypeStatsDataDto getDefectTrendsDefectTypeStats(String emailAddress, String projectId,
            Integer monthCount) {
        projectHelper.findAndValidateProjectByIdAndUser(projectId);
        return DefectTrendsTypeStatsDataDto.builder()
            .data(calculateDefectTrendsStats(monthCount, new ObjectId(projectId)))
            .build();
    }

    @Override
    public DefectTrendSeverityStatsDataDto getDefectTrendDefectSeverityStats(String emailAddress, String projectId,
            Integer month) {
        List<DefectTrendsType> defectTrendsTypes = new ArrayList<>();
        projectHelper.findAndValidateProjectByIdAndUser(projectId);
        Integer monthCount = month;
        DefectTrendsType defectTrendsType;
        Long date;
        for (int count = 0; count < month; count++) {
            date = getLastDayOfMonthAtStart(LocalDate.now(ZoneOffset.UTC).minusMonths(monthCount));
            defectTrendsType = clientDao.getDefectTrendsType(date, new ObjectId(projectId));
            if (defectTrendsType != null) {
                defectTrendsTypes.add(defectTrendsType);
            }
            --monthCount;
        }
        if (CollectionUtils.isEmpty(defectTrendsTypes)) {
            throw new RecordNotFoundException("Data is not available");
        }
        return DefectTrendSeverityStatsDataDto.builder()
            .data(buildDefectTrendSeverityStatsDto(defectTrendsTypes))
            .build();
    }

    private DefectTrendSeverityStatsDto buildDefectTrendSeverityStatsDto(List<DefectTrendsType> defectTrendsTypes) {
        List<DefectTrendSeverityStatsMonthDto> monthData = defectTrendsTypes.stream()
            .map(defectTrendsType -> DefectTrendSeverityStatsMonthDto.builder()
                .month(getMonthAndYearFromLongDate(defectTrendsType.getDate()))
                .planned(PlannedSeverityDto.builder()
                    .major(defectTrendsType.getOpenMajor())
                    .minor(defectTrendsType.getOpenMinor())
                    .critical(defectTrendsType.getOpenCritical())
                    .blocker(defectTrendsType.getOpenBlocker())
                    .totalPlanned(defectTrendsType.getOpenMajor() + defectTrendsType.getOpenMinor()
                            + defectTrendsType.getOpenCritical() + defectTrendsType.getOpenBlocker())
                    .build())
                .backlog(BacklogSeverityDto.builder()
                    .blocker(defectTrendsType.getUnattendedBlocker())
                    .major(defectTrendsType.getUnattendedMajor())
                    .minor(defectTrendsType.getUnattendedMinor())
                    .critical(defectTrendsType.getUnattendedCritical())
                    .build())
                .build())
            .toList();
        return DefectTrendSeverityStatsDto.builder()
            .id(defectTrendsTypes.get(0).getProjectId().toString())
            .jiraProjectId(defectTrendsTypes.get(0).getJiraProjectId())
            .name(defectTrendsTypes.get(0).getProjectName())
            .monthData(monthData)
            .build();
    }

    private DefectTrendsTypeStatsDto calculateDefectTrendsStats(Integer month, ObjectId projectId) {
        Integer monthCount = month;
        Long date;
        List<DefectTrendsStatsMonthDataDto> defectTrendsStatsMonthData = new ArrayList<>();
        DefectTrendsType defectTrendsTypePreviousMonthData;
        DefectTrendsType defectTrendsType = null;
        int raisedPreviousMonth;
        int raised;
        for (int count = 0; count < month; count++) {
            if (Objects.nonNull(defectTrendsType)) {
                defectTrendsTypePreviousMonthData = defectTrendsType;
            }
            else {
                date = getLastDayOfMonthAtStart(LocalDate.now(ZoneOffset.UTC).minusMonths(monthCount + 1L));
                defectTrendsTypePreviousMonthData = defectTrendsRepository.findByProjectIdAndDate(projectId, date);
                if (defectTrendsTypePreviousMonthData == null) {
                    throw new RecordNotFoundException("No Data Available");
                }
            }
            date = getLastDayOfMonthAtStart(LocalDate.now(ZoneOffset.UTC).minusMonths(monthCount));
            defectTrendsType = defectTrendsRepository.findByProjectIdAndDate(projectId, date);
            if (defectTrendsType == null) {
                throw new RecordNotFoundException("No Data Available");
            }

            raised = defectTrendsType.getOpenBug() + defectTrendsType.getReopenBug() + defectTrendsType.getCloseBug();
            raisedPreviousMonth = defectTrendsTypePreviousMonthData.getOpenBug()
                    + defectTrendsTypePreviousMonthData.getReopenBug()
                    + defectTrendsTypePreviousMonthData.getCloseBug();
            defectTrendsStatsMonthData.add(DefectTrendsStatsMonthDataDto.builder()
                .month(getMonthAndYearFromLongDate(defectTrendsType.getDate()))
                .closedDefects(defectTrendsType.getCloseBug() - defectTrendsTypePreviousMonthData.getCloseBug())
                .openDefects(defectTrendsType.getOpenBug() + defectTrendsType.getReopenBug())
                .raisedDefects(raised - raisedPreviousMonth)
                .build());
            --monthCount;
        }
        return DefectTrendsTypeStatsDto.builder()
            .id(String.valueOf(defectTrendsType.getProjectId()))
            .name(defectTrendsType.getProjectName())
            .jiraProjectId(defectTrendsType.getJiraProjectId())
            .monthData(defectTrendsStatsMonthData)
            .build();
    }

    private List<DefectTrendsTypeDto> buildDefectTrendsType(List<DefectTrendsType> defectTrendsTypeList) {

        return defectTrendsTypeList.stream().map(defectTrendsType -> {
            int acceptedDefects = defectTrendsType.getOpenBug() + defectTrendsType.getReopenBug()
                    + defectTrendsType.getCloseBug();
            int totalDefect = acceptedDefects + defectTrendsType.getUnattendedBug() + defectTrendsType.getRejectedBug();
            DefectTrendsType defectTrendsTypePreviousMonth = clientDao.getDefectTrendsType(
                    getLastDayOfMonthAtStart(LocalDate.now(ZoneOffset.UTC).minusMonths(TWO)),
                    defectTrendsType.getProjectId());
            return DefectTrendsTypeDto.builder()
                .id(defectTrendsType.getId())
                .name(defectTrendsType.getProjectName())
                .jiraProjectId(defectTrendsType.getJiraProjectId())
                .unattendedDefects(defectTrendsType.getUnattendedBug())
                .unattendedDefectsStatus(calculateDefectsStatus(defectTrendsType.getUnattendedBug(),
                        defectTrendsTypePreviousMonth.getUnattendedBug()))
                .rejectedDefects(defectTrendsType.getRejectedBug())
                .rejectedDefectsStatus(calculateDefectsStatus(defectTrendsType.getRejectedBug(),
                        defectTrendsTypePreviousMonth.getRejectedBug()))
                .acceptedDefects(acceptedDefects)
                .acceptedDefectsStatus(calculateDefectsStatus(acceptedDefects,
                        defectTrendsTypePreviousMonth.getOpenBug() + defectTrendsTypePreviousMonth.getReopenBug()
                                + defectTrendsTypePreviousMonth.getCloseBug()))
                .totalDefects(totalDefect)
                .openDefects(defectTrendsType.getOpenBug())
                .closedDefects(defectTrendsType.getCloseBug())
                .reopenedDefects(defectTrendsType.getReopenBug())
                .build();
        }).toList();
    }

    private String calculateDefectsStatus(Integer month, Integer previousMonth) {
        int result = month - previousMonth;
        if (result > 0) {
            return "high";
        }
        else if (result < 0) {
            return "low";
        }
        return "neutral";
    }

    @Override
    public DefectAgeingStatsDto getDefectAgeingStats(String emailAddress, String projectId) {
        projectHelper.findAndValidateProjectByIdAndUser(projectId);
        try {
            List<DefectAgeingTrends> defectAgeingTrends = defectAgeingTrendsRepository
                .findByProjectIdIn(Collections.singletonList(new ObjectId(projectId)));
            if (CollectionUtils.isEmpty(defectAgeingTrends)) {
                log.info("No record available for {} project", projectId);
                throw new RecordNotFoundException(RECORD_NOT_FOUND);
            }
            DefectAgeingTrends defectAgeingTrend = defectAgeingTrends.get(0);
            if (CollectionUtils.isEmpty(defectAgeingTrend.getData())) {
                log.info("No record available for {} project under data field of defects_ageing_trends", projectId);
                throw new RecordNotFoundException(RECORD_NOT_FOUND);
            }
            return DefectAgeingStatsDto.builder()
                .id(defectAgeingTrend.getId())
                .jiraProjectId(defectAgeingTrend.getJiraProjectId())
                .name(defectAgeingTrend.getProjectName())
                .range(buildDefectAgeingStatsRangeDto(defectAgeingTrend))
                .build();
        }
        catch (IllegalArgumentException ex) {
            log.info("projectId {} is not in hex format", projectId);
            throw new BadRequestException("Please enter valid project id");
        }
    }

    private List<DefectAgeingStatsRangeDto> buildDefectAgeingStatsRangeDto(DefectAgeingTrends defectAgeingTrend) {
        return defectAgeingTrend.getData()
            .stream()
            .map(trend -> DefectAgeingStatsRangeDto.builder()
                .value(createRange(trend.getMin(), trend.getMax()))
                .blocker(trend.getBlocker())
                .critical(trend.getCritical())
                .major(trend.getMajor())
                .minor(trend.getMinor())
                .unattended(trend.getUnattended())
                .build())
            .toList();
    }

    private String createRange(int min, int max) {
        if (max > 100) {
            return ">100";
        }
        return min + "-" + max;
    }

}
