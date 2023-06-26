package com.apexon.compass.psrservice.service.impl;

import static com.apexon.compass.constants.PsrServiceConstants.*;
import static com.apexon.compass.constants.WidgetsNameConstants.*;
import static com.apexon.compass.utilities.ArithmeticUtils.getRatioOfInteger;
import static com.apexon.compass.utilities.DateTimeUtils.convertMilisToWeek;
import static com.apexon.compass.utilities.DateTimeUtils.epochToMonthDate;
import static com.apexon.compass.utilities.DateTimeUtils.getDayOfWeekFromLongDate;
import static com.apexon.compass.utilities.DateTimeUtils.getDaysLeft;
import static com.apexon.compass.utilities.DateTimeUtils.getWorkingDays;
import static org.apache.commons.lang3.math.NumberUtils.INTEGER_ZERO;
import static org.springframework.data.domain.Sort.Direction.DESC;

import com.apexon.compass.aggregation.vo.AllocationVo;
import com.apexon.compass.aggregation.vo.LeavesVo;
import com.apexon.compass.aggregation.vo.NestMemberDetailsByMemberIdVo;
import com.apexon.compass.aggregation.vo.NestProjectWiseProjectMembersVo;
import com.apexon.compass.aggregation.vo.NestProjectsIdWiseProjectMemberLeaveVo;
import com.apexon.compass.aggregation.vo.ProjectMemberLeavesVo;
import com.apexon.compass.aggregation.vo.ProjectMembersDetailsVo;
import com.apexon.compass.aggregation.vo.ProjectWiseStorySummaryVo;
import com.apexon.compass.aggregation.vo.SprintAcceptedVsDeliveredVo;
import com.apexon.compass.aggregation.vo.SprintLoggedVsAcceptedBugsVo;
import com.apexon.compass.aggregation.vo.SprintMemberStatusByIdVo;
import com.apexon.compass.aggregation.vo.SprintMemberStatusVo;
import com.apexon.compass.aggregation.vo.SprintWithBlockersVo;
import com.apexon.compass.aggregation.vo.SprintWithBugsVo;
import com.apexon.compass.aggregation.vo.SprintWithOldTicketsVo;
import com.apexon.compass.aggregation.vo.SprintWithStoriesVo;
import com.apexon.compass.aggregation.vo.SprintWithStoryReportVo;
import com.apexon.compass.aggregation.vo.StoriesMemberStatusVo;
import com.apexon.compass.entities.*;
import com.apexon.compass.entities.Project;
import com.apexon.compass.exception.custom.ForbiddenException;
import com.apexon.compass.exception.custom.RecordNotFoundException;
import com.apexon.compass.exception.custom.UnauthorizedException;
import com.apexon.compass.psrservice.service.impl.helper.ProjectHelper;
import com.apexon.compass.psrservice.service.impl.helper.JiraRulesHelper;
import com.apexon.compass.psrservice.dao.DerivedSprintsDao;
import com.apexon.compass.psrservice.dao.NestProjectMemberDao;
import com.apexon.compass.psrservice.dao.ProjectMemberLeavesDao;
import com.apexon.compass.psrservice.dao.ProjectMembersDao;
import com.apexon.compass.psrservice.dao.PublicHolidaysDao;
import com.apexon.compass.psrservice.dao.SprintDao;
import com.apexon.compass.psrservice.dto.AcceptedVsDeliveredSummaryDto;
import com.apexon.compass.psrservice.dto.Allocation;
import com.apexon.compass.psrservice.dto.BlockersDto;
import com.apexon.compass.psrservice.dto.BugsDto;
import com.apexon.compass.psrservice.dto.CompletedSprintDto;
import com.apexon.compass.psrservice.dto.CompletionRateDto;
import com.apexon.compass.psrservice.dto.DailyAcceptedDeliveredDto;
import com.apexon.compass.psrservice.dto.DailyStatusDto;
import com.apexon.compass.psrservice.dto.DatewiseTeamPointsCompletionDto;
import com.apexon.compass.psrservice.dto.DefectsDto;
import com.apexon.compass.psrservice.dto.EffortVarianceDto;
import com.apexon.compass.psrservice.dto.LeaveCalenderDataDto;
import com.apexon.compass.psrservice.dto.LeaveCalenderDto;
import com.apexon.compass.psrservice.dto.LeaveDataDto;
import com.apexon.compass.psrservice.dto.LeavesDto;
import com.apexon.compass.psrservice.dto.MemberDto;
import com.apexon.compass.psrservice.dto.MemberStatusDto;
import com.apexon.compass.psrservice.dto.MembersDto;
import com.apexon.compass.psrservice.dto.PercentageTimeDto;
import com.apexon.compass.psrservice.dto.PointsCompletionDto;
import com.apexon.compass.psrservice.dto.ProjectDataWithBlockersDto;
import com.apexon.compass.psrservice.dto.ReportSummaryDto;
import com.apexon.compass.psrservice.dto.SayDoDto;
import com.apexon.compass.psrservice.dto.SayDoListDto;
import com.apexon.compass.psrservice.dto.SprintAcceptedVsDeliveredDto;
import com.apexon.compass.psrservice.dto.SprintAcceptedVsDeliveredDtos;
import com.apexon.compass.psrservice.dto.SprintBurndownChartDto;
import com.apexon.compass.psrservice.dto.SprintBurndownDataDto;
import com.apexon.compass.psrservice.dto.SprintBurndownDto;
import com.apexon.compass.psrservice.dto.SprintDataDto;
import com.apexon.compass.psrservice.dto.SprintDefectBugsDto;
import com.apexon.compass.psrservice.dto.SprintDto;
import com.apexon.compass.psrservice.dto.SprintEffortVarianceDto;
import com.apexon.compass.psrservice.dto.SprintLoggedVsAcceptedBugsDto;
import com.apexon.compass.psrservice.dto.SprintMemberDetailsByIdDto;
import com.apexon.compass.psrservice.dto.SprintMemberStatusByIdDto;
import com.apexon.compass.psrservice.dto.SprintMemberStatusDto;
import com.apexon.compass.psrservice.dto.SprintOverviewDto;
import com.apexon.compass.psrservice.dto.SprintStoryReportDto;
import com.apexon.compass.psrservice.dto.StatusDto;
import com.apexon.compass.psrservice.dto.SummaryDto;
import com.apexon.compass.psrservice.dto.TaskDto;
import com.apexon.compass.psrservice.dto.TasksDto;
import com.apexon.compass.psrservice.dto.TeamDto;
import com.apexon.compass.psrservice.dto.TeamVelocityDto;
import com.apexon.compass.psrservice.dto.JiraBoardIdsDetailsDto;
import com.apexon.compass.psrservice.dto.BoardDetailsDto;
import com.apexon.compass.psrservice.repository.JiraConfigurationRepository;
import com.apexon.compass.psrservice.repository.NestProjectsRepository;
import com.apexon.compass.psrservice.repository.ProjectMembersRepository;
import com.apexon.compass.psrservice.repository.SprintChartRepository;
import com.apexon.compass.psrservice.repository.SprintsRepository;
import com.apexon.compass.psrservice.repository.BoardsRepository;
import com.apexon.compass.psrservice.service.JiraService;
import com.apexon.compass.utilities.DateTimeUtils;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.ParseException;
import java.time.DayOfWeek;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

import lombok.AllArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor(onConstructor = @__({ @Autowired }))
public class JiraServiceImpl implements JiraService {

    public static final String NO_JIRA_DATA_AVAILABLE_FOR_THIS_ID_OR_ACTIVE_SPRINT = "No jira data available for this id or active sprint ";

    public static final String PLEASE_PASS_BOARD_ID = "Please pass boardId";

    public static final String DATA_IS_UNAVAILABLE = "Data is unavailable";

    public static final String DATA_IS_NOT_AVAILABLE = "Data is not available";

    public static final String NO_ACTIVE_SPRINT_IS_AVAILABLE_FOR_THIS_PROJECT = "No active sprint is available for this project";

    private SprintChartRepository sprintChartRepository;

    private DerivedSprintsDao derivedSprintsDao;

    private JiraConfigurationRepository jiraConfigurationRepository;

    private SprintsRepository sprintsRepository;

    private SprintDao sprintDao;

    private ProjectHelper projectHelper;

    private ProjectMembersRepository projectMembersRepository;

    private ProjectMemberLeavesDao projectMemberLeavesDao;

    private NestProjectsRepository nestProjectsRepository;

    private ProjectMembersDao projectMembersDao;

    private NestProjectMemberDao nestProjectMemberDao;

    private PublicHolidaysDao publicHolidaysDao;

    private JiraRulesHelper jiraRulesHelper;

    private BoardsRepository boardsRepository;

    @Override
    public TeamVelocityDto getTeamVelocity(String jiraProjectId) {
        List<SprintChart> sprintChart = sprintChartRepository.findByJiraProjectId(Integer.parseInt(jiraProjectId),
                PageRequest.of(0, 10, Sort.by(Direction.DESC, "endDate")));
        if (sprintChart.isEmpty()) {
            throw new RecordNotFoundException("No jira data available for this id ");
        }
        List<SprintDataDto> teamVelocity = new ArrayList<>();
        sprintChart.forEach(sprint -> teamVelocity.add(SprintDataDto.builder()
            .sprintName(sprint.getName())
            .team(getAccumulatedTeamData(sprint.getStatusData()))
            .build()));
        return TeamVelocityDto.builder()
            .data(teamVelocity)
            .teamMembers(derivedSprintsDao.findDistinctTeamMembersByProjectId(jiraProjectId)
                .stream()
                .map(name -> name == null ? NA : name)
                .toList())
            .projectName(StringUtils.EMPTY)
            .jiraProjectId(sprintChart.get(0).getJiraProjectId())
            .projectId(StringUtils.EMPTY)
            .build();
    }

    @Override
    public List<TeamDto> getAccumulatedTeamData(List<StatusData> statusList) {
        List<TeamDto> teamList = new ArrayList<>();
        statusList.forEach(status -> status.getTeam().forEach(team -> {
            TeamDto teamDto;
            Optional<TeamDto> dataOptional = teamList.stream()
                .filter(map -> map.getPersonName().equalsIgnoreCase(team.getPersonName()))
                .findAny();
            if (dataOptional.isPresent()) {
                teamDto = dataOptional.get();
                teamDto.setPersonName(team.getPersonName());
                teamDto.setStoryPointCompleted(teamDto.getStoryPointCompleted() + team.getStoryPointCompleted());
            }
            else {
                teamList.add(TeamDto.builder()
                    .personName(team.getPersonName() == null ? NA : team.getPersonName())
                    .storyPointCompleted(team.getStoryPointCompleted() != null ? team.getStoryPointCompleted() : 0)
                    .build());
            }
        }));
        return teamList;
    }

    @Override
    public SprintBurndownDto getSprintBurndown(String jiraProjectId) {
        int counter = 1;
        List<SprintBurndownDataDto> burnDownList = new ArrayList<>();
        List<SprintChart> sprintChart = sprintChartRepository
            .findByJiraProjectIdAndState(Integer.parseInt(jiraProjectId), ACTIVE);
        if (sprintChart.isEmpty()) {
            throw new RecordNotFoundException(NO_JIRA_DATA_AVAILABLE_FOR_THIS_ID_OR_ACTIVE_SPRINT);
        }
        List<StatusData> sortedStatusData = CollectionUtils.isNotEmpty(sprintChart)
                ? sprintChart.get(0).getStatusData().stream().toList() : new ArrayList<>();
        for (StatusData status : sortedStatusData) {
            burnDownList.add(SprintBurndownDataDto.builder()
                .name(StringUtils.join(DAY_STRING, StringUtils.SPACE, (counter++)))
                .date(status.getDate())
                .remainingEfforts(status.getRemainingEfforts())
                .remainingTasks(status.getRemainingTasks())
                .completedTasks(status.getCompletedTasks())
                .build());
        }
        return SprintBurndownDto.builder()
            .data(burnDownList)
            .projectName(StringUtils.EMPTY)
            .jiraProjectId(sprintChart.get(0).getJiraProjectId())
            .projectId(StringUtils.EMPTY)
            .build();
    }

    @Override
    public PointsCompletionDto getStoryPointCompletionStatus(String jiraProjectId) {
        List<DatewiseTeamPointsCompletionDto> statusList = new ArrayList<>();
        List<SprintChart> sprintChart = sprintChartRepository
            .findByJiraProjectIdAndState(Integer.parseInt(jiraProjectId), ACTIVE);
        if (sprintChart.isEmpty()) {
            throw new RecordNotFoundException(NO_JIRA_DATA_AVAILABLE_FOR_THIS_ID_OR_ACTIVE_SPRINT);
        }
        List<StatusData> sortedStatusData = CollectionUtils.isNotEmpty(sprintChart)
                ? sprintChart.get(0).getStatusData().stream().toList() : new ArrayList<>();
        sortedStatusData.forEach(statusData -> {
            DatewiseTeamPointsCompletionDto datewiseTeamPointsCompletionDto = new DatewiseTeamPointsCompletionDto();
            datewiseTeamPointsCompletionDto.setDate(convertMilisToWeek(statusData.getDate()));
            List<TeamDto> teamList = new ArrayList<>();
            statusData.getTeam().forEach(team -> {
                TeamDto teamDto;
                Optional<TeamDto> dataOptional = teamList.stream()
                    .filter(map -> map.getPersonName().equalsIgnoreCase(team.getPersonName()))
                    .findAny();
                if (dataOptional.isPresent()) {
                    teamDto = dataOptional.get();
                    teamDto.setPersonName(team.getPersonName());
                    teamDto.setStoryPointCompleted(teamDto.getStoryPointCompleted() + team.getStoryPointCompleted());
                }
                else {
                    teamList.add(TeamDto.builder()
                        .personName(team.getPersonName())
                        .storyPointCompleted(team.getStoryPointCompleted())
                        .build());
                    datewiseTeamPointsCompletionDto.setTeam(teamList);
                }
            });
            statusList.add(datewiseTeamPointsCompletionDto);
        });
        return PointsCompletionDto.builder()
            .data(statusList)
            .teamMembers(derivedSprintsDao.findDistinctTeamMembersByProjectId(jiraProjectId))
            .build();
    }

    @Override
    public CompletionRateDto getCompletionRate(String jiraProjectId) {
        List<SprintChart> sprintChart = sprintChartRepository
            .findByJiraProjectIdAndState(Integer.parseInt(jiraProjectId), ACTIVE);
        if (sprintChart.isEmpty()) {
            throw new RecordNotFoundException(NO_JIRA_DATA_AVAILABLE_FOR_THIS_ID_OR_ACTIVE_SPRINT);
        }
        List<StatusData> sortedStatusData = CollectionUtils.isNotEmpty(sprintChart)
                ? sprintChart.get(0).getStatusData().stream().toList() : new ArrayList<>();
        int completedEfforts = sprintChart.get(0).getTotalEfforts()
                - sortedStatusData.get(sortedStatusData.size() - 1).getRemainingEfforts();
        return CompletionRateDto.builder()
            .completionRate(getRatioOfInteger(completedEfforts, sprintChart.get(0).getTotalEfforts()))
            .lastUpdated(sprintChart.get(0).getCreatedDate())
            .projectName(StringUtils.EMPTY)
            .projectId(StringUtils.EMPTY)
            .jiraProjectId(sprintChart.get(0).getJiraProjectId())
            .build();
    }

    @Override
    public CompletedSprintDto getCompletedSprintRate(String jiraProjectId) {
        List<SprintChart> sprintCharts = sprintChartRepository.findByJiraProjectId(Integer.parseInt(jiraProjectId));
        if (sprintCharts.isEmpty()) {
            throw new RecordNotFoundException("No jira data available for this id ");
        }
        int totalSprints = sprintCharts.size();
        int remainingSprints = sprintCharts.stream()
            .filter(sprint -> !sprint.getState().equalsIgnoreCase("CLOSED"))
            .toList()
            .size();
        return CompletedSprintDto.builder()
            .totalSprint(totalSprints)
            .remainingSprint(remainingSprints)
            .ratio(BigDecimal.valueOf((((double) (totalSprints - remainingSprints) * 100) / totalSprints))
                .setScale(2, RoundingMode.HALF_DOWN) + PERCENTAGE)
            .lastUpdated(sprintCharts.get(0).getCreatedDate())
            .projectName(StringUtils.EMPTY)
            .jiraProjectId(sprintCharts.get(0).getJiraProjectId())
            .projectId(StringUtils.EMPTY)
            .build();
    }

    @Override
    public DailyAcceptedDeliveredDto getDailyAcceptedDelivered(String jiraProjectId) {
        DailyAcceptedDeliveredDto dailyAcceptedDeliveredDto = new DailyAcceptedDeliveredDto();
        List<TasksDto> dataList = new ArrayList<>();
        List<SprintChart> sprintCharts = sprintChartRepository
            .findByJiraProjectIdAndState(Integer.valueOf(jiraProjectId), ACTIVE);
        if (sprintCharts.isEmpty()) {
            throw new RecordNotFoundException(NO_JIRA_DATA_AVAILABLE_FOR_THIS_ID_OR_ACTIVE_SPRINT);
        }
        sprintCharts.forEach(sprintChart -> sprintChart.getStatusData().forEach(status -> {
            TasksDto tasksDto = TasksDto.builder()
                .date(epochToMonthDate(status.getDate()))
                .acceptedTasks(status.getAcceptedTasks())
                .completedTasks(status.getCompletedTasks())
                .build();
            dataList.add(tasksDto);
        }));
        dailyAcceptedDeliveredDto.setData(dataList);
        dailyAcceptedDeliveredDto.setProjectName(StringUtils.EMPTY);
        dailyAcceptedDeliveredDto.setJiraProjectId(jiraProjectId);
        dailyAcceptedDeliveredDto.setProjectId(jiraProjectId);
        return dailyAcceptedDeliveredDto;
    }

    @Override
    public SprintOverviewDto getSprintOverview(String projectId, Integer boardId) {
        if (boardId == null) {
            throw new RecordNotFoundException(PLEASE_PASS_BOARD_ID);
        }
        Project project = projectHelper.findAndValidateProjectByIdAndUser(projectId);
        validateAccess(GET_SPRINT_OVERVIEW);
        List<SprintChart> derivedSprints = sprintChartRepository.findByJiraProjectIdAndStateAndBoardId(
                Integer.parseInt(getJiraConfiguration(project.getId()).getJiraProjectId()), ACTIVE, boardId);
        if (CollectionUtils.isEmpty(derivedSprints)) {
            throw new RecordNotFoundException("No jira record found");
        }
        SprintChart sprintChart = derivedSprints.get(0);
        SprintOverviewDto sprintOverviewDto = SprintOverviewDto.builder()
            .id(project.getId())
            .jiraProjectId(getJiraConfiguration(projectId).getJiraProjectId())
            .boardId(sprintChart.getBoardId())
            .build();
        List<ProjectMembers> projectMembers = projectMembersRepository
            .findByNestIdIn(project.getNestIds().stream().filter(Objects::nonNull).map(Integer::valueOf).toList());

        Long startDate = getStartDateOfIST(Instant.now().toEpochMilli());
        List<NestProjects> nestProjects = nestProjectsRepository.findByIscProjectId(new ObjectId(project.getId()));
        List<NestProjectsIdWiseProjectMemberLeaveVo> nestProjectsIdWiseProjectMemberLeaves = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(nestProjects)) {
            nestProjectsIdWiseProjectMemberLeaves = projectMemberLeavesDao.getMemberLeavesForSprint(
                    nestProjects.get(0).getIscProjectId(),
                    Optional.ofNullable(sprintChart.getStartDate()).isPresent() ? startDate
                            : Instant.now().toEpochMilli(),
                    Optional.ofNullable(sprintChart.getEndDate()).isPresent() ? sprintChart.getEndDate()
                            : Instant.now().plus(14, ChronoUnit.DAYS).toEpochMilli());
        }
        List<ProjectMemberLeavesVo> projectMemberLeaves = new ArrayList<>();
        nestProjectsIdWiseProjectMemberLeaves.forEach(nest -> {
            if (CollectionUtils.isNotEmpty(nest.getProjectMemberLeavesVo())) {
                projectMemberLeaves.addAll(nest.getProjectMemberLeavesVo());
            }
        });
        double leaveHoursCount = CollectionUtils.isNotEmpty(projectMemberLeaves) ? projectMemberLeaves.stream()
            .map(ProjectMemberLeavesVo::getLeaves)
            .toList()
            .stream()
            .mapToDouble(value -> value.stream()
                .filter(leave -> leave.getDate() >= startDate && leave.getDate() < sprintChart.getEndDate())
                .mapToDouble(LeavesVo::getHour)
                .sum())
            .sum() : 0;
        List<Long> holidays = publicHolidaysDao.findDistinctHolidayByDate(
                getStartDateOfIST(Instant.now().toEpochMilli()), sprintChart.getEndDate(), INDIA_LOCATIONS);
        int remainingHours = CollectionUtils.isNotEmpty(projectMembers)
                ? (int) ((getDaysLeft(sprintChart.getEndDate()) - holidays.size()) * 8 * projectMembers.size()
                        - leaveHoursCount)
                : 0;

        SprintDto sprintDto = SprintDto.builder()
            .daysLeft(Math.max((getDaysLeft(sprintChart.getEndDate()) - holidays.size()), 0))
            .hoursLeft(Math.max(remainingHours, 0))
            .startDate(sprintChart.getStartDate())
            .id(String.valueOf(sprintChart.getSprintId()))
            .endDate(sprintChart.getEndDate())
            .name(sprintChart.getName())
            .build();
        List<DailyStatusDto> dailyStatusDtos = new ArrayList<>();
        sprintChart.getStatusData()
            .forEach(statusData -> dailyStatusDtos.add((DailyStatusDto.builder()
                .completed(statusData.getCompletedTasks())
                .inprogress(statusData.getInProgressTasks())
                .date(statusData.getDate())
                .remaining(statusData.getRemainingTasks())
                .todo(statusData.getToDoTasks())
                .build())));
        sprintDto.setDailyStatus(dailyStatusDtos);
        sprintOverviewDto.setSprint(sprintDto);
        return sprintOverviewDto;
    }

    private Long getStartDateOfIST(Long date) {
        return Instant.ofEpochMilli(date)
            .atZone(ZoneId.of(INDIA_TIME_ZONE))
            .toLocalDate()
            .atStartOfDay(ZoneId.of(INDIA_TIME_ZONE))
            .toInstant()
            .toEpochMilli();
    }

    @Override
    public StatusDto getStatusByProject(String projectId, Integer boardId) {
        if (boardId == null) {
            throw new RecordNotFoundException(PLEASE_PASS_BOARD_ID);
        }
        Project project = projectHelper.findAndValidateProjectByIdAndUser(projectId);
        validateAccess(GET_STATUS_BY_PROJECT);
        List<Sprints> sprints = sprintsRepository.findByJiraProjectIdAndState(
                Integer.parseInt(getJiraConfiguration(project.getId()).getJiraProjectId()), ACTIVE);
        if (CollectionUtils.isEmpty(sprints)) {
            throw new RecordNotFoundException(DATA_IS_UNAVAILABLE);
        }
        Sprints activeSprint = getActiveSprint(
                sprints.stream().filter(sprint -> sprint.getBoardId().equals(boardId)).findAny());
        ProjectWiseStorySummaryVo projectWiseStorySummaryVo = sprintDao.getStories(activeSprint.getSprintId(),
                projectId, boardId);
        if (projectWiseStorySummaryVo == null) {
            throw new RecordNotFoundException(DATA_IS_NOT_AVAILABLE);
        }
        if (CollectionUtils.isEmpty(projectWiseStorySummaryVo.getStories())) {
            throw new RecordNotFoundException("No record Found for Sprint");
        }
        if (projectWiseStorySummaryVo.getStories().get(0) == null) {
            throw new RecordNotFoundException("No record Found for Sprint");
        }
        return StatusDto.builder()
            .id(project.getId())
            .jiraProjectId(getJiraConfiguration(project.getId()).getJiraProjectId())
            .boardId(projectWiseStorySummaryVo.getBoardId())
            .sprintId(projectWiseStorySummaryVo.getSprintId())
            .sprintName(projectWiseStorySummaryVo.getName())
            .summary(SummaryDto.builder()
                .assigned(projectWiseStorySummaryVo.getStories().get(0).getTotalAssigned())
                .completed(projectWiseStorySummaryVo.getStories().get(0).getTotalCompleted())
                .total(projectWiseStorySummaryVo.getStories().get(0).getTotal())
                .inProgress(projectWiseStorySummaryVo.getStories().get(0).getTotalInProgress())
                .todo(projectWiseStorySummaryVo.getStories().get(0).getTotalToDo())
                .unAssigned(projectWiseStorySummaryVo.getStories().get(0).getTotalUnassigned())
                .build())
            .build();
    }

    private Sprints getActiveSprint(Optional<Sprints> activeSprints) {
        return activeSprints.isPresent() ? activeSprints.get()
                : activeSprints.orElseThrow(() -> new RecordNotFoundException("No active sprint is available"));
    }

    @Override
    public DefectsDto getBugs(String projectId, Integer boardId) {
        if (boardId == null) {
            throw new RecordNotFoundException(PLEASE_PASS_BOARD_ID);
        }
        Project project = projectHelper.findAndValidateProjectByIdAndUser(projectId);
        validateAccess(GET_BUGS);
        List<Sprints> sprints = getSprints(project.getId());
        if (CollectionUtils.isEmpty(sprints)) {
            throw new RecordNotFoundException(DATA_IS_UNAVAILABLE);
        }
        Sprints activeSprint = getActiveSprint(
                sprints.stream().filter(sprint -> sprint.getBoardId().equals(boardId)).findAny());
        SprintWithBugsVo sprintWithBugs = sprintDao.getBugs(activeSprint.getSprintId(), projectId, boardId);
        if (sprintWithBugs == null) {
            throw new RecordNotFoundException("Data is available");
        }
        BugsDto bugs;
        if (CollectionUtils.isNotEmpty(sprintWithBugs.getBugs())) {
            bugs = BugsDto.builder()
                .total(sprintWithBugs.getBugs().get(0).getTotal().intValue())
                .pending(sprintWithBugs.getBugs().get(0).getTotalOpen().intValue())
                .completed(sprintWithBugs.getBugs().get(0).getTotalCompleted().intValue())
                .inProgress(sprintWithBugs.getBugs().get(0).getTotalInProgress().intValue())
                .build();
        }
        else {
            bugs = BugsDto.builder()
                .inProgress(INTEGER_ZERO)
                .completed(INTEGER_ZERO)
                .pending(INTEGER_ZERO)
                .total(INTEGER_ZERO)
                .build();
        }
        return DefectsDto.builder()
            .id(project.getId())
            .sprintName(activeSprint.getName())
            .sprintId(String.valueOf(activeSprint.getSprintId()))
            .boardId(sprintWithBugs.getBoardId())
            .jiraProjectId(String.valueOf(activeSprint.getJiraProjectId()))
            .bugs(bugs)
            .build();
    }

    @Override
    public SprintLoggedVsAcceptedBugsDto getLoggedVsAcceptedBugs(String projectId, Integer boardId) {
        if (boardId == null) {
            throw new RecordNotFoundException(PLEASE_PASS_BOARD_ID);
        }
        Project project = projectHelper.findAndValidateProjectByIdAndUser(projectId);
        validateAccess(GET_LOGGED_VS_ACCEPTED_BUGS);
        List<Sprints> sprints = getSprints(project.getId());
        if (CollectionUtils.isEmpty(sprints)) {
            throw new RecordNotFoundException("No Active Sprint record is available");
        }
        Sprints activeSprint = getActiveSprint(
                sprints.stream().filter(sprint -> sprint.getBoardId().equals(boardId)).findAny());
        SprintLoggedVsAcceptedBugsVo sprintLoggedVsAcceptedBugs = sprintDao
            .getLoggedVsAcceptedBugs(activeSprint.getSprintId(), projectId, boardId);
        if (sprintLoggedVsAcceptedBugs == null) {
            throw new RecordNotFoundException(DATA_IS_NOT_AVAILABLE);
        }
        SprintDefectBugsDto sprintDefectBugs;
        if (CollectionUtils.isNotEmpty(sprintLoggedVsAcceptedBugs.getBugs())) {
            sprintDefectBugs = SprintDefectBugsDto.builder()
                .accepted(sprintLoggedVsAcceptedBugs.getBugs().get(0).getTotalCompleted().intValue())
                .logged(sprintLoggedVsAcceptedBugs.getBugs().get(0).getTotalLogged().intValue())
                .build();
        }
        else {
            sprintDefectBugs = SprintDefectBugsDto.builder().accepted(INTEGER_ZERO).logged(INTEGER_ZERO).build();
        }
        return SprintLoggedVsAcceptedBugsDto.builder()
            .id(project.getId())
            .jiraProjectId(String.valueOf(activeSprint.getJiraProjectId()))
            .sprintId(String.valueOf(activeSprint.getSprintId()))
            .sprintName(activeSprint.getName())
            .boardId(sprintLoggedVsAcceptedBugs.getBoardId())
            .bugs(sprintDefectBugs)
            .build();
    }

    private JiraConfiguration getJiraConfiguration(String projectId) {
        Optional<JiraConfiguration> jiraConfiguration = jiraConfigurationRepository
            .findByProjectId(new ObjectId(projectId));
        return jiraConfiguration.isPresent() ? jiraConfiguration.get()
                : jiraConfiguration.orElseThrow(() -> new RecordNotFoundException("No jira record found"));
    }

    private List<Sprints> getSprints(String projectId) {
        List<Sprints> sprint = sprintsRepository
            .findByJiraProjectIdAndState(Integer.parseInt(getJiraConfiguration(projectId).getJiraProjectId()), ACTIVE);
        if (CollectionUtils.isEmpty(sprint)) {
            throw new RecordNotFoundException("No sprint record found");
        }
        return sprint;
    }

    @Override
    public ProjectDataWithBlockersDto getBlockers(String projectId, Integer boardId) {
        if (boardId == null) {
            throw new RecordNotFoundException(PLEASE_PASS_BOARD_ID);
        }
        Project project = projectHelper.findAndValidateProjectByIdAndUser(projectId);
        validateAccess(GET_BLOCKERS);
        List<Sprints> sprints = getSprints(project.getId());
        if (CollectionUtils.isEmpty(sprints)) {
            throw new RecordNotFoundException(NO_ACTIVE_SPRINT_IS_AVAILABLE_FOR_THIS_PROJECT);
        }
        Sprints activeSprint = getActiveSprint(
                sprints.stream().filter(sprint -> sprint.getBoardId().equals(boardId)).findAny());
        SprintWithBlockersVo sprintWithBlockers = sprintDao.getBlockers(activeSprint.getSprintId(), projectId, boardId);
        return ProjectDataWithBlockersDto.builder()
            .id(project.getId())
            .jiraProjectId(String.valueOf(activeSprint.getJiraProjectId()))
            .boardId(boardId)
            .sprintId(String.valueOf(activeSprint.getSprintId()))
            .sprintName(activeSprint.getName())
            .blockers(BlockersDto.builder()
                .total((int) (sprintWithBlockers == null || CollectionUtils.isEmpty(sprintWithBlockers.getBlockers())
                        ? 0 : sprintWithBlockers.getBlockers().stream().count()))
                .build())
            .build();
    }

    @Override
    public SprintEffortVarianceDto getEffortVariance(String projectId, Integer boardId) {
        if (boardId == null) {
            throw new RecordNotFoundException(PLEASE_PASS_BOARD_ID);
        }
        Project project = projectHelper.findAndValidateProjectByIdAndUser(projectId);
        validateAccess(GET_EFFORT_VARIANCE);
        List<Sprints> sprints = getSprints(project.getId());
        if (CollectionUtils.isEmpty(sprints)) {
            throw new RecordNotFoundException(NO_ACTIVE_SPRINT_IS_AVAILABLE_FOR_THIS_PROJECT);
        }
        Sprints activeSprint = getActiveSprint(
                sprints.stream().filter(sprint -> sprint.getBoardId().equals(boardId)).findAny());
        SprintWithStoriesVo sprintVarianceVo = sprintDao.getSprintVariance(activeSprint.getSprintId(), boardId,
                projectId);
        SprintWithOldTicketsVo overallVarianceVo = sprintDao.getOverallVariance(activeSprint.getSprintId(), boardId,
                projectId);

        if (sprintVarianceVo == null || overallVarianceVo == null) {
            throw new RecordNotFoundException(DATA_IS_NOT_AVAILABLE);
        }
        if (CollectionUtils.isEmpty(sprintVarianceVo.getSprintTickets())
                || sprintVarianceVo.getSprintTickets().get(0) == null
                || CollectionUtils.isEmpty(overallVarianceVo.getSprintTickets())
                || overallVarianceVo.getSprintTickets().get(0) == null) {
            throw new RecordNotFoundException(DATA_IS_NOT_AVAILABLE);
        }
        if (CollectionUtils.isEmpty(sprintVarianceVo.getSprintTickets())
                && CollectionUtils.isEmpty(overallVarianceVo.getSprintTickets())) {
            throw new RecordNotFoundException("No story record is available");
        }
        int actualEffort = overallVarianceVo.getSprintTickets().stream().mapToInt(Stories::getEstimatedTime).sum();
        int actualSp = sprintVarianceVo.getSprintTickets().stream().mapToInt(Stories::getEstimate).sum();
        return SprintEffortVarianceDto.builder()
            .id(project.getId())
            .jiraProjectId(String.valueOf(activeSprint.getJiraProjectId()))
            .sprintId(String.valueOf(activeSprint.getSprintId()))
            .boardId(sprintVarianceVo.getBoardId())
            .sprintName(activeSprint.getName())
            .ev(EffortVarianceDto.builder()
                .sprint(PercentageTimeDto.builder().time(0).percentage(0 + PERCENTAGE).build())
                .overall(PercentageTimeDto.builder().time(0).percentage(0 + PERCENTAGE).build())
                .build())
            .build();
    }

    @Override
    public SprintAcceptedVsDeliveredDtos getAcceptedVsDelivered(String projectId, Integer boardId) {
        if (boardId == null) {
            throw new RecordNotFoundException(PLEASE_PASS_BOARD_ID);
        }
        projectHelper.findAndValidateProjectByIdAndUser(projectId);
        validateAccess(GET_ACCEPTED_VS_DELIVERED);
        List<SprintAcceptedVsDeliveredVo> sprintAcceptedVsDeliveredVos = sprintDao.getAcceptedVsDelivered(boardId,
                projectId);
        if (CollectionUtils.isEmpty(sprintAcceptedVsDeliveredVos)) {
            throw new RecordNotFoundException("No record found");
        }
        List<SprintAcceptedVsDeliveredDto> sprintAcceptedVsDeliveredDtos = getAcceptedVsDelivered(
                sprintAcceptedVsDeliveredVos);
        Collections.reverse(sprintAcceptedVsDeliveredDtos);
        return SprintAcceptedVsDeliveredDtos.builder().data(sprintAcceptedVsDeliveredDtos).build();
    }

    private List<SprintAcceptedVsDeliveredDto> getAcceptedVsDelivered(
            List<SprintAcceptedVsDeliveredVo> sprintAcceptedVsDeliveredVos) {
        return sprintAcceptedVsDeliveredVos.stream()
            .map(sprintAcceptedVsDeliveredVo -> SprintAcceptedVsDeliveredDto.builder()
                .id(sprintAcceptedVsDeliveredVo.getProjectId().toString())
                .jiraProjectId(String.valueOf(sprintAcceptedVsDeliveredVo.getProjectId()))
                .sprintId(String.valueOf(sprintAcceptedVsDeliveredVo.getSprintId()))
                .sprintName(sprintAcceptedVsDeliveredVo.getName())
                .boardId(sprintAcceptedVsDeliveredVo.getBoardId())
                .summary(buildAcceptedVsDeliveredSummaryDto(sprintAcceptedVsDeliveredVo))
                .build())
            .toList();
    }

    private AcceptedVsDeliveredSummaryDto buildAcceptedVsDeliveredSummaryDto(
            SprintAcceptedVsDeliveredVo sprintAcceptedVsDeliveredVo) {
        boolean flag = sprintAcceptedVsDeliveredVo.getStoriesVos().stream().noneMatch(Objects::nonNull);
        return CollectionUtils.isEmpty(sprintAcceptedVsDeliveredVo.getStoriesVos())
                ? AcceptedVsDeliveredSummaryDto.builder()
                    .totalDone(INTEGER_ZERO)
                    .totalClosed(INTEGER_ZERO)
                    .total(INTEGER_ZERO)
                    .build()
                : AcceptedVsDeliveredSummaryDto.builder()
                    .total(flag ? null : sprintAcceptedVsDeliveredVo.getStoriesVos().get(0).getTotal())
                    .totalClosed(flag ? null : sprintAcceptedVsDeliveredVo.getStoriesVos().get(0).getTotalClosed())
                    .totalDone(
                            flag ? null : sprintAcceptedVsDeliveredVo.getStoriesVos().get(0).getTotalDone().intValue())
                    .build();
    }

    @Override
    public SprintMemberStatusDto getMemberStatus(String projectId, Integer boardId) {
        if (boardId == null) {
            throw new RecordNotFoundException(PLEASE_PASS_BOARD_ID);
        }
        Project project = projectHelper.findAndValidateProjectByIdAndUser(projectId);
        validateAccess(GET_MEMBER_STATUS);
        List<Sprints> sprints = getSprints(project.getId());
        if (CollectionUtils.isEmpty(sprints)) {
            throw new RecordNotFoundException("No sprint is available for this project");
        }
        Sprints activeSprint = getActiveSprint(
                sprints.stream().filter(sprint -> sprint.getBoardId().equals(boardId)).findAny());
        SprintMemberStatusVo sprintMemberStatusVo = sprintDao.getMemberStatus(activeSprint.getSprintId(), boardId,
                projectId);
        if (sprintMemberStatusVo == null || CollectionUtils.isEmpty(sprintMemberStatusVo.getStories())
                || sprintMemberStatusVo.getStories().get(0) == null) {
            throw new RecordNotFoundException("No record is available ");
        }
        List<NestProjectWiseProjectMembersVo> nestProjectWiseProjectMembersVos = projectMembersDao
            .findProjectMemberForSprint(project.getId());
        List<Long> holidays = publicHolidaysDao.findDistinctHolidayByDate(
                getStartDateOfIST(Instant.now().toEpochMilli()), activeSprint.getEndDate(), INDIA_LOCATIONS);
        List<ObjectId> nestProjectIds = nestProjectsRepository.findByIscProjectId(new ObjectId(project.getId()))
            .stream()
            .filter(Objects::nonNull)
            .map(nest -> new ObjectId(nest.getId()))
            .toList();

        List<MemberDto> members = new ArrayList<>();
        for (StoriesMemberStatusVo statusVo : sprintMemberStatusVo.getStories()) {
            if (CollectionUtils.isNotEmpty(statusVo.getOwnerFullNames())) {
                MemberDto memberDto;
                Optional<MemberDto> memberDtoOptional = members.stream()
                    .filter(member -> member.getName().equalsIgnoreCase(statusVo.getOwnerFullNames().get(0)))
                    .findAny();
                if (memberDtoOptional.isPresent()) {
                    memberDto = memberDtoOptional.get();
                    memberDto
                        .setCompleted(
                                memberDto.getCompleted()
                                        + (String
                                            .join(COMMA_PUNCTUATION,
                                                    jiraRulesHelper.getDefinitionOfDone(activeSprint.getProjectId()),
                                                    jiraRulesHelper
                                                        .getDefinitionOfAccepted(activeSprint.getProjectId()))
                                            .contains(statusVo.getState()) ? 1 : 0));
                    memberDto.setInProgress(memberDto.getInProgress()
                            + (jiraRulesHelper.getInProgressByProjectId(activeSprint.getProjectId())
                                .contains(statusVo.getState()) ? 1 : 0));
                    memberDto
                        .setTodo(memberDto.getTodo() + (jiraRulesHelper.getDefinitionOfToDo(activeSprint.getProjectId())
                            .contains(statusVo.getState()) ? 1 : 0));
                }
                else {
                    List<ProjectMembersDetailsVo> projectMembersDetailsVos = new ArrayList<>();
                    nestProjectWiseProjectMembersVos.forEach(nest -> {
                        if (CollectionUtils.isNotEmpty(nest.getProjectMembersDetailsVo()))
                            projectMembersDetailsVos.addAll(nest.getProjectMembersDetailsVo());
                    });
                    Optional<ProjectMembersDetailsVo> projectMembersDetailsVo = projectMembersDetailsVos.stream()
                        .filter(member -> member.getName().equalsIgnoreCase(statusVo.getOwnerFullNames().get(0)))
                        .findAny();
                    ProjectMemberLeaves memberLeave = getMemberWiseLeavesForSprint(nestProjectIds,
                            getStartDateOfIST(Instant.now().toEpochMilli()), activeSprint.getEndDate(),
                            projectMembersDetailsVo.isPresent() ? statusVo.getOwnerFullNames().get(0)
                                    : StringUtils.EMPTY);
                    members.add(MemberDto.builder()
                        .name(statusVo.getOwnerFullNames().get(0))
                        .accountId(statusVo.getOwnerIds().get(0))
                        .memberId(projectMembersDetailsVo.isPresent() ? projectMembersDetailsVo.get().getMemberId()
                                : StringUtils.EMPTY)
                        .todo((jiraRulesHelper.getDefinitionOfToDo(activeSprint.getProjectId())
                            .contains(statusVo.getState()) ? 1 : 0))
                        .inProgress((jiraRulesHelper.getInProgressByProjectId(activeSprint.getProjectId())
                            .contains(statusVo.getState()) ? 1 : 0))
                        .completed(((String.join(COMMA_PUNCTUATION,
                                jiraRulesHelper.getDefinitionOfDone(activeSprint.getProjectId()),
                                jiraRulesHelper.getDefinitionOfAccepted(activeSprint.getProjectId())))
                            .contains(statusVo.getState()) ? 1 : 0))
                        .availableHours(Math.max(((DateTimeUtils.getDaysLeft(activeSprint.getEndDate())
                                - holidays.size()) * 8)
                                - (int) (memberLeave != null ? memberLeave.getLeaves()
                                    .stream()
                                    .filter(leave -> leave.getDate() >= getStartDateOfIST(Instant.now().toEpochMilli())
                                            && leave.getDate() < activeSprint.getEndDate())
                                    .mapToDouble(Leaves::getHour)
                                    .sum() : 0),
                                0))
                        .build());
                }
            }
        }
        return SprintMemberStatusDto.builder()
            .id(project.getId())
            .jiraProjectId(String.valueOf(activeSprint.getJiraProjectId()))
            .sprintId(String.valueOf(activeSprint.getSprintId()))
            .sprintName(activeSprint.getName())
            .boardId(activeSprint.getBoardId())
            .members(members)
            .build();
    }

    private ProjectMemberLeaves getMemberWiseLeavesForSprint(List<ObjectId> nestProjectIds, long startDate,
            long endDate, String name) {
        return projectMemberLeavesDao.getMemberLeave(nestProjectIds, getStartDateOfIST(startDate), endDate, name);
    }

    @Override
    public SprintStoryReportDto getStoryReport(String projectId, Integer boardId) {
        if (boardId == null) {
            throw new RecordNotFoundException(PLEASE_PASS_BOARD_ID);
        }
        Project project = projectHelper.findAndValidateProjectByIdAndUser(projectId);
        validateAccess(GET_STORY_REPORT);
        List<Sprints> sprints = getSprints(project.getId());
        if (CollectionUtils.isEmpty(sprints)) {
            throw new RecordNotFoundException(NO_ACTIVE_SPRINT_IS_AVAILABLE_FOR_THIS_PROJECT);
        }
        Sprints activeSprint = getActiveSprint(
                sprints.stream().filter(sprint -> sprint.getBoardId().equals(boardId)).findAny());
        SprintWithStoryReportVo sprint = sprintDao.getStoryReport(activeSprint.getSprintId(), boardId, projectId);
        if (sprint == null || CollectionUtils.isEmpty(sprint.getStoryReport())) {
            throw new RecordNotFoundException(DATA_IS_NOT_AVAILABLE);
        }
        if (sprint.getStoryReport().get(0) == null) {
            throw new RecordNotFoundException("No story record is available");
        }
        return SprintStoryReportDto.builder()
            .id(project.getId())
            .jiraProjectId(String.valueOf(activeSprint.getJiraProjectId()))
            .sprintId(String.valueOf(activeSprint.getSprintId()))
            .sprintName(activeSprint.getName())
            .boardId(sprint.getBoardId())
            .summary(ReportSummaryDto.builder()
                .blocker(sprint.getStoryReport().get(0).getTotalBlocked().intValue())
                .completed(sprint.getStoryReport().get(0).getTotalCompleted().intValue())
                .delivered(sprint.getStoryReport().get(0).getTotalDelivered().intValue())
                .planned(sprint.getStoryReport().get(0).getTotalPlanned().intValue())
                .build())
            .build();
    }

    public SprintMemberStatusByIdDto getMemberStatusById(String projectId, String memberId, Integer boardId) {
        if (boardId == null) {
            throw new RecordNotFoundException(PLEASE_PASS_BOARD_ID);
        }
        Project project = projectHelper.findAndValidateProjectByIdAndUser(projectId);
        validateAccess(GET_MEMBER_STATUS_BY_ID);
        List<Sprints> sprints = getSprints(project.getId());
        Sprints activeSprint = getActiveSprint(
                sprints.stream().filter(sprint -> sprint.getBoardId().equals(boardId)).findAny());
        SprintMemberStatusByIdVo sprintMemberStatusbyIdVo = sprintDao
            .getMemberStatusByMemberId(activeSprint.getSprintId(), memberId, boardId, projectId);
        if (sprintMemberStatusbyIdVo == null || CollectionUtils.isEmpty(sprintMemberStatusbyIdVo.getTasks())) {
            throw new RecordNotFoundException("No record is available ");
        }

        int completed = (int) (sprintMemberStatusbyIdVo.getTasks()
            .stream()
            .filter(stories -> (String.join(COMMA_PUNCTUATION,
                    jiraRulesHelper.getDefinitionOfDone(activeSprint.getProjectId()),
                    jiraRulesHelper.getDefinitionOfAccepted(activeSprint.getProjectId())))
                .contains(stories.getState()))
            .count());
        int inProgress = (int) (sprintMemberStatusbyIdVo.getTasks()
            .stream()
            .filter(stories -> jiraRulesHelper.getInProgressByProjectId(activeSprint.getProjectId())
                .contains(stories.getState()))
            .count());
        int toDo = (int) (sprintMemberStatusbyIdVo.getTasks()
            .stream()
            .filter(stories -> jiraRulesHelper.getDefinitionOfToDo(activeSprint.getProjectId())
                .contains(stories.getState()))
            .count());

        List<TaskDto> taskDtos = new ArrayList<>();
        for (StoriesMemberStatusVo statusVo : sprintMemberStatusbyIdVo.getTasks()) {
            if (CollectionUtils.isNotEmpty(statusVo.getOwnerIds())) {
                taskDtos.add(TaskDto.builder()
                    .name(statusVo.getName())
                    .number(statusVo.getNumber())
                    .isSpilledOver(CollectionUtils.isNotEmpty(statusVo.getSprintJourney())
                            && statusVo.getSprintJourney().size() > 1)
                    .type(statusVo.getType())
                    .status(getStatus(statusVo, activeSprint))
                    .url(statusVo.getUrl())
                    .build());
            }
        }
        ProjectMemberLeaves projectMemberLeaves = getMemberWiseLeavesForSprint(
                nestProjectsRepository.findByIscProjectId(new ObjectId(project.getId()))
                    .stream()
                    .map(nest -> new ObjectId(nest.getId()))
                    .toList(),
                getStartDateOfIST(Instant.now().toEpochMilli()), activeSprint.getEndDate(),
                sprintMemberStatusbyIdVo.getTasks().get(0).getOwnerFullNames().get(0));

        return SprintMemberStatusByIdDto.builder()
            .id(project.getId())
            .jiraProjectId(String.valueOf(activeSprint.getJiraProjectId()))
            .sprintId(String.valueOf(activeSprint.getSprintId()))
            .sprintName(activeSprint.getName())
            .boardId(sprintMemberStatusbyIdVo.getBoardId())
            .member(MembersDto.builder()
                .name((sprintMemberStatusbyIdVo.getTasks().get(0).getOwnerFullNames().get(0)))
                .accountId(sprintMemberStatusbyIdVo.getTasks().get(0).getOwnerIds().get(0))
                .status(MemberStatusDto.builder()
                    .availableHours(
                            Math.max(((DateTimeUtils.getDaysLeft(activeSprint.getEndDate()) - publicHolidaysDao
                                .findDistinctHolidayByDate(getStartDateOfIST(Instant.now().toEpochMilli()),
                                        activeSprint.getEndDate(), INDIA_LOCATIONS)
                                .size()) * 8)
                                    - (Optional.ofNullable(projectMemberLeaves).isPresent()
                                            ? projectMemberLeaves.getLeaves()
                                                .stream()
                                                .filter(leave -> leave
                                                    .getDate() >= getStartDateOfIST(Instant.now().toEpochMilli())
                                                        && leave.getDate() < activeSprint.getEndDate())
                                                .mapToInt(leave -> leave.getHour().intValue())
                                                .sum()
                                            : 0),
                                    0))
                    .averageStoryPoints((sprintMemberStatusbyIdVo.getTasks()
                        .stream()
                        .mapToInt(StoriesMemberStatusVo::getEstimate)
                        .sum()) / sprintMemberStatusbyIdVo.getTasks().size())
                    .completed(completed)
                    .inProgress(inProgress)
                    .todo(toDo)
                    .spilledOver((int) taskDtos.stream().filter(TaskDto::getIsSpilledOver).count())
                    .build())
                .tasks(taskDtos)
                .build())
            .build();
    }

    private String getStatus(StoriesMemberStatusVo statusVo, Sprints activeSprint) {
        if ((String.join(COMMA_PUNCTUATION, jiraRulesHelper.getDefinitionOfDone(activeSprint.getProjectId()),
                jiraRulesHelper.getDefinitionOfAccepted(activeSprint.getProjectId())))
            .contains(statusVo.getState())) {
            return COMPLETED;
        }
        else if (jiraRulesHelper.getInProgressByProjectId(activeSprint.getProjectId()).contains(statusVo.getState())) {
            return IN_PROGRESS;
        }
        else {
            return TO_DO;
        }
    }

    @Override
    public SprintBurndownChartDto getSprintBurndownChart(String projectId, Integer boardId)
            throws UnauthorizedException, ForbiddenException, RecordNotFoundException, ParseException {
        if (boardId == null) {
            throw new RecordNotFoundException(PLEASE_PASS_BOARD_ID);
        }
        Project project = projectHelper.findAndValidateProjectByIdAndUser(projectId);
        validateAccess(GET_SPRINT_BURNDOWN_CHART_WDIGET);
        int counter = 1;
        int dayCounter = 0;
        List<SprintBurndownDataDto> burnDownList = new ArrayList<>();
        List<Sprints> sprints = getSprints(project.getId());
        Sprints activeSprint = getActiveSprint(
                sprints.stream().filter(sprint -> sprint.getBoardId().equals(boardId)).findAny());
        List<SprintChart> sprintChart = sprintChartRepository
            .findBySprintIdAndStateAndBoardId(activeSprint.getSprintId(), ACTIVE, boardId);
        if (CollectionUtils.isEmpty(sprintChart) || CollectionUtils.isEmpty(sprintChart.get(0).getStatusData())
                || sprintChart.get(0).getStatusData().get(0) == null) {
            throw new RecordNotFoundException("No data available for this projectId or active sprint");
        }
        List<StatusData> sortedStatusData = CollectionUtils.isNotEmpty(sprintChart)
                ? sprintChart.get(0).getStatusData().stream().toList() : new ArrayList<>();
        double totalEfforts = (double) sprintChart.get(0).getTotalEfforts();
        Long startDate = sprintChart.get(0).getStartDate();
        Long endDate = sprintChart.get(0).getEndDate();
        LocalDate startDates = Instant.ofEpochMilli(startDate).atZone(ZoneOffset.UTC).minusDays(1).toLocalDate();
        LocalDate endDates = Instant.ofEpochMilli(endDate).atZone(ZoneOffset.UTC).toLocalDate();
        long workingDays = getWorkingDays(startDates, endDates);

        for (StatusData status : sortedStatusData) {
            burnDownList.add(SprintBurndownDataDto.builder()
                .name(StringUtils.join(DAY_STRING, StringUtils.SPACE, (counter++)))
                .date(status.getDate())
                .remainingEfforts(status.getRemainingEfforts())
                .remainingTasks(status.getRemainingTasks())
                .completedTasks(status.getCompletedTasks())
                .idealCompleted((int) ((double) sprintChart.stream()
                    .filter(a -> a.getTotalEfforts() != null)
                    .filter(a -> (double) a.getTotalEfforts() == totalEfforts)
                    .filter(a -> (getDayOfWeekFromLongDate(status.getDate()).getValue() == 5
                            || getDayOfWeekFromLongDate(status.getDate()).getValue() == 6))
                    .toList()
                    .size() != 0
                            ? sprintChart.stream()
                                .filter(a -> (double) a.getTotalEfforts() == totalEfforts)
                                .mapToInt(SprintChart::getTotalEfforts)
                                .sum() - (dayCounter) * (totalEfforts / workingDays)
                            : sprintChart.stream()
                                .filter(a -> (double) a.getTotalEfforts() == totalEfforts)
                                .filter(a -> (getDayOfWeekFromLongDate(status.getDate()).getValue() != 5
                                        || getDayOfWeekFromLongDate(status.getDate()).getValue() != 6))
                                .mapToInt(SprintChart::getTotalEfforts)
                                .sum() - (dayCounter++) * (totalEfforts / workingDays)))
                .build());
        }
        return SprintBurndownChartDto.builder()
            .jiraProjectId(getJiraConfiguration(project.getId()).getJiraProjectId())
            .sprintId(activeSprint.getSprintId())
            .sprintName(activeSprint.getName())
            .boardId(sprintChart.get(0).getBoardId())
            .dailyStatus(burnDownList)
            .build();
    }

    public SayDoListDto getSayDoRatio(String projectId, Integer boardId) {
        if (boardId == null) {
            throw new RecordNotFoundException(PLEASE_PASS_BOARD_ID);
        }
        Project project = projectHelper.findAndValidateProjectByIdAndUser(projectId);
        validateAccess(GET_SAY_DO_RATIO);
        List<SprintChart> sprintCharts = sprintChartRepository.findByJiraProjectIdAndBoardId(
                Integer.valueOf(getJiraConfiguration(project.getId()).getJiraProjectId()), boardId,
                Sort.by(DESC, SPRINT_ID));
        if (CollectionUtils.isEmpty(sprintCharts)) {
            throw new RecordNotFoundException("No sprint record is available");
        }

        List<SprintChart> sprintChartsWithNoFuture = new ArrayList<>();
        sprintCharts.forEach(sprintChart -> {
            if (!sprintChart.getState().equals(FUTURE)) {
                sprintChartsWithNoFuture.add(sprintChart);
            }
        });
        List<SprintChart> sprintChartsLatestTen;
        if (sprintChartsWithNoFuture.size() > 10) {
            sprintChartsLatestTen = sprintChartsWithNoFuture.subList(0, 10);
        }
        else {
            sprintChartsLatestTen = sprintChartsWithNoFuture;
        }
        List<SayDoDto> sayDoDtos = new ArrayList<>();
        sprintChartsLatestTen.forEach(sprintChart -> {
            if (CollectionUtils.isNotEmpty(sprintChart.getStatusData())) {
                sayDoDtos.add(SayDoDto.builder()
                    .id(sprintChart.getSprintId())
                    .jiraProjectId(String.valueOf(sprintChart.getJiraProjectId()))
                    .boardId(sprintChart.getBoardId())
                    .name(sprintChart.getName())
                    .totalExpected(sprintChart.getTotalEfforts())
                    .totalCompleted(sprintChart.getStatusData()
                        .stream()
                        .filter(status -> CollectionUtils.isNotEmpty(status.getTeam()))
                        .mapToInt(status -> status.getTeam()
                            .stream()
                            .filter(team -> team.getStoryPointCompleted() != null)
                            .mapToInt(Team::getStoryPointCompleted)
                            .sum())
                        .reduce(0, Integer::sum))
                    .build());
            }
        });
        Collections.reverse(sayDoDtos);
        return SayDoListDto.builder().sprints(sayDoDtos).build();
    }

    @Override
    public LeaveCalenderDataDto getLeaveCalenderBySprintId(String projectId, Integer boardId) {
        if (boardId == null) {
            throw new RecordNotFoundException(PLEASE_PASS_BOARD_ID);
        }
        Project project = projectHelper.findAndValidateProjectByIdAndUser(projectId);
        validateAccess(GET_LEAVE_CALENDAR_BY_SPRINT_ID);
        List<Sprints> sprints = sprintsRepository.findByJiraProjectIdAndState(
                Integer.parseInt(getJiraConfiguration(project.getId()).getJiraProjectId()), ACTIVE);
        if (CollectionUtils.isEmpty(sprints)) {
            throw new RecordNotFoundException(DATA_IS_UNAVAILABLE);
        }
        Sprints activeSprint = getActiveSprint(
                sprints.stream().filter(sprint -> sprint.getBoardId().equals(boardId)).findAny());

        List<PublicHolidays> projectPublicHolidays = publicHolidaysDao.getProjectMemberLocationWisePublicHolidays(
                Optional.ofNullable(activeSprint.getStartDate()).isPresent() ? activeSprint.getStartDate()
                        : Instant.now().toEpochMilli(),
                Optional.ofNullable(activeSprint.getEndDate()).isPresent() ? activeSprint.getEndDate()
                        : Instant.now().plus(14, ChronoUnit.DAYS).toEpochMilli(),
                INDIA_LOCATIONS);
        List<NestProjectsIdWiseProjectMemberLeaveVo> nestProjectMemberLeaves = projectMemberLeavesDao
            .getMemberLeaveData(
                    Optional.ofNullable(activeSprint.getStartDate()).isPresent()
                            ? getStartDateOfIST(activeSprint.getStartDate()) : Instant.now().toEpochMilli(),
                    Optional.ofNullable(activeSprint.getEndDate()).isPresent() ? activeSprint.getEndDate()
                            : Instant.now().plus(14, ChronoUnit.DAYS).toEpochMilli(),
                    new ObjectId(project.getId()));
        if (null == nestProjectMemberLeaves) {
            throw new RecordNotFoundException("project data not available");
        }
        Long activeSprintStartDate = Optional.ofNullable(activeSprint.getStartDate()).isPresent()
                ? activeSprint.getStartDate() : Instant.now().toEpochMilli();
        LocalDate startDate = Instant.ofEpochMilli(activeSprintStartDate).atZone(ZoneOffset.UTC).toLocalDate();

        Long activeSprintEndDate = Optional.ofNullable(activeSprint.getEndDate()).isPresent()
                ? activeSprint.getEndDate() : Instant.now().toEpochMilli();
        LocalDate endDate = Instant.ofEpochMilli(activeSprintEndDate).atZone(ZoneOffset.UTC).toLocalDate();
        return getLeavecalenderData(startDate, endDate, projectPublicHolidays, nestProjectMemberLeaves);
    }

    private LeaveCalenderDataDto getLeavecalenderData(LocalDate startDate, LocalDate endDate,
            List<PublicHolidays> projectPublicHolidays,
            List<NestProjectsIdWiseProjectMemberLeaveVo> nestProjectMemberLeaves) {
        LeaveCalenderDataDto leaveCalenderDataDto = new LeaveCalenderDataDto();
        LeaveCalenderDto leaveCalenderDto = new LeaveCalenderDto();
        List<LeaveDataDto> data = new ArrayList<>();
        List<ProjectMemberLeavesVo> projectMemberLeaves = new ArrayList<>();
        nestProjectMemberLeaves.forEach(nestProject -> {
            if (CollectionUtils.isNotEmpty(nestProject.getProjectMemberLeavesVo()))
                projectMemberLeaves.addAll(nestProject.getProjectMemberLeavesVo());
        });
        for (LocalDate date = startDate; date.isBefore(endDate.plusDays(1)); date = date.plusDays(1)) {
            Long leaveDate = date.atStartOfDay(ZoneOffset.UTC).toInstant().toEpochMilli();
            if ((DayOfWeek.SATURDAY == getDayOfWeekFromLongDate(leaveDate))
                    || (DayOfWeek.SUNDAY == getDayOfWeekFromLongDate(leaveDate))) {
                data.add(generateWeekendHolidayDto(leaveDate));
            }
            else {
                Boolean isHoliday = publicHolidayLeaveCount(projectPublicHolidays, data, leaveDate);
                if (isHoliday.equals(Boolean.FALSE)) {
                    memberLeaveCount(leaveDate, projectMemberLeaves, data);
                }
            }
        }
        leaveCalenderDto.setLeaveCalendar(data);
        leaveCalenderDataDto.setData(leaveCalenderDto);
        return leaveCalenderDataDto;
    }

    private void memberLeaveCount(Long leaveDate, List<ProjectMemberLeavesVo> projectMemberLeaves,
            List<LeaveDataDto> data) {
        List<Double> memberLeaveHours = new ArrayList<>();
        List<String> membersOnLeave = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(projectMemberLeaves)) {
            projectMemberLeaves.forEach(projectMemberLeave -> projectMemberLeave.getLeaves().forEach(leave -> {
                LocalDate memberLeave = Instant.ofEpochMilli(leave.getDate())
                    .atZone(ZoneId.of(INDIA_TIME_ZONE))
                    .toLocalDate();
                Long memberLeaveDate = memberLeave.atStartOfDay(ZoneOffset.UTC).toInstant().toEpochMilli();
                if (memberLeaveDate.equals(leaveDate)) {
                    memberLeaveHours.add(leave.getHour());
                    membersOnLeave.add(appendLeaveDuration(projectMemberLeave.getName(), leave.getHour()));
                }
            }));
        }
        data.add(generateMemberLeaveDto(leaveDate,
                (int) memberLeaveHours.stream().mapToDouble(memberLeaveHour -> memberLeaveHour).sum(), membersOnLeave));
    }

    private String appendLeaveDuration(String name, Double leaveHour) {
        return leaveHour == 8.0 ? name.concat("(F)") : name.concat("(H)");
    }

    private Boolean publicHolidayLeaveCount(List<PublicHolidays> projectPublicHolidays, List<LeaveDataDto> data,
            Long leaveDate) {
        Boolean holidayCheck = false;
        List<String> locations = new ArrayList<>();
        if (null != projectPublicHolidays) {
            projectPublicHolidays.forEach(publicHoliday -> {
                LocalDate holidayDateFormate = Instant.ofEpochMilli(publicHoliday.getDate())
                    .atZone(ZoneOffset.ofHours(+6))
                    .toLocalDate();
                Long publicHolidayDate = holidayDateFormate.atStartOfDay(ZoneOffset.UTC).toInstant().toEpochMilli();
                if (publicHolidayDate.equals(leaveDate))
                    locations.add(publicHoliday.getLocation());
            });
        }
        String holidayLocation = locations.stream().map(Object::toString).collect(Collectors.joining(", "));
        if (!holidayLocation.isEmpty()) {
            data.add(generatePublicHolidayDto(leaveDate, holidayLocation));
            holidayCheck = true;
        }
        return holidayCheck;
    }

    private LeaveDataDto generateMemberLeaveDto(Long leaveDate, int leaveHoursCount, List<String> membersOnLeave) {
        LeaveDataDto leaveDto;
        if (leaveHoursCount == 0) {
            leaveDto = LeaveDataDto.builder()
                .date(leaveDate)
                .isPublicHoliday(false)
                .publicHolidayHours(0)
                .isMemberLeave(false)
                .memberLeaveHours(leaveHoursCount)
                .membersOnLeave(membersOnLeave)
                .publicHolidayLocation(null)
                .build();
        }
        else {
            leaveDto = LeaveDataDto.builder()
                .date(leaveDate)
                .isPublicHoliday(false)
                .publicHolidayHours(0)
                .isMemberLeave(true)
                .memberLeaveHours(leaveHoursCount)
                .membersOnLeave(membersOnLeave)
                .publicHolidayLocation(null)
                .build();
        }
        return leaveDto;
    }

    private LeaveDataDto generatePublicHolidayDto(Long holidayDate, String holidayLocation) {
        return LeaveDataDto.builder()
            .date(holidayDate)
            .isPublicHoliday(true)
            .publicHolidayHours(8)
            .isMemberLeave(false)
            .memberLeaveHours(0)
            .publicHolidayLocation(0 != holidayLocation.length() ? holidayLocation : null)
            .build();
    }

    private LeaveDataDto generateWeekendHolidayDto(Long leaveDate) {
        return LeaveDataDto.builder()
            .date(leaveDate)
            .isPublicHoliday(false)
            .publicHolidayHours(0)
            .isMemberLeave(false)
            .memberLeaveHours(0)
            .publicHolidayLocation(null)
            .build();
    }

    public SprintMemberDetailsByIdDto getMemberDetailsById(String projectId, String memberId) {
        Project project = projectHelper.findAndValidateProjectByIdAndUser(projectId);
        validateAccess(GET_MEMBERS_DETAILS_BY_ID);
        Optional<NestMemberDetailsByMemberIdVo> memberDetailsByMemberIdVo = nestProjectMemberDao
            .getMemberDetailsByMemberId(project.getId(), memberId)
            .stream()
            .filter(nest -> CollectionUtils.isNotEmpty(nest.getProjectMembers()))
            .findFirst();
        if (!memberDetailsByMemberIdVo.isPresent()
                || CollectionUtils.isEmpty(memberDetailsByMemberIdVo.get().getProjectMembers())) {
            throw new RecordNotFoundException("No record available for member");
        }
        ProjectMemberLeaves projectMemberLeaves = projectMemberLeavesDao.getMemberLeavebyId(
                nestProjectsRepository.findByIscProjectId(new ObjectId(project.getId()))
                    .stream()
                    .map(nest -> new ObjectId(nest.getId()))
                    .toList(),
                LocalDate.of(LocalDate.now().getYear(), 01, 01)
                    .atTime(00, 00, 00)
                    .atZone(ZoneId.systemDefault())
                    .toInstant()
                    .toEpochMilli(),
                LocalDate.of(LocalDate.now().getYear() + 1, 01, 01)
                    .atTime(00, 00, 00)
                    .atZone(ZoneId.systemDefault())
                    .toInstant()
                    .toEpochMilli(),
                memberId);

        List<LeavesDto> leavesList = new ArrayList<>();
        if (null != projectMemberLeaves) {
            for (Leaves leaves : projectMemberLeaves.getLeaves()) {
                leavesList.add(LeavesDto.builder().date(leaves.getDate().toString()).build());
            }
        }
        ProjectMembersDetailsVo vo = memberDetailsByMemberIdVo.get().getProjectMembers().get(0);
        AllocationVo allocation = CollectionUtils.isNotEmpty(vo.getAllocation()) ? vo.getAllocation().get(0) : null;
        return SprintMemberDetailsByIdDto.builder()
            .name(vo.getName())
            .accountId(vo.getId())
            .email(vo.getEmail())
            .dp(vo.getDp())
            .designation(vo.getDesignation())
            .leaves(leavesList)
            .allocation(Allocation.builder()
                .billable(allocation != null ? allocation.getPercentage().toString() : null)
                .startDate(allocation != null ? DateTimeUtils.getLongDateTimeToString(allocation.getStartDate()) : null)
                .endDate(allocation != null ? DateTimeUtils.getLongDateTimeToString(allocation.getEndDate()) : null)
                .totalDays(allocation != null ? allocation.getTotalDays().toString() : null)
                .client(memberDetailsByMemberIdVo.get().getClientName())
                .project(memberDetailsByMemberIdVo.get().getName())
                .build())
            .build();
    }

    @Override
    public JiraBoardIdsDetailsDto getJiraBoardIds(String projectId) {
        Project project = projectHelper.findAndValidateProjectByIdAndUser(projectId);
        List<Boards> boards = boardsRepository
            .findByJiraProjectId(Integer.parseInt(getJiraConfiguration(project.getId()).getJiraProjectId()));
        if (CollectionUtils.isEmpty(boards)) {
            throw new RecordNotFoundException("No board record is available for this Id");
        }
        return JiraBoardIdsDetailsDto.builder()
            .data(boards.stream()
                .map(board -> BoardDetailsDto.builder()
                    .boardId(String.valueOf(board.getBoardId()))
                    .name(board.getName())
                    .build())
                .toList())
            .build();
    }

    private void validateAccess(String widget) {
        List<String> assignedWidgets = projectHelper.getWidget();
        if (!assignedWidgets.contains(widget)) {
            throw new RecordNotFoundException(WIDGET_VALIDATION_EXCEPTION_MESSAGE);
        }
    }

}
