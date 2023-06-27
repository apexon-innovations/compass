package com.apexon.compass.dashboard.request;

import jakarta.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

public class CloudInstanceListRefreshRequest extends BaseRequest {

	@NotNull
	private String accountNumber;

	@NotNull
	private List<String> instanceIds;

	private Date refreshDate;

	public String getAccountNumber() {
		return accountNumber;
	}

	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
	}

	public List<String> getInstanceIds() {
		return instanceIds;
	}

	public void setInstanceIds(List<String> instanceIds) {
		this.instanceIds = instanceIds;
	}

	public Date getRefreshDate() {
		return refreshDate;
	}

	public void setRefreshDate(Date refreshDate) {
		this.refreshDate = refreshDate;
	}

}
