package com.apexon.compass.psrservice.repository;

import com.apexon.compass.entities.Sprints;

import java.util.List;
import java.util.Optional;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SprintsRepository extends MongoRepository<Sprints, String> {

    List<Sprints> findByJiraProjectIdAndState(Integer jiraProjectId, String state);

    Optional<Sprints> findByProjectIdAndSprintIdAndBoardId(int parseInt, int parseInt2, Integer boardId);

}
