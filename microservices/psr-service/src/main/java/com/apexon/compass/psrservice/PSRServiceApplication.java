package com.apexon.compass.psrservice;

import static com.apexon.compass.constants.PsrServiceConstants.BASE_PACKAGE;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@EnableMongoRepositories
@SpringBootApplication(scanBasePackages = BASE_PACKAGE)
@EnableDiscoveryClient
public class PSRServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(PSRServiceApplication.class, args);
    }

}
