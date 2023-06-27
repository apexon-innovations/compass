package com.apexon.compass.dashboard.repository;

import com.apexon.compass.dashboard.model.AuditRequestLog;
import org.bson.types.ObjectId;
import org.springframework.data.repository.CrudRepository;

public interface AuditRequestLogRepository extends CrudRepository<AuditRequestLog, ObjectId> {

}
