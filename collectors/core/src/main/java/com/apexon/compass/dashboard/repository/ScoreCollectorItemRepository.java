package com.apexon.compass.dashboard.repository;

import com.apexon.compass.dashboard.model.score.ScoreCollectorItem;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

/**
 * Repository for {@link ScoreCollectorItem}s.
 */
public interface ScoreCollectorItemRepository extends BaseCollectorItemRepository<ScoreCollectorItem> {

	/**
	 * Finds all enabled {@link ScoreCollectorItem}s
	 * @param collectorId ID
	 * @return list of {@link ScoreCollectorItem}s
	 */
	@Query(value = "{ 'collectorId' : ?0, enabled: true}")
	List<ScoreCollectorItem> findEnabledScores(ObjectId collectorId);

	@Query(value = "{'collectorId': ?0, 'options.dashboardId': ?1}")
	ScoreCollectorItem findCollectorItemByCollectorIdAndDashboardId(ObjectId collectorId, ObjectId dashboardId);

}
