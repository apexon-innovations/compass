package com.apexon.compass.sonarservice.repository;

import com.apexon.compass.entities.ScmConfiguration;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ScmConfigurationRepository extends MongoRepository<ScmConfiguration, String> {

    ScmConfiguration findByProjectId(ObjectId projectId);

}
