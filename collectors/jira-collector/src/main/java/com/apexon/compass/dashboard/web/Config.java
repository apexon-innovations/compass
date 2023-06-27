package com.apexon.compass.dashboard.web;

import com.apexon.compass.dashboard.model.DesJiraConfiguration;
import com.apexon.compass.dashboard.model.JiraConfiguration;
import com.apexon.compass.dashboard.repository.JiraConfigurationRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Configuration
public class Config {

	@Bean(name = "desJiraConfiguration")
	public DesJiraConfiguration init(JiraConfigurationRepository jiraConfigurationRepository) {
		Iterable<JiraConfiguration> jiraConfigurations = jiraConfigurationRepository.findAll();

		DesJiraConfiguration desJiraConfiguration = new DesJiraConfiguration();
		desJiraConfiguration.setJiraConfigurations(
				StreamSupport.stream(jiraConfigurations.spliterator(), false).collect(Collectors.toList()));

		return desJiraConfiguration;
	}

	@Bean(name = "threadPoolExecutor")
	public Executor asyncExecutor() {
		ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
		executor.setCorePoolSize(3);
		executor.setMaxPoolSize(3);
		executor.setQueueCapacity(100);
		executor.setThreadNamePrefix("AsynchThread-");
		executor.initialize();
		return executor;
	}

}
