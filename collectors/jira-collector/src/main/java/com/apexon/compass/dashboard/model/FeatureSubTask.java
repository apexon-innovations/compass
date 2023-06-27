package com.apexon.compass.dashboard.model;

/**
 * Represents an sub-task of a Jira story.
 */
public class FeatureSubTask {

	/*
	 * Issue data
	 */
	private String issueId;

	private String issueKey;

	private String issueUrl;

	private String issueSummary;

	/*
	 * Status data
	 */
	private String statusId;

	private String statusName;

	private String statusCategoryId;

	private String statusCategoryKey;

	private String statusCategoryName;

	/*
	 * Priority data
	 */
	private String priorityId;

	private String priorityName;

	/*
	 * Issue-Type data
	 */
	private String issueTypeName;

	private String issueTypeId;

	public String getIssueKey() {
		return issueKey;
	}

	public void setIssueKey(String issueKey) {
		this.issueKey = issueKey;
	}

	public String getIssueUrl() {
		return issueUrl;
	}

	public void setIssueUrl(String issueUrl) {
		this.issueUrl = issueUrl;
	}

	public String getIssueSummary() {
		return issueSummary;
	}

	public void setIssueSummary(String issueSummary) {
		this.issueSummary = issueSummary;
	}

	public String getStatusName() {
		return statusName;
	}

	public void setStatusName(String statusName) {
		this.statusName = statusName;
	}

	public String getStatusId() {
		return statusId;
	}

	public void setStatusId(String statusId) {
		this.statusId = statusId;
	}

	public String getStatusCategoryKey() {
		return statusCategoryKey;
	}

	public void setStatusCategoryKey(String statusCategoryKey) {
		this.statusCategoryKey = statusCategoryKey;
	}

	public String getStatusCategoryId() {
		return statusCategoryId;
	}

	public void setStatusCategoryId(String statusCategoryId) {
		this.statusCategoryId = statusCategoryId;
	}

	public String getStatusCategoryName() {
		return statusCategoryName;
	}

	public void setStatusCategoryName(String statusCategoryName) {
		this.statusCategoryName = statusCategoryName;
	}

	public String getPriorityName() {
		return priorityName;
	}

	public void setPriorityName(String priorityName) {
		this.priorityName = priorityName;
	}

	public String getPriorityId() {
		return priorityId;
	}

	public void setPriorityId(String priorityId) {
		this.priorityId = priorityId;
	}

	public String getIssueTypeName() {
		return issueTypeName;
	}

	public void setIssueTypeName(String issueTypeName) {
		this.issueTypeName = issueTypeName;
	}

	public String getIssueTypeId() {
		return issueTypeId;
	}

	public void setIssueTypeId(String issueTypeId) {
		this.issueTypeId = issueTypeId;
	}

	public String getIssueId() {
		return issueId;
	}

	public void setIssueId(String issueId) {
		this.issueId = issueId;
	}

}
