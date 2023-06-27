package com.apexon.compass.dashboard.collector;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Bean to hold settings specific to the Sonar collector.
 */
@Component
@ConfigurationProperties(prefix = "sonar")
public class SonarSettings {

	private String cron;

	private List<String> usernames = new ArrayList<>();

	private List<String> passwords = new ArrayList<>();

	private List<String> servers = new ArrayList<>();

	private List<String> niceNames;

	private List<String> tokens;

	private String metrics63andAbove; // 6.3 is the sonar version

	private String metricsBefore63;

	public String getCron() {
		return cron;
	}

	public void setCron(String cron) {
		this.cron = cron;
	}

	public List<String> getUsernames() {
		return usernames;
	}

	public void setUsernames(List<String> usernames) {
		this.usernames = usernames;
	}

	public List<String> getPasswords() {
		return passwords;
	}

	public void setPasswords(List<String> passwords) {
		this.passwords = passwords;
	}

	public List<String> getServers() {
		return servers;
	}

	public void setServers(List<String> servers) {
		this.servers = servers;
	}

	public List<String> getNiceNames() {
		return niceNames;
	}

	public void setNiceNames(List<String> niceNames) {
		this.niceNames = niceNames;
	}

	public List<String> getTokens() {
		return tokens;
	}

	public void setTokens(List<String> tokens) {
		this.tokens = tokens;
	}

	public String getMetrics63andAbove() {
		return metrics63andAbove;
	}

	public void setMetrics63andAbove(String metrics63andAbove) {
		this.metrics63andAbove = metrics63andAbove;
	}

	public String getMetricsBefore63() {
		return metricsBefore63;
	}

	public void setMetricsBefore63(String metricsBefore63) {
		this.metricsBefore63 = metricsBefore63;
	}

}
