package com.apexon.compass.dashboard.config;

import com.apexon.compass.dashboard.collector.FeatureSettings;
import com.apexon.compass.dashboard.model.DesJiraConfiguration;
import com.apexon.compass.dashboard.model.JiraConfiguration;
import org.mockito.Mockito;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.core.annotation.Order;
import org.springframework.scheduling.TaskScheduler;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Spring context configuration for Testing purposes
 */
@Order(1)
@Configuration
@ComponentScan(
		basePackages = { "com.capitalone.dashboard.model", "com.capitalone.dashboard.collector",
				"com.capitalone.dashboard.client" },
		includeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, value = FeatureSettings.class))
public class TestConfig {

	@Bean
	public FeatureSettings featureSettings() {
		FeatureSettings settings = new FeatureSettings();
		settings.setCron("* * * * * *");
		settings.setJiraBaseUrl("https://jira.com/");
		settings.setJiraTeamFieldName("customfield_11248");
		settings.setJiraSprintDataFieldName("customfield_10007");
		settings.setJiraEpicIdFieldName("customfield_10003");
		settings.setJiraStoryPointsFieldName("customfield_10004");
		settings.setMaxNumberOfFeaturesPerBoard(10000);

		return settings;
	}

	@Bean
	public DesJiraConfiguration desJiraConfiguration() {
		JiraConfiguration jiraConfiguration = new JiraConfiguration();

		jiraConfiguration.setJiraProjectId("13700");
		jiraConfiguration.setJiraBoardId(Arrays.asList("999"));
		jiraConfiguration.setUrl("https://jira.com");
		jiraConfiguration.setJiraTeamFieldName("customfield_11248");
		jiraConfiguration.setSprintDataFieldName("customfield_10007");
		jiraConfiguration.setJiraEpicIdFieldName("customfield_10003");
		jiraConfiguration.setJiraStoryPointsFieldName("customfield_10004");
		jiraConfiguration.setCron("* * * * * *");
		jiraConfiguration.setJiraBoardAsTeam(true);
		Map<String, String> issueTypesId = new HashMap<>();
		issueTypesId.put("Story", "7");
		issueTypesId.put("Epic", "6");
		jiraConfiguration.setIssueTypesId(issueTypesId);
		jiraConfiguration.setUpdatedDate(1585023552000L);

		DesJiraConfiguration desJiraConfiguration = new DesJiraConfiguration();
		desJiraConfiguration.setJiraConfigurations(Arrays.asList(jiraConfiguration));

		return desJiraConfiguration;
	}

	@Bean
	public TaskScheduler taskScheduler() {
		return Mockito.mock(TaskScheduler.class);
	}

}
