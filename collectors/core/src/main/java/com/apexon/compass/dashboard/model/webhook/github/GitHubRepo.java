package com.apexon.compass.dashboard.model.webhook.github;

import com.apexon.compass.dashboard.model.CollectorItem;

/**
 * CollectorItem extension to store the github repo url and branch.
 */
public class GitHubRepo extends CollectorItem {

	public static final String REPO_URL = "url"; // http://github.company.com/jack/somejavacode

	public static final String BRANCH = "branch"; // main, development, release etc.

	public static final String USER_ID = "userID";

	public static final String PASSWORD = "password";

	public static final String PERSONAL_ACCESS_TOKEN = "personalAccessToken";

	public static final String TYPE = "type"; // SOURCE, BUILD, INFRA, TEST etc

	public String getUserId() {
		return (String) getOptions().get(USER_ID);
	}

	public String getPassword() {
		return (String) getOptions().get(PASSWORD);
	}

	public String getRepoUrl() {
		return (String) getOptions().get(REPO_URL);
	}

	public void setRepoUrl(String instanceUrl) {
		getOptions().put(REPO_URL, instanceUrl);
	}

	public String getBranch() {
		return (String) getOptions().get(BRANCH);
	}

	public void setBranch(String branch) {
		getOptions().put(BRANCH, branch);
	}

	public String getType() {
		return (String) getOptions().get(TYPE);
	}

	public void setType(String type) {
		getOptions().put(TYPE, type);
	}

	public String getPersonalAccessToken() {
		return (String) getOptions().get(PERSONAL_ACCESS_TOKEN);
	}

	public void setPersonalAccessToken(String personalAccessToken) {
		getOptions().put(PERSONAL_ACCESS_TOKEN, personalAccessToken);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}

		GitHubRepo gitHubRepo = (GitHubRepo) o;

		return getRepoUrl().equals(gitHubRepo.getRepoUrl()) && getBranch().equals(gitHubRepo.getBranch());
	}

	@Override
	public int hashCode() {
		return getRepoUrl().hashCode();
	}

}
