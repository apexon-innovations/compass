package com.apexon.compass.psrservice.service.impl.helper;

import static com.apexon.compass.constants.PsrServiceConstants.BLOCKER_DEFINITION;
import static com.apexon.compass.constants.PsrServiceConstants.BUG_TYPES;
import static com.apexon.compass.constants.PsrServiceConstants.DEFINITION_OF_ACCEPTED;
import static com.apexon.compass.constants.PsrServiceConstants.DEFINITION_OF_DEV_COMPLETE;
import static com.apexon.compass.constants.PsrServiceConstants.DEFINITION_OF_DONE;
import static com.apexon.compass.constants.PsrServiceConstants.DEFINITION_OF_IN_PROGRESS;
import static com.apexon.compass.constants.PsrServiceConstants.DEFINITION_OF_QA_COMPLETE;
import static com.apexon.compass.constants.PsrServiceConstants.DEFINITION_OF_TODO;
import static com.apexon.compass.constants.PsrServiceConstants.JIRA_STAGES;
import static com.apexon.compass.constants.PsrServiceConstants.STORY_TYPES;
import static com.apexon.compass.constants.PsrServiceConstants.TASK_TYPES;
import static com.apexon.compass.utilities.StringOperationsUtils.concatString;

import com.apexon.compass.entities.JiraRules;
import com.apexon.compass.psrservice.repository.JiraRulesRepository;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.ListUtils;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor(onConstructor = @__({ @Autowired }))
@Slf4j
public class JiraRulesHelper {

    private JiraRulesRepository jiraRulesRepository;

    @Getter
    private Map<ObjectId, JiraRules> jiraRulesByJiraId;

    @PostConstruct
    public void cacheJiraRules() {
        ScheduledExecutorService scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
        scheduledExecutorService.scheduleAtFixedRate(() -> {
            log.info("Cached jira_rules at --------> {}", LocalDateTime.now(ZoneOffset.UTC));
            List<JiraRules> jiraRules = jiraRulesRepository.findAll();
            if (CollectionUtils.isNotEmpty(jiraRules)) {
                jiraRulesByJiraId = jiraRules.stream().collect(Collectors.toMap(JiraRules::getProjectId, rule -> rule));
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

    public String getDefinitionOfToDo(ObjectId projectId) {
        JiraRules jiraRules = jiraRulesByJiraId.getOrDefault(projectId, getDefaultJiraRule());
        return concatString(jiraRules.getDefinitionOfTodo());
    }

    public List<String> getDefinitionOfToDoInList(ObjectId projectId) {
        JiraRules jiraRules = jiraRulesByJiraId.getOrDefault(projectId, getDefaultJiraRule());
        return jiraRules.getDefinitionOfTodo();
    }

    public String getDefinitionOfInProgress(ObjectId projectId) {
        JiraRules jiraRules = jiraRulesByJiraId.getOrDefault(projectId, getDefaultJiraRule());
        return concatString(jiraRules.getDefinitionOfInProgress());
    }

    public List<String> getDefinitionOfInProgressInList(ObjectId projectId) {
        JiraRules jiraRules = jiraRulesByJiraId.getOrDefault(projectId, getDefaultJiraRule());
        return jiraRules.getDefinitionOfInProgress();
    }

    public String getDefinitionOfDone(ObjectId projectId) {
        JiraRules jiraRules = jiraRulesByJiraId.getOrDefault(projectId, getDefaultJiraRule());
        return concatString(jiraRules.getDefinitionOfDone());
    }

    public List<String> getDefinitionOfDoneinList(ObjectId projectId) {
        JiraRules jiraRules = jiraRulesByJiraId.getOrDefault(projectId, getDefaultJiraRule());
        return jiraRules.getDefinitionOfDone();
    }

    public String getDefinitionOfAccepted(ObjectId projectId) {
        JiraRules jiraRules = jiraRulesByJiraId.getOrDefault(projectId, getDefaultJiraRule());
        return concatString(jiraRules.getDefinitionOfAccepted());
    }

    public List<String> getDefinitionOfAcceptedinList(ObjectId projectId) {
        JiraRules jiraRules = jiraRulesByJiraId.getOrDefault(projectId, getDefaultJiraRule());
        return jiraRules.getDefinitionOfAccepted();
    }

    public String getDefinitionOfDevComplete(ObjectId projectId) {
        JiraRules jiraRules = jiraRulesByJiraId.getOrDefault(projectId, getDefaultJiraRule());
        return concatString(jiraRules.getDefinitionOfDevComplete());
    }

    public List<String> getDefinitionOfDevCompleteInList(ObjectId projectId) {
        JiraRules jiraRules = jiraRulesByJiraId.getOrDefault(projectId, getDefaultJiraRule());
        return jiraRules.getDefinitionOfDevComplete();
    }

    public List<String> getAllStatesByProjectIdInList(ObjectId projectId) {
        return Stream
            .of(getDefinitionOfToDoInList(projectId), getDefinitionOfInProgressInList(projectId),
                    getDefinitionOfDevCompleteInList(projectId), getDefinitionOfDoneinList(projectId),
                    getDefinitionOfAcceptedinList(projectId))
            .flatMap(Collection::stream)
            .toList();
    }

    public List<String> getStoryTaskTypesByProjectIdInList(ObjectId projectId) {
        JiraRules jiraRules = jiraRulesByJiraId.getOrDefault(projectId, getDefaultJiraRule());
        return ListUtils.union(jiraRules.getTaskTypes(), jiraRules.getStoryTypes());
    }

    public List<String> getStoryTaskBugTypesByProjectIdInList(ObjectId projectId) {
        JiraRules jiraRules = jiraRulesByJiraId.getOrDefault(projectId, getDefaultJiraRule());
        return Stream.of(jiraRules.getTaskTypes(), jiraRules.getStoryTypes(), jiraRules.getBugTypes())
            .flatMap(Collection::stream)
            .toList();
    }

    public List<String> getBugTypesByProjectIdInList(ObjectId projectId) {
        JiraRules jiraRules = jiraRulesByJiraId.getOrDefault(projectId, getDefaultJiraRule());
        return jiraRules.getBugTypes();
    }

    public String getInProgressByProjectId(ObjectId projectId) {
        return getDefinitionOfInProgress(projectId) + getDefinitionOfDevComplete(projectId);
    }

    public List<String> getInProgressByProjectIdInList(ObjectId projectId) {
        return ListUtils.union(getDefinitionOfInProgressInList(projectId), getDefinitionOfDevCompleteInList(projectId));
    }

    public List<String> getDefinitionOfBlockerInList(ObjectId projectId) {
        JiraRules jiraRules = jiraRulesByJiraId.getOrDefault(projectId, getDefaultJiraRule());
        return jiraRules.getBlockerDefinition();
    }

    public List<String> getStoryPointsCalculationIssuesByProjectIdInList(ObjectId projectId) {
        JiraRules jiraRules = jiraRulesByJiraId.getOrDefault(projectId, getDefaultJiraRule());
        return jiraRules.getStoryPointsCalculation();
    }

}
