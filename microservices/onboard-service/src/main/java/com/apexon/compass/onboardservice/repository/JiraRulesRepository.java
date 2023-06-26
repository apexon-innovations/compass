package com.apexon.compass.onboardservice.repository;

import com.apexon.compass.entities.JiraRules;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface JiraRulesRepository extends MongoRepository<JiraRules, String> {

}
