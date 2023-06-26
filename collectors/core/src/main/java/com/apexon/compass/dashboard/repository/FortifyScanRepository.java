package com.apexon.compass.dashboard.repository;

import com.apexon.compass.dashboard.model.FortifyScanReport;
import org.bson.types.ObjectId;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface FortifyScanRepository extends CrudRepository<FortifyScanReport, ObjectId> {

	FortifyScanReport findByCollectorItemIdAndTimestamp(ObjectId collectorItemId, long timestamp);

	List<FortifyScanReport> findByCollectorItemId(ObjectId id);

}
