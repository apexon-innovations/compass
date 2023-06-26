package com.apexon.compass.dashboard.repository;

import com.apexon.compass.dashboard.model.RallyBurnDownData;
import org.bson.types.ObjectId;
import org.springframework.data.repository.CrudRepository;

public interface RallyBurnDownRepository extends CrudRepository<RallyBurnDownData, ObjectId> {

	RallyBurnDownData findByIterationIdAndProjectId(String iterationId, String projectId);

}
