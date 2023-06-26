package com.apexon.compass.sonarservice.repository;

import com.apexon.compass.entities.SonarConfigurations;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SonarConfigurationsRepository extends MongoRepository<SonarConfigurations, String> {

}
