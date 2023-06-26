package com.apexon.compass.constants;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class StrategyServiceRouteConstants {

    public static final String PROJECT_STRATEGIC_BOARD = "/project/strategic-board";

    public static final String PROJECT = "/project";

    public static final String PROJECT_ID = "{projectId}";

    public static final String SECURITY_REPORT = "/{id}/securityReport";

    public static final String PR_JOURNEY = "/repo/pullrequests/journey";

    public static final String GET_RISK = "/repo/risk/measurements";

    public static final String PROJECT_QUALITY_MEASUREMENTS = "/repo/quality/measurements";

    public static final String VIOLATIONS_SUMMARY = "/repo/violation/summary";

    public static final String SPRINT_PULL_REQUEST_SUMMARY = "/repo/pullrequests/summary";

    public static final String SPRINT_REVIEWER_METRICS = "/repo/reviewer/metrics";

    public static final String COLLABORATION_METRICS = "/repo/collaboration/metrics";

    public static final String ID = "/{id}";

    public static final String SPRINT = "/sprint/{sprintId}";

    public static final String SPRINT_SUBMITTER_METRICS = "/repo/submitter/metrics";

    public static final String MEMBERWISE_ACTIVITY_METRICS = "/repo/activity/metrics";

    public static final String GET_QUALITY_SUMMARY = "/repo/quality/summary";

    public static final String TECHNICAL_DEBT_METRICS = "/repo/techdebt/metrics";

    public static final String GET_COMPLIANCE_ANALYSIS = "/repo/compliance/analysis";

    public static final String CODE_METRICS = "/repo/code/metrics";

}
