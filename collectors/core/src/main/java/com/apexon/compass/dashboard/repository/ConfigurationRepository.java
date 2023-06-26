package com.apexon.compass.dashboard.repository;

import com.apexon.compass.dashboard.model.Configuration;
import org.bson.types.ObjectId;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.CrudRepository;

public interface ConfigurationRepository
		extends CrudRepository<Configuration, ObjectId>, QuerydslPredicateExecutor<Configuration> {

	Configuration findByCollectorName(String collectorNiceName);

}
