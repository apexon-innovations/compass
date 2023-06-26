package com.apexon.compass.dashboard;

import com.apexon.compass.fileupload.config.S3Config;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * Application configuration and bootstrap
 */
@SpringBootApplication
@EnableAsync
@Import(S3Config.class)
@ComponentScan(basePackages = "com.apexon.compass")
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

}
