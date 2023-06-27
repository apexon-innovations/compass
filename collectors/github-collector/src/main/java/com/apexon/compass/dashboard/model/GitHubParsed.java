package com.apexon.compass.dashboard.model;

import com.apexon.compass.dashboard.misc.HygieiaException;

import java.net.MalformedURLException;
import java.net.URL;

public class GitHubParsed {

	private String url;

	private String host;

	private String apiUrl;

	private String searchPrApiUrl;

	private String orgName;

	private String repoName;

	private static final String SEGMENT_API = "/api/v3/repos";

	private static final String SEARCH_SEGMENT_API = "/api/v3/search/issues";

	private static final String PUBLIC_GITHUB_REPO_HOST = "api.github.com/repos";

	private static final String PUBLIC_GITHUB_SEARCH_HOST = "api.github.com/search/issues";

	private static final String PUBLIC_GITHUB_HOST_NAME = "github.com";

	public GitHubParsed(String url) throws MalformedURLException, HygieiaException {
		this.url = url;
		parse();
	}

	private void parse() throws MalformedURLException, HygieiaException {
		if (url.endsWith(".git")) {
			url = url.substring(0, url.lastIndexOf(".git"));
		}
		URL u = new URL(url);
		host = u.getHost();
		String protocol = u.getProtocol();
		String path = u.getPath();
		String[] parts = path.split("/");
		if ((parts == null) || (parts.length < 3)) {
			throw new HygieiaException("Bad github repo URL: " + url, HygieiaException.BAD_DATA);
		}
		orgName = parts[1];
		repoName = parts[2];
		if (host.startsWith(PUBLIC_GITHUB_HOST_NAME)) {
			apiUrl = protocol + "://" + PUBLIC_GITHUB_REPO_HOST + path;
			searchPrApiUrl = protocol + "://" + PUBLIC_GITHUB_SEARCH_HOST + "?q=repo:"
					+ path.substring(1, path.length()) + " is:pr ";
		}
		else {
			apiUrl = protocol + "://" + host + SEGMENT_API + path;
			searchPrApiUrl = protocol + "://" + host + SEARCH_SEGMENT_API + "?q=repo:"
					+ path.substring(1, path.length()) + " is:pr ";
		}
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public String getApiUrl() {
		return apiUrl;
	}

	public void setApiUrl(String apiUrl) {
		this.apiUrl = apiUrl;
	}

	public String getOrgName() {
		return orgName;
	}

	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}

	public String getRepoName() {
		return repoName;
	}

	public void setRepoName(String repoName) {
		this.repoName = repoName;
	}

	public String getSearchPrApiUrl() {
		return searchPrApiUrl;
	}

	public void setSearchPrApiUrl(String searchPrApiUrl) {
		this.searchPrApiUrl = searchPrApiUrl;
	}

}
