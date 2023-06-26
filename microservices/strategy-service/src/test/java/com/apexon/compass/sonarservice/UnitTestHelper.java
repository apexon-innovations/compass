package com.apexon.compass.sonarservice;

import com.apexon.compass.aggregation.vo.*;
import com.apexon.compass.entities.*;
import com.apexon.compass.sonarservice.dto.*;
import org.bson.types.ObjectId;

import java.util.*;

public class UnitTestHelper {

    public static List<SprintSubmitterMetricsVo> getSprintSubmitterMetricsVos() {
        List<SprintSubmitterMetricsVo> sprintPullRequestSummaryVos = new ArrayList<>();

        SprintSubmitterMetricsVo sprintSubmitterMetricsVoOne = SprintSubmitterMetricsVo.builder()
            ._id("Sameer Tiwari")
            .totalPrs(2.0)
            .openPrs(0.0)
            .mergedPrs(2.0)
            .declinedPrs(0.0)
            .reviewerComments(1.0)
            .recommits(1.0)
            .build();

        SprintSubmitterMetricsVo sprintSubmitterMetricsVoTwo = SprintSubmitterMetricsVo.builder()
            ._id("Vaghela Binita")
            .totalPrs(1.0)
            .openPrs(0.0)
            .mergedPrs(0.0)
            .declinedPrs(1.0)
            .reviewerComments(1.0)
            .recommits(2.0)
            .build();

        SprintSubmitterMetricsVo sprintSubmitterMetricsVoThree = SprintSubmitterMetricsVo.builder()
            ._id("Abhijit Raval")
            .totalPrs(2.0)
            .openPrs(0.0)
            .mergedPrs(2.0)
            .declinedPrs(0.0)
            .reviewerComments(0.0)
            .recommits(1.0)
            .build();

        SprintSubmitterMetricsVo sprintSubmitterMetricsVoFour = SprintSubmitterMetricsVo.builder()
            ._id("Sweety Panchal")
            .totalPrs(4.0)
            .openPrs(0.0)
            .mergedPrs(3.0)
            .declinedPrs(1.0)
            .reviewerComments(2.0)
            .recommits(1.0)
            .build();

        sprintPullRequestSummaryVos.add(sprintSubmitterMetricsVoOne);
        sprintPullRequestSummaryVos.add(sprintSubmitterMetricsVoTwo);
        sprintPullRequestSummaryVos.add(sprintSubmitterMetricsVoThree);
        sprintPullRequestSummaryVos.add(sprintSubmitterMetricsVoFour);
        return sprintPullRequestSummaryVos;
    }

    public static List<SubmitterMetricsDto> getSprintSubmitterMetricsDtos() {
        List<SubmitterMetricsDto> submitterMetricsDtos = new ArrayList<>();

        SubmitterMetricsDto submitterMetricsDtoOne = SubmitterMetricsDto.builder()
            .name("Sameer Tiwari")
            .totalPrs(2)
            .openPrs(0)
            .mergedPrs(2)
            .declinedPrs(0)
            .reviewerComments(1)
            .recommits(1)
            .build();

        SubmitterMetricsDto submitterMetricsDtoTwo = SubmitterMetricsDto.builder()
            .name("Vaghela Binita")
            .totalPrs(1)
            .openPrs(0)
            .mergedPrs(0)
            .declinedPrs(1)
            .reviewerComments(1)
            .recommits(2)
            .build();

        SubmitterMetricsDto submitterMetricsDtoThree = SubmitterMetricsDto.builder()
            .name("Abhijit Raval")
            .totalPrs(2)
            .openPrs(0)
            .mergedPrs(2)
            .declinedPrs(0)
            .reviewerComments(0)
            .recommits(1)
            .build();

        SubmitterMetricsDto submitterMetricsDtoFour = SubmitterMetricsDto.builder()
            .name("Sweety Panchal")
            .totalPrs(4)
            .openPrs(0)
            .mergedPrs(3)
            .declinedPrs(1)
            .reviewerComments(2)
            .recommits(1)
            .build();

        submitterMetricsDtos.add(submitterMetricsDtoOne);
        submitterMetricsDtos.add(submitterMetricsDtoTwo);
        submitterMetricsDtos.add(submitterMetricsDtoThree);
        submitterMetricsDtos.add(submitterMetricsDtoFour);
        return submitterMetricsDtos;
    }

    public static Project getDummyIscProject() {
        return Project.builder()
            .bitbucket(Arrays.asList(BitBucket.builder().name("bitbucket").url("https//bitbucket.dymmy.com").build()))
            .jira(Arrays.asList(Jira.builder().id("10003").url("www.jiradummy.com").build()))
            .name("InterMiles")
            .nestId("1000")
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
            .build();
    }

    public static SprintPullRequestSummaryVo getSprintPullRequestSummaryVo() {
        return SprintPullRequestSummaryVo.builder()
            ._id(123)
            .total(36.0)
            .merged(30.0)
            .open(2.0)
            .declined(2.0)
            .unattended(2.0)
            .build();
    }

    public static SprintPullRequestSummaryDataDto getSprintPullRequestSummaryDataDto() {
        return SprintPullRequestSummaryDataDto.builder()
            .repoId("F6edsBx7")
            .repoName("rxsense")
            .total(36)
            .merged(30)
            .open(2)
            .declined(2)
            .unattended(2)
            .build();
    }

    public static SonarConfigurations getSonarConfigurations() {
        return SonarConfigurations.builder()
            .projectId(new ObjectId("5e7dbe36215e091c132c8f88"))
            .projects(Arrays.asList(
                    Projects.builder()
                        .key("AW7pc6H6HqpRtKjX1nD9")
                        .language("Java")
                        .url("https://svn.infostretch.com/svn/PSR/Active/IS_PDO_PSR_InterMiles.xlsx")
                        .build(),
                    Projects.builder()
                        .key("AW6sZS9uvX9Qb-HnV8hb")
                        .language("Java")
                        .url("https://svn.infostretch.com/svn/PSR/Active/IS_PDO_PSR_InterMiles.xlsx")
                        .build()))
            .build();
    }

    public static ViolationsSummaryDto getViolationsSummaryDto() {
        return ViolationsSummaryDto.builder()
            .data(ViolationsDataDto.builder().violations(1).language("x").build())
            .build();
    }

    public static CollaborationMetricDataDto getCollaborationMetricDataDto() {
        return CollaborationMetricDataDto.builder()
            .average(CollaborationMetricAverageDto.builder()
                .firstComment("1 hour")
                .prResolveTime("1 Day")
                .recommit(8)
                .build())
            .build();
    }

    public static List<SonarStats> getSonarStats() {
        return Arrays.asList(
                SonarStats.builder()
                    .projectId(new ObjectId("5e7dbe36215e091c132c8f88"))
                    .complexity("test")
                    .sonarProjectId("AW7pc6H6HqpRtKjX1nD9")
                    .name("name")
                    .createdDate(Long.parseLong("15646527225"))
                    .securityEfforts("securityEfforts")
                    .sonarurl("https://svn.infostretch.com/svn/PSR/Active/IS_PDO_PSR_InterMiles.xlsx")
                    .newCoverage("newCoverage")
                    .violations(Violations.builder().total(230).build())
                    .newViolations(NewViolations.builder().total(230).build())
                    .qualityMatrix(QualityMatrix.builder()
                        .bugs("2")
                        .codeSmells("3")
                        .coverage("5")
                        .newBugs("3")
                        .newCodeSmells("2")
                        .newCoverage("2")
                        .build())
                    .duplication(Duplication.builder()
                        .blocks("2")
                        .lineDensity("2")
                        .lines("2")
                        .newBlocks("2")
                        .newLineDensity("2")
                        .newLines("2")
                        .build())
                    .codeMatrix(CodeMatrix.builder()
                        .classes("test")
                        .commentLines("2")
                        .directoryCnt("2")
                        .files("test")
                        .lines("2")
                        .lineToCover("12")
                        .ncloc("3")
                        .build())
                    .build(),
                SonarStats.builder()
                    .projectId(new ObjectId("5e7dbe36215e091c132c8f88"))
                    .complexity("test")
                    .sonarProjectId("AW6sZS9uvX9Qb-HnV8hb")
                    .name("name")
                    .createdDate(Long.parseLong("15646527225"))
                    .securityEfforts("securityEfforts")
                    .sonarurl("https://svn.infostretch.com/svn/PSR/Active/IS_PDO_PSR_InterMiles.xlsx")
                    .newCoverage("newCoverage")
                    .violations(Violations.builder().total(230).build())
                    .newViolations(NewViolations.builder().total(230).build())
                    .qualityMatrix(QualityMatrix.builder()
                        .bugs("2")
                        .codeSmells("3")
                        .coverage("5")
                        .newBugs("3")
                        .newCodeSmells("2")
                        .newCoverage("2")
                        .build())
                    .duplication(Duplication.builder()
                        .blocks("2")
                        .lineDensity("2")
                        .lines("2")
                        .newBlocks("2")
                        .newLineDensity("2")
                        .newLines("2")
                        .build())
                    .codeMatrix(CodeMatrix.builder()
                        .classes("test")
                        .commentLines("2")
                        .directoryCnt("2")
                        .files("test")
                        .lines("2")
                        .lineToCover("12")
                        .ncloc("3")
                        .build())
                    .build());
    }

    public static List<PullRequestsCommitsVo> getPullRequestsCommitsVo() {
        return Arrays.asList(PullRequestsCommitsVo.builder()
            .pull_requests(PullRequestsVo.builder()
                .commitLog("Utthan3108/Des-1454: resolved bug and add junit testcase for calender")
                .projectId(new ObjectId("5ee31002d48d514e801cb10b"))
                .prCreatedTime(1592471666746L)
                .prDeclinedTime(1592471666749L)
                .prDeclinedBy("tanmay")
                .prLastUpdatedTime(1592471666746L)
                .prMergedBy("tanmay")
                .prMergeTime(1592485495452L)
                .prNumber("129")
                .scmUrl("http://SCMdummyUrl.com")
                .sourceBranch("feature/20200608/Utthan/Des-1454")
                .sourceRepoId("{aa63baa5-5290-406f-a2cc-e8aef23902f6}")
                .state("Open")
                .scmConfigurationId(new ObjectId("5ee0ae16613c3ccdf13a47af"))
                .revisionNumber("1")
                .approvers(Arrays.asList(Approvers.builder()
                    .at(1592485701952L)
                    .name("Tanmay Baxi")
                    .id("{cb2857de-8fec-47af-8f75-12fb7d395580}")
                    .build()))
                .comments(Arrays.asList(Comments.builder()
                    .name("Tanmay Baxi")
                    .commentedOn(1591864617669L)
                    .commentUpdatedOn(1591864620273L)
                    .message("* the formatting is not applied on new files")
                    .build()))
                .commits(Arrays.asList(
                        PullRequestsCommits.builder()
                            .id("5eec66a887f89b1ae2dd3158")
                            .authorEmail("heena.rajput@infostretch.com")
                            .authorUserId("heena3108")
                            .projectId(new ObjectId("5ee31002d48d514e801cb10b"))
                            .time(1592870400000L)
                            .message("heena3108/Des-639: pr comment resolved.")
                            .build(),
                        PullRequestsCommits.builder()
                            .id("5eec66a887f89b1ae2dd3158")
                            .authorEmail("heena.rajput@infostretch.com")
                            .authorUserId("heena3108")
                            .projectId(new ObjectId("5e7dbe36215e091c132c8f88"))
                            .message("heena3108/Des-639 add formatting.")
                            .time(1592870400000L)
                            .build()))
                .reviewers(Arrays.asList(AuthorIdAndName.builder().id("test").name("Tanmay Baxi").build(),
                        AuthorIdAndName.builder().id("test1").name("Devendrasinh Zala").build()))
                .scmAuthor(AuthorIdAndName.builder()
                    .id("{2a3409f3-e94b-40a5-98b9-9672d627a5eb}")
                    .name("Utthan Dharwa")
                    .build())
                .targetBranch("develop")
                .targetRepoId("{aa63baa5-5290-406f-a2cc-e8aef23902f6}")
                .build())
            .build());
    }

    public static Sprints getOptionalSprintsObject() {

        Sprints sprint = Sprints.builder()
            .jiraProjectId(10003)
            .boardId(2)
            .sprintId(1)
            .name("IM Sprint 1")
            .startDate(Long.parseLong("1575267304479"))
            .endDate(Long.parseLong("1576476900000"))
            .state("CLOSED")
            .jiraChangeDate(null)
            .isDeleted(true)
            .createdDate(Long.parseLong("1589984431264"))
            .build();

        return sprint;
    }

    public static List<PullRequestReviewersVo> getPullRequestReviewersVo() {

        List<PullRequestReviewersVo> pullRequestReviewersVos = new ArrayList<>();

        PullRequestReviewersVo vo1 = PullRequestReviewersVo.builder()
            ._id("Tanmay Baxi")
            .totalPrs(Double.valueOf(46))
            .totalTimeForMergedPr(60.0)
            .totalTimeForDeclinedPr(40.0)
            .totalTimeForOpenPr(20.0)
            .build();

        PullRequestReviewersVo vo2 = PullRequestReviewersVo.builder()
            ._id("Anurag Bombarde")
            .totalPrs(Double.valueOf(46))
            .totalTimeForMergedPr(Double.valueOf(46))
            .totalTimeForDeclinedPr(Double.valueOf(46))
            .totalTimeForOpenPr(Double.valueOf(46))
            .build();

        PullRequestReviewersVo vo3 = PullRequestReviewersVo.builder()
            ._id("Devendrasinh Zala")
            .totalPrs(Double.valueOf(46))
            .totalTimeForMergedPr(Double.valueOf(46))
            .totalTimeForDeclinedPr(Double.valueOf(46))
            .totalTimeForOpenPr(Double.valueOf(46))
            .build();

        pullRequestReviewersVos.add(vo1);
        pullRequestReviewersVos.add(vo2);
        pullRequestReviewersVos.add(vo3);
        return pullRequestReviewersVos;
    }

    public static ReviewerMetricsDto getReviewerMetricsDto() {
        List<ReviewersDto> reviewersDtos = new ArrayList<>();
        ReviewersDto reviewersDto1 = ReviewersDto.builder()
            .name("Tanmay Baxi")
            .totalPrs("46")
            .totalReviewTime("120  Hours")
            .build();
        ReviewersDto reviewersDto2 = ReviewersDto.builder()
            .name("Anurag Bombarde")
            .totalPrs("2")
            .totalReviewTime("0  Hours")
            .build();
        ReviewersDto reviewersDto3 = ReviewersDto.builder()
            .name("Devendrasinh Zala")
            .totalPrs("37")
            .totalReviewTime("50  Hours")
            .build();
        reviewersDtos.add(reviewersDto1);
        reviewersDtos.add(reviewersDto2);
        reviewersDtos.add(reviewersDto3);
        return ReviewerMetricsDto.builder().averagePrTime("2  Hours").reviewers(reviewersDtos).build();
    }

    public static JiraConfiguration getJiraConfiguration() {
        return JiraConfiguration.builder()
            .id("123")
            .projectId(new ObjectId("5e7dbe36215e091c132c8f88"))
            .jiraProjectId("10003")
            .jiraBoardId(List.of("2"))
            .url("http://dummyUrl.com")
            .queryEndPoint("rest/endPoint")
            .projectStartDate(946684800000L)
            .jiraApiKey("1234abcd5678")
            .issueTypes(Arrays.asList("Issue1", "Issue2"))
            .sprintDataFieldName("sprintFieldName")
            .jiraEpicIdFieldName("jiraFieldName")
            .jiraStoryPointsFieldName("jiraPointsName")
            .jiraBoardAsTeam(true)
            .cron("0/1 0 0/4 * * *")
            .createdBy("Ankit Shah")
            .createdDate(1585023552000L)
            .updatedBy("Ankit Shah")
            .updatedDate(1585023552000L)
            .build();
    }

    public static PullRequestsDto getPullRequestsDto() {
        return PullRequestsDto.builder()
            .pullRequests(Arrays.asList(PullRequestDto.builder()
                .id("5e7dbe36215e091c132c8f88")
                .number("129")
                .commitLog("heena3108/Des-639: resolved bug and add junit testcase for calender")
                .createdDateTime(1592471666746L)
                .journeyDuration("10h25m")
                .mergedDateTime(1592485580656L)
                .author(AuthorReviewerDto.builder()
                    .id("{2a3409f3-e94b-40a5-98b9-9672d627a5eb}")
                    .name("Heena Rajput")
                    .build())
                .build()))
            .build();
    }

    public static CodeMetricsDto getCodeMetricsDto() {
        List<CodeMetricsMembersDto> codeMetricsDtos = new ArrayList<>();
        CodeMetricsMembersDto dto1 = CodeMetricsMembersDto.builder().name("Ankit").legacyRefactor(100L).build();
        CodeMetricsMembersDto dto2 = CodeMetricsMembersDto.builder().name("Abhijit").legacyRefactor(-20L).build();
        CodeMetricsMembersDto dto3 = CodeMetricsMembersDto.builder().name("Utthan").legacyRefactor(300L).build();
        codeMetricsDtos.add(dto1);
        codeMetricsDtos.add(dto2);
        codeMetricsDtos.add(dto3);
        return CodeMetricsDto.builder()
            .members(codeMetricsDtos)
            .overall(OverAllDto.builder()
                .addedLineOfCode(1L)
                .addedLineOfCodeTotal(2L)
                .removedLineOfCode(1L)
                .codeChurn(1L)
                .removedLineOfCodeTotal(1L)
                .lineOfCode(10500L)
                .legacyRefactor(380L)
                .build())
            .average(OverAllDto.builder()
                .addedLineOfCode(1L)
                .addedLineOfCodeTotal(2L)
                .removedLineOfCode(1L)
                .codeChurn(1L)
                .removedLineOfCodeTotal(1L)
                .lineOfCode(10500L)
                .legacyRefactor(380L)
                .build())
            .build();
    }

    public static TechnicalDebtMetricsDto getTechnicalDebtDto() {
        return TechnicalDebtMetricsDto.builder()
            .complexity(1)
            .coverage(1.0)
            .duplications(1)
            .issues(1)
            .maintainability("1")
            .reliability("1")
            .security("1")
            .size(1)
            .build();
    }

    public static List<CodeChurnVo> getCodeChurnVos() {
        return Arrays.asList(CodeChurnVo.builder().id("Sameer Tiwari").productiveCode(10).codeChurn(-5).build(),
                CodeChurnVo.builder().id("Anshul Patel").productiveCode(5).codeChurn(1).build(),
                CodeChurnVo.builder().id("Binita Vaghela").productiveCode(2).codeChurn(3).build());
    }

    public static ComplianceAnalysisVO getComplianceVo() {
        return ComplianceAnalysisVO.builder()
            .efficiency(2.00)
            .robustness(3.00)
            .security(15.80)
            .testCodeCoverage(19.80)
            .vulnerabilities(17.50)
            .id(new ObjectId("5e7dbe36215e091c132c8f88"))
            .build();
    }

    public static ComplianceAnalysisVO getComplainceVoNull() {
        return ComplianceAnalysisVO.builder()
            .efficiency(null)
            .robustness(null)
            .security(null)
            .testCodeCoverage(null)
            .vulnerabilities(null)
            .id(null)
            .build();
    }

    public static ComplianceAnalysisDto getComplianceAnalysisDto() {
        return ComplianceAnalysisDto.builder().compliance(0.0).build();
    }

    public static SprintSubmitterMetricsDto getSprintSubmitterMetricsDto() {
        return SprintSubmitterMetricsDto.builder().submitters(getSprintSubmitterMetricsDtos()).averagePrs(2.25).build();
    }

    public static QualityMetricsSummaryDto getQualityMetricsSummaryDto() {
        return QualityMetricsSummaryDto.builder()
            .overall(QualityOverallSummaryDto.builder()
                .bugs(9)
                .vulnerabilities(2)
                .codeCoverage("98%")
                .duplications("5%")
                .duplicationBlocks(8)
                .build())
            .newSummary(QualityNewSummaryDto.builder()
                .bugs(9)
                .vulnerabilities(2)
                .codeCoverage("89%")
                .duplications("2%")
                .newLineOfCodes(3976)
                .duplicationLines(549)
                .build())
            .build();
    }

    public static ActivityMetricsDto getActivityMetricsDto() {
        return ActivityMetricsDto.builder()
            .months(Arrays.asList(MonthWiseDto.builder()
                .members(Arrays.asList(MemberDto.builder()
                    .mergedPrs(6)
                    .name("Abhijit Raval")
                    .codeCommits(18)
                    .totalPrs(6)
                    .reviewCommentsOnOthersPrs(0)
                    .build()))
                .build()))
            .build();
    }

    public static List<Sprints> getSprints() {
        return Arrays.asList(
                Sprints.builder()
                    .boardId(123)
                    .createdDate(15646527225L)
                    .endDate(15646527225L)
                    .isDeleted(false)
                    .jiraChangeDate(15646527225L)
                    .name("test")
                    .jiraProjectId(11)
                    .state("active")
                    .sprintId(10003)
                    .startDate(15646527225L)
                    .boardId(123)
                    .build(),
                Sprints.builder()
                    .boardId(123)
                    .createdDate(15646527225L)
                    .endDate(15646527225L)
                    .isDeleted(false)
                    .jiraChangeDate(15646527225L)
                    .name("test")
                    .jiraProjectId(11)
                    .state("active")
                    .sprintId(10003)
                    .startDate(15646527225L)
                    .boardId(123)
                    .build());
    }

    public static List<MemberWiseActivityMetricsVo> getMemberWiseActivityMetricsVo() {
        return Arrays.asList(
                MemberWiseActivityMetricsVo.builder()
                    ._id("2423")
                    .codeCommits(18)
                    .mergedPrs(6D)
                    .reviewCommentsOnOthersPrs(0D)
                    .totalPrs(6D)
                    .build(),
                MemberWiseActivityMetricsVo.builder()
                    ._id("2423")
                    .codeCommits(null)
                    .mergedPrs(null)
                    .reviewCommentsOnOthersPrs(null)
                    .totalPrs(null)
                    .build());
    }

    public static Optional<SonarStateMeasuresAverageCalculationVo> getSonarStateMeasuresAverageCalculationVo() {
        return Optional.ofNullable(SonarStateMeasuresAverageCalculationVo.builder()
            .id(new ObjectId("5e7dbe36215e091c132c8f88"))
            .robustness(25D)
            .efficiency(24D)
            .security(23D)
            .build());
    }

}
