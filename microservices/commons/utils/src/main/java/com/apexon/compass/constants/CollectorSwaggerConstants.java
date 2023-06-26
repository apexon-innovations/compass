package com.apexon.compass.constants;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * @author amit.bhoraniya
 * @created 23/11/20
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CollectorSwaggerConstants {

    public static final String COLLECTOR_CONTROLLER_TAG = "Collectors API";

    public static final String GET_RUNS = "Get run histories";

    public static final String GET_RUN_BY_ID = "Get run detail";

    public static final String POST_RUN = "Start new collector";

    public static final String PROJECT_DETAILS_CONTROLLER_TAG = "Project details API";

    public static final String PROJECT_ONBOARD_CONTROLLER_TAG = "Project onboard API";

    public static final String JIRA_CONFIGURATION_REQUEST_DESC = "Jira configuration request";

    public static final String JIRA_RULES_REQUEST_DESC = "Jira rules request";

    public static final String POST_JIRA_CONFIGURATION = "Create jira configuration";

    public static final String POST_JIRA_RULES = "Create jira mappings";

}
