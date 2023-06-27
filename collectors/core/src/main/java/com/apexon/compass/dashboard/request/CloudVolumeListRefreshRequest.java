package com.apexon.compass.dashboard.request;

import jakarta.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

public class CloudVolumeListRefreshRequest extends BaseRequest {

	@NotNull
	private String accountNumber;

	@NotNull
	private List<String> volumeIds;

	private Date refreshDate;

	public String getAccountNumber() {
		return accountNumber;
	}

	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
	}

	public List<String> getVolumeIds() {
		return volumeIds;
	}

	public void setVolumeIds(List<String> volumeIds) {
		this.volumeIds = volumeIds;
	}

	public Date getRefreshDate() {
		return refreshDate;
	}

	public void setRefreshDate(Date refreshDate) {
		this.refreshDate = refreshDate;
	}

}
