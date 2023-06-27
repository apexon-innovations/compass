package com.apexon.compass.dashboard.web;

import com.apexon.compass.dashboard.model.DesSCMConfigurations;
import com.apexon.compass.dashboard.model.ScmConfiguration;
import com.apexon.compass.dashboard.repository.ScmConfigurationRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Configuration
public class Config {

	@Bean(name = "desSCMConfigurations")
	public DesSCMConfigurations init(ScmConfigurationRepository scmConfigurationRepository) {
		Iterable<ScmConfiguration> scmConfigurations = scmConfigurationRepository.findBySource("bitbucket");

		DesSCMConfigurations desSCMConfigurations = new DesSCMConfigurations();
		desSCMConfigurations.setScmConfigurations(
				StreamSupport.stream(scmConfigurations.spliterator(), false).collect(Collectors.toList()));

		return desSCMConfigurations;
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
