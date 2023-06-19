package com.apexon.compass.psrservice.repository;

import com.apexon.compass.entities.ProjectOneTime;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProjectOneTimeRepository extends MongoRepository<ProjectOneTime, String> {

    ProjectOneTime findByProjectIDAndIsArchive(ObjectId objectId, Boolean isArchive);

    List<ProjectOneTime> findByProjectIDAndIsArchiveFalse(ObjectId objectId);

}
