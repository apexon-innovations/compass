package com.apexon.compass.sonarservice.controller;

import com.apexon.compass.exception.custom.RecordNotFoundException;
import com.apexon.compass.exception.custom.UnauthorizedException;
import com.apexon.compass.exception.handler.ApiExceptionHandler;
import com.apexon.compass.sonarservice.UnitTestHelper;
import com.apexon.compass.sonarservice.dto.SprintPullRequestSummaryDto;
import com.apexon.compass.sonarservice.dto.SprintSubmitterMetricsResponseDto;
import com.apexon.compass.sonarservice.service.StrategicService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static com.apexon.compass.constants.StrategyServiceConstants.*;
import static com.apexon.compass.constants.StrategyServiceRouteConstants.*;
import static com.apexon.compass.constants.StrategyServiceRouteConstants.SPRINT_PULL_REQUEST_SUMMARY;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebAppConfiguration
class StrategicControllerTestCase {

    private static final String CONTROLLER_URL = "/project";

    private static final String PROJECT_ID = "5e7dbe36215e091c132c8f88";

    private static final String AUTHORIZATION = "Authorization";

    private static final String DUMMY_TOKEN = "token";

    private static final String REPO_ID = "F6edsBx7";

    private static final String SORT = "ASC";

    private static final String DAY_COUNT = "2";

    protected MockMvc mockMvc;

    @MockBean
    private StrategicService strategicService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(new StrategicController(strategicService))
            .setControllerAdvice(new ApiExceptionHandler())
            .build();
    }

    @Test
    void testViolationsSummary() throws Exception {
        // ARRANGE
        String url = CONTROLLER_URL + VIOLATIONS_SUMMARY;
        Mockito.when(strategicService.getViolationsSummary(PROJECT_ID, REPO_ID))
            .thenReturn(UnitTestHelper.getViolationsSummaryDto());
        // Act
        MockHttpServletRequestBuilder requestBuilder = get(url).param("projectId", PROJECT_ID)
            .param("repoIds", REPO_ID)
            .header(AUTHORIZATION, DUMMY_TOKEN)
            .accept(MediaType.APPLICATION_JSON)
            .contentType(MediaType.APPLICATION_JSON);
        // Assert
        mockMvc.perform(requestBuilder)
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON));
        verify(strategicService).getViolationsSummary(PROJECT_ID, REPO_ID);
    }

    @Test
    void testCollaborationMetrics() throws Exception {
        // ARRANGE
        String url = CONTROLLER_URL + COLLABORATION_METRICS;
        Mockito.when(strategicService.getCollaborationMetrics(PROJECT_ID, REPO_ID, DAY_COUNT))
            .thenReturn(UnitTestHelper.getCollaborationMetricDataDto());
        // Act
        MockHttpServletRequestBuilder requestBuilder = get(url).param("projectId", PROJECT_ID)
            .param("repoIds", REPO_ID)
            .param("dayCount", DAY_COUNT)
            .header(AUTHORIZATION, DUMMY_TOKEN)
            .accept(MediaType.APPLICATION_JSON)
            .contentType(MediaType.APPLICATION_JSON);
        // Assert
        mockMvc.perform(requestBuilder)
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON));
        verify(strategicService).getCollaborationMetrics(PROJECT_ID, REPO_ID, DAY_COUNT);
    }

    @Test
    void getReviewerMetricsTest() throws Exception {
        String url = CONTROLLER_URL + SPRINT_REVIEWER_METRICS;
        Mockito.when(strategicService.getReviewerMetrics(PROJECT_ID, REPO_ID, DAY_COUNT))
            .thenReturn(UnitTestHelper.getReviewerMetricsDto());
        MockHttpServletRequestBuilder requestBuilder = get(url).param("projectId", PROJECT_ID)
            .param("repoIds", REPO_ID)
            .param("dayCount", DAY_COUNT)
            .header(AUTHORIZATION, DUMMY_TOKEN)
            .accept(MediaType.APPLICATION_JSON)
            .contentType(MediaType.APPLICATION_JSON);
        mockMvc.perform(requestBuilder)
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON));
        verify(strategicService).getReviewerMetrics(PROJECT_ID, REPO_ID, DAY_COUNT);
    }

    @Test
    void testPrJourney() throws Exception {
        String url = CONTROLLER_URL + PR_JOURNEY;
        Mockito.when(strategicService.getPrJourney(PROJECT_ID, REPO_ID, DAY_COUNT))
            .thenReturn(UnitTestHelper.getPullRequestsDto());
        MockHttpServletRequestBuilder requestBuilder = get(url).param("projectId", PROJECT_ID)
            .param("repoIds", REPO_ID)
            .param("dayCount", DAY_COUNT)
            .header(AUTHORIZATION, DUMMY_TOKEN)
            .accept(MediaType.APPLICATION_JSON)
            .contentType(MediaType.APPLICATION_JSON);
        mockMvc.perform(requestBuilder)
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON));
        verify(strategicService).getPrJourney(PROJECT_ID, REPO_ID, DAY_COUNT);
    }

    @Test
    void testCodeMetrics() throws Exception {
        String url = CONTROLLER_URL + CODE_METRICS;
        Mockito.when(strategicService.getCodeMetrics(PROJECT_ID, REPO_ID, DAY_COUNT))
            .thenReturn(UnitTestHelper.getCodeMetricsDto());
        MockHttpServletRequestBuilder requestBuilder = get(url).param("projectId", PROJECT_ID)
            .param("repoIds", REPO_ID)
            .param("dayCount", DAY_COUNT)
            .header(AUTHORIZATION, DUMMY_TOKEN)
            .accept(MediaType.APPLICATION_JSON)
            .contentType(MediaType.APPLICATION_JSON);
        mockMvc.perform(requestBuilder)
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON));
        verify(strategicService).getCodeMetrics(PROJECT_ID, REPO_ID, DAY_COUNT);
    }

    @Test
    void GetTechnicalDebtTest() throws Exception {
        String url = CONTROLLER_URL + TECHNICAL_DEBT_METRICS;
        Mockito.when(strategicService.getTechnicalDebtMetrics(PROJECT_ID, REPO_ID))
            .thenReturn(UnitTestHelper.getTechnicalDebtDto());
        MockHttpServletRequestBuilder requestBuilder = get(url).param("projectId", PROJECT_ID)
            .param("repoIds", REPO_ID)
            .header(AUTHORIZATION, DUMMY_TOKEN)
            .accept(MediaType.APPLICATION_JSON)
            .contentType(MediaType.APPLICATION_JSON);
        mockMvc.perform(requestBuilder)
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON));
        verify(strategicService).getTechnicalDebtMetrics(PROJECT_ID, REPO_ID);
    }

    @Test
    void getComplianceAnalysisTestCase() throws Exception {
        // ARRANGE
        String url = CONTROLLER_URL + GET_COMPLIANCE_ANALYSIS;
        Mockito.when(strategicService.getComplianceAnalysis(PROJECT_ID, REPO_ID))
            .thenReturn(UnitTestHelper.getComplianceAnalysisDto());
        // ACT
        MockHttpServletRequestBuilder requestBuilder = get(url).param("projectId", PROJECT_ID)
            .param("repoIds", REPO_ID)
            .header(AUTHORIZATION, DUMMY_TOKEN)
            .accept(MediaType.APPLICATION_JSON)
            .contentType(MediaType.APPLICATION_JSON);
        // Assert
        mockMvc.perform(requestBuilder)
            .andExpect(status().isOk())
            .andExpect(content().contentType(org.springframework.http.MediaType.APPLICATION_JSON));
        verify(strategicService).getComplianceAnalysis(PROJECT_ID, REPO_ID);
    }

    @Test
    void getSprintPullRequestSummaryTest() throws Exception {
        String url = CONTROLLER_URL + SPRINT_PULL_REQUEST_SUMMARY;
        Mockito.when(strategicService.getSprintPullRequestSummary(PROJECT_ID, REPO_ID, DAY_COUNT))
            .thenReturn(SprintPullRequestSummaryDto.builder()
                .data(UnitTestHelper.getSprintPullRequestSummaryDataDto())
                .build());
        // ACT
        MockHttpServletRequestBuilder requestBuilder = get(url).param("projectId", PROJECT_ID)
            .param("repoIds", REPO_ID)
            .param("dayCount", DAY_COUNT)
            .header(AUTHORIZATION, DUMMY_TOKEN)
            .accept(MediaType.APPLICATION_JSON)
            .contentType(MediaType.APPLICATION_JSON);
        // Assert
        mockMvc.perform(requestBuilder)
            .andExpect(status().isOk())
            .andExpect(content().contentType(org.springframework.http.MediaType.APPLICATION_JSON));
        verify(strategicService).getSprintPullRequestSummary(PROJECT_ID, REPO_ID, DAY_COUNT);
    }

    @Test
    void getSprintPullRequestSummaryTestWithUnauthorizedException() throws Exception {
        String url = CONTROLLER_URL + SPRINT_PULL_REQUEST_SUMMARY;
        Mockito.when(strategicService.getSprintPullRequestSummary(PROJECT_ID, REPO_ID, DAY_COUNT))
            .thenThrow(new UnauthorizedException("Invalid claim exception"));
        mockMvc
            .perform(get(url).param("projectId", PROJECT_ID)
                .param("repoIds", REPO_ID)
                .param("dayCount", DAY_COUNT)
                .header(AUTHORIZATION, DUMMY_TOKEN)
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isUnauthorized())
            .andReturn();
    }

    @Test
    void getSprintPullRequestSummaryTestWithRecordNotFoundException() throws Exception {
        String url = CONTROLLER_URL + "%s/repo/%s/pullrequests/summary";
        Mockito.when(strategicService.getSprintPullRequestSummary(PROJECT_ID, REPO_ID, DAY_COUNT))
            .thenThrow(new RecordNotFoundException(RECORD_NOT_FOUND));
        mockMvc.perform(get(url).header(AUTHORIZATION, DUMMY_TOKEN).contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isNotFound())
            .andReturn();
    }

    @Test
    void getSprintSubmitterMetricsTest() throws Exception {
        Mockito.when(strategicService.getSprintSubmitterMetrics(PROJECT_ID, SORT, REPO_ID, DAY_COUNT))
            .thenReturn(SprintSubmitterMetricsResponseDto.builder()
                .data(UnitTestHelper.getSprintSubmitterMetricsDto())
                .build());
        MvcResult result = mockMvc
            .perform(get(CONTROLLER_URL.concat(SPRINT_SUBMITTER_METRICS)).param("projectId", PROJECT_ID)
                .param("sortBy", SORT)
                .param("repoIds", REPO_ID)
                .param("dayCount", DAY_COUNT)
                .header(AUTHORIZATION, DUMMY_TOKEN)
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andReturn();
        assertEquals(objectMapper.writeValueAsString(SprintSubmitterMetricsResponseDto.builder()
            .data(UnitTestHelper.getSprintSubmitterMetricsDto())
            .build()), result.getResponse().getContentAsString());
    }

    @Test
    void getSprintSubmitterMetricsTestWithUnauthorizedException() throws Exception {
        Mockito.when(strategicService.getSprintSubmitterMetrics(PROJECT_ID, SORT, REPO_ID, DAY_COUNT))
            .thenThrow(new UnauthorizedException("Invalid claim exception"));
        mockMvc
            .perform(get(CONTROLLER_URL.concat(SPRINT_SUBMITTER_METRICS)).param("projectId", PROJECT_ID)
                .param("sortBy", SORT)
                .param("repoIds", REPO_ID)
                .param("dayCount", DAY_COUNT)
                .header(AUTHORIZATION, DUMMY_TOKEN)
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isUnauthorized())
            .andReturn();
    }

    @Test
    void getSprintSubmitterMetricsTestWithRecordNotFoundException() throws Exception {
        String url = CONTROLLER_URL + "%s/repo/%s/submitter/metrics";
        Mockito.when(strategicService.getSprintSubmitterMetrics(PROJECT_ID, SORT, REPO_ID, DAY_COUNT))
            .thenThrow(new RecordNotFoundException(RECORD_NOT_FOUND));
        mockMvc
            .perform(get(url).param("projectId", PROJECT_ID)
                .param("repoIds", REPO_ID)
                .header(AUTHORIZATION, DUMMY_TOKEN)
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isNotFound())
            .andReturn();
    }

    @Test
    void getQualityMetricsTest() throws Exception {
        String url = CONTROLLER_URL + GET_QUALITY_SUMMARY;
        when(strategicService.getQualityMetrics(PROJECT_ID, REPO_ID))
            .thenReturn(UnitTestHelper.getQualityMetricsSummaryDto());
        MvcResult result = mockMvc
            .perform(get(url).param("projectId", PROJECT_ID)
                .param("repoIds", REPO_ID)
                .header(AUTHORIZATION, DUMMY_TOKEN)
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andReturn();
        assertEquals(objectMapper.writeValueAsString(UnitTestHelper.getQualityMetricsSummaryDto()),
                result.getResponse().getContentAsString());
    }

  @Test
  void getQualityMetricsTestWithUnauthorizedException() throws Exception {
    when(strategicService.getQualityMetrics(PROJECT_ID, REPO_ID))
        .thenThrow(new UnauthorizedException("Invalid claim exception"));
    mockMvc
        .perform(
            get(CONTROLLER_URL.concat(GET_QUALITY_SUMMARY))
                .param("projectId", PROJECT_ID)
                .param("repoIds", REPO_ID)
                .header(AUTHORIZATION, DUMMY_TOKEN)
                .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isUnauthorized())
        .andReturn();
  }

  @Test
  void getQualityMetricsTestWithRecordNotFoundException() throws Exception {
    when(strategicService.getQualityMetrics(PROJECT_ID, REPO_ID))
        .thenThrow(new RecordNotFoundException(RECORD_NOT_FOUND));
    mockMvc
        .perform(
            get(CONTROLLER_URL.concat(GET_QUALITY_SUMMARY))
                .param("projectId", PROJECT_ID)
                .param("repoIds", REPO_ID)
                .header(AUTHORIZATION, DUMMY_TOKEN)
                .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isNotFound())
        .andReturn();
  }

    @Test
    void testActivityMetrics() throws Exception {
        Mockito.when(strategicService.getMemberWiseActivityMetrics(PROJECT_ID, REPO_ID, DAY_COUNT))
            .thenReturn(UnitTestHelper.getActivityMetricsDto());
        String url = CONTROLLER_URL + MEMBERWISE_ACTIVITY_METRICS;
        String formattedUrl = String.format(url, PROJECT_ID, REPO_ID);
        // ACT
        MockHttpServletRequestBuilder requestBuilder = get(formattedUrl).param("projectId", PROJECT_ID)
            .param("repoIds", REPO_ID)
            .param("dayCount", DAY_COUNT)
            .header(AUTHORIZATION, DUMMY_TOKEN)
            .accept(MediaType.APPLICATION_JSON)
            .contentType(MediaType.APPLICATION_JSON);
        // Assert
        mockMvc.perform(requestBuilder)
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON));
        verify(strategicService).getMemberWiseActivityMetrics(PROJECT_ID, REPO_ID, DAY_COUNT);
    }

}
