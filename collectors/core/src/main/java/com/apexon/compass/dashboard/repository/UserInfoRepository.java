package com.apexon.compass.dashboard.repository;

import com.apexon.compass.dashboard.model.AuthType;
import com.apexon.compass.dashboard.model.UserInfo;
import com.apexon.compass.dashboard.model.UserRole;
import org.bson.types.ObjectId;
import org.springframework.data.repository.CrudRepository;

import java.util.Collection;

public interface UserInfoRepository extends CrudRepository<UserInfo, ObjectId> {

	UserInfo findByUsernameAndAuthType(String username, AuthType authType);

	Collection<UserInfo> findByAuthoritiesIn(UserRole roleAdmin);

	Iterable<UserInfo> findByOrderByUsernameAsc();

	UserInfo findByUsername(String userName);

}
