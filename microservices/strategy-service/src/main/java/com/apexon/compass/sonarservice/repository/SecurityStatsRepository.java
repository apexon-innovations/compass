package com.apexon.compass.sonarservice.repository;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import com.apexon.compass.entities.SecurityStats;

@Repository
public interface SecurityStatsRepository extends MongoRepository<SecurityStats, String> {

    List<SecurityStats> findByProjectId(String projectId, Sort sort);

}
