package com.apexon.compass.dashboard.status;

public enum BuildAuditStatus {

	BUILD_JOB_IS_NON_PROD, BUILD_JOB_CONFIGURATION_REVIEW_FAIL, BUILD_JOB_CONFIGURATION_REVIEW_PASS,
	BUILD_JOB_CONFIGURATION_NO_CHANGE, COLLECTOR_ITEM_ERROR, BUILD_MATCHES_REPO, BUILD_REPO_MISMATCH,
	BUILD_AUTHOR_EQ_REPO_AUTHOR, BUILD_AUTHOR_NE_REPO_AUTHOR, NO_BUILD_FOUND, BUILD_JOB_IS_PROD

}
