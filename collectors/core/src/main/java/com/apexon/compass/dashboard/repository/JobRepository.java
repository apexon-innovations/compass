package com.apexon.compass.dashboard.repository;

import com.apexon.compass.dashboard.model.JobCollectorItem;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface JobRepository<T extends JobCollectorItem> extends BaseCollectorItemRepository<T> {

	@Query(value = "{ 'collectorId' : ?0, 'options.instanceUrl' : ?1, 'options.jobName' : ?2}")
	T findJob(ObjectId collectorId, String instanceUrl, String jobName);

	@Query(value = "{ 'collectorId' : ?0, 'options.jobUrl' : ?1, 'options.jobName' : ?2}")
	T findJobByJobUrl(ObjectId collectorId, String jobUrl, String jobName);

	@Query(value = "{ 'collectorId' : ?0, 'options.instanceUrl' : ?1, enabled: true}")
	List<T> findEnabledJobs(ObjectId collectorId, String instanceUrl);

}
