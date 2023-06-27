package com.apexon.compass.dashboard.model;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document(collection = "sonar_configuration")
public class SonarConfiguration extends Collector {

	private ObjectId iscProjectId;

	private String url;

	private String userName;

	private String credentials;

	private String encryptionKey;

	private String version;

	private List<String> metricsFields;

	private List<SonarSubProject> projects;

	private String cron;

	private String createdBy;

	private Long createdDate;

	private String updatedBy;

	private Long updatedDate;

	public ObjectId getIscProjectId() {
		return iscProjectId;
	}

	public void setIscProjectId(ObjectId iscProjectId) {
		this.iscProjectId = iscProjectId;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getCredentials() {
		return credentials;
	}

	public void setCredentials(String credentials) {
		this.credentials = credentials;
	}

	public String getEncryptionKey() {
		return encryptionKey;
	}

	public void setEncryptionKey(String encryptionKey) {
		this.encryptionKey = encryptionKey;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public List<String> getMetricsFields() {
		return metricsFields;
	}

	public void setMetricsFields(List<String> metricsFields) {
		this.metricsFields = metricsFields;
	}

	public List<SonarSubProject> getProjects() {
		return projects;
	}

	public void setProjects(List<SonarSubProject> projects) {
		this.projects = projects;
	}

	public String getCron() {
		return cron;
	}

	public void setCron(String cron) {
		this.cron = cron;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public Long getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Long createdDate) {
		this.createdDate = createdDate;
	}

	public String getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}

	public Long getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(Long updatedDate) {
		this.updatedDate = updatedDate;
	}

}