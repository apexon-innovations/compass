package com.apexon.compass.dashboard.model;

import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.*;

/**
 * The collectors that have been registered in the given Dashboard app instance.
 */
@Document(collection = "collectors")
public class Collector extends BaseModel {

	private String name;

	private CollectorType collectorType;

	private boolean enabled;

	private boolean online;

	private List<CollectionError> errors = new ArrayList<>();

	// Every collector will have its own set of required and all fields depending upon the
	// specific tool.
	private Map<String, Object> uniqueFields = new HashMap<>();

	private Map<String, Object> allFields = new HashMap<>();

	private long lastExecuted;

	@DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
	private java.util.Date lastExecutedTime;

	private long lastExecutionRecordCount;

	private long lastExecutedSeconds;

	private List<String> searchFields = Arrays.asList("description");

	private Map<String, Object> properties = new HashMap<>(); // general purpose
																// name-value properties

	public Collector() {
	}

	public Collector(String name, CollectorType collectorType) {
		this.name = name;
		this.collectorType = collectorType;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public CollectorType getCollectorType() {
		return collectorType;
	}

	public void setCollectorType(CollectorType collectorType) {
		this.collectorType = collectorType;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public boolean isOnline() {
		return online;
	}

	public void setOnline(boolean online) {
		this.online = online;
	}

	public long getLastExecuted() {
		return lastExecuted;
	}

	public void setLastExecuted(long lastExecuted) {

		this.lastExecuted = lastExecuted;
		this.setLastExecutedTime(new Date(lastExecuted));
	}

	public long getLastExecutionRecordCount() {
		return lastExecutionRecordCount;
	}

	public long getLastExecutedSeconds() {
		return lastExecutedSeconds;
	}

	public void setLastExecutedSeconds(long lastExecutedSeconds) {
		this.lastExecutedSeconds = lastExecutedSeconds;
	}

	public void setLastExecutionRecordCount(long lastExecutionRecordCount) {
		this.lastExecutionRecordCount = lastExecutionRecordCount;
	}

	public List<CollectionError> getErrors() {
		return errors;
	}

	public void setErrors(List<CollectionError> errors) {
		this.errors = errors;
	}

	public Map<String, Object> getUniqueFields() {
		return uniqueFields;
	}

	public void setUniqueFields(Map<String, Object> uniqueFields) {
		this.uniqueFields = uniqueFields;
	}

	public Map<String, Object> getAllFields() {
		return allFields;
	}

	public void setAllFields(Map<String, Object> allFields) {
		this.allFields = allFields;
	}

	public List<String> getSearchFields() {
		return searchFields;
	}

	public void setSearchFields(List<String> searchFields) {
		this.searchFields = searchFields;
	}

	public Map<String, Object> getProperties() {
		return properties;
	}

	public void setProperties(Map<String, Object> properties) {
		this.properties = properties;
	}

	public Date getLastExecutedTime() {
		return lastExecutedTime == null ? new Date(System.currentTimeMillis()) : lastExecutedTime;
	}

	public void setLastExecutedTime(Date lastExecutedTime) {
		this.lastExecutedTime = lastExecutedTime;
	}

}
