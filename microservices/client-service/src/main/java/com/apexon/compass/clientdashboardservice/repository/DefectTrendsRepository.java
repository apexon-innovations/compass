package com.apexon.compass.clientdashboardservice.repository;

import com.apexon.compass.entities.DefectTrendsType;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface DefectTrendsRepository extends MongoRepository<DefectTrendsType, String> {

    List<DefectTrendsType> findByProjectIdInAndDateIs(List<ObjectId> projectIds, Long date);

    DefectTrendsType findByProjectIdAndDate(ObjectId projectId, Long date);

}
