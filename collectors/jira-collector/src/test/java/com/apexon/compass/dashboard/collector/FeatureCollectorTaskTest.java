package com.apexon.compass.dashboard.collector;

import com.apexon.compass.dashboard.client.RestClient;
import com.apexon.compass.dashboard.client.RestOperationsSupplier;
import com.apexon.compass.dashboard.common.TestUtils;
import com.apexon.compass.dashboard.config.TestConfig;
import com.apexon.compass.dashboard.model.*;
import com.apexon.compass.dashboard.repository.*;
import com.apexon.compass.dashboard.testutil.GsonUtil;
import com.apexon.compass.dashboard.utils.AsyncUtil;
import com.github.fakemongo.junit.FongoRule;
import com.google.common.io.Resources;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.apache.commons.io.IOUtils;
import org.bson.types.ObjectId;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.client.RestOperations;

import java.io.IOException;
import java.net.URL;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { TestConfig.class, AsyncUtil.class })
@DirtiesContext
public class FeatureCollectorTaskTest {

	private String TEAM_TYPE_SCRUM = "scrum";

	@Mock
	private DefaultJiraClient defaultJiraClient;

	@InjectMocks
	private FeatureCollectorTask featureCollectorTask;

	@Rule
	public FongoRule fongoRule = new FongoRule();

	@SuppressWarnings("unchecked")
	@Mock
	private RestOperationsSupplier restOperationsSupplier = mock(RestOperationsSupplier.class);

	@Mock
	private RestOperations rest = mock(RestOperations.class);

	@Autowired
	private DesJiraConfiguration desJiraConfiguration;

	@Autowired
	private FeatureCollectorRepository featureCollectorRepository;

	@Autowired
	private TeamRepository teamRepository;

	@Autowired
	private ScopeRepository projectRepository;

	@Autowired
	private FeatureRepository featureRepository;

	@Autowired
	private JiraConfigurationRepository jiraConfigurationRepository;

	@Mock
	private FeatureBoardRepository featureBoardRepository = mock(FeatureBoardRepository.class);

	@Mock
	private FeatureCollector featureCollector;

	private AsyncUtil asyncUtil;

	@Mock
	private AsyncUtil asyncUtilSpy;

	@Before
	public void loadStuff() throws IOException {
		TestUtils.loadCollectorFeature(featureCollectorRepository);
		TestUtils.loadTeams(teamRepository);
		TestUtils.loadScope(projectRepository);
		TestUtils.loadFeature(featureRepository);
		when(restOperationsSupplier.get()).thenReturn(rest);
		RestClient restClient = new RestClient(restOperationsSupplier);
		defaultJiraClient = new DefaultJiraClient(restClient);
		asyncUtil = new AsyncUtil();
		asyncUtilSpy = Mockito.spy(asyncUtil);
		featureCollectorTask = new FeatureCollectorTask(null, featureRepository, teamRepository, projectRepository,
				featureCollectorRepository, jiraConfigurationRepository, defaultJiraClient, featureBoardRepository,
				desJiraConfiguration, asyncUtilSpy);

		doReturn(new ResponseEntity<>(getExpectedJSON("response/issuetype.json"), HttpStatus.OK)).when(rest)
			.exchange(contains("rest/api/2/issuetype"), eq(HttpMethod.GET), any(HttpEntity.class), eq(String.class));
		featureCollector = featureCollectorTask.getCollector();
		featureCollector.setId(new ObjectId("5c38f2f087cd1f53ca81bd3d"));
		doNothing().when(asyncUtilSpy).close();
	}

	@Test
	public void shouldCollect() throws IOException {
		doReturn(new ResponseEntity<>(getExpectedJSON("response/projectresponse.json"), HttpStatus.OK)).when(rest)
			.exchange(contains("api/2/project"), eq(HttpMethod.GET), any(HttpEntity.class), eq(String.class));
		doReturn(new ResponseEntity<>(getExpectedJSON("response/boardsresponse.json"), HttpStatus.OK)).when(rest)
			.exchange(contains("/rest/agile/1.0/board"), eq(HttpMethod.GET), any(HttpEntity.class), eq(String.class));

		featureCollectorTask.collect(featureCollector);
		assertNotNull(teamRepository.findByTeamId("999"));
	}

	@Test
	public void validateTeamCleanUp() throws IOException {
		doReturn(new ResponseEntity<>(getExpectedJSON("response/projectresponse.json"), HttpStatus.OK)).when(rest)
			.exchange(contains("api/2/project"), eq(HttpMethod.GET), any(HttpEntity.class), eq(String.class));
		String issueResponseQuery = "&fields=id,key,issuetype,status,summary,created,updated,project,issuelinks,assignee,sprint,epic,aggregatetimeoriginalestimate,timeoriginalestimate,customfield_11248,customfield_10007,customfield_10003,customfield_10004";
		doReturn(new ResponseEntity<>(getExpectedJSON("response/issueresponse-combo.json"), HttpStatus.OK)).when(rest)
			.exchange(contains(issueResponseQuery), eq(HttpMethod.GET), any(HttpEntity.class), eq(String.class));
		doReturn(new ResponseEntity<>(getExpectedJSON("response/issueresponse-empty.json"), HttpStatus.OK)).when(rest)
			.exchange(contains("/issue?jql=issueType"), eq(HttpMethod.GET), any(HttpEntity.class), eq(String.class));
		doReturn(new ResponseEntity<>(getExpectedJSON("response/boardsresponse.json"), HttpStatus.OK)).when(rest)
			.exchange(contains("/rest/agile/1.0/board?"), eq(HttpMethod.GET), any(HttpEntity.class), eq(String.class));
		featureCollector.setLastExecuted(System.currentTimeMillis());
		featureCollectorTask.collect(featureCollector);
		assertNull(teamRepository.findByTeamId("996"));
		assertNull(teamRepository.findByTeamId("997"));
	}

	@Test
	public void addBoardAsTeamInformation() throws IOException {
		List<JiraBoard> expected = getExpectedReviewResponse("./expected/boardasteam-expected.json");
		doReturn(new ResponseEntity<>(getExpectedJSON("response/boardsresponse.json"), HttpStatus.OK)).when(rest)
			.exchange(contains("jira"), eq(HttpMethod.GET), any(HttpEntity.class), eq(String.class));
		List<JiraBoard> actual = featureCollectorTask.updateTeamInformation(featureCollector);
		assertThat(expected.get(0).getBoardId()).isEqualTo(actual.get(0).getBoardId());
		assertThat(expected.get(0).getBoardName()).isEqualTo(actual.get(0).getBoardName());
	}

	@Test
	public void updateTeamAsBoardInformation() throws IOException {
		doReturn(new ResponseEntity<>(getExpectedJSON("response/boardsresponse-update.json"), HttpStatus.OK)).when(rest)
			.exchange(contains("jira"), eq(HttpMethod.GET), any(HttpEntity.class), eq(String.class));
		List<JiraBoard> expected = featureCollectorTask.updateTeamInformation(featureCollector);
		assertNotNull(teamRepository.findByName(expected.get(0).getBoardName()));
	}

	@Test
	public void updateTeamAsBoardType() throws IOException {
		doReturn(new ResponseEntity<>(getExpectedJSON("response/boardsresponse-update-1.json"), HttpStatus.OK))
			.when(rest)
			.exchange(contains("jira"), eq(HttpMethod.GET), any(HttpEntity.class), eq(String.class));
		List<JiraBoard> expected = featureCollectorTask.updateTeamInformation(featureCollector);

		assertEquals(TEAM_TYPE_SCRUM, teamRepository.findByTeamId(expected.get(0).getBoardId()).getTeamType());
	}

	@Test
	public void addStoryInformation() throws IOException {
		List<DesFeature> expected = getExpectedFeature("./expected/feature-epic-expected.json");
		String epicId = expected.get(0).getsEpicID();
		doReturn(new ResponseEntity<>(getExpectedJSON("response/epicresponse.json"), HttpStatus.OK)).when(rest)
			.exchange(contains("/rest/agile/1.0/issue/" + epicId), eq(HttpMethod.GET), any(HttpEntity.class),
					eq(String.class));
		doReturn(new ResponseEntity<>(getExpectedJSON("response/issueresponse-story.json"), HttpStatus.OK)).when(rest)
			.exchange(contains("board/999"), eq(HttpMethod.GET), any(HttpEntity.class), eq(String.class));
		when(featureBoardRepository.findFeatureBoard(new ObjectId(), "123455")).thenReturn(null);
		List<JiraBoard> jiraBoard = getExpectedReviewResponse("./expected/boardasteam-expected.json");
		// Todo :check s3 upload
		featureCollectorTask.updateStoryInformation(jiraBoard);
		List<Feature> actual = featureRepository.getStoryByTeamID(expected.get(0).getsTeamID());
		assertThat(actual.size()).isEqualTo(0);
	}

	@Test
	public void addStoryInformationTypeAll() throws IOException {
		List<DesFeature> expected = getExpectedFeature("./expected/feature-epic-expected.json");
		String epicId = expected.get(0).getsEpicID();
		doReturn(new ResponseEntity<>(getExpectedJSON("response/epicresponse.json"), HttpStatus.OK)).when(rest)
			.exchange(contains("/rest/agile/1.0/issue/" + epicId), eq(HttpMethod.GET), any(HttpEntity.class),
					eq(String.class));
		doReturn(new ResponseEntity<>(getExpectedJSON("response/issueresponse-combo.json"), HttpStatus.OK)).when(rest)
			.exchange(contains("board/999"), eq(HttpMethod.GET), any(HttpEntity.class), eq(String.class));
		when(featureBoardRepository.findFeatureBoard(new ObjectId(), "123455")).thenReturn(null);
		List<JiraBoard> jiraBoard = getExpectedReviewResponse("./expected/boardasteam-expected.json");
		// Todo :check s3 upload
		featureCollectorTask.updateStoryInformation(jiraBoard);
		List<Feature> actual = featureRepository.getStoryByTeamID(expected.get(0).getsTeamID());
		assertThat(actual.size()).isEqualTo(0);
	}

	@Test
	public void updateFeatureEpicName() throws IOException {

		List<DesFeature> expected = getExpectedFeature("./expected/feature-epic-expected.json");
		String epicId = expected.get(0).getsEpicID();
		expected.get(0).setsEpicName("Update Epic Name");
		doReturn(new ResponseEntity<>(getExpectedJSON("response/epicresponse.json"), HttpStatus.OK)).when(rest)
			.exchange(contains("/rest/agile/1.0/issue/" + epicId), eq(HttpMethod.GET), any(HttpEntity.class),
					eq(String.class));
		doReturn(new ResponseEntity<>(getExpectedJSON("response/issueresponse-combo-update.json"), HttpStatus.OK))
			.when(rest)
			.exchange(contains("board/999"), eq(HttpMethod.GET), any(HttpEntity.class), eq(String.class));
		when(featureBoardRepository.findFeatureBoard(new ObjectId(), "123455")).thenReturn(null);
		List<JiraBoard> jiraBoard = getExpectedReviewResponse("./expected/boardasteam-expected.json");
		// Todo :check s3 upload
		featureCollectorTask.updateStoryInformation(jiraBoard);
		List<Feature> actual = featureRepository.getStoryByTeamID(expected.get(0).getsTeamID());
		assertThat(actual.size()).isEqualTo(0);
	}

	@Test
	public void updateEpicBeginDate() throws IOException {

		List<DesFeature> expected = getExpectedFeature("./expected/feature-epic-expected.json");
		String epicId = expected.get(0).getsEpicID();
		doReturn(new ResponseEntity<>(getExpectedJSON("response/epicresponse.json"), HttpStatus.OK)).when(rest)
			.exchange(contains("/rest/agile/1.0/issue/" + epicId), eq(HttpMethod.GET), any(HttpEntity.class),
					eq(String.class));
		doReturn(new ResponseEntity<>(getExpectedJSON("response/issueresponse-combo-update-1.json"), HttpStatus.OK))
			.when(rest)
			.exchange(contains("board/999"), eq(HttpMethod.GET), any(HttpEntity.class), eq(String.class));
		when(featureBoardRepository.findFeatureBoard(new ObjectId(), "123455")).thenReturn(null);
		List<JiraBoard> jiraBoard = getExpectedReviewResponse("./expected/boardasteam-expected.json");
		// Todo :check s3 upload
		featureCollectorTask.updateStoryInformation(jiraBoard);

		List<Feature> actual = featureRepository.getStoryByTeamID(expected.get(0).getsTeamID());
		expected.get(0).setSprintJourney(null);
		assertThat(actual.size()).isEqualTo(0);
	}

	private String getExpectedJSON(String fileName) throws IOException {
		String path = "./" + fileName;
		URL fileUrl = Resources.getResource(path);
		return IOUtils.toString(fileUrl);
	}

	private List<JiraBoard> getExpectedReviewResponse(String fileName) throws IOException {
		Gson gson = GsonUtil.getGson();
		return gson.fromJson(getExpectedJSON(fileName), new TypeToken<List<JiraBoard>>() {
		}.getType());
	}

	private List<DesFeature> getExpectedFeature(String fileName) throws IOException {
		Gson gson = GsonUtil.getGson();
		return gson.fromJson(getExpectedJSON(fileName), new TypeToken<List<DesFeature>>() {
		}.getType());
	}

}
