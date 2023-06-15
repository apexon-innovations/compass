package com.apexon.compass.constants;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.List;

public class QueryConstants {

    public static final String PROJECT_CLIENT_PM_EMAIL = "project.clientPm.email";

    private QueryConstants() {
    }

    public static final String FULLSTOP_PUNCTUATION = ".";

    // 1.Strategy constants copied
    public static final int values[] = { 5, 4, 3, 2, 1 };

    public static final String MAJOR = "major";

    public static final String INFO = "info";

    public static final String MINOR = "minor";

    public static final String ID = "id";

    public static final String REPO_ID = "repoId";

    public static final String VIOLATIONS = "violations";

    public static final String NEWVIOLATIONS = "newViolations";

    public static final String MEASURES = "measures";

    public static final String RISK = "risk";

    public static final String ALL = "all";

    public static final String SEARCH = "search";

    public static final String SECURITY = "security";

    public static final String SECURITY_DOLLAR = "$security";

    public static final String EFFICIENCY = "efficiency";

    public static final String ROBUSTNESS = "robustness";

    public static final String TECHDEBT = "technicalDebt";

    public static final String ADDED = "added";

    public static final String REMOVED = "removed";

    public static final String STRING = "String";

    public static final String NAME = "name";

    public static final String BASE_PACKAGE = "com.apexon.compass";

    public static final String DAYINITIAL = "d";

    public static final String HOURINITIAL = "h";

    public static final String MINUTEINITIAL = "m";

    public static final String CREATED_DATE = "createdDate";

    public static final String DOLLAR_SIGN = "$";

    public static final String MATCH = "match";

    public static final String MATCH_DOLLAR = "$match";

    public static final String UNWIND = "unwind";

    public static final String UNWIND_DOLLAR = "$unwind";

    public static final String PROJECTS = "$projects";

    public static final String LOOKUP = "lookup";

    public static final String LOOKUP_DOLLAR = "$lookup";

    public static final String FROM = "from";

    public static final String LET = "let";

    public static final String PROJECTKEY = "projectKey";

    public static final String PROJECT_KEY = "$projects.key";

    public static final String PIPELINE = "pipeline";

    public static final String EXPRESSION = "expr";

    public static final String EXPRESSION_DOLLAR = "$expr";

    public static final String EQUAL = "eq";

    public static final String EQUAL_DOLLAR = "$eq";

    public static final String SONARPROJECTID = "sonarProjectId";

    public static final String SORT = "sort";

    public static final String SORT_DOLLAR = "$sort";

    public static final String LIMIT = "limit";

    public static final String LIMIT_DOLLAR = "$limit";

    public static final String PROJECT = "project";

    public static final String PROJECT_DOLLAR = "$project";

    public static final String RATINGS_SECURITY = "ratings.security";

    public static final String RATINGS_MAINTAINABILITY = "ratings.maintainability";

    public static final String RATINGS_RELIABILITY = "ratings.reliability";

    public static final String AS = "as";

    public static final String QUALITY_MEASUREMENTS = "quality_measurements";

    public static final String GROUP = "group";

    public static final String GROUP_DOLLAR = "$group";

    public static final String AVG = "$avg";

    public static final String QUALITY_MEASUREMENTS_PARAM = "$quality_measurements";

    public static final String SONAR_PROJECT_ID_QUERY_PRAM = "$sonarProjectId";

    public static final String DERIVED_STORIES = "derived_stories_collection";

    public static final String PROJECTKEY_QUERY_PRAM = "$$projectKey";

    public static final String VIOLATIONS_BLOCKER = "violations.blocker";

    public static final String VIOLATIONS_CRITICAL = "violations.critical";

    public static final String QUAILTY_MATRIX_COVERAGE = "qualityMatrix.coverage";

    public static final String SONAR_PROJECT_ID = "sonarProjectId";

    public static final String ID_PRE_UNDER = "_id";

    public static final String TEST_CODE_COVERAGE = "testCodeCoverage";

    public static final String MULTIPLY = "multiply";

    public static final String MULTIPLY_DOLLAR = "$multiply";

    public static final String DIVIDE_DOLLAR = "$divide";

    public static final String QUALITY_MEASUREMENTS_QUALITY_MATRIX_COVERAGE = "$quality_measurements.qualityMatrix.coverage";

    public static final String SWITCH = "$switch";

    public static final String BRANCHES = "branches";

    public static final String CASE = "case";

    public static final String AND = "and";

    public static final String AND_DOLLAR = "$and";

    public static final String GTE = "gte";

    public static final String GTE_DOLLAR = "$gte";

    public static final String QUALITY_MEASUREMENT_VIOLATION_BLOCKER_AND_CRITICAL = "$quality_measurements.violations.blocker,$quality_measurements.violations.critical";

    public static final String LTE = "lte";

    public static final String LTE_DOLLAR = "$lte";

    public static final String THEN = "then";

    public static final String DEFAULT = "default";

    public static final String QUALITY_MEASUREMENTS_RATINGS_SECURITY = "$quality_measurements.ratings.security";

    public static final String QUALITY_MEASUREMENTS_RATINGS_RELIABILITY = "$quality_measurements.ratings.reliability";

    public static final String QUALITY_MEASUREMENTS_RATINGS_MAINTAINABILITY = "$quality_measurements.ratings.maintainability";

    public static final String PR_CREATED_TIME = "prCreatedTime";

    public static final String PR_CREATED_TIME_DOLLAR = "$prCreatedTime";

    public static final String PR_MERGE_TIME = "prMergeTime";

    public static final String PR_MERGE_TIME_DOLLAR = "$prMergeTime";

    public static final String PR_DECLINED_TIME = "prDeclinedTime";

    public static final String PR_DECLINED_TIME_DOLLAR = "$prDeclinedTime";

    public static final Long FOUR_HOURS = 14400000l; // 1000 * 60 * 60 * 4

    public static final String ACTIVE = "active";

    public static final List<String> ROLES = Arrays.asList("PROJECT_CLIENT", "PDO", "PROJECT_MANAGER", "DES_ADMIN",
            "COE_MANAGER");

    public static final String HOURS = " Hours";

    public static final String DAYS = " Days";

    public static final String DECIMAL_FORMAT = "#.##";

    public static final String REVIEWERS = "reviewers";

    public static final String NEW = "new";

    public static final String BUGS = "bugs";

    public static final String VULNERABILITIES = "vulnerabilities";

    public static final String NEW_LINE_OF_CODES = "newLineOfCodes";

    public static final String DUPLICATION_BLOCKS = "duplicationBlocks";

    public static final String DUPLICATION_LINES = "duplicationLines";

    public static final String PERCENTAGE = "%";

    public static final String SUM = "sum";

    public static final String SUM_DOLLAR = "$sum";

    public static final String ADD = "add";

    public static final String ADD_DOLLAR = "$add";

    public static final String TECH_DEBT_SUMMARY_QUERY_PARAM = "$tech_debt_summary";

    public static final String TECHNICAL_DEBT_SUMMARY = "tech_debt_summary";

    public static final String TECHNICAL_DEBT_SUMMARY_SONAR_CONFIGURATION_ID = "$tech_debt_summary.sonarConfigurationId";

    public static final String COMPLEXITY = "complexity";

    public static final String COMPLEXITY_DOLLAR = "$complexity";

    public static final String TESTS = "tests";

    public static final String CONGNITIVE_COMPLEXITY = "cognitiveComplexity";

    public static final String SIZE = "size";

    public static final String SIZE_DOLLAR = "$size";

    public static final String ISSUES = "issues";

    public static final String ISSUES_DOLLAR = "$issues";

    public static final String MAINTAINABILITY = "maintainability";

    public static final String MAINTAINABILITY_DOLLAR = "$maintainability";

    public static final String RELIABILITY = "reliability";

    public static final String RELIABILITY_DOLLAR = "$reliability";

    public static final String DUPLICATION = "duplication";

    public static final String DUPLICATION_DOLLAR = "$duplication";

    public static final String SONAR_STATS = "sonar_stats";

    public static final String TECH_DEBT_SUMMARY_DUPLICATION_BLOCKS = "$tech_debt_summary.duplication.blocks";

    public static final String TECH_DEBT_SUMMARY_DUPLICATION_LINES = "$tech_debt_summary.duplication.lines";

    public static final String TECH_DEBT_SUMMARY_DUPLICATION_LINE_DENSITY = "$tech_debt_summary.duplication.lineDensity";

    public static final String TECH_DEBT_SUMMARY_DUPLICATION_FILES = "$tech_debt_summary.duplication.files";

    public static final String TECH_DEBT_SUMMARY_ISSUES_OPEN = "$tech_debt_summary.issues.open";

    public static final String TECH_DEBT_SUMMARY_ISSUES_CONFIRMED = "$tech_debt_summary.issues.confirmed";

    public static final String TECH_DEBT_SUMMARY_ISSUES_REOPENED = "$tech_debt_summary.issues.reopened";

    public static final String TECH_DEBT_SUMMARY_VIOLATIONS_TOTAL = "$tech_debt_summary.violations.total";

    public static final String TECH_DEBT_SUMMARY_QUALITY_MATRICES_BUGS = "$tech_debt_summary.qualityMatrix.bugs";

    public static final String TECH_DEBT_SUMMARY_QUALITY_MATRICES_CODE_SMELLS = "$tech_debt_summary.qualityMatrix.codeSmells";

    public static final String TECH_DEBT_SUMMARY_SECURITY_VULNERABILITIES = "$tech_debt_summary.security.vulnerabilities";

    public static final String TECH_DEBT_SUMMARY_SECURITY_NEW_VULNERABILITIES = "$tech_debt_summary.security.newVulnerabilities";

    public static final String TECH_DEBT_SUMMARY_CODE_MATRIX_LINES = "$tech_debt_summary.codeMatrix.lines";

    public static final String TECH_DEBT_SUMMARY_QUALITY_MATRICES_COVERAGE = "$tech_debt_summary.qualityMatrix.coverage";

    public static final String TECH_DEBT_SUMMARY_TECH_DEPT_INDEX = "$tech_debt_summary.technicalDebtIndex";

    public static final String TECH_DEBT_SUMMARY_RELIABILITY_EFFORTS = "$tech_debt_summary.reliability.efforts";

    public static final String TECH_DEBT_SUMMARY_SECURITY_EFFORTS = "$tech_debt_summary.securityEfforts";

    public static final String CODE_ANALYSIS = "code_analysis";

    public static final String CODE_ANALYSIS_LEGACY_REFACTOR = "$code_analysis.legacyRefactor";

    public static final String LEGACY_REFACTOR = "legacyRefactor";

    public static final String LINE_OF_CODE = "lineOfCode";

    public static final String SCM_CONFIGURATION_ID = "scmConfigurationId";

    public static final String IS_ARCHIVE = "isArchive";

    public static final String CODE_ANALYSIS_DEVELOPER_NAME = "$code_analysis.authors.developerName";

    public static final String CODE_ANALYSIS_TOTAL_LOC = "$code_analysis.authors.totalLineOfCode";

    public static final String CODE_ANALYSIS_CODE_CHURN = "$code_analysis.authors.codeChurn";

    public static final String CODE_ANALYSIS_AUTHORS_LEGACY_REFACTOR = "$code_analysis.authors.legacyRefactor";

    public static final String CODE_ANALYSIS_VALUE = "$code_analysis.legacyRefactor.value";

    public static final String CODE_ANALYSIS_ADDED_LINE_OF_CODE_IN_SPRINT = "$code_analysis.authors.addedLineOfCodeInSprint";

    public static final String CODE_ANALYSIS_REMOVED_LINE_OF_CODE_IN_SPRINT = "$code_analysis.authors.removedLineOfCodeInSprint";

    public static final String CODE_ANALYSIS_ADDED_LINE_OF_CODE = "$code_analysis.authors.addedLineOfCode";

    public static final String CODE_ANALYSIS_REMOVED_LINE_OF_CODE = "$code_analysis.authors.removedLineOfCode";

    public static final String PRODUCTIVE_CODE = "productiveCode";

    public static final String CODE_CHURN = "codeChurn";

    public static final String SCM_CONFIGURATION_ID_KEY = "$scmConfigurationId";

    public static final String SCM_CONFIGURATION_ID_VALUE = "$$scmConfigurationId";

    public static final String PRODUCTIVE_CODE_ANALYSIS = "$code_analysis";

    public static final String PRODUCTIVE_CODE_CHURN_ANALYSIS = "$code_analysis.codeChurn";

    public static final String PRODUCTIVE_CODE_CHURN_DEVELOPER_NAME = "$code_analysis.codeChurn.developerName";

    public static final String PRODUCTIVE_CODE_CHURN_TOTAL_LINE_CODE = "$code_analysis.codeChurn.totalLineOfCode";

    public static final String PRODUCTIVE_CODE_CHURN_VALUE = "$code_analysis.codeChurn.value";

    public static final String PRODUCTIVE_CODE_CHURN_ADDED_LINE_OF_CODE = "$code_analysis.codeChurn.addedLineOfCode";

    public static final String PRODUCTIVE_CODE_CHURN_REMOVED_LINE_OF_CODE = "$code_analysis.codeChurn.removedLineOfCode";

    public static final String RECORD_NOT_FOUND = "No record found";

    public static final String SPRINT_SUBMITTER_METRICS_SORT_BY_PATTERN = "^(top|memberwise|mostRecommits|mostComments)$";

    public static final String INVALID_SORT_BY_VALUE = "Invalid sortBy value";

    public static final String SEPARATOR = ":";

    public static final String DESC = "-1";

    public static final String ASC = "1";

    public static final String AGGREGATE_SORT_BY_QUERY_START = "{$sort : {";

    public static final String AGGREGATE_SORT_BY_QUERY_END = "}}";

    public static final String PROJECT_S = "projects";

    public static final String TECH_DEBT_SUMMARY_QUERY_PARAM_COMPLEXITY = TECH_DEBT_SUMMARY_QUERY_PARAM
            + FULLSTOP_PUNCTUATION + COMPLEXITY;

    public static final String TECH_DEBT_SUMMARY_QUERY_PARAM_CONGNITIVE_COMPLEXITY = TECH_DEBT_SUMMARY_QUERY_PARAM
            + FULLSTOP_PUNCTUATION + CONGNITIVE_COMPLEXITY;

    public static final String START_DATE = "startDate";

    public static final String START_DATE_DOLLAR = "$startDate";

    public static final String FACET = "$facet";

    public static final String PR_METRICS = "prMetrics";

    public static final String COMMENTS = "comments";

    public static final String COMMENTS_DOLLAR = "$comments";

    public static final String CODE_COMMITS = "codeCommits";

    public static final String MERGED_PRS = "mergedPrs";

    public static final String TOTAL_PRS = "totalPrs";

    public static final String REVIEW_COMMENTS_ON_OTHER_PR = "reviewCommentsOnOthersPrs";

    public static final String OR = "or";

    public static final String OR_DOLLAR = "$or";

    public static final String PULL_REQUEST_ID = "pullRequestId";

    public static final String PULL_REQUEST_ID_DOLLAR = "$pullRequestId";

    public static final String PULL_REQUEST_ID_DOUBLE_DOLLAR = "$$pullRequestId";

    public static final String COMMITS = "commits";

    public static final String SCM_AUTHOR_NAME = "$scmAuthor.name";

    public static final String COND = "cond";

    public static final String COND_DOLLAR = "$cond";

    public static final String IN = "in";

    public static final String IN_DOLLAR = "$in";

    public static final String STATE = "state";

    public static final String STATE_DOLLAR = "$state";

    public static final String MERGED = "merged";

    public static final String COMMENTS_NAME = "$comments.name";

    public static final String NE = "$ne";

    public static final String ADDED_LINE_OF_CODE = "addedLineOfCode";

    public static final String REMOVED_LINE_OF_CODE = "removedLineOfCode";

    public static final String CODE_ANALYSIS_AUTHORS = "$code_analysis.authors";

    public static final String ADDED_LINE_OF_CODE_TILL_DATE = "addedLineOfCodeTillDate";

    public static final String REMOVED_LINE_OF_CODE_TILL_DATE = "removedLineOfCodeTillDate";

    public static final String MINUTES = "Minutes";

    // 2.Clientdashboard service constants
    public static final Double ONE_DOUBLE = 1.0;

    public static final Integer INTEGER_SEVEN = 7;

    public static final Integer INTEGER_FIFTEEN = 15;

    public static final Integer INTEGER_THIRTY = 30;

    public static final Integer INTEGER_NINETY = 90;

    public static final Double ZERO_DOUBLE = 0.0;

    public static final Double THIRTY_SIX_LAKH_DOUBLE = 3600000.0;

    public static final Double TEN_DOUBLE = 10.0;

    public static final Double ONE_HUNDRED_DOUBLE = 100.0;

    public static final Double ZERO_DOT_TWO_DOUBLE = 0.2;

    public static final Double TWENTY_DOUBLE = 20.0;

    public static final Double THREE_HUNDRED_ONE_DOUBLE = 301.0;

    public static final Double FIFTY_DOUBLE = 50.0;

    public static final Integer ZERO = 0;

    public static final String X_USER_HEADER = "X-User-Header";

    public static final String PM_EMAIL = "pm.email";

    public static final String DM_EMAIL = "dm.email";

    public static final String AM_EMAIL = "am.email";

    public static final String CLIENT_PM_EMAIL = "clientPm.email";

    public static final String IS_DELETED = "isDeleted";

    public static final String PROJECT_ID = "projectId";

    public static final String SPRINT_ID = "sprintId";

    public static final String SPRINT_DATA = "sprintData";

    public static final String SPRINT_DATA_DOLLAR = "$sprintData";

    public static final String FUTURE = "future";

    public static final String JIRA_RULES = "jira_rules";

    public static final String TO_INT_DOLLAR = "$toInt";

    public static final String JIRACONFIGURATION = "jiraConfiguration";

    public static final String JIRA_ID = "jiraId";

    public static final String STATUS_DATA = "statusData";

    public static final String FULL_STOP = ".";

    public static final String DATE = "date";

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

    public static final String NAME_DOLLAR = "$name";

    public static final String ID_DOT_PROJECTID = "$_id.projectId";

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

    public static final String DEFINATION_OF_DONE_ONLY = "definitionOfDone";

    public static final String DEFINATION_OF_ACCEPTED = "definitionOfAccepted";

    public static final String OPEN_STATUS = "openedStatus";

    public static final String CONCAT_ARRAYS = "$concatArrays";

    public static final String DEFINATION_OF_TO_DO = "$definitionOfTodo";

    public static final String DEFINATION_OF_IN_PROGRESS = "$definitionOfInProgress";

    public static final String DEFINATION_OF_DEV_COMPLETE = "$definitionOfDevComplete";

    public static final String BLOCKER_DEFINATION = "$blockerDefintion";

    public static final String CLOSED_STATUS = "closedStatus";

    public static final String DEFINATION_OF_DONE = "$definitionOfDone";

    public static final String DEFINATION_OF_ACCEPTED_DOLLAR = "$definitionOfAccepted";

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

    public static final String EXISTS = "exists";

    public static final String EXISTS_DOLLAR = "$exists";

    public static final String REPOS = "repos";

    public static final String REPOS_DOLLAR = "$repos";

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

    public static final String QUALITY_MATRIX_COVERAGE = "$qualityMatrix.coverage";

    public static final String VIOLATIONS_BLOCKER_VIOLATIONS_CRITICAL = "$violations.blocker,$violations.critical";

    public static final String COMPLIANCE = "compliance";

    public static final String SCM_DATA = "scmData";

    public static final String VIOLATION_DATA = "violationData";

    public static final String REPO_NAME = "repoName";

    public static final String IF_NULL = "$ifNull";

    public static final String VIOLATIONS_AND_TECH_DEBT = "violationsAndTechnicalDebt";

    public static final String REPO_URL_CAMEL_CASE = "repoUrl";

    // 3.PSR service constants
    public static final SimpleDateFormat MONGO_MONTH_FORMAT = new SimpleDateFormat("MMM/yyyy");

    public static final SimpleDateFormat MONGO_WEEK_FORMAT = new SimpleDateFormat("MMM dd");

    public static final SimpleDateFormat MONGO_DATE_TIMESTAMP_FORMAT = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss");

    public static final String MONTH = "month";

    public static final Integer[] RANGEARRAY = { 5, 5, 10, 5000 };

    public static final String NA = "NA";

    public static final String TEAM_MEMBERS = "teamMembers";

    public static final String TIME_ELAPSED = "Time Elapsed";

    public static final String WORK_COMPLETED = "Work Completed";

    public static final String SCOPE_CHANGE = "Scope Change";

    public static final String FLAGGED = "Flagged";

    public static final String[] FEATURES = { "To Do", "In Progress", "Completed" };

    public static final String COMPLETEDTASKS = "completedTasks";

    public static final String ACCEPTEDTASKS = "acceptedTasks";

    public static final String JIRAPROJECTID = "jiraProjectId";

    public static final String HEALTH = "Health";

    public static final String HEADER = "header";

    public static final String MORE_THAN = "More than ";

    public static final String PEOPLE = "People";

    public static final String KEY = "key";

    public static final String VALUE = "value";

    public static final String INDUSTRY_TYPE = "industryType";

    public static final String SOLUTION_TYPE = "solutionType";

    public static final String PROJECT_COUNT = "projectCount";

    public static final String TEAM_SIZE = "teamSize";

    public static final String OWNERS_ID = "$ownerIds";

    public static final String TOTAL_COMPLETED = "totalCompleted";

    public static final String TOTAL_TO_DO = "totalToDo";

    public static final String TOTAL_IN_PROGRESS = "totalInProgress";

    public static final String TOTAL_ASSIGNED = "totalAssigned";

    public static final String TOTAL_UNASSIGNED = "totalUnassigned";

    public static final String RED = "red";

    public static final String AMBER = "amber";

    public static final String GREEN = "green";

    public static final String RINITIAL = "R";

    public static final String AINITIAL = "A";

    public static final String GINITIAL = "G";

    public static final String NAINITIAL = "-";

    public static final String JIRA_CHANGE_DATE = "jiraChangeDate";

    public static final String JIRA_CHANGE_DATE_SPRINT_DATA_DOLLAR = "$sprintData.jiraChangeDate";

    public static final String PROJECT_NUMBER = "numberOfProjects";

    public static final String SCORE = "score";

    public static final String ON_TRACK = "On Track";

    public static final String DELAY = "Delay";

    public static final String GREENCAP = "Green";

    public static final String AMBERCAP = "Amber";

    public static final String REDCAP = "Red";

    public static final String SPRINT_NAME = "sprintName";

    public static final String START_DATE_SPRINT_DATA_DOLLAR = "$sprintData.startDate";

    public static final String TEAM = "team";

    public static final String PERSON_NAME = "personName";

    public static final String STORY_POINTS = "storyPointCompleted";

    public static final String BLOCKERS = "blockers";

    public static final String STATUS_DOLLAR = "$status";

    public static final String BOARD_ID_DOLLAR = "$boardId";

    public static final String STORIES = "stories";

    public static final String TOTAL_OPEN = "totalOpen";

    public static final String SPRINT_TICKETS = "sprint_tickets";

    public static final String IDEAL_BURNDOWN = "idealBurndown";

    public static final String DAY_STRING = "Day";

    public static final String OLD_TICKETS = "old_tickets";

    public static final String JIRA_PROJECT_ID = "jiraProjectId";

    public static final String TOTAL_SPRINT = "totalSprint";

    public static final String REMAINING_SPRINT = "remainigSprint";

    public static final String RATIO = "ratio";

    public static final String LAST_UPDATED_DATE = "lastUpdated";

    public static final String STATUS = "status";

    public static final String BACKLOG = "Backlog";

    public static final String IN_PROGRESS = "In Progress";

    public static final String DONE = "Done";

    public static final String COUNT = "count";

    public static final String COMPLETED = "Completed";

    public static final String COMPLETED_SMALL = "completed";

    public static final String ACCEPTED = "accepted";

    public static final String LABEL = "label";

    public static final String OPEN = "Open";

    public static final String TODO = "Todo"; // duplicating

    public static final String TO_DO = "To Do";

    public static final String TARGET = "target";

    public static final String CLOSED = "Closed";

    public static final String DEFAULT_ID = "0000000";

    public static final String PROJECT_ID_CAPITALIZED = "projectId";

    public static final String MEMBER_ID = "memberId";

    public static final String NULL = "null";

    public static final String JAVA = "Java";

    public static final String CSHARP = "C#";

    public static final String PYTHON = "Python";

    public static final String JAVASCRIPT = "JavaScript";

    public static final String ANDROID = "Android";

    public static final String IOS = "iOS";

    public static final String REACTJS = "ReactJS";

    public static final String HTML = "HTML";

    public static final String ESTIMATED_TIME = "estimatedTime";

    public static final String PROJECTID = "project_id";

    public static final String ID_CAPITALIZED = "id";

    public static final String ISC_PROJECT_ID_CAPITALIZED = "isc_project_id";

    public static final String LAST_ONE_TIME_PROJECT_START_UP_CRITERIA_UPDATED_DATE = "last_one_time_project_start_up_criteria_updated_date";

    public static final String LAST_ONE_TIME_PERIODIC_REVIEW_CRITERIA_UPDATED_DATE = "last_one_time_periodic_review_criteria_updated_date";

    public static final String PSR_PROJECT_ONE_TIME_CRITERIA = "psr_project_one_time_criteria";

    public static final String DATE_CAPITALIZED = "Date";

    public static final String FORMATTED_DATE = "FormattedDate";

    public static final String REGEX_URL = "https?:\\/\\/(www\\.)?[-a-zA-Z0-9@:%._\\+~#=]{1,256}\\.[a-zA-Z0-9()]{1,6}\\b([-a-zA-Z0-9()@:%_\\+.~#?&//=]*)";

    public static final String REGEX_PROJECT_NAME = "^[a-zA-Z0-9\\-\\.\\[\\]\\(\\)_/#@&\\s]+";

    public static final String REGEX_PSR_LOCATION = "https?:\\/\\/(www\\.)?[-a-zA-Z0-9@:%._\\+~#=]{1,256}\\.[a-zA-Z0-9()]{1,6}\\b([-a-zA-Z0-9()@:%_\\+.~#?&//=]*)\\b([-a-zA-Z0-9()@:%_\\+.~#?&//= \\s]*)";

    public static final String JWT = "jwt";

    public static final String BEARER = "Bearer";

    public static final String USER_ID = "usr";

    public static final String ROLE = "rol";

    public static final String STATE_SPRINT_DATA_DOLLAR = "$sprintData.state";

    public static final String JIRA_PROJECT_ID_DOUBLE_DOLLAR = "$$jiraProjectId";

    public static final String STORY_REPORT = "storyReport";

    public static final String REOPENED = "Reopened";

    public static final String IN_DEVELOPMENT = "In Development";

    public static final String REVIEW = "Review";

    public static final String TESTING = "Testing";

    public static final String CREATED_DATE_SPRINT_DATA_DOLLAR = "$sprintData.createdDate";

    public static final String NAME_SPRINT_DATA_DOLLAR = "$sprintData.name";

    public static final String ISC_PROJECT_ID_SPRINT_DATA_DOLLAR = "$sprintData.iscProjectId";

    public static final String SPRINT_ID_SPRINT_DATA_DOLLAR = "$sprintData.sprintId";

    public static final String BOARD_ID_SPRINT_DATA_DOLLAR = "$sprintData.boardId";

    public static final String PROJECT_ID_SPRINT_DATA_DOLLAR = "$sprintData.projectId";

    public static final String PROJECT_ID_DATA_DOLLAR = "$data.projectId";

    public static final String BOARD_ID_DATA_DOLLAR = "$data.boardId";

    public static final String SPRINT_ID_DATA_DOLLAR = "$data.sprintId";

    public static final String ISC_PROJECT_ID_DATA_DOLLAR = "$data.iscProjectId";

    public static final String NAME_DATA_DOLLAR = "$data.name";

    public static final String START_DATE_DATA_DOLLAR = "$data.startDate";

    public static final String END_DATE_DATA_DOLLAR = "$data.endDate";

    public static final String STATE_DATA_DOLLAR = "$data.state";

    public static final String JIRA_CHANGE_DATE_DATA_DOLLAR = "$data.jiraChangeDate";

    public static final String IS_DELETED_DATA_DOLLAR = "$data.isDeleted";

    public static final String CREATED_DATE_DATA_DOLLAR = "$data.createdDate";

    public static final String RESOLVED = "Resolved";

    public static final String TOTAL_BLOCKED = "totalBlocked";

    public static final String TOTAL_PLANNED = "totalPlanned";

    public static final String TOTAL_DELIVERED = "totalDelivered";

    public static final String TOTAL_LOGGED = "totalLogged";

    public static final String IS_DELETED_SPRINT_DATA_DOLLAR = "$sprintData.isDeleted";

    public static final String REGEX_FILENAME = "(.*\\.(xlsx|xls)$)";

    public static final String PATH_SEPERATER = "/";

    public static final String IMAGE_FILE_EXT_REGX = "(^.*\\.(?i)(jpg|jpeg|png|svg)$)";

    public static final String IMAGE_FILE_EXT_CONTAIN_REGX = "(^.*\\b(jpg|jpeg|png|svg)\\b.*$)";

    public static final List<String> STORY_TYPE = Arrays.asList("Story", "Improvement", "Enhancement");

    public static final List<String> TASK_TYPES = Arrays.asList("Task", "Sub-Task");

    public static final List<String> JIRA_STAGES = Arrays.asList("Open", "Backlog", "Reopened", "In Progress",
            "In Development", "Review", "Testing", "Done", "Resolved", "Closed");

    public static final List<String> DEFINITION_OF_TODO = Arrays.asList("Open", "Backlog", "Reopened",
            "Ready for Development");

    public static final List<String> DEFINITION_OF_IN_PROGRESS = Arrays.asList("In Progress", "In Development",
            "Review", "Testing");

    public static final List<String> DEFINITION_OF_DONE = Arrays.asList("Done", "Resolved");

    public static final List<String> BLOCKER_DEFINITION = Arrays.asList("Blocked", "Blocker");

    public static final List<String> DEFINITION_OF_ACCEPTED = Arrays.asList("Closed");

    public static final String SEMICOLON_PUNCTUATION = ";";

    public static final String COMMA_PUNCTUATION = ",";

    public static final List<String> DEFINITION_OF_DEV_COMPLETE = Arrays.asList("Dev Complete");

    public static final List<String> DEFINITION_OF_QA_COMPLETE = Arrays.asList("QA Complete");

    public static final String HISTORICAL = "historical";

    public static final String RECENT = "recent";

    public static final String SVG_TYPE = "svg";

    public static final String JIRA_BASE_URL = "https://jira.infostretch.com/browse";

    public static final String MINOR_NC = "Minor NC";

    public static final String MAJOR_NC = "Major NC";

    public static final String COMPLIANCE_ISSUES = "Compliance Issues";

    public static final String COMPLIANCE_GAPS = "Compliance Gaps";

    public static final String FORWARD_SLASH = "/";

    public static final String NEST_PROJECTS_ID = "nestProjectId";

    public static final String LOCATION = "location";

    public static final String LEAVES_DATE = "leaves.date";

    public static final String MANAGER = "Manager";

    public static final String PROJECT_ID_VALIDATION_REGEX = "^[0-9a-fA-F]{24}$";

    public static final String PROJECT_ID_VALIDATION_MESSAGE = "Invalid project ID";

    public static final String SPRINT_ID_VALIDATION_REGEX = "^[0-9]+$";

    public static final String SPRINT_ID_VALIDATION_MESSAGE = "Invalid Sprint ID";

    public static final String INDIA_TIME_ZONE = "Asia/Kolkata";

    public static final String ACCOUNT_MANAGER_VO = "accountManagersVo";

    public static final String DELIVERY_MANAGER_VO = "deliveryManagersVo";

    public static final String PROJECT_MANAGER_VO = "projectManagersVo";

    public static final String ACTIVE_DIRECTORY = "Active";

    public static final String TESTCOMPASSUTILITY_DIRECTORY = "TestCompassUtility";

    public static final String PROD = "prod";

    public static final String COMPASS_PSR = "%s-compass-psr";

    public static final List<String> INDIA_LOCATIONS = Arrays.asList("Ahmedabad", "Pune");

    public static final String SCOPE = "Scope";

    public static final String PLANNING = "Planning";

    public static final String SCHEDULE = "Schedule";

    public static final String DELIVERABLES = "Deliverables (As per SOW)";

    public static final String FINANCIALS = "Financials";

    public static final String CUSTOMER = "Customer";

    public static final String METRICS_AND_COMPLIANCE = "Metrics & Compliance";

    public static final String PROCESS = "Process";

    public static final String REPOS_REPOID = "repos.repoId";

    public static final String REPOS_SONAR_PROJECTID = "repos.sonarProjectId";

    public static final String REPOS_SONAR_PROJECTID_DOLLAR = "$repos.sonarProjectId";

    public static final String QUALITY_METRICS_SUMMARY_DOLLAR_DUPLICATION_LINEDENSITY = "$quality_metrics_summary.duplication.lineDensity";

    public static final String REPOS_SCM_REPO_URL = "repos.scmRepoUrl";

    public static final String REPOS_SCM_REPO_URL_DOLLAR = "$repos.scmRepoUrl";

    public static final String PULL_REQUESTS = "pull_requests";

    public static final String PULL_REQUESTS_DOLLAR = "$pull_requests";

    public static final String PULL_REQUESTS_PR_MERGED_BY_DOLLAR = "$pull_requests.prMergedBy";

    public static final String PULL_REQUESTS_REVIEWERS_DOLLAR = "$pull_requests.reviewers";

    public static final String PULL_REQUESTS_COMMITS = "pull_request_commits";

    public static final String PULL_REQUESTS_DOT_COMMITS = "pull_requests.commits";

    public static final String PULL_REQUESTS_DOT_COMMITS_DOLLAR = "$pull_requests.commits";

    public static final String PULL_REQUESTS_DOT_ID_DOLLAR = "$pull_requests._id";

    public static final String ISCPROJECTID_DOUBLE_DOLLAR = "$$iscProjectId";

    public static final String SCMURL_DOLLAR = "$scmUrl";

    public static final String REPO_URL_DOUBLE_DOLLAR = "$$repoUrl";

    public static final String OPEN_LOWER_CASE = "open";

    public static final String GT_DOLLAR = "$gt";

    public static final String LT_DOLLAR = "$lt";

    public static final String APPROVERS_DOLLAR = "$approvers";

    public static final String DECLINED = "declined";

    public static final String UNATTENDED = "unattended";

    public static final String TEST_CALCULATION = "testCalculation";

    public static final String TEST_CALCULATION_DOLLAR = "$testCalculation";

    public static final String PULL_REQUESTS_DOT_TOTAL_DOLLAR = "$pull_requests.total";

    public static final String PULL_REQUESTS_DOT_MERGED_DOLLAR = "$pull_requests.merged";

    public static final String PULL_REQUESTS_DOT_OPEN_DOLLAR = "$pull_requests.open";

    public static final String PULL_REQUESTS_DOT_DECLINED_DOLLAR = "$pull_requests.declined";

    public static final String PULL_REQUESTS_DOT_UNATTENDED_DOLLAR = "$pull_requests.unattended";

    public static final String OVERALL_VULNERABILITIES = "overallVulnerabilities";

    public static final String OVERALL_VULNERABILITIES_DOLLAR = "$overallVulnerabilities";

    public static final String OVERALL_BUGS = "overallBugs";

    public static final String OVERALL_BUGS_DOLLAR = "$overallBugs";

    public static final String OVERALL_CODE_COVERAGE = "overallCodeCoverage";

    public static final String OVERALL_DUPLICATIONS = "overallDuplications";

    public static final String OVERALL_DUPLICATIONS_DOLLAR = "$overallDuplications";

    public static final String OVERALL_DUPLICATIONS_BLOCKS = "overallDuplicationBlocks";

    public static final String OVERALL_DUPLICATIONS_BLOCKS_DOLLAR = "$overallDuplicationBlocks";

    public static final String NEW_BUGS = "newBugs";

    public static final String NEW_BUGS_DOLLAR = "$newBugs";

    public static final String NEW_VULNERABILITIES = "newVulnerabilities";

    public static final String NEW_VULNERABILITIES_DOLLAR = "$newVulnerabilities";

    public static final String NEW_CODE_COVERAGE = "newCodeCoverage";

    public static final String NEW_NEW_LINE_OF_CODES = "newNewLineOfCodes";

    public static final String NEW_NEW_LINE_OF_CODES_DOLLAR = "$newNewLineOfCodes";

    public static final String NEW_DUPLICATIONS = "newDuplications";

    public static final String NEW_DUPLICATIONS_DOLLAR = "$newDuplications";

    public static final String NEW_DUPLICATIONS_LINES = "newDuplicationLines";

    public static final String NEW_DUPLICATIONS_LINES_DOLLAR = "$newDuplicationLines";

    public static final String NEW_CODE_CODE_CALCULATION = "newCodeCodeCalculation";

    public static final String NEW_CODE_CODE_CALCULATION_DOLLAR = "$newCodeCodeCalculation";

    public static final String OVERALL_CODE_CALCULATION = "overallCodeCalculation";

    public static final String OVERALL_CODE_CALCULATION_DOLLAR = "$overallCodeCalculation";

    public static final String QUALITY_METRICS_SUMMARY = "quality_metrics_summary";

    public static final String QUALITY_METRICS_SUMMARY_DOLLAR = "$quality_metrics_summary";

    public static final String QUALITY_METRICS_SUMMARY_QUALITY_METRICS_BUGS = "$quality_metrics_summary.qualityMatrix.bugs";

    public static final String QUALITY_METRICS_SUMMARY_VIOLATIONS_TOTAL = "$quality_metrics_summary.violations.total";

    public static final String QUALITY_METRICS_SUMMARY_QM_COVERAGE = "$quality_metrics_summary.qualityMatrix.coverage";

    public static final String QUALITY_METRICS_SUMMARY_DUPLICATION_LINE_DENSITY = "$quality_metrics_summary.duplication.lineDensity";

    public static final String QUALITY_METRICS_SUMMARY_DUPLICATION_BLOCKS = "$quality_metrics_summary.duplication.blocks";

    public static final String QUALITY_METRICS_SUMMARY_QUALITY_MATRIX_NEW_BUGS = "$quality_metrics_summary.qualityMatrix.newBugs";

    public static final String QUALITY_METRICS_SUMMARY_NEW_VIOLATIONS_TOTAL = "$quality_metrics_summary.newViolations.total";

    public static final String QUALITY_METRICS_SUMMARY_QUALITY_MATRIX_NEW_COVERAGE = "$quality_metrics_summary.qualityMatrix.newCoverage";

    public static final String QUALITY_METRICS_SUMMARY_CODE_MATRIX_NCLOC = "$quality_metrics_summary.codeMatrix.ncloc";

    public static final String QUALITY_METRICS_SUMMARY_CODE_DUP_NEW_LINE_DENSITY = "$quality_metrics_summary.duplication.newLineDensity";

    public static final String QUALITY_METRICS_SUMMARY_CODE_DUP_NEW_LINES = "$quality_metrics_summary.duplication.newLines";

    public static final String TECH_DEBT_SUMMARY_DOT_COMPLEXITY_DOLLAR = "$tech_debt_summary.complexity";

    public static final String TECH_DEBT_SUMMARY_DOT_CODEMATRIX_DOT_NCLOC_DOLLAR = "$tech_debt_summary.codeMatrix.ncloc";

    public static final String TECH_DEBT_SUMMARY_DOT_COMPLEXITY = "$tech_debt_summary.complexity";

    public static final String TECH_DEBT_SUMMARY_DOT_CONG_COMPLEXITY_DOLLAR = "$tech_debt_summary.cognitiveComplexity";

    public static final String TECH_DEBT_SUMMARY_QUERY_PARAM_DUPLICATION_LINES = "$tech_debt_summary.duplication.lines";

    public static final String PULL_REQUESTS_SCM_AUTHOR_NAME = "$pull_requests.scmAuthor.name";

    public static final String PULL_REQUESTS_STATE = "$pull_requests.state";

    public static final String OPEN_PRS = "openPrs";

    public static final String DECLINED_PRS = "declinedPrs";

    public static final String REVIEWER_COMMENTS = "reviewerComments";

    public static final String PULL_REQUESTS_COMMENTS = "$pull_requests.comments";

    public static final String PULL_REQUESTS_PR_CREATED_TIME = "$pull_requests.prCreatedTime";

    public static final String PULL_REQUESTS_COMMENTS_NAME = "$pull_requests.comments.name";

    public static final String RECOMMITS = "recommits";

    public static final String COMMIT = "commit";

    public static final String COMMIT_TIME_DOUBLE_DOLLAR = "$$commit.time";

    public static final String FILTER_DOLLAR = "$filter";

    public static final String NE_DOLLAR = "$ne";

    public static final String INPUT = "input";

    public static final String TEST_CODE_COVERAGE_DOLLAR = "$testCodeCoverage";

    public static final String EFFICIENCY_DOLLAR = "$efficiency";

    public static final String ROBUSTNESS_DOLLAR = "$robustness";

    public static final String VULNERABILITIES_DOLLAR = "$vulnerabilities";

    public static final String COMPLIANCE_DOLLAR = "$compliance";

    public static final String COUNT_DOLLAR = "$count";

    public static final String SONAR_CONFIGURATION = "sonar_configuration";

    public static final String LANGUAGE = "language";

    public static final String LANGUAGE_DOLLAR = "$language";

    public static final String VIOLATIONS_TOTAL = "$violations.total";

    public static final String REPOS_REPONAME_DOLLAR = "$repos.repoName";

    public static final String SONAR_CONFIG_DATA_PROJECTS = "$sonar_config_data.projects";

    public static final String SONAR_CONFIG_DATA_PROJECTS_LANG = "$sonar_config_data.projects.language";

    public static final String SONAR_CONFIG_DATA_PROJECTS_KEY = "$sonar_config_data.projects.key";

    public static final String VIOLATIONS_DOT_VIOLATIONS = "$violations.violations";

    public static final String REPOS_LANGUAGE = "reposLanguage";

    public static final String CMP = "cmp";

    public static final String CMP_DOLLAR = "$cmp";

    public static final String SONAR_CONFIG_DATA = "sonar_config_data";

    public static final String SONAR_CONFIG_DATA_DOLLAR = "$sonar_config_data";

    public static final String SUBTRACT_DOLLAR = "$subtract";

    public static final String PULL_REQUESTS_REVIEWERS = "$pull_requests.reviewers";

    public static final String ELSE = "else";

    public static final String PULL_REQUESTS_REVIEWERS_NAME = "$pull_requests.reviewers.name";

    public static final String PULL_REQUESTS_APPROVERS_NAME = "$pull_requests.approvers.name";

    public static final String PULL_REQUESTS_PR_MERGE_TIME = "$pull_requests.prMergeTime";

    public static final String PULL_REQUESTS_PR_DECLINED_TIME = "$pull_requests.prDeclinedTime";

    public static final String PULL_REQUESTS_PR_DECLINED_BY = "$pull_requests.prDeclinedBy";

    public static final String IF = "if";

    public static final String TOTAL_TIME_FOR_MERGED_PR = "totalTimeForMergedPr";

    public static final String PULL_REQUESTS_PR_MERGED_BY = "$pull_requests.prMergedBy";

    public static final String TOTAL_TIME_FOR_DECLINED_PR = "totalTimeForDeclinedPr";

    public static final String TOTAL_TIME_FOR_OPEN_PR = "totalTimeForOpenPr";

    public static final String VIOLATIONS_DOLLAR = "$violations";

    public static final String REPOS_REPOID_DOLLAR = "$repos.repoId";

    public static final String SCM_CODE_STAT_ANALYSIS = "scm_code_statistical_analysis";

    public static final String SCM_REPO_URL_DOUBLE_DOLLAR = "$$scmRepoUrl";

    public static final String CODE_ANALYSIS_DOLLAR = "$code_analysis";

    public static final String OVERALL_DATA = "overallData";

    public static final String OVERALL_DATA_DOLLAR = "$overallData";

    public static final String FIRST = "first";

    public static final String FIRST_DOLLAR = "$first";

    public static final String CODE_ANALYSIS_TOTAL_LINE_CODE = "$code_analysis.totalLineOfCode";

    public static final String AUTHORS_DATA = "authorsData";

    public static final String AUTHORS_DATA_DOLLAR = "$authorsData";

    public static final String OVERALL_DATA_TOTAL_LOC = "$overallData.totalLineOfCode";

    public static final String REMOVED_LOC_BY_AUTHOR_90_DAYS = "removedLineOfCodeByAuthorInLast90Days";

    public static final String ADDED_LOC_BY_AUTHOR_90_DAYS = "addedLineOfCodeByAuthorInLast90Days";

    public static final String REMOVED_LOC_BY_AUTHOR_30_DAYS = "removedLineOfCodeByAuthorInLast30Days";

    public static final String ADDED_LOC_BY_AUTHOR_30_DAYS = "addedLineOfCodeByAuthorInLast30Days";

    public static final String REMOVED_LOC_BY_AUTHOR_15_DAYS = "removedLineOfCodeByAuthorInLast15Days";

    public static final String ADDED_LOC_BY_AUTHOR_15_DAYS = "addedLineOfCodeByAuthorInLast15Days";

    public static final String REMOVED_LOC_BY_AUTHOR_7_DAYS = "removedLineOfCodeByAuthorInLast7Days";

    public static final String ADDED_LOC_BY_AUTHOR_7_DAYS = "addedLineOfCodeByAuthorInLast7Days";

    public static final String CODE_ANALYSIS_ADDED_LOC_AUTHOR_7_DAYS = "$code_analysis.authors.addedLineOfCodeByAuthorInLast7Days";

    public static final String CODE_ANALYSIS_REMOVED_LOC_AUTHOR_7_DAYS = "$code_analysis.authors.removedLineOfCodeByAuthorInLast7Days";

    public static final String CODE_ANALYSIS_ADDED_LOC_AUTHOR_15_DAYS = "$code_analysis.authors.addedLineOfCodeByAuthorInLast15Days";

    public static final String CODE_ANALYSIS_REMOVED_LOC_AUTHOR_15_DAYS = "$code_analysis.authors.removedLineOfCodeByAuthorInLast15Days";

    public static final String CODE_ANALYSIS_ADDED_LOC_AUTHOR_30_DAYS = "$code_analysis.authors.addedLineOfCodeByAuthorInLast30Days";

    public static final String CODE_ANALYSIS_REMOVED_LOC_AUTHOR_30_DAYS = "$code_analysis.authors.removedLineOfCodeByAuthorInLast30Days";

    public static final String CODE_ANALYSIS_ADDED_LOC_AUTHOR_90_DAYS = "$code_analysis.authors.addedLineOfCodeByAuthorInLast90Days";

    public static final String CODE_ANALYSIS_REMOVED_LOC_AUTHOR_90_DAYS = "$code_analysis.authors.removedLineOfCodeByAuthorInLast90Days";

    public static final String RISK_MEASUREMENTS = "risk_measurements";

    public static final String RISK_MEASUREMENTS_DOLLAR = "$risk_measurements";

    public static final String CURRENT_DATA = "currentData";

    public static final String CURRENT_DATA_DOLLAR = "$currentData";

    public static final String PREVIOUS_DATA = "previousData";

    public static final String PREVIOUS_DATA_DOLLAR = "$previousData";

    public static final String PROJECT_CAPS = "Project";

    public static final String RISK_MEASUREMENTS_CREATED_DATE_DOLLAR = "$risk_measurements.createdDate";

    public static final String RISK_MEASUREMENTS_CREATED_DATE = "risk_measurements.createdDate";

    public static final String ARRAY_ELEM_AT_DOLLAR = "$arrayElemAt";

    public static final String SET_UNION = "setUnion";

    public static final String SET_UNION_DOLLAR = "$setUnion";

    public static final String NEW_ROOT = "newRoot";

    public static final String PR_METRICS_DOLLAR = "$prMetrics";

    public static final String REVIEW_METRICS = "reviewMetrics";

    public static final String REVIEW_METRICS_DOLLAR = "$reviewMetrics";

    public static final String ALL_METRICS = "allMetrics";

    public static final String ALL_METRICS_DOLLAR = "$allMetrics";

    public static final String REPLACE_ROOT = "replaceRoot";

    public static final String REPLACE_ROOT_DOLLAR = "$replaceRoot";

    public static final String REPO_URL_CAMEL_CASE_DOLLAR = "$repoUrl";

    public static final String SPRINT_DATA_SPRINT_ID = "$sprintData.sprintId";

    public static final String ID_ISC_PROJECT_ID = "$_id.projectId";

    public static final String ID_JIRA_PROJECT_ID = "$_id.jiraProjectId";

    public static final String DOUBLE_DOLLAR_ISC_PROJECT_ID = "$$iscProject_id";

    public static final String MONTH_CREATED_DATE = "month.createdDate";

    public static final String COVERAGE = "coverage";

    public static final String DOLLAR_STATUS_DATA = "$statusData";

    public static final String DOLLAR_CREATED_DATE = "$createdDate";

    public static final String DOLLAR_REPO_ID = "$repoId";

    public static final String DOUBLE_DOLLAR_SONAR_PROJECT_ID = "$$sonar_project_id";

    public static final String DOLLAR_MONTH = "$month";

    public static final String DOLLAR_TO_DATE = "$toDate";

    public static final String DATA_ID_MONTH = "$data._id.month";

    public static final String DOLLAR_YEAR = "$year";

    public static final String DATA_ID_YEAR = "$data._id.year";

    public static final String DOLLAR_END_DATE = "$endDate";

    public static final String DOLLAR_CONCAT = "$concat";

    public static final String DOLLAR_REPO_NAME = "$repoName";

    public static final String DOLLAR_ID_MONTH = "$_id.month";

    public static final String DOLLAR_ID_YEAR = "$_id.year";

    public static final String MONTH_DATA = "monthData";

}
