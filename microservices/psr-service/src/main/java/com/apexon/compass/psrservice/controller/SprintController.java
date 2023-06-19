package com.apexon.compass.psrservice.controller;

import static com.apexon.compass.constants.PsrServiceConstants.ID_CAPITALIZED;
import static com.apexon.compass.constants.PsrServiceConstants.PROJECT__ID;
import static com.apexon.compass.constants.PsrServiceConstants.MEMBER_ID;
import static com.apexon.compass.constants.PsrServiceConstants.PROJECT_ID_CAPITALIZED;
import static com.apexon.compass.constants.PsrServiceConstants.PROJECT_ID_VALIDATION_MESSAGE;
import static com.apexon.compass.constants.PsrServiceConstants.PROJECT_ID_VALIDATION_REGEX;
import static com.apexon.compass.constants.PsrServiceRouteConstants.BLOCKERS_BY_PROJECT_ID;
import static com.apexon.compass.constants.PsrServiceRouteConstants.EFFORT_VARIANCE_BY_PROJECT_ID;
import static com.apexon.compass.constants.PsrServiceRouteConstants.GET_LEAVE_CALENDER_SPRINT_ID;
import static com.apexon.compass.constants.PsrServiceRouteConstants.PROJECT;
import static com.apexon.compass.constants.PsrServiceRouteConstants.PROJECTID_SPRINT_STATUS;
import static com.apexon.compass.constants.PsrServiceRouteConstants.SPRINTLOGGEDVSACCEPTED_BUGS;
import static com.apexon.compass.constants.PsrServiceRouteConstants.SPRINT_ACCEPTED_VS_DELIVERED;
import static com.apexon.compass.constants.PsrServiceRouteConstants.SPRINT_BUGS;
import static com.apexon.compass.constants.PsrServiceRouteConstants.SPRINT_BURNDOWN_CHART;
import static com.apexon.compass.constants.PsrServiceRouteConstants.SPRINT_MEMBER_DETAILS_BY_MEMBERID;
import static com.apexon.compass.constants.PsrServiceRouteConstants.SPRINT_MEMBER_STATUS;
import static com.apexon.compass.constants.PsrServiceRouteConstants.SPRINT_MEMBER_STATUS_BY_MEMBERID;
import static com.apexon.compass.constants.PsrServiceRouteConstants.SPRINT_OVERVIEW;
import static com.apexon.compass.constants.PsrServiceRouteConstants.SPRINT_REPORT;
import static com.apexon.compass.constants.PsrServiceRouteConstants.SPRINT_SAY_DO_RATIO;
import static com.apexon.compass.constants.PsrServiceRouteConstants.PROJECTID_JIRA_BOARDS;

import java.text.ParseException;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

import com.apexon.compass.psrservice.dto.DefectsDto;
import com.apexon.compass.psrservice.dto.LeaveCalenderDataDto;
import com.apexon.compass.psrservice.dto.ProjectDataWithBlockersDto;
import com.apexon.compass.psrservice.dto.SayDoListDto;
import com.apexon.compass.psrservice.dto.SprintAcceptedVsDeliveredDtos;
import com.apexon.compass.psrservice.dto.SprintBurndownChartDto;
import com.apexon.compass.psrservice.dto.SprintEffortVarianceDto;
import com.apexon.compass.psrservice.dto.SprintLoggedVsAcceptedBugsDto;
import com.apexon.compass.psrservice.dto.SprintMemberDetailsByIdDto;
import com.apexon.compass.psrservice.dto.SprintMemberStatusByIdDto;
import com.apexon.compass.psrservice.dto.SprintMemberStatusDto;
import com.apexon.compass.psrservice.dto.SprintOverviewDto;
import com.apexon.compass.psrservice.dto.SprintStoryReportDto;
import com.apexon.compass.psrservice.dto.StatusDto;
import com.apexon.compass.psrservice.dto.JiraBoardIdsDetailsDto;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestParam;
import com.apexon.compass.exception.custom.ForbiddenException;
import com.apexon.compass.exception.custom.RecordNotFoundException;
import com.apexon.compass.exception.custom.UnauthorizedException;
import com.apexon.compass.exception.handler.ApiExceptionHandler;
import com.apexon.compass.psrservice.service.JiraService;
import lombok.AllArgsConstructor;
import lombok.NonNull;

@RestController
@RequestMapping(value = PROJECT)
@AllArgsConstructor
@Validated
public class SprintController extends ApiExceptionHandler {

    private JiraService jiraService;

    @GetMapping(path = SPRINT_OVERVIEW)
    public SprintOverviewDto getSprintOverview(
            @Pattern(regexp = PROJECT_ID_VALIDATION_REGEX,
                    message = PROJECT_ID_VALIDATION_MESSAGE) @PathVariable(PROJECT_ID_CAPITALIZED) String projectId,
            @RequestParam(required = false) Integer boardId)
            throws UnauthorizedException, ForbiddenException, RecordNotFoundException {
        return jiraService.getSprintOverview(projectId, boardId);
    }

    @GetMapping(path = PROJECTID_SPRINT_STATUS)
    public StatusDto getStatusByProject(
            @Pattern(regexp = PROJECT_ID_VALIDATION_REGEX,
                    message = PROJECT_ID_VALIDATION_MESSAGE) @NotNull @PathVariable(ID_CAPITALIZED) String projectId,
            @RequestParam(required = false) Integer boardId)
            throws UnauthorizedException, ForbiddenException, RecordNotFoundException {
        return jiraService.getStatusByProject(projectId, boardId);
    }

    @GetMapping(path = SPRINT_BUGS)
    public DefectsDto getBugs(
            @Pattern(regexp = PROJECT_ID_VALIDATION_REGEX,
                    message = PROJECT_ID_VALIDATION_MESSAGE) @PathVariable(PROJECT_ID_CAPITALIZED) String projectId,
            @RequestParam(required = false) Integer boardId)
            throws UnauthorizedException, ForbiddenException, RecordNotFoundException {
        return jiraService.getBugs(projectId, boardId);
    }

    @GetMapping(path = SPRINTLOGGEDVSACCEPTED_BUGS)
    public SprintLoggedVsAcceptedBugsDto getLoggedVsAcceptedBugs(
            @Pattern(regexp = PROJECT_ID_VALIDATION_REGEX,
                    message = PROJECT_ID_VALIDATION_MESSAGE) @PathVariable(PROJECT_ID_CAPITALIZED) String projectId,
            @RequestParam(required = false) Integer boardId)
            throws UnauthorizedException, ForbiddenException, RecordNotFoundException {
        return jiraService.getLoggedVsAcceptedBugs(projectId, boardId);
    }

    @GetMapping(path = BLOCKERS_BY_PROJECT_ID)
    public ProjectDataWithBlockersDto getBlockers(
            @Pattern(regexp = PROJECT_ID_VALIDATION_REGEX,
                    message = PROJECT_ID_VALIDATION_MESSAGE) @NonNull @PathVariable(ID_CAPITALIZED) String projectId,
            @RequestParam(required = false) Integer boardId)
            throws UnauthorizedException, RecordNotFoundException, ForbiddenException {
        return jiraService.getBlockers(projectId, boardId);
    }

    @GetMapping(path = EFFORT_VARIANCE_BY_PROJECT_ID)
    public SprintEffortVarianceDto getEffortVariance(
            @Pattern(regexp = PROJECT_ID_VALIDATION_REGEX,
                    message = PROJECT_ID_VALIDATION_MESSAGE) @PathVariable(PROJECT_ID_CAPITALIZED) String projectId,
            @RequestParam(required = false) Integer boardId) {
        return jiraService.getEffortVariance(projectId, boardId);
    }

    @GetMapping(path = SPRINT_ACCEPTED_VS_DELIVERED)
    public SprintAcceptedVsDeliveredDtos getAcceptedVsDelivered(
            @Pattern(regexp = PROJECT_ID_VALIDATION_REGEX,
                    message = PROJECT_ID_VALIDATION_MESSAGE) @PathVariable(PROJECT__ID) String projectId,
            @RequestParam(required = false) Integer boardId)
            throws UnauthorizedException, ForbiddenException, RecordNotFoundException {
        return jiraService.getAcceptedVsDelivered(projectId, boardId);
    }

    @GetMapping(path = SPRINT_MEMBER_STATUS)
    public SprintMemberStatusDto getMemberStatus(@Pattern(regexp = PROJECT_ID_VALIDATION_REGEX,
            message = PROJECT_ID_VALIDATION_MESSAGE) @NonNull @PathVariable(PROJECT_ID_CAPITALIZED) String projectId,
            @RequestParam(required = false) Integer boardId)
            throws UnauthorizedException, RecordNotFoundException, ForbiddenException {
        return jiraService.getMemberStatus(projectId, boardId);
    }

    @GetMapping(path = SPRINT_REPORT)
    public SprintStoryReportDto getStoryReport(
            @Pattern(regexp = PROJECT_ID_VALIDATION_REGEX,
                    message = PROJECT_ID_VALIDATION_MESSAGE) @PathVariable(PROJECT_ID_CAPITALIZED) String projectId,
            @RequestParam(required = false) Integer boardId) {
        return jiraService.getStoryReport(projectId, boardId);
    }

    @GetMapping(path = SPRINT_MEMBER_STATUS_BY_MEMBERID)
    public SprintMemberStatusByIdDto getMemberStatusById(@Pattern(regexp = PROJECT_ID_VALIDATION_REGEX,
            message = PROJECT_ID_VALIDATION_MESSAGE) @NonNull @PathVariable(PROJECT_ID_CAPITALIZED) String projectId,
            @NonNull @PathVariable(MEMBER_ID) String memberId, @RequestParam(required = false) Integer boardId)
            throws UnauthorizedException, RecordNotFoundException, ForbiddenException {
        return jiraService.getMemberStatusById(projectId, memberId, boardId);
    }

    @GetMapping(path = SPRINT_BURNDOWN_CHART)
    public SprintBurndownChartDto getSprintBurnDownChart(
            @Pattern(regexp = PROJECT_ID_VALIDATION_REGEX,
                    message = PROJECT_ID_VALIDATION_MESSAGE) @NonNull @PathVariable(PROJECT__ID) String projectId,
            @RequestParam(required = false) Integer boardId)
            throws UnauthorizedException, RecordNotFoundException, ForbiddenException, ParseException {
        return jiraService.getSprintBurndownChart(projectId, boardId);
    }

    @GetMapping(path = SPRINT_SAY_DO_RATIO)
    public SayDoListDto getSayDoRatio(
            @Pattern(regexp = PROJECT_ID_VALIDATION_REGEX,
                    message = PROJECT_ID_VALIDATION_MESSAGE) @PathVariable(PROJECT_ID_CAPITALIZED) String projectId,
            @RequestParam(required = false) Integer boardId) {
        return jiraService.getSayDoRatio(projectId, boardId);
    }

    @GetMapping(path = GET_LEAVE_CALENDER_SPRINT_ID)
    public LeaveCalenderDataDto getLeaveCalenderBySprintId(@Pattern(regexp = PROJECT_ID_VALIDATION_REGEX,
            message = PROJECT_ID_VALIDATION_MESSAGE) @NonNull @PathVariable(PROJECT_ID_CAPITALIZED) String projectId,
            @RequestParam(required = false) Integer boardId) {
        return jiraService.getLeaveCalenderBySprintId(projectId, boardId);
    }

    @GetMapping(path = SPRINT_MEMBER_DETAILS_BY_MEMBERID)
    public SprintMemberDetailsByIdDto getMemberDetailsById(@Pattern(regexp = PROJECT_ID_VALIDATION_REGEX,
            message = PROJECT_ID_VALIDATION_MESSAGE) @NonNull @PathVariable(PROJECT_ID_CAPITALIZED) String projectId,
            @NonNull @PathVariable(MEMBER_ID) String memberId) {
        return jiraService.getMemberDetailsById(projectId, memberId);
    }

    @GetMapping(path = PROJECTID_JIRA_BOARDS)
    public JiraBoardIdsDetailsDto getJiraBoardIds(@Pattern(regexp = PROJECT_ID_VALIDATION_REGEX,
            message = PROJECT_ID_VALIDATION_MESSAGE) @NonNull @PathVariable(PROJECT_ID_CAPITALIZED) String projectId) {
        return jiraService.getJiraBoardIds(projectId);
    }

}
