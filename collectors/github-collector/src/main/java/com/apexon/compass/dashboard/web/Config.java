package com.apexon.compass.dashboard.web;

import com.apexon.compass.dashboard.model.CompassScmConfigurations;
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

	@Bean(name = "compassScmConfigurations")
	public CompassScmConfigurations init(ScmConfigurationRepository scmConfigurationRepository) {
		Iterable<ScmConfiguration> scmConfigurations = scmConfigurationRepository.findBySource("github");

		CompassScmConfigurations compassScmConfigurations = new CompassScmConfigurations();
		compassScmConfigurations.setScmConfigurations(
				StreamSupport.stream(scmConfigurations.spliterator(), false).collect(Collectors.toList()));

		return compassScmConfigurations;
	}

	@Bean(name = "threadPoolExecutor")
	public Executor asyncExecutor() {
		ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
		executor.setCorePoolSize(3);
		executor.setMaxPoolSize(3);
		executor.setQueueCapacity(100);
		executor.setThreadNamePrefix("AsyncThread-");
		executor.initialize();
		return executor.getThreadPoolExecutor();
	}

}
