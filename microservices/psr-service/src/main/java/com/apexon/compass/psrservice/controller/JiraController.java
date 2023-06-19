package com.apexon.compass.psrservice.controller;

import static com.apexon.compass.constants.PsrServiceRouteConstants.JIRA_DETAILS;
import static com.apexon.compass.constants.PsrServiceRouteConstants.PROJECTID_DELIVERED_ACCEPTED;
import static com.apexon.compass.constants.PsrServiceRouteConstants.PROJECTID_POINTS_COMPLETION;
import static com.apexon.compass.constants.PsrServiceRouteConstants.PROJECTID_SPRINT_BURNDOWN;
import static com.apexon.compass.constants.PsrServiceRouteConstants.PROJECTID_TEAM_VELOCITY;

import com.apexon.compass.psrservice.dto.PointsCompletionDto;
import com.apexon.compass.psrservice.dto.SprintBurndownDto;
import com.apexon.compass.psrservice.dto.TeamVelocityDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import com.apexon.compass.exception.handler.ApiExceptionHandler;
import com.apexon.compass.psrservice.dto.DailyAcceptedDeliveredDto;
import com.apexon.compass.psrservice.service.JiraService;
import lombok.AllArgsConstructor;

@RestController
@RequestMapping(JIRA_DETAILS)
@AllArgsConstructor(onConstructor = @__({ @Autowired }))
public class JiraController extends ApiExceptionHandler {

    private JiraService jiraService;

    @GetMapping(path = PROJECTID_TEAM_VELOCITY)
    @ResponseStatus(value = HttpStatus.OK)
    public TeamVelocityDto getTeamVelocity(@PathVariable("jiraProjectId") String jiraProjectId) {
        return jiraService.getTeamVelocity(jiraProjectId);
    }

    @GetMapping(path = PROJECTID_SPRINT_BURNDOWN)
    @ResponseStatus(value = HttpStatus.OK)
    public SprintBurndownDto getJiraDetails(@PathVariable("jiraProjectId") String jiraProjectId) {
        return jiraService.getSprintBurndown(jiraProjectId);
    }

    @GetMapping(path = PROJECTID_POINTS_COMPLETION)
    @ResponseStatus(value = HttpStatus.OK)
    public PointsCompletionDto getStoryPointCompletionStatus(@PathVariable("jiraProjectId") String jiraProjectId) {
        return jiraService.getStoryPointCompletionStatus(jiraProjectId);
    }

    @GetMapping(path = PROJECTID_DELIVERED_ACCEPTED)
    @ResponseStatus(value = HttpStatus.OK)
    public DailyAcceptedDeliveredDto getDailyAcceptedDelivered(@PathVariable("jiraProjectId") String jiraProjectId) {
        return jiraService.getDailyAcceptedDelivered(jiraProjectId);
    }

}
