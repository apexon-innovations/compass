package com.apexon.compass.constants;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import static com.apexon.compass.constants.PsrServiceConstants.AINITIAL;
import static com.apexon.compass.constants.PsrServiceConstants.GINITIAL;
import static com.apexon.compass.constants.PsrServiceConstants.NA;
import static com.apexon.compass.constants.PsrServiceConstants.RINITIAL;

import java.util.Arrays;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CompassUtilityConstants {

    public static final String INFOSTRETCH_DOMAIN = "infostretch.com";

    public static final String EMAIL_REGX = "^([a-zA-Z0-9_\\-\\.]+)@([a-zA-Z0-9_\\-\\.]+)\\.([a-zA-Z]{2,5})$";

    public static final String INT_REGX = "^[0-9]+$";

    public static final String DOUBLE_REGX = "^(\\d{0,10}|\\d{0,10}\\.\\d{1,2})$";

    public static final String DATE_FORMAT_EXCEL = "E MMM dd yyyy HH:mm:ss 'GMT'z";

    public static final String[] STATUS_CODE = { RINITIAL, AINITIAL, GINITIAL, NA };

    public static final List<String> PROJECT_CATEGORIES = Arrays.asList("Devlopment", "Manual & Automation QA",
            "Development & Manual QA", "Development & Automation QA", "Devops", "AWS", "Support", "Other");

    public static final List<String> RESOURCE_STATUS_LIST = Arrays.asList(
            "No visible attrition/Right Resouruces on board", "Expected Attrition/Need better resources",
            "Insufficient resources. Quality and timeline will be impacted.");

    public static final List<String> SOLUTION_TYPE_DISTRIBUTIONS = Arrays.asList("Inception/Ideation", "Support",
            "Revamping the system", "Maintenance");

    public static final List<String> INDUSTRY_TYPE_SPECIFIC_EXPOSURES = Arrays.asList("Financial services",
            "Government", "Retails", "Insurance", "Telecommunications", "Manufacturing", "IT & Business Consulting",
            "Automobile", "Health Care", "Others");

    public static final List<String> PROJECT_TYPES = Arrays.asList("Fixed Price", "T&M", "Internal",
            "Delivery Outcome-Based Pricing");

    public static final String EMPTY_FILE_ERROR_MESSAGE = "File Must not be empty.";

    public static final String FILE_TYPE_ERROR = "File must be from any of the type: ";

    public static final String COMMA = ",";

    public static final String SUCCESS = "Success";

    public static final String PSR_UPLOAD_SUCCES_MESSAGE = "PSR uploaded successfully";

    public static final String INCORRECT_SVN_URL = "Incorrect SVN url";

    public static final String SVN_URL_CONNECTION_ERROR = "Unable to connect to a repository at URL '";

    public static final String VPN_NOT_CONNECTED = "because VPN is not connected.";

    public static final String SINGLE_QUOTE = "'";

    public static final String FILE_CREATION_ERROR = "Error while file creation";

    public static final String SVN = "svn";

    public static final String SCM_SVN_URL_PREFIX = "scm:svn:";

    public static final String SCM_EXCEPTION = "Something went wrong with SVM:";

    public static final String IS_SUCCESS = "Is process succeed?:";

    public static final String CHECKOUT_PROCESS = "CHECKOUT PROCESS";

    public static final String ADD_PROCESS = "ADD PROCESS";

    public static final String COMMIT_PROCESS = "COMMIT PROCESS";

    public static final String CHECKOUT_PROCESS_NOT_SUCCEED = "Checkout process does not succeed.";

    public static final String ADD_PROCESS_NOT_SUCCEED = "Add process does not succeed.";

    public static final String COMMIT_PROCESS_NOT_SUCCEED = "Commit process does not succeed.";

    public static final String FILE_DELETION_ERROR_MESSAGE = "Error in file deletion.";

    public static final String SVN_URL = "SVN url:";

    public static final String PSR_DASHBOARD = "psrDashboard";

    public static final String PROCESS_CHECKLIST = "processChecklist";

}
