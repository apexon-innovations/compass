package com.apexon.compass.onboardservice.service;

import com.apexon.compass.onboardservice.dto.*;

public interface OnboardService {

    JiraConfigResponse addJiraConfig(JiraConfigRequest jiraConfigRequest);

    JiraRulesResponse addJiraRule(JiraRulesRequest jiraRulesRequest);

    ScmConfigResponse addScmConfig(ScmConfigRequest scmConfigRequest);

    SonarConfigResponse addSonarConfig(SonarConfigRequest sonarConfigRequest);

    ProjectResponse addProject(ProjectRequest projectRequest);

}
