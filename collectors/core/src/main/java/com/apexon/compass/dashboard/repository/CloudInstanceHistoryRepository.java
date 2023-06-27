package com.apexon.compass.dashboard.repository;

import com.apexon.compass.dashboard.model.CloudInstanceHistory;
import org.bson.types.ObjectId;
import org.springframework.data.repository.CrudRepository;

import java.util.Collection;

public interface CloudInstanceHistoryRepository extends CrudRepository<CloudInstanceHistory, ObjectId> {

	Collection<CloudInstanceHistory> findByAccountNumber(String accountNumber);

}
