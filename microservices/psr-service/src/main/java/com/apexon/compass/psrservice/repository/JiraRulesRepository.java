package com.apexon.compass.psrservice.repository;

import com.apexon.compass.entities.JiraRules;
import java.util.List;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JiraRulesRepository extends MongoRepository<JiraRules, String> {

    @Override
    List<JiraRules> findAll();

}
