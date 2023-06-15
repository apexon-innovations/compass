package com.apexon.compass.constants;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.List;

public class PsrServiceConstants {

    private PsrServiceConstants() {
    }

    public static final String BASE_PACKAGE = "com.apexon.compass";

    public static final SimpleDateFormat MONGO_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");

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

    public static final String BLOCKER = "Blocker";

    public static final String COMPLETEDTASKS = "completedTasks";

    public static final String ACCEPTEDTASKS = "acceptedTasks";

    public static final String JIRAPROJECTID = "jiraProjectId";

    public static final String HEALTH = "Health";

    public static final String HEADER = "header";

    public static final String MORE_THAN = "More than ";

    public static final String PERCENTAGE = "%";

    public static final String PEOPLE = "People";

    public static final String KEY = "key";

    public static final String VALUE = "value";

    public static final String INDUSTRY_TYPE = "industryType";

    public static final String SOLUTION_TYPE = "solutionType";

    public static final String PROJECT_COUNT = "projectCount";

    public static final String TEAM_SIZE = "teamSize";

    public static final String OWNERS_ID = "$ownerIds";

    public static final String TOTAL = "total";

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

    public static final String NAME = "name";

    public static final String JIRA_CHANGE_DATE = "jiraChangeDate";

    public static final String JIRA_CHANGE_DATE_SPRINT_DATA_DOLLAR = "$sprintData.jiraChangeDate";

    public static final String PROJECT_NUMBER = "numberOfProjects";

    public static final String SCORE = "score";

    public static final String ON_TRACK = "On Track";

    public static final String DELAY = "Delay";

    public static final String CRITICAL = "Critical";

    public static final String GREENCAP = "Green";

    public static final String AMBERCAP = "Amber";

    public static final String REDCAP = "Red";

    public static final String SPRINT_NAME = "sprintName";

    public static final String START_DATE = "startDate";

    public static final String START_DATE_SPRINT_DATA_DOLLAR = "$sprintData.startDate";

    public static final String TEAM = "team";

    public static final String PERSON_NAME = "personName";

    public static final String STORY_POINTS = "storyPointCompleted";

    public static final String ACTIVE = "active";

    public static final String BLOCKERS = "blockers";

    public static final String STATUS_DOLLAR = "$status";

    public static final String BOARD_ID_DOLLAR = "$boardId";

    public static final String DATE = "date";

    public static final String STORIES = "stories";

    public static final String REMAINING_EFFORTS = "remainingEfforts";

    public static final String REMAINING_TASKS = "remainingTasks";

    public static final String COMPLETED_TASKS = "completedTasks";

    public static final String TOTAL_OPEN = "totalOpen";

    public static final String SPRINT_TICKETS = "sprint_tickets";

    public static final String IDEAL_BURNDOWN = "idealBurndown";

    public static final String DAY_STRING = "Day";

    public static final String DATA = "data";

    public static final String OLD_TICKETS = "old_tickets";

    public static final String PROJECT_NAME = "projectName";

    public static final String JIRA_PROJECT_ID = "jiraProjectId";

    public static final String PROJECT_ID = "projectId";

    public static final String TOTAL_SPRINT = "totalSprint";

    public static final String SPRINT_ID = "sprintId";

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

    public static final List<String> ROLES = Arrays.asList("PROJECT_CLIENT", "PDO", "PROJECT_MANAGER", "DES_ADMIN",
            "COE_MANAGER");

    public static final String PM_EMAIL = "pm.email";

    public static final String DM_EMAIL = "dm.email";

    public static final String AM_EMAIL = "am.email";

    public static final String CLIENT_PM_EMAIL = "clientPm.email";

    public static final String ID = "_id";

    public static final String FROM = "from";

    public static final String STATE = "state";

    public static final String STATE_SPRINT_DATA_DOLLAR = "$sprintData.state";

    public static final String LOOKUP = "$lookup";

    public static final String JIRA_PROJECT_ID_DOUBLE_DOLLAR = "$$jiraProjectId";

    public static final String SPRINTS = "sprints";

    public static final String FUTURE = "future";

    public static final String STORY_REPORT = "storyReport";

    public static final String REOPENED = "Reopened";

    public static final String IN_DEVELOPMENT = "In Development";

    public static final String REVIEW = "Review";

    public static final String TESTING = "Testing";

    public static final String CREATED_DATE = "createdDate";

    public static final String CREATED_DATE_SPRINT_DATA_DOLLAR = "$sprintData.createdDate";

    public static final String NAME_SPRINT_DATA_DOLLAR = "$sprintData.name";

    public static final String ISC_PROJECT_ID_SPRINT_DATA_DOLLAR = "$sprintData.projectId";

    public static final String SPRINT_ID_SPRINT_DATA_DOLLAR = "$sprintData.sprintId";

    public static final String BOARD_ID_SPRINT_DATA_DOLLAR = "$sprintData.boardId";

    public static final String PROJECT_ID_SPRINT_DATA_DOLLAR = "$sprintData.jiraProjectId";

    public static final String PROJECT_ID_DATA_DOLLAR = "$data.projectId";

    public static final String BOARD_ID_DATA_DOLLAR = "$data.boardId";

    public static final String SPRINT_ID_DATA_DOLLAR = "$data.sprintId";

    public static final String ISC_PROJECT_ID_DATA_DOLLAR = "$data.projectId";

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

    public static final String X_USER_HEADER = "X-User-Header";

    public static final String IS_DELETED = "isDeleted";

    public static final String IS_DELETED_SPRINT_DATA_DOLLAR = "$sprintData.isDeleted";

    public static final String PROJECT__ID = "projectId";

    public static final List<String> STORY_TYPES = Arrays.asList("Story", "Sprint-Bug", "Enhancement");

    public static final String REGEX_FILENAME = "(.*\\.(xlsx|xls)$)";

    public static final String BUCKETNAME = "%s-isc-project-logos";

    public static final String PATH_SEPERATER = "/";

    public static final String IMAGE_FILE_EXT_REGX = "(^.*\\.(?i)(jpg|jpeg|png|svg)$)";

    public static final String IMAGE_FILE_EXT_CONTAIN_REGX = "(^.*\\b(jpg|jpeg|png|svg)\\b.*$)";

    public static final List<String> STORY_TYPE = Arrays.asList("Story", "Improvement", "Enhancement");

    public static final List<String> BUG_TYPES = Arrays.asList("Bug", "Sprint-Bug");

    public static final List<String> TASK_TYPES = Arrays.asList("Task", "Sub-Task");

    public static final List<String> JIRA_STAGES = Arrays.asList(OPEN, BACKLOG, REOPENED, IN_PROGRESS, IN_DEVELOPMENT,
            REVIEW, TESTING, DONE, RESOLVED, CLOSED);

    public static final List<String> DEFINITION_OF_TODO = Arrays.asList("Open", "Backlog", "Reopened",
            "Ready for Development");

    public static final List<String> DEFINITION_OF_IN_PROGRESS = Arrays.asList("In Progress", "In Development",
            "Review", "Testing");

    public static final List<String> DEFINITION_OF_DONE = Arrays.asList("Done", "Resolved");

    public static final List<String> BLOCKER_DEFINITION = Arrays.asList("Blocked", "Blocker");

    public static final List<String> DEFINITION_OF_ACCEPTED = Arrays.asList("Closed");

    public static final String SEMICOLON_PUNCTUATION = ";";

    public static final String COMMA_PUNCTUATION = ",";

    public static final String FULLSTOP_PUNCTUATION = ".";

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

    public static final String BOARD_ID = "boardId";

    public static final List<String> INDIA_LOCATIONS = Arrays.asList("Ahmedabad", "Pune");

    public static final String SCOPE = "Scope";

    public static final String PLANNING = "Planning";

    public static final String SCHEDULE = "Schedule";

    public static final String DELIVERABLES = "Deliverables (As per SOW)";

    public static final String FINANCIALS = "Financials";

    public static final String CUSTOMER = "Customer";

    public static final String METRICS_AND_COMPLIANCE = "Metrics & Compliance";

    public static final String PROCESS = "Process";

    public static final String[] FEATURES = { TO_DO, IN_PROGRESS, COMPLETED };

}
