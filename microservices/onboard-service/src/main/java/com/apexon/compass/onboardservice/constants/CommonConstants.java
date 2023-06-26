package com.apexon.compass.onboardservice.constants;

import com.fasterxml.jackson.annotation.JsonAlias;
import java.util.Arrays;
import java.util.List;

public class CommonConstants {

    public static final List<String> METRIC_NAMES_LIST = Arrays.asList("new_technical_debt", "blocker_violations",
            "bugs", "burned_budget", "business_value", "classes", "code_smells", "cognitive_complexity",
            "comment_lines", "comment_lines_density", "branch_coverage", "new_branch_coverage", "conditions_to_cover",
            "new_conditions_to_cover", "confirmed_issues", "coverage", "new_coverage", "critical_violations",
            "complexity", "directories", "duplicated_blocks", "new_duplicated_blocks", "duplicated_files",
            "duplicated_lines", "duplicated_lines_density", "new_duplicated_lines", "new_duplicated_lines_density",
            "effort_to_reach_maintainability_rating_a", "false_positive_issues", "files", "functions",
            "generated_lines", "generated_ncloc", "info_violations", "violations", "line_coverage", "new_line_coverage",
            "lines", "ncloc", "lines_to_cover", "new_lines_to_cover", "sqale_rating", "new_maintainability_rating",
            "major_violations", "minor_violations", "new_blocker_violations", "new_bugs", "new_code_smells",
            "new_critical_violations", "new_info_violations", "new_violations", "new_lines", "new_major_violations",
            "new_minor_violations", "new_vulnerabilities", "open_issues", "projects", "alert_status",
            "reliability_rating", "new_reliability_rating", "reliability_remediation_effort",
            "new_reliability_remediation_effort", "reopened_issues", "security_rating", "new_security_rating",
            "security_remediation_effort", "new_security_remediation_effort", "skipped_tests", "statements",
            "team_size", "sqale_index", "sqale_debt_ratio", "new_sqale_debt_ratio", "uncovered_conditions",
            "new_uncovered_conditions", "uncovered_lines", "new_uncovered_lines", "test_execution_time", "test_errors",
            "test_failures", "test_success_density", "tests", "vulnerabilities", "wont_fix_issues");

    public static final String DATA = "data";

    public static final String CREATOR_NAME = "Onboard Service";

    public static final String JIRA_QUERY_ENDPOINT = "rest/api/2/";

    public static final String IMAGE_FILE_EXT_CONTAIN_REGX = "(^.*\\b(jpg|jpeg|png|svg)\\b.*$)";

    public static final String IMAGE_FILE_EXT_REGX = "(^.*\\.(?i)(jpg|jpeg|png|svg)$)";

    public static final String COMPASS_IMAGES = "compass-images";

    public enum Source {

        @JsonAlias({ "Bitbucket", "bitbucket" })
        BITBUCKET, @JsonAlias({ "Github", "github" })
        GITHUB

    }

    public enum Product {

        @JsonAlias({ "cloud", "Cloud" })
        CLOUD, @JsonAlias({ "Server", "server" })
        SERVER

    }

}
