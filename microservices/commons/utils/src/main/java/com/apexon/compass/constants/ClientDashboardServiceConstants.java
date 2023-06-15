package com.apexon.compass.constants;

import java.text.DecimalFormat;

public class ClientDashboardServiceConstants {

    private ClientDashboardServiceConstants() {
    }

    public static final Double ONE_DOUBLE = 1.0;

    public static final Double ZERO_DOUBLE = 0.0;

    public static final Integer ZERO = 0;

    public static final String X_USER_HEADER = "X-User-Header";

    public static final String PM_EMAIL = "pm.email";

    public static final String DM_EMAIL = "dm.email";

    public static final String AM_EMAIL = "am.email";

    public static final String CLIENT_PM_EMAIL = "clientPm.email";

    public static final String ID = "_id";

    public static final String IS_DELETED = "isDeleted";

    public static final String PROJECT_ID = "projectId";

    public static final String STATE = "state";

    public static final String SPRINT_ID = "sprintId";

    public static final String ACTIVE = "active";

    public static final String SPRINT_DATA = "sprintData";

    public static final String SPRINT_DATA_DOLLAR = "$sprintData";

    public static final String FUTURE = "future";

    public static final String JIRA_RULES = "jira_rules";

    public static final String ALL = "all";

    public static final String TO_INT_DOLLAR = "$toInt";

    public static final String PROJECTID = "projectId";

    public static final String JIRACONFIGURATION = "jiraConfiguration";

    public static final String JIRA_ID = "jiraId";

    public static final String PROJECT = "project";

    public static final String STATUS_DATA = "statusData";

    public static final String LIMIT_DOLLAR = "$limit";

    public static final String FULL_STOP = ".";

    public static final String DATE = "date";

    public static final String LTE_DOLLAR = "$lte";

    public static final String SORT_DOLLAR = "$sort";

    public static final String START_DATE = "startDate";

    public static final String END_DATE = "endDate";

    public static final String END_DATE_SPRINTDATA_ENDDATE = "$sprintData.endDate";

    public static final String TOTAL_EFFORTS = "totalEfforts";

    public static final String TOTAL_TASKS = "totalTasks";

    public static final String REMAINING_EFFORTS = "remainingEfforts";

    public static final String OPEN_TILL_NOW_POINTS = "openTillNowPoints";

    public static final String COMPLETED_EFFORTS = "completedEfforts";

    public static final String COMPLETED_POINTS = "completedPoints";

    public static final String NEWLY_ADDED_EFFORTS = "newlyAddedEfforts";

    public static final String NEWLY_ADDED_POINTS = "newlyAddedPoints";

    public static final String REOPEN_EFFORTS = "reopenEfforts";

    public static final String REOPEN_POINTS = "reopenPoints";

    public static final String REMAINING_TASKS = "remainingTasks";

    public static final String OPEN_TILL_NOW_COUNTS = "openTillNowCounts";

    public static final String COMPLETED_TASKS = "completedTasks";

    public static final String COMPLETED_COUNTS = "completedCounts";

    public static final String NEWLY_ADDED_TASKS = "newlyAddedTasks";

    public static final String NEWLY_ADDED_COUNTS = "newlyAddedCounts";

    public static final String REOPEN_TASKS = "reopenTasks";

    public static final String REOPEN_COUNTS = "reopenCounts";

    public static final String FIRST = "$first";

    public static final String NAME_DOLLAR = "$name";

    public static final String ID_DOT_JIRA_PROJECTID = "$_id.jirProjectId";

    public static final String ID_UNDERSCORE_DOLLAR = "$_id";

    public static final String LAST = "$last";

    public static final String ROOT_DOUBLE_DOLLAR = "$$ROOT";

    public static final String SUBTRACT = "$subtract";

    public static final String TOTAL_BOARDS = "totalBoards";

    public static final String TOTAL_BOARDS_DOLLAR_SIGN = "$totalBoards";

    public static final String POINTS_1 = "points1";

    public static final String POINTS_2 = "points2";

    public static final String COUNT_1 = "count1";

    public static final String COUNT_2 = "count2";

    public static final String BUG_TYPES = "bugTypes";

    public static final String DEFINITION_OF_DONE_ONLY = "definitionOfDone";

    public static final String DEFINITION_OF_ACCEPTED = "definitionOfAccepted";

    public static final String OPEN_STATUS = "openedStatus";

    public static final String CONCAT_ARRAYS = "$concatArrays";

    public static final String DEFINITION_OF_TO_DO = "$definitionOfTodo";

    public static final String DEFINITION_OF_IN_PROGRESS = "$definitionOfInProgress";

    public static final String DEFINITION_OF_DEV_COMPLETE = "$definitionOfDevComplete";

    public static final String BLOCKER_DEFINITION = "$blockerDefintion";

    public static final String CLOSED_STATUS = "closedStatus";

    public static final String DEFINITION_OF_DONE = "$definitionOfDone";

    public static final String DEFINITION_OF_ACCEPTED_DOLLAR = "$definitionOfAccepted";

    public static final String PROJECT__ID = "project_id";

    public static final String IN_PROGRESS_DEFECTS = "inProgressDefects";

    public static final String PROJECT_ID_DOLLAR = "$projectId";

    public static final String DATA = "data";

    public static final String JIRA_RULE = "jiraRules";

    public static final String JIRA_RULES_DOLLAR = "$jiraRules";

    public static final String BOARD_ID = "boardId";

    public static final String BUG__TYPES = "bug_type";

    public static final String BUG_TYPES_DOLLAR_SIGN = "$bugTypes";

    public static final String OPENED_STATUS = "opened_status";

    public static final String CLOSED__STATUS = "closed_status";

    public static final String OPENED_STATUS_DOLLAR = "$openedStatus";

    public static final String CLOSED_STATUS_DOLLAR = "$closedStatus";

    public static final String PROJECT_ID_DOUBLE_DOLLAR_SIGN = "$$project_id";

    public static final String JIRA_PROJECT_ID_DOUBLE_DOLLAR_SIGN = "$$jira_project_id";

    public static final String TYPE_DOLLAR_SIGN = "$type";

    public static final String STORY_TYPES_DOUBLE_DOLLAR = "$$story_types";

    public static final String TOTAL_STORIES = "totalStories";

    public static final String COMPLETED_STORIES = "completedStories";

    public static final String SPRINT_ID_DOLLAR_SIGN = "$sprintId";

    public static final String SPRINT_ID_DOUBLE_DOLLAR_SIGN = "$$sprint_id";

    public static final String SPRINT_IDS = "sprintIds";

    public static final String DEFECTS = "defects";

    public static final String ESTIMATE_DOLLAR = "$estimate";

    public static final String PLANNED_POINTS = "plannedPoints";

    public static final String PLANNED_COUNTS = "plannedCounts";

    public static final String SPRINT_JOURNEY_DOLLAR_SYMBOL = "$sprintJourney";

    public static final String SLICE_DOLLAR = "$slice";

    public static final String SPRINT_IDS_DOLLAR = "$sprintIds";

    public static final String SPRINTID = "sprint_id";

    public static final String PUSH_DOLLAR = "$push";

    public static final String BACKLOG_STORIES = "backlogStories";

    public static final String IN_PROGRESS_STORIES = "inProgressStories";

    public static final String BUG_TYPES_DOUBLE_DOLLAR = "$$bug_types";

    public static final String RESOLVED_DEFECTS = "resolvedDefects";

    public static final String BUG_TYPE_DOUBLE_DOLLAR_SIGN = "$$bug_type";

    public static final String TOTAL_DEFECTS = "totalDefects";

    public static final String OPEN_DEFECTS = "openDefects";

    public static final String STATE_DOLLAR_SIGN = "$state";

    public static final String OPENED_STATUS_DOUBLE_DOLLAR_SIGN = "$$opened_status";

    public static final String TOTAL_OPENED = "totalOpened";

    public static final String BACKLOG_DEFECTS = "backlogDefects";

    public static final String CRITICAL = "critical";

    public static final String PRIORITY_TYPE_DOLLAR_SIGN = "$priorityType";

    public static final String BLOCKER = "blocker";

    public static final String CLOSE_DEFECTS = "closeDefects";

    public static final String CLOSED_STATUS_DOUBLE_DOLLAR_SIGN = "$$closed_status";

    public static final String TOTAL_CLOSED = "totalClosed";

    public static final String PATH = "path";

    public static final String DATA_WITH_DOLLAR_SIGN = "$data";

    public static final String DATA_DOT_STORIES_DOLLAR_SIGN = "$data.stories";

    public static final String DATA_DOT_STORIES = "data.stories";

    public static final String DATA_DOT_DEFECTS_DOLLAR_SIGN = "$data.defects";

    public static final String DATA_DOT_DEFECTS = "data.defects";

    public static final String TOTAL = "total";

    public static final String DATA_DOT_TOTAL_DEFECTS_DOLLAR_SIGN = "$data.totalDefects";

    public static final String DATA_DOT_OPEN_DEFECTS_DOLLAR_SIGN = "$data.openDefects";

    public static final String DATA_DOT_OPEN_DEFECTS = "data.openDefects";

    public static final String DATA_DOT_CLOSE_DEFECTS_DOLLAR_SIGN = "$data.closeDefects";

    public static final String DATA_DOT_CLOSE_DEFECTS = "data.closeDefects";

    public static final String TO_STRING_DOLLAR = "$toString";

    public static final String JIRA_PROJECT_ID_UNDERSCORE = "jira_project_id";

    public static final String PROJECT_ID_DOUBLE_DOLLAR = "$$projectId";

    public static final String JIRA_PROJECT_ID_DOLLAR = "$jiraProjectId";

    public static final String PROJECT_NAME = "projectName";

    public static final String PROJECT_NAME_DOLLAR = "$projectName";

    public static final String PROJECT_NAME_DOLLAR_DOT_NAME = "$projectName.name";

    public static final String DATA_DOT_TOTAL_DEFECTS_DOT_TOTAL_DOLLAR = "$data.totalDefects.total";

    public static final String STORY_TYPES = "storyTypes";

    public static final String STORY_TYPES_UNDER = "story_types";

    public static final String STORY_TYPES_DOLLAR = "$storyTypes";

    public static final String OPENED_STATUS_CAMLE_CASE = "openedStatus";

    public static final String BUG_TYPES_UNDER = "bug_types";

    public static final String BUG_TYPES_DOLLAR = "$bugTypes";

    public static final String DEFINITION_OF_MAJOR_SEVERITY = "definitionOfMajorSeverity";

    public static final String DEFINITION_OF_MINOR_SEVERITY = "definitionOfMinorSeverity";

    public static final String DEFINITION_OF_BLOCKER_SEVERITY = "definitionOfBlockerSeverity";

    public static final String DEFINITION_OF_CRITICAL_SEVERITY = "definitionOfCriticalSeverity";

    public static final String PRIORITY_NAME = "$priorityName";

    public static final String JSON_PROCESSING_LOG_MESSAGE = "Json Processing Exception Occured , Details :";

    public static final String JSON_PROCESSING_EXCEPTION_MESSAGE = "JSON Processing Exception Occured";

    public static final String MONGO_EXCEPTION_MESSAGE = "Something went wrong while performing DB operation";

    public static final Integer TWO = 2;

    public static final String EXISTS = "$exists";

    public static final String REPOS = "repos";

    public static final String SPRINTS = "sprints";

    public static final String SCM_REPO_URL = "scmRepoUrl";

    public static final String BOTH_DATA = "bothData";

    public static final String REPO_URL = "repo_url";

    public static final String TOTAL_LINE_OF_CODE = "totalLineOfCode";

    public static final String SCM_MONTH = "scmMonth";

    public static final String OVERALL = "overall";

    public static final String SCM = "scm";

    public static final String TOTAL_VIOLATION = "totalViolation";

    public static final String TECHNICAL_DEBT = "technicalDebt";

    public static final String TECHNICAL_DEBT_INDEX = "technicalDebtIndex";

    public static final String RELIABILITY_EFFORTS = "$reliability.efforts";

    public static final String SECURITY_EFFORTS = "$securityEfforts";

    public static final String TEST_CODE_COVERAGE = "testCodeCoverage";

    public static final String QUALITY_MATRIX_COVERAGE = "$qualityMatrix.coverage";

    public static final String VIOLATIONS_BLOCKER_VIOLATIONS_CRITICAL = "$violations.blocker,$violations.critical";

    public static final String COMPLIANCE = "compliance";

    public static final String SCM_DATA = "scmData";

    public static final String VIOLATION_DATA = "violationData";

    public static final String REPO_NAME = "repoName";

    public static final String IF_NULL = "$ifNull";

    public static final String VIOLATIONS_AND_TECH_DEBT = "violationsAndTechnicalDebt";

    public static final String REPO_URL_CAMEL_CASE = "repoUrl";

    public static final String AVERAGE = "Average";

    public static final String NO_PROJECTS_AVAILABLE_FOR_USER_FOR_THIS_ISC_PROJECT_ID = "No projects available for user for this iscProjectId";

    public static final String NO_RECORD_AVAILABLE = "No record available";

    public static final String DATA_IS_UNAVAILABLE = "Data is unavailable";

    public static final String DECIMAL_FORMAT = "##.##";

    public static final String NO_PROJECTS_AVAILABLE_UNDER_USER = "No projects available under user";

    public static final String NO_RECORD_AVAILABLE_FOR_PROJECTS = "No record available for {} projects";

    public static final String SOMETHING_WENT_WRONG_WHILE_PARSING_THE_DATE = "Something went wrong while parsing the date";

    public static final String NO_MONTHLY_DATA_AVAILABLE_FOR_PROJECT_ID = "No monthly data available for projectId";

    public static final String SONAR_PROJECT_ID = "sonar_project_id";

    public static final DecimalFormat DECIMAL_FORMATTER = new DecimalFormat(DECIMAL_FORMAT);

}
