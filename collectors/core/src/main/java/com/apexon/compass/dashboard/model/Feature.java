package com.apexon.compass.dashboard.model;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Represents a feature (story/requirement) of a component.
 *
 * Possible collectors: VersionOne PivotalTracker Rally Trello Jira
 *
 * @author kfk884
 *
 */
@Document(collection = "feature")
public class Feature extends BaseModel {

	private ObjectId collectorId;

	/*
	 * Story data
	 */
	@Indexed
	private String sId;

	private String sNumber;

	private String sName;

	private String sTypeId;

	private String sTypeName;

	private String sStatus;

	private String sState;

	private String sEstimate; // estimate in story points

	private Integer sEstimateTime; // estimate in minutes

	private String sUrl;

	@Indexed
	private String changeDate;

	private String isDeleted;

	/*
	 * Owner data
	 */
	private List<String> sOwnersID;

	private List<String> sOwnersIsDeleted;

	private List<String> sOwnersChangeDate;

	private List<String> sOwnersState;

	private List<String> sOwnersUsername;

	private List<String> sOwnersFullName;

	private List<String> sOwnersShortName;

	/*
	 * ScopeOwner data
	 */
	private String sTeamIsDeleted;

	private String sTeamAssetState;

	private String sTeamChangeDate;

	private String sTeamName;

	@Indexed
	private String sTeamID;

	/*
	 * Sprint data
	 */
	private String sSprintIsDeleted;

	private String sSprintChangeDate;

	private String sSprintAssetState;

	@Indexed
	private String sSprintEndDate;

	@Indexed
	private String sSprintBeginDate;

	private String sSprintName;

	@Indexed
	private String sSprintID;

	private String sSprintUrl;

	/*
	 * Epic data
	 */
	private String sEpicIsDeleted;

	private String sEpicChangeDate;

	private String sEpicAssetState;

	private String sEpicType;

	private String sEpicEndDate;

	private String sEpicBeginDate;

	private String sEpicName;

	private String sEpicUrl;

	private String sEpicNumber;

	@Indexed
	private String sEpicID;

	/*
	 * Scope data
	 */
	private String sProjectPath;

	private String sProjectIsDeleted;

	private String sProjectState;

	private String sProjectChangeDate;

	private String sProjectEndDate;

	private String sProjectBeginDate;

	private String sProjectName;

	@Indexed
	private String sProjectID;

	private Collection<FeatureIssueLink> issueLinks = new ArrayList<>();

	public ObjectId getCollectorId() {
		return collectorId;
	}

	public void setCollectorId(ObjectId collectorId) {
		this.collectorId = collectorId;
	}

	public String getsId() {
		return sId;
	}

	public void setsId(String sId) {
		this.sId = sId;
	}

	public String getsNumber() {
		return sNumber;
	}

	public void setsNumber(String sNumber) {
		this.sNumber = sNumber;
	}

	public String getsName() {
		return sName;
	}

	public void setsName(String sName) {
		this.sName = sName;
	}

	public String getsTypeId() {
		return sTypeId;
	}

	public void setsTypeId(String sTypeId) {
		this.sTypeId = sTypeId;
	}

	public String getsTypeName() {
		return sTypeName;
	}

	public void setsTypeName(String sTypeName) {
		this.sTypeName = sTypeName;
	}

	public String getsStatus() {
		return sStatus;
	}

	public void setsStatus(String sStatus) {
		this.sStatus = sStatus;
	}

	public String getsState() {
		return sState;
	}

	public void setsState(String sState) {
		this.sState = sState;
	}

	public String getsEstimate() {
		return sEstimate;
	}

	public void setsEstimate(String sEstimate) {
		this.sEstimate = sEstimate;
	}

	public Integer getsEstimateTime() {
		return sEstimateTime;
	}

	public void setsEstimateTime(Integer sEstimateTime) {
		this.sEstimateTime = sEstimateTime;
	}

	public void setsUrl(String sUrl) {
		this.sUrl = sUrl;
	}

	public String getsUrl() {
		return sUrl;
	}

	public String getsProjectID() {
		return sProjectID;
	}

	public void setsProjectID(String sProjectID) {
		this.sProjectID = sProjectID;
	}

	public String getsEpicID() {
		return sEpicID;
	}

	public void setsEpicID(String sEpicID) {
		this.sEpicID = sEpicID;
	}

	public String getsSprintID() {
		return sSprintID;
	}

	public void setsSprintID(String sSprintID) {
		this.sSprintID = sSprintID;
	}

	public void setsSprintUrl(String sSprintUrl) {
		this.sSprintUrl = sSprintUrl;
	}

	public String getsSprintUrl() {
		return sSprintUrl;
	}

	public String getsTeamID() {
		return sTeamID;
	}

	public void setsTeamID(String sTeamID) {
		this.sTeamID = sTeamID;
	}

	public String getChangeDate() {
		return changeDate;
	}

	public void setChangeDate(String changeDate) {
		this.changeDate = changeDate;
	}

	public List<String> getsOwnersID() {
		return sOwnersID;
	}

	public void setsOwnersID(List<String> sOwnersID) {
		this.sOwnersID = sOwnersID;
	}

	public String getIsDeleted() {
		return isDeleted;
	}

	public void setIsDeleted(String isDeleted) {
		this.isDeleted = isDeleted;
	}

	public void setsProjectName(String sProjectName) {
		this.sProjectName = sProjectName;
	}

	public String getsProjectName() {
		return this.sProjectName;
	}

	public void setsProjectBeginDate(String sProjectBeginDate) {
		this.sProjectBeginDate = sProjectBeginDate;
	}

	public String getsProjectBeginDate() {
		return this.sProjectBeginDate;
	}

	public void setsProjectEndDate(String sProjectEndDate) {
		this.sProjectEndDate = sProjectEndDate;
	}

	public String getsProjectEndDate() {
		return this.sProjectEndDate;
	}

	public void setsProjectChangeDate(String sProjectChangeDate) {
		this.sProjectChangeDate = sProjectChangeDate;
	}

	public String getsProjectChangeDate() {
		return this.sProjectChangeDate;
	}

	public void setsProjectState(String sProjectState) {
		this.sProjectState = sProjectState;
	}

	public String getsProjectState() {
		return this.sProjectState;
	}

	public void setsProjectIsDeleted(String sProjectIsDeleted) {
		this.sProjectIsDeleted = sProjectIsDeleted;
	}

	public String getsProjectIsDeleted() {
		return this.sProjectIsDeleted;
	}

	public void setsProjectPath(String sProjectPath) {
		this.sProjectPath = sProjectPath;
	}

	public String getsProjectPath() {
		return this.sProjectPath;
	}

	public void setsEpicNumber(String sEpicNumber) {
		this.sEpicNumber = sEpicNumber;
	}

	public String getsEpicNumber() {
		return this.sEpicNumber;
	}

	public void setsEpicName(String sEpicName) {
		this.sEpicName = sEpicName;
	}

	public String getsEpicName() {
		return this.sEpicName;
	}

	public void setsEpicUrl(String sEpicUrl) {
		this.sEpicUrl = sEpicUrl;
	}

	public String getsEpicUrl() {
		return sEpicUrl;
	}

	public void setsEpicBeginDate(String sEpicBeginDate) {
		this.sEpicBeginDate = sEpicBeginDate;
	}

	public String getsEpicBeginDate() {
		return this.sEpicBeginDate;
	}

	public void setsEpicEndDate(String sEpicEndDate) {
		this.sEpicEndDate = sEpicEndDate;
	}

	public String getsEpicEndDate() {
		return this.sEpicEndDate;
	}

	public void setsEpicType(String sEpicType) {
		this.sEpicType = sEpicType;
	}

	public String getsEpicType() {
		return this.sEpicType;
	}

	public void setsEpicAssetState(String sEpicAssetState) {
		this.sEpicAssetState = sEpicAssetState;
	}

	public String getsEpicAssetState() {
		return this.sEpicAssetState;
	}

	public void setsEpicChangeDate(String sEpicChangeDate) {
		this.sEpicChangeDate = sEpicChangeDate;
	}

	public String getsEpicChangeDate() {
		return this.sEpicChangeDate;
	}

	public void setsEpicIsDeleted(String sEpicIsDeleted) {
		this.sEpicIsDeleted = sEpicIsDeleted;
	}

	public String getsEpicIsDeleted() {
		return this.sEpicIsDeleted;
	}

	public void setsSprintName(String sSprintName) {
		this.sSprintName = sSprintName;
	}

	public String getsSprintName() {
		return this.sSprintName;
	}

	public void setsSprintBeginDate(String sSprintBeginDate) {
		this.sSprintBeginDate = sSprintBeginDate;
	}

	public String getsSprintBeginDate() {
		return this.sSprintBeginDate;
	}

	public void setsSprintEndDate(String sSprintEndDate) {
		this.sSprintEndDate = sSprintEndDate;
	}

	public String getsSprintEndDate() {
		return this.sSprintEndDate;
	}

	public void setsSprintAssetState(String sSprintAssetState) {
		this.sSprintAssetState = sSprintAssetState;
	}

	public String getsSprintAssetState() {
		return this.sSprintAssetState;
	}

	public void setsSprintChangeDate(String sSprintChangeDate) {
		this.sSprintChangeDate = sSprintChangeDate;
	}

	public String getsSprintChangeDate() {
		return this.sSprintChangeDate;
	}

	public void setsSprintIsDeleted(String sSprintIsDeleted) {
		this.sSprintIsDeleted = sSprintIsDeleted;
	}

	public String getsSprintIsDeleted() {
		return this.sSprintIsDeleted;
	}

	public void setsTeamName(String sTeamName) {
		this.sTeamName = sTeamName;
	}

	public String getsTeamName() {
		return this.sTeamName;
	}

	public void setsTeamChangeDate(String sTeamChangeDate) {
		this.sTeamChangeDate = sTeamChangeDate;
	}

	public String getsTeamChangeDate() {
		return this.sTeamChangeDate;
	}

	public void setsTeamAssetState(String sTeamAssetState) {
		this.sTeamAssetState = sTeamAssetState;
	}

	public String getsTeamAssetState() {
		return this.sTeamAssetState;
	}

	public void setsTeamIsDeleted(String sTeamIsDeleted) {
		this.sTeamIsDeleted = sTeamIsDeleted;
	}

	public String getsTeamIsDeleted() {
		return this.sTeamIsDeleted;
	}

	public void setsOwnersShortName(List<String> list) {
		this.sOwnersShortName = list;
	}

	public List<String> getsOwnersShortName() {
		return this.sOwnersShortName;
	}

	public void setsOwnersFullName(List<String> list) {
		this.sOwnersFullName = list;
	}

	public List<String> getsOwnersFullName() {
		return this.sOwnersFullName;
	}

	public void setsOwnersUsername(List<String> list) {
		this.sOwnersUsername = list;
	}

	public List<String> getsOwnersUsername() {
		return this.sOwnersUsername;
	}

	public void setsOwnersState(List<String> list) {
		this.sOwnersState = list;
	}

	public List<String> getsOwnersState() {
		return this.sOwnersState;
	}

	public void setsOwnersChangeDate(List<String> list) {
		this.sOwnersChangeDate = list;
	}

	public List<String> getsOwnersChangeDate() {
		return this.sOwnersChangeDate;
	}

	public void setsOwnersIsDeleted(List<String> list) {
		this.sOwnersIsDeleted = list;
	}

	public List<String> getsOwnersIsDeleted() {
		return this.sOwnersIsDeleted;
	}

	public Collection<FeatureIssueLink> getIssueLinks() {
		return issueLinks;
	}

	public void setIssueLinks(Collection<FeatureIssueLink> issueLinks) {
		this.issueLinks = issueLinks;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || !(o instanceof Feature))
			return false;

		Feature that = (Feature) o;
		EqualsBuilder builder = new EqualsBuilder();
		return builder.append(collectorId, that.collectorId).append(sId, that.sId).build();
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder(17, 37).append(collectorId).append(sId).toHashCode();
	}

}
