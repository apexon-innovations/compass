package com.apexon.compass.dashboard;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * Application configuration and bootstrap
 */
@SpringBootApplication
@EnableAsync
@ComponentScan(basePackages = "com.apexon.compass")
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

}
