package com.apexon.compass.dashboard.repository;

import com.apexon.compass.dashboard.model.AuthType;
import com.apexon.compass.dashboard.model.UserEntitlements;
import org.bson.types.ObjectId;
import org.springframework.data.repository.CrudRepository;

public interface UserEntitlementsRepository extends CrudRepository<UserEntitlements, ObjectId> {

	public UserEntitlements findTopByAuthTypeAndUsername(AuthType authType, String username);

	public UserEntitlements findTopByAuthTypeAndEntitlementTypeAndUsername(AuthType authType, String entitlementType,
			String username);

}
