package com.apexon.compass.dashboard.model;

public enum FeatureStatus {

	BACKLOG("Backlog"), GROOMING("Grooming"), WAITING("Waiting"), IN_PROGRESS("In Progress"), IMPEDED("Impeded"),
	DONE("Done"), ACCEPTED("Accepted");

	private final String status;

	FeatureStatus(String status) {
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
