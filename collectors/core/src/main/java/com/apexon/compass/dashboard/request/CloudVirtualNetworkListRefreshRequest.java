package com.apexon.compass.dashboard.request;

import jakarta.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

public class CloudVirtualNetworkListRefreshRequest extends BaseRequest {

	@NotNull
	private String accountNumber;

	@NotNull
	private List<String> virtualNetworkIds;

	private Date refreshDate;

	public String getAccountNumber() {
		return accountNumber;
	}

	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
	}

	public List<String> getVirtualNetworkIds() {
		return virtualNetworkIds;
	}

	public void setVirtualNetworkIds(List<String> virtualNetworkIds) {
		this.virtualNetworkIds = virtualNetworkIds;
	}

	public Date getRefreshDate() {
		return refreshDate;
	}

	public void setRefreshDate(Date refreshDate) {
		this.refreshDate = refreshDate;
	}

}
