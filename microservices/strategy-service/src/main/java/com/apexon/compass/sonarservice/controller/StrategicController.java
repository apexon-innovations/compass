package com.apexon.compass.sonarservice.controller;

import com.apexon.compass.exception.custom.RecordNotFoundException;
import com.apexon.compass.exception.custom.UnauthorizedException;
import com.apexon.compass.exception.handler.ApiExceptionHandler;
import com.apexon.compass.sonarservice.dto.*;
import com.apexon.compass.sonarservice.service.StrategicService;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import static com.apexon.compass.constants.PsrServiceConstants.*;
import static com.apexon.compass.constants.PsrServiceRouteConstants.PROJECT;
import static com.apexon.compass.constants.StrategyServiceConstants.INVALID_SORT_BY_VALUE;
import static com.apexon.compass.constants.StrategyServiceConstants.SPRINT_SUBMITTER_METRICS_SORT_BY_PATTERN;
import static com.apexon.compass.constants.StrategyServiceRouteConstants.*;

@RestController
@AllArgsConstructor
@RequestMapping(value = PROJECT)
@Validated
public class StrategicController extends ApiExceptionHandler {

    private StrategicService strategicService;

    @GetMapping(value = PR_JOURNEY)
    public PullRequestsDto getPrJourney(
            @Pattern(regexp = PROJECT_ID_VALIDATION_REGEX,
                    message = PROJECT_ID_VALIDATION_MESSAGE) @NonNull @RequestParam String projectId,
            @RequestParam String repoIds, @RequestParam String dayCount) {
        return strategicService.getPrJourney(projectId, repoIds, dayCount);
    }

    @GetMapping(value = PROJECT_QUALITY_MEASUREMENTS)
    public QualityMeasurementsDto getProjectQualityMeasurements(
            @Pattern(regexp = PROJECT_ID_VALIDATION_REGEX,
                    message = PROJECT_ID_VALIDATION_MESSAGE) @NonNull @RequestParam String projectId,
            @RequestParam String repoIds) {
        return strategicService.getProjectQualityMeasurements(projectId, repoIds);
    }

    @GetMapping(path = SPRINT_PULL_REQUEST_SUMMARY)
    public SprintPullRequestSummaryDto getSprintPullRequestSummary(
            @Pattern(regexp = PROJECT_ID_VALIDATION_REGEX,
                    message = PROJECT_ID_VALIDATION_MESSAGE) @NonNull @RequestParam String projectId,
            @RequestParam String repoIds, @RequestParam String dayCount)
            throws UnauthorizedException, RecordNotFoundException {
        return strategicService.getSprintPullRequestSummary(projectId, repoIds, dayCount);
    }

    @GetMapping(value = VIOLATIONS_SUMMARY)
    public ViolationsSummaryDto getViolationsSummary(
            @Pattern(regexp = PROJECT_ID_VALIDATION_REGEX,
                    message = PROJECT_ID_VALIDATION_MESSAGE) @NonNull @RequestParam String projectId,
            @RequestParam String repoIds) {
        return strategicService.getViolationsSummary(projectId, repoIds);
    }

    @GetMapping(path = GET_RISK)
    public RiskMeasurementsDto getRiskMeasurements(
            @Pattern(regexp = PROJECT_ID_VALIDATION_REGEX,
                    message = PROJECT_ID_VALIDATION_MESSAGE) @NonNull @RequestParam String projectId,
            @RequestParam String repoIds, @RequestParam String dayCount) {
        return strategicService.getRiskMeasurements(projectId, repoIds, dayCount);
    }

    @GetMapping(path = SPRINT_REVIEWER_METRICS)
    public ReviewerMetricsDto getReviewerMetrics(
            @Pattern(regexp = PROJECT_ID_VALIDATION_REGEX,
                    message = PROJECT_ID_VALIDATION_MESSAGE) @NonNull @RequestParam String projectId,
            @RequestParam String repoIds, @RequestParam String dayCount) throws RecordNotFoundException {
        return strategicService.getReviewerMetrics(projectId, repoIds, dayCount);
    }

    @GetMapping(path = GET_QUALITY_SUMMARY)
    public QualityMetricsSummaryDto getQualityMetrics(
            @Pattern(regexp = PROJECT_ID_VALIDATION_REGEX,
                    message = PROJECT_ID_VALIDATION_MESSAGE) @NonNull @RequestParam String projectId,
            @RequestParam String repoIds) throws RecordNotFoundException {
        return strategicService.getQualityMetrics(projectId, repoIds);
    }

    @GetMapping(path = SPRINT_SUBMITTER_METRICS)
    public SprintSubmitterMetricsResponseDto getSprintSubmitterMetrics(
            @Pattern(regexp = PROJECT_ID_VALIDATION_REGEX,
                    message = PROJECT_ID_VALIDATION_MESSAGE) @RequestParam String projectId,
            @Pattern(regexp = SPRINT_SUBMITTER_METRICS_SORT_BY_PATTERN,
                    message = INVALID_SORT_BY_VALUE) @RequestParam(required = false) String sortBy,
            @RequestParam String repoIds, @RequestParam String dayCount)
            throws UnauthorizedException, RecordNotFoundException {
        return strategicService.getSprintSubmitterMetrics(projectId, sortBy, repoIds, dayCount);
    }

    @GetMapping(value = COLLABORATION_METRICS)
    public CollaborationMetricDataDto getCollaborationMetrics(
            @Pattern(regexp = PROJECT_ID_VALIDATION_REGEX,
                    message = PROJECT_ID_VALIDATION_MESSAGE) @RequestParam String projectId,
            @RequestParam String repoIds, @RequestParam String dayCount) {
        return strategicService.getCollaborationMetrics(projectId, repoIds, dayCount);
    }

    @GetMapping(path = TECHNICAL_DEBT_METRICS)
    public TechnicalDebtMetricsDto getTechnicalDebtMetrics(
            @Pattern(regexp = PROJECT_ID_VALIDATION_REGEX,
                    message = PROJECT_ID_VALIDATION_MESSAGE) @RequestParam String projectId,
            @RequestParam String repoIds) throws RecordNotFoundException {
        return strategicService.getTechnicalDebtMetrics(projectId, repoIds);
    }

    @GetMapping(path = CODE_METRICS)
    public CodeMetricsDto getCodeMetrics(
            @Pattern(regexp = PROJECT_ID_VALIDATION_REGEX,
                    message = PROJECT_ID_VALIDATION_MESSAGE) @RequestParam String projectId,
            @RequestParam String repoIds, @RequestParam String dayCount) {
        return strategicService.getCodeMetrics(projectId, repoIds, dayCount);
    }

    @GetMapping(path = GET_COMPLIANCE_ANALYSIS)
    public ComplianceAnalysisDto getComplianceAnalysis(
            @Pattern(regexp = PROJECT_ID_VALIDATION_REGEX,
                    message = PROJECT_ID_VALIDATION_MESSAGE) @RequestParam String projectId,
            @RequestParam String repoIds) throws RecordNotFoundException {
        return strategicService.getComplianceAnalysis(projectId, repoIds);
    }

    @GetMapping(value = MEMBERWISE_ACTIVITY_METRICS)
    public ActivityMetricsDto getMemberWiseActivityMetrics(
            @Pattern(regexp = PROJECT_ID_VALIDATION_REGEX,
                    message = PROJECT_ID_VALIDATION_MESSAGE) @RequestParam String projectId,
            @RequestParam String repoIds, @RequestParam String dayCount) {
        return strategicService.getMemberWiseActivityMetrics(projectId, repoIds, dayCount);
    }

}
