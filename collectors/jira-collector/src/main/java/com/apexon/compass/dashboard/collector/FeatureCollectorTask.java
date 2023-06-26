package com.apexon.compass.dashboard.collector;

import com.apexon.compass.dashboard.model.*;
import com.apexon.compass.dashboard.repository.*;
import com.apexon.compass.dashboard.util.FeatureCollectorConstants;
import com.apexon.compass.fileupload.dto.FileUploadDto;
import com.apexon.compass.dashboard.utils.AsyncUtil;
import com.apexon.compass.dashboard.utils.Utilities;
import com.apexon.compass.fileupload.service.UploadService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import jakarta.annotation.PostConstruct;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.stereotype.Component;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.*;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

/**
 * Collects {@link FeatureCollector} data from feature content source system.
 *
 * @author KFK884
 */
@Component
public class FeatureCollectorTask extends CollectorTask<FeatureCollector> {

	private static final Logger LOGGER = LoggerFactory.getLogger(FeatureCollectorTask.class);

	private final FeatureRepository featureRepository;

	private final FeatureBoardRepository featureBoardRepository;

	private final TeamRepository teamRepository;

	private final ScopeRepository projectRepository;

	private final FeatureCollectorRepository featureCollectorRepository;

	private final JiraConfigurationRepository jiraConfigurationRepository;

	private final JiraClient jiraClient;

	private final List<JiraConfiguration> jiraConfigurations;

	private JiraConfiguration jiraConfiguration;

	@Autowired
	private Environment environment;

	@Autowired
	private UploadService uploadService;

	private static final String BUCKET_NAME = "%s-des-jira-data";

	private static final String DEV = "dev";

	private TaskScheduler taskScheduler;

	private static final String DEFAULT_REGION = "us-east-2";

	private final AsyncUtil asyncUtil;

	/**
	 * Default constructor for the collector task. This will construct this collector task
	 * with all repository, scheduling, and settings configurations custom to this
	 * collector.
	 * @param taskScheduler A task scheduler artifact
	 * @param teamRepository The repository being use for feature collection
	 * @param desJiraConfiguration The configurations being used for feature collection
	 * from the source system
	 */
	@Autowired
	public FeatureCollectorTask(TaskScheduler taskScheduler, FeatureRepository featureRepository,
			TeamRepository teamRepository, ScopeRepository projectRepository,
			FeatureCollectorRepository featureCollectorRepository,
			JiraConfigurationRepository jiraConfigurationRepository, JiraClient jiraClient,
			FeatureBoardRepository featureBoardRepository, DesJiraConfiguration desJiraConfiguration,
			AsyncUtil asyncUtil) {
		super(taskScheduler, FeatureCollectorConstants.JIRA);
		this.featureCollectorRepository = featureCollectorRepository;
		this.teamRepository = teamRepository;
		this.projectRepository = projectRepository;
		this.featureRepository = featureRepository;
		this.jiraConfigurationRepository = jiraConfigurationRepository;
		this.jiraClient = jiraClient;
		this.featureBoardRepository = featureBoardRepository;
		this.taskScheduler = taskScheduler;
		this.jiraConfigurations = desJiraConfiguration.getJiraConfigurations();
		if (this.jiraConfigurations.size() > 0) {
			this.jiraConfiguration = this.jiraConfigurations.get(0);
		}
		this.asyncUtil = asyncUtil;
	}

	/** Accessor method for the collector prototype object */
	@Override
	public FeatureCollector getCollector() {

		JiraMode mode = this.jiraConfiguration.isJiraBoardAsTeam() ? JiraMode.Board : JiraMode.Team;
		FeatureCollector collector = FeatureCollector.prototype(mode);
		FeatureCollector existing = featureCollectorRepository.findByName(collector.getName());
		if (existing != null) {
			collector.setLastRefreshTime(existing.getLastRefreshTime());
		}
		return collector;
	}

	/** Accessor method for the collector repository */
	@Override
	public BaseCollectorRepository<FeatureCollector> getCollectorRepository() {
		return featureCollectorRepository;
	}

	@PostConstruct
	@Override
	public void onStartup() {
		taskScheduler.schedule(this, new Date(System.currentTimeMillis() + Duration.ofMinutes(1L).toMillis()));
	}

	/** Accessor method for the current chronology setting, for the scheduler */
	@Override
	public String getCron() {
		return null;
	}

	/**
	 * The collection action. This is the task which will run on a schedule to gather data
	 * from the feature content source system and update the repository with retrieved
	 * data.
	 */
	@Override
	public void collect(FeatureCollector collector) {

		this.jiraConfigurations.stream().forEach(jiraConfiguration -> {
			this.jiraConfiguration = jiraConfiguration;
			logBanner(this.jiraConfiguration.getUrl());
			ObjectMapper objectMapper = new ObjectMapper();
			try {
				System.out.println(objectMapper.writeValueAsString(collector));
			}
			catch (JsonProcessingException e1) {
				e1.printStackTrace();
			}

			String proxyUrl = this.jiraConfiguration.getJiraProxyUrl();
			String proxyPort = this.jiraConfiguration.getJiraProxyPort();

			if (!StringUtils.isEmpty(proxyUrl) && !StringUtils.isEmpty(proxyPort)) {
				System.setProperty("http.proxyHost", proxyUrl);
				System.setProperty("https.proxyHost", proxyUrl);
				System.setProperty("http.proxyPort", proxyPort);
				System.setProperty("https.proxyPort", proxyPort);
			}

			try {
				long startTime = System.currentTimeMillis();
				long diff = TimeUnit.MILLISECONDS.toHours(startTime - this.jiraConfiguration.getUpdatedDate());
				LOGGER
					.info("Hours since last run = " + diff + ". Collector is about to refresh Team/Board information");
				List<JiraBoard> teams = updateTeamInformation(collector);
				updateStoryInformation(teams);
				this.jiraConfiguration.setUpdatedDate(startTime);
				this.jiraConfigurationRepository.save(this.jiraConfiguration);
				log("Finished", startTime);
			}
			catch (Exception e) {
				LOGGER.error("Failed to collect jira information", e);
				taskScheduler.schedule(this, new Date(System.currentTimeMillis() + Duration.ofMinutes(1L).toMillis()));
			}
		});
		asyncUtil.close();
	}

	/**
	 * Update team information
	 * @param collector
	 * @return list of teams collected
	 */
	protected List<JiraBoard> updateTeamInformation(FeatureCollector collector) {
		long projectDataStart = System.currentTimeMillis();
		List<JiraBoard> teams = this.jiraConfiguration.isJiraBoardAsTeam()
				? jiraClient.getBoards(this.jiraConfiguration) : jiraClient.getTeams(this.jiraConfiguration);

		// filter and save teams that we got from api
		teams = teams.stream()
			.filter(team -> this.jiraConfiguration.getJiraBoardId().contains(team.getBoardId()))
			.collect(Collectors.toList());

		log(collector.getMode() + " Data Collected. Added ", projectDataStart, teams.size());
		return teams;
	}

	/**
	 * Update project information
	 * @param collector
	 * @return List of projects
	 */
	protected Set<Scope> updateProjectInformation(Collector collector) {
		Set<Scope> projects = jiraClient.getProjects(this.jiraConfiguration);
		return projects;
	}

	/**
	 * Update story/feature information for all the projects one at a time
	 * @param boards
	 */
	protected void updateStoryInformation(List<JiraBoard> boards) {
		long storyDataStart = System.currentTimeMillis();
		AtomicLong count = new AtomicLong();
		Map<String, String> issueTypesMap = jiraConfiguration.getIssueTypesId();

		// List<Team> boards = getBoardList(collector.getId());
		boards.forEach(board -> {
			LOGGER.info("Collecting " + count.incrementAndGet() + " of " + boards.size() + " boards.");
			long lastCollection = System.currentTimeMillis();
			FeatureEpicResult featureEpicResult = jiraClient.getIssues(board, issueTypesMap, this.jiraConfiguration);
			List<DesFeature> features = featureEpicResult.getFeatureList();
			saveFeatures(features);
			// updateFeaturesWithLatestEpics(featureEpicResult.getEpicList(), collector);
			log("Story Data Collected since " + LocalDateTime
				.ofInstant(Instant.ofEpochMilli(jiraConfiguration.getUpdatedDate()), ZoneId.systemDefault()),
					storyDataStart, features.size());

			jiraConfiguration.setLastExecuted(lastCollection); // set it after everything
																// is successfully done
		});
	}

	/**
	 * Returns a full team list or partial based on whether or not
	 * isCollectorItemOnlyUpdate is true
	 * @param collectorId
	 * @return
	 */
	private List<Team> getBoardList(ObjectId collectorId) {
		List<Team> boards;
		if (this.jiraConfiguration.isCollectorItemOnlyUpdate()) {
			Set<Team> uniqueTeams = new HashSet<>();
			for (FeatureBoard featureBoard : enabledFeatureBoards(collectorId)) {
				Team team = teamRepository.findByTeamId(featureBoard.getTeamId());
				if (team != null) {
					uniqueTeams.add(team);
				}
			}

			boards = new ArrayList<>(uniqueTeams);
		}
		else {
			boards = teamRepository.findByCollectorId(collectorId);
		}

		return boards;
	}

	/**
	 * Returns a full project list or partial based on whether or not
	 * isCollectorItemOnlyUpdate is true
	 * @param collectorId
	 * @return
	 */
	private Set<Scope> getScopeList(ObjectId collectorId) {
		Set<Scope> projects = new HashSet<>();
		if (this.jiraConfiguration.isCollectorItemOnlyUpdate()) {
			for (FeatureBoard featureBoard : enabledFeatureBoards(collectorId)) {
				Scope scope = projectRepository.findByCollectorIdAndPId(collectorId, featureBoard.getProjectId());
				if (scope != null) {
					projects.add(scope);
				}
			}
		}
		else {
			projects = new HashSet<>(projectRepository.findByCollectorId(collectorId));
		}
		return projects;
	}

	private List<FeatureBoard> enabledFeatureBoards(ObjectId collectorId) {
		return featureBoardRepository.findEnabledFeatureBoards(collectorId);
	}

	/**
	 * Save features to repository
	 * @param features
	 */
	private void saveFeatures(List<DesFeature> features) {
		String region = Objects.isNull(environment.getProperty("AWS_REGION")) ? DEFAULT_REGION
				: environment.getProperty("AWS_REGION");
		if (CollectionUtils.isNotEmpty(features)) {
			try {
				Gson gson = new GsonBuilder().serializeNulls().create();
				String jsonData = gson.toJson(features);
				DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
				String dateString = formatter.format(Calendar.getInstance().getTime());
				OffsetDateTime utc = OffsetDateTime.now(ZoneOffset.UTC);
				long epochMillis = utc.toEpochSecond() * 1000;
				String objectPath = dateString + "/issue_" + epochMillis + ".json";
				FileUploadDto featureUpload = new FileUploadDto(region, String.valueOf(getBucketName()), objectPath,
						jsonData);
				uploadService.uploadToStorage(featureUpload);
				LOGGER.info(objectPath + " file uploaded to S3.");
			}
			catch (ClassCastException e) {
				LOGGER.info(e.getMessage());
			}
			catch (Exception e) {
				LOGGER.info("Unable to upload the file to S3.");
				LOGGER.info(e.getMessage());
			}
		}
	}

	/**
	 * Update all features with the latest Epic Information, if any.
	 * @param epicList
	 * @param collector
	 */
	private void updateFeaturesWithLatestEpics(List<Epic> epicList, FeatureCollector collector) {
		epicList.stream().filter(Epic::isRecentUpdate).forEach(e -> {
			List<Feature> existing = featureRepository.findAllByCollectorIdAndSEpicID(collector.getId(), e.getId());
			existing.stream().filter(ex -> isEpicChanged(ex, e)).forEach(ex -> {
				ex.setsEpicAssetState(e.getStatus());
				ex.setsEpicName(e.getName());
				featureRepository.save(ex);
			});
		});
	}

	/**
	 * Get a list of all issue ids for a given board or project and delete ones that are
	 * not in JIRA anymore
	 * @param collector
	 * @param teams
	 * @param scopes
	 */
	private void refreshValidIssues(FeatureCollector collector, List<Team> teams, Set<Scope> scopes) {
		long refreshValidIssuesStart = System.currentTimeMillis();
		List<String> lookUpIds = Objects.equals(collector.getMode(), JiraMode.Board)
				? teams.stream().map(Team::getTeamId).collect(Collectors.toList())
				: scopes.stream().map(Scope::getpId).collect(Collectors.toList());
		lookUpIds.forEach(l -> {
			LOGGER.info("Refreshing issues for " + collector.getMode() + " ID:" + l);
			List<String> issueIds = jiraClient.getAllIssueIds(l, collector.getMode(), this.jiraConfiguration);
			List<Feature> existingFeatures = Objects.equals(collector.getMode(), JiraMode.Board)
					? featureRepository.findAllByCollectorIdAndSTeamID(collector.getId(), l)
					: featureRepository.findAllByCollectorIdAndSProjectID(collector.getId(), l);
			List<Feature> deletedFeatures = existingFeatures.stream()
				.filter(e -> !issueIds.contains(e.getsId()))
				.collect(Collectors.toList());
			deletedFeatures.forEach(d -> {
				LOGGER.info("Deleting Feature " + d.getsId() + ':' + d.getsName());
				featureRepository.delete(d);
			});
		});
		log(collector.getMode() + " Issues Refreshed ", refreshValidIssuesStart);
	}

	// Checks if epic information on a feature needs update
	private static boolean isEpicChanged(Feature feature, Epic epic) {
		if (!feature.getsEpicAssetState().equalsIgnoreCase(epic.getStatus())) {
			return true;
		}
		if (!feature.getsEpicName().equalsIgnoreCase(epic.getName())
				|| !feature.getsEpicNumber().equalsIgnoreCase(epic.getNumber())) {
			return true;
		}

		if (!StringUtils.isEmpty(feature.getChangeDate()) && !StringUtils.isEmpty(epic.getChangeDate())
				&& !Objects.equals(Utilities.parseDateWithoutFraction(feature.getChangeDate()),
						Utilities.parseDateWithoutFraction(epic.getChangeDate()))) {
			return true;
		}
		if (!StringUtils.isEmpty(feature.getsEpicBeginDate()) && !StringUtils.isEmpty(epic.getBeginDate())
				&& !Objects.equals(Utilities.parseDateWithoutFraction(feature.getsEpicBeginDate()),
						Utilities.parseDateWithoutFraction(epic.getBeginDate()))) {
			return true;
		}
		return !StringUtils.isEmpty(feature.getsEpicEndDate()) && !StringUtils.isEmpty(epic.getEndDate())
				&& !Objects.equals(Utilities.parseDateWithoutFraction(feature.getsEpicEndDate()),
						Utilities.parseDateWithoutFraction(epic.getEndDate()));
	}

	private StringBuilder getBucketName() {
		StringBuilder bucketName = new StringBuilder();
		if (environment != null && Arrays.stream(environment.getActiveProfiles()).count() > 0) {
			return bucketName.append(String.format(BUCKET_NAME,
					Arrays.asList(environment.getActiveProfiles()).stream().findAny().get()));
		}
		else {
			return bucketName.append(String.format(BUCKET_NAME, DEV));
		}
	}

}
