package com.apexon.compass.dashboard.model;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Document(collection = "jira_configuration")
public class JiraConfiguration extends Collector {

	private ObjectId iscProjectId;

	private String jiraProjectId;

	private List<String> jiraBoardId;

	private String url;

	private String queryEndPoint;

	private Long projectStartDate;

	private String jiraApiKey;

	private List<String> issueTypes;

	private String sprintDataFieldName;

	private String jiraEpicIdFieldName;

	private String jiraStoryPointsFieldName;

	private boolean jiraBoardAsTeam;

	private Map<String, String> issueTypesId = new HashMap<>();

	private String cron;

	private String createdBy;

	private Long createdDate;

	private String updatedBy;

	private Long updatedDate;

	// Added Extra Default Fields
	private int maxNumberOfFeaturesPerBoard = 10000;

	private String jiraTeamFieldName = "";

	private int firstRunHistoryDays = 7200;

	private String jiraProxyUrl = "";

	private String jiraProxyPort = "";

	private int refreshTeamAndProjectHours = 3;

	private boolean collectorItemOnlyUpdate = false;

	public ObjectId getIscProjectId() {
		return iscProjectId;
	}

	public String getJiraProjectId() {
		return jiraProjectId;
	}

	public List<String> getJiraBoardId() {
		return jiraBoardId;
	}

	public String getUrl() {
		return url;
	}

	public String getQueryEndPoint() {
		return queryEndPoint;
	}

	public Long getProjectStartDate() {
		return projectStartDate;
	}

	public String getJiraApiKey() {
		return jiraApiKey;
	}

	public List<String> getIssueTypes() {
		return issueTypes;
	}

	public String getSprintDataFieldName() {
		return sprintDataFieldName;
	}

	public String getJiraEpicIdFieldName() {
		return jiraEpicIdFieldName;
	}

	public String getJiraStoryPointsFieldName() {
		return jiraStoryPointsFieldName;
	}

	public boolean isJiraBoardAsTeam() {
		return jiraBoardAsTeam;
	}

	public String getCron() {
		return cron;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public Long getCreatedDate() {
		return createdDate;
	}

	public String getUpdatedBy() {
		return updatedBy;
	}

	public Long getUpdatedDate() {
		return updatedDate;
	}

	public int getMaxNumberOfFeaturesPerBoard() {
		return maxNumberOfFeaturesPerBoard;
	}

	public String getJiraTeamFieldName() {
		return jiraTeamFieldName;
	}

	public int getFirstRunHistoryDays() {
		return firstRunHistoryDays;
	}

	public String getJiraProxyUrl() {
		return jiraProxyUrl;
	}

	public String getJiraProxyPort() {
		return jiraProxyPort;
	}

	public int getRefreshTeamAndProjectHours() {
		return refreshTeamAndProjectHours;
	}

	public boolean isCollectorItemOnlyUpdate() {
		return collectorItemOnlyUpdate;
	}

	public Map<String, String> getIssueTypesId() {
		return issueTypesId;
	}

	public void setIssueTypesId(Map<String, String> issueTypesId) {
		this.issueTypesId = issueTypesId;
	}

	public void setIscProjectId(ObjectId iscProjectId) {
		this.iscProjectId = iscProjectId;
	}

	public void setJiraProjectId(String jiraProjectId) {
		this.jiraProjectId = jiraProjectId;
	}

	public void setJiraBoardId(List<String> jiraBoardId) {
		this.jiraBoardId = jiraBoardId;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public void setQueryEndPoint(String queryEndPoint) {
		this.queryEndPoint = queryEndPoint;
	}

	public void setProjectStartDate(Long projectStartDate) {
		this.projectStartDate = projectStartDate;
	}

	public void setJiraApiKey(String jiraApiKey) {
		this.jiraApiKey = jiraApiKey;
	}

	public void setIssueTypes(List<String> issueTypes) {
		this.issueTypes = issueTypes;
	}

	public void setSprintDataFieldName(String sprintDataFieldName) {
		this.sprintDataFieldName = sprintDataFieldName;
	}

	public void setJiraEpicIdFieldName(String jiraEpicIdFieldName) {
		this.jiraEpicIdFieldName = jiraEpicIdFieldName;
	}

	public void setJiraStoryPointsFieldName(String jiraStoryPointsFieldName) {
		this.jiraStoryPointsFieldName = jiraStoryPointsFieldName;
	}

	public void setJiraBoardAsTeam(boolean jiraBoardAsTeam) {
		this.jiraBoardAsTeam = jiraBoardAsTeam;
	}

	public void setCron(String cron) {
		this.cron = cron;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public void setCreatedDate(Long createdDate) {
		this.createdDate = createdDate;
	}

	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}

	public void setUpdatedDate(Long updatedDate) {
		this.updatedDate = updatedDate;
	}

	public void setMaxNumberOfFeaturesPerBoard(int maxNumberOfFeaturesPerBoard) {
		this.maxNumberOfFeaturesPerBoard = maxNumberOfFeaturesPerBoard;
	}

	public void setJiraTeamFieldName(String jiraTeamFieldName) {
		this.jiraTeamFieldName = jiraTeamFieldName;
	}

	public void setFirstRunHistoryDays(int firstRunHistoryDays) {
		this.firstRunHistoryDays = firstRunHistoryDays;
	}

	public void setJiraProxyUrl(String jiraProxyUrl) {
		this.jiraProxyUrl = jiraProxyUrl;
	}

	public void setJiraProxyPort(String jiraProxyPort) {
		this.jiraProxyPort = jiraProxyPort;
	}

	public void setRefreshTeamAndProjectHours(int refreshTeamAndProjectHours) {
		this.refreshTeamAndProjectHours = refreshTeamAndProjectHours;
	}

	public void setCollectorItemOnlyUpdate(boolean collectorItemOnlyUpdate) {
		this.collectorItemOnlyUpdate = collectorItemOnlyUpdate;
	}

}
