package com.apexon.compass.sonarservice.repository;

import com.apexon.compass.entities.JiraConfiguration;
import java.util.Optional;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JiraConfigurationRepository extends MongoRepository<JiraConfiguration, String> {

}
