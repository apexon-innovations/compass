package com.apexon.compass.dashboard.collector;

import com.apexon.compass.dashboard.model.*;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface JiraClient {

	FeatureEpicResult getIssues(JiraBoard board, Map<String, String> issueTypeIds, JiraConfiguration jiraConfiguration);

	FeatureEpicResult getIssues(Scope project, Map<String, String> issueTypeIds, JiraConfiguration jiraConfiguration);

	Set<Scope> getProjects(JiraConfiguration jiraConfiguration);

	List<JiraBoard> getBoards(JiraConfiguration jiraConfiguration);

	List<JiraBoard> getTeams(JiraConfiguration jiraConfiguration);

	Epic getEpic(String epicKey, Map<String, Epic> epicMap, JiraConfiguration jiraConfiguration);

	List<String> getAllIssueIds(String id, JiraMode mode, JiraConfiguration jiraConfiguration);

	Map<String, String> getJiraIssueTypeIds(JiraConfiguration jiraConfiguration);

}
