package com.apexon.compass.dashboard.repository;

import com.apexon.compass.dashboard.model.*;
import org.bson.types.ObjectId;

import java.util.List;
import java.util.Map;

public interface CustomRepositoryQuery {

	List<CollectorItem> findCollectorItemsBySubsetOptions(ObjectId id, Map<String, Object> allOptions,
			Map<String, Object> uniqueOptions, Map<String, Object> uniqueOptionsFromCollector);

	List<Component> findComponents(Collector collector);

	List<Component> findComponents(CollectorType collectorType);

	List<Component> findComponents(Collector collector, CollectorItem collectorItem);

	List<Component> findComponents(ObjectId collectorId, CollectorType collectorType, CollectorItem collectorItem);

	List<Component> findComponents(ObjectId collectorId, CollectorType collectorType, ObjectId collectorItemId);

	Iterable<Metadata> findAllMetaDataBySearchQuery(String searchKey, String value);

}
