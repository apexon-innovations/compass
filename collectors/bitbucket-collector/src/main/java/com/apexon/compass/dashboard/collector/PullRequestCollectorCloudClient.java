package com.apexon.compass.dashboard.collector;

import com.apexon.compass.dashboard.bitbucketapi.BitbucketApiUrlBuilder;
import com.apexon.compass.dashboard.model.*;
import com.apexon.compass.dashboard.util.Encryption;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.client.utils.URIBuilder;
import org.joda.time.DateTime;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.http.ResponseEntity;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import static com.apexon.compass.dashboard.collector.JSONParserUtils.parseAsObject;
import static com.apexon.compass.dashboard.collector.JSONParserUtils.str;

public class PullRequestCollectorCloudClient implements PullRequestCollectorClient {

	private SCMHttpRestClient scmHttpRestClient;

	private BitbucketApiUrlBuilder bitbucketApiUrlBuilder;

	private static final Log LOG = LogFactory.getLog(PullRequestCollector.class);

	private static final String DECLINED = "declined";

	private static final String MERGED = "merged";

	private static final String ACTIVITY_MERGE = "MERGED";

	private static final String ACTIVITY_DECLINE = "DECLINED";

	public PullRequestCollectorCloudClient(ScmConfiguration scmConfiguration, SCMHttpRestClient scmHttpRestClient) {
		this.bitbucketApiUrlBuilder = new BitbucketApiUrlBuilder(scmConfiguration);
		this.scmHttpRestClient = scmHttpRestClient;
	}

	/**
	 * Pull Requests Metadata - Source/Destination branch and repos
	 * @param scmConfiguration
	 * @param repo
	 * @param pull
	 * @param userName
	 * @param password
	 */
	public void populatePullRequestMetadata(ScmConfiguration scmConfiguration, ScmRepo repo, DesGitRequest pull,
			String userName, String password) {

		String repoUser = null;
		String repoPassword = null;
		if (scmConfiguration.getCredentials() != null && !scmConfiguration.getCredentials().isEmpty()) {
			try {
				repoUser = scmConfiguration.getUserName();
				repoPassword = Encryption.decryptString(scmConfiguration.getCredentials(),
						scmConfiguration.getEncryptionKey());
			}
			catch (Exception e) {
				throw new RuntimeException("Unable to decrypt SCM credentials", e);
			}
		}
		else {
			repoUser = userName;
			repoPassword = password;
		}
		try {
			URI uri = bitbucketApiUrlBuilder.buildPullRequestSelfApiUrl(repo.getRepoUrl(), pull.getNumber());
			URI queryUrl = new URIBuilder(uri).build();

			LOG.info("sExecuting [" + queryUrl);
			ResponseEntity<String> response = scmHttpRestClient.makeRestCall(queryUrl, repoUser, repoPassword);
			JSONObject jsonObject = parseAsObject(response);

			// Add source and destination branch and repository
			JSONObject sourceObject = (JSONObject) jsonObject.get("source");
			JSONObject sourceBranch = (JSONObject) sourceObject.get("branch");
			pull.setSourceBranch((String) sourceBranch.get("name"));

			JSONObject sourceRepo = (JSONObject) sourceObject.get("repository");
			pull.setSourceRepo((String) sourceRepo.get("full_name"));
			pull.setSourceRepoId((String) sourceRepo.get("uuid"));

			JSONObject destinationObject = (JSONObject) jsonObject.get("destination");
			JSONObject destinationBranch = (JSONObject) destinationObject.get("branch");
			pull.setTargetBranch((String) destinationBranch.get("name"));

			JSONObject destinationRepo = (JSONObject) destinationObject.get("repository");
			pull.setTargetRepo((String) destinationRepo.get("full_name"));
			pull.setTargetRepoId((String) destinationRepo.get("uuid"));

			// Add reviewers name
			List<Reviewer> reviewerList = new ArrayList<>();
			JSONArray reviewersJsonArray = (JSONArray) jsonObject.get("reviewers");
			for (Object value : reviewersJsonArray) {
				JSONObject reviewerObject = (JSONObject) value;

				Reviewer reviewer = new Reviewer();
				reviewer.setReviewerId((String) reviewerObject.get("uuid"));
				reviewer.setName((String) reviewerObject.get("display_name"));

				reviewerList.add(reviewer);
			}
			pull.setReviewers(reviewerList);

		}
		catch (Exception e) {
			LOG.error("Unable to construct Bitbucket API URL: " + e.getMessage());
			throw new RuntimeException("Unable to construct Bitbucket API URL: ", e);
		}
	}

	/**
	 * Pull Requests activities like merge commit, approvers, actions are being fetched
	 * here.
	 * @param scmConfiguration
	 * @param repo
	 * @param pull
	 * @param userName
	 * @param password
	 */
	public void populatePullRequestActivity(ScmConfiguration scmConfiguration, ScmRepo repo, DesGitRequest pull,
			String userName, String password) {
		List<Approver> approverList = new ArrayList<>();
		if (MERGED.equals(pull.getState())) {
			String repoUser = null;
			String repoPassword = null;

			if (scmConfiguration.getCredentials() != null && !scmConfiguration.getCredentials().isEmpty()) {
				try {
					repoUser = scmConfiguration.getUserName();
					repoPassword = Encryption.decryptString(scmConfiguration.getCredentials(),
							scmConfiguration.getEncryptionKey());
				}
				catch (Exception e) {
					throw new RuntimeException("Unable to decrypt SCM credentials", e);
				}
			}
			else {
				repoUser = userName;
				repoPassword = password;
			}
			URI pageUrl;
			try {
				URI uri = bitbucketApiUrlBuilder.buildPullRequestActivitiesApiUrl(repo.getRepoUrl(), pull.getNumber());
				pageUrl = new URIBuilder(uri).build();

				boolean lastPage = false;
				URI queryUrlPage = pageUrl;
				while (!lastPage) {
					LOG.info("sExecuting [" + queryUrlPage);
					ResponseEntity<String> response = scmHttpRestClient.makeRestCall(queryUrlPage, repoUser,
							repoPassword);
					JSONObject jsonArray = parseAsObject(response);
					JSONArray values = (JSONArray) jsonArray.get("values");
					for (Object item : values) {
						JSONObject jsonObject = (JSONObject) item;

						if (jsonObject.get("update") != null) {
							JSONObject update = (JSONObject) jsonObject.get("update");
							String state = (String) update.get("state");

							if (ACTIVITY_MERGE.equals(state)) {
								Long mergedAt = new DateTime(update.get("date")).getMillis();
								pull.setMergedAt(mergedAt);

								JSONObject mergeAuthorObject = (JSONObject) update.get("author");
								pull.setMergeAuthor((String) mergeAuthorObject.get("display_name"));
								pull.setMergeAuthorLDAPDN((String) mergeAuthorObject.get("uuid"));
							}
						}
						else if (null != jsonObject.get("approval")) {
							Approver approver = new Approver();

							JSONObject approval = (JSONObject) jsonObject.get("approval");
							Long approvedAt = new DateTime(approval.get("date")).getMillis();

							JSONObject user = (JSONObject) approval.get("user");

							approver.setApprovedAt(approvedAt);
							approver.setApproverId((String) user.get("uuid"));
							approver.setApproverName((String) user.get("display_name"));

							approverList.add(approver);
						}
					}
					PageMetadata pageMetadata = new PageMetadata(pageUrl, jsonArray, values,
							scmConfiguration.getProduct());
					lastPage = pageMetadata.isLastPage();
					queryUrlPage = pageMetadata.getNextPageUrl();
				}
			}
			catch (Exception e) {
				LOG.error("Unable to construct Bitbucket API URL: " + e.getMessage());
			}
		}
		else if (DECLINED.equals(pull.getState())) {
			String repoUser = null;
			String repoPassword = null;

			if (scmConfiguration.getCredentials() != null && !scmConfiguration.getCredentials().isEmpty()) {
				try {
					repoUser = scmConfiguration.getUserName();
					repoPassword = Encryption.decryptString(scmConfiguration.getCredentials(),
							scmConfiguration.getEncryptionKey());
				}
				catch (Exception e) {
					throw new RuntimeException("Unable to decrypt SCM credentials", e);
				}
			}
			else {
				repoUser = userName;
				repoPassword = password;
			}
			URI pageUrl;
			try {
				URI uri = bitbucketApiUrlBuilder.buildPullRequestActivitiesApiUrl(repo.getRepoUrl(), pull.getNumber());
				pageUrl = new URIBuilder(uri).build();

				boolean lastPage = false;
				boolean stop = false;
				URI queryUrlPage = pageUrl;
				while (!lastPage && !stop) {
					LOG.info("sExecuting [" + queryUrlPage);
					ResponseEntity<String> response = scmHttpRestClient.makeRestCall(queryUrlPage, repoUser,
							repoPassword);
					JSONObject jsonArray = parseAsObject(response);
					JSONArray values = (JSONArray) jsonArray.get("values");
					for (Object item : values) {
						JSONObject jsonObject = (JSONObject) item;

						if (jsonObject.get("update") != null) {
							JSONObject update = (JSONObject) jsonObject.get("update");
							String state = (String) update.get("state");

							stop = ACTIVITY_DECLINE.equals(state);
							if (stop) {
								Long declinedAt = new DateTime(update.get("date")).getMillis();
								pull.setClosedAt(declinedAt);

								JSONObject declineAuthorObject = (JSONObject) update.get("author");
								pull.setClosedAuthor((String) declineAuthorObject.get("display_name"));
								pull.setClosedAuthorLDAPDN((String) declineAuthorObject.get("uuid"));

								break;
							}
						}
					}
					PageMetadata pageMetadata = new PageMetadata(pageUrl, jsonArray, values,
							scmConfiguration.getProduct());
					lastPage = pageMetadata.isLastPage();
					queryUrlPage = pageMetadata.getNextPageUrl();
				}
			}
			catch (Exception e) {
				LOG.error("Unable to construct Bitbucket API URL: " + e.getMessage());
			}
		}
		pull.setApprovers(approverList);
	}

	/**
	 * Pull Requests commits
	 * @param scmConfiguration
	 * @param repo
	 * @param pull
	 * @param userName
	 * @param password
	 */
	public void populatePullRequestCommits(ScmConfiguration scmConfiguration, ScmRepo repo, DesGitRequest pull,
			String userName, String password) {

		List<Commit> commitList = new ArrayList<>();

		String repoUser = null;
		String repoPassword = null;
		if (scmConfiguration.getCredentials() != null && !scmConfiguration.getCredentials().isEmpty()) {
			try {
				repoUser = scmConfiguration.getUserName();
				repoPassword = Encryption.decryptString(scmConfiguration.getCredentials(),
						scmConfiguration.getEncryptionKey());
			}
			catch (Exception e) {
				throw new RuntimeException("Unable to decrypt SCM credentials", e);
			}
		}
		else {
			repoUser = userName;
			repoPassword = password;
		}
		URI pageUrl;
		try {
			URI uri = bitbucketApiUrlBuilder.buildPullRequestCommitsApiUrl(repo.getRepoUrl(), pull.getNumber());
			pageUrl = new URIBuilder(uri).build();

			boolean lastPage = false;
			boolean stop = false;
			URI queryUrlPage = pageUrl;
			while (!lastPage && !stop) {
				LOG.info("sExecuting [" + queryUrlPage);
				ResponseEntity<String> response = scmHttpRestClient.makeRestCall(queryUrlPage, repoUser, repoPassword);
				JSONObject jsonArray = parseAsObject(response);
				JSONArray values = (JSONArray) jsonArray.get("values");
				for (Object item : values) {
					JSONObject jsonObject = (JSONObject) item;
					String sha = str(jsonObject, "hash");

					// TODO: Verify with tanmay that this is correct solution of there is
					// a workaround
					if (sha.substring(0, 12).equalsIgnoreCase(pull.getScmRevisionNumber())) {
						pull.setScmRevisionNumber(sha);
						continue;
					}

					JSONObject authorObject = (JSONObject) jsonObject.get("author");
					String message = str(jsonObject, "message");
					String author = str(authorObject, "raw");
					long timestamp = new DateTime(str(jsonObject, "date")).getMillis();
					JSONArray parents = (JSONArray) jsonObject.get("parents");
					List<String> parentShas = new ArrayList<>();
					if (parents != null) {
						for (Object parentObj : parents) {
							parentShas.add(str((JSONObject) parentObj, "hash"));
						}
					}

					Commit commit = new Commit();
					commit.setTimestamp(System.currentTimeMillis());
					commit.setScmUrl(repo.getRepoUrl());
					commit.setScmBranch(repo.getDefaultBranchToScan());
					commit.setScmRevisionNumber(sha);
					commit.setScmParentRevisionNumbers(parentShas);
					commit.setScmAuthor(author);
					commit.setScmCommitLog(message);
					commit.setScmCommitTimestamp(timestamp);
					// TODO: need to check the number of changes in commits api or
					// somewhere.
					// commit.setNumberOfChanges(1);
					commit.setType(parentShas.size() > 1 ? CommitType.Merge : CommitType.New);
					commitList.add(commit);
				}
				PageMetadata pageMetadata = new PageMetadata(pageUrl, jsonArray, values, scmConfiguration.getProduct());
				lastPage = pageMetadata.isLastPage();
				queryUrlPage = pageMetadata.getNextPageUrl();
			}
		}
		catch (Exception e) {
			LOG.error("Unable to construct Bitbucket API URL: " + e.getMessage());
		}
		pull.setCommits(commitList);
	}

	/**
	 * Pull Requests comments
	 * @param scmConfiguration
	 * @param repo
	 * @param pull
	 * @param userName
	 * @param password
	 */
	public void populatePullRequestComments(ScmConfiguration scmConfiguration, ScmRepo repo, DesGitRequest pull,
			String userName, String password) {

		List<Comment> commentList = new ArrayList<>();

		String repoUser = null;
		String repoPassword = null;
		if (scmConfiguration.getCredentials() != null && !scmConfiguration.getCredentials().isEmpty()) {
			try {
				repoUser = scmConfiguration.getUserName();
				repoPassword = Encryption.decryptString(scmConfiguration.getCredentials(),
						scmConfiguration.getEncryptionKey());
			}
			catch (Exception e) {
				throw new RuntimeException("Unable to decrypt SCM credentials", e);
			}
		}
		else {
			repoUser = userName;
			repoPassword = password;
		}
		URI pageUrl;
		try {
			URI uri = bitbucketApiUrlBuilder.buildPullRequestCommentsApiUrl(repo.getRepoUrl(), pull.getNumber());
			pageUrl = new URIBuilder(uri).build();

			boolean lastPage = false;
			URI queryUrlPage = pageUrl;
			while (!lastPage) {
				LOG.info("Executing [" + queryUrlPage);
				ResponseEntity<String> response = scmHttpRestClient.makeRestCall(queryUrlPage, repoUser, repoPassword);
				JSONObject jsonArray = parseAsObject(response);
				JSONArray values = (JSONArray) jsonArray.get("values");
				for (Object item : values) {
					JSONObject jsonObject = (JSONObject) item;

					JSONObject content = (JSONObject) jsonObject.get("content");
					String body = (String) content.get("raw");

					JSONObject user = (JSONObject) jsonObject.get("user");
					String displayName = (String) user.get("display_name");
					String uuid = (String) user.get("uuid");

					Long createdAt = new DateTime(jsonObject.get("created_on")).getMillis();
					Long updatedAt = new DateTime(jsonObject.get("updated_on")).getMillis();

					Comment comment = new Comment();
					comment.setUserLDAPDN(uuid);
					comment.setUser(displayName);
					comment.setBody(body);
					comment.setCreatedAt(createdAt);
					comment.setUpdatedAt(updatedAt);

					commentList.add(comment);
				}
				PageMetadata pageMetadata = new PageMetadata(pageUrl, jsonArray, values, scmConfiguration.getProduct());
				lastPage = pageMetadata.isLastPage();
				queryUrlPage = pageMetadata.getNextPageUrl();
			}
		}
		catch (Exception e) {
			LOG.error("Unable to construct Bitbucket API URL: " + e.getMessage());
		}
		pull.setComments(commentList);
	}

	/**
	 * Pull Requests Changes - Lines removed and added in the PR
	 * @param scmConfiguration
	 * @param pull
	 * @param userName
	 * @param password
	 * @param url
	 */
	public void populatePullRequestChanges(ScmConfiguration scmConfiguration, DesGitRequest pull, String userName,
			String password, String url) {

		String repoUser = null;
		String repoPassword = null;
		if (scmConfiguration.getCredentials() != null && !scmConfiguration.getCredentials().isEmpty()) {
			try {
				repoUser = scmConfiguration.getUserName();
				repoPassword = Encryption.decryptString(scmConfiguration.getCredentials(),
						scmConfiguration.getEncryptionKey());
			}
			catch (Exception e) {
				throw new RuntimeException("Unable to decrypt SCM credentials", e);
			}
		}
		else {
			repoUser = userName;
			repoPassword = password;
		}
		URI pageUrl;
		try {
			URI uri = new URI(url);
			pageUrl = new URIBuilder(uri).build();

			boolean lastPage = false;
			URI queryUrlPage = pageUrl;
			long numberOfChanges = 0;
			while (!lastPage) {
				LOG.info("Executing [" + queryUrlPage);
				ResponseEntity<String> response = scmHttpRestClient.makeRestCall(queryUrlPage, repoUser, repoPassword);
				JSONObject jsonArray = parseAsObject(response);
				JSONArray values = (JSONArray) jsonArray.get("values");
				for (Object item : values) {
					JSONObject jsonObject = (JSONObject) item;
					Long linesRemoved = (Long) jsonObject.get("lines_removed");
					Long linesAdded = (Long) jsonObject.get("lines_added");
					numberOfChanges += (linesAdded - linesRemoved);
				}
				PageMetadata pageMetadata = new PageMetadata(pageUrl, jsonArray, values, scmConfiguration.getProduct());
				lastPage = pageMetadata.isLastPage();
				queryUrlPage = pageMetadata.getNextPageUrl();
			}
			pull.setNumberOfChanges(numberOfChanges);
		}
		catch (Exception e) {
			LOG.error("Unable to populate " + e.getMessage());
		}
	}

}
