package com.apexon.compass.constants;

import static com.apexon.compass.constants.PsrServiceConstants.FULLSTOP_PUNCTUATION;

import java.util.Arrays;
import java.util.List;

public class StrategyServiceConstants {

    private StrategyServiceConstants() {
    }

    public static final int[] values = { 5, 4, 3, 2, 1 };

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

    public static final String MATCH = "$match";

    public static final String UNWIND = "$unwind";

    public static final String PROJECTS = "$projects";

    public static final String LOOKUP = "$lookup";

    public static final String FROM = "from";

    public static final String LET = "let";

    public static final String PROJECTKEY = "projectKey";

    public static final String PROJECT_KEY = "$projects.key";

    public static final String PIPELINE = "pipeline";

    public static final String EXPRESSION = "$expr";

    public static final String EQUAL = "$eq";

    public static final String SONARPROJECTID = "sonarProjectId";

    public static final String SORT = "$sort";

    public static final String LIMIT = "$limit";

    public static final String PROJECT = "$project";

    public static final String RATINGS_SECURITY = "ratings.security";

    public static final String RATINGS_MAINTAINABILITY = "ratings.maintainability";

    public static final String RATINGS_RELIABILITY = "ratings.reliability";

    public static final String AS = "as";

    public static final String QUALITY_MEASUREMENTS = "quality_measurements";

    public static final String GROUP = "$group";

    public static final String AVG = "$avg";

    public static final String QUALITY_MEASUREMENTS_PARAM = "$quality_measurements";

    public static final String SONAR_PROJECT_ID_QUERY_PRAM = "$sonarProjectId";

    public static final String PROJECTKEY_QUERY_PRAM = "$$projectKey";

    public static final String VIOLATIONS_BLOCKER = "violations.blocker";

    public static final String VIOLATIONS_CRITICAL = "violations.critical";

    public static final String QUAILTY_MATRIX_COVERAGE = "qualityMatrix.coverage";

    public static final String SONAR_PROJECT_ID = "sonarProjectId";

    public static final String ID_PRE_UNDER = "_id";

    public static final String TEST_CODE_COVERAGE = "testCodeCoverage";

    public static final String MULTIPLY = "$multiply";

    public static final String QUALITY_MEASUREMENTS_QUALITY_MATRIX_COVERAGE = "$quality_measurements.qualityMatrix.coverage";

    public static final String SWITCH = "$switch";

    public static final String BRANCHES = "branches";

    public static final String CASE = "case";

    public static final String AND = "$and";

    public static final String GTE = "$gte";

    public static final String QUALITY_MEASUREMENT_VIOLATION_BLOCKER_AND_CRITICAL = "$quality_measurements.violations.blocker,$quality_measurements.violations.critical";

    public static final String LTE = "$lte";

    public static final String THEN = "then";

    public static final String DEFAULT = "default";

    public static final String QUALITY_MEASUREMENTS_RATINGS_SECURITY = "$quality_measurements.ratings.security";

    public static final String QUALITY_MEASUREMENTS_RATINGS_RELIABILITY = "$quality_measurements.ratings.reliability";

    public static final String QUALITY_MEASUREMENTS_RATINGS_MAINTAINABILITY = "$quality_measurements.ratings.maintainability";

    public static final String ACTIVE = "active";

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

    public static final String SUM = "$sum";

    public static final String ADD = "$add";

    public static final String TECH_DEBT_SUMMARY_QUERY_PARAM = "$tech_debt_summary";

    public static final String TECHNICAL_DEBT_SUMMARY = "tech_debt_summary";

    public static final String TECHNICAL_DEBT_SUMMARY_SONAR_CONFIGURATION_ID = "$tech_debt_summary.sonarConfigurationId";

    public static final String COMPLEXITY = "complexity";

    public static final String TESTS = "tests";

    public static final String CONGNITIVE_COMPLEXITY = "cognitiveComplexity";

    public static final String SIZE = "size";

    public static final String ISSUES = "issues";

    public static final String MAINTAINABILITY = "maintainability";

    public static final String RELIABILITY = "reliability";

    public static final String DUPLICATION = "duplication";

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

    public static final String SCM_ID = "$_id";

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

    public static final String REVIEW_METRICS = "reviewMetrics";

    public static final String COMMENTS = "$comments";

    public static final String ALL_METRICS = "allMetrics";

    public static final String SET_UNION = "$setUnion";

    public static final String REPLACE_ROOT = "$replaceRoot";

    public static final String NEW_ROOT = "newRoot";

    public static final String CODE_COMMITS = "codeCommits";

    public static final String MERGED_PRS = "mergedPrs";

    public static final String TOTAL_PRS = "totalPrs";

    public static final String REVIEW_COMMENTS_ON_OTHER_PR = "reviewCommentsOnOthersPrs";

    public static final String OR = "$or";

    public static final String PULL_REQUEST_ID = "pullRequestId";

    public static final String COMMITS = "commits";

    public static final String SCM_AUTHOR_NAME = "$scmAuthor.name";

    public static final String COND = "$cond";

    public static final String IN = "$in";

    public static final String STATE = "$state";

    public static final String MERGED = "merged";

    public static final String COMMENTS_NAME = "$comments.name";

    public static final String NE = "$ne";

    public static final String ADDED_LINE_OF_CODE = "addedLineOfCode";

    public static final String REMOVED_LINE_OF_CODE = "removedLineOfCode";

    public static final String CODE_ANALYSIS_AUTHORS = "$code_analysis.authors";

    public static final String ADDED_LINE_OF_CODE_TILL_DATE = "addedLineOfCodeTillDate";

    public static final String REMOVED_LINE_OF_CODE_TILL_DATE = "removedLineOfCodeTillDate";

    public static final String MINUTES = "Minutes";

}
