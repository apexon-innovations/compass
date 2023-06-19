package com.apexon.compass.clientdashboardservice.controller;

import com.apexon.compass.clientdashboardservice.dto.*;
import com.apexon.compass.clientdashboardservice.service.ClientDashboardService;
import com.apexon.compass.exception.custom.BadRequestException;
import com.apexon.compass.exception.handler.ApiExceptionHandler;
import jakarta.annotation.security.RolesAllowed;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import static com.apexon.compass.constants.ClientDashboardRouteConstants.*;
import static com.apexon.compass.constants.ClientDashboardServiceConstants.X_USER_HEADER;

@RestController
@RequestMapping(value = PROJECTS)
@AllArgsConstructor
@Validated
public class ClientController extends ApiExceptionHandler {

    private ClientDashboardService clientDashboardService;

    // TODO should have access to widget named "client_operational_velocityTrends" so map
    // roles accordingly
    @RolesAllowed({ "admin", "manager" })
    @GetMapping(path = VELOCITY_TRENDS)
    public ProjectIdAndSprintDtos getVelocityTrends(
            @RequestHeader(value = X_USER_HEADER, required = false) String emailAddress,
            @RequestParam String projectIds, @RequestParam Integer sprintCount) {
        return clientDashboardService.getVelocityTrends(projectIds, sprintCount, emailAddress);
    }

    // TODO should have access to widget name
    // "client_operational_storyPointsDeliveryTrends" so map roles accordingly
    @RolesAllowed({ "admin", "manager" })
    @GetMapping(path = STORY_POINTS_DELIVERY_CONSOLIDATED)
    public StoryPointsDeliversDto getStoryPointsDeliveryTrend(
            @RequestHeader(value = X_USER_HEADER, required = false) String email, @RequestParam String projectIds,
            @RequestParam Integer sprintNumber) {
        return clientDashboardService.getStoryPointsDelivered(email, projectIds, sprintNumber);
    }

    // TODO should have access to widget named
    // "client_operational_storyPointsDeliveryTrends" so map role accordingly
    @RolesAllowed({ "admin", "manager" })
    @GetMapping(path = STORY_POINT_DELIVERY_TRENDS)
    public StoryPointsTrendsDto getStoryPointsDeliveryTrendForProject(
            @RequestHeader(value = X_USER_HEADER, required = false) String email, @RequestParam String projectId,
            @RequestParam String sprintCount) {
        return clientDashboardService.getStoryPointsDeliveryTrendForProject(email, projectId, sprintCount);
    }

    // TODO should have access to widget name "client_operational_storyPointsDefectsRatio"
    // so map roles accordingly
    @GetMapping(path = CONSOLIDATED_STORY_DEFECT_RATIO)
    public ProjectIdAndSprintDataDtos getConsolidatedDefectDefectRatio(
            @RequestHeader(value = X_USER_HEADER, required = false) String emailAddress,
            @RequestParam String projectIds, @RequestParam String sprintCount) {
        return clientDashboardService.getConsolidatedDefectRatio(projectIds, emailAddress, sprintCount);
    }

    // TODO should have access to widget name "client_operational_storyPointsDefectsRatio"
    // so map roles accordingly
    @RolesAllowed({ "admin", "manager" })
    @GetMapping(path = STORY_POINT_DEFECT_RATIO)
    public ProjectStoryPointDtos getProjectStoryPointDefectRatio(@RequestParam String projectId,
            @RequestParam String sprintCount) {
        return clientDashboardService.getProjectStoryPointDefectRatio(projectId, sprintCount);
    }

    // TODO should have access to widget name "client_operational_projectCompletionTrends"
    // so map roles accordingly
    @RolesAllowed({ "admin", "manager" })
    @GetMapping(path = PRODUCT_COMPLETION_TREND)
    public ProductCompletionTrendsDto getProductCompletionTrend(
            @RequestHeader(value = X_USER_HEADER, required = false) String email, @RequestParam String projectIds,
            @RequestParam(required = false) Integer sprintCount) {
        return clientDashboardService.getProductCompletionTrend(email, projectIds, sprintCount);
    }

    // TODO should have access to widget name "client_operational_openDefectsTrends" so
    // map roles accordingly
    @RolesAllowed({ "admin", "manager" })
    @GetMapping(path = CONSOLIDATED_DEFECT_SANAPSHOT)
    public DefectSnapshotDto getConsolidatedDefectSnapShot(
            @RequestHeader(value = X_USER_HEADER, required = false) String emailAddress,
            @RequestParam String projectIds) {
        return clientDashboardService.getConsolidatedDefectSnapshot(emailAddress, projectIds);
    }

    // TODO should have access to widget name "client_operational_productHealthOverview"
    // so map roles accordingly
    @RolesAllowed({ "admin", "manager" })
    @GetMapping(path = PRODUCT_HEALTH_OVERVIEW)
    public ProductHealthOverviewDto getProductHealthOverview(
            @RequestHeader(value = X_USER_HEADER, required = false) String emailAddress,
            @RequestParam String projectIds) {
        return clientDashboardService.getProductHealthOverview(emailAddress, projectIds);
    }

    // TODO should have access to widget name "client_business_codeHealth" so map roles
    // accordingly
    @GetMapping(path = CODE_HEALTH_SCORE)
    public CodeScoreDataDto getCodeScore(@RequestHeader(value = X_USER_HEADER, required = false) String emailAddress,
            @RequestParam String projectId, @RequestParam(required = false) String repoId,
            @RequestParam @NotNull int monthCount) {
        return clientDashboardService.getCodeScore(emailAddress, projectId, repoId, monthCount);
    }

    // TODO should have access to widget name "client_business_codeHealth" so map roles
    // accordingly
    @RolesAllowed({ "admin", "manager" })
    @GetMapping(path = CODE_METRICS)
    public CodeMetricsDataDto getCodeMetrics(
            @RequestHeader(value = X_USER_HEADER, required = false) String emailAddress, @RequestParam String projectId,
            @RequestParam(required = false) String repoId, @RequestParam @NotNull int monthCount) {
        return clientDashboardService.getCodeMetrics(emailAddress, projectId, repoId, monthCount);
    }

    // TODO should have access to widget name "client_business_codeHealth" so map roles
    // accordingly
    @RolesAllowed({ "admin", "manager" })
    @GetMapping(path = CODE_VIOLATIONS)
    public CodeViolationsDataDto getCodeViolations(
            @RequestHeader(value = X_USER_HEADER, required = false) String emailAddress, @RequestParam String projectId,
            @RequestParam(required = false) String repoId, @RequestParam int monthCount) {
        return clientDashboardService.getCodeViolations(emailAddress, projectId, repoId, monthCount);
    }

    // TODO should have access to widget name "client_business_codeHealth" so map roles
    // accordingly
    @RolesAllowed({ "admin", "manager" })
    @GetMapping(path = CODE_HEALTH)
    public CodeHealthDataDto getCodeViolations(@RequestHeader(value = X_USER_HEADER) String emailAddress) {
        return clientDashboardService.getCodeHealthSnapShot(emailAddress);
    }

    // client_business_codeHealth
    // TODO should have access to widget name "client_operational_projectCompletionTrends"
    // so map roles accordingly
    @RolesAllowed({ "admin", "manager" })
    @GetMapping(path = CODE_TECH_DEBT)
    public TechDebtDto getTechnicalDebt(@RequestHeader(value = X_USER_HEADER, required = false) String emailAddress,
            @RequestParam String projectId, @RequestParam(required = false) String repoId,
            @RequestParam @NotNull int monthCount) {
        return clientDashboardService.getTechnicalDebt(emailAddress, projectId, repoId, monthCount);
    }

    // TODO should have access to widget name "client_operational_defectAgeing" so map
    // roles accordingly
    @RolesAllowed({ "admin", "manager" })
    @GetMapping(path = DEFECT_AGEING_TRENDS)
    public DefectAgeingTrendsDataDto getDefectAgeingTrends(
            @RequestHeader(value = X_USER_HEADER, required = false) String emailAddress,
            @RequestParam String projectIds) {
        return clientDashboardService.getDefectAgeingTrends(emailAddress, projectIds);
    }

    // TODO should have access to widget name "client_operational_defectAgeing" so map
    // roles accordingly
    @RolesAllowed({ "admin", "manager" })
    @GetMapping(path = DEFECT_AGEING_STATS)
    public DefectAgeingStatsDto getDefectAgeingStats(
            @RequestHeader(value = X_USER_HEADER, required = false) String emailAddress,
            @RequestParam String projectId) {
        return clientDashboardService.getDefectAgeingStats(emailAddress, projectId);
    }

    @GetMapping(path = DEFECT_TRENDS)
    public DefectTrendsTypeDataDto getDefectTrendsDefectType(
            @RequestHeader(value = X_USER_HEADER, required = false) String emailAddress,
            @NotBlank @RequestParam String projectIds) {
        return clientDashboardService.getDefectTrendsDefectType(emailAddress, projectIds);
    }

    @GetMapping(path = DEFECT_TRENDS_SEVERITY)
    public DefectTrendsSeverityDataDto getDefectTrendsDefectSeverity(
            @RequestHeader(value = X_USER_HEADER, required = false) String emailAddress,
            @RequestParam String projectIds) {
        return clientDashboardService.getDefectTrendsDefectSeverity(emailAddress, projectIds);
    }

    @GetMapping(path = DEFECT_TRENDS_STATS)
    public DefectTrendsTypeStatsDataDto getDefectTrendsDefectTypeStats(
            @RequestHeader(value = X_USER_HEADER, required = false) String emailAddress, @RequestParam String projectId,
            @RequestParam Integer month) {
        return clientDashboardService.getDefectTrendsDefectTypeStats(emailAddress, projectId, month);
    }

    @GetMapping(path = DEFECT_TREND_SEVERITY_STATS)
    public DefectTrendSeverityStatsDataDto getDefectTrendDefectSeverityStats(
            @RequestHeader(value = X_USER_HEADER, required = false) String emailAddress, @RequestParam String projectId,
            @RequestParam Integer month) {
        return clientDashboardService.getDefectTrendDefectSeverityStats(emailAddress, projectId, month);
    }

    @GetMapping(path = DEFECT_TRENDS_ACCEPTANCE)
    public DefectTrendsAcceptanceDataDto getDefectTrendsDefectAcceptance(
            @RequestHeader(value = X_USER_HEADER, required = false) String emailAddress,
            @RequestParam String projectIds) {
        return clientDashboardService.getDefectTrendsDefectAcceptance(emailAddress, projectIds);
    }

    @GetMapping(path = DEFECT_TRENDS_ACCEPTANCE_STATS)
    public DefectTrendAcceptanceStatsDataDto getDefectTrendsDefectAcceptanceStats(
            @RequestHeader(value = X_USER_HEADER, required = false) String emailAddress, @RequestParam String projectId,
            @RequestParam Integer month) {
        return clientDashboardService.getDefectTrendDefectAcceptanceStats(emailAddress, projectId, month);
    }

    @GetMapping("/")
    public String greetings() {
        throw new BadRequestException("Hi");
        // return "greetings";
    }

}
