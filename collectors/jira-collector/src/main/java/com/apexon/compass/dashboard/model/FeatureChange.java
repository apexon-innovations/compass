package com.apexon.compass.dashboard.model;

import java.util.List;

public class FeatureChange {

	private String changeId;

	private String changeCreatedDateTime;

	private List<FeatureChangesUpdate> updates;

	/* changelog author */
	private String authorID;

	private String authorIsDeleted;

	private String authorState;

	private String authorUsername;

	private String authorFullName;

	private String authorShortName;

	public String getChangeId() {
		return changeId;
	}

	public String getChangeCreatedDateTime() {
		return changeCreatedDateTime;
	}

	public List<FeatureChangesUpdate> getUpdates() {
		return updates;
	}

	public String getAuthorID() {
		return authorID;
	}

	public void setAuthorID(String authorID) {
		this.authorID = authorID;
	}

	public String getAuthorIsDeleted() {
		return authorIsDeleted;
	}

	public void setAuthorIsDeleted(String authorIsDeleted) {
		this.authorIsDeleted = authorIsDeleted;
	}

	public String getAuthorState() {
		return authorState;
	}

	public void setAuthorState(String authorState) {
		this.authorState = authorState;
	}

	public String getAuthorUsername() {
		return authorUsername;
	}

	public void setAuthorUsername(String authorUsername) {
		this.authorUsername = authorUsername;
	}

	public String getAuthorFullName() {
		return authorFullName;
	}

	public void setAuthorFullName(String authorFullName) {
		this.authorFullName = authorFullName;
	}

	public String getAuthorShortName() {
		return authorShortName;
	}

	public void setAuthorShortName(String authorShortName) {
		this.authorShortName = authorShortName;
	}

	public void setChangeId(String changeId) {
		this.changeId = changeId;
	}

	public void setChangeCreatedDateTime(String changeCreatedDateTime) {
		this.changeCreatedDateTime = changeCreatedDateTime;
	}

	public void setUpdates(List<FeatureChangesUpdate> updates) {
		this.updates = updates;
	}

}
