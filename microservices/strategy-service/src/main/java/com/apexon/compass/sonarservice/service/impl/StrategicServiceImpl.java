package com.apexon.compass.sonarservice.service.impl;

import com.apexon.compass.aggregation.vo.*;
import com.apexon.compass.entities.Project;
import com.apexon.compass.entities.ScmConfiguration;
import com.apexon.compass.exception.custom.ForbiddenException;
import com.apexon.compass.exception.custom.RecordNotFoundException;
import com.apexon.compass.exception.custom.UnauthorizedException;
import com.apexon.compass.sonarservice.dao.*;
import com.apexon.compass.sonarservice.dto.*;
import com.apexon.compass.sonarservice.enums.SubmitterMetricsSortFilterEnum;
import com.apexon.compass.sonarservice.repository.*;
import com.apexon.compass.sonarservice.service.StrategicService;
import com.apexon.compass.sonarservice.service.impl.helper.ProjectHelper;
import com.apexon.compass.sonarservice.utils.DateTimeUtils;
import com.apexon.compass.utilities.ArithmeticUtils;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

import static com.apexon.compass.constants.QueryConstants.*;
import static com.apexon.compass.constants.StrategyServiceConstants.DAYS;
import static com.apexon.compass.constants.StrategyServiceConstants.DECIMAL_FORMAT;
import static com.apexon.compass.constants.StrategyServiceConstants.HOURS;
import static com.apexon.compass.constants.StrategyServiceConstants.MERGED;
import static com.apexon.compass.constants.StrategyServiceConstants.PERCENTAGE;
import static com.apexon.compass.constants.StrategyServiceConstants.RECORD_NOT_FOUND;
import static com.apexon.compass.sonarservice.utils.DateTimeUtils.*;
import static com.apexon.compass.utilities.DateTimeUtils.convertMinutesToDays;
import static com.apexon.compass.utilities.DateTimeUtils.getDurationInDays;
import static com.apexon.compass.utilities.StringOperationsUtils.concatHourToInteger;
import static com.apexon.compass.utilities.StringOperationsUtils.csvToList;
import static org.apache.commons.lang3.math.NumberUtils.DOUBLE_ZERO;
import static org.apache.commons.lang3.math.NumberUtils.INTEGER_ZERO;

@Service
@AllArgsConstructor(onConstructor = @__({ @Autowired }))
@Slf4j
public class StrategicServiceImpl implements StrategicService {

    private ProjectHelper projectHelper;

    private SprintsRepository sprintsRepository;

    private JiraConfigurationRepository jiraConfigurationRepository;

    private PullRequestsDao pullRequestsDao;

    private PrSummaryDao prSummaryDao;

    private SonarConfigurationsRepository sonarConfigurationsRepository;

    private SonarStatsRepository sonarStatsRepository;

    private SonarConfigurationsDao sonarConfigurationsDao;

    private SprintSubmitterMetricsDao sprintSubmitterMetricsDao;

    private ScmConfigurationDao scmConfigurationDao;

    private ScmConfigurationRepository scmConfigurationRepository;

    @Override
    public PullRequestsDto getPrJourney(String projectId, String repoIds, String dayCount) {
        Project project = projectHelper.findAndValidateProjectByIdAndUser(projectId);

        ScmConfiguration scmConfiguration = scmConfigurationRepository.findByProjectId(new ObjectId(project.getId()));
        if (Objects.isNull(scmConfiguration)) {
            log.info("No SCM data available for {} project", project.getName());
            throw new RecordNotFoundException("No SCM data available");
        }
        List<PullRequestsCommitsVo> pullRequestsCommitsVos = pullRequestsDao.getPrData(projectId,
                DateTimeUtils.getPreviousNDaysDateAndTime(Integer.parseInt(dayCount) - 1),
                ZonedDateTime.now().toInstant().toEpochMilli(), csvToList(repoIds));
        if (CollectionUtils.isEmpty(pullRequestsCommitsVos)) {
            throw new RecordNotFoundException("No PR records found");
        }
        List<PullRequestDto> pullRequests = new ArrayList<>();
        pullRequestsCommitsVos.forEach(prCommit -> {
            String journeyDuration;
            if (prCommit.getPull_requests().getPrMergeTime() != null
                    && prCommit.getPull_requests().getPrMergeTime() != 0) {
                journeyDuration = getDurationInDays(prCommit.getPull_requests().getPrCreatedTime(),
                        prCommit.getPull_requests().getPrMergeTime());
            }
            else if (prCommit.getPull_requests().getPrDeclinedTime() != null
                    && prCommit.getPull_requests().getPrDeclinedTime() != 0) {
                journeyDuration = getDurationInDays(prCommit.getPull_requests().getPrCreatedTime(),
                        prCommit.getPull_requests().getPrDeclinedTime());
            }
            else {
                journeyDuration = getDurationInDays(prCommit.getPull_requests().getPrCreatedTime(),
                        scmConfiguration.getUpdatedDate());
            }
            pullRequests.add(PullRequestDto.builder()
                .id(prCommit.get_id().toString())
                .number(prCommit.getPull_requests().getPrNumber())
                .commitLog(prCommit.getPull_requests().getCommitLog())
                .createdDateTime(prCommit.getPull_requests().getPrCreatedTime())
                .journeyDuration(journeyDuration)
                .mergedDateTime(prCommit.getPull_requests().getPrMergeTime())
                .mergedBy(prCommit.getPull_requests().getPrMergedBy())
                .declinedDateTime(prCommit.getPull_requests().getPrDeclinedTime())
                .declinedBy(prCommit.getPull_requests().getPrDeclinedBy())
                .state(prCommit.getPull_requests().getState())
                .sourceBranch(prCommit.getPull_requests().getSourceBranch())
                .destinationBranch(prCommit.getPull_requests().getTargetBranch())
                .author(AuthorReviewerDto.builder()
                    .id(prCommit.getPull_requests().getScmAuthor() != null
                            ? prCommit.getPull_requests().getScmAuthor().getId() : null)
                    .name(prCommit.getPull_requests().getScmAuthor() != null
                            ? prCommit.getPull_requests().getScmAuthor().getName() : null)
                    .build())
                .approvers(CollectionUtils.isEmpty(prCommit.getPull_requests().getApprovers()) ? Collections.emptyList()
                        : prCommit.getPull_requests()
                            .getApprovers()
                            .stream()
                            .map(approvers -> ApproversDto.builder()
                                .id(approvers.getId())
                                .name(approvers.getName())
                                .time(approvers.getAt())
                                .build())
                            .collect(Collectors.toList()))
                .comments(CollectionUtils.isEmpty(prCommit.getPull_requests().getComments()) ? Collections.emptyList()
                        : prCommit.getPull_requests()
                            .getComments()
                            .stream()
                            .map(comment -> CommentsDto.builder()
                                .by(comment.getName())
                                .message(comment.getMessage())
                                .time(comment.getCommentedOn())
                                .build())
                            .collect(Collectors.toList()))
                .commits(CollectionUtils.isEmpty(prCommit.getPull_requests().getCommits()) ? Collections.emptyList()
                        : prCommit.getPull_requests()
                            .getCommits()
                            .stream()
                            .map(commit -> CommitsDto.builder()
                                .time(commit.getTime())
                                .revisionNumber(commit.getRevisionNumber())
                                .authorEmail(commit.getAuthorEmail())
                                .authorUserId(commit.getAuthorUserId())
                                .message(commit.getMessage())
                                .type(commit.getType())
                                .build())
                            .collect(Collectors.toList()))
                .reviewers(CollectionUtils.isEmpty(prCommit.getPull_requests().getReviewers()) ? Collections.emptyList()
                        : prCommit.getPull_requests()
                            .getReviewers()
                            .stream()
                            .map(reviewer -> AuthorReviewerDto.builder()
                                .name(reviewer.getName())
                                .id(reviewer.getId())
                                .build())
                            .collect(Collectors.toList()))
                .build());
        });
        return PullRequestsDto.builder()
            .lastUpdatedDate(scmConfiguration.getUpdatedDate())
            .pullRequests(pullRequests)
            .build();
    }

    @Override
    public SprintPullRequestSummaryDto getSprintPullRequestSummary(String projectId, String repoIds, String dayCount)
            throws UnauthorizedException, ForbiddenException, RecordNotFoundException {
        Project project = projectHelper.findAndValidateProjectByIdAndUser(projectId);
        ScmConfiguration scmConfiguration = scmConfigurationRepository.findByProjectId(new ObjectId(project.getId()));
        if (Objects.isNull(scmConfiguration)) {
            log.info("No SCM data available for {} project", project.getName());
            throw new RecordNotFoundException("No SCM data available");
        }
        SprintPullRequestSummaryVo sprintPullRequestSummaryVo = prSummaryDao.getSprintPullRequestSummary(projectId,
                csvToList(repoIds), DateTimeUtils.getPreviousNDaysDateAndTime(Integer.parseInt(dayCount) - 1),
                ZonedDateTime.now().toInstant().toEpochMilli(), scmConfiguration.getUpdatedDate());

        return SprintPullRequestSummaryDto.builder()
            .data(SprintPullRequestSummaryDataDto.builder()
                .lastUpdatedDate(scmConfiguration.getUpdatedDate())
                .total(sprintPullRequestSummaryVo.getTotal().intValue())
                .merged(sprintPullRequestSummaryVo.getMerged().intValue())
                .open(sprintPullRequestSummaryVo.getOpen().intValue())
                .declined(sprintPullRequestSummaryVo.getDeclined().intValue())
                .unattended(sprintPullRequestSummaryVo.getUnattended().intValue())
                .build())
            .build();
    }

    @Override
    public QualityMeasurementsDto getProjectQualityMeasurements(String projectId, String repoIds) {
        projectHelper.findAndValidateProjectByIdAndUser(projectId);
        SonarStateMeasuresAverageCalculationVo sonarStateMeasuresAverageCalculationVo = sonarConfigurationsDao
            .findQualityMeasurements(projectId, csvToList(repoIds));
        return QualityMeasurementsDto.builder()
            .efficiency(Double.parseDouble(
                    new DecimalFormat(DECIMAL_FORMAT).format(sonarStateMeasuresAverageCalculationVo.getEfficiency())))
            .robustness(Double.parseDouble(
                    new DecimalFormat(DECIMAL_FORMAT).format(sonarStateMeasuresAverageCalculationVo.getRobustness())))
            .security(Double.parseDouble(
                    new DecimalFormat(DECIMAL_FORMAT).format(sonarStateMeasuresAverageCalculationVo.getSecurity())))
            .build();
    }

    @Override
    public ViolationsSummaryDto getViolationsSummary(String projectId, String repoIds) {
        projectHelper.findAndValidateProjectByIdAndUser(projectId);
        LanguageSpecificViolationsVo languageSpecificViolations = scmConfigurationDao.getViolationsSummary(projectId,
                csvToList(repoIds));
        if (Objects.isNull(languageSpecificViolations)) {
            throw new RecordNotFoundException("project data not available");
        }
        return ViolationsSummaryDto.builder()
            .data(ViolationsDataDto.builder()
                .language(languageSpecificViolations.getLanguage())
                .violations(languageSpecificViolations.getViolations())
                .build())
            .build();
    }

    @Override
    public RiskMeasurementsDto getRiskMeasurements(String projectId, String repoIds, String dayCount) {
        projectHelper.findAndValidateProjectByIdAndUser(projectId);
        RiskMeasurementsVo riskMeasurementsVo = sonarConfigurationsDao.getRiskMeasurements(projectId,
                csvToList(repoIds), getPreviousNDaysDateAndTime(Integer.parseInt(dayCount) - 1),
                ZonedDateTime.now().toInstant().toEpochMilli());
        if (Objects.isNull(riskMeasurementsVo)) {
            throw new RecordNotFoundException("Record Not Found");
        }
        int current = 0;
        int previous = 0;

        for (ProjectRiskMeasurementVo projectRiskMeasurementVo : riskMeasurementsVo.getProject()) {
            if (projectRiskMeasurementVo.getCurrentData() != null) {
                current += projectRiskMeasurementVo.getCurrentData().getViolations().getCritical();
            }
            if (projectRiskMeasurementVo.getPreviousData() != null) {
                previous += projectRiskMeasurementVo.getPreviousData().getViolations().getCritical();
            }
        }
        return RiskMeasurementsDto.builder()
            .violations(RiskViolationsDto.builder()
                .added(Math.max(0, current - previous))
                .removed(Math.max(0, previous - current))
                .build())
            .build();
    }

    @Override
    public ComplianceAnalysisDto getComplianceAnalysis(String projectId, String repoIds) {
        projectHelper.findAndValidateProjectByIdAndUser(projectId);
        ComplianceAnalysisVO complianceAnalysis = sonarConfigurationsDao.getComplianceAnalysis(projectId,
                csvToList(repoIds));
        return ComplianceAnalysisDto.builder()
            .compliance(
                    Double.parseDouble(new DecimalFormat(DECIMAL_FORMAT).format(complianceAnalysis.getCompliance())))
            .build();
    }

    @Override
    public ReviewerMetricsDto getReviewerMetrics(String projectId, String repoIds, String dayCount)
            throws RecordNotFoundException {
        projectHelper.findAndValidateProjectByIdAndUser(projectId);
        List<PullRequestReviewersVo> prReviewersVos = pullRequestsDao.getReviewerMetrics(projectId,
                getPreviousNDaysDateAndTime(Integer.parseInt(dayCount) - 1),
                ZonedDateTime.now().toInstant().toEpochMilli(), csvToList(repoIds));
        if (CollectionUtils.isEmpty(prReviewersVos)) {
            throw new RecordNotFoundException("No Record Found for projectId");
        }
        List<ReviewersDto> reviewers = prReviewersVos.stream()
            .filter(Objects::nonNull)
            .map(this::getReviewer)
            .collect(Collectors.toList());
        int allTotalPrsTime = reviewers.stream().map(ReviewersDto::getTotalPrs).mapToInt(Integer::valueOf).sum();
        return ReviewerMetricsDto.builder()
            .averagePrTime(allTotalPrsTime != 0 ? concatHourToInteger(reviewers.stream()
                .map(ReviewersDto::getTotalReviewTime)
                .mapToInt(s -> Integer.parseInt(s.replace(HOURS, StringUtils.EMPTY).trim()))
                .sum() / allTotalPrsTime) : concatHourToInteger(0))
            .reviewers(reviewers)
            .build();
    }

    private ReviewersDto getReviewer(PullRequestReviewersVo pullRequestReviewer) {
        return ReviewersDto.builder()
            .name(pullRequestReviewer.get_id())
            .totalPrs(String.valueOf(pullRequestReviewer.getTotalPrs().intValue()))
            .totalReviewTime(concatHourToInteger((int) (pullRequestReviewer.getTotalTimeForMergedPr()
                    + pullRequestReviewer.getTotalTimeForDeclinedPr() + pullRequestReviewer.getTotalTimeForOpenPr())))
            .build();
    }

    @Override
    public QualityMetricsSummaryDto getQualityMetrics(String projectId, String repoIds) {
        projectHelper.findAndValidateProjectByIdAndUser(projectId);
        QualityMetricsVo qualityMetrics = scmConfigurationDao.getQualityMetrics(projectId, csvToList(repoIds));
        return QualityMetricsSummaryDto.builder()
            .overall(QualityOverallSummaryDto.builder()
                .bugs(qualityMetrics.getOverallBugs())
                .codeCoverage(ArithmeticUtils.round(qualityMetrics.getOverallCodeCoverage(), 2) + PERCENTAGE)
                .duplicationBlocks(qualityMetrics.getOverallDuplicationBlocks())
                .duplications(ArithmeticUtils.round(qualityMetrics.getOverallDuplications(), 2) + PERCENTAGE)
                .vulnerabilities(qualityMetrics.getOverallVulnerabilities())
                .build())
            .newSummary(QualityNewSummaryDto.builder()
                .duplicationLines(qualityMetrics.getNewDuplicationLines())
                .bugs(qualityMetrics.getNewBugs())
                .codeCoverage(ArithmeticUtils.round(qualityMetrics.getNewCodeCoverage(), 2) + PERCENTAGE)
                .vulnerabilities(qualityMetrics.getNewVulnerabilities())
                .duplications(ArithmeticUtils.round(qualityMetrics.getNewDuplications(), 2) + PERCENTAGE)
                .newLineOfCodes(qualityMetrics.getNewNewLineOfCodes())
                .build())
            .build();
    }

    @Override
    public CollaborationMetricDataDto getCollaborationMetrics(String projectId, String repoIds, String dayCount) {
        projectHelper.findAndValidateProjectByIdAndUser(projectId);
        List<PullRequestsCommitsVo> collaborationMetricsVo = pullRequestsDao.getPrData(projectId,
                DateTimeUtils.getPreviousNDaysDateAndTime(Integer.parseInt(dayCount) - 1),
                ZonedDateTime.now().toInstant().toEpochMilli(), csvToList(repoIds));
        if (CollectionUtils.isEmpty(collaborationMetricsVo)) {
            throw new RecordNotFoundException("PR data not found");
        }
        return CollaborationMetricDataDto.builder()
            .average(generateCollaborationMetrics(collaborationMetricsVo))
            .build();
    }

    private CollaborationMetricAverageDto generateCollaborationMetrics(
            List<PullRequestsCommitsVo> collaborationMetricsVo) {
        List<Integer> prResolveCount = new ArrayList<>();
        List<Long> prResolveCountAvg = new ArrayList<>();
        List<Double> firstCommentCount = new ArrayList<>();
        List<Long> firstCommentAvg = new ArrayList<>();
        List<Integer> commitCount = new ArrayList<>();
        List<Integer> avgCommitCount = new ArrayList<>();
        collaborationMetricsVo.stream().peek(collaborationVo -> {
            LocalDateTime prCreatedTime = Instant.ofEpochMilli(collaborationVo.getPull_requests().getPrCreatedTime())
                .atZone(ZoneOffset.UTC)
                .toLocalDateTime();
            if (CollectionUtils.isNotEmpty(collaborationVo.getPull_requests().getComments())) {
                List<Long> firstCommentList = new ArrayList<>();
                collaborationVo.getPull_requests()
                    .getComments()
                    .forEach(comment -> collaborationVo.getPull_requests().getReviewers().forEach(reviewer -> {
                        if (comment.getName().equalsIgnoreCase(reviewer.getName())) {
                            firstCommentList.add(comment.getCommentedOn());
                        }
                    }));
                if (CollectionUtils.isNotEmpty(firstCommentList)) {
                    Collections.sort(firstCommentList);
                    Long firstComment = firstCommentList.get(0);
                    LocalDateTime firstCommentTime = Instant.ofEpochMilli(firstComment)
                        .atZone(ZoneOffset.UTC)
                        .toLocalDateTime();
                    long avg = ChronoUnit.HOURS.between(prCreatedTime, firstCommentTime);
                    if (avg > 0) {
                        firstCommentCount.add(1.0);
                        firstCommentAvg.add(avg);
                    }
                }
            }
            List<Integer> commitCountList = new ArrayList<>();
            if (CollectionUtils.isNotEmpty(collaborationVo.getPull_requests().getCommits())) {
                collaborationVo.getPull_requests()
                    .getCommits()
                    .stream()
                    .filter(commit -> commit.getTime() > collaborationVo.getPull_requests().getPrCreatedTime())
                    .forEach(commit -> {
                        commitCountList.add(0, 1);
                        avgCommitCount.add(1);
                    });
                if (CollectionUtils.isNotEmpty(commitCountList)) {
                    commitCount.add(1);
                }
            }
        })
            .filter(collaborationVo -> collaborationVo.getPull_requests().getState().equalsIgnoreCase(MERGED)
                    && null != collaborationVo.getPull_requests().getPrMergeTime())
            .forEach(collaborationVo -> calculateAveragePrResolveTime(collaborationVo, prResolveCount,
                    prResolveCountAvg));
        return generateCollaborationMetricsDtoResponse(firstCommentCount, firstCommentAvg, prResolveCount,
                prResolveCountAvg, commitCount, avgCommitCount);
    }

    private void calculateAveragePrResolveTime(PullRequestsCommitsVo collaborationVo, List<Integer> prResolveCount,
            List<Long> prResolveCountAvg) {
        List<LocalDateTime> createdTime = new ArrayList<>();
        List<LocalDateTime> mergeTime = new ArrayList<>();
        createdTime.add(Instant.ofEpochMilli(collaborationVo.getPull_requests().getPrCreatedTime())
            .atZone(ZoneOffset.UTC)
            .toLocalDateTime());
        mergeTime.add(Instant.ofEpochMilli(collaborationVo.getPull_requests().getPrMergeTime())
            .atZone(ZoneOffset.UTC)
            .toLocalDateTime());
        long avg = ChronoUnit.MINUTES.between(createdTime.get(0), mergeTime.get(0));
        if (avg > 0) {
            prResolveCount.add(1);
            prResolveCountAvg.add(avg);
        }
    }

    private CollaborationMetricAverageDto generateCollaborationMetricsDtoResponse(List<Double> firstCommentCount,
            List<Long> firstCommentAvg, List<Integer> prResolveCount, List<Long> prResolveCountAvg,
            List<Integer> commitCount, List<Integer> avgCommitCount) {
        return CollaborationMetricAverageDto.builder()
            .firstComment(firstCommentCount.isEmpty() ? INTEGER_ZERO.toString()
                    : Double.parseDouble(new DecimalFormat(DECIMAL_FORMAT)
                        .format(firstCommentAvg.stream().mapToDouble(firstCommit -> firstCommit).sum()
                                / firstCommentCount.size()))
                            + HOURS)
            .prResolveTime(prResolveCount.isEmpty() ? INTEGER_ZERO.toString() + DAYS
                    : getDaysAndHoursFromMinutes((long) prResolveCountAvg.stream()
                        .mapToLong(prResolveTime -> prResolveTime)
                        .average()
                        .getAsDouble()))
            .recommit(commitCount.isEmpty() ? INTEGER_ZERO : avgCommitCount.size() / commitCount.size())
            .build();
    }

    @Override
    public SprintSubmitterMetricsResponseDto getSprintSubmitterMetrics(String projectId, String sortBy, String repoIds,
            String dayCount) throws UnauthorizedException, ForbiddenException, RecordNotFoundException {
        projectHelper.findAndValidateProjectByIdAndUser(projectId);
        List<SprintSubmitterMetricsVo> sprintPullRequestSummaryVos = sprintSubmitterMetricsDao
            .getSprintSubmitterMetrics(projectId, getPreviousNDaysDateAndTime(Integer.parseInt(dayCount) - 1),
                    ZonedDateTime.now().toInstant().toEpochMilli(),
                    StringUtils.isNotEmpty(sortBy) && Objects.nonNull(SubmitterMetricsSortFilterEnum.findByKey(sortBy))
                            ? SubmitterMetricsSortFilterEnum.findByKey(sortBy) : SubmitterMetricsSortFilterEnum.TOP,
                    csvToList(repoIds));
        if (CollectionUtils.isEmpty(sprintPullRequestSummaryVos)) {
            throw new RecordNotFoundException(RECORD_NOT_FOUND);
        }

        List<SubmitterMetricsDto> submitters = getSubmitters(sprintPullRequestSummaryVos);
        return SprintSubmitterMetricsResponseDto.builder()
            .data(SprintSubmitterMetricsDto.builder()
                .averagePrs(CollectionUtils.isNotEmpty(submitters) ? Double
                    .parseDouble(new DecimalFormat(DECIMAL_FORMAT).format(sprintPullRequestSummaryVos.stream()
                        .filter(sprintSubmitterMetricsVo -> Objects.nonNull(sprintSubmitterMetricsVo.getTotalPrs()))
                        .mapToDouble(SprintSubmitterMetricsVo::getTotalPrs)
                        .sum() / submitters.size())) : DOUBLE_ZERO)
                .submitters(submitters)
                .build())
            .build();
    }

    private List<SubmitterMetricsDto> getSubmitters(List<SprintSubmitterMetricsVo> sprintPullRequestSummaryVos) {
        return sprintPullRequestSummaryVos.stream()
            .map(sprintSubmitterMetricsVo -> SubmitterMetricsDto.builder()
                .name(sprintSubmitterMetricsVo.get_id())
                .totalPrs(sprintSubmitterMetricsVo.getTotalPrs().intValue())
                .openPrs(sprintSubmitterMetricsVo.getOpenPrs().intValue())
                .mergedPrs(sprintSubmitterMetricsVo.getMergedPrs().intValue())
                .declinedPrs(sprintSubmitterMetricsVo.getDeclinedPrs().intValue())
                .reviewerComments(sprintSubmitterMetricsVo.getReviewerComments().intValue())
                .recommits(sprintSubmitterMetricsVo.getRecommits().intValue())
                .averagePrCycleTime(new DecimalFormat(DECIMAL_FORMAT)
                    .format(sprintSubmitterMetricsVo.getTotalTimeForMergedPr().equals(0.0)
                            && sprintSubmitterMetricsVo.getTotalTimeForDeclinedPr().equals(0.0)
                                    ? 0.0
                                    : (sprintSubmitterMetricsVo.getTotalTimeForMergedPr()
                                            + sprintSubmitterMetricsVo.getTotalTimeForDeclinedPr())
                                            / (sprintSubmitterMetricsVo.getDeclinedPrs()
                                                    + sprintSubmitterMetricsVo.getMergedPrs()))
                    .concat(HOURS))
                .build())
            .toList();
    }

    @Override
    public TechnicalDebtMetricsDto getTechnicalDebtMetrics(String projectId, String repoId) {
        projectHelper.findAndValidateProjectByIdAndUser(projectId);
        TechnicalDebtVo technicalDebtVo = sonarConfigurationsDao.getTechnicalDebtSummary(projectId, csvToList(repoId));
        return TechnicalDebtMetricsDto.builder()
            .complexity(technicalDebtVo.getComplexity())
            .duplications(
                    Double.parseDouble(new DecimalFormat(DECIMAL_FORMAT).format(technicalDebtVo.getDuplication())))
            .issues(technicalDebtVo.getIssues())
            .maintainability(convertMinutesToDays(technicalDebtVo.getMaintainability()))
            .reliability(convertMinutesToDays(technicalDebtVo.getReliability()))
            .security(convertMinutesToDays(technicalDebtVo.getSecurity().longValue()))
            .size(technicalDebtVo.getSize())
            .coverage(Double.parseDouble(new DecimalFormat(DECIMAL_FORMAT).format(technicalDebtVo.getTests())))
            .build();
    }

    @Override
    public CodeMetricsDto getCodeMetrics(String projectId, String repoIds, String dayCount) {
        projectHelper.findAndValidateProjectByIdAndUser(projectId);
        List<CodeMetricsVo> codeMetricsVo = scmConfigurationDao.getMemberWiseCodeMetrics(projectId, csvToList(repoIds));
        if (CollectionUtils.isEmpty(codeMetricsVo)) {
            throw new RecordNotFoundException("No record available");
        }
        OverAllDto overAll = buildOverAllCodeMetrics(codeMetricsVo, dayCount);
        return CodeMetricsDto.builder()
            .overall(overAll)
            .members(codeMetricsVo.get(0)
                .getAuthorsData()
                .stream()
                .map(author -> CodeMetricsMembersDto.builder()
                    .name(author.get_id())
                    .lineOfCode(author.getLineOfCode())
                    .codeChurn(author.getCodeChurn())
                    .legacyRefactor(author.getLegacyRefactor())
                    .addedLineOfCode(fetchMemberwiseAddedLineOfCode(author, Integer.valueOf(dayCount)))
                    .removedLineOfCode(fetchMemberwiseRemovedLineOfCode(author, Integer.valueOf(dayCount)))
                    .addedLineOfCodeTillDate(author.getAddedLineOfCodeTillDate())
                    .removedLineOfCodeTillDate(author.getRemovedLineOfCodeTillDate())
                    .build())
                .collect(Collectors.toList()))
            .average(buildAverageCodeMetrics(codeMetricsVo, overAll))
            .build();
    }

    private Long fetchMemberwiseAddedLineOfCode(AuthorsDataVo author, Integer dayCount) {
        if (dayCount.equals(INTEGER_SEVEN)) {
            return author.getAddedLineOfCodeByAuthorInLast7Days();
        }
        else if (dayCount.equals(INTEGER_FIFTEEN)) {
            return author.getAddedLineOfCodeByAuthorInLast15Days();
        }
        else if (dayCount.equals(INTEGER_THIRTY)) {
            return author.getAddedLineOfCodeByAuthorInLast30Days();
        }
        else if (dayCount.equals(INTEGER_NINETY)) {
            return author.getAddedLineOfCodeByAuthorInLast90Days();
        }
        return 0L;
    }

    private Long fetchMemberwiseRemovedLineOfCode(AuthorsDataVo author, Integer dayCount) {
        if (dayCount.equals(INTEGER_SEVEN)) {
            return author.getRemovedLineOfCodeByAuthorInLast7Days();
        }
        else if (dayCount.equals(INTEGER_FIFTEEN)) {
            return author.getRemovedLineOfCodeByAuthorInLast15Days();
        }
        else if (dayCount.equals(INTEGER_THIRTY)) {
            return author.getRemovedLineOfCodeByAuthorInLast30Days();
        }
        else if (dayCount.equals(INTEGER_NINETY)) {
            return author.getAddedLineOfCodeByAuthorInLast90Days();
        }
        return 0L;
    }

    private Long fetchAddedLineOfCodeByDayCount(List<CodeMetricsVo> codeMetrics, String dayCount) {
        long addedLineOfCode = 0L;
        if (Integer.valueOf(dayCount).equals(INTEGER_SEVEN)) {
            addedLineOfCode = codeMetrics.get(0)
                .getAuthorsData()
                .stream()
                .mapToLong(AuthorsDataVo::getAddedLineOfCodeByAuthorInLast7Days)
                .sum();

        }
        else if (Integer.valueOf(dayCount).equals(INTEGER_FIFTEEN)) {
            addedLineOfCode = codeMetrics.get(0)
                .getAuthorsData()
                .stream()
                .mapToLong(AuthorsDataVo::getAddedLineOfCodeByAuthorInLast15Days)
                .sum();
        }
        else if (Integer.valueOf(dayCount).equals(INTEGER_THIRTY)) {
            addedLineOfCode = codeMetrics.get(0)
                .getAuthorsData()
                .stream()
                .mapToLong(AuthorsDataVo::getAddedLineOfCodeByAuthorInLast30Days)
                .sum();
        }
        else if (Integer.valueOf(dayCount).equals(INTEGER_NINETY)) {
            addedLineOfCode = codeMetrics.get(0)
                .getAuthorsData()
                .stream()
                .mapToLong(AuthorsDataVo::getAddedLineOfCodeByAuthorInLast90Days)
                .sum();
        }
        return addedLineOfCode;
    }

    private Long fetchRemovedLineOfCodeByDayCount(List<CodeMetricsVo> codeMetrics, String dayCount) {
        long removedLineOfCode = 0L;
        if (Integer.valueOf(dayCount).equals(INTEGER_SEVEN)) {
            removedLineOfCode = codeMetrics.get(0)
                .getAuthorsData()
                .stream()
                .mapToLong(AuthorsDataVo::getRemovedLineOfCodeByAuthorInLast7Days)
                .sum();
        }
        else if (Integer.valueOf(dayCount).equals(INTEGER_FIFTEEN)) {
            removedLineOfCode = codeMetrics.get(0)
                .getAuthorsData()
                .stream()
                .mapToLong(AuthorsDataVo::getRemovedLineOfCodeByAuthorInLast15Days)
                .sum();
        }
        else if (Integer.valueOf(dayCount).equals(INTEGER_THIRTY)) {
            removedLineOfCode = codeMetrics.get(0)
                .getAuthorsData()
                .stream()
                .mapToLong(AuthorsDataVo::getRemovedLineOfCodeByAuthorInLast30Days)
                .sum();
        }
        else if (Integer.valueOf(dayCount).equals(INTEGER_NINETY)) {
            removedLineOfCode = codeMetrics.get(0)
                .getAuthorsData()
                .stream()
                .mapToLong(AuthorsDataVo::getRemovedLineOfCodeByAuthorInLast90Days)
                .sum();
        }
        return removedLineOfCode;
    }

    private OverAllDto buildAverageCodeMetrics(List<CodeMetricsVo> codeMetrics, OverAllDto overAll) {
        int members = codeMetrics.get(0).getAuthorsData().size();
        return OverAllDto.builder()
            .lineOfCode(overAll.getLineOfCode() / members)
            .codeChurn(overAll.getCodeChurn() / members)
            .legacyRefactor(overAll.getLegacyRefactor() / members)
            .addedLineOfCode(overAll.getAddedLineOfCode() / members)
            .removedLineOfCode(overAll.getRemovedLineOfCode() / members)
            .addedLineOfCodeTotal(overAll.getAddedLineOfCodeTotal() / members)
            .removedLineOfCodeTotal(overAll.getRemovedLineOfCodeTotal() / members)
            .build();
    }

    private OverAllDto buildOverAllCodeMetrics(List<CodeMetricsVo> codeMetrics, String dayCount) {
        return OverAllDto.builder()
            .lineOfCode(codeMetrics.get(0).getTotalLineOfCode().longValue())
            .codeChurn(codeMetrics.get(0).getAuthorsData().stream().mapToLong(AuthorsDataVo::getCodeChurn).sum())
            .legacyRefactor(
                    codeMetrics.get(0).getAuthorsData().stream().mapToLong(AuthorsDataVo::getLegacyRefactor).sum())
            .addedLineOfCode(fetchAddedLineOfCodeByDayCount(codeMetrics, dayCount))
            .removedLineOfCode(fetchRemovedLineOfCodeByDayCount(codeMetrics, dayCount))
            .addedLineOfCodeTotal(codeMetrics.get(0)
                .getAuthorsData()
                .stream()
                .mapToLong(AuthorsDataVo::getAddedLineOfCodeTillDate)
                .sum())
            .removedLineOfCodeTotal(codeMetrics.get(0)
                .getAuthorsData()
                .stream()
                .mapToLong(AuthorsDataVo::getRemovedLineOfCodeTillDate)
                .sum())
            .build();
    }

    @Override
    public ActivityMetricsDto getMemberWiseActivityMetrics(String projectId, String repoIds, String dayCount) {
        projectHelper.findAndValidateProjectByIdAndUser(projectId);
        List<MemberWiseActivityMetricsVo> latestActivityDataVo = pullRequestsDao.getMembersActivityMetrics(projectId,
                getPreviousNDaysDateAndTime(Integer.parseInt(dayCount) - 1),
                ZonedDateTime.now().toInstant().toEpochMilli(), csvToList(repoIds));
        List<MonthWiseDto> latestSprint = new ArrayList<>();
        latestSprint.add(MonthWiseDto.builder()
            .dateRange(getDateRange(getPreviousNDaysDateAndTime(Integer.parseInt(dayCount) - 1),
                    ZonedDateTime.now().toInstant().toEpochMilli()))
            .members(getMembersPrData(latestActivityDataVo))
            .build());
        List<MemberWiseActivityMetricsVo> previousActivityData = pullRequestsDao.getMembersActivityMetrics(projectId,
                getPreviousNDaysDateAndTime(Integer.parseInt(dayCount) + Integer.parseInt(dayCount) - 1),
                getPreviousNDaysDateAndTime(Integer.parseInt(dayCount) - 1), csvToList(repoIds));
        latestSprint.add(MonthWiseDto.builder()
            .dateRange(getDateRange(
                    getPreviousNDaysDateAndTime(Integer.parseInt(dayCount) + Integer.parseInt(dayCount) - 1),
                    getPreviousNDaysDateAndTime(Integer.parseInt(dayCount) - 1)))
            .members(getMembersPrData(previousActivityData))
            .build());
        checkForEmptyMembers(latestSprint);

        if (CollectionUtils.isEmpty(previousActivityData) && CollectionUtils.isEmpty(latestActivityDataVo)) {
            throw new RecordNotFoundException(RECORD_NOT_FOUND);
        }
        return ActivityMetricsDto.builder().months(latestSprint).build();
    }

    private void checkForEmptyMembers(List<MonthWiseDto> sprints) {
        if (sprints.size() > 1) {
            if (CollectionUtils.isEmpty(sprints.get(0).getMembers())
                    && CollectionUtils.isEmpty(sprints.get(1).getMembers())) {
                throw new RecordNotFoundException("No data available");
            }
        }
        else if (sprints.size() == 1 && CollectionUtils.isEmpty(sprints.get(0).getMembers())) {
            throw new RecordNotFoundException("No data available");
        }
    }

    private List<MemberDto> getMembersPrData(List<MemberWiseActivityMetricsVo> activityData) {
        List<MemberDto> members = new ArrayList<>();
        activityData.forEach(metric -> {
            Optional<MemberDto> existingMember = members.stream()
                .filter(memberData -> memberData.getName().equalsIgnoreCase(metric.get_id()))
                .findAny();
            if (existingMember.isPresent()) {
                updateMember(metric, existingMember.get());
            }
            else {
                members.add(buildMember(metric));
            }
        });
        return members;
    }

    private MemberDto buildMember(MemberWiseActivityMetricsVo metric) {
        return MemberDto.builder()
            .name(metric.get_id())
            .totalPrs((int) (metric.getTotalPrs() == null ? INTEGER_ZERO : metric.getTotalPrs()))
            .codeCommits(metric.getCodeCommits() == null ? INTEGER_ZERO : metric.getCodeCommits())
            .mergedPrs((int) (metric.getMergedPrs() == null ? INTEGER_ZERO : metric.getMergedPrs()))
            .reviewCommentsOnOthersPrs((int) (metric.getReviewCommentsOnOthersPrs() == null ? INTEGER_ZERO
                    : metric.getReviewCommentsOnOthersPrs()))
            .build();
    }

    private void updateMember(MemberWiseActivityMetricsVo metric, MemberDto member) {
        member.setCodeCommits(
                member.getCodeCommits() + (metric.getCodeCommits() == null ? INTEGER_ZERO : metric.getCodeCommits()));
        member.setMergedPrs(
                (int) (member.getMergedPrs() + (metric.getMergedPrs() == null ? INTEGER_ZERO : metric.getMergedPrs())));
        member.setReviewCommentsOnOthersPrs(
                (int) (member.getReviewCommentsOnOthersPrs() + (metric.getReviewCommentsOnOthersPrs() == null
                        ? INTEGER_ZERO : metric.getReviewCommentsOnOthersPrs())));
        member.setTotalPrs(
                (int) (member.getTotalPrs() + (metric.getTotalPrs() == null ? INTEGER_ZERO : metric.getTotalPrs())));
    }

}
