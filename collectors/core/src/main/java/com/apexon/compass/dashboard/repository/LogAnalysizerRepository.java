package com.apexon.compass.dashboard.repository;

import com.apexon.compass.dashboard.model.LogAnalysis;
import org.bson.types.ObjectId;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.CrudRepository;

public interface LogAnalysizerRepository
		extends CrudRepository<LogAnalysis, ObjectId>, QuerydslPredicateExecutor<LogAnalysis> {

}
