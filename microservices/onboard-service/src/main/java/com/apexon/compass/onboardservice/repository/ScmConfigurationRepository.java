package com.apexon.compass.onboardservice.repository;

import com.apexon.compass.entities.ScmConfiguration;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ScmConfigurationRepository extends MongoRepository<ScmConfiguration, String> {

}
