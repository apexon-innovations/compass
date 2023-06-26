package com.apexon.compass.dashboard.collector;

import com.apexon.compass.dashboard.model.DesGitRequest;
import com.apexon.compass.dashboard.model.GitHubRepo;
import com.apexon.compass.dashboard.model.ScmConfiguration;
import com.apexon.compass.dashboard.model.ScmRepo;
import com.apexon.compass.dashboard.misc.HygieiaException;
import com.apexon.compass.dashboard.model.*;

import java.net.MalformedURLException;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Client for fetching commit history from GitHub
 */
public interface GitHubClient {

	List<Commit> getCommits(GitHubRepo repo, boolean firstRun, List<Pattern> commitExclusionPatterns)
			throws MalformedURLException, HygieiaException;

	int getPulls(boolean firstRun, ScmConfiguration scmConfiguration, ScmRepo repo, String status)
			throws MalformedURLException, HygieiaException;

	List<DesGitRequest> getIssues(GitHubRepo repo, boolean firstRun) throws MalformedURLException, HygieiaException;

}
