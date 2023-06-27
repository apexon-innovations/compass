package com.apexon.compass.dashboard.model;

import com.apexon.compass.dashboard.model.CodeQuality;

public class DesCodeQuality extends CodeQuality {

	private String sonarProjectId;

	private String iscProjectId;

	private String sonarConfigurationId;

	public String getSonarProjectId() {
		return sonarProjectId;
	}

	public void setSonarProjectId(String sonarProjectId) {
		this.sonarProjectId = sonarProjectId;
	}

	public String getIscProjectId() {
		return iscProjectId;
	}

	public void setIscProjectId(String iscProjectId) {
		this.iscProjectId = iscProjectId;
	}

	public String getSonarConfigurationId() {
		return sonarConfigurationId;
	}

	public void setSonarConfigurationId(String sonarConfigurationId) {
		this.sonarConfigurationId = sonarConfigurationId;
	}

}
