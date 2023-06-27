package com.apexon.compass.dashboard.repository;

import com.apexon.compass.dashboard.model.Authentication;
import org.bson.types.ObjectId;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface AuthenticationRepository
		extends PagingAndSortingRepository<Authentication, ObjectId>, CrudRepository<Authentication, ObjectId> {

	Authentication findByUsername(String username);

}
