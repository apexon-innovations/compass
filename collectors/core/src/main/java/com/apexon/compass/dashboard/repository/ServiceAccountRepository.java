package com.apexon.compass.dashboard.repository;

import com.apexon.compass.dashboard.model.ServiceAccount;
import org.bson.types.ObjectId;
import org.springframework.data.repository.CrudRepository;

public interface ServiceAccountRepository extends CrudRepository<ServiceAccount, ObjectId> {

}
