package com.apexon.compass.psrservice.controller;

import com.apexon.compass.exception.custom.BadRequestException;
import com.apexon.compass.exception.custom.RecordNotFoundException;
import com.apexon.compass.exception.custom.UnauthorizedException;
import com.apexon.compass.psrservice.UnitTestHelper;
import com.apexon.compass.psrservice.dao.SprintDao;
import com.apexon.compass.psrservice.service.IscService;
import com.apexon.compass.psrservice.service.JiraService;
import com.apexon.compass.psrservice.service.ProjectService;
import com.apexon.compass.psrservice.service.StoriesService;
import com.apexon.compass.psrservice.service.impl.helper.JiraRulesHelper;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static com.apexon.compass.constants.PsrServiceRouteConstants.NPS_DATAPARSING;
import static com.apexon.compass.constants.StrategyServiceConstants.RECORD_NOT_FOUND;
import static com.apexon.compass.psrservice.UnitTestHelper.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class IscControllerTest {

    private static final String CONTROLLER_URL = "/project/";

    private static final String ISC_PROJECT_ID = "5e7dbe36215e091c132c8f88";

    private static final String AUTHORIZATION = "Authorization";

    private static final String DUMMY_TOKEN = "token";

    @Autowired
    protected MockMvc mockMvc;

    @MockBean
    private IscService iscService;

    @MockBean
    private ProjectService projectService;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private JiraService jiraService;

    @MockBean
    private StoriesService storiesService;

    @MockBean
    private SprintDao sprintDao;

    @MockBean
    private JiraRulesHelper jiraRulesHelper;

    @Test
  void storeNpsDataTest() throws Exception {
    when(iscService.storeNpsData(Mockito.anyString(), Mockito.any()))
        .thenReturn(getNpsDataResponseDto());

    MvcResult result =
        mockMvc
            .perform(
                post(CONTROLLER_URL.concat(NPS_DATAPARSING), ISC_PROJECT_ID)
                    .header(AUTHORIZATION, DUMMY_TOKEN)
                    .content(
                        objectMapper
                            .writer()
                            .withDefaultPrettyPrinter()
                            .writeValueAsString(getNpsDataRequestDto()))
                    .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isCreated())
            .andReturn();

    assertEquals(
        objectMapper.writeValueAsString(getNpsDataResponseDto()),
        result.getResponse().getContentAsString());
  }

    @Test
  void storeNpsDataTestWithBadRequestException() throws Exception {
    when(iscService.storeNpsData(Mockito.anyString(), Mockito.any()))
        .thenThrow(new BadRequestException("Invalid SubmissionDate"));

    mockMvc
        .perform(
            post(CONTROLLER_URL.concat(NPS_DATAPARSING), ISC_PROJECT_ID)
                .header(AUTHORIZATION, DUMMY_TOKEN)
                .content(
                    objectMapper
                        .writer()
                        .withDefaultPrettyPrinter()
                        .writeValueAsString(getNpsDataRequestDto()))
                .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isBadRequest())
        .andReturn();
  }

    @Test
    void storeNpsDataTestWithInvalidRequestException() throws Exception {
        mockMvc.perform(post(CONTROLLER_URL.concat(NPS_DATAPARSING), ISC_PROJECT_ID).header(AUTHORIZATION, DUMMY_TOKEN)
            .content(objectMapper.writer().withDefaultPrettyPrinter().writeValueAsString(getInvalidNpsDataRequestDto()))
            .contentType(MediaType.APPLICATION_JSON)).andExpect(status().isBadRequest()).andReturn();
    }

    @Test
    void storeNpsDataTestWithBlankRequestParamException() throws Exception {
        mockMvc
            .perform(post(CONTROLLER_URL.concat(NPS_DATAPARSING), ISC_PROJECT_ID).header(AUTHORIZATION, DUMMY_TOKEN)
                .content(objectMapper.writer()
                    .withDefaultPrettyPrinter()
                    .writeValueAsString(UnitTestHelper.getInvalidNpsDataRequestDtoWithBlankSubmissionPeriodParam()))
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isBadRequest())
            .andReturn();
    }

    @Test
    void storeNpsDataTestWithBlankRequestCustomerRoleParamException() throws Exception {
        mockMvc
            .perform(post(CONTROLLER_URL.concat(NPS_DATAPARSING), ISC_PROJECT_ID).header(AUTHORIZATION, DUMMY_TOKEN)
                .content(objectMapper.writer()
                    .withDefaultPrettyPrinter()
                    .writeValueAsString(UnitTestHelper.getInvalidNpsDataRequestDtoWithBlankCustomerRoleParam()))
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isBadRequest())
            .andReturn();
    }

    @Test
    void storeNpsDataTestWithBlankSurveyRatingParamException() throws Exception {
        mockMvc
            .perform(post(CONTROLLER_URL.concat(NPS_DATAPARSING), ISC_PROJECT_ID).header(AUTHORIZATION, DUMMY_TOKEN)
                .content(objectMapper.writer()
                    .withDefaultPrettyPrinter()
                    .writeValueAsString(UnitTestHelper.getInvalidNpsDataRequestDtoWithBlankSurveyRatingParam()))
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isBadRequest())
            .andReturn();
    }

    @Test
  void getNpsReportTest() throws Exception {
    when(iscService.getNpsReport(Mockito.anyString(), Mockito.anyString()))
        .thenReturn(getNpsReportsDto());
    MvcResult result =
        mockMvc
            .perform(
                get(CONTROLLER_URL.concat(NPS_DATAPARSING), ISC_PROJECT_ID)
                    .header(AUTHORIZATION, DUMMY_TOKEN)
                    .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andReturn();
    assertEquals(
        objectMapper.writeValueAsString(getNpsReportsDto()),
        result.getResponse().getContentAsString());
  }

    @Test
  void getNpsReportTestWithUnauthorizedException() throws Exception {
    when(iscService.getNpsReport(Mockito.anyString(), Mockito.anyString()))
        .thenThrow(new UnauthorizedException("Invalid claim exception"));
    mockMvc
        .perform(
            get(CONTROLLER_URL.concat(NPS_DATAPARSING), ISC_PROJECT_ID)
                .header(AUTHORIZATION, DUMMY_TOKEN)
                .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isUnauthorized())
        .andReturn();
  }

    @Test
  void getNpsReportTestWithRecordNotFoundException() throws Exception {
    when(iscService.getNpsReport(Mockito.anyString(), Mockito.anyString()))
        .thenThrow(new RecordNotFoundException(RECORD_NOT_FOUND));
    mockMvc
        .perform(
            get(CONTROLLER_URL.concat(NPS_DATAPARSING), ISC_PROJECT_ID)
                .header(AUTHORIZATION, DUMMY_TOKEN)
                .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isNotFound())
        .andReturn();
  }

}
