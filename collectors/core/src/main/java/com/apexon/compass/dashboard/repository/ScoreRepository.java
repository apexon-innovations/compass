package com.apexon.compass.dashboard.repository;

import com.apexon.compass.dashboard.model.CodeQuality;
import com.apexon.compass.dashboard.model.score.ScoreMetric;
import org.bson.types.ObjectId;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.CrudRepository;

/**
 * Repository for {@link CodeQuality} data.
 */
public interface ScoreRepository extends CrudRepository<ScoreMetric, ObjectId>, QuerydslPredicateExecutor<ScoreMetric> {

	/**
	 * Finds the {@link ScoreMetric} data point at the given timestamp for a specific
	 * {@link com.apexon.compass.dashboard.model.CollectorItem}.
	 * @param collectorItemId collector item id
	 * @param timestamp timstamp
	 * @return a {@link CodeQuality}
	 */
	ScoreMetric findByCollectorItemIdAndTimestamp(ObjectId collectorItemId, long timestamp);

	/**
	 * Finds all {@link ScoreMetric}s for a given
	 * {@link com.apexon.compass.dashboard.model.CollectorItem}.
	 * @param collectorItemId collector item id
	 * @return list of {@link ScoreMetric}
	 */
	ScoreMetric findByCollectorItemId(ObjectId collectorItemId);

}
