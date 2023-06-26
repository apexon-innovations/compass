package com.apexon.compass.psrservice.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import com.apexon.compass.entities.ProjectMemberLeaves;

public interface ProjectMemberLeavesRepository extends MongoRepository<ProjectMemberLeaves, String> {

}
