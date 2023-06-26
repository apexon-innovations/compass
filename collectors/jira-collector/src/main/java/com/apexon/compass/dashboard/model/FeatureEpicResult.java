package com.apexon.compass.dashboard.model;

import java.util.ArrayList;
import java.util.List;

public class FeatureEpicResult {

	List<DesFeature> featureList = new ArrayList<>();

	List<Epic> epicList = new ArrayList<>();

	public List<DesFeature> getFeatureList() {
		return featureList;
	}

	public void setFeatureList(List<DesFeature> featureList) {
		this.featureList = featureList;
	}

	public List<Epic> getEpicList() {
		return epicList;
	}

	public void setEpicList(List<Epic> epicList) {
		this.epicList = epicList;
	}

}
