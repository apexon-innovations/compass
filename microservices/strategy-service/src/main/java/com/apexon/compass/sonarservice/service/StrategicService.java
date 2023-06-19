package com.apexon.compass.sonarservice.service;

import com.apexon.compass.sonarservice.dto.ActivityMetricsDto;
import com.apexon.compass.sonarservice.dto.CodeMetricsDto;
import com.apexon.compass.sonarservice.dto.CollaborationMetricDataDto;
import com.apexon.compass.sonarservice.dto.ComplianceAnalysisDto;
import com.apexon.compass.sonarservice.dto.PullRequestsDto;
import com.apexon.compass.sonarservice.dto.QualityMeasurementsDto;
import com.apexon.compass.sonarservice.dto.QualityMetricsSummaryDto;
import com.apexon.compass.sonarservice.dto.ReviewerMetricsDto;
import com.apexon.compass.sonarservice.dto.RiskMeasurementsDto;
import com.apexon.compass.sonarservice.dto.SprintPullRequestSummaryDto;
import com.apexon.compass.sonarservice.dto.SprintSubmitterMetricsResponseDto;
import com.apexon.compass.sonarservice.dto.TechnicalDebtMetricsDto;
import com.apexon.compass.sonarservice.dto.ViolationsSummaryDto;

public interface StrategicService {

    SprintPullRequestSummaryDto getSprintPullRequestSummary(String projectId, String repoId, String dayCount);

    QualityMetricsSummaryDto getQualityMetrics(String projectId, String repoIds);

    RiskMeasurementsDto getRiskMeasurements(String projectId, String repoIds, String dayCount);

    PullRequestsDto getPrJourney(String projectId, String repoId, String dayCount);

    QualityMeasurementsDto getProjectQualityMeasurements(String projectId, String repoIds);

    ViolationsSummaryDto getViolationsSummary(String projectId, String repoIds);

    ReviewerMetricsDto getReviewerMetrics(String projectId, String repoIds, String dayCount);

    CollaborationMetricDataDto getCollaborationMetrics(String projectId, String repoId, String dayCount);

    SprintSubmitterMetricsResponseDto getSprintSubmitterMetrics(String projectId, String sortBy, String repoIds,
            String dayCount);

    TechnicalDebtMetricsDto getTechnicalDebtMetrics(String projectId, String repoId);

    CodeMetricsDto getCodeMetrics(String projectId, String repoIds, String dayCount);

    ComplianceAnalysisDto getComplianceAnalysis(String projectId, String repoIds);

    ActivityMetricsDto getMemberWiseActivityMetrics(String projectId, String repoId, String dayCount);

}
