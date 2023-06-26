package com.apexon.compass.sonarservice.repository;

import com.apexon.compass.entities.Project;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProjectRepository extends MongoRepository<Project, String> {

    Optional<Project> findByIdAndIsDeletedIn(String projectId, List<Boolean> b);

}
