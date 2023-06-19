package com.apexon.compass.psrservice.controller;

import com.apexon.compass.exception.custom.ForbiddenException;
import com.apexon.compass.exception.custom.RecordNotFoundException;
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

import static com.apexon.compass.constants.PsrServiceRouteConstants.*;
import static com.apexon.compass.constants.StrategyServiceConstants.RECORD_NOT_FOUND;
import static com.apexon.compass.psrservice.UnitTestHelper.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class SprintControllerTest {

    private static final String CONTROLLER_URL = "/project/";

    private static final String ISC_PROJECT_ID = "5e7dbe36215e091c132c8f88";

    private static final String MEMBER_ID = "2345";

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
  void getMemberStatusTest() throws Exception {
    when(jiraService.getMemberStatus(Mockito.anyString(), Mockito.anyInt()))
        .thenReturn(getSprintMemberStatusDto());
    MvcResult result =
        mockMvc
            .perform(
                get(CONTROLLER_URL.concat(SPRINT_MEMBER_STATUS), ISC_PROJECT_ID)
                    .header(AUTHORIZATION, DUMMY_TOKEN)
                    .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andReturn();
    assertEquals(
        objectMapper.writeValueAsString(getSprintMemberStatusDto()),
        result.getResponse().getContentAsString());
  }

    @Test
  void getMemberStatusTestWithUnauthorizedException() throws Exception {
    when(jiraService.getMemberStatus(Mockito.anyString(), Mockito.anyInt()))
        .thenThrow(new ForbiddenException("Access denied"));
    mockMvc
        .perform(
            get(CONTROLLER_URL.concat(SPRINT_MEMBER_STATUS), ISC_PROJECT_ID)
                .header(AUTHORIZATION, DUMMY_TOKEN)
                .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isForbidden())
        .andReturn();
  }

    @Test
  void getMemberStatusTestWithRecordNotFoundException() throws Exception {
    when(jiraService.getMemberStatus(Mockito.anyString(), Mockito.anyInt()))
        .thenThrow(new RecordNotFoundException(RECORD_NOT_FOUND));
    mockMvc
        .perform(
            get(CONTROLLER_URL.concat(SPRINT_MEMBER_STATUS), ISC_PROJECT_ID)
                .header(AUTHORIZATION, DUMMY_TOKEN)
                .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isNotFound())
        .andReturn();
  }

    @Test
  void getMemberStatusByIdTest() throws Exception {
    when(jiraService.getMemberStatusById(
            Mockito.anyString(), Mockito.anyString(), Mockito.anyInt()))
        .thenReturn(getSprintMemberStatusByIdDto());

    MvcResult result =
        mockMvc
            .perform(
                get(
                        CONTROLLER_URL.concat(SPRINT_MEMBER_STATUS_BY_MEMBERID),
                        ISC_PROJECT_ID,
                        MEMBER_ID)
                    .header(AUTHORIZATION, DUMMY_TOKEN)
                    .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andReturn();
    assertEquals(
        objectMapper.writeValueAsString(getSprintMemberStatusByIdDto()),
        result.getResponse().getContentAsString());
  }

    @Test
  void getMemberStatusByIdTestWithUnauthorizedException() throws Exception {
    when(jiraService.getMemberStatusById(
            Mockito.anyString(), Mockito.anyString(), Mockito.anyInt()))
        .thenThrow(new ForbiddenException("Access denied"));
    mockMvc
        .perform(
            get(CONTROLLER_URL.concat(SPRINT_MEMBER_STATUS_BY_MEMBERID), ISC_PROJECT_ID, MEMBER_ID)
                .header(AUTHORIZATION, DUMMY_TOKEN)
                .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isForbidden())
        .andReturn();
  }

    @Test
  void getMemberStatusByIdTestWithRecordNotFoundException() throws Exception {
    when(jiraService.getMemberStatusById(
            Mockito.anyString(), Mockito.anyString(), Mockito.anyInt()))
        .thenThrow(new RecordNotFoundException(RECORD_NOT_FOUND));
    mockMvc
        .perform(
            get(CONTROLLER_URL.concat(SPRINT_MEMBER_STATUS_BY_MEMBERID), ISC_PROJECT_ID, MEMBER_ID)
                .header(AUTHORIZATION, DUMMY_TOKEN)
                .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isNotFound())
        .andReturn();
  }

    @Test
  void getMemberDetailsByIdTest() throws Exception {
    when(jiraService.getMemberDetailsById(
            Mockito.anyString(), Mockito.anyString()))
        .thenReturn(getSprintMemberDetailsByIdDto());

    MvcResult result =
        mockMvc
            .perform(
                get(
                        CONTROLLER_URL.concat(SPRINT_MEMBER_DETAILS_BY_MEMBERID),
                        ISC_PROJECT_ID,
                        MEMBER_ID)
                    .header(AUTHORIZATION, DUMMY_TOKEN)
                    .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andReturn();
    assertEquals(
        objectMapper.writeValueAsString(getSprintMemberDetailsByIdDto()),
        result.getResponse().getContentAsString());
  }

    @Test
  void getMemberDetailsByIdTestWithUnauthorizedException() throws Exception {
    when(jiraService.getMemberDetailsById(
            Mockito.anyString(), Mockito.anyString()))
        .thenThrow(new ForbiddenException("Access denied"));
    mockMvc
        .perform(
            get(CONTROLLER_URL.concat(SPRINT_MEMBER_DETAILS_BY_MEMBERID), ISC_PROJECT_ID, MEMBER_ID)
                .header(AUTHORIZATION, DUMMY_TOKEN)
                .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isForbidden())
        .andReturn();
  }

    @Test
  void getMemberDetailsByIdTestWithRecordNotFoundException() throws Exception {
    when(jiraService.getMemberDetailsById(
            Mockito.anyString(), Mockito.anyString()))
        .thenThrow(new RecordNotFoundException("No record available for member"));
    mockMvc
        .perform(
            get(CONTROLLER_URL.concat(SPRINT_MEMBER_DETAILS_BY_MEMBERID), ISC_PROJECT_ID, MEMBER_ID)
                .header(AUTHORIZATION, DUMMY_TOKEN)
                .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isNotFound())
        .andReturn();
  }

}
