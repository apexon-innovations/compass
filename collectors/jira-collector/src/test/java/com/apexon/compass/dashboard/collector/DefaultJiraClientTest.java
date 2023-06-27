package com.apexon.compass.dashboard.collector;

import com.apexon.compass.dashboard.client.RestClient;
import com.apexon.compass.dashboard.client.RestOperationsSupplier;
import com.apexon.compass.dashboard.config.TestConfig;
import com.apexon.compass.dashboard.model.*;
import com.google.common.io.Resources;
import org.apache.commons.io.IOUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
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
import java.text.ParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { TestConfig.class })
@DirtiesContext
public class DefaultJiraClientTest {

	@Mock
	private DefaultJiraClient defaultJiraClient;

	@SuppressWarnings("unchecked")
	@Mock
	private RestOperationsSupplier restOperationsSupplier = mock(RestOperationsSupplier.class);

	@Mock
	private RestOperations rest = mock(RestOperations.class);

	@Autowired
	private FeatureSettings featureSettings;

	@Autowired
	private DesJiraConfiguration desJiraConfiguration;

	private JiraConfiguration jiraConfiguration;

	@Before
  public void loadStuff() {
    when(restOperationsSupplier.get()).thenReturn(rest);
    defaultJiraClient = new DefaultJiraClient(new RestClient(restOperationsSupplier));
    jiraConfiguration = desJiraConfiguration.getJiraConfigurations().get(0);
  }

	@Test
	public void getJiraIssueTypeIds() throws IOException {
		doReturn(new ResponseEntity<>(getExpectedJSON("response/issuetype.json"), HttpStatus.OK)).when(rest)
			.exchange(contains("rest/api/2/issuetype"), eq(HttpMethod.GET), any(HttpEntity.class), eq(String.class));
		Map<String, String> issueTypeIds = defaultJiraClient.getJiraIssueTypeIds(jiraConfiguration);
		featureSettings.setJiraIssueTypeNames(new String[] { "Story" });
		defaultJiraClient = new DefaultJiraClient(new RestClient(restOperationsSupplier));
		assertEquals(issueTypeIds.containsKey("Epic"), true);
	}

	@Test
	public void getIssuesBoard() throws IOException {
		JiraBoard board = new JiraBoard();
		board.setBoardId("123");
		board.setBoardName("testTeam");
		doReturn(new ResponseEntity<>(getExpectedJSON("response/epicresponse.json"), HttpStatus.OK)).when(rest)
			.exchange(contains("rest/agile/1.0/issue/"), eq(HttpMethod.GET), any(HttpEntity.class), eq(String.class));

		doReturn(new ResponseEntity<>(getExpectedJSON("response/issueresponse-combo.json"), HttpStatus.OK)).when(rest)
			.exchange(contains("rest/agile/1.0/board/" + board.getBoardId()), eq(HttpMethod.GET), any(HttpEntity.class),
					eq(String.class));
		Map<String, String> issueTypeIds = new HashMap<>();
		issueTypeIds.put("Epic", "6");
		issueTypeIds.put("Story", "7");
		FeatureEpicResult featureEpicResult = defaultJiraClient.getIssues(board, issueTypeIds, jiraConfiguration);
		assertThat(featureEpicResult.getEpicList().size()).isEqualTo(1);
		assertThat(featureEpicResult.getFeatureList().size()).isEqualTo(1);
	}

	@Test
	public void getProjectsWithAuth() throws IOException {
		doReturn(new ResponseEntity<>(getExpectedJSON("response/projectresponse.json"), HttpStatus.OK)).when(rest)
			.exchange(contains("api/2/project"), eq(HttpMethod.GET), any(HttpEntity.class), eq(String.class));
		featureSettings.setJiraCredentials("dXNlcm5hbWU6cGFzc3dvcmQ=");
		defaultJiraClient = new DefaultJiraClient(new RestClient(restOperationsSupplier));
		Set<Scope> projects = defaultJiraClient.getProjects(jiraConfiguration);
		assertThat(projects.stream().count()).isEqualTo(1);
	}

	@Test
	public void getProjects() throws IOException {
		doReturn(new ResponseEntity<>(getExpectedJSON("response/projectresponse.json"), HttpStatus.OK)).when(rest)
			.exchange(contains("api/2/project"), eq(HttpMethod.GET), any(HttpEntity.class), eq(String.class));
		Set<Scope> projects = defaultJiraClient.getProjects(jiraConfiguration);
		assertThat(projects.stream().count()).isEqualTo(1);
	}

	@Test
	public void getProjectsParseException() {
		doReturn(new ResponseEntity<>("{}", HttpStatus.OK)).when(rest)
			.exchange(contains("api/2/project"), eq(HttpMethod.GET), any(HttpEntity.class), eq(String.class));
		ParseException excep = new ParseException(
				"org.json.simple.JSONObject cannot be cast to org.json.simple.JSONArray", 0);

		try {
			defaultJiraClient.getProjects(jiraConfiguration);
			fail("Should throw ParseException");
		}
		catch (Exception e) {
			assertEquals(excep.getMessage(), e.getMessage());
		}
	}

	@Test
	public void getBoards() throws IOException {
		doReturn(new ResponseEntity<>(getExpectedJSON("response/boardsresponse.json"), HttpStatus.OK)).when(rest)
			.exchange(contains("/rest/agile/1.0/board"), eq(HttpMethod.GET), any(HttpEntity.class), eq(String.class));
		List<JiraBoard> projects = defaultJiraClient.getBoards(jiraConfiguration);
		assertThat(projects.stream().count()).isEqualTo(1);
	}

	@Test
	public void getAllIssueIds() throws IOException {
		doReturn(new ResponseEntity<>(getExpectedJSON("response/issuerefreshresponse.json"), HttpStatus.OK)).when(rest)
			.exchange(contains("rest/agile/1.0/"), eq(HttpMethod.GET), any(HttpEntity.class), eq(String.class));
		List<String> ids = defaultJiraClient.getAllIssueIds("1234", JiraMode.Board, jiraConfiguration);
		assertThat(ids.stream().count()).isEqualTo(7);
	}

	private String getExpectedJSON(String fileName) throws IOException {
		String path = "./" + fileName;
		URL fileUrl = Resources.getResource(path);
		return IOUtils.toString(fileUrl);
	}

}
