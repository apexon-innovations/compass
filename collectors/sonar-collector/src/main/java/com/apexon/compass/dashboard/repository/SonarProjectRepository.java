package com.apexon.compass.dashboard.repository;

import com.apexon.compass.dashboard.model.SonarProject;
import com.apexon.compass.dashboard.repository.BaseCollectorItemRepository;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface SonarProjectRepository extends BaseCollectorItemRepository<SonarProject> {

	@Query(value = "{ 'collectorId' : ?0, options.instanceUrl : ?1, options.projectId : ?2}")
	SonarProject findSonarProject(ObjectId collectorId, String instanceUrl, String projectId);

	@Query(value = "{ 'collectorId' : ?0, options.instanceUrl : ?1, enabled: true}")
	List<SonarProject> findEnabledProjects(ObjectId collectorId, String instanceUrl);

}
