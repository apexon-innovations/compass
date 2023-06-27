package com.apexon.compass.dashboard.request;

import jakarta.validation.constraints.NotNull;

public class Monitor2DataCreateRequest extends BaseRequest {

	@NotNull
	private String name;

	private String url;

	private int status;

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUrl() {
		return this.url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public int getStatus() {
		return this.status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

}
