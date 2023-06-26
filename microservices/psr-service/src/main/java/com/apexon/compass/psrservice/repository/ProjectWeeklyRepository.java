package com.apexon.compass.psrservice.repository;

import com.apexon.compass.entities.ProjectWeekly;
import java.util.Date;
import java.util.List;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ProjectWeeklyRepository extends MongoRepository<ProjectWeekly, String> {

    List<ProjectWeekly> findByProjectID(ObjectId objectId, Pageable pageable);

    List<ProjectWeekly> findByDateBetweenAndProjectIDIn(Date startDate, Date endDate, List<ObjectId> ids, Sort sort);

    ProjectWeekly findByProjectIDAndDate(ObjectId projectId, Date date);

    List<ProjectWeekly> findByProjectIDAndDateIsBefore(ObjectId projectId, Date date, Pageable pageable);

}
