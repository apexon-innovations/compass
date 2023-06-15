package com.apexon.compass.onboardservice.controller;

import com.apexon.compass.exception.handler.ApiExceptionHandler;
import com.apexon.compass.onboardservice.dto.*;
import com.apexon.compass.onboardservice.service.OnboardService;
import jakarta.annotation.security.RolesAllowed;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/onboard")
@RequiredArgsConstructor
public class OnboardController extends ApiExceptionHandler {

    private final OnboardService onboardService;

    @PostMapping("/project")
    @RolesAllowed({ "admin", "manager" })
    @ResponseStatus(HttpStatus.CREATED)
    public ProjectResponse addProject(@Valid @RequestBody ProjectRequest projectRequest) {
        return onboardService.addProject(projectRequest);
    }

    @PostMapping("/config/jira")
    @RolesAllowed({ "admin", "manager" })
    @ResponseStatus(HttpStatus.CREATED)
    public JiraConfigResponse addJiraConfig(@Valid @RequestBody JiraConfigRequest jiraConfigRequest) {
        return onboardService.addJiraConfig(jiraConfigRequest);
    }

    @PostMapping("/rules/jira")
    @RolesAllowed({ "admin", "manager" })
    @ResponseStatus(HttpStatus.CREATED)
    public JiraRulesResponse addJiraRule(@Valid @RequestBody JiraRulesRequest jiraRulesRequest) {
        return onboardService.addJiraRule(jiraRulesRequest);
    }

    @PostMapping("/config/scm")
    @RolesAllowed({ "admin", "manager" })
    @ResponseStatus(HttpStatus.CREATED)
    public ScmConfigResponse addScmConfig(@Valid @RequestBody ScmConfigRequest scmConfigRequest) {
        return onboardService.addScmConfig(scmConfigRequest);
    }

    @PostMapping("/config/sonar")
    @RolesAllowed({ "admin", "manager" })
    @ResponseStatus(HttpStatus.CREATED)
    public SonarConfigResponse addSonarConfig(@Valid @RequestBody SonarConfigRequest sonarConfigRequest) {
        return onboardService.addSonarConfig(sonarConfigRequest);
    }

}
