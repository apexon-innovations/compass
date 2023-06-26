package com.apexon.compass.dashboard.repository;

import com.apexon.compass.dashboard.model.SonarConfiguration;
import org.springframework.stereotype.Repository;

@Repository
public interface SonarConfigurationRepository extends BaseCollectorRepository<SonarConfiguration> {

	@Override
	Iterable<SonarConfiguration> findAll();

}
