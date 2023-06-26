package com.apexon.compass.psrservice.repository;

import com.apexon.compass.entities.Stories;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StoriesRepository extends MongoRepository<Stories, String> {

    List<Stories> findByJiraProjectIdAndType(Integer jiraProjectId, String type);

}
