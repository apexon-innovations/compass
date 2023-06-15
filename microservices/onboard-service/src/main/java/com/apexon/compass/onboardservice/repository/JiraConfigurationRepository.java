package com.apexon.compass.onboardservice.repository;

import com.apexon.compass.entities.JiraConfiguration;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface JiraConfigurationRepository extends MongoRepository<JiraConfiguration, String> {

}
