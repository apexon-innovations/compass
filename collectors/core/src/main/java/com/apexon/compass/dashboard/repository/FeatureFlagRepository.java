package com.apexon.compass.dashboard.repository;

import com.apexon.compass.dashboard.model.FeatureFlag;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface FeatureFlagRepository extends CrudRepository<FeatureFlag, ObjectId> {

	@Query
	FeatureFlag findByName(String name);

}
