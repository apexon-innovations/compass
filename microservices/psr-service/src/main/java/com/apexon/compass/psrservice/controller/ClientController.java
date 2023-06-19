package com.apexon.compass.psrservice.controller;

import static com.apexon.compass.constants.PsrServiceRouteConstants.CLIENT_SPRINT_DETAILS;
import static com.apexon.compass.constants.PsrServiceRouteConstants.PROJECTID_COMPLETED_SPRINT;
import static com.apexon.compass.constants.PsrServiceRouteConstants.PROJECTID_COMPLETION_RATE;
import static com.apexon.compass.constants.PsrServiceRouteConstants.PROJECTID_CURRENT_SPRINT_PROGRESS;
import static com.apexon.compass.constants.PsrServiceRouteConstants.PROJECTID_DELIVERED_ACCEPTED;
import static com.apexon.compass.constants.PsrServiceRouteConstants.PROJECTID_FEATURE_STAGES;
import static com.apexon.compass.constants.PsrServiceRouteConstants.PROJECTID_TEAM_VELOCITY;

import com.apexon.compass.psrservice.dto.CompletionRateDto;
import com.apexon.compass.psrservice.dto.FeatureStagesDto;
import com.apexon.compass.psrservice.dto.SprintProgressDto;
import com.apexon.compass.psrservice.dto.TeamVelocityDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import com.apexon.compass.exception.handler.ApiExceptionHandler;
import com.apexon.compass.psrservice.dto.CompletedSprintDto;
import com.apexon.compass.psrservice.dto.DeliveredAcceptedDto;
import com.apexon.compass.psrservice.service.JiraService;
import com.apexon.compass.psrservice.service.StoriesService;
import lombok.AllArgsConstructor;

@RestController
@RequestMapping(value = CLIENT_SPRINT_DETAILS)
@AllArgsConstructor(onConstructor = @__({ @Autowired }))
public class ClientController extends ApiExceptionHandler {

    private JiraService jiraService;

    private StoriesService storiesService;

    @GetMapping(path = PROJECTID_TEAM_VELOCITY)
    @ResponseStatus(value = HttpStatus.OK)
    public TeamVelocityDto getTeamVelocity(@PathVariable("jiraProjectId") String jiraProjectId) {
        return jiraService.getTeamVelocity(jiraProjectId);
    }

    @GetMapping(path = PROJECTID_COMPLETION_RATE)
    @ResponseStatus(value = HttpStatus.OK)
    public CompletionRateDto getCompletionRate(@PathVariable("jiraProjectId") String jiraProjectId) {
        return jiraService.getCompletionRate(jiraProjectId);
    }

    @GetMapping(path = PROJECTID_COMPLETED_SPRINT)
    @ResponseStatus(value = HttpStatus.OK)
    public CompletedSprintDto getCompletedSprintRate(@PathVariable("jiraProjectId") String jiraProjectId) {
        return jiraService.getCompletedSprintRate(jiraProjectId);
    }

    @GetMapping(path = PROJECTID_CURRENT_SPRINT_PROGRESS)
    @ResponseStatus(value = HttpStatus.OK)
    public SprintProgressDto getCurrentSprintProgress(@PathVariable("jiraProjectId") String jiraProjectId) {
        return storiesService.getCurrentSprintProgress(jiraProjectId);
    }

    @GetMapping(path = PROJECTID_FEATURE_STAGES)
    @ResponseStatus(value = HttpStatus.OK)
    public FeatureStagesDto getFeaturesAtDifferentStage(@PathVariable("jiraProjectId") String jiraProjectId) {
        return storiesService.getFeaturesAtDiffrentStage(jiraProjectId);
    }

    @GetMapping(path = PROJECTID_DELIVERED_ACCEPTED)
    @ResponseStatus(value = HttpStatus.OK)
    public DeliveredAcceptedDto getAcceptedDeliveredFeatures(@PathVariable("jiraProjectId") String jiraProjectId) {
        return storiesService.getAcceptedDeliveredFeatures(jiraProjectId);
    }

}
