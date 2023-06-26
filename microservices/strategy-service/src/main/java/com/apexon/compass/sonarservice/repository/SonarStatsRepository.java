package com.apexon.compass.sonarservice.repository;

import com.apexon.compass.entities.SonarStats;
import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SonarStatsRepository extends MongoRepository<SonarStats, Integer> {

    List<SonarStats> findBySonarProjectId(String key, Sort sort);

    List<SonarStats> findBySonarProjectId(String key, Pageable pageable);

}
