package com.apexon.compass.constants;

public class PsrServiceRouteConstants {

    private PsrServiceRouteConstants() {
    }

    public static final String CLIENT_SPRINT_DETAILS = "/client-sprint-details";

    public static final String PROJECTID_TEAM_VELOCITY = "/{jiraProjectId}/team-velocity";

    public static final String PROJECTID_COMPLETION_RATE = "/{jiraProjectId}/completion-rate";

    public static final String PROJECTID_COMPLETED_SPRINT = "/{jiraProjectId}/completed-sprint";

    public static final String PROJECTID_CURRENT_SPRINT_PROGRESS = "/{jiraProjectId}/current-sprint-progress";

    public static final String PROJECTID_FEATURE_STAGES = "/{jiraProjectId}/feature-stages";

    public static final String PROJECTID_DELIVERED_ACCEPTED = "/{jiraProjectId}/daily-accepted-delivered";

    public static final String CLIENT_PROJECT = "/client/project";

    public static final String COE_DETAILS = "/coe-details";

    public static final String TEAM_WISE = "/team-wise";

    public static final String INDUSTRY_WISE = "/industry-wise";

    public static final String SOLUTION_WISE = "/solution-wise";

    public static final String OVERALL_HEALTH = "/overall-health";

    public static final String PROJECT = "/project";

    public static final String JIRA_DETAILS = "/jira-details";

    public static final String PROJECTID_SPRINT_BURNDOWN = "/{jiraProjectId}/sprint-burndown";

    public static final String PROJECTID_POINTS_COMPLETION = "/{jiraProjectId}/points-completion";

    public static final String JIRAPROJECTID_DAILY_ACCEPTED_DELEVERED = "/{jiraProjectId}/daily-accepted-delivered";

    public static final String PROJECT_DETAILS = "/project-details";

    public static final String PROJECTID = "/{projectId}";

    public static final String PROJECT_ID = "/{id}";

    public static final String BLOCKERS_BY_PROJECT_ID = "/{id}/sprint/blockers";

    public static final String PROJECTID_SPRINT_STATUS = "/{id}/sprint/status";

    public static final String ALL_PROJECT = "/all";

    public static final String ALL_CLIENT_PROJECT = "/client/all";

    public static final String PEOPLE_STATUS_TREND = "/people-status-trend/{year}";

    public static final String STRATEGIC_DETAILS = "/strategic-details";

    public static final String NET_PROMOTER = "/net-promoter";

    public static final String ACCOUNT_MANAGER = "/account-manager";

    public static final String DELIVERY_MANAGER = "/delivery-manager";

    public static final String WEEKLY_STATUS = "/weekly-status";

    public static final String OVERALL_PROJECT_HEALTH = "/overall-project-health";

    public static final String CRITERIA_WISE_HEALTH = "/criteria-wise-health";

    public static final String ON_BOARD = "/onboard";

    public static final String PROJECTID_OFFBOARD = "/{isc_project_id}/offboard";

    public static final String SPRINT_OVERVIEW = "/{projectId}/sprint/overview";

    public static final String SPRINT_BUGS = "{projectId}/sprint/bugs";

    public static final String SPRINTLOGGEDVSACCEPTED_BUGS = "{projectId}/sprint/bugs/status";

    public static final String EFFORT_VARIANCE_BY_PROJECT_ID = "/{projectId}/sprint/ev";

    public static final String SPRINT_MEMBER_STATUS = "/{projectId}/sprint/member/status";

    public static final String SPRINT_REPORT = "/{projectId}/sprint/story/report";

    public static final String SPRINT_MEMBER_STATUS_BY_MEMBERID = "/{projectId}/sprint/member/{memberId}/status";

    public static final String SPRINT_ACCEPTED_VS_DELIVERED = "/{iscProjectId}/sprint/accepted/delivered";

    public static final String SPRINT_BURNDOWN_CHART = "/{iscProjectId}/sprint/burndown";

    public static final String NPS_DATAPARSING = "/{isc_project_id}/nps";

    public static final String SPRINT_SAY_DO_RATIO = "/{projectId}/sprint/saydo";

    public static final String UPDATE_PROJECT_ICON = "/{isc_project_id}/logo";

    public static final String PROJECT_COMPLIANCE = "{isc_project_id}/compliance";

    public static final String GET_LEAVE_CALENDER_SPRINT_ID = "/{projectId}/sprint/leaves";

    public static final String SPRINT_MEMBER_DETAILS_BY_MEMBERID = "{projectId}/member/{memberId}/details";

    public static final String PROJECTID_JIRA_BOARDS = "/{projectId}/jiraBoards";

}
