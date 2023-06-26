package com.apexon.compass.dashboard.collector;

import static com.apexon.compass.dashboard.utils.Utilities.getBoolean;
import static com.apexon.compass.dashboard.utils.Utilities.getLong;
import static com.apexon.compass.dashboard.utils.Utilities.getString;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import com.apexon.compass.dashboard.client.RestClient;
import com.apexon.compass.dashboard.model.*;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;

import com.apexon.compass.dashboard.client.RestClient;
import com.apexon.compass.dashboard.misc.HygieiaException;

/**
 * A client that communicates via REST API calls to jira.
 *
 * <p>
 * Latest REST API: https://docs.atlassian.com/jira/REST/latest/ <br>
 * Created against API for Jira 7.x. Should work with 6.x and 5.x.
 *
 * @author <a href="mailto:MarkRx@users.noreply.github.com">MarkRx</a>
 */
@Component
public class DefaultJiraClient implements JiraClient {

	private static final Logger LOGGER = LoggerFactory.getLogger(DefaultJiraClient.class);

	private static final String TEMPO_TEAMS_REST_SUFFIX = "rest/tempo-teams/1/team";

	private static final String BOARD_TEAMS_REST_SUFFIX = "rest/agile/1.0/board";

	private static final String PROJECT_REST_SUFFIX = "rest/api/2/project";

	private static final String ISSUE_BY_PROJECT_REST_SUFFIX_BY_DATE = "rest/api/2/search?jql=project=%s and issueType in ('%s') and updatedDate>='%s'&fields=%s&startAt=%s";

	private static final String ISSUE_BY_PROJECT_REST_SUFFIX_BY_DATE_CHANGE_LOG = "rest/api/2/search?jql=project=%s and issueType in ('%s') and updatedDate>='%s'&fields=%s&startAt=%s%&expand=changelog";

	private static final String ISSUE_BY_BOARD_REST_SUFFIX_BY_DATE = "rest/agile/1.0/board/%s/issue?jql=issueType in ('%s') and updatedDate>='%s'&fields=%s&startAt=%s";

	private static final String ISSUE_BY_BOARD_REST_SUFFIX_BY_DATE_CHANGE_LOG = "rest/agile/1.0/board/%s/issue?jql=issueType in ('%s') and updatedDate>='%s'&fields=%s&startAt=%s&expand=changelog";

	private static final String EPIC_REST_SUFFIX = "rest/agile/1.0/issue/%s";

	private static final String ISSUE_TYPES_REST_SUFFIX = "rest/api/2/issuetype";

	private static final String ISSUE_BY_BOARD_FULL_REFRESH_REST_SUFFIX = "rest/agile/1.0/%s/%s/issue?jql=issueType in ('%s') and updatedDate>='%s'&fields=id&startAt=%s";

	private static final String STATIC_ISSUE_FIELDS = "id,key,issuetype,status,summary,created,updated,project,issuelinks,assignee,sprint,epic,aggregatetimeoriginalestimate,timeoriginalestimate,subtasks,priority";

	private static final String DEFAULT_ISSUE_TYPES = "Story,Epic";

	private static final String EPIC_ISSUE_TYPE = "Epic";

	private static final int JIRA_BOARDS_PAGING = 50;

	private final RestClient restClient;

	private static JSONParser parser = new JSONParser();

	@Autowired
	public DefaultJiraClient(RestClient restClient) {
		this.restClient = restClient;
	}

	/**
	 * Get all the Scope (Project in Jira terms)
	 * @return List of Scope
	 */
	@Override
	public Set<Scope> getProjects(JiraConfiguration jiraConfiguration) {

		try {
			String url = jiraConfiguration.getUrl() + (jiraConfiguration.getUrl().endsWith("/") ? "" : "/")
					+ PROJECT_REST_SUFFIX;

			ResponseEntity<String> responseEntity = makeRestCall(url, jiraConfiguration);
			String responseBody = responseEntity.getBody();

			JSONArray projects = (JSONArray) parser.parse(responseBody);

			JSONArray filteredProjects = new JSONArray();
			// Filter project as per its isc project id
			for (int i = 0; i < projects.size(); ++i) {
				JSONObject obj = (JSONObject) projects.get(i);
				String id = (String) obj.get("id");
				if (jiraConfiguration.getJiraProjectId().equals(id)) {
					filteredProjects.add(obj);
				}
			}

			return parseAsScopes(filteredProjects);

		}
		catch (ParseException pe) {
			LOGGER.error("Parser exception when parsing teams", pe);
		}
		catch (HygieiaException e) {
			LOGGER.error("Error in calling JIRA API", e);
		}
		return Collections.EMPTY_SET;
	}

	protected static Set<Scope> parseAsScopes(JSONArray projects) {
		Set<Scope> result = new HashSet<>();
		if (CollectionUtils.isEmpty(projects)) {
			return Collections.EMPTY_SET;
		}

		for (Object obj : projects) {
			JSONObject jo = (JSONObject) obj;
			String pId = getString(jo, "id");
			String pName = getString(jo, "name").trim();
			if (!StringUtils.isEmpty(pName)) {
				Scope scope = new Scope();
				scope.setpId(pId);
				scope.setName(pName);
				scope.setProjectPath(pName);
				scope.setBeginDate("");
				// endDate - does not exist for jira
				scope.setEndDate("");
				// changeDate - does not exist for jira
				scope.setChangeDate("");
				// assetState - does not exist for jira
				// isDeleted - does not exist for jira
				scope.setIsDeleted("False");
				result.add(scope);
			}
		}

		return result;
	}

	/**
	 * Get a list of Boards. It's saved as Team in Hygieia
	 * @return List of Team
	 */
	@Override
	public List<JiraBoard> getBoards(JiraConfiguration jiraConfiguration) {
		int count = 0;
		int startAt = 0;
		boolean isLast = false;
		List<JiraBoard> result = new ArrayList<>();
		while (!isLast) {
			String url = jiraConfiguration.getUrl() + (jiraConfiguration.getUrl().endsWith("/") ? "" : "/")
					+ BOARD_TEAMS_REST_SUFFIX + "?startAt=" + startAt;
			try {
				ResponseEntity<String> responseEntity = makeRestCall(url, jiraConfiguration);
				String responseBody = responseEntity.getBody();
				JSONObject teamsJson = (JSONObject) parser.parse(responseBody);

				if (teamsJson != null) {
					JSONArray valuesArray = (JSONArray) teamsJson.get("values");
					if (!CollectionUtils.isEmpty(valuesArray)) {
						for (Object obj : valuesArray) {
							JSONObject jo = (JSONObject) obj;
							String teamId = getString(jo, "id");
							String teamName = getString(jo, "name");
							JiraBoard board = new JiraBoard();
							board.setBoardId(teamId);
							board.setBoardName(teamName);
							result.add(board);
							count = count + 1;
						}
						isLast = (boolean) teamsJson.get("isLast");
						LOGGER.info("JIRA Collector collected " + count + " boards");
						if (!isLast) {
							startAt += JIRA_BOARDS_PAGING;
						}
					}
					else {
						isLast = true;
					}
				}
				else {
					isLast = true;
				}
			}
			catch (ParseException pe) {
				isLast = true;
				LOGGER.error("Parser exception when parsing teams", pe);
			}
			catch (HygieiaException | HttpClientErrorException | HttpServerErrorException e) {
				isLast = true;
				LOGGER.error("Error in calling JIRA API: " + url, e.getMessage());
			}
		}
		return result;
	}

	/**
	 * Get all the teams using Jira Rest API
	 * @return List of Teams
	 */
	@Override
	public List<JiraBoard> getTeams(JiraConfiguration jiraConfiguration) {
		List<JiraBoard> result = new ArrayList<>();
		try {
			String url = jiraConfiguration.getUrl() + (jiraConfiguration.getUrl().endsWith("/") ? "" : "/")
					+ TEMPO_TEAMS_REST_SUFFIX;
			ResponseEntity<String> responseEntity = makeRestCall(url, jiraConfiguration);
			String responseBody = responseEntity.getBody();
			JSONArray teamsJson = (JSONArray) parser.parse(responseBody);
			if (teamsJson != null) {
				for (Object obj : teamsJson) {
					JSONObject jo = (JSONObject) obj;
					String teamId = getString(jo, "id");
					String teamName = getString(jo, "name");
					JiraBoard board = new JiraBoard();
					board.setBoardId(teamId);
					board.setBoardName(teamName);
					result.add(board);
				}
			}
		}
		catch (ParseException pe) {
			LOGGER.error("Parser exception when parsing teams", pe);
		}
		catch (HygieiaException e) {
			LOGGER.error("Error in calling JIRA API", e);
		}
		return result;
	}

	/**
	 * Get list of Features (Issues in Jira terms) given a project.
	 * @param board
	 * @return List of Feature
	 */
	@Override
	public FeatureEpicResult getIssues(JiraBoard board, Map<String, String> issueTypeIds,
			JiraConfiguration jiraConfiguration) {
		Map<String, Epic> epicMap = new HashMap<>();
		FeatureEpicResult featureEpicResult = new FeatureEpicResult();

		String lookBackDate = getUpdatedSince(jiraConfiguration.getUpdatedDate(), jiraConfiguration);

		String issueTypes = getIssueTypes(jiraConfiguration);

		List<DesFeature> features = new ArrayList<>();
		boolean isLast = false;
		long startAt = 0;

		String issueFields = STATIC_ISSUE_FIELDS + ',' + jiraConfiguration.getJiraTeamFieldName() + ','
				+ jiraConfiguration.getSprintDataFieldName() + ',' + jiraConfiguration.getJiraEpicIdFieldName() + ','
				+ jiraConfiguration.getJiraStoryPointsFieldName();

		while (!isLast) {
			String url = jiraConfiguration.getUrl() + (jiraConfiguration.getUrl().endsWith("/") ? "" : "/")
					+ ISSUE_BY_BOARD_REST_SUFFIX_BY_DATE_CHANGE_LOG;
			url = String.format(url, board.getBoardId(), issueTypes.replaceAll(",", "','"), lookBackDate, issueFields,
					startAt);
			try {
				IssueResult temp = getFeaturesFromQueryURL(url, epicMap, board, issueTypeIds, jiraConfiguration);

				if (temp.getTotal() >= jiraConfiguration.getMaxNumberOfFeaturesPerBoard()) {
					LOGGER.info("Board: \'" + board.getBoardName() + "\' passes the feature max limit at "
							+ temp.getTotal() + " features. Skipping..");
					isLast = true;
					continue;
				}

				// For issues collected in board mode, overwrite the team information
				temp.getFeatures().forEach(f -> {
					f.setsTeamID(board.getBoardId());
					f.setsTeamName(board.getBoardName());
					f.setIscProjectId(jiraConfiguration.getIscProjectId().toString());
				});
				features.addAll(temp.getFeatures());
				isLast = temp.getTotal() < startAt || temp.getTotal() < JIRA_BOARDS_PAGING;
				startAt += temp.getPageSize();
			}
			catch (ParseException pe) {
				isLast = true;
				LOGGER.error("Parser exception when parsing issue", pe);
			}
			catch (HygieiaException | HttpClientErrorException | HttpServerErrorException e) {
				isLast = true;
				LOGGER.error("Error in calling JIRA API: " + url, e.getMessage());
			}
		}
		featureEpicResult.setFeatureList(features);
		featureEpicResult.getEpicList().addAll(epicMap.values());
		return featureEpicResult;
	}

	@Override
	public Map<String, String> getJiraIssueTypeIds(JiraConfiguration jiraConfiguration) {
		Map<String, String> issueTypes = new HashMap<>();
		try {
			String url = jiraConfiguration.getUrl() + (jiraConfiguration.getUrl().endsWith("/") ? "" : "/")
					+ ISSUE_TYPES_REST_SUFFIX;
			ResponseEntity<String> responseEntity = makeRestCall(url, jiraConfiguration);
			String responseBody = responseEntity.getBody();

			JSONArray issueTypesArray = (JSONArray) parser.parse(responseBody);
			if (issueTypesArray == null) {
				throw new HygieiaException("Unable to get Issue Type IDs from: " + url,
						HygieiaException.INVALID_CONFIGURATION);
			}
			List<String> issueTypesFromSettings = new ArrayList<>(
					Arrays.asList(getIssueTypes(jiraConfiguration).split(",")));
			if (!issueTypesFromSettings.contains(EPIC_ISSUE_TYPE)) {
				// If the EPIC_ISSUE_TYPE isn't configured, add it automatically as this
				// issue type is
				// required by this collector.
				issueTypesFromSettings.add(EPIC_ISSUE_TYPE);
			}
			for (Object obj : issueTypesArray) {
				JSONObject jo = (JSONObject) obj;
				String name = getString(jo, "name");
				String id = getString(jo, "id");
				if (issueTypesFromSettings.contains(name)) {
					issueTypes.put(name, id);
				}
			}

		}
		catch (ParseException pe) {
			LOGGER.error("Parser exception when parsing teams", pe);
		}
		catch (HygieiaException e) {
			LOGGER.error("Error in calling JIRA API", e);
		}

		return issueTypes;
	}

	/**
	 * Get list of Features (Issues in Jira terms) given a project.
	 * @param project
	 * @return List of Feature
	 */
	@Override
	public FeatureEpicResult getIssues(Scope project, Map<String, String> issueTypeIds,
			JiraConfiguration jiraConfiguration) {
		Map<String, Epic> epicMap = new HashMap<>();
		FeatureEpicResult featureEpicResult = new FeatureEpicResult();

		String lookBackDate = getUpdatedSince(project.getLastCollected(), jiraConfiguration);

		String issueTypes = getIssueTypes(jiraConfiguration);

		List<DesFeature> features = new ArrayList<>();

		//
		String issueFields = STATIC_ISSUE_FIELDS + ',' + jiraConfiguration.getJiraTeamFieldName() + ','
				+ jiraConfiguration.getSprintDataFieldName() + ',' + jiraConfiguration.getJiraEpicIdFieldName() + ','
				+ jiraConfiguration.getJiraStoryPointsFieldName();

		boolean isLast = false;
		long startAt = 0;

		while (!isLast) {
			try {
				String url = jiraConfiguration.getUrl() + (jiraConfiguration.getUrl().endsWith("/") ? "" : "/")
						+ ISSUE_BY_PROJECT_REST_SUFFIX_BY_DATE_CHANGE_LOG;
				url = String.format(url, project.getpId(), issueTypes.replaceAll(",", "','"), lookBackDate, issueFields,
						startAt);

				IssueResult temp = getFeaturesFromQueryURL(url, epicMap, null, issueTypeIds, jiraConfiguration);

				features.addAll(temp.getFeatures());
				isLast = temp.getTotal() == features.size() || CollectionUtils.isEmpty(temp.getFeatures());
				startAt += temp.getPageSize();
			}
			catch (ParseException pe) {
				LOGGER.error("Parser exception when parsing issue", pe);
			}
			catch (HygieiaException e) {
				LOGGER.error("Error in calling JIRA API", e);
			}
		}
		featureEpicResult.setFeatureList(features);
		featureEpicResult.getEpicList().addAll(epicMap.values());
		return featureEpicResult;
	}

	private IssueResult getFeaturesFromQueryURL(String url, Map<String, Epic> epicMap, JiraBoard board,
			Map<String, String> issueTypeIds, JiraConfiguration jiraConfiguration)
			throws HygieiaException, ParseException {
		IssueResult result = new IssueResult();
		try {
			ResponseEntity<String> responseEntity = makeRestCall(url, jiraConfiguration);
			String responseBody = responseEntity.getBody();
			JSONObject bodyObject = (JSONObject) parser.parse(responseBody);

			if (bodyObject != null) {
				long pageSize = getLong(bodyObject, "maxResults");
				long total = getLong(bodyObject, "total");
				result.setPageSize(pageSize);
				result.setTotal(total);
				JSONArray issueArray = (JSONArray) bodyObject.get("issues");
				if (CollectionUtils.isEmpty(issueArray)) {
					return result;
				}

				issueArray.forEach(issue -> {
					JSONObject issueJson = (JSONObject) issue;
					String type = getIssueType(issueJson);

					if (!StringUtils.isEmpty(issueTypeIds.get(EPIC_ISSUE_TYPE))
							&& issueTypeIds.get(EPIC_ISSUE_TYPE).equals(type)) {
						saveEpic(issueJson, epicMap, true, jiraConfiguration);
						return;
					}

					boolean isValidIssueType = !type.equals(issueTypeIds.get(EPIC_ISSUE_TYPE))
							&& !CollectionUtils.isEmpty(issueTypeIds.values()) && issueTypeIds.values().contains(type);

					if (isValidIssueType) {
						DesFeature feature = getFeature((JSONObject) issue, board, jiraConfiguration);
						String epicId = feature.getsEpicID();
						if (!StringUtils.isEmpty(epicId)) {
							Epic epic = epicMap.containsKey(epicId) ? epicMap.get(epicId)
									: getEpic(epicId, epicMap, jiraConfiguration);
							if (!Objects.isNull(epic)) {
								processEpicData(feature, epic);
							}
							else {
								// Set empty epic id as it has invalid access / invalid
								// parsing occurred
								feature.setsEpicID("");
							}
						}
						result.getFeatures().add(feature);
					}
				});
			}
		}
		catch (HttpClientErrorException | HttpServerErrorException he) {
			LOGGER.error("ERROR collecting issues. " + he.getResponseBodyAsString() + ". Url = " + url);
		}
		return result;
	}

	private static String getIssueType(JSONObject issueJson) {
		JSONObject fields = (JSONObject) issueJson.get("fields");
		JSONObject issueType = (JSONObject) fields.get("issuetype");
		return getString(issueType, "id");
	}

	/**
	 * Returns issue types from settings or default values if settings don't exist
	 * @return issue type id string
	 */
	private String getIssueTypes(JiraConfiguration jiraConfiguration) {
		return jiraConfiguration.getIssueTypes() == null ? DEFAULT_ISSUE_TYPES
				: String.join(",", jiraConfiguration.getIssueTypes());
	}

	/**
	 * Construct Feature object
	 * @param issue
	 * @return DesFeature
	 */
	@SuppressWarnings("PMD.NPathComplexity")
	protected DesFeature getFeature(JSONObject issue, JiraBoard board, JiraConfiguration jiraConfiguration) {
		DesFeature feature = new DesFeature();
		feature.setsId(getString(issue, "id"));
		feature.setsNumber(getString(issue, "key"));

		JSONObject fields = (JSONObject) issue.get("fields");

		JSONObject epic = (JSONObject) fields.get("epic");
		String epicId = getString(fields, jiraConfiguration.getJiraEpicIdFieldName());
		feature.setsEpicID(epic != null ? getString(epic, "id") : epicId);

		JSONObject issueType = (JSONObject) fields.get("issuetype");
		if (issueType != null) {
			feature.setsTypeId(getString(issueType, "id"));
			feature.setsTypeName(getString(issueType, "name"));
		}

		JSONObject status = (JSONObject) fields.get("status");
		if (status != null) {
			feature.setsState(getString(status, "name"));
			feature.setsStatus(feature.getsState());
		}

		String summary = getString(fields, "summary");
		feature.setsName(summary);

		feature.setsUrl(jiraConfiguration.getUrl() + (jiraConfiguration.getUrl().endsWith("/") ? "" : "/") + "browse/"
				+ feature.getsNumber());

		long aggEstimate = getLong(fields, "aggregatetimeoriginalestimate");
		Long estimate = getLong(fields, "timeoriginalestimate");

		int originalEstimate = 0;
		// Tasks use timetracking, stories use aggregatetimeoriginalestimate and
		// aggregatetimeestimate
		if (estimate != 0) {
			originalEstimate = estimate.intValue();
		}
		else if (aggEstimate != 0) {
			// this value is in seconds
			originalEstimate = Math.round((float) aggEstimate / 3600);
		}

		feature.setsEstimateTime(originalEstimate);

		String storyPoints = getString(fields, jiraConfiguration.getJiraStoryPointsFieldName());

		feature.setsEstimate(storyPoints);

		feature.setCreatedDate(getString(fields, "created"));
		feature.setChangeDate(getString(fields, "updated"));
		feature.setIsDeleted("False");

		JSONObject project = (JSONObject) fields.get("project");
		feature.setsProjectID(project != null ? getString(project, "id") : "");
		feature.setsProjectName(project != null ? getString(project, "name") : "");
		// sProjectBeginDate - does not exist in Jira
		feature.setsProjectBeginDate("");
		// sProjectEndDate - does not exist in Jira
		feature.setsProjectEndDate("");
		// sProjectChangeDate - does not exist for this asset level in Jira
		feature.setsProjectChangeDate("");
		// sProjectState - does not exist in Jira
		feature.setsProjectState("");
		// sProjectIsDeleted - does not exist in Jira
		feature.setsProjectIsDeleted("False");
		// sProjectPath - does not exist in Jira
		feature.setsProjectPath("");

		JSONObject priority = (JSONObject) fields.get("priority");
		feature.setsPriorityId(priority != null ? getString(priority, "id") : StringUtils.EMPTY);
		feature.setsPriorityName(priority != null ? getString(priority, "name") : StringUtils.EMPTY);

		if (board != null) {
			feature.setsTeamID(board.getBoardId());
			feature.setsTeamName(board.getBoardName());
		}
		else {
			JSONObject team = (JSONObject) fields.get(jiraConfiguration.getJiraTeamFieldName());
			if (team != null) {
				feature.setsTeamID(getString(team, "id"));
				feature.setsTeamName(getString(team, "value"));
			}
		}
		// sTeamChangeDate - not able to retrieve at this asset level from Jira
		feature.setsTeamChangeDate("");
		// sTeamAssetState
		feature.setsTeamAssetState("");
		// sTeamIsDeleted
		feature.setsTeamIsDeleted("False");
		// sOwnersChangeDate - does not exist in Jira
		feature.setsOwnersChangeDate(Collections.EMPTY_LIST);
		// issueLinks
		JSONArray issueLinkArray = (JSONArray) fields.get("issuelinks");
		feature.setIssueLinks(getIssueLinks(issueLinkArray));
		// subtasks
		JSONArray subTaskArray = (JSONArray) fields.get("subtasks");
		feature.setSubTasks(getSubTasks(subTaskArray));

		Sprint sprint = getSprint(fields, jiraConfiguration);
		if (sprint != null) {
			processSprintData(feature, sprint, jiraConfiguration);
		}
		JSONObject assignee = (JSONObject) fields.get("assignee");
		processAssigneeData(feature, assignee);

		List<Sprint> sprintJourney = getSprintJourney(fields, jiraConfiguration);

		List<String> sprintJourneyIds = new ArrayList<>();
		if (sprintJourney != null) {
			for (Sprint prevSprint : sprintJourney) {
				sprintJourneyIds.add(prevSprint.getId());
			}
		}
		feature.setSprintJourney(sprintJourneyIds);

		// Process Change Log data

		JSONObject changelog = issue.get("changelog") != null ? (JSONObject) issue.get("changelog") : null;
		JSONArray histories = changelog != null ? (JSONArray) changelog.get("histories") : new JSONArray();

		List<FeatureChange> changes = new ArrayList<>();

		histories.forEach(history -> {
			JSONObject historyJSON = (JSONObject) history;
			FeatureChange change = getChanges(historyJSON, jiraConfiguration);
			changes.add(change);
		});

		feature.setChanges(changes);

		return feature;
	}

	/**
	 * Construct FeatureChange object
	 * @param history
	 * @return FeatureChange
	 */
	@SuppressWarnings("PMD.NPathComplexity")
	protected FeatureChange getChanges(JSONObject history, JiraConfiguration jiraConfiguration) {
		FeatureChange change = new FeatureChange();

		change.setChangeId((String) history.get("id"));
		change.setChangeCreatedDateTime((String) history.get("created"));

		JSONObject authorData = (JSONObject) history.get("author");
		processAuthorData(change, authorData);

		JSONArray itemsArray = (JSONArray) history.get("items");

		List<FeatureChangesUpdate> updates = new ArrayList<>();

		itemsArray.forEach(item -> {
			JSONObject itemJSON = (JSONObject) item;
			FeatureChangesUpdate update = getUpdates(itemJSON, jiraConfiguration);
			updates.add(update);
		});

		change.setUpdates(updates);
		return change;
	}

	/**
	 * Process Assignee data for a feature, updating the passed in feature
	 * @param change
	 * @param author
	 */
	@SuppressWarnings("PMD.NPathComplexity")
	protected static void processAuthorData(FeatureChange change, JSONObject author) {
		if (author == null) {
			return;
		}

		String displayName = getString(author, "displayName");
		change.setAuthorFullName(displayName != null ? displayName : "");
		Boolean isActive = getBoolean(author, "active");
		if (isActive) {
			change.setAuthorState("Active");
			change.setAuthorIsDeleted("false");
		}
		else {
			change.setAuthorState("Inactive");
			change.setAuthorIsDeleted("true");
		}

		String key = getString(author, "key");
		if (key == null || key == "") {
			// API 2.0 compatible (Latest Jira Version / Jira Clouds)
			key = getString(author, "accountId");
			change.setAuthorID(key != null ? key : "");
		}
		else {
			// API 1.0 compatible (Old Jira Version / Jira Server)
			String name = getString(author, "name");
			String emailAddress = getString(author, "emailAddress");
			change.setAuthorID(key != null ? key : "");
			change.setAuthorUsername(emailAddress != null ? emailAddress : "");
			change.setAuthorShortName(name != null ? name : "");
		}
	}

	/**
	 * Construct FeatureChangesUpdate object
	 * @param item
	 * @return FeatureChangesUpdate
	 */
	@SuppressWarnings("PMD.NPathComplexity")
	protected FeatureChangesUpdate getUpdates(JSONObject item, JiraConfiguration jiraConfiguration) {
		FeatureChangesUpdate update = new FeatureChangesUpdate();

		String fieldId = (String) item.get("fieldId");

		update.setField((String) item.get("field"));
		update.setFieldtype((String) item.get("fieldtype"));

		if (fieldId == null) {
			update.setFieldId(null);
		}
		else if (fieldId.equalsIgnoreCase(jiraConfiguration.getSprintDataFieldName())) {
			update.setFieldId("SprintData");
		}
		else if (fieldId.equalsIgnoreCase(jiraConfiguration.getJiraEpicIdFieldName())) {
			update.setFieldId("EpicId");
		}
		else if (fieldId.equalsIgnoreCase(jiraConfiguration.getJiraStoryPointsFieldName())) {
			update.setFieldId("StoryPoints");
		}
		else {
			update.setFieldId(fieldId);
		}

		if (item.get("from") == null) {
			update.setFrom(null);
		}
		else {
			update.setFrom((String) item.get("from"));
		}

		if (item.get("fromString") == null) {
			update.setFromString(null);
		}
		else {
			update.setFromString((String) item.get("fromString"));
		}

		if (item.get("to") == null) {
			update.setTo(null);
		}
		else {
			update.setTo((String) item.get("to"));
		}

		if (item.get("toString") == null) {
			update.setToString(null);
		}
		else {
			update.setToString((String) item.get("toString"));
		}

		return update;
	}

	/**
	 * Get status
	 * @param status
	 * @return status
	 */
	private static String getStatus(JSONObject status) {
		if (status == null) {
			return "";
		}

		String statusString = getString(status, "name");
		DESFeatureStatus normalizedStatus;
		switch (statusString) {
			case "Open":
			case "Reopened":
				normalizedStatus = DESFeatureStatus.BACKLOG;
				break;

			case "In Progress":
			case "Testing":
			case "Review":
				normalizedStatus = DESFeatureStatus.IN_PROGRESS;
				break;

			case "Resolved":
				normalizedStatus = DESFeatureStatus.DONE;
				break;

			case "Closed":
				normalizedStatus = DESFeatureStatus.CLOSED;
				break;

			default:
				normalizedStatus = DESFeatureStatus.BACKLOG;
				break;
		}
		return normalizedStatus.getStatus();
	}

	/**
	 * Process Epic data for a feature, updating the passed in feature
	 * @param feature
	 * @param epic
	 */
	protected static void processEpicData(DesFeature feature, Epic epic) {
		feature.setsEpicID(epic.getId());
		feature.setsEpicIsDeleted("false");
		feature.setsEpicName(epic.getName());
		feature.setsEpicNumber(epic.getNumber());
		feature.setsEpicType("");
		feature.setsEpicAssetState(epic.getStatus());
		feature.setsEpicBeginDate(epic.getBeginDate());
		feature.setsEpicChangeDate(epic.getChangeDate());
		feature.setsEpicEndDate(epic.getEndDate());
		feature.setsEpicUrl(epic.getUrl());
	}

	protected Sprint getSprint(JSONObject fields, JiraConfiguration jiraConfiguration) {
		JSONObject sprintJson = (JSONObject) fields.get("sprint");
		if (sprintJson != null) {
			Sprint sprint = new Sprint();
			sprint.setId(getString(sprintJson, "id"));
			sprint.setName(getString(sprintJson, "name"));
			sprint.setStartDateStr(getString(sprintJson, "startDate"));
			sprint.setEndDateStr(getString(sprintJson, "endDate"));
			sprint.setState(getString(sprintJson, "state"));
			sprint.setRapidViewId(getString(sprintJson, "originBoardId"));
			return sprint;
		}
		else {
			JSONArray sprintCustom = (JSONArray) fields.get(jiraConfiguration.getSprintDataFieldName());
			return SprintFormatter.parseSprint(sprintCustom);
		}
	}

	protected List<Sprint> getSprintJourney(JSONObject fields, JiraConfiguration jiraConfiguration) {
		JSONArray sprintCustom = (JSONArray) fields.get(jiraConfiguration.getSprintDataFieldName());
		return SprintFormatter.parseSprintJourney(sprintCustom);
	}

	/**
	 * Process Sprint data for a feature, updating the passed in feature
	 * @param feature
	 * @param sprint
	 */
	protected void processSprintData(DesFeature feature, Sprint sprint, JiraConfiguration jiraConfiguration) {

		// sSprintChangeDate - does not exist in Jira
		feature.setsSprintChangeDate("");
		// sSprintIsDeleted - does not exist in Jira
		feature.setsSprintIsDeleted("False");

		feature.setsSprintID(sprint.getId());
		feature.setsSprintName(sprint.getName());
		feature.setsSprintBeginDate(sprint.getStartDateStr());
		feature.setsSprintEndDate(sprint.getEndDateStr());
		feature.setsSprintAssetState(sprint.getState());
		String rapidViewId = sprint.getRapidViewId();
		if (!StringUtils.isEmpty(rapidViewId) && !StringUtils.isEmpty(feature.getsSprintID())) {
			feature.setsSprintUrl(jiraConfiguration.getUrl()
					+ (Objects.equals(jiraConfiguration.getUrl().substring(jiraConfiguration.getUrl().length() - 1),
							"/") ? "" : "/")
					+ "secure/RapidBoard.jspa?rapidView=" + rapidViewId
					+ "&view=reporting&chart=sprintRetrospective&sprint=" + feature.getsSprintID());
		}
	}

	/**
	 * Process Assignee data for a feature, updating the passed in feature
	 * @param feature
	 * @param assignee
	 */
	@SuppressWarnings("PMD.NPathComplexity")
	protected static void processAssigneeData(DesFeature feature, JSONObject assignee) {
		if (assignee == null) {
			return;
		}

		String displayName = getString(assignee, "displayName");
		feature
			.setsOwnersFullName(displayName != null ? Collections.singletonList(displayName) : Collections.EMPTY_LIST);

		String emailAddress = getString(assignee, "emailAddress");
		feature.setsOwnersUsername(
				emailAddress != null ? Collections.singletonList(emailAddress) : Collections.EMPTY_LIST);

		Boolean isActive = getBoolean(assignee, "active");
		if (isActive) {
			feature.setsOwnersState(Collections.singletonList("Active"));
			feature.setsOwnersIsDeleted(Collections.singletonList("false"));
		}
		else {
			feature.setsOwnersState(Collections.singletonList("Inactive"));
			feature.setsOwnersIsDeleted(Collections.singletonList("true"));
		}

		String key = getString(assignee, "key");
		if (key == null || key == "") {
			// API 2.0 compatible (Latest Jira Version / Jira Clouds)
			key = getString(assignee, "accountId");
			feature.setsOwnersID(key != null ? Collections.singletonList(key) : Collections.EMPTY_LIST);
		}
		else {
			// API 1.0 compatible (Old Jira Version / Jira Server)
			String name = getString(assignee, "name");
			feature.setsOwnersID(key != null ? Collections.singletonList(key) : Collections.EMPTY_LIST);
			feature.setsOwnersShortName(name != null ? Collections.singletonList(name) : Collections.EMPTY_LIST);
		}
	}

	/**
	 * Get Array of issuesLinks
	 * @param issueLinkArray
	 * @return List of FeatureIssueLink
	 */
	protected static List<FeatureIssueLink> getIssueLinks(JSONArray issueLinkArray) {
		if (issueLinkArray == null) {
			return Collections.EMPTY_LIST;
		}
		List<FeatureIssueLink> jiraIssueLinks = new ArrayList<>();

		issueLinkArray.forEach(issueLink -> {
			JSONObject outward = (JSONObject) ((JSONObject) issueLink).get("outwardIssue");
			JSONObject inward = (JSONObject) ((JSONObject) issueLink).get("inwardIssue");
			JSONObject type = (JSONObject) ((JSONObject) issueLink).get("type");
			String targetKey = "";
			String direction = "";
			String name = "";
			String description = "";
			String targetUrl = "";

			if (outward != null) {
				targetKey = getString(outward, "key");
				direction = "OUTBOUND";
				name = getString(type, "name");
				description = getString(type, "outward");
				targetUrl = getString(outward, "self");

			}
			else if (inward != null) {
				targetKey = getString(inward, "key");
				direction = "INBOUND";
				name = getString(type, "name");
				description = getString(type, "inward");
				targetUrl = getString(inward, "self");
			}
			FeatureIssueLink jiraIssueLink = new FeatureIssueLink();
			// story number of the linked issue
			jiraIssueLink.setTargetIssueKey(targetKey);
			// name of the linked issue
			jiraIssueLink.setIssueLinkName(name);
			// type of the linked issue
			jiraIssueLink.setIssueLinkType(description);
			// direction of the linked issue (inbount/outbound)
			jiraIssueLink.setIssueLinkDirection(direction);
			// uri of the linked issue
			jiraIssueLink.setTargetIssueUri(targetUrl);
			jiraIssueLinks.add(jiraIssueLink);
		});
		return jiraIssueLinks;
	}

	/**
	 * Get Array of subtasks
	 * @param subTaskArray
	 * @return List of FeatureSubTask
	 */
	protected static List<FeatureSubTask> getSubTasks(JSONArray subTaskArray) {
		if (subTaskArray == null) {
			return Collections.EMPTY_LIST;
		}
		List<FeatureSubTask> jiraSubTasks = new ArrayList<>();

		subTaskArray.forEach(subTask -> {
			JSONObject fields = (JSONObject) ((JSONObject) subTask).get("fields");
			JSONObject status = (JSONObject) ((JSONObject) fields).get("status");
			JSONObject priority = (JSONObject) ((JSONObject) fields).get("priority");
			JSONObject issuetype = (JSONObject) ((JSONObject) fields).get("issuetype");
			JSONObject statusCategory = (JSONObject) ((JSONObject) status).get("statusCategory");

			String issueId = getString((JSONObject) subTask, "id");
			String issueKey = getString((JSONObject) subTask, "key");
			String issueUrl = getString((JSONObject) subTask, "self");
			String issueSummary = "";
			String statusId = "";
			String statusName = "";
			String statusCategoryId = "";
			String statusCategoryKey = "";
			String statusCategoryName = "";
			String priorityId = "";
			String priorityName = "";
			String issueTypeId = "";
			String issueTypeName = "";

			if (fields != null) {
				issueSummary = getString(fields, "summary");
				if (status != null) {
					statusId = getString(status, "id");
					statusName = getString(status, "name");
					if (statusCategory != null) {
						statusCategoryId = getString(statusCategory, "id");
						statusCategoryKey = getString(statusCategory, "key");
						statusCategoryName = getString(statusCategory, "name");
					}
				}
				if (priority != null) {
					priorityId = getString(priority, "id");
					priorityName = getString(priority, "name");
				}
				if (issuetype != null) {
					issueTypeId = getString(issuetype, "id");
					issueTypeName = getString(issuetype, "name");
				}
			}

			FeatureSubTask jiraSubTask = new FeatureSubTask();
			jiraSubTask.setIssueId(issueId);
			jiraSubTask.setIssueKey(issueKey);
			jiraSubTask.setIssueSummary(issueSummary);
			jiraSubTask.setIssueTypeId(issueTypeId);
			jiraSubTask.setIssueTypeName(issueTypeName);
			jiraSubTask.setIssueUrl(issueUrl);
			jiraSubTask.setPriorityId(priorityId);
			jiraSubTask.setPriorityName(priorityName);
			jiraSubTask.setStatusCategoryId(statusCategoryId);
			jiraSubTask.setStatusCategoryKey(statusCategoryKey);
			jiraSubTask.setStatusCategoryName(statusCategoryName);
			jiraSubTask.setStatusId(statusId);
			jiraSubTask.setStatusName(statusName);
			jiraSubTasks.add(jiraSubTask);
		});
		return jiraSubTasks;
	}

	/**
	 * Get Epic using Jira API
	 * @param epicKey
	 * @return epic
	 */
	@Override
	public Epic getEpic(String epicKey, Map<String, Epic> epicMap, JiraConfiguration jiraConfiguration) {
		String url = jiraConfiguration.getUrl() + (jiraConfiguration.getUrl().endsWith("/") ? "" : "/")
				+ String.format(EPIC_REST_SUFFIX, epicKey);
		try {

			ResponseEntity<String> responseEntity = makeRestCall(url, jiraConfiguration);
			String responseBody = responseEntity.getBody();
			JSONObject issue = (JSONObject) parser.parse(responseBody);

			if (issue == null) {
				return null;
			}

			return saveEpic(issue, epicMap, false, jiraConfiguration);
		}
		catch (ParseException pe) {
			LOGGER.error("Parser exception when parsing teams", pe);
		}
		catch (HygieiaException e) {
			LOGGER.error("ERROR: in calling JIRA API", e);
		}
		catch (HttpClientErrorException | HttpServerErrorException he) {
			LOGGER.info("INFO: collecting epic " + he.getResponseBodyAsString() + " for url: " + url);
		}
		catch (Exception e) {
			LOGGER.error("ERROR: collecting epic ", e.getLocalizedMessage());
		}
		return null;
	}

	private Epic saveEpic(JSONObject issueJson, Map<String, Epic> epicMap, boolean recentUpdate,
			JiraConfiguration jiraConfiguration) {
		Epic epic = new Epic();
		epic.setId(getString(issueJson, "id"));
		epic.setNumber(getString(issueJson, "key"));
		JSONObject fields = (JSONObject) issueJson.get("fields");
		epic.setName(getString(fields, "summary"));
		epic.setChangeDate(getString(fields, "updated"));
		epic.setBeginDate(getString(fields, "created"));
		epic.setEndDate(getString(fields, "resolutiondate"));
		JSONObject status = (JSONObject) fields.get("status");
		epic.setStatus(getString(status, "name"));
		epic.setRecentUpdate(recentUpdate);
		epic.setUrl(jiraConfiguration.getUrl() + (jiraConfiguration.getUrl().endsWith("/") ? "" : "/") + "browse/"
				+ epic.getNumber());
		if (epicMap.containsKey(epic.getId())) {
			if (recentUpdate) {
				epicMap.put(epic.getId(), epic);
			}
		}
		else {
			epicMap.put(epic.getId(), epic);
		}
		return epic;
	}

	@Override
	public List<String> getAllIssueIds(String id, JiraMode mode, JiraConfiguration jiraConfiguration) {
		int count = 0;
		int startAt = 0;
		boolean isLast = false;
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm");
		String updatedDate = LocalDateTime.now()
			.minusDays(jiraConfiguration.getFirstRunHistoryDays())
			.format(formatter);
		String issueTypes = getIssueTypes(jiraConfiguration);
		List<String> result = new ArrayList<>();
		while (!isLast) {

			String url = jiraConfiguration.getUrl() + (jiraConfiguration.getUrl().endsWith("/") ? "" : "/")
					+ ISSUE_BY_BOARD_FULL_REFRESH_REST_SUFFIX;
			url = String.format(url, mode.toString().toLowerCase(), id, issueTypes.replaceAll(",", "','"), updatedDate,
					startAt);
			try {
				ResponseEntity<String> responseEntity = makeRestCall(url, jiraConfiguration);

				String responseBody = responseEntity.getBody();
				JSONObject jsonObject = (JSONObject) parser.parse(responseBody);

				if (jsonObject != null) {
					JSONArray valuesArray = (JSONArray) jsonObject.get("issues");
					if (!CollectionUtils.isEmpty(valuesArray)) {
						for (Object obj : valuesArray) {
							String sId = getString((JSONObject) obj, "id");
							result.add(sId);
							count = count + 1;
						}
						long total = getLong(jsonObject, "total");
						long maxResults = getLong(jsonObject, "maxResults");
						isLast = total == result.size();

						startAt += maxResults;
					}
					else {
						isLast = true;
					}

				}
				else {
					isLast = true;
				}
			}
			catch (ParseException pe) {
				isLast = true;
				LOGGER.error("Parser exception when parsing issue refresh json", pe);
			}
			catch (HygieiaException | HttpClientErrorException | HttpServerErrorException e) {
				isLast = true;
				LOGGER.error("Error in calling JIRA API: " + url, e.getMessage());
			}
		}
		return result;
	}

	////////////////// Helper Methods ////////////////////

	private ResponseEntity<String> makeRestCall(String url, JiraConfiguration jiraConfiguration)
			throws HygieiaException {
		String jiraAccess = jiraConfiguration.getJiraApiKey();
		if (StringUtils.isEmpty(jiraAccess)) {
			return restClient.makeRestCallGet(url);
		}
		else {
			String jiraAccessBase64 = new String(Base64.decodeBase64(jiraAccess));
			String[] parts = jiraAccessBase64.split(":");
			if (parts.length != 2) {
				throw new HygieiaException("Invalid Jira credentials", HygieiaException.INVALID_CONFIGURATION);
			}
			return restClient.makeRestCallGet(url, createHeaders(parts[0], parts[1]));
		}
	}

	private static HttpHeaders createHeaders(final String userId, final String password) {
		String auth = userId + ':' + password;
		byte[] encodedAuth = Base64.encodeBase64(auth.getBytes(StandardCharsets.US_ASCII));
		String authHeader = "Basic " + new String(encodedAuth);

		HttpHeaders headers = new HttpHeaders();
		headers.set("Authorization", authHeader);
		return headers;
	}

	protected String getUpdatedSince(long lastCollected, JiraConfiguration jiraConfiguration) {
		LocalDateTime since = LocalDateTime.now();
		if (lastCollected == 0) {
			since = since.minusDays(jiraConfiguration.getFirstRunHistoryDays());
		}
		else {
			since = LocalDateTime.ofInstant(Instant.ofEpochMilli(lastCollected), ZoneId.systemDefault());
			since = since.minusDays(1);
		}
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd");
		return since.format(formatter);
	}

}
