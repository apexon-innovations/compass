package com.apexon.compass.dashboard.model;

public enum DESFeatureStatus {

	BACKLOG("Backlog"), GROOMING("Grooming"), WAITING("Waiting"), IN_PROGRESS("In Progress"), IMPEDED("Impeded"),
	DONE("Done"), ACCEPTED("Accepted"), CLOSED("Closed");

	private String status;

	DESFeatureStatus(String status) {
		this.status = status;
	}

	public String getStatus() {
		return this.status;
	}

	@Override
	public String toString() {
		return this.status;
	}

}
