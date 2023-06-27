package com.apexon.compass.dashboard.repository;

import com.apexon.compass.dashboard.model.GitRepo;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface GitRepoRepository extends BaseCollectorItemRepository<GitRepo> {

	@Query(value = "{ 'collectorId' : ?0, options.url : ?1, options.branch : ?2}")
	GitRepo findGitRepo(ObjectId collectorId, String url, String branch);

	@Query(value = "{ 'collectorId' : ?0, enabled: true}")
	List<GitRepo> findEnabledGitRepos(ObjectId collectorId);

}
