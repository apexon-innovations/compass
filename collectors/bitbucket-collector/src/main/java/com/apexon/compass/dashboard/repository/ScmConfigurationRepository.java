package com.apexon.compass.dashboard.repository;

import com.apexon.compass.dashboard.model.ScmConfiguration;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ScmConfigurationRepository extends MongoRepository<ScmConfiguration, String> {

	List<ScmConfiguration> findBySource(String source);

}
