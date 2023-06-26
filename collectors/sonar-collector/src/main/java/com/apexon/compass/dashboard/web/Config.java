package com.apexon.compass.dashboard.web;

import com.apexon.compass.dashboard.model.DesSonarConfigurations;
import com.apexon.compass.dashboard.model.SonarConfiguration;
import com.apexon.compass.dashboard.repository.SonarConfigurationRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Configuration
public class Config {

	@Bean
	public DesSonarConfigurations init(SonarConfigurationRepository sonarConfigurationRepository) {
		Iterable<SonarConfiguration> sonarConfigurations = sonarConfigurationRepository.findAll();

		DesSonarConfigurations desSonarConfigurations = new DesSonarConfigurations();
		desSonarConfigurations.setSonarConfigurations(
				StreamSupport.stream(sonarConfigurations.spliterator(), false).collect(Collectors.toList()));

		return desSonarConfigurations;
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
