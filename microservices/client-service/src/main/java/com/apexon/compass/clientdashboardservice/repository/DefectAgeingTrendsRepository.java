package com.apexon.compass.clientdashboardservice.repository;

import com.apexon.compass.entities.DefectAgeingTrends;
import java.util.List;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface DefectAgeingTrendsRepository extends MongoRepository<DefectAgeingTrends, String> {

    List<DefectAgeingTrends> findByProjectIdIn(List<ObjectId> projectIds);

}
