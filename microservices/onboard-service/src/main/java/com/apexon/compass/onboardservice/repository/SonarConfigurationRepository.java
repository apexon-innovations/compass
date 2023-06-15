package com.apexon.compass.onboardservice.repository;

import com.apexon.compass.entities.SonarConfigurations;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface SonarConfigurationRepository extends MongoRepository<SonarConfigurations, String> {

}
