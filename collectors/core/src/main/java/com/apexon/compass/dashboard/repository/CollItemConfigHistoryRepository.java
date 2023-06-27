package com.apexon.compass.dashboard.repository;

import com.apexon.compass.dashboard.model.CollectorItemConfigHistory;
import org.bson.types.ObjectId;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface CollItemConfigHistoryRepository extends CrudRepository<CollectorItemConfigHistory, ObjectId> {

	List<CollectorItemConfigHistory> findByCollectorItemIdAndTimestampIsBetweenOrderByTimestampDesc(
			ObjectId collectorItemId, long beginDate, long endDate);

	CollectorItemConfigHistory findByCollectorItemIdAndTimestamp(ObjectId collectorItemId, long timestamp);

}
