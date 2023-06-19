package com.apexon.compass.psrservice;

import static com.apexon.compass.constants.PsrServiceConstants.ACTIVE;
import static com.apexon.compass.constants.PsrServiceConstants.DAY_STRING;

import com.apexon.compass.aggregation.vo.LeavesVo;
import com.apexon.compass.aggregation.vo.NestProjectsIdWiseProjectMemberLeaveVo;
import com.apexon.compass.aggregation.vo.ProjectMemberLeavesVo;
import com.apexon.compass.aggregation.vo.ProjectWiseStorySummaryVo;
import com.apexon.compass.aggregation.vo.SprintAcceptedVsDeliveredVo;
import com.apexon.compass.aggregation.vo.StoriesVo;
import com.apexon.compass.aggregation.vo.StoryStatusVo;
import com.apexon.compass.entities.BitBucket;
import com.apexon.compass.entities.Project;
import com.apexon.compass.entities.Jira;
import com.apexon.compass.entities.ManagerDetails;
import com.apexon.compass.entities.ProjectManager;
import com.apexon.compass.entities.ProjectMembers;
import com.apexon.compass.entities.PublicHolidays;
import com.apexon.compass.entities.Resources;
import com.apexon.compass.entities.Sonar;
import com.apexon.compass.entities.SprintChart;
import com.apexon.compass.entities.StatusData;
import com.apexon.compass.entities.Team;
import com.apexon.compass.psrservice.dto.Allocation;
import com.apexon.compass.psrservice.dto.CustomerDto;
import com.apexon.compass.psrservice.dto.LeaveCalenderDataDto;
import com.apexon.compass.psrservice.dto.LeaveCalenderDto;
import com.apexon.compass.psrservice.dto.LeaveDataDto;
import com.apexon.compass.psrservice.dto.LeavesDto;
import com.apexon.compass.psrservice.dto.MemberDto;
import com.apexon.compass.psrservice.dto.MemberStatusDto;
import com.apexon.compass.psrservice.dto.MembersDto;
import com.apexon.compass.psrservice.dto.NpsDataRequestDto;
import com.apexon.compass.psrservice.dto.NpsDataResponseDto;
import com.apexon.compass.psrservice.dto.NpsReportsDto;
import com.apexon.compass.psrservice.dto.OverallSatisfactionDto;
import com.apexon.compass.psrservice.dto.SprintBurndownDataDto;
import com.apexon.compass.psrservice.dto.SprintDataDto;
import com.apexon.compass.psrservice.dto.SprintMemberDetailsByIdDto;
import com.apexon.compass.psrservice.dto.SprintMemberStatusByIdDto;
import com.apexon.compass.psrservice.dto.SprintMemberStatusDto;
import com.apexon.compass.psrservice.dto.SurveyDto;
import com.apexon.compass.psrservice.dto.TaskDto;
import com.apexon.compass.psrservice.dto.TeamDto;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.bson.types.ObjectId;

public class UnitTestHelper {

    public static List<SprintChart> getDummysprintCharts() {
        return Arrays.asList(SprintChart.builder()
            .endDate(1576476900000L)
            .jiraProjectId(10003)
            .boardId(2)
            .sprintId(11)
            .name("IA Sprint 9")
            .state(ACTIVE)
            .totalEfforts(37)
            .totalTasks(42)
            .statusData(Arrays.asList(StatusData.builder()
                .date(1589221800000L)
                .remainingEfforts(24)
                .acceptedEfforts(0)
                .acceptedTasks(0)
                .toDoTasks(17)
                .inProgressTasks(21)
                .blockerTasks(4)
                .completedTasks(1)
                .remainingTasks(42)
                .team(Arrays.asList(Team.builder().personName("gopi manikandan").storyPointCompleted(8).build()))
                .build()))
            .build());
    }

    public static List<SprintBurndownDataDto> getburnDownList() {
        return Arrays.asList(SprintBurndownDataDto.builder()
            .name(StringUtils.join(DAY_STRING, StringUtils.SPACE, 1))
            .date(1589221800000L)
            .remainingEfforts(24)
            .remainingTasks(42)
            .completedTasks(1)
            .build());
    }

    public static List<SprintDataDto> getSprintDataDtos() {
        return Arrays.asList(SprintDataDto.builder()
            .sprintName("IA Sprint 9")
            .team(Arrays.asList(TeamDto.builder().personName("gopi manikandan").storyPointCompleted(8).build()))
            .build());
    }

    public static Project getDummyIscProject() {
        return Project.builder()
            .bitbucket(Arrays.asList(BitBucket.builder().name("bitbucket").url("https//bitbucket.dymmy.com").build()))
            .jira(Arrays.asList(Jira.builder().id("10003").url("www.jiradummy.com").build()))
            .name("InterMiles")
            .nestId("532")
            .resources(Arrays.asList(Resources.builder()
                .sow(6d)
                .actual(6d)
                .status("No visible attrition/Right Resouruces on board")
                .build()))
            .psrLocation("https://svn.infostretch.com/svn/PSR/Active/IS_PDO_PSR_InterMiles.xlsx")
            .sonar(Arrays.asList(Sonar.builder()
                .key("AXA5eeI0LmMa11FchLv5")
                .language("Java")
                .url("http://10.12.41.28:9000/dashboard/index/com.qmetry:intermiles")
                .build()))
            .accountManager(Arrays
                .asList(ManagerDetails.builder().name("demo.apexon").email("demo@apexon.com").build()))
            .billingType("T&M")
            .category("All")
            .clientName("Jet Privilege Pvt Ltd")
            .clientProjectManager(Arrays
                .asList(ManagerDetails.builder().name("demo.apexon").email("demo@apexon.com").build()))
            .deliveryManager(Arrays
                .asList(ManagerDetails.builder().name("demo.apexon").email("demo@apexon.com").build()))
            .projectManager(Arrays.asList(ProjectManager.builder()
                .email("demo@apexon.com")
                .bandwidth(0)
                .name("demo.apexon")
                .build()))
            .endDate(1640943925)
            .startDate(1564652725)
            .industrySpecificExposure("Financial services")
            .solutionTypeDistribution("Inception/Ideation")
            .id("5e7dbe36215e091c132c8f88")
            .jiraProjectId("10003")
            .nestIds(Arrays.asList("519"))
            .build();
    }

    public static List<SprintAcceptedVsDeliveredVo> getDummmyListSprintAcceptedVsDeliveredVo() {

        List<SprintAcceptedVsDeliveredVo> sprintAcceptedVsDeliveredVos = new ArrayList<>();

        sprintAcceptedVsDeliveredVos.add(SprintAcceptedVsDeliveredVo.builder()
            .projectId(new ObjectId("5ec53867bdaa23a9cf0ddcd4"))
            .jiraProjectId(10003)
            .name("IM Sprint 9")
            .storiesVos(getDummystoriesVos())
            .build());

        return sprintAcceptedVsDeliveredVos;
    }

    public static List<PublicHolidays> getPublicHolidays() {
        List<PublicHolidays> publicHolidays = new ArrayList<PublicHolidays>();

        publicHolidays.add(PublicHolidays.builder()
            .date(Long.parseLong("1596412800000"))
            .day("Mon")
            .location("Ahmedabad")
            .description("Rakshabandhan")
            .holidayId(254)
            .isRecurring(false)
            .build());
        publicHolidays.add(PublicHolidays.builder()
            .date(Long.parseLong("1597170600000"))
            .day("Wed")
            .location("Ahmedabad")
            .description("Janmashtami")
            .holidayId(255)
            .isRecurring(false)
            .build());
        return publicHolidays;
    }

    public static List<StoriesVo> getDummystoriesVos() {
        List<StoriesVo> storiesVo = new ArrayList<>();
        storiesVo.add(StoriesVo.builder().total(159).totalDone(159.0).totalClosed(105).build());
        return storiesVo;
    }

    public static ProjectWiseStorySummaryVo getDummyProjectWiseStorySummaryVo() {
        return ProjectWiseStorySummaryVo.builder()
            .sprintId(11)
            .name("Story Name")
            .stories(Arrays.asList(StoryStatusVo.builder()
                .total(20)
                .totalToDo(1)
                .totalCompleted(17)
                .totalInProgress(2)
                .totalAssigned(19)
                .totalUnassigned(1)
                .build()))
            .build();
    }

    public static ProjectMembers getPojectMembers() {
        return ProjectMembers.builder()
            .name("test")
            .userName("test")
            .designation("test")
            .dp("test")
            .initials("test")
            .memberId("1937")
            .email("test")
            .location("Ahmedabad")
            .nestProjectId("5eda20c6a0b6b358415f15e9")
            .build();
    }

    public static List<NestProjectsIdWiseProjectMemberLeaveVo> getNestProjectsIdWiseProjectMemberLeaveVo() {
        return Arrays.asList(NestProjectsIdWiseProjectMemberLeaveVo.builder()
            .projectMemberLeavesVo(Arrays.asList(ProjectMemberLeavesVo.builder()
                .memberId("2584")
                .name("test")
                .leaves(Arrays.asList(LeavesVo.builder()
                    .day("Monday")
                    .type("PTO")
                    .date(Long.parseLong("1590710400000"))
                    .hour(Double.parseDouble("8"))
                    .build()))
                .build()))
            .build());
    }

    public static LeaveCalenderDataDto getLeaveCalenderDataDto() {
        return LeaveCalenderDataDto.builder()
            .data(LeaveCalenderDto.builder()
                .leaveCalendar(Arrays.asList(LeaveDataDto.builder()
                    .date(1590710400000L)
                    .isMemberLeave(false)
                    .isPublicHoliday(false)
                    .memberLeaveHours(0)
                    .publicHolidayHours(0)
                    .publicHolidayLocation(null)
                    .build()))
                .build())
            .build();
    }

    public static NpsDataRequestDto getNpsDataRequestDto() {
        return NpsDataRequestDto.builder()
            .fileName("nps-example.xlsx")
            .submissionDate("mm/dd/yyyy")
            .submissionPeriod("Jan-2020 to May-2020")
            .customer(CustomerDto.builder().name("Marcie King").role("Chief Product Officer").build())
            .teamSize("100 people")
            .duration("90 days")
            .survey(getSurvey())
            .overallSatisfaction(getOverallSatisfaction())
            .conclusionRemarks("Keep doing what they've been doing.")
            .build();
    }

    public static NpsDataRequestDto getInvalidNpsDataRequestDto() {
        return NpsDataRequestDto.builder()
            .fileName("nps-example.docx")
            .submissionDate("mm/dd/yyyy")
            .submissionPeriod("Jan-2020 to May-2020")
            .customer(CustomerDto.builder().name("Marcie King").role("Chief Product Officer").build())
            .teamSize("100 people")
            .duration("90 days")
            .survey(getSurvey())
            .overallSatisfaction(getOverallSatisfaction())
            .conclusionRemarks("Keep doing what they've been doing.")
            .build();
    }

    public static NpsDataRequestDto getInvalidNpsDataRequestDtoWithBlankSubmissionPeriodParam() {
        return NpsDataRequestDto.builder()
            .fileName("nps-example.xlsx")
            .submissionDate("mm/dd/yyyy")
            .customer(CustomerDto.builder().name("Marcie King").role("Chief Product Officer").build())
            .teamSize("100 people")
            .duration("90 days")
            .survey(getSurvey())
            .overallSatisfaction(getOverallSatisfaction())
            .conclusionRemarks("Keep doing what they've been doing.")
            .build();
    }

    public static NpsDataRequestDto getInvalidNpsDataRequestDtoWithBlankCustomerRoleParam() {
        return NpsDataRequestDto.builder()
            .fileName("nps-example.xlsx")
            .submissionDate("mm/dd/yyyy")
            .submissionPeriod("Jan-2020 to May-2020")
            .customer(CustomerDto.builder().name("Marcie King").build())
            .teamSize("100 people")
            .duration("90 days")
            .survey(getSurvey())
            .overallSatisfaction(getOverallSatisfaction())
            .conclusionRemarks("Keep doing what they've been doing.")
            .build();
    }

    public static NpsDataRequestDto getInvalidNpsDataRequestDtoWithBlankSurveyRatingParam() {
        return NpsDataRequestDto.builder()
            .fileName("nps-example.xlsx")
            .submissionDate("mm/dd/yyyy")
            .submissionPeriod("Jan-2020 to May-2020")
            .customer(CustomerDto.builder().name("Marcie King").role("Chief Product Officer").build())
            .teamSize("100 people")
            .duration("90 days")
            .survey(getInvalidSurveyParam())
            .overallSatisfaction(getOverallSatisfaction())
            .conclusionRemarks("Keep doing what they've been doing.")
            .build();
    }

    public static List<SurveyDto> getSurvey() {
        return Arrays.asList(
                SurveyDto.builder().key("overall").rating("10").response("High quality people, great partners").build(),
                SurveyDto.builder()
                    .key("requirements")
                    .rating("10")
                    .response("High quality people, great partners")
                    .build(),
                SurveyDto.builder()
                    .key("qualityAndCompleteness")
                    .rating("10")
                    .response("High quality people, great partners")
                    .build());
    }

    public static List<SurveyDto> getInvalidSurveyParam() {
        return Arrays.asList(
                SurveyDto.builder().key("overall").rating("10").response("High quality people, great partners").build(),
                SurveyDto.builder()
                    .key("requirements")
                    .rating("10")
                    .response("High quality people, great partners")
                    .build(),
                SurveyDto.builder()
                    .key("qualityAndCompleteness")
                    .response("High quality people, great partners")
                    .build());
    }

    public static List<OverallSatisfactionDto> getOverallSatisfaction() {
        return Arrays.asList(OverallSatisfactionDto.builder()
            .key("bestThing")
            .response(
                    "Partnership. They don't just do what we ask, they look for ways to be business partners. They realize we depend on each other for success.")
            .build(), OverallSatisfactionDto.builder().key("likelyhoodOfNext").rating("Definitely").build(),
                OverallSatisfactionDto.builder()
                    .key("ratings")
                    .rating("Better")
                    .response(
                            "We've had some really bad partnerships. This is the best vendor/partner team I could ever hope for")
                    .build());
    }

    public static NpsDataResponseDto getNpsDataResponseDto() {

        List<SurveyDto> SurveyDtos = Arrays.asList(
                SurveyDto.builder().key("overall").rating("10").response("High quality people, great partners").build(),
                SurveyDto.builder()
                    .key("requirements")
                    .rating("10")
                    .response("High quality people, great partners")
                    .build(),
                SurveyDto.builder()
                    .key("qualityAndCompleteness")
                    .rating("10")
                    .response("High quality people, great partners")
                    .build());

        List<OverallSatisfactionDto> overallSatisfaction = Arrays.asList(OverallSatisfactionDto.builder()
            .key("bestThing")
            .response(
                    "Partnership. They don't just do what we ask, they look for ways to be business partners. They realize we depend on each other for success.")
            .build(), OverallSatisfactionDto.builder().key("likelyhoodOfNext").rating("Definitely").build(),
                OverallSatisfactionDto.builder()
                    .key("ratings")
                    .rating("Better")
                    .response(
                            "We've had some really bad partnerships. This is the best vendor/partner team I could ever hope for")
                    .build());

        return NpsDataResponseDto.builder()
            .id("2426e654-6304-40f9-8492-5bed192db69f")
            .customer(CustomerDto.builder().name("Marcie King").role("Chief Product Officer").build())
            .teamSize("100 people")
            .duration("90 days")
            .survey(SurveyDtos)
            .overallSatisfaction(overallSatisfaction)
            .conclusionRemarks("Keep doing what they've been doing.")
            .submissionPeriod("Jan-2020 to May-2020")
            .submissionDate(1589192799L)
            .build();
    }

    public static NpsReportsDto getNpsReportsDto() {
        return NpsReportsDto.builder().iscProjectId("e818b44565957705fa2c1b4").averageNps(9.37).build();
    }

    public static SprintMemberStatusDto getSprintMemberStatusDto() {
        return SprintMemberStatusDto.builder()
            .id("5e7d9d1552596c3bfc0cb1a6")
            .jiraProjectId("00000")
            .sprintId("8")
            .sprintName("IA Sprint 6")
            .members(Arrays.asList(
                    MemberDto.builder()
                        .name("Parth Shah")
                        .accountId("")
                        .memberId("nest member id")
                        .todo(20)
                        .inProgress(25)
                        .completed(8)
                        .availableHours(24)
                        .build(),
                    MemberDto.builder()
                        .name("Nisarg Thakkar")
                        .accountId("")
                        .memberId("nest member id")
                        .todo(20)
                        .inProgress(25)
                        .completed(8)
                        .availableHours(24)
                        .build()))
            .build();
    }

    public static SprintMemberStatusByIdDto getSprintMemberStatusByIdDto() {
        return SprintMemberStatusByIdDto.builder()
            .id("5e7d9d1552596c3bfc0cb1a6")
            .jiraProjectId("00000")
            .sprintId("8")
            .sprintName("IA Sprint 6")
            .member(MembersDto.builder()
                .name("Parth Shah")
                .accountId("")
                .status(MemberStatusDto.builder()
                    .todo(20)
                    .inProgress(25)
                    .completed(8)
                    .availableHours(24)
                    .averageStoryPoints(1)
                    .build())
                .tasks(Arrays.asList(
                        TaskDto.builder()
                            .number("IA-206")
                            .name("Efforts to be done")
                            .isSpilledOver(false)
                            .type("Sub-task")
                            .url("https://intermilesapp.atlassian.net/browse/IA-206")
                            .build(),
                        TaskDto.builder()
                            .number("IA-207")
                            .name("Efforts to be done")
                            .isSpilledOver(true)
                            .type("Sub-task")
                            .url("https://intermilesapp.atlassian.net/browse/IA-207")
                            .build()))
                .build())
            .build();
    }

    public static SprintMemberDetailsByIdDto getSprintMemberDetailsByIdDto() {
        return SprintMemberDetailsByIdDto.builder()
            .name("Parth Shah")
            .email("parth.shah@infostretch.com")
            .dp("url")
            .designation("Lead UX Developer")
            .leaves(Arrays.asList(LeavesDto.builder().date("20/01/2020").build(),
                    LeavesDto.builder().date("20/02/2020").build()))
            .allocation(Allocation.builder()
                .billable("100%")
                .startDate("13-Feb-2020")
                .endDate("15-May-2020")
                .totalDays("93")
                .client("Office")
                .project("DES")
                .build())
            .build();
    }

}
