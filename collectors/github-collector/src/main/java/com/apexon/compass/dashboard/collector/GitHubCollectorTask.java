package com.apexon.compass.dashboard.collector;

import com.apexon.compass.dashboard.Util.AsyncUtil;
import com.apexon.compass.dashboard.model.CompassScmConfigurations;
import com.apexon.compass.dashboard.model.GitHubRepo;
import com.apexon.compass.dashboard.model.ScmConfiguration;
import com.apexon.compass.dashboard.model.ScmRepo;
import com.apexon.compass.dashboard.repository.GitHubRepoRepository;
import com.apexon.compass.dashboard.repository.ScmConfigurationRepository;
import com.apexon.compass.dashboard.misc.HygieiaException;
import com.apexon.compass.dashboard.model.*;
import com.apexon.compass.dashboard.repository.*;
import jakarta.annotation.PostConstruct;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientException;

import java.net.MalformedURLException;
import java.time.Duration;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * CollectorTask that fetches Commit information from GitHub
 */
@Component
public class GitHubCollectorTask extends CollectorTask<Collector> {

	private static final Log LOG = LogFactory.getLog(GitHubCollectorTask.class);

	private final BaseCollectorRepository<Collector> collectorRepository;

	private final GitHubRepoRepository gitHubRepoRepository;

	private final CommitRepository commitRepository;

	private final GitRequestRepository gitRequestRepository;

	private final GitHubClient gitHubClient;

	private final ComponentRepository dbComponentRepository;

	private static final long FOURTEEN_DAYS_MILLISECONDS = 14 * 24 * 60 * 60 * 1000;

	private static final String API_RATE_LIMIT_MESSAGE = "API rate limit exceeded";

	private List<Pattern> commitExclusionPatterns = new ArrayList<>();

	private TaskScheduler taskScheduler;

	private final ScmConfigurationRepository scmConfigurationRepository;

	private final List<ScmConfiguration> scmConfigurations;

	private final AsyncUtil asyncUtil;

	@Autowired
	public GitHubCollectorTask(TaskScheduler taskScheduler, BaseCollectorRepository<Collector> collectorRepository,
			GitHubRepoRepository gitHubRepoRepository, CommitRepository commitRepository,
			GitRequestRepository gitRequestRepository, GitHubClient gitHubClient,
			ComponentRepository dbComponentRepository, ScmConfigurationRepository scmConfigurationRepository,
			CompassScmConfigurations compassScmConfigurations, AsyncUtil asyncUtil) {
		super(taskScheduler, "GitHub");
		this.collectorRepository = collectorRepository;
		this.gitHubRepoRepository = gitHubRepoRepository;
		this.commitRepository = commitRepository;
		this.gitHubClient = gitHubClient;
		this.dbComponentRepository = dbComponentRepository;
		this.gitRequestRepository = gitRequestRepository;
		this.taskScheduler = taskScheduler;
		this.scmConfigurationRepository = scmConfigurationRepository;
		this.scmConfigurations = compassScmConfigurations.getScmConfigurations();
		this.asyncUtil = asyncUtil;
	}

	@Override
	public Collector getCollector() {
		Collector protoType = new Collector();
		protoType.setName("GitHub");
		protoType.setCollectorType(CollectorType.SCM);
		protoType.setOnline(true);
		protoType.setEnabled(true);

		Map<String, Object> allOptions = new HashMap<>();
		allOptions.put(GitHubRepo.REPO_URL, "");
		allOptions.put(GitHubRepo.BRANCH, "");
		allOptions.put(GitHubRepo.USER_ID, "");
		allOptions.put(GitHubRepo.PASSWORD, "");
		allOptions.put(GitHubRepo.PERSONAL_ACCESS_TOKEN, "");
		protoType.setAllFields(allOptions);

		Map<String, Object> uniqueOptions = new HashMap<>();
		uniqueOptions.put(GitHubRepo.REPO_URL, "");
		uniqueOptions.put(GitHubRepo.BRANCH, "");
		protoType.setUniqueFields(uniqueOptions);
		return protoType;
	}

	@Override
	public BaseCollectorRepository<Collector> getCollectorRepository() {
		return collectorRepository;
	}

	@PostConstruct
	@Override
	public void onStartup() {
		taskScheduler.schedule(this, new Date(System.currentTimeMillis() + Duration.ofMinutes(1L).toMillis()));
	}

	@Override
	public String getCron() {
		return null;
	}

	/**
	 * Clean up unused deployment collector items
	 * @param collector the {@link Collector}
	 */
	@SuppressWarnings("PMD.AvoidDeeplyNestedIfStmts") // agreed, fixme
	private void clean(Collector collector) {
		Set<ObjectId> uniqueIDs = new HashSet<>();
		/**
		 * Logic: For each component, retrieve the collector item list of the type SCM.
		 * Store their IDs in a unique set ONLY if their collector IDs match with GitHub
		 * collectors ID.
		 */
		for (com.apexon.compass.dashboard.model.Component comp : dbComponentRepository.findAll()) {
			if (comp.getCollectorItems() != null && !comp.getCollectorItems().isEmpty()) {
				List<CollectorItem> itemList = comp.getCollectorItems().get(CollectorType.SCM);
				if (itemList != null) {
					for (CollectorItem ci : itemList) {
						if (ci != null && ci.getCollectorId().equals(collector.getId())) {
							uniqueIDs.add(ci.getId());
						}
					}
				}
			}
		}

		/**
		 * Logic: Get all the collector items from the collector_item collection for this
		 * collector. If their id is in the unique set (above), keep them enabled; else,
		 * disable them.
		 */
		List<GitHubRepo> repoList = new ArrayList<>();
		Set<ObjectId> gitID = new HashSet<>();
		gitID.add(collector.getId());
		for (GitHubRepo repo : gitHubRepoRepository.findByCollectorIdIn(gitID)) {
			if (repo.isPushed()) {
				continue;
			}

			repo.setEnabled(uniqueIDs.contains(repo.getId()));
			repoList.add(repo);
		}
		gitHubRepoRepository.saveAll(repoList);
	}

	@Override
	public void collect(Collector collector) {

		logBanner("Starting...");
		long start = System.currentTimeMillis();
		int repoCount = 0;
		int commitCount = 0;
		int pullCount = 0;
		int issueCount = 0;

		try {
			for (ScmConfiguration scmConfiguration : this.scmConfigurations) {
				long time = System.currentTimeMillis();
				// Iterate all the repositories
				for (ScmRepo repo : scmConfiguration.getScm()) {
					boolean firstRun = ((scmConfiguration.getUpdatedDate() == 0));
					try {
						LOG.info(repo.getRepoName() + "::" + repo.getDefaultBranchToScan() + ":: get commits");

						// Get all the Pull Requests
						LOG.info(repo.getRepoName() + "::" + repo.getDefaultBranchToScan() + ":: get pulls");
						pullCount += gitHubClient.getPulls(firstRun, scmConfiguration, repo, "all");

						repoCount++;
					}
					catch (HttpStatusCodeException hc) {
						LOG.error("Error fetching data for:" + repo.getRepoUrl(), hc);
						if (!(isRateLimitError(hc) || hc.getStatusCode() == HttpStatus.SERVICE_UNAVAILABLE)) {
							CollectionError error = new CollectionError(hc.getStatusCode().toString(), hc.getMessage());
						}
					}
					catch (ResourceAccessException ex) {
						// handle case where repo is valid but github returns connection
						// refused due to outages??
						if (ex.getMessage() != null && ex.getMessage().contains("Connection refused")) {
							LOG.error("Error fetching data for:" + repo.getRepoUrl(), ex);
						}
						else {
							LOG.error("Error fetching data for:" + repo.getRepoUrl(), ex);
						}
					}
					catch (RestClientException | MalformedURLException ex) {
						LOG.error("Error fetching data for:" + repo.getRepoUrl(), ex);
					}
					catch (HygieiaException he) {
						LOG.error("Error fetching data for:" + repo.getRepoUrl(), he);
					}
				}
				scmConfiguration.setUpdatedDate(time);
				scmConfigurationRepository.save(scmConfiguration);
			}
		}
		catch (Exception e) {
			LOG.error("Failed to collect github information", e);
			taskScheduler.schedule(this, new Date(System.currentTimeMillis() + Duration.ofMinutes(1L).toMillis()));
		}
		log("Finished", start);
		asyncUtil.close();

		log("Repo Count", start, repoCount);
		log("New Commits", start, commitCount);
		log("New Pulls", start, pullCount);
		log("New Issues", start, issueCount);

		log("Finished", start);
	}

	private int processList(ScmRepo repo, List<GitRequest> entries, String type) {
		int count = 0;
		if (CollectionUtils.isEmpty(entries))
			return 0;

		for (GitRequest entry : entries) {
			LOG.debug(entry.getTimestamp() + ":::" + entry.getScmCommitLog());

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

	private boolean isRateLimitError(HttpStatusCodeException hc) {
		String response = hc.getResponseBodyAsString();
		return StringUtils.isEmpty(response) ? false : response.contains(API_RATE_LIMIT_MESSAGE);
	}

	private List<GitHubRepo> enabledRepos(Collector collector) {
		List<GitHubRepo> repos = gitHubRepoRepository.findEnabledGitHubRepos(collector.getId());

		List<GitHubRepo> pulledRepos = Optional.ofNullable(repos)
			.orElseGet(Collections::emptyList)
			.stream()
			.filter(pulledRepo -> !pulledRepo.isPushed())
			.collect(Collectors.toList());

		if (CollectionUtils.isEmpty(pulledRepos)) {
			return new ArrayList<>();
		}

		return pulledRepos;
	}

	private GitRequest getExistingRequest(GitHubRepo repo, GitRequest request, String type) {
		return gitRequestRepository.findByCollectorItemIdAndNumberAndRequestType(repo.getId(), request.getNumber(),
				type);
	}

}
