package com.apexon.compass.psrservice.service;

import com.apexon.compass.entities.StatusData;
import com.apexon.compass.exception.custom.ForbiddenException;
import com.apexon.compass.exception.custom.RecordNotFoundException;
import com.apexon.compass.exception.custom.UnauthorizedException;
import com.apexon.compass.psrservice.dto.*;

import java.text.ParseException;
import java.util.List;
import lombok.NonNull;

public interface JiraService {

    TeamVelocityDto getTeamVelocity(String projectId);

    List<TeamDto> getAccumulatedTeamData(List<StatusData> statusList);

    SprintBurndownDto getSprintBurndown(String projectId);

    PointsCompletionDto getStoryPointCompletionStatus(String projectId);

    CompletionRateDto getCompletionRate(String projectId);

    CompletedSprintDto getCompletedSprintRate(String projectId);

    DailyAcceptedDeliveredDto getDailyAcceptedDelivered(String jiraProjectId);

    SprintOverviewDto getSprintOverview(String id, Integer boardId)
            throws UnauthorizedException, ForbiddenException, RecordNotFoundException;

    StatusDto getStatusByProject(String id, Integer boardId)
            throws UnauthorizedException, ForbiddenException, RecordNotFoundException;

    DefectsDto getBugs(String id, Integer boardId)
            throws UnauthorizedException, ForbiddenException, RecordNotFoundException;

    SprintLoggedVsAcceptedBugsDto getLoggedVsAcceptedBugs(String id, Integer boardId)
            throws UnauthorizedException, ForbiddenException, RecordNotFoundException;

    ProjectDataWithBlockersDto getBlockers(String id, Integer boardId)
            throws UnauthorizedException, ForbiddenException, RecordNotFoundException;

    SprintEffortVarianceDto getEffortVariance(String projectId, Integer boardId);

    SprintMemberStatusDto getMemberStatus(String id, Integer boardId)
            throws UnauthorizedException, ForbiddenException, RecordNotFoundException;

    SprintBurndownChartDto getSprintBurndownChart(String id, Integer boardId)
            throws UnauthorizedException, ForbiddenException, RecordNotFoundException, ParseException;

    SprintAcceptedVsDeliveredDtos getAcceptedVsDelivered(String id, Integer boardId)
            throws UnauthorizedException, ForbiddenException, RecordNotFoundException;

    SprintStoryReportDto getStoryReport(String projectId, Integer boardId);

    SprintMemberStatusByIdDto getMemberStatusById(String id, String memberId, Integer boardId);

    SayDoListDto getSayDoRatio(String id, Integer boardId);

    LeaveCalenderDataDto getLeaveCalenderBySprintId(String projectId, Integer boardId);

    SprintMemberDetailsByIdDto getMemberDetailsById(@NonNull String id, @NonNull String memberId);

    JiraBoardIdsDetailsDto getJiraBoardIds(String projectId);

}
