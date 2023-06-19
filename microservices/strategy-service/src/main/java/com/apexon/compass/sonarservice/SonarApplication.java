package com.apexon.compass.sonarservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.config.EnableMongoAuditing;

import static com.apexon.compass.constants.StrategyServiceConstants.BASE_PACKAGE;

@EnableMongoAuditing
@SpringBootApplication(scanBasePackages = { BASE_PACKAGE })
public class SonarApplication {

    public static void main(String[] args) {
        SpringApplication.run(SonarApplication.class, args);
    }

}
