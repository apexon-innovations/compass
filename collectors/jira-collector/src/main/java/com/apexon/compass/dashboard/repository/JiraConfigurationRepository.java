package com.apexon.compass.dashboard.repository;

import com.apexon.compass.dashboard.model.JiraConfiguration;
import org.springframework.stereotype.Repository;

@Repository
public interface JiraConfigurationRepository extends BaseCollectorRepository<JiraConfiguration> {

	@Override
	Iterable<JiraConfiguration> findAll();

}