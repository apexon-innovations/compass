package com.apexon.compass.dashboard.model;

import java.util.List;

public class DesGitRequest extends GitRequest {

	private String sourceRepoId;

	private String targetRepoId;

	private String closedAuthor;

	private String closedAuthorLDAPDN;

	private String iscProjectId;

	private String scmConfigurationId;

	private List<Reviewer> reviewers;

	private List<Approver> approvers;

	public String getSourceRepoId() {
		return sourceRepoId;
	}

	public void setSourceRepoId(String sourceRepoId) {
		this.sourceRepoId = sourceRepoId;
	}

	public String getTargetRepoId() {
		return targetRepoId;
	}

	public void setTargetRepoId(String targetRepoId) {
		this.targetRepoId = targetRepoId;
	}

	public String getClosedAuthor() {
		return closedAuthor;
	}

	public void setClosedAuthor(String closedAuthor) {
		this.closedAuthor = closedAuthor;
	}

	public String getClosedAuthorLDAPDN() {
		return closedAuthorLDAPDN;
	}

	public void setClosedAuthorLDAPDN(String closedAuthorLDAPDN) {
		this.closedAuthorLDAPDN = closedAuthorLDAPDN;
	}

	public String getIscProjectId() {
		return iscProjectId;
	}

	public void setIscProjectId(String iscProjectId) {
		this.iscProjectId = iscProjectId;
	}

	public String getScmConfigurationId() {
		return scmConfigurationId;
	}

	public void setScmConfigurationId(String scmConfigurationId) {
		this.scmConfigurationId = scmConfigurationId;
	}

	public List<Reviewer> getReviewers() {
		return reviewers;
	}

	public void setReviewers(List<Reviewer> reviewers) {
		this.reviewers = reviewers;
	}

	public List<Approver> getApprovers() {
		return approvers;
	}

	public void setApprovers(List<Approver> approvers) {
		this.approvers = approvers;
	}

}
