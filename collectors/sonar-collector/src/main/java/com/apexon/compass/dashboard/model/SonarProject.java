package com.apexon.compass.dashboard.model;

import java.util.Objects;

public class SonarProject extends CollectorItem {

	protected static final String INSTANCE_URL = "instanceUrl";

	protected static final String PROJECT_NAME = "projectName";

	protected static final String PROJECT_ID = "projectId";

	protected static final String PROJECT_KEY = "projectKey";

	public String getInstanceUrl() {
		return (String) getOptions().get(INSTANCE_URL);
	}

	public void setInstanceUrl(String instanceUrl) {
		getOptions().put(INSTANCE_URL, instanceUrl);
	}

	public String getProjectId() {
		return (String) getOptions().get(PROJECT_ID);
	}

	public String getProjectKey() {
		return (String) getOptions().get(PROJECT_KEY);
	}

	public void setProjectId(String id) {
		getOptions().put(PROJECT_ID, id);
	}

	public String getProjectName() {
		return (String) getOptions().get(PROJECT_NAME);
	}

	public void setProjectName(String name) {
		getOptions().put(PROJECT_NAME, name);
	}

	public void setProjectKey(String key) {
		getOptions().put(PROJECT_KEY, key);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;

		SonarProject that = (SonarProject) o;
		return Objects.equals(getProjectName(), that.getProjectName())
				&& Objects.equals(getInstanceUrl(), that.getInstanceUrl());
	}

	@Override
	public int hashCode() {
		int result = getInstanceUrl().hashCode();
		result = 31 * result + getProjectName().hashCode();
		return result;
	}

}
