package com.apexon.compass.clientdashboardservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

import static com.apexon.compass.constants.StrategyServiceConstants.BASE_PACKAGE;

@SpringBootApplication(scanBasePackages = { BASE_PACKAGE })
@EnableDiscoveryClient
public class ClientDashboardServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(ClientDashboardServiceApplication.class, args);
    }

}
