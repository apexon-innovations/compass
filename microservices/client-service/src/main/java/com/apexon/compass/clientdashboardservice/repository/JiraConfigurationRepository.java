package com.apexon.compass.clientdashboardservice.repository;

import com.apexon.compass.entities.JiraConfiguration;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface JiraConfigurationRepository extends MongoRepository<JiraConfiguration, String> {

    Optional<JiraConfiguration> findByJiraProjectId(String jiraId);

}
