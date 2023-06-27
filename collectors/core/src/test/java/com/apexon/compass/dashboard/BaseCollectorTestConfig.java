package com.apexon.compass.dashboard;

import com.apexon.compass.dashboard.client.RestOperationsSupplier;
import com.apexon.compass.dashboard.testutil.TestResponse;
import com.apexon.compass.dashboard.testutil.TestRestOperations;
import org.mockito.Mockito;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.scheduling.TaskScheduler;

import java.util.HashMap;
import java.util.Map;

@ComponentScan(basePackages = { "com.apexon.compass.dashboard.collector", "com.apexon.compass.dashboard.model" },
		includeFilters = { @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, value = TaskScheduler.class),
				@ComponentScan.Filter(type = FilterType.REGEX,
						pattern = "com.apexon.compass.dashboard.collector.*CollectorTask.class"),
				@ComponentScan.Filter(type = FilterType.REGEX,
						pattern = "com.apexon.compass.dashboard.collector.*Settings.class") })
public class BaseCollectorTestConfig {

	@Bean
	public RestOperationsSupplier restOperationsSupplier() {
		Map<String, TestResponse<String>> responseMap = new HashMap<>();
		return new TestRestOperations(responseMap);
	}

	@Bean
	public TaskScheduler taskScheduler() {
		return Mockito.mock(TaskScheduler.class);
	}

}
