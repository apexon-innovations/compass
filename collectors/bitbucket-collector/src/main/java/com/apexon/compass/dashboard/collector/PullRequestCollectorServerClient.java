package com.apexon.compass.dashboard.collector;

import com.apexon.compass.dashboard.bitbucketapi.BitbucketApiUrlBuilder;
import com.apexon.compass.dashboard.model.*;
import com.apexon.compass.dashboard.util.Encryption;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.client.utils.URIBuilder;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.http.ResponseEntity;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class PullRequestCollectorServerClient implements PullRequestCollectorClient {

	private SCMHttpRestClient scmHttpRestClient;

	private BitbucketApiUrlBuilder bitbucketApiUrlBuilder;

	private static final Log LOG = LogFactory.getLog(PullRequestCollector.class);

	private static final String ACTIVITY_MERGE = "MERGED";

	private static final String ACTIVITY_APPROVE = "APPROVED";

	private static final String ACTIVITY_DECLINE = "DECLINED";

	private static final String ACTIVITY_COMMENT = "COMMENTED";

	public PullRequestCollectorServerClient(ScmConfiguration scmConfiguration, SCMHttpRestClient scmHttpRestClient) {
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
			JSONObject jsonObject = JSONParserUtils.parseAsObject(response);

			// Add source and destination branch and repository
			JSONObject sourceObject = (JSONObject) jsonObject.get("fromRef");
			pull.setSourceBranch(JSONParserUtils.str(sourceObject, "displayId"));

			JSONObject sourceRepo = (JSONObject) sourceObject.get("repository");
			pull.setSourceRepo(JSONParserUtils.str(sourceRepo, "slug"));
			pull.setSourceRepoId(JSONParserUtils.str(sourceRepo, "hierarchyId"));

			JSONObject targetObject = (JSONObject) jsonObject.get("toRef");
			pull.setTargetBranch(JSONParserUtils.str(targetObject, "displayId"));

			JSONObject targetRepo = (JSONObject) targetObject.get("repository");
			pull.setTargetRepo(JSONParserUtils.str(targetRepo, "slug"));
			pull.setTargetRepoId(JSONParserUtils.str(targetRepo, "hierarchyId"));

			// Add reviewers name
			List<Reviewer> reviewerList = new ArrayList<>();
			JSONArray reviewersJsonArray = (JSONArray) jsonObject.get("reviewers");
			for (Object value : reviewersJsonArray) {
				JSONObject reviewerObject = (JSONObject) value;
				JSONObject user = (JSONObject) reviewerObject.get("user");

				Reviewer reviewer = new Reviewer();
				reviewer.setReviewerId(JSONParserUtils.str(user, "slug"));
				reviewer.setName(JSONParserUtils.str(user, "displayName"));

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
	 * Pull Requests activities like merge commit, comment, actions are being fetched
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
			URI uri = bitbucketApiUrlBuilder.buildPullRequestActivitiesApiUrl(repo.getRepoUrl(), pull.getNumber());
			pageUrl = new URIBuilder(uri).build();

			boolean lastPage = false;
			URI queryUrlPage = pageUrl;
			while (!lastPage) {
				LOG.info("sExecuting [" + queryUrlPage);
				ResponseEntity<String> response = scmHttpRestClient.makeRestCall(queryUrlPage, repoUser, repoPassword);
				JSONObject jsonArray = JSONParserUtils.parseAsObject(response);
				JSONArray values = (JSONArray) jsonArray.get("values");

				for (Object item : values) {
					JSONObject jsonObject = (JSONObject) item;
					String action = JSONParserUtils.str(jsonObject, "action");

					if (Objects.isNull(action) || action.isEmpty()) {
						continue;
					}

					if (ACTIVITY_COMMENT.equals(action)) {
						parseComments((JSONObject) jsonObject.get("comment"), commentList);
					}
					else if (ACTIVITY_APPROVE.equals(action)) {
						Long approvedAt = (Long) jsonObject.get("createdDate");

						JSONObject user = (JSONObject) jsonObject.get("user");

						Approver approver = new Approver();
						approver.setApprovedAt(approvedAt);
						approver.setApproverId((String) user.get("slug"));
						approver.setApproverName((String) user.get("displayName"));
						approverList.add(approver);

					}
					else if (ACTIVITY_MERGE.equals(action)) {
						Long mergedAt = (Long) jsonObject.get("createdDate");
						pull.setMergedAt(mergedAt);

						JSONObject user = (JSONObject) jsonObject.get("user");

						pull.setMergeAuthor((String) user.get("displayName"));
						pull.setMergeAuthorLDAPDN((String) user.get("slug"));

					}
					else if (ACTIVITY_DECLINE.equals(action)) {
						Long declinedAt = (Long) jsonObject.get("createdDate");
						pull.setClosedAt(declinedAt);

						JSONObject user = (JSONObject) jsonObject.get("user");

						pull.setClosedAuthor((String) user.get("displayName"));
						pull.setClosedAuthorLDAPDN((String) user.get("slug"));
					}
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
		pull.setApprovers(approverList);
	}

	private void parseComments(JSONObject commentObject, List<Comment> commentList) {

		JSONObject user = (JSONObject) commentObject.get("author");

		Comment comment = new Comment();
		comment.setUserLDAPDN(JSONParserUtils.str(user, "slug"));
		comment.setUser(JSONParserUtils.str(user, "displayName"));
		comment.setBody(JSONParserUtils.str(commentObject, "text"));
		comment.setCreatedAt((Long) commentObject.get("createdDate"));
		comment.setUpdatedAt((Long) commentObject.get("updatedDate"));
		commentList.add(comment);

		JSONArray subComments = (JSONArray) commentObject.get("comments");
		for (Object subComment : subComments) {
			parseComments((JSONObject) subComment, commentList);
		}
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
				JSONObject jsonArray = JSONParserUtils.parseAsObject(response);
				JSONArray values = (JSONArray) jsonArray.get("values");
				for (Object item : values) {
					JSONObject jsonObject = (JSONObject) item;
					String sha = JSONParserUtils.str(jsonObject, "id");

					// TODO: Verify with tanmay that this is correct solution of there is
					// a workaround
					if (sha.substring(0, 12).equalsIgnoreCase(pull.getScmRevisionNumber())) {
						pull.setScmRevisionNumber(sha);
						continue;
					}

					JSONObject authorObject = (JSONObject) jsonObject.get("author");
					String message = JSONParserUtils.str(jsonObject, "message");
					String author = "";
					if (JSONParserUtils.str(authorObject, "displayName") == null) {
						author = JSONParserUtils.str(authorObject, "name") + " <"
								+ JSONParserUtils.str(authorObject, "emailAddress") + ">";
					}
					else {
						author = JSONParserUtils.str(authorObject, "displayName") + " <"
								+ JSONParserUtils.str(authorObject, "emailAddress") + ">";
					}
					Long timestamp = (Long) jsonObject.get("authorTimestamp");
					JSONArray parents = (JSONArray) jsonObject.get("parents");
					List<String> parentShas = new ArrayList<>();
					if (parents != null) {
						for (Object parentObj : parents) {
							parentShas.add(JSONParserUtils.str((JSONObject) parentObj, "id"));
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

		// IMPORTANT NOTE
		// Unable to fetch comment list for Bitbucket Server
		// Path value is compulsory for fetching the comment list.
		// Use /activities end point to fetch general comment details from PR.
		// REF:
		// https://jira.atlassian.com/browse/BSERV-7643?error=login_required&error_description=Login+required&state=3fd81a17-37a7-4f18-8285-f22eb4acfa7b
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
			Long numberOfChanges = 0L;
			while (!lastPage) {
				LOG.info("Executing [" + queryUrlPage);
				ResponseEntity<String> response = scmHttpRestClient.makeRestCall(queryUrlPage, repoUser, repoPassword);
				JSONObject jsonArray = JSONParserUtils.parseAsObject(response);
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
