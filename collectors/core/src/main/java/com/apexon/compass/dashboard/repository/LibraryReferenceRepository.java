package com.apexon.compass.dashboard.repository;

import com.apexon.compass.dashboard.model.LibraryPolicyReference;
import org.bson.types.ObjectId;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.CrudRepository;

/**
 * Repository for {@link LibraryPolicyReference} data.
 */
public interface LibraryReferenceRepository
		extends CrudRepository<LibraryPolicyReference, ObjectId>, QuerydslPredicateExecutor<LibraryPolicyReference> {

	LibraryPolicyReference findByLibraryNameAndOrgName(String name, String orgName);

}
