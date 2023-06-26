package com.apexon.compass.clientdashboardservice.repository;

import com.apexon.compass.entities.JiraRules;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JiraRulesRepository extends MongoRepository<JiraRules, String> {

    @Override
    List<JiraRules> findAll();

}
