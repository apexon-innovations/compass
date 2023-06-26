package com.apexon.compass.dashboard.repository;

import com.apexon.compass.dashboard.model.Feature;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * Repository for {@link Feature}.
 */
public interface FeatureRepository
		extends CrudRepository<Feature, ObjectId>, QuerydslPredicateExecutor<Feature>, FeatureRepositoryCustom {

	/**
	 * This essentially returns the max change date from the collection, based on the last
	 * change date (or default delta change date property) available
	 * @param collectorId Collector ID of source system collector
	 * @param changeDate Last available change date or delta begin date property
	 * @return A single Change Date value that is the maximum value of the existing
	 * collection
	 */
	@Query
	List<Feature> findTopByCollectorIdAndChangeDateGreaterThanOrderByChangeDateDesc(ObjectId collectorId,
			String changeDate);

	@Query(value = "{'sId' : ?0}", fields = "{'sId' : 1}")
	List<Feature> getFeatureIdById(String sId);

	@Query(value = " {'sNumber' : ?0 }")
	List<Feature> getStoryByNumber(String sNumber);

	@Query(value = " {'sTeamID' : ?0 }")
	List<Feature> getStoryByTeamID(String sTeamID);

	@Query(value = " {'sTypeName' : ?0 }")
	List<Feature> getStoryByType(String sTypeName);

	Feature findByCollectorIdAndSId(ObjectId collectorId, String sId);

	List<Feature> findAllByCollectorIdAndSEpicID(ObjectId collectorId, String sEpicID);

	List<Feature> findAllByCollectorIdAndSTeamID(ObjectId collectorId, String sTeamID);

	List<Feature> findAllByCollectorIdAndSProjectID(Object collectorId, String sProjectID);

	Feature findByCollectorIdAndSIdAndSTeamID(Object collectorId, String sProjectID, String sTeamID);

}