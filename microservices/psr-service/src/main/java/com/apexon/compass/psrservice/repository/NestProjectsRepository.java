package com.apexon.compass.psrservice.repository;

import com.apexon.compass.entities.NestProjects;
import java.util.List;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NestProjectsRepository extends MongoRepository<NestProjects, String> {

    List<NestProjects> findByIscProjectId(ObjectId id);

}
