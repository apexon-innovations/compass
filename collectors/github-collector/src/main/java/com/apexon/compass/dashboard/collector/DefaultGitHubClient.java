package com.apexon.compass.dashboard.collector;

import com.apexon.compass.dashboard.client.RestOperationsSupplier;
import com.apexon.compass.dashboard.misc.HygieiaException;
import com.apexon.compass.dashboard.model.*;
import com.apexon.compass.dashboard.util.Encryption;
import com.apexon.compass.dashboard.util.EncryptionException;
import com.apexon.compass.fileupload.dto.FileUploadDto;
import com.apexon.compass.fileupload.service.UploadService;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.joda.time.DateTime;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestOperations;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.*;
import java.util.regex.Pattern;

/**
 * GitHubClient implementation that uses SVNKit to fetch information about Subversion
 * repositories.
 */
@Component
public class DefaultGitHubClient implements GitHubClient {

	private static final Log LOG = LogFactory.getLog(DefaultGitHubClient.class);

	private final GitHubSettings settings;

	private final RestOperations restOperations;

	private static final int FIRST_RUN_HISTORY_DEFAULT = 14;

	@Autowired
	private Environment environment;

	@Autowired
	private UploadService uploadService;

	private static final String BUCKET_NAME = "%s-des-github-data";

	private static final String DEV = "dev";

	private static final String DEFAULT_REGION = "us-east-2";

	@Autowired
	public DefaultGitHubClient(GitHubSettings settings, RestOperationsSupplier restOperationsSupplier) {
		this.settings = settings;
		this.restOperations = restOperationsSupplier.get();
	}

	/**
	 * Gets commits for a given repo
	 * @param repo
	 * @param firstRun
	 * @return list of commits
	 * @throws RestClientException
	 * @throws MalformedURLException
	 * @throws HygieiaException
	 */
	@Override
	public List<Commit> getCommits(GitHubRepo repo, boolean firstRun, List<Pattern> commitExclusionPatterns)
			throws RestClientException, MalformedURLException, HygieiaException {

		List<Commit> commits = new ArrayList<>();

		try {
			// format URL
			String repoUrl = (String) repo.getOptions().get("url");
			GitHubParsed gitHubParsed = new GitHubParsed(repoUrl);
			String apiUrl = gitHubParsed.getApiUrl();

			String queryUrl = apiUrl
				.concat("/commits?sha=" + repo.getBranch() + "&since=" + getTimeForApi(getRunDate(repo, firstRun)));
			String decryptedPassword = decryptString(repo.getPassword(), settings.getKey());
			String personalAccessToken = (String) repo.getOptions().get("personalAccessToken");
			String decryptedPersonalAccessToken = decryptString(personalAccessToken, settings.getKey());
			boolean lastPage = false;
			String queryUrlPage = queryUrl;
			while (!lastPage) {
				LOG.info("Executing " + queryUrlPage);
				ResponseEntity<String> response = makeRestCall(queryUrlPage, decryptedPersonalAccessToken);
				JSONArray jsonArray = parseAsArray(response);
				for (Object item : jsonArray) {
					JSONObject jsonObject = (JSONObject) item;
					String sha = str(jsonObject, "sha");
					JSONObject commitObject = (JSONObject) jsonObject.get("commit");
					JSONObject commitAuthorObject = (JSONObject) commitObject.get("author");
					String message = str(commitObject, "message");
					String author = str(commitAuthorObject, "name");
					long timestamp = new DateTime(str(commitAuthorObject, "date")).getMillis();
					JSONObject authorObject = (JSONObject) jsonObject.get("author");
					String authorLogin = "";
					if (authorObject != null) {
						authorLogin = str(authorObject, "login");
					}
					JSONArray parents = (JSONArray) jsonObject.get("parents");
					List<String> parentShas = new ArrayList<>();
					if (parents != null) {
						for (Object parentObj : parents) {
							parentShas.add(str((JSONObject) parentObj, "sha"));
						}
					}

					Commit commit = new Commit();
					commit.setTimestamp(System.currentTimeMillis());
					commit.setScmUrl(repo.getRepoUrl());
					commit.setScmBranch(repo.getBranch());
					commit.setScmRevisionNumber(sha);
					commit.setScmParentRevisionNumbers(parentShas);
					commit.setScmAuthor(author);
					commit.setScmAuthorLogin(authorLogin);
					commit.setScmCommitLog(message);
					commit.setScmCommitTimestamp(timestamp);
					commit.setNumberOfChanges(1);
					commit.setType(getCommitType(CollectionUtils.size(parents), message, commitExclusionPatterns));
					commits.add(commit);
				}
				if (CollectionUtils.isEmpty(jsonArray)) {
					lastPage = true;
				}
				else {
					if (isThisLastPage(response)) {
						lastPage = true;
					}
					else {
						lastPage = false;
						queryUrlPage = getNextPageUrl(response);
					}
				}
			}
		}
		catch (Exception e) {
			LOG.error(e.getMessage());
		}
		return commits;
	}

	private CommitType getCommitType(int parentSize, String commitMessage, List<Pattern> commitExclusionPatterns) {
		if (parentSize > 1)
			return CommitType.Merge;
		if (settings.getNotBuiltCommits() == null)
			return CommitType.New;
		if (!CollectionUtils.isEmpty(commitExclusionPatterns)) {
			for (Pattern pattern : commitExclusionPatterns) {
				if (pattern.matcher(commitMessage).matches()) {
					return CommitType.NotBuilt;
				}
			}
		}
		return CommitType.New;
	}

	/**
	 * Gets pulls for a given repo
	 * @param repo
	 * @param status
	 * @return list of pull request objects
	 * @throws MalformedURLException
	 * @throws HygieiaException
	 */
	@Override
	@SuppressWarnings({ "PMD.NPathComplexity", "PMD.ExcessiveMethodLength", "PMD.NcssMethodCount" }) // agreed,
																										// fixme
	public int getPulls(boolean firstRun, ScmConfiguration scmConfiguration, ScmRepo repo, String status)
			throws MalformedURLException, HygieiaException {

		List<DesGitRequest> pulls = new ArrayList<>();
		int pullCount = 0;

		try {
			GitHubParsed gitHubParsed = new GitHubParsed(repo.getRepoUrl());
			String branch = (repo.getDefaultBranchToScan() != null) ? repo.getDefaultBranchToScan() : "master";
			String personalAccessToken = scmConfiguration.getCredentials();
			String decryptedPersonalAccessToken = decryptString(personalAccessToken,
					scmConfiguration.getEncryptionKey());

			// NOTE
			// Writing this logic of search api for PRs on assumption that between last
			// updated date and now there will be no more than 1000 PRs
			// If more than 1000 PRs change the updated date to 0 and run the collector

			String pageUrl;
			if (firstRun) {
				pageUrl = gitHubParsed.getApiUrl()
					.concat("/pulls?state=" + status + "&base=" + branch + "&sort=updated&direction=desc");
			}
			else {
				Date lastUpdatedDate = new Date(scmConfiguration.getUpdatedDate());
				DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
				String lastUpdatedDateString = formatter.format(lastUpdatedDate);
				pageUrl = gitHubParsed.getSearchPrApiUrl()
					.concat("base:" + repo.getDefaultBranchToScan() + " updated:>=" + lastUpdatedDateString);
			}

			boolean lastPage = false;
			String queryUrlPage = pageUrl;

			while (!lastPage) {
				LOG.info("Executing [" + queryUrlPage);
				ResponseEntity<String> response;
				JSONArray jsonArray;

				response = makeRestCall(queryUrlPage, decryptedPersonalAccessToken);
				if (firstRun) {
					jsonArray = parseAsArray(response);
				}
				else {
					// response = makeRestCall(queryUrlPage,
					// decryptedPersonalAccessToken);
					JSONObject result = parseAsObject(response);
					jsonArray = (JSONArray) result.get("items");
				}

				for (Object item : jsonArray) {

					JSONObject jsonObject = (JSONObject) item;
					String message = str(jsonObject, "title");
					String number = str(jsonObject, "number");
					LOG.info("pr " + number + " " + message);

					JSONObject userObject = (JSONObject) jsonObject.get("user");
					String name = str(userObject, "login");
					String userId = str(userObject, "id");

					// Create necessary URLs from JSONObject
					String prUrl = gitHubParsed.getApiUrl().concat("/pulls/").concat(number);
					String issueUrl = gitHubParsed.getApiUrl().concat("/issues/").concat(number);

					DesGitRequest pull = new DesGitRequest();

					pull.setIscProjectId(scmConfiguration.getIscProjectId().toString());
					pull.setScmConfigurationId(scmConfiguration.getId().toString());
					pull.setNumber(number);
					pull.setScmCommitLog(message);
					pull.setUserId(userId);
					pull.setScmAuthor(name);
					pull.setScmUrl(repo.getRepoUrl());
					pull.setScmBranch(branch);
					pull.setRequestType("pull");
					pull.setMergedAt(0);
					pull.setClosedAt(0);
					pull.setOrgName(gitHubParsed.getOrgName());
					pull.setRepoName(gitHubParsed.getRepoName());

					Map<String, String> urlList = populatePullRequestMetadata(prUrl, issueUrl, scmConfiguration, pull);
					String commitsUrl = urlList.get("commits_url");
					String reviewCommentsUrl = urlList.get("review_comments_url");
					String commentsUrl = str(jsonObject, "comments_url");
					String reviewsUrl = prUrl + "/reviews";

					pull.setCommentsUrl(commentsUrl);

					populatePullRequestsReviewersApproversAndComments(reviewsUrl, scmConfiguration, pull);

					List<Comment> comments = CollectionUtils.isNotEmpty(pull.getComments()) ? pull.getComments()
							: new ArrayList<>();
					comments.addAll(populatePullRequestsComments(reviewCommentsUrl, scmConfiguration));
					comments.addAll(populateIssueComments(commentsUrl, scmConfiguration));
					pull.setComments(comments);

					populatePullRequestCommits(commitsUrl, scmConfiguration, repo, pull);

					pulls.add(pull);

					// Upload Pull request JSON file to S3 Bucket
					// perform batch uploads in a set of 100
					if (pulls.size() >= 100) {
						uploadToS3(pulls);
						pullCount += pulls.size();
						pulls.clear();
					}
				}

				// check if next page urls is available or not
				// if available then process further
				if (CollectionUtils.isEmpty(jsonArray)) {
					lastPage = true;
				}
				else {
					if (isThisLastPage(response)) {
						lastPage = true;
					}
					else {
						lastPage = false;
						queryUrlPage = getNextPageUrl(response);
					}
				}
			}

			// Upload rest of the PRs
			uploadToS3(pulls);
			pullCount += pulls.size();
			pulls.clear();

		}
		catch (Exception e) {
			LOG.error(e.getMessage());
		}

		return pullCount;
	}

	/**
	 * Gets issues for a repo
	 * @param repo
	 * @param firstRun
	 * @return list of issues
	 * @throws MalformedURLException
	 * @throws HygieiaException
	 */
	@Override
	public List<DesGitRequest> getIssues(GitHubRepo repo, boolean firstRun)
			throws MalformedURLException, HygieiaException {

		List<DesGitRequest> issues = new ArrayList<>();
		try {

			GitHubParsed gitHubParsed = new GitHubParsed((String) repo.getOptions().get("url"));
			String apiUrl = gitHubParsed.getApiUrl();
			// decrypt password
			String decryptedPassword = decryptString(repo.getPassword(), settings.getKey());
			String queryUrl = apiUrl.concat("/issues?state=all&since=" + getTimeForApi(getRunDate(repo, firstRun)));
			String personalAccessToken = (String) repo.getOptions().get("personalAccessToken");
			String decryptedPersonalAccessToken = decryptString(personalAccessToken, settings.getKey());
			boolean lastPage = false;
			String queryUrlPage = queryUrl;
			while (!lastPage) {
				LOG.info("Executing " + queryUrlPage);
				ResponseEntity<String> response = makeRestCall(queryUrlPage, decryptedPersonalAccessToken);
				JSONArray jsonArray = parseAsArray(response);
				for (Object item : jsonArray) {
					JSONObject jsonObject = (JSONObject) item;
					// pull requests are also issues
					if (jsonObject.get("pull_request") != null) {
						continue;
					}
					String message = str(jsonObject, "title");
					String number = str(jsonObject, "number");

					JSONObject userObject = (JSONObject) jsonObject.get("user");
					String name = str(userObject, "login");
					String created = str(jsonObject, "created_at");
					String closed = str(jsonObject, "closed_at");
					long createdTimestamp = new DateTime(created).getMillis();
					DesGitRequest issue = new DesGitRequest();
					if (closed != null && closed.length() >= 10) {
						long mergedTimestamp = new DateTime(closed).getMillis();
						issue.setScmCommitTimestamp(mergedTimestamp);
						issue.setResolutiontime((mergedTimestamp - createdTimestamp) / (24 * 3600000));
					}
					issue.setUserId(name);
					issue.setScmUrl(repo.getRepoUrl());
					issue.setTimestamp(createdTimestamp);
					issue.setScmRevisionNumber(number);
					issue.setScmCommitLog(message);
					issue.setCreatedAt(createdTimestamp);
					issue.setClosedAt(new DateTime(closed).getMillis());
					issue.setNumber(number);
					issue.setRequestType("issue");
					if (closed != null) {
						issue.setState("closed");
					}
					else {
						issue.setState("open");
					}
					issue.setOrgName(gitHubParsed.getOrgName());
					issue.setRepoName(gitHubParsed.getRepoName());
					issues.add(issue);
				}

				if (CollectionUtils.isEmpty(jsonArray)) {
					lastPage = true;
				}
				else {
					if (isThisLastPage(response)) {
						lastPage = true;
					}
					else {
						lastPage = false;
						queryUrlPage = getNextPageUrl(response);
					}
				}
			}
		}
		catch (Exception e) {
			LOG.error(e.getMessage());
		}
		return issues;
	}

	/**
	 * Get comments from the given comment url
	 * @param commentsUrl
	 * @param scmConfiguration
	 * @return
	 * @throws RestClientException
	 */
	public List<Comment> getComments(String commentsUrl, ScmConfiguration scmConfiguration) throws RestClientException {

		List<Comment> comments = new ArrayList<>();
		try {
			// decrypt token
			String personalAccessToken = scmConfiguration.getCredentials();
			String decryptedPersonalAccessToken = decryptString(personalAccessToken,
					scmConfiguration.getEncryptionKey());

			boolean lastPage = false;
			String queryUrlPage = commentsUrl;
			while (!lastPage) {
				ResponseEntity<String> response = makeRestCall(queryUrlPage, decryptedPersonalAccessToken);
				JSONArray jsonArray = parseAsArray(response);
				for (Object item : jsonArray) {
					JSONObject jsonObject = (JSONObject) item;

					Comment comment = new Comment();
					JSONObject userJsonObj = (JSONObject) jsonObject.get("user");
					comment.setUser((String) userJsonObj.get("login"));
					long crt = new DateTime(str(jsonObject, "created_at")).getMillis();
					comment.setCreatedAt(crt);
					long upd = new DateTime(str(jsonObject, "updated_at")).getMillis();
					comment.setUpdatedAt(upd);
					comment.setBody(str(jsonObject, "body"));
					comments.add(comment);
				}
				if (CollectionUtils.isEmpty(jsonArray)) {
					lastPage = true;
				}
				else {
					if (isThisLastPage(response)) {
						lastPage = true;
					}
					else {
						lastPage = false;
						queryUrlPage = getNextPageUrl(response);
					}
				}
			}
		}
		catch (Exception e) {
			LOG.error(e.getMessage());
		}
		return comments;
	}

	/**
	 * Get commit statuses from the given commit status url Retrieve the most recent
	 * status for each unique context.
	 *
	 * <p>
	 * See
	 * https://developer.github.com/v3/repos/statuses/#list-statuses-for-a-specific-ref
	 * and
	 * https://developer.github.com/v3/repos/statuses/#get-the-combined-status-for-a-specific-ref
	 * @param statusUrl
	 * @param scmConfiguration
	 * @return
	 * @throws RestClientException
	 */
	public List<CommitStatus> getCommitStatuses(String statusUrl, ScmConfiguration scmConfiguration)
			throws RestClientException {

		Map<String, CommitStatus> statuses = new HashMap<>();
		try {

			// decrypt token
			String personalAccessToken = scmConfiguration.getCredentials();
			String decryptedPersonalAccessToken = decryptString(personalAccessToken,
					scmConfiguration.getEncryptionKey());

			boolean lastPage = false;
			String queryUrlPage = statusUrl;
			while (!lastPage) {
				ResponseEntity<String> response = makeRestCall(queryUrlPage, decryptedPersonalAccessToken);
				JSONArray jsonArray = parseAsArray(response);
				for (Object item : jsonArray) {
					JSONObject jsonObject = (JSONObject) item;

					String context = str(jsonObject, "context");
					if ((context != null) && !statuses.containsKey(context)) {
						CommitStatus status = new CommitStatus();
						status.setContext(context);
						status.setDescription(str(jsonObject, "description"));
						status.setState(str(jsonObject, "state"));
						statuses.put(context, status);
					}
				}
				if (CollectionUtils.isEmpty(jsonArray)) {
					lastPage = true;
				}
				else {
					if (isThisLastPage(response)) {
						lastPage = true;
					}
					else {
						lastPage = false;
						queryUrlPage = getNextPageUrl(response);
					}
				}
			}
		}
		catch (Exception e) {
			LOG.error(e.getMessage());
		}
		return new ArrayList<>(statuses.values());
	}

	/**
	 * Get reviews from the given reviews url
	 * @param reviewsUrl
	 * @param scmConfiguration
	 * @param pull
	 * @return
	 * @throws RestClientException
	 */
	private void populatePullRequestsReviewersApproversAndComments(String reviewsUrl, ScmConfiguration scmConfiguration,
			DesGitRequest pull) throws RestClientException {

		List<Reviewer> reviewers = pull.getReviewers();
		List<Approver> approvers = new ArrayList<>();
		List<Comment> comments = new ArrayList<>();

		try {
			// decrypt token
			String personalAccessToken = scmConfiguration.getCredentials();
			String decryptedPersonalAccessToken = decryptString(personalAccessToken,
					scmConfiguration.getEncryptionKey());

			boolean lastPage = false;
			String queryUrlPage = reviewsUrl;

			while (!lastPage) {
				LOG.info("Executing [" + queryUrlPage);
				ResponseEntity<String> response = makeRestCall(queryUrlPage, decryptedPersonalAccessToken);
				JSONArray jsonArray = parseAsArray(response);
				for (Object item : jsonArray) {
					JSONObject jsonObject = (JSONObject) item;
					String status = str(jsonObject, "state");
					if (status != null && status.equalsIgnoreCase("APPROVED")) {
						JSONObject userObject = (JSONObject) jsonObject.get("user");
						Approver approver = new Approver();
						approver.setApproverName(str(userObject, "login"));
						approver.setApproverId(str(userObject, "id"));
						String submittedDate = str(jsonObject, "submitted_at");
						if (submittedDate != null) {
							approver.setApprovedAt(new DateTime(submittedDate).getMillis());
						}
						approvers.add(approver);
					}

					JSONObject userObject = (JSONObject) jsonObject.get("user");
					String name = str(userObject, "login");
					if (reviewers.stream().noneMatch(reviewer -> reviewer.getName().equals(name))
							&& !pull.getScmAuthor().equals(name)) {
						Reviewer reviewer = new Reviewer();
						reviewer.setName(name);
						reviewer.setReviewerId(str(userObject, "id"));
						reviewers.add(reviewer);
					}

					String body = str(jsonObject, "body");
					if (StringUtils.isNotBlank(body)) {
						Comment comment = new Comment();
						comment.setBody(body);
						comment.setUser(str(userObject, "login"));
						comment.setUserLDAPDN(str(userObject, "id"));
						comment.setCreatedAt(new DateTime(str(jsonObject, "submitted_at")).getMillis());
						comments.add(comment);
					}
				}
				if (CollectionUtils.isEmpty(jsonArray)) {
					lastPage = true;
				}
				else {
					if (isThisLastPage(response)) {
						lastPage = true;
					}
					else {
						lastPage = false;
						queryUrlPage = getNextPageUrl(response);
					}
				}
			}
		}
		catch (Exception e) {
			LOG.error(e.getMessage());
		}

		pull.setApprovers(approvers);
		pull.setReviewers(reviewers);
		pull.setComments(comments);

	}

	/**
	 * Get comments from the given pull requests url
	 * @param reviewCommentsUrl
	 * @param scmConfiguration
	 * @return returns list of comments
	 * @throws RestClientException
	 */
	private List<Comment> populatePullRequestsComments(String reviewCommentsUrl, ScmConfiguration scmConfiguration) {

		List<Comment> comments = new ArrayList<>();

		try {

			// decrypt token
			String personalAccessToken = scmConfiguration.getCredentials();
			String decryptedPersonalAccessToken = decryptString(personalAccessToken,
					scmConfiguration.getEncryptionKey());

			boolean lastPage = false;
			String queryUrlPage = reviewCommentsUrl;

			while (!lastPage) {
				LOG.info("Executing [" + queryUrlPage);
				ResponseEntity<String> response = makeRestCall(queryUrlPage, decryptedPersonalAccessToken);
				JSONArray jsonArray = parseAsArray(response);
				for (Object item : jsonArray) {
					JSONObject jsonObject = (JSONObject) item;
					Comment comment = new Comment();
					JSONObject userJsonObj = (JSONObject) jsonObject.get("user");
					comment.setUser((String) userJsonObj.get("login"));
					comment.setUserLDAPDN(str(userJsonObj, "id"));
					long crt = new DateTime(str(jsonObject, "created_at")).getMillis();
					comment.setCreatedAt(crt);
					long upd = new DateTime(str(jsonObject, "updated_at")).getMillis();
					comment.setUpdatedAt(upd);
					comment.setBody(str(jsonObject, "body"));
					comments.add(comment);
				}
				if (CollectionUtils.isEmpty(jsonArray)) {
					lastPage = true;
				}
				else {
					if (isThisLastPage(response)) {
						lastPage = true;
					}
					else {
						lastPage = false;
						queryUrlPage = getNextPageUrl(response);
					}
				}
			}
		}
		catch (Exception e) {
			LOG.error(e.getMessage());
		}
		return comments;

	}

	/**
	 * Get comments from the given issues requests url
	 * @param commentsUrl
	 * @param scmConfiguration
	 * @return returns list of comments
	 * @throws RestClientException
	 */
	private List<Comment> populateIssueComments(String commentsUrl, ScmConfiguration scmConfiguration) {

		List<Comment> comments = new ArrayList<>();
		try {

			// decrypt token
			String personalAccessToken = scmConfiguration.getCredentials();
			String decryptedPersonalAccessToken = decryptString(personalAccessToken,
					scmConfiguration.getEncryptionKey());

			boolean lastPage = false;
			String queryUrlPage = commentsUrl;

			while (!lastPage) {
				LOG.info("Executing [" + queryUrlPage);
				ResponseEntity<String> response = makeRestCall(queryUrlPage, decryptedPersonalAccessToken);
				JSONArray jsonArray = parseAsArray(response);
				for (Object item : jsonArray) {
					JSONObject jsonObject = (JSONObject) item;
					Comment comment = new Comment();
					JSONObject userJsonObj = (JSONObject) jsonObject.get("user");
					comment.setUser((String) userJsonObj.get("login"));
					comment.setUserLDAPDN(str(userJsonObj, "id"));
					long crt = new DateTime(str(jsonObject, "created_at")).getMillis();
					comment.setCreatedAt(crt);
					long upd = new DateTime(str(jsonObject, "updated_at")).getMillis();
					comment.setUpdatedAt(upd);
					comment.setBody(str(jsonObject, "body"));
					comments.add(comment);
				}
				if (CollectionUtils.isEmpty(jsonArray)) {
					lastPage = true;
				}
				else {
					if (isThisLastPage(response)) {
						lastPage = true;
					}
					else {
						lastPage = false;
						queryUrlPage = getNextPageUrl(response);
					}
				}
			}
		}
		catch (Exception e) {
			LOG.error(e.getMessage());
		}
		return comments;
	}

	/**
	 * Get reviews from the given reviews url
	 * @param scmConfiguration
	 * @param pull
	 * @return map of 2 url for review and commits
	 * @throws RuntimeException
	 */
	public Map<String, String> populatePullRequestMetadata(String selfUrl, String issueUrl,
			ScmConfiguration scmConfiguration, DesGitRequest pull) throws RuntimeException {

		Map<String, String> urlList = new HashMap<>();

		try {

			// decrypt token
			String personalAccessToken = scmConfiguration.getCredentials();
			String decryptedPersonalAccessToken = decryptString(personalAccessToken,
					scmConfiguration.getEncryptionKey());

			LOG.info("Executing [" + selfUrl);
			ResponseEntity<String> response = makeRestCall(selfUrl, decryptedPersonalAccessToken);
			JSONObject jsonObject = parseAsObject(response);

			String sha = str(jsonObject, "merge_commit_sha");
			String created = str(jsonObject, "created_at");
			String merged = str(jsonObject, "merged_at");
			String closed = str(jsonObject, "closed_at");
			String updated = str(jsonObject, "updated_at");

			String commitsUrl = str(jsonObject, "commits_url");
			String reviewCommentsUrl = str(jsonObject, "review_comments_url");

			urlList.put("commits_url", commitsUrl);
			urlList.put("review_comments_url", reviewCommentsUrl);

			long createdTimestamp = new DateTime(created).getMillis();
			if (merged != null && merged.length() >= 10) {
				long mergedTimestamp = new DateTime(merged).getMillis();
				pull.setScmCommitTimestamp(mergedTimestamp);
				pull.setResolutiontime((mergedTimestamp - createdTimestamp) / (24 * 3600000));
			}
			pull.setTimestamp(createdTimestamp);
			pull.setScmRevisionNumber(sha);
			pull.setCreatedAt(createdTimestamp);
			pull.setUpdatedAt(new DateTime(updated).getMillis());
			pull.setState("open");
			if (merged != null) {
				pull.setState("merged");
				pull.setMergedAt(new DateTime(merged).getMillis());
			}
			else if (closed != null) {
				pull.setState("declined");
				pull.setClosedAt(new DateTime(closed).getMillis());
			}
			JSONObject headObject = (JSONObject) jsonObject.get("head");
			if (headObject != null) {
				String headSha = str(headObject, "sha");
				pull.setHeadSha(headSha);
				pull.setSourceBranch(str(headObject, "ref"));
				JSONObject headRepoObject = (JSONObject) headObject.get("repo");
				if (headRepoObject != null) {
					pull.setSourceRepo(str(headRepoObject, "full_name"));
				}

				pull.setSourceBranch(str(headObject, "label"));
				pull.setSourceRepoId(str(headObject, "sha"));
			}

			JSONObject baseObject = (JSONObject) jsonObject.get("base");
			if (baseObject != null) {
				pull.setBaseSha(str(baseObject, "sha"));
				pull.setTargetBranch(str(baseObject, "ref"));
				JSONObject baseRepoObject = (JSONObject) baseObject.get("repo");
				if (baseRepoObject != null) {
					pull.setTargetRepo(str(baseRepoObject, "full_name"));
				}

				pull.setTargetBranch(str(baseObject, "label"));
				pull.setTargetRepoId(str(baseObject, "sha"));
			}

			List<Reviewer> reviewers = new ArrayList<>();
			JSONArray requestedReviewers = (JSONArray) jsonObject.get("requested_reviewers");
			for (Object requestedReviewerObject : requestedReviewers) {
				JSONObject requestedReviewer = (JSONObject) requestedReviewerObject;
				String reviewerName = str(requestedReviewer, "login");
				if (reviewers.stream().noneMatch(reviewer -> reviewer.getName().equals(reviewerName))
						&& !pull.getScmAuthor().equals(reviewerName)) {
					Reviewer reviewer = new Reviewer();
					reviewer.setName(reviewerName);
					reviewer.setReviewerId(str(requestedReviewer, "id"));
					reviewers.add(reviewer);
				}
			}
			pull.setReviewers(reviewers);

			if (pull.getState().equals("merged")) {
				try {

					JSONObject mergedBy = (JSONObject) jsonObject.get("merged_by");
					if (mergedBy != null) {
						pull.setMergeAuthor(str(mergedBy, "login"));
						pull.setMergeAuthorLDAPDN(str(mergedBy, "id"));
					}

				}
				catch (Exception e) {
					throw new RuntimeException("Unable to parse metadata: ", e);
				}
			}
			else if (pull.getState().equals("declined")) {

				try {
					LOG.info("Executing [" + issueUrl);
					ResponseEntity<String> issueResponse = makeRestCall(issueUrl, decryptedPersonalAccessToken);
					JSONObject issueJsonObject = parseAsObject(issueResponse);

					JSONObject closedBy = (JSONObject) issueJsonObject.get("closed_by");
					if (closedBy != null) {
						pull.setClosedAuthor(str(closedBy, "login"));
						pull.setClosedAuthorLDAPDN(str(closedBy, "id"));
					}

				}
				catch (Exception e) {
					throw new RuntimeException("Unable to parse metadata: ", e);
				}
			}
		}
		catch (Exception e) {
			LOG.error(e.getMessage());
		}

		return urlList;
	}

	/**
	 * Get reviews from the given reviews url
	 * @param commitsUrl
	 * @param scmConfiguration
	 * @param repo
	 * @param pull
	 * @throws RestClientException
	 */
	public void populatePullRequestCommits(String commitsUrl, ScmConfiguration scmConfiguration, ScmRepo repo,
			DesGitRequest pull) throws RestClientException {

		try {

			List<Commit> commits = new ArrayList<>();

			// decrypt token
			String personalAccessToken = scmConfiguration.getCredentials();
			String decryptedPersonalAccessToken = decryptString(personalAccessToken,
					scmConfiguration.getEncryptionKey());

			boolean lastPage = false;
			String queryUrlPage = commitsUrl;
			while (!lastPage) {
				LOG.info("Executing [" + queryUrlPage);
				ResponseEntity<String> response = makeRestCall(queryUrlPage, decryptedPersonalAccessToken);
				JSONArray jsonArray = parseAsArray(response);
				for (Object item : jsonArray) {
					JSONObject jsonObject = (JSONObject) item;
					String sha = str(jsonObject, "sha");
					JSONObject commitObject = (JSONObject) jsonObject.get("commit");
					JSONObject commitAuthorObject = (JSONObject) commitObject.get("author");
					String message = str(commitObject, "message");
					String author = str(commitAuthorObject, "name");
					long timestamp = new DateTime(str(commitAuthorObject, "date")).getMillis();
					JSONObject authorObject = (JSONObject) jsonObject.get("author");
					String authorLogin = "";
					String authorLDAPDN = "";
					if (authorObject != null) {
						authorLogin = str(authorObject, "login");
						authorLDAPDN = str(authorObject, "id");
					}
					JSONArray parents = (JSONArray) jsonObject.get("parents");
					List<String> parentShas = new ArrayList<>();
					if (parents != null) {
						for (Object parentObj : parents) {
							parentShas.add(str((JSONObject) parentObj, "sha"));
						}
					}

					Commit commit = new Commit();
					commit.setTimestamp(System.currentTimeMillis());
					commit.setScmUrl(repo.getRepoUrl());
					commit.setScmBranch(repo.getDefaultBranchToScan());
					commit.setScmRevisionNumber(sha);
					commit.setScmParentRevisionNumbers(parentShas);
					commit.setScmAuthor(author);
					commit.setScmAuthorLogin(authorLogin);
					commit.setScmAuthorLDAPDN(authorLDAPDN);
					commit.setScmCommitLog(message);
					commit.setScmCommitTimestamp(timestamp);
					// TODO: need to check the number of changes in commits api or
					// somewhere.
					// commit.setNumberOfChanges(1);
					// commit.setType(getCommitType(CollectionUtils.size(parents),
					// message));
					commits.add(commit);
				}
				if (CollectionUtils.isEmpty(jsonArray)) {
					lastPage = true;
				}
				else {
					if (isThisLastPage(response)) {
						lastPage = true;
					}
					else {
						lastPage = false;
						queryUrlPage = getNextPageUrl(response);
					}
				}
				pull.setCommits(commits);
			}
		}
		catch (Exception e) {
			LOG.error(e.getMessage());
		}
	}

	// Utilities

	/**
	 * See if it is the last page: obtained from the response header
	 * @param response
	 * @return
	 */
	private boolean isThisLastPage(ResponseEntity<String> response) {
		HttpHeaders header = response.getHeaders();
		List<String> link = header.get("Link");
		if (link == null || link.isEmpty()) {
			return true;
		}
		else {
			for (String l : link) {
				if (l.contains("rel=\"next\"")) {
					return false;
				}
			}
		}
		return true;
	}

	private String getNextPageUrl(ResponseEntity<String> response) {
		String nextPageUrl = "";
		HttpHeaders header = response.getHeaders();
		List<String> link = header.get("Link");
		if (link == null || link.isEmpty()) {
			return nextPageUrl;
		}
		else {
			for (String l : link) {
				if (l.contains("rel=\"next\"")) {
					String[] parts = l.split(",");
					if (parts != null && parts.length > 0) {
						for (int i = 0; i < parts.length; i++) {
							if (parts[i].contains("rel=\"next\"")) {
								nextPageUrl = parts[i].split(";")[0];
								nextPageUrl = nextPageUrl.replaceFirst("<", "");
								nextPageUrl = nextPageUrl.replaceFirst(">", "").trim();
								// Github Link headers for 'next' and 'last' are URL
								// Encoded
								String decodedPageUrl;
								try {
									decodedPageUrl = URLDecoder.decode(nextPageUrl, StandardCharsets.UTF_8.name());
								}
								catch (UnsupportedEncodingException e) {
									decodedPageUrl = URLDecoder.decode(nextPageUrl);
								}
								return decodedPageUrl;
							}
						}
					}
				}
			}
		}
		return nextPageUrl;
	}

	/**
	 * Checks rate limit
	 * @param response
	 * @return boolean
	 */
	private boolean isRateLimitReached(ResponseEntity<String> response) {
		HttpHeaders header = response.getHeaders();
		List<String> limit = header.get("X-RateLimit-Remaining");
		boolean rateLimitReached = CollectionUtils.isEmpty(limit) ? false
				: Integer.valueOf(limit.get(0)) < settings.getRateLimitThreshold();
		if (rateLimitReached) {
			LOG.error("Github rate limit reached. Threshold =" + settings.getRateLimitThreshold()
					+ ". Current remaining =" + Integer.valueOf(limit.get(0)));
		}
		return rateLimitReached;
	}

	private ResponseEntity<String> makeRestCall(String url, String personalAccessToken) {
		if ((personalAccessToken != null && !"".equals(personalAccessToken))) {
			return restOperations.exchange(url, HttpMethod.GET, new HttpEntity<>(createHeaders(personalAccessToken)),
					String.class);
		}
		else if (settings.getPersonalAccessToken() != null && !"".equals(settings.getPersonalAccessToken())) {
			return restOperations.exchange(url, HttpMethod.GET,
					new HttpEntity<>(createHeaders(settings.getPersonalAccessToken())), String.class);
		}
		else {
			return restOperations.exchange(url, HttpMethod.GET, null, String.class);
		}
	}

	private HttpHeaders createHeaders(final String userId, final String password) {
		String auth = userId + ":" + password;
		byte[] encodedAuth = Base64.encodeBase64(auth.getBytes(StandardCharsets.US_ASCII));
		String authHeader = "Basic " + new String(encodedAuth);

		HttpHeaders headers = new HttpHeaders();
		headers.set("Authorization", authHeader);
		return headers;
	}

	private HttpHeaders createHeaders(final String token) {
		String authHeader = "token " + token;

		HttpHeaders headers = new HttpHeaders();
		headers.set("Authorization", authHeader);
		return headers;
	}

	private JSONArray parseAsArray(ResponseEntity<String> response) {
		try {
			return (JSONArray) new JSONParser().parse(response.getBody());
		}
		catch (ParseException pe) {
			LOG.error(pe.getMessage());
		}
		return new JSONArray();
	}

	private JSONObject parseAsObject(ResponseEntity<String> response) {
		try {
			return (JSONObject) new JSONParser().parse(response.getBody());
		}
		catch (ParseException pe) {
			LOG.error(pe.getMessage());
		}
		return new JSONObject();
	}

	private int asInt(JSONObject json, String key) {
		String val = str(json, key);
		try {
			if (val != null) {
				return Integer.parseInt(val);
			}
		}
		catch (NumberFormatException ex) {
			LOG.error(ex.getMessage());
		}
		return 0;
	}

	private String str(JSONObject json, String key) {
		Object value = json.get(key);
		return value == null ? null : value.toString();
	}

	/**
	 * Get run date based off of firstRun boolean
	 * @param repo
	 * @param firstRun
	 * @return
	 */
	private Date getRunDate(GitHubRepo repo, boolean firstRun) {
		if (firstRun) {
			int firstRunDaysHistory = settings.getFirstRunHistoryDays();
			if (firstRunDaysHistory > 0) {
				return getDate(new Date(), -firstRunDaysHistory, 0);
			}
			else {
				return getDate(new Date(), -FIRST_RUN_HISTORY_DEFAULT, 0);
			}
		}
		else {
			return getDate(new Date(repo.getLastUpdated()), 0, -10);
		}
	}

	/**
	 * Date utility
	 * @param dateInstance
	 * @param offsetDays
	 * @param offsetMinutes
	 * @return
	 */
	private Date getDate(Date dateInstance, int offsetDays, int offsetMinutes) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(dateInstance);
		cal.add(Calendar.DATE, offsetDays);
		cal.add(Calendar.MINUTE, offsetMinutes);
		return cal.getTime();
	}

	/**
	 * Decrypt string
	 * @param string
	 * @param key
	 * @return String
	 */
	public static String decryptString(String string, String key) throws EncryptionException {
		if (!StringUtils.isEmpty(string)) {
			try {
				return Encryption.decryptString(string, key);
			}
			catch (EncryptionException e) {
				LOG.error(e.getMessage());
				throw e;
			}
		}
		return "";
	}

	/**
	 * Format date the way Github api wants
	 * @param dt
	 * @return String
	 */
	private static String getTimeForApi(Date dt) {
		Calendar calendar = new GregorianCalendar();
		TimeZone timeZone = calendar.getTimeZone();
		Calendar cal = Calendar.getInstance(timeZone);
		cal.setTime(dt);
		return String.format("%tFT%<tRZ", cal);
	}

	/**
	 * Convert Pull Requests to JSON array and upload to S3 bucket for further processing
	 * @param pullRequests
	 */
	private void uploadToS3(List<DesGitRequest> pullRequests) {
		try {
			String REGION = Objects.isNull(environment.getProperty("AWS_REGION")) ? DEFAULT_REGION
					: environment.getProperty("AWS_REGION");
			if (pullRequests.size() > 0) {
				DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
				String dateString = formatter.format(Calendar.getInstance().getTime());
				OffsetDateTime utc = OffsetDateTime.now(ZoneOffset.UTC);
				long epochMillis = utc.toEpochSecond() * 1000;
				String pullRequestFilePath = dateString + "/pullrequest_" + epochMillis + ".json";

				// Upload pull requests data to S3 bucket as per the environment
				Gson gson = new GsonBuilder().serializeNulls().create();
				String pullRequestData = gson.toJson(pullRequests);
				FileUploadDto pullRequestUpload = new FileUploadDto(REGION, String.valueOf(getBucketName()),
						pullRequestFilePath, pullRequestData);
				uploadService.uploadToStorage(pullRequestUpload);
				LOG.info(pullRequestFilePath + " file uploaded to S3.");
			}
		}
		catch (ClassCastException e) {
			LOG.info("Class Cast Exception:", e);
		}
		catch (Exception e) {
			LOG.error("Unable to upload file to S3: ", e);
		}
	}

	/**
	 * Get S3 bucket name to upload file to S3
	 */
	private StringBuilder getBucketName() {
		StringBuilder bucketName = new StringBuilder();
		if (Arrays.stream(environment.getActiveProfiles()).count() > 0) {
			return bucketName.append(String.format(BUCKET_NAME,
					Arrays.asList(environment.getActiveProfiles()).stream().findAny().get()));
		}
		else {
			return bucketName.append(String.format(BUCKET_NAME, DEV));
		}
	}

}

// X-RateLimit-Remaining
