package com.apexon.compass.dashboard.model;

import java.util.ArrayList;
import java.util.List;

public class IssueResult {

	private long total;

	private long pageSize;

	private List<DesFeature> features = new ArrayList<>();

	public long getTotal() {
		return total;
	}

	public void setTotal(long total) {
		this.total = total;
	}

	public long getPageSize() {
		return pageSize;
	}

	public void setPageSize(long pageSize) {
		this.pageSize = pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public List<DesFeature> getFeatures() {
		return features;
	}

	public void setFeatures(List<DesFeature> features) {
		this.features = features;
	}

}
