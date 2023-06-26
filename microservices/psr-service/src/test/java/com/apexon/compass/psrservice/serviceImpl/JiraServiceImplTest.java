package com.apexon.compass.psrservice.serviceImpl;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import com.apexon.compass.entities.*;
import com.apexon.compass.psrservice.dto.SayDoDto;
import com.apexon.compass.psrservice.dto.SayDoListDto;
import com.apexon.compass.psrservice.service.impl.JiraServiceImpl;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.data.domain.Sort;

import com.apexon.compass.aggregation.vo.NestProjectsIdWiseProjectMemberLeaveVo;
import com.apexon.compass.entities.Project;
import com.apexon.compass.exception.custom.RecordNotFoundException;
import com.apexon.compass.psrservice.UnitTestHelper;
import com.apexon.compass.psrservice.dao.ProjectMemberLeavesDao;
import com.apexon.compass.psrservice.dao.PublicHolidaysDao;
import com.apexon.compass.psrservice.repository.ProjectRepository;
import com.apexon.compass.psrservice.repository.JiraConfigurationRepository;
import com.apexon.compass.psrservice.repository.ProjectMembersRepository;
import com.apexon.compass.psrservice.repository.SprintChartRepository;
import com.apexon.compass.psrservice.repository.SprintsRepository;
import com.apexon.compass.psrservice.service.impl.helper.ProjectHelper;

@AutoConfigureMockMvc
@ExtendWith(MockitoExtension.class)
class JiraServiceImplTest {

    @Mock
    SprintChartRepository sprintChartRepository;

    @Mock
    ProjectHelper projectHelper;

    @Mock
    JiraConfigurationRepository jiraConfigurationRepository;

    @Mock
    ProjectRepository projectRepository;

    @Mock
    ProjectMembersRepository projectMembersRepository;

    @Mock
    PublicHolidaysDao publicHolidaysDao;

    @Mock
    SprintsRepository sprintsRepository;

    @Mock
    ProjectMemberLeavesDao projectMemberLeavesDao;

    @InjectMocks
    JiraServiceImpl jiraService;

    private static final String ISC_PROJECT_ID = "5e7dbe36215e091c132c8f88";

    private static final String JIRAPROJECT_ID = "10003";

    private static final Integer BOARD_ID = 179;

    private static final String SPRINT_NAME = "IA Sprint 9";

    private static final String DUMMY_TOKEN = "token";

    private static final String SPRINT_ID = "924";

    @Test
  void getSayDoRatioTest() {
    when(projectHelper.findAndValidateProjectByIdAndUser(ISC_PROJECT_ID))
        .thenReturn(UnitTestHelper.getDummyIscProject());
    when(jiraConfigurationRepository.findByProjectId(Mockito.any(ObjectId.class)))
        .thenReturn(
            Optional.of(
                JiraConfiguration.builder()
                    .projectId(new ObjectId(ISC_PROJECT_ID))
                    .jiraProjectId(JIRAPROJECT_ID)
                    .build()));
    when(sprintChartRepository.findByJiraProjectId(Mockito.anyInt(), Mockito.any(Sort.class)))
        .thenReturn(UnitTestHelper.getDummysprintCharts());
    assertEquals(
        SayDoListDto.builder()
            .sprints(
                Arrays.asList(
                    SayDoDto.builder()
                        .id(11)
                        .name(SPRINT_NAME)
                        .jiraProjectId(JIRAPROJECT_ID)
                        .totalExpected(37)
                        .totalCompleted(8)
                        .build()))
            .build(),
        jiraService.getSayDoRatio(ISC_PROJECT_ID, BOARD_ID));
  }

    @Test
  void getSayDoRatioTestWithException() {
    when(projectHelper.findAndValidateProjectByIdAndUser(ISC_PROJECT_ID))
        .thenReturn(UnitTestHelper.getDummyIscProject());
    when(jiraConfigurationRepository.findByProjectId(Mockito.any(ObjectId.class)))
        .thenReturn(
            Optional.of(
                JiraConfiguration.builder()
                    .projectId(new ObjectId(ISC_PROJECT_ID))
                    .jiraProjectId(JIRAPROJECT_ID)
                    .build()));
    when(sprintChartRepository.findByJiraProjectId(Mockito.anyInt(), Mockito.any(Sort.class)))
        .thenReturn(Collections.emptyList());
    when(jiraService.getSayDoRatio(ISC_PROJECT_ID, BOARD_ID))
        .thenThrow(RecordNotFoundException.class);
  }

    @Test
  void getLeaveCalenderBySprintIdTestCase_SuccessResponse() {
    when(projectHelper.findAndValidateProjectByIdAndUser(ISC_PROJECT_ID))
        .thenReturn(UnitTestHelper.getDummyIscProject());
    Project actualResponse =
        projectHelper.findAndValidateProjectByIdAndUser(ISC_PROJECT_ID);
    assertEquals(UnitTestHelper.getDummyIscProject(), actualResponse);
    when(projectRepository.findByIdAndIsDeletedIn(
            ISC_PROJECT_ID, Arrays.asList(null, Boolean.FALSE)))
        .thenReturn(Optional.of(UnitTestHelper.getDummyIscProject()));
    Optional<Project> projectDetail =
        projectRepository.findByIdAndIsDeletedIn(
            ISC_PROJECT_ID, Arrays.asList(null, Boolean.FALSE));
    assertEquals(Optional.of(UnitTestHelper.getDummyIscProject()), projectDetail);

    when(jiraConfigurationRepository.findByProjectId(Mockito.any(ObjectId.class)))
        .thenReturn(
            Optional.of(
                JiraConfiguration.builder()
                    .projectId(new ObjectId(ISC_PROJECT_ID))
                    .jiraProjectId(JIRAPROJECT_ID)
                    .build()));
    when(sprintsRepository.findByProjectIdAndSprintIdAndBoardId(
            Integer.parseInt(JIRAPROJECT_ID), Integer.parseInt(SPRINT_ID), BOARD_ID))
        .thenReturn(
            Optional.of(
                Sprints.builder()
                    .startDate(Long.parseLong("1589155200000"))
                    .endDate(Long.parseLong("1590710400000"))
                    .build()));

    projectDetail.get().setNestId("532");
    when(projectMembersRepository.findLocationByNestId(
            Integer.valueOf(projectDetail.get().getNestId())))
        .thenReturn(Arrays.asList(UnitTestHelper.getPojectMembers()));
    List<ProjectMembers> projectMembers =
        projectMembersRepository.findLocationByNestId(
            Integer.valueOf(projectDetail.get().getNestId()));
    assertEquals(Arrays.asList(UnitTestHelper.getPojectMembers()), projectMembers);
    when(publicHolidaysDao.getProjectMemberLocationWisePublicHolidays(
            Long.parseLong("1589155200000"),
            Long.parseLong("1590710400000"),
            Arrays.asList(projectMembers.get(0).getLocation())))
        .thenReturn(new ArrayList<>());
    List<PublicHolidays> projectPublicHolidays =
        publicHolidaysDao.getProjectMemberLocationWisePublicHolidays(
            Long.parseLong("1589155200000"),
            Long.parseLong("1590710400000"),
            Arrays.asList(projectMembers.get(0).getLocation()));
    assertEquals(new ArrayList<>(), projectPublicHolidays);
    when(projectMemberLeavesDao.getMemberLeaveData(
            Long.parseLong("1589155200000"),
            Long.parseLong("1590710400000"),
            new ObjectId(ISC_PROJECT_ID)))
        .thenReturn(UnitTestHelper.getNestProjectsIdWiseProjectMemberLeaveVo());
    List<NestProjectsIdWiseProjectMemberLeaveVo> projectMemberLeaves =
        projectMemberLeavesDao.getMemberLeaveData(
            Long.parseLong("1589155200000"),
            Long.parseLong("1590710400000"),
            new ObjectId(ISC_PROJECT_ID));
    assertNotNull(projectMemberLeaves);
    jiraService.getLeaveCalenderBySprintId(ISC_PROJECT_ID, BOARD_ID);
  }

    @Test
  void getLeaveCalenderBySprintIdTestCase_SprintDataNotFound() {
    when(projectHelper.findAndValidateProjectByIdAndUser(ISC_PROJECT_ID))
        .thenReturn(UnitTestHelper.getDummyIscProject());

    when(projectRepository.findByIdAndIsDeletedIn(
            ISC_PROJECT_ID, Arrays.asList(null, Boolean.FALSE)))
        .thenReturn(Optional.of(UnitTestHelper.getDummyIscProject()));
    when(jiraConfigurationRepository.findByProjectId(Mockito.any(ObjectId.class)))
        .thenReturn(
            Optional.of(
                JiraConfiguration.builder()
                    .projectId(new ObjectId("0"))
                    .jiraProjectId("0")
                    .build()));
    when(sprintsRepository.findByProjectIdAndSprintIdAndBoardId(0, 0, BOARD_ID)).thenReturn(null);
    assertThatExceptionOfType(RecordNotFoundException.class)
        .isThrownBy(
            () ->
                jiraService.getLeaveCalenderBySprintId(
                    ISC_PROJECT_ID, BOARD_ID));
  }

    @Test
  void getLeaveCalenderBySprintIdTestCase_ActiveSprintDataNotFound() {
    when(projectHelper.findAndValidateProjectByIdAndUser(ISC_PROJECT_ID))
        .thenReturn(UnitTestHelper.getDummyIscProject());
    Project actualResponse =
        projectHelper.findAndValidateProjectByIdAndUser(ISC_PROJECT_ID);
    assertEquals(UnitTestHelper.getDummyIscProject(), actualResponse);
    when(projectRepository.findByIdAndIsDeletedIn(
            ISC_PROJECT_ID, Arrays.asList(null, Boolean.FALSE)))
        .thenReturn(Optional.of(UnitTestHelper.getDummyIscProject()));
    Optional<Project> projectDetail =
        projectRepository.findByIdAndIsDeletedIn(
            ISC_PROJECT_ID, Arrays.asList(null, Boolean.FALSE));
    assertEquals(Optional.of(UnitTestHelper.getDummyIscProject()), projectDetail);

    when(jiraConfigurationRepository.findByProjectId(Mockito.any(ObjectId.class)))
        .thenReturn(
            Optional.of(
                JiraConfiguration.builder()
                    .projectId(new ObjectId(ISC_PROJECT_ID))
                    .jiraProjectId(JIRAPROJECT_ID)
                    .build()));
    when(sprintsRepository.findByProjectIdAndSprintIdAndBoardId(
            Integer.parseInt(JIRAPROJECT_ID), Integer.parseInt(SPRINT_ID), BOARD_ID))
        .thenReturn(Optional.of(Sprints.builder().startDate(null).endDate(null).build()));
    projectDetail.get().setNestId("532");
    when(projectMembersRepository.findLocationByNestId(
            Integer.valueOf(projectDetail.get().getNestId())))
        .thenReturn(Arrays.asList(UnitTestHelper.getPojectMembers()));
    List<ProjectMembers> projectMembers =
        projectMembersRepository.findLocationByNestId(
            Integer.valueOf(projectDetail.get().getNestId()));
    assertEquals(Arrays.asList(UnitTestHelper.getPojectMembers()), projectMembers);
    when(publicHolidaysDao.getProjectMemberLocationWisePublicHolidays(
            Instant.now().toEpochMilli(),
            Instant.now().plus(14, ChronoUnit.DAYS).toEpochMilli(),
            Arrays.asList(projectMembers.get(0).getLocation())))
        .thenReturn(new ArrayList<>());
    List<PublicHolidays> projectPublicHolidays =
        publicHolidaysDao.getProjectMemberLocationWisePublicHolidays(
            Instant.now().toEpochMilli(),
            Instant.now().plus(14, ChronoUnit.DAYS).toEpochMilli(),
            Arrays.asList(projectMembers.get(0).getLocation()));
    assertEquals(new ArrayList<>(), projectPublicHolidays);
    when(projectMemberLeavesDao.getMemberLeaveData(
            Instant.now().toEpochMilli(),
            Instant.now().plus(14, ChronoUnit.DAYS).toEpochMilli(),
            new ObjectId(ISC_PROJECT_ID)))
        .thenThrow(NullPointerException.class);
    jiraService.getLeaveCalenderBySprintId(DUMMY_TOKEN, BOARD_ID);
  }

    @Test
  void getLeaveCalenderBySprintIdTestCase_ProjectDataNotFound() {
    when(projectHelper.findAndValidateProjectByIdAndUser(ISC_PROJECT_ID))
        .thenReturn(UnitTestHelper.getDummyIscProject());
    when(projectRepository.findByIdAndIsDeletedIn(
            ISC_PROJECT_ID, Arrays.asList(null, Boolean.FALSE)))
        .thenReturn(Optional.of(UnitTestHelper.getDummyIscProject()));
    assertThrows(RecordNotFoundException.class,()->
    jiraService.getLeaveCalenderBySprintId(
        UnitTestHelper.getDummyIscProject().getId(), BOARD_ID));
  }

    @Test
  void getLeaveCalenderBySprintIdTestCase_ProjectMemberLeaveNullCheck() {
    when(projectHelper.findAndValidateProjectByIdAndUser(ISC_PROJECT_ID))
        .thenReturn(UnitTestHelper.getDummyIscProject());
    when(projectRepository.findByIdAndIsDeletedIn(
            ISC_PROJECT_ID, Arrays.asList(null, Boolean.FALSE)))
        .thenReturn(Optional.of(UnitTestHelper.getDummyIscProject()));
    when(jiraConfigurationRepository.findByProjectId(Mockito.any(ObjectId.class)))
        .thenReturn(
            Optional.of(
                JiraConfiguration.builder()
                    .projectId(new ObjectId(ISC_PROJECT_ID))
                    .jiraProjectId(JIRAPROJECT_ID)
                    .build()));
    when(sprintsRepository.findByProjectIdAndSprintIdAndBoardId(
            Integer.parseInt(JIRAPROJECT_ID), Integer.parseInt(SPRINT_ID), BOARD_ID))
        .thenReturn(
            Optional.of(
                Sprints.builder()
                    .startDate(Long.parseLong("1589155200000"))
                    .endDate(Long.parseLong("1597476287000"))
                    .build()));
    when(projectMembersRepository.findLocationByNestId(Integer.valueOf("532")))
        .thenReturn(Arrays.asList(UnitTestHelper.getPojectMembers()));

    when(publicHolidaysDao.getProjectMemberLocationWisePublicHolidays(
            Long.parseLong("1589155200000"),
            Long.parseLong("1597476287000"),
            Arrays.asList(UnitTestHelper.getPojectMembers().getLocation())))
        .thenReturn(UnitTestHelper.getPublicHolidays());
    List<NestProjectsIdWiseProjectMemberLeaveVo> expected =
        UnitTestHelper.getNestProjectsIdWiseProjectMemberLeaveVo();
    expected.get(0).setProjectMemberLeavesVo(null);
    when(projectMemberLeavesDao.getMemberLeaveData(
            Long.parseLong("1589155200000"),
            Long.parseLong("1597476287000"),
            new ObjectId(ISC_PROJECT_ID)))
        .thenReturn(expected);

    // jiraService.getLeaveCalenderBySprintId(DUMMY_TOKEN, ISC_PROJECT_ID, SPRINT_ID);
    when(jiraService.getLeaveCalenderBySprintId(ISC_PROJECT_ID, BOARD_ID))
        .thenThrow(NullPointerException.class);
  }

    @Test
  void getLeaveCalenderBySprintIdTestCase_PublicHolidayResponse() {
    when(projectHelper.findAndValidateProjectByIdAndUser(ISC_PROJECT_ID))
        .thenReturn(UnitTestHelper.getDummyIscProject());
    when(projectRepository.findByIdAndIsDeletedIn(
            ISC_PROJECT_ID, Arrays.asList(null, Boolean.FALSE)))
        .thenReturn(Optional.of(UnitTestHelper.getDummyIscProject()));
    when(jiraConfigurationRepository.findByProjectId(Mockito.any(ObjectId.class)))
        .thenReturn(
            Optional.of(
                JiraConfiguration.builder()
                    .projectId(new ObjectId(ISC_PROJECT_ID))
                    .jiraProjectId(JIRAPROJECT_ID)
                    .build()));
    when(sprintsRepository.findByProjectIdAndSprintIdAndBoardId(
            Integer.parseInt(JIRAPROJECT_ID), Integer.parseInt(SPRINT_ID), BOARD_ID))
        .thenReturn(
            Optional.of(
                Sprints.builder()
                    .startDate(Long.parseLong("1589155200000"))
                    .endDate(Long.parseLong("1597476287000"))
                    .build()));
    when(projectMembersRepository.findLocationByNestId(Integer.valueOf("532")))
        .thenReturn(Arrays.asList(UnitTestHelper.getPojectMembers()));

    when(publicHolidaysDao.getProjectMemberLocationWisePublicHolidays(
            Long.parseLong("1589155200000"),
            Long.parseLong("1597476287000"),
            Arrays.asList(UnitTestHelper.getPojectMembers().getLocation())))
        .thenReturn(UnitTestHelper.getPublicHolidays());
    when(projectMemberLeavesDao.getMemberLeaveData(
            Long.parseLong("1589155200000"),
            Long.parseLong("1597476287000"),
            new ObjectId(ISC_PROJECT_ID)))
        .thenReturn(UnitTestHelper.getNestProjectsIdWiseProjectMemberLeaveVo());

    jiraService.getLeaveCalenderBySprintId(ISC_PROJECT_ID, BOARD_ID);
  }

}
