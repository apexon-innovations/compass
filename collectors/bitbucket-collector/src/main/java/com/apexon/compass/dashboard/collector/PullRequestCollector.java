package com.apexon.compass.dashboard.collector;

import com.apexon.compass.dashboard.bitbucketapi.BitbucketApiUrlBuilder;
import com.apexon.compass.dashboard.model.*;
import com.apexon.compass.dashboard.repository.CommitRepository;
import com.apexon.compass.dashboard.util.Encryption;
import com.apexon.compass.fileupload.dto.FileUploadDto;
import com.apexon.compass.fileupload.service.UploadService;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.client.utils.URIBuilder;
import org.joda.time.DateTime;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.net.URISyntaxException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.*;

@Component
public class PullRequestCollector {

	private static final Log LOG = LogFactory.getLog(PullRequestCollector.class);

	private static final String PULL = "pull";

	private static final String OPEN = "open";

	private static final String DECLINED = "declined";

	private static final String MERGED = "merged";

	private BitbucketApiUrlBuilder bitbucketApiUrlBuilder;

	@Autowired
	private SCMHttpRestClient scmHttpRestClient;

	@Autowired
	private CommitRepository commitRepository;

	@Autowired
	private Environment environment;

	PullRequestCollectorClient pullRequestCollectorClient;

	@Autowired
	private UploadService uploadService;

	private static final String BUCKET_NAME = "%s-des-bitbucket-data";

	private static final String DEV = "dev";

	private static final String DEFAULT_REGION = "us-east-2";

	/**
	 * This method fetches pull-request using Bitbucket REST APIs and stores them to
	 * Hygieia DB. We can stop this processing as soon as we find a PR which has not
	 * changed(updateAt is same in Hygieia DB and REST response).
	 * @param scmConfiguration Bitbucket ScmConfiguration object
	 * @param status open/merged
	 * @return
	 */
	@SuppressWarnings("PMD.NPathComplexity")
	public int getPullRequests(ScmConfiguration scmConfiguration, String status) {

		bitbucketApiUrlBuilder = new BitbucketApiUrlBuilder(scmConfiguration);
		if (scmConfiguration.getProduct().equalsIgnoreCase("cloud")) {
			pullRequestCollectorClient = new PullRequestCollectorCloudClient(scmConfiguration, this.scmHttpRestClient);
		}
		else {
			pullRequestCollectorClient = new PullRequestCollectorServerClient(scmConfiguration, this.scmHttpRestClient);
		}

		List<DesGitRequest> pulls;
		List<DesGitRequest> pullRequests = new ArrayList<>();
		int pullCount = 0;

		// Get repo credentials
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
			// if git credentials not given then ignore
			return pullCount;
		}

		// Fetch repo pull requests
		for (ScmRepo repo : scmConfiguration.getScm()) {

			String branch = (repo.getDefaultBranchToScan() != null) ? repo.getDefaultBranchToScan() : "master";

			URI pageUrl;
			try {
				URI uri = bitbucketApiUrlBuilder.buildPullRequestApiUrl(repo.getRepoUrl());
				String branchId = "refs/heads/" + branch;
				Date lastUpdatedDate = new Date(scmConfiguration.getUpdatedDate());
				DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
				String lastUpdatedDateString = formatter.format(lastUpdatedDate);
				pageUrl = new URIBuilder(uri).addParameter("at", branchId)
					.addParameter("state", status)
					.addParameter("q", "updated_on>=" + lastUpdatedDateString)
					.build();

				boolean lastPage = false;
				boolean stop = false;
				URI queryUrlPage = pageUrl;

				while (!lastPage && !stop) {
					LOG.info("Executing [" + queryUrlPage);
					pulls = new ArrayList<>();
					ResponseEntity<String> response = scmHttpRestClient.makeRestCall(queryUrlPage, repoUser,
							repoPassword);
					JSONObject jsonArray = new JSONObject();
					try {
						jsonArray = JSONParserUtils.parseAsObject(response);
					}
					catch (Exception e) {
						LOG.error("Exception=> " + e.getMessage());
					}
					JSONArray values = (JSONArray) jsonArray.get("values");
					for (Object item : values) {
						JSONObject jsonObject = (JSONObject) item;

						DesGitRequest pull = new DesGitRequest();
						try {
							if (scmConfiguration.getProduct().equalsIgnoreCase("cloud")) {
								pull = getPullRequestCloud(repo, jsonObject);

								// Find changes
								JSONObject links = (JSONObject) jsonObject.get("links");
								JSONObject diffStat = (JSONObject) links.get("diffstat");
								String url = (String) diffStat.get("href");
								url = url.replace("!api", "api");

								pullRequestCollectorClient.populatePullRequestChanges(scmConfiguration, pull, repoUser,
										repoPassword, url);

							}
							else if (scmConfiguration.getProduct().equalsIgnoreCase("server")) {
								pull = getPullRequestServer(repo, jsonObject);
							}
							// set configuration and isc project id reference
							pull.setScmConfigurationId(scmConfiguration.getId().toString());
							pull.setIscProjectId(scmConfiguration.getIscProjectId().toString());

							pullRequestCollectorClient.populatePullRequestMetadata(scmConfiguration, repo, pull,
									repoUser, repoPassword);
							pullRequestCollectorClient.populatePullRequestActivity(scmConfiguration, repo, pull,
									repoUser, repoPassword);
							pullRequestCollectorClient.populatePullRequestCommits(scmConfiguration, repo, pull,
									repoUser, repoPassword);
							pullRequestCollectorClient.populatePullRequestComments(scmConfiguration, repo, pull,
									repoUser, repoPassword);

							pulls.add(pull);
						}
						catch (Exception e) {
							LOG.error("Unable to parse pull request");
							continue;
						}
					}
					try {
						processList(pulls, "pull");
						pullRequests.addAll(pulls);
						// perform batch uploads in a set of 100
						if (pullRequests.size() >= 100) {
							uploadToS3(pullRequests);
							pullCount += pullRequests.size();
							pullRequests.clear();
						}

					}
					catch (Exception ex) {
						LOG.error("failed to process Pull Requests", ex);
						throw new RuntimeException("Unable to process pull requests", ex);
					}

					PageMetadata pageMetadata = new PageMetadata(pageUrl, jsonArray, values,
							scmConfiguration.getProduct());
					lastPage = pageMetadata.isLastPage();
					queryUrlPage = pageMetadata.getNextPageUrl();
				}
			}
			catch (URISyntaxException e) {
				LOG.error("Unable to construct Bitbucket API URL: " + e.getMessage());
			}
			catch (Exception e) {
				LOG.error("Exception block: " + e.getMessage());
			}
		}

		// upload the rest of the PRs
		uploadToS3(pullRequests);
		pullCount += pullRequests.size();
		pullRequests.clear();

		return pullCount;
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

	public int processList(List<DesGitRequest> entries, String type) {
		int count = 0;
		if (CollectionUtils.isEmpty(entries))
			return 0;

		for (DesGitRequest entry : entries) {

			// fix merge commit type for squash merged and rebased merged PRs
			// PRs that were squash merged or rebase merged have only one parent
			if ("pull".equalsIgnoreCase(type) && "merged".equalsIgnoreCase(entry.getState())) {
				List<Commit> commits = commitRepository.findByScmRevisionNumber(entry.getScmRevisionNumber());
				for (Commit commit : commits) {
					if (commit.getType() != null) {
						if (commit.getType() != CommitType.Merge) {
							commit.setType(CommitType.Merge);
							commitRepository.save(commit);
						}
					}
					else {
						commit.setType(CommitType.Merge);
						commitRepository.save(commit);
					}
				}
			}
		}
		return count;
	}

	private DesGitRequest getPullRequestServer(ScmRepo repo, JSONObject jsonObject) {
		String prNumber = jsonObject.get("id").toString();
		String message = (String) jsonObject.get("title");
		JSONObject fromRef = (JSONObject) jsonObject.get("fromRef");
		String sha = (String) fromRef.get("latestCommit");
		Long createdAt = (Long) jsonObject.get("createdDate");
		Long updatedAt = (Long) jsonObject.get("updatedDate");
		DesGitRequest pull = new DesGitRequest();
		pull.setScmCommitLog(message);
		pull.setNumber(prNumber);
		pull.setScmUrl(repo.getRepoUrl());
		pull.setScmRevisionNumber(sha);
		long currentTimeMillis = new DateTime(createdAt).getMillis();
		pull.setCreatedAt(currentTimeMillis);
		pull.setTimestamp(currentTimeMillis);
		pull.setUpdatedAt(new DateTime(updatedAt).getMillis());
		populateScmAuthorServer(pull, jsonObject);
		pull.setRequestType(PULL);
		pull.setScmBranch(repo.getDefaultBranchToScan());

		String state = (String) jsonObject.get("state");
		if (StringUtils.equals("OPEN", state)) {
			pull.setState(OPEN);
		}
		else if (StringUtils.equals("DECLINED", state)) {
			pull.setState(DECLINED);
		}
		else if (StringUtils.equals("MERGED", state)) {
			pull.setState(MERGED);
		}

		return pull;
	}

	private DesGitRequest getPullRequestCloud(ScmRepo repo, JSONObject jsonObject) throws ParseException {
		String prNumber = jsonObject.get("id").toString();
		String message = (String) jsonObject.get("title");
		JSONObject source = (JSONObject) jsonObject.get("source");
		JSONObject commit = (JSONObject) source.get("commit");
		String sha = (String) commit.get("hash");

		Long createdAt = new DateTime(jsonObject.get("created_on")).getMillis();
		Long updatedAt = new DateTime(jsonObject.get("updated_on")).getMillis();

		DesGitRequest pull = new DesGitRequest();
		pull.setScmCommitLog(message);
		pull.setNumber(prNumber);
		pull.setScmUrl(repo.getRepoUrl());
		pull.setCreatedAt(createdAt);
		pull.setTimestamp(createdAt);
		pull.setUpdatedAt(updatedAt);
		populateScmAuthorCloud(pull, jsonObject);
		pull.setRequestType(PULL);
		pull.setScmBranch(repo.getDefaultBranchToScan());

		if (null != jsonObject.get("merge_commit")) {
			JSONObject mergeCommitObject = (JSONObject) jsonObject.get("merge_commit");
			pull.setScmRevisionNumber((String) mergeCommitObject.get("hash"));
		}

		String state = (String) jsonObject.get("state");
		if (StringUtils.equals("OPEN", state)) {
			pull.setState(OPEN);
		}
		else if (StringUtils.equals("DECLINED", state)) {
			pull.setState(DECLINED);
		}
		else if (StringUtils.equals("MERGED", state)) {
			pull.setState(MERGED);
		}

		return pull;
	}

	private void populateScmAuthorServer(DesGitRequest pull, JSONObject jsonObject) {
		JSONObject author = (JSONObject) jsonObject.get("author");
		JSONObject user = (JSONObject) author.get("user");
		pull.setScmAuthor(JSONParserUtils.str(user, "displayName"));
		pull.setUserId(JSONParserUtils.str(user, "slug"));
	}

	private void populateScmAuthorCloud(DesGitRequest pull, JSONObject jsonObject) {
		JSONObject author = (JSONObject) jsonObject.get("author");
		pull.setScmAuthor(JSONParserUtils.str(author, "display_name"));
		pull.setUserId(JSONParserUtils.str(author, "uuid"));
	}

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
