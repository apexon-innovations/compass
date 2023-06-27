
package com.apexon.compass.dashboard.util;

/**
 * This class is established to house any globally-referenced constant values. Most values
 * that can or should be modifiable at deploy-time should be kept in a properties file,
 * but valid use cases for constant values should be added here.
 *
 * @author kfk884
 *
 */
public final class FeatureCollectorConstants {

	public static final String JIRA = "Jira";

	public static final String JIRA_XRAY = "Jira XRay";

	public static final String VERSIONONE = "VersionOne";

	public static final String GITLAB = "GitlabFeature";

	@SuppressWarnings("PMD.AvoidUsingHardCodedIP")
	// not an IP
	public static final String AGENT_VER = "01.00.00.01";

	public static final String AGENT_NAME = "Hygieia Dashboard - VersionOne Feature Collector";

	public static final String STORY_HOURS_ESTIMATE = "hours";

	public static final String STORY_POINTS_ESTIMATE = "storypoints";

	public static final String STORY_COUNT_ESTIMATE = "count";

	public static final String SPRINT_SCRUM = "scrum";

	public static final String SPRINT_KANBAN = "kanban";

	public static final String TEAM_ID_ANY = "any";

	public static final String PROJECT_ID_ANY = "any";

	public static final String TOOL_TYPE = "featureTool";

	public static final String PROJECT_NAME = "projectName";

	public static final String PROJECT_ID = "projectId";

	public static final String TEAM_NAME = "teamName";

	public static final String TEAM_ID = "teamId";

	public static final String ESTIMATE_METRIC_TYPE = "estimateMetricType";

	public static final String SPRINT_TYPE = "sprintType";

	public static final String LIST_TYPE = "listType";

	public static final String SHOW_STATUS = "showStatus";

	private FeatureCollectorConstants() {
		// This class should not be instantiable
	}

}