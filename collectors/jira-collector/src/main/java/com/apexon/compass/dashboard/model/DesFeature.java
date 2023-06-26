package com.apexon.compass.dashboard.model;

import org.springframework.data.mongodb.core.index.Indexed;

import java.util.List;

public class DesFeature extends Feature {

	@Indexed
	private String createdDate;

	private List<FeatureSubTask> subTasks;

	private List<String> sprintJourney;

	private String sPriorityId;

	private String sPriorityName;

	private String iscProjectId;

	public String getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(String createdDate) {
		this.createdDate = createdDate;
	}

	public List<FeatureSubTask> getSubTasks() {
		return subTasks;
	}

	public void setSubTasks(List<FeatureSubTask> subTasks) {
		this.subTasks = subTasks;
	}

	private List<FeatureChange> changes;

	public void setChanges(List<FeatureChange> changes) {
		this.changes = changes;
	}

	public List<FeatureChange> getChanges() {
		return changes;
	}

	public List<String> getSprintJourney() {
		return sprintJourney;
	}

	public void setSprintJourney(List<String> sprintJourney) {
		this.sprintJourney = sprintJourney;
	}

	public String getsPriorityId() {
		return sPriorityId;
	}

	public void setsPriorityId(String sPriorityId) {
		this.sPriorityId = sPriorityId;
	}

	public String getsPriorityName() {
		return sPriorityName;
	}

	public void setsPriorityName(String sPriorityName) {
		this.sPriorityName = sPriorityName;
	}

	public String getIscProjectId() {
		return iscProjectId;
	}

	public void setIscProjectId(String iscProjectId) {
		this.iscProjectId = iscProjectId;
	}

}
