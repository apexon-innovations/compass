package com.apexon.compass.dashboard.collector;

import com.apexon.compass.dashboard.model.DesGitRequest;
import com.apexon.compass.dashboard.model.ScmConfiguration;
import com.apexon.compass.dashboard.model.ScmRepo;

public interface PullRequestCollectorClient {

	void populatePullRequestMetadata(ScmConfiguration scmConfiguration, ScmRepo repo, DesGitRequest pull,
			String userName, String password);

	void populatePullRequestActivity(ScmConfiguration scmConfiguration, ScmRepo repo, DesGitRequest pull,
			String userName, String password);

	void populatePullRequestCommits(ScmConfiguration scmConfiguration, ScmRepo repo, DesGitRequest pull,
			String userName, String password);

	void populatePullRequestComments(ScmConfiguration scmConfiguration, ScmRepo repo, DesGitRequest pull,
			String userName, String password);

	void populatePullRequestChanges(ScmConfiguration scmConfiguration, DesGitRequest pull, String userName,
			String password, String url);

}
