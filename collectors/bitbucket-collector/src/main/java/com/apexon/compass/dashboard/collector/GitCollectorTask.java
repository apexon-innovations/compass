package com.apexon.compass.dashboard.collector;

import com.apexon.compass.dashboard.model.*;
import com.apexon.compass.dashboard.repository.*;
import com.apexon.compass.dashboard.util.AsyncUtil;
import jakarta.annotation.PostConstruct;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.*;

/** CollectorTask that fetches Commit information from Git */
@Component
public class GitCollectorTask extends CollectorTask<Collector> {

	private static final Log LOG = LogFactory.getLog(GitCollectorTask.class);

	private final BaseCollectorRepository<Collector> collectorRepository;

	private final GitRepoRepository gitRepoRepository;

	private final CommitRepository commitRepository;

	private final ComponentRepository dbComponentRepository;

	private final ScmConfigurationRepository scmConfigurationRepository;

	private TaskScheduler taskScheduler;

	private final List<ScmConfiguration> scmConfigurations;

	@Autowired
	private PullRequestCollector pullRequestCollector;

	private final AsyncUtil asyncUtil;

	@Autowired
	public GitCollectorTask(TaskScheduler taskScheduler, BaseCollectorRepository<Collector> collectorRepository,
			GitRepoRepository gitRepoRepository, CommitRepository commitRepository,
			ComponentRepository dbComponentRepository, ScmConfigurationRepository scmConfigurationRepository,
			DesSCMConfigurations desSCMConfigurations, AsyncUtil asyncUtil) {
		super(taskScheduler, "Bitbucket");
		this.collectorRepository = collectorRepository;
		this.gitRepoRepository = gitRepoRepository;
		this.commitRepository = commitRepository;
		this.dbComponentRepository = dbComponentRepository;
		this.scmConfigurationRepository = scmConfigurationRepository;
		this.scmConfigurations = desSCMConfigurations.getScmConfigurations();
		this.taskScheduler = taskScheduler;
		this.asyncUtil = asyncUtil;
	}

	@Override
	public Collector getCollector() {
		Collector protoType = new Collector();
		protoType.setName("Bitbucket");
		protoType.setCollectorType(CollectorType.SCM);
		protoType.setOnline(true);
		protoType.setEnabled(true);

		Map<String, Object> allOptions = new HashMap<>();
		allOptions.put(GitRepo.REPO_URL, "");
		allOptions.put(GitRepo.BRANCH, "");
		allOptions.put(GitRepo.USER_ID, "");
		allOptions.put(GitRepo.PASSWORD, "");
		allOptions.put(GitRepo.LAST_UPDATE_TIME, System.currentTimeMillis());
		allOptions.put(GitRepo.LAST_UPDATE_COMMIT, "");
		protoType.setAllFields(allOptions);

		Map<String, Object> uniqueOptions = new HashMap<>();
		uniqueOptions.put(GitRepo.REPO_URL, "");
		uniqueOptions.put(GitRepo.BRANCH, "");
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
	private void clean(Collector collector) {
		Set<ObjectId> uniqueIDs = new HashSet<>();
		/**
		 * Logic: For each component, retrieve the collector item list of the type SCM.
		 * Store their IDs in a unique set ONLY if their collector IDs match with
		 * Bitbucket collectors ID.
		 */
		for (com.apexon.compass.dashboard.model.Component comp : dbComponentRepository.findAll()) {
			if (comp.getCollectorItems() == null || comp.getCollectorItems().isEmpty())
				continue;
			List<CollectorItem> itemList = comp.getCollectorItems().get(CollectorType.SCM);
			if (itemList == null)
				continue;
			for (CollectorItem ci : itemList) {
				if (ci != null && ci.getCollectorId().equals(collector.getId())) {
					uniqueIDs.add(ci.getId());
				}
			}
		}

		/**
		 * Logic: Get all the collector items from the collector_item collection for this
		 * collector. If their id is in the unique set (above), keep them enabled; else,
		 * disable them.
		 */
		List<GitRepo> repoList = new ArrayList<GitRepo>();
		Set<ObjectId> gitID = new HashSet<ObjectId>();
		gitID.add(collector.getId());
		for (GitRepo repo : gitRepoRepository.findByCollectorIdIn(gitID)) {
			if (repo != null) {
				repo.setEnabled(uniqueIDs.contains(repo.getId()));
				repoList.add(repo);
			}
		}
		gitRepoRepository.saveAll(repoList);
	}

	@Override
	public void collect(Collector collector) {

		logBanner("Starting...");
		long start = System.currentTimeMillis();
		int configurationCount = 0;
		int pullCount = 0;

		try {
			for (ScmConfiguration scmConfiguration : this.scmConfigurations) {
				long time = System.currentTimeMillis();
				pullCount += pullRequestCollector.getPullRequests(scmConfiguration, "all");

				// Update the configuration time after successful run
				scmConfiguration.setUpdatedDate(time);
				scmConfigurationRepository.save(scmConfiguration);
				configurationCount++;
			}
			log("Configuration Count", start, configurationCount);
			log("Pull Count", start, pullCount);
		}
		catch (Exception e) {
			LOG.error("Failed to collect bibucket information", e);
			taskScheduler.schedule(this, new Date(System.currentTimeMillis() + Duration.ofMinutes(1L).toMillis()));
		}
		log("Finished", start);
		asyncUtil.close();
	}

	private List<GitRepo> enabledRepos(Collector collector) {
		return gitRepoRepository.findEnabledGitRepos(collector.getId());
	}

	private boolean isNewCommit(Commit commit) {
		return commitRepository.findByScmRevisionNumber(commit.getScmUrl()) == null;
	}

	private String getUrlDomainName(String url) {
		String domainName = url;
		String domain = null;
		int index = domainName.indexOf("://");
		if (index != -1) {
			domain = domainName.substring(0, index + 3);
			domainName = domainName.substring(index + 3);
		}
		index = domainName.indexOf('/');

		if (index != -1) {
			domainName = domainName.substring(0, index);
		}
		domainName = domain + domainName;
		domainName = domainName.replaceFirst("^www.*?\\.", "");
		return domainName;
	}

}
