package com.apexon.compass.constants;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * @author amit.bhoraniya
 * @created 23/11/20
 */

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CollectorRouteConstants {

    public static final String PROJECT = "/project/{iscProjectId}";

    public static final String RUNS = "/run";

    public static final String RUN_BY_ID = "/runs/{runId}";

    public static final String PROJECT_ID = "iscProjectId";

    public static final String PROJECT_ID_DESC = "Project Id";

    public static final String RUN_ID = "runId";

    public static final String RUN_ID_DESC = "Run Id";

    public static final String START_REQUEST_DESC = "Start collector request";

    public static final String WEB_SOCKET_ENDPOINT = "/ws";

    public static final String TOPIC_ENDPOINT = "/topic";

    public static final String APP_ENDPOINT = "/app";

    public static final String STATUS = "/status";

    public static final String PROJECT_ONBOARD = PROJECT + "/onboard";

    public static final String JIRA_CONFIGURATION = "/jira-configuration";

    public static final String JIRA_RULES = "/jira-rules";

}
