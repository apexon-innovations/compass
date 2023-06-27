package com.apexon.compass.dashboard.model;

public class Approver {

	private String approverId;

	private String approverName;

	private Long approvedAt;

	public String getApproverId() {
		return approverId;
	}

	public void setApproverId(String approverId) {
		this.approverId = approverId;
	}

	public String getApproverName() {
		return approverName;
	}

	public void setApproverName(String approverName) {
		this.approverName = approverName;
	}

	public Long getApprovedAt() {
		return approvedAt;
	}

	public void setApprovedAt(Long approvedAt) {
		this.approvedAt = approvedAt;
	}

}
