package com.apexon.compass.psrservice.repository;

import java.util.List;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;
import com.apexon.compass.entities.ProjectMonthly;

@Repository
public interface ProjectMonthlyRepository extends MongoRepository<ProjectMonthly, String> {

    @Query("{ 'project_id' : ?0 }")
    List<ProjectMonthly> findByProjectID(ObjectId objectId, Sort sort);

}
