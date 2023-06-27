package com.apexon.compass.dashboard.repository;

import com.apexon.compass.dashboard.model.RequestLog;
import org.bson.types.ObjectId;
import org.springframework.data.repository.CrudRepository;

public interface RequestLogRepository extends CrudRepository<RequestLog, ObjectId> {

}
