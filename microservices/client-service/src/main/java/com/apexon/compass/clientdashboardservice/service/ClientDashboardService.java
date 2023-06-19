package com.apexon.compass.clientdashboardservice.service;

import com.apexon.compass.clientdashboardservice.dto.*;

public interface ClientDashboardService {

    StoryPointsDeliversDto getStoryPointsDelivered(String email, String iscProjectIds, Integer sprintNumber);

    ProjectIdAndSprintDtos getVelocityTrends(String iscProjectIds, Integer sprintCount, String emailAddress);

    StoryPointsTrendsDto getStoryPointsDeliveryTrendForProject(String emailAddress, String iscProjectId,
            String sprintNumbers);

    ProjectIdAndSprintDataDtos getConsolidatedDefectRatio(String iscProjectIds, String emailAddress,
            String sprintNumbers);

    ProjectStoryPointDtos getProjectStoryPointDefectRatio(String iscProjectIds, String sprintNumbers);

    ProductCompletionTrendsDto getProductCompletionTrend(String email, String iscProjectIds, Integer sprintCount);

    DefectSnapshotDto getConsolidatedDefectSnapshot(String emailAddress, String iscProjectIds);

    ProductHealthOverviewDto getProductHealthOverview(String emailAddress, String iscProjectIds);

    CodeScoreDataDto getCodeScore(String emailAddress, String iscProjectId, String repoId, Integer monthCount);

    CodeMetricsDataDto getCodeMetrics(String emailAddress, String iscProjectId, String repoId, int monthCount);

    CodeViolationsDataDto getCodeViolations(String emailAddress, String iscProjectId, String repoId, int monthCount);

    CodeHealthDataDto getCodeHealthSnapShot(String emailAddress);

    TechDebtDto getTechnicalDebt(String emailAddress, String iscProjectId, String repoId, Integer monthCount);

    DefectAgeingTrendsDataDto getDefectAgeingTrends(String emailAddress, String iscProjectIds);

    DefectTrendsTypeDataDto getDefectTrendsDefectType(String emailAddress, String iscProjectIds);

    DefectTrendsSeverityDataDto getDefectTrendsDefectSeverity(String emailAddress, String iscProjectIds);

    DefectTrendsAcceptanceDataDto getDefectTrendsDefectAcceptance(String emailAddress, String iscProjectIds);

    DefectTrendAcceptanceStatsDataDto getDefectTrendDefectAcceptanceStats(String emailAddress, String iscProjectId,
            Integer monthCount);

    DefectTrendsTypeStatsDataDto getDefectTrendsDefectTypeStats(String emailAddress, String iscProjectId,
            Integer monthCount);

    DefectAgeingStatsDto getDefectAgeingStats(String emailAddress, String iscProjectId);

    DefectTrendSeverityStatsDataDto getDefectTrendDefectSeverityStats(String emailAddress, String iscProjectId,
            Integer month);

}
