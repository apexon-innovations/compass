package com.apexon.compass.dashboard.model;

public class ScmRepo {

	private String repoName;

	private String repoUrl;

	private String projectLanguage;

	private String defaultBranchToScan;

	public String getRepoName() {
		return repoName;
	}

	public void setRepoName(String repoName) {
		this.repoName = repoName;
	}

	public String getRepoUrl() {
		return repoUrl;
	}

	public void setRepoUrl(String repoUrl) {
		this.repoUrl = repoUrl;
	}

	public String getProjectLanguage() {
		return projectLanguage;
	}

	public void setProjectLanguage(String projectLanguage) {
		this.projectLanguage = projectLanguage;
	}

	public String getDefaultBranchToScan() {
		return defaultBranchToScan;
	}

	public void setDefaultBranchToScan(String defaultBranchToScan) {
		this.defaultBranchToScan = defaultBranchToScan;
	}

}
