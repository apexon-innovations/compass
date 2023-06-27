package com.apexon.compass.dashboard.collector;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Bean to hold settings specific to the git collector.
 */
@Component
@ConfigurationProperties(prefix = "git")
public class GitSettings {

	private String cron;

	private List<String> host;

	private String key;

	private int firstRunHistoryDays;

	private String api;

	private int pageSize;

	private String product;

	private List<String> username;

	private List<String> password;

	public List<String> getHost() {
		return host;
	}

	public void setHost(List<String> host) {
		this.host = host;
	}

	public String getCron() {
		return cron;
	}

	public void setCron(String cron) {
		this.cron = cron;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public int getFirstRunHistoryDays() {
		return firstRunHistoryDays;
	}

	public void setFirstRunHistoryDays(int firstRunHistoryDays) {
		this.firstRunHistoryDays = firstRunHistoryDays;
	}

	public String getApi() {
		return api;
	}

	public void setApi(String api) {
		this.api = api;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public String getProduct() {
		return product;
	}

	public void setProduct(String product) {
		this.product = product;
	}

	public List<String> getUsername() {
		return username;
	}

	public void setUsername(List<String> username) {
		this.username = username;
	}

	public void setPassword(List<String> password) {
		this.password = password;
	}

	public List<String> getPassword() {
		return password;
	}

}
