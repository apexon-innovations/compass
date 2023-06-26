package com.apexon.compass.sonarservice.repository;

import com.apexon.compass.entities.Sprints;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SprintsRepository extends MongoRepository<Sprints, String> {

    Optional<Sprints> findByProjectIdAndState(Integer projectId, String state);

    List<Sprints> findByProjectIdAndSprintId(Integer projectId, Integer sprintId);

    List<Sprints> findByProjectId(Integer projectId, Sort sort);

}
