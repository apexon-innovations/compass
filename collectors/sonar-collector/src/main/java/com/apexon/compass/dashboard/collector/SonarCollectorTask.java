package com.apexon.compass.dashboard.collector;

import com.apexon.compass.dashboard.model.CollectorItemConfigHistory;
import com.apexon.compass.dashboard.model.CollectorType;
import com.apexon.compass.dashboard.model.ConfigHistOperationType;
import com.apexon.compass.dashboard.model.SonarProject;
import com.apexon.compass.dashboard.repository.BaseCollectorRepository;
import com.apexon.compass.dashboard.repository.CodeQualityRepository;
import com.apexon.compass.dashboard.repository.ComponentRepository;
import com.apexon.compass.fileupload.dto.FileUploadDto;
import com.apexon.compass.dashboard.model.*;
import com.apexon.compass.dashboard.repository.*;
import com.apexon.compass.dashboard.util.AsyncUtil;
import com.apexon.compass.dashboard.util.Encryption;
import com.apexon.compass.dashboard.util.EncryptionException;
import com.apexon.compass.dashboard.repository.SonarCollectorRepository;
import com.apexon.compass.dashboard.repository.SonarProfileRepostory;
import com.apexon.compass.fileupload.service.UploadService;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import jakarta.annotation.PostConstruct;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.bson.types.ObjectId;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.stereotype.Component;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.StreamSupport;

@Component
public class SonarCollectorTask extends CollectorTask<SonarCollector> {

	private static final Log LOG = LogFactory.getLog(SonarCollectorTask.class);

	private Environment environment;

	@Autowired
	private UploadService uploadService;

	private static final String BUCKET_NAME = "%s-des-sonarqube-data";

	private static final String DEV = "dev";

	private static final String DEFAULT_REGION = "us-east-2";

	private final SonarCollectorRepository sonarCollectorRepository;

	private final SonarProjectRepository sonarProjectRepository;

	private final CodeQualityRepository codeQualityRepository;

	private final SonarProfileRepostory sonarProfileRepostory;

	private final SonarClientSelector sonarClientSelector;

	private final ComponentRepository dbComponentRepository;

	private TaskScheduler taskScheduler;

	private List<SonarConfiguration> sonarConfigurations;

	private SonarConfiguration sonarConfiguration;

	private AsyncUtil asyncUtil;

	@Autowired
	public SonarCollectorTask(TaskScheduler taskScheduler, SonarCollectorRepository sonarCollectorRepository,
			SonarProjectRepository sonarProjectRepository, CodeQualityRepository codeQualityRepository,
			SonarProfileRepostory sonarProfileRepostory, SonarClientSelector sonarClientSelector,
			ComponentRepository dbComponentRepository, DesSonarConfigurations desSonarConfigurations,
			Environment environment, AsyncUtil asyncUtil) {
		super(taskScheduler, "Sonar");
		this.sonarCollectorRepository = sonarCollectorRepository;
		this.sonarProjectRepository = sonarProjectRepository;
		this.codeQualityRepository = codeQualityRepository;
		this.sonarProfileRepostory = sonarProfileRepostory;
		this.sonarClientSelector = sonarClientSelector;
		this.dbComponentRepository = dbComponentRepository;
		this.taskScheduler = taskScheduler;
		this.asyncUtil = asyncUtil;
		this.environment = environment;
		this.sonarConfigurations = desSonarConfigurations.getSonarConfigurations();
		if (this.sonarConfigurations.size() > 0) {
			this.sonarConfiguration = this.sonarConfigurations.get(0);
		}
	}

	@Override
	public SonarCollector getCollector() {

		List<String> iscProjectIds = new ArrayList<>();
		List<String> urls = new ArrayList<>();
		this.sonarConfigurations.stream().forEach(sonarConfiguration -> {
			iscProjectIds.add(sonarConfiguration.getIscProjectId().toString());
			urls.add(sonarConfiguration.getUrl());
		});

		return SonarCollector.prototype(urls, iscProjectIds);
	}

	@Override
	public BaseCollectorRepository<SonarCollector> getCollectorRepository() {
		return sonarCollectorRepository;
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

	@Override
	public void collect(SonarCollector collector) {

		try {
			if (!CollectionUtils.isEmpty(collector.getSonarServers())) {
				for (int i = 0; i < collector.getSonarServers().size(); i++) {

					this.sonarConfiguration = this.sonarConfigurations.get(i);

					long start = System.currentTimeMillis();

					Set<ObjectId> udId = new HashSet<>();
					udId.add(collector.getId());
					List<SonarProject> existingProjects = sonarProjectRepository.findByCollectorIdIn(udId);
					List<SonarProject> latestProjects = new ArrayList<>();
					List<SonarSubProject> configurationProjects = sonarConfiguration.getProjects();
					clean(collector, existingProjects);

					String instanceUrl = this.sonarConfiguration.getUrl();
					logBanner(instanceUrl);

					Double version = sonarClientSelector.getSonarVersion(instanceUrl);
					SonarClient sonarClient = sonarClientSelector.getSonarClient(version);

					String username = this.sonarConfiguration.getUserName();
					String password = null;
					try {
						password = Encryption.decryptString(this.sonarConfiguration.getCredentials(),
								this.sonarConfiguration.getEncryptionKey());
					}
					catch (EncryptionException e) {
						e.printStackTrace();
						continue;
					}

					if (null == password) {
						// if password is not available continue to next project
						continue;
					}

					String token = "";
					sonarClient.setServerCredentials(username, password, token);

					List<SonarProject> projects = sonarClient.getProjects(instanceUrl);

					// Filter projects from configuration
					projects = projects.stream()
						.filter(project -> configurationProjects.stream()
							.anyMatch(configurationProject -> configurationProject.getKey()
								.equals(project.getOptions().get("projectKey"))))
						.collect(Collectors.toList());

					latestProjects.addAll(projects);

					int projSize = CollectionUtils.size(projects);
					log("Fetched projects   " + projSize, start);

					addNewProjects(projects, existingProjects, collector);

					refreshData(enabledProjects(collector, instanceUrl), sonarClient);

					log("Completed", start);
					deleteUnwantedJobs(latestProjects, existingProjects, collector);
				}
			}
		}
		catch (Exception e) {
			LOG.error("Failed to collect sonar information", e);
			taskScheduler.schedule(this, new Date(System.currentTimeMillis() + Duration.ofMinutes(1L).toMillis()));
		}
		log("Finished");
		this.asyncUtil.close();
	}

	private String getFromListSafely(List<String> ls, int index) {
		if (CollectionUtils.isEmpty(ls)) {
			return null;
		}
		else if (ls.size() > index) {
			return ls.get(index);
		}
		return null;
	}

	/**
	 * Clean up unused sonar collector items
	 * @param collector the {@link SonarCollector}
	 */
	private void clean(SonarCollector collector, List<SonarProject> existingProjects) {
		// extract unique collector item IDs from components
		// (in this context collector_items are sonar projects)
		Set<ObjectId> uniqueIDs = StreamSupport.stream(dbComponentRepository.findAll().spliterator(), false)
			.filter(comp -> comp.getCollectorItems() != null && !comp.getCollectorItems().isEmpty())
			.map(comp -> comp.getCollectorItems().get(CollectorType.CodeQuality))
			// keep nonNull List<CollectorItem>
			.filter(Objects::nonNull)
			// merge all lists (flatten) into a stream
			.flatMap(List::stream)
			// keep nonNull CollectorItems
			.filter(ci -> ci != null && ci.getCollectorId().equals(collector.getId()))
			.map(CollectorItem::getId)
			.collect(Collectors.toSet());

		List<SonarProject> stateChangeJobList = new ArrayList<>();

		for (SonarProject job : existingProjects) {
			// collect the jobs that need to change state : enabled vs disabled.
			if ((job.isEnabled() && !uniqueIDs.contains(job.getId())) || // if it was
																			// enabled but
																			// not on a
																			// dashboard
					(!job.isEnabled() && uniqueIDs.contains(job.getId()))) { // OR it was
																				// disabled
																				// and now
																				// on a
				// dashboard
				job.setEnabled(uniqueIDs.contains(job.getId()));
				stateChangeJobList.add(job);
			}
		}
		if (!CollectionUtils.isEmpty(stateChangeJobList)) {
			sonarProjectRepository.saveAll(stateChangeJobList);
		}
	}

	private void deleteUnwantedJobs(List<SonarProject> latestProjects, List<SonarProject> existingProjects,
			SonarCollector collector) {
		List<SonarProject> deleteJobList = new ArrayList<>();

		// First delete collector items that are not supposed to be collected anymore
		// because the servers have moved(?)
		for (SonarProject job : existingProjects) {
			if (job.isPushed())
				continue; // do not delete jobs that are being pushed via API
			if (!collector.getSonarServers().contains(job.getInstanceUrl())
					|| (!job.getCollectorId().equals(collector.getId())) || (!latestProjects.contains(job))) {
				if (!job.isEnabled()) {
					LOG.debug("drop deleted sonar project which is disabled " + job.getProjectName());
					deleteJobList.add(job);
				}
				else {
					LOG.debug("drop deleted sonar project which is enabled " + job.getProjectName());
					// CollectorItem should be removed from components and dashboards
					// first
					// then the CollectorItem (sonar proj in this case) can be deleted

					List<com.apexon.compass.dashboard.model.Component> comps = dbComponentRepository
						.findByCollectorTypeAndItemIdIn(CollectorType.CodeQuality,
								Collections.singletonList(job.getId()));

					for (com.apexon.compass.dashboard.model.Component c : comps) {
						c.getCollectorItems().remove(CollectorType.CodeQuality);
					}
					dbComponentRepository.saveAll(comps);

					// other collectors also delete the widget but not here
					// should not remove the code analysis widget
					// because it is shared by other collectors

					deleteJobList.add(job);
				}
			}
		}
		if (!CollectionUtils.isEmpty(deleteJobList)) {
			sonarProjectRepository.deleteAll(deleteJobList);
		}
	}

	private void refreshData(List<SonarProject> sonarProjects, SonarClient sonarClient) {
		long start = System.currentTimeMillis();
		int count = 0;

		for (SonarProject project : sonarProjects) {
			DesCodeQuality desCodeQuality = sonarClient.currentDesCodeQuality(project, this.sonarConfiguration.getId(),
					this.sonarConfiguration.getIscProjectId(),
					String.join(",", this.sonarConfiguration.getMetricsFields()));
			if (desCodeQuality != null && isNewQualityData(project, desCodeQuality)) {
				// AWS S3 upload code
				uploadCodeToS3(desCodeQuality);
				count++;
			}
		}
		log("Updated", start, count);
	}

	private void uploadCodeToS3(DesCodeQuality desCodeQuality) {
		try {
			String region = Objects.isNull(environment.getProperty("AWS_REGION")) ? DEFAULT_REGION
					: environment.getProperty("AWS_REGION");
			Gson gson = new GsonBuilder().serializeNulls().create();
			String jsonData = gson.toJson(desCodeQuality);
			DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
			String dateString = formatter.format(Calendar.getInstance().getTime());
			OffsetDateTime utc = OffsetDateTime.now(ZoneOffset.UTC);
			long epochMillis = utc.toEpochSecond() * 1000;
			String objectPath = dateString + "/" + desCodeQuality.getName().replaceAll("[^A-Za-z0-9]+", "") + "/sonar_"
					+ epochMillis + ".json";
			FileUploadDto sonarUpload = new FileUploadDto(region, String.valueOf(getBucketName()), objectPath,
					jsonData);
			uploadService.uploadToStorage(sonarUpload);
			LOG.info(objectPath + " file uploaded to S3.");
		}
		catch (ClassCastException e) {
			LOG.info("Class Cast Exception:", e);
		}
		catch (Exception e) {
			LOG.error("Unable to upload file to S3: ", e);
		}
	}

	private void fetchQualityProfileConfigChanges(SonarCollector collector, String instanceUrl, SonarClient sonarClient)
			throws org.json.simple.parser.ParseException {
		JSONArray qualityProfiles = sonarClient.getQualityProfiles(instanceUrl);
		JSONArray sonarProfileConfigurationChanges = new JSONArray();

		for (Object qualityProfile : qualityProfiles) {
			JSONObject qualityProfileJson = (JSONObject) qualityProfile;
			String qualityProfileKey = (String) qualityProfileJson.get("key");

			List<String> sonarProjects = sonarClient.retrieveProfileAndProjectAssociation(instanceUrl,
					qualityProfileKey);
			if (sonarProjects != null) {
				sonarProfileConfigurationChanges = sonarClient.getQualityProfileConfigurationChanges(instanceUrl,
						qualityProfileKey);
				addNewConfigurationChanges(collector, sonarProfileConfigurationChanges);
			}
		}
	}

	private void addNewConfigurationChanges(SonarCollector collector, JSONArray sonarProfileConfigurationChanges) {
		ArrayList<CollectorItemConfigHistory> profileConfigChanges = new ArrayList<>();

		for (Object configChange : sonarProfileConfigurationChanges) {
			JSONObject configChangeJson = (JSONObject) configChange;
			CollectorItemConfigHistory profileConfigChange = new CollectorItemConfigHistory();
			Map<String, Object> changeMap = new HashMap<>();
			profileConfigChange.setCollectorItemId(collector.getId());
			profileConfigChange.setUserName((String) configChangeJson.get("authorName"));
			profileConfigChange.setUserID((String) configChangeJson.get("authorLogin"));
			changeMap.put("event", configChangeJson);

			profileConfigChange.setChangeMap(changeMap);

			ConfigHistOperationType operation = determineConfigChangeOperationType(
					(String) configChangeJson.get("action"));
			profileConfigChange.setOperation(operation);

			long timestamp = convertToTimestamp((String) configChangeJson.get("date"));
			profileConfigChange.setTimestamp(timestamp);

			if (isNewConfig(collector.getId(), (String) configChangeJson.get("authorLogin"), operation, timestamp)) {
				profileConfigChanges.add(profileConfigChange);
			}
		}
		sonarProfileRepostory.saveAll(profileConfigChanges);
	}

	private Boolean isNewConfig(ObjectId collectorId, String authorLogin, ConfigHistOperationType operation,
			long timestamp) {
		List<CollectorItemConfigHistory> storedConfigs = sonarProfileRepostory.findProfileConfigChanges(collectorId,
				authorLogin, operation, timestamp);
		return storedConfigs.isEmpty();
	}

	private List<SonarProject> enabledProjects(SonarCollector collector, String instanceUrl) {
		return sonarProjectRepository.findEnabledProjects(collector.getId(), instanceUrl);
	}

	private void addNewProjects(List<SonarProject> projects, List<SonarProject> existingProjects,
			SonarCollector collector) {
		long start = System.currentTimeMillis();
		int count = 0;
		List<SonarProject> newProjects = new ArrayList<>();
		List<SonarProject> updateProjects = new ArrayList<>();
		for (SonarProject project : projects) {
			String niceName = getNiceName(project, collector);
			if (!existingProjects.contains(project)) {
				project.setCollectorId(collector.getId());
				project.setEnabled(project.isEnabled());
				project.setDescription(project.getProjectName());
				project.setNiceName(niceName);
				newProjects.add(project);
				count++;
			}
			else {
				if (CollectionUtils.isNotEmpty(existingProjects)) {
					int[] indexes = IntStream.range(0, existingProjects.size())
						.filter(i -> existingProjects.get(i).equals(project))
						.toArray();
					for (int index : indexes) {
						SonarProject s = existingProjects.get(index);
						s.setProjectId(project.getProjectId());
						s.setProjectKey(project.getProjectKey());
						if (StringUtils.isEmpty(s.getNiceName())) {
							s.setNiceName(niceName);
						}
						s.setEnabled(project.isEnabled());
						updateProjects.add(s);
					}
				}
			}
		}
		// save all in one shot
		if (!CollectionUtils.isEmpty(newProjects)) {
			sonarProjectRepository.saveAll(newProjects);
		}
		if (!CollectionUtils.isEmpty(updateProjects)) {
			sonarProjectRepository.saveAll(updateProjects);
		}
		log("New projects", start, count);
	}

	private String getNiceName(SonarProject project, SonarCollector sonarCollector) {

		if (org.springframework.util.CollectionUtils.isEmpty(sonarCollector.getSonarServers()))
			return "";
		List<String> servers = sonarCollector.getSonarServers();
		List<String> niceNames = sonarCollector.getNiceNames();
		if (org.springframework.util.CollectionUtils.isEmpty(niceNames))
			return "";
		for (int i = 0; i < servers.size(); i++) {
			if (servers.get(i).equalsIgnoreCase(project.getInstanceUrl()) && (niceNames.size() > i)) {
				return niceNames.get(i);
			}
		}
		return "";
	}

	@SuppressWarnings("unused")
	private boolean isNewProject(SonarCollector collector, SonarProject application) {
		return sonarProjectRepository.findSonarProject(collector.getId(), application.getInstanceUrl(),
				application.getProjectId()) == null;
	}

	private boolean isNewQualityData(SonarProject project, DesCodeQuality desCodeQuality) {
		return codeQualityRepository.findByCollectorItemIdAndTimestamp(project.getId(),
				desCodeQuality.getTimestamp()) == null;
	}

	private long convertToTimestamp(String date) {

		DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ssZ");
		DateTime dt = formatter.parseDateTime(date);

		return new DateTime(dt).getMillis();
	}

	private ConfigHistOperationType determineConfigChangeOperationType(String changeAction) {
		switch (changeAction) {
			case "DEACTIVATED":
				return ConfigHistOperationType.DELETED;

			case "ACTIVATED":
				return ConfigHistOperationType.CREATED;
			default:
				return ConfigHistOperationType.CHANGED;
		}
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
