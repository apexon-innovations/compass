package com.apexon.compass.clientdashboardservice.serviceImpl.helper;

import com.apexon.compass.clientdashboardservice.repository.JiraRulesRepository;
import com.apexon.compass.entities.JiraRules;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.apexon.compass.constants.PsrServiceConstants.*;

@Component
@AllArgsConstructor(onConstructor = @__({ @Autowired }))
@Slf4j
public class JiraRulesHelper {

    private JiraRulesRepository jiraRulesRepository;

    @Getter
    private Map<ObjectId, JiraRules> jiraRulesByJiraId;

    @PostConstruct
    private void cacheJiraRules() {
        ScheduledExecutorService scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
        scheduledExecutorService.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                log.info("Cached jira_rules at --------> {}", LocalDateTime.now(ZoneOffset.UTC));
                List<JiraRules> jiraRules = jiraRulesRepository.findAll();
                if (CollectionUtils.isNotEmpty(jiraRules)) {
                    jiraRulesByJiraId = jiraRules.stream()
                        .collect(Collectors.toMap(JiraRules::getProjectId, rule -> rule));
                }
            }
        }, 0, 5, TimeUnit.MINUTES);
    }

    private JiraRules getDefaultJiraRule() {
        return JiraRules.builder()
            .blockerDefinition(BLOCKER_DEFINITION)
            .bugTypes(BUG_TYPES)
            .definitionOfAccepted(DEFINITION_OF_ACCEPTED)
            .definitionOfDevComplete(DEFINITION_OF_DEV_COMPLETE)
            .definitionOfDone(DEFINITION_OF_DONE)
            .definitionOfInProgress(DEFINITION_OF_IN_PROGRESS)
            .definitionOfQaComplete(DEFINITION_OF_QA_COMPLETE)
            .definitionOfTodo(DEFINITION_OF_TODO)
            .storyTypes(STORY_TYPES)
            .taskTypes(TASK_TYPES)
            .jiraStages(JIRA_STAGES)
            .build();
    }

    public List<String> getCompletedByProjectId(List<ObjectId> projectIds) {
        List<String> rules = new ArrayList<>();
        for (ObjectId projectId : projectIds) {
            JiraRules jiraRules = jiraRulesByJiraId.getOrDefault(projectId, getDefaultJiraRule());
            for (String s : jiraRules.getDefinitionOfDone()) {
                rules.add(s);
            }
            for (String s : jiraRules.getDefinitionOfAccepted()) {
                rules.add(s);
            }
        }
        return rules;
    }

}
