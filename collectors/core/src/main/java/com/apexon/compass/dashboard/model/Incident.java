package com.apexon.compass.dashboard.model;

import org.bson.types.ObjectId;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Objects;

@Document(collection = "incident")
public class Incident extends BaseModel {

	private ObjectId collectorItemId;

	private Long timestamp;

	private String incidentItem;

	private String incidentID;

	private String category;

	private Long openTime;

	private String openedBy;

	private String severity;

	private Long updatedTime;

	private String primaryAssignmentGroup;

	private String status;

	private Long closedTime;

	private String closedBy;

	private String closureCode;

	private String affectedItem;

	private String incidentDescription;

	private String incidentSysID;

	private String affectedItemType;

	private String affectedItemSysId;

	private String affectedBusinessServiceItem;

	private String affectedBusinessServiceItemType;

	private String affectedBusinessServiceItemSysId;

	private boolean attachedToBusinessServiceOrEnvironmentOnly;

	private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ssZZ");

	public ObjectId getCollectorItemId() {
		return collectorItemId;
	}

	public void setCollectorItemId(ObjectId collectorItemId) {
		this.collectorItemId = collectorItemId;
	}

	public long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}

	public void setTimestamp(String timestamp) {
		this.timestamp = DATE_FORMATTER.parseMillis(timestamp);
	}

	public String getIncidentItem() {
		return incidentItem;
	}

	public void setIncidentItem(String incidentItem) {
		this.incidentItem = incidentItem;
	}

	public String getIncidentID() {
		return incidentID;
	}

	public void setIncidentID(String incidentID) {
		this.incidentID = incidentID;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public Long getOpenTime() {
		return openTime;
	}

	public void setOpenTime(Long openTime) {
		this.openTime = openTime;
	}

	public void setOpenTime(String openTime) {
		this.setOpenTime(DATE_FORMATTER.parseMillis(openTime));
	}

	public String getOpenedBy() {
		return openedBy;
	}

	public void setOpenedBy(String openedBy) {
		this.openedBy = openedBy;
	}

	public String getSeverity() {
		return severity;
	}

	public void setSeverity(String severity) {
		this.severity = severity;
	}

	public long getUpdatedTime() {
		return updatedTime;
	}

	public void setUpdatedTime(long updatedTime) {
		this.updatedTime = updatedTime;
	}

	public void setUpdatedTime(String updatedTime) {
		this.updatedTime = DATE_FORMATTER.parseMillis(updatedTime);
	}

	public String getPrimaryAssignmentGroup() {
		return primaryAssignmentGroup;
	}

	public void setPrimaryAssignmentGroup(String primaryAssignmentGroup) {
		this.primaryAssignmentGroup = primaryAssignmentGroup;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Long getClosedTime() {
		return closedTime;
	}

	public void setClosedTime(Long closedTime) {
		this.closedTime = closedTime;
	}

	public void setClosedTime(String closedTime) {
		this.setClosedTime(DATE_FORMATTER.parseMillis(closedTime));
	}

	public String getClosedBy() {
		return closedBy;
	}

	public void setClosedBy(String closedBy) {
		this.closedBy = closedBy;
	}

	public String getClosureCode() {
		return closureCode;
	}

	public void setClosureCode(String closureCode) {
		this.closureCode = closureCode;
	}

	public String getAffectedItem() {
		return affectedItem;
	}

	public void setAffectedItem(String affectedItem) {
		this.affectedItem = affectedItem;
	}

	public String getIncidentDescription() {
		return incidentDescription;
	}

	public void setIncidentDescription(String incidentDescription) {
		this.incidentDescription = incidentDescription;
	}

	public String getIncidentSysID() {
		return incidentSysID;
	}

	public void setIncidentSysID(String incidentSysID) {
		this.incidentSysID = incidentSysID;
	}

	public String getAffectedItemType() {
		return affectedItemType;
	}

	public void setAffectedItemType(String affectedItemType) {
		this.affectedItemType = affectedItemType;
	}

	public String getAffectedItemSysId() {
		return affectedItemSysId;
	}

	public void setAffectedItemSysId(String affectedItemSysId) {
		this.affectedItemSysId = affectedItemSysId;
	}

	public String getAffectedBusinessServiceItem() {
		return affectedBusinessServiceItem;
	}

	public void setAffectedBusinessServiceItem(String affectedBusinessServiceItem) {
		this.affectedBusinessServiceItem = affectedBusinessServiceItem;
	}

	public String getAffectedBusinessServiceItemType() {
		return affectedBusinessServiceItemType;
	}

	public void setAffectedBusinessServiceItemType(String affectedBusinessServiceItemType) {
		this.affectedBusinessServiceItemType = affectedBusinessServiceItemType;
	}

	public String getAffectedBusinessServiceItemSysId() {
		return affectedBusinessServiceItemSysId;
	}

	public void setAffectedBusinessServiceItemSysId(String affectedBusinessServiceItemSysId) {
		this.affectedBusinessServiceItemSysId = affectedBusinessServiceItemSysId;
	}

	public boolean isAttachedToBusinessServiceOrEnvironmentOnly() {
		return attachedToBusinessServiceOrEnvironmentOnly;
	}

	public void setAttachedToBusinessServiceOrEnvironmentOnly(boolean attachedToBusinessServiceOrEnvironmentOnly) {
		this.attachedToBusinessServiceOrEnvironmentOnly = attachedToBusinessServiceOrEnvironmentOnly;
	}

	@Override
	public boolean equals(Object compareTo) {
		boolean doesEqual = true;

		if (!(compareTo instanceof Incident)) {
			doesEqual = false;
		}
		else {
			Incident newIncident = (Incident) compareTo;

			if (!newIncident.toString().equals(toString())) {
				doesEqual = false;
			}
		}

		return doesEqual;
	}

	/**
	 * Returns human readable string of the Incident Object. * equals(Object object)
	 * depends on this method. Changing this method could alter the return of the equals
	 * method.
	 * @return object to string
	 */
	@Override
	public String toString() {

		StringBuilder builder = new StringBuilder(210);
		builder.append("incidentID: ").append(incidentID);

		return builder.toString();
	}

	@Override
	public int hashCode() {
		return Objects.hash(incidentID);
	}

}
