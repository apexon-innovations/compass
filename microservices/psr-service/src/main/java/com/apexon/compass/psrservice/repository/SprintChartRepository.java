package com.apexon.compass.psrservice.repository;

import com.apexon.compass.entities.SprintChart;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SprintChartRepository extends MongoRepository<SprintChart, String> {

    List<SprintChart> findByJiraProjectId(Integer jiraProjectId, Pageable pageable);

    List<SprintChart> findByJiraProjectIdAndState(Integer jiraProjectId, String status);

    List<SprintChart> findByJiraProjectIdAndStateAndBoardId(Integer jiraProjectId, String status, Integer boardId);

    @Query("{'state': 'active'}")
    List<SprintChart> findAllByState();

    List<SprintChart> findByJiraProjectId(Integer projectId);

    List<SprintChart> findBySprintIdAndStateAndBoardId(Integer sprintId, String status, Integer boardId);

    List<SprintChart> findByJiraProjectId(Integer projectId, Sort sort);

    List<SprintChart> findByJiraProjectIdAndBoardId(Integer jiraProjectId, Integer boardId, Sort sort);

}
