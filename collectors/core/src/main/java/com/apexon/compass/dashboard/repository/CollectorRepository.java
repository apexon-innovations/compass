package com.apexon.compass.dashboard.repository;

import com.apexon.compass.dashboard.model.Collector;
import com.apexon.compass.dashboard.model.CollectorType;
import org.bson.types.ObjectId;

import java.util.List;
import java.util.Optional;

/**
 * A {@link Collector} repository
 */
public interface CollectorRepository extends BaseCollectorRepository<Collector> {

	Optional<Collector> findById(ObjectId id);

	List<Collector> findAllByCollectorType(CollectorType collectorType);

}
