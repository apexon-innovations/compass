package com.apexon.compass.psrservice.repository;

import com.apexon.compass.entities.ProjectCompliance;
import java.util.List;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ProjectComplianceRepository extends MongoRepository<ProjectCompliance, String> {

    List<ProjectCompliance> findByProjectId(ObjectId objectId);

}
