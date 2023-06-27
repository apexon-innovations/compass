package com.apexon.compass.dashboard.service;

import com.apexon.compass.dashboard.model.Build;
import org.bson.types.ObjectId;

public interface BuildCommonService {

	/**
	 * Fetches a build.
	 * @param id build unique identifier
	 * @return Build instance
	 */
	Build get(ObjectId id);

	/**
	 * Fetches a build.
	 * @param buildUrl build url
	 * @return Build instance
	 */
	Build get(String buildUrl);

}
