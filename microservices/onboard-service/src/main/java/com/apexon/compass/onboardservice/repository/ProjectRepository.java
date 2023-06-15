package com.apexon.compass.onboardservice.repository;

import com.apexon.compass.entities.Project;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ProjectRepository extends MongoRepository<Project, String> {

}
