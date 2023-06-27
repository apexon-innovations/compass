package com.apexon.compass.dashboard.repository;

import com.apexon.compass.dashboard.model.Monitor2;
import org.bson.types.ObjectId;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface Monitor2Repository extends PagingAndSortingRepository<Monitor2, ObjectId> {

	/**
	 * Find all {@link Monitor2}s for a given
	 * {@link com.apexon.compass.dashboard.model.Dashboard}.
	 * @param dashboardId dashboard id
	 * @return list of {@link Monitor2}s
	 */
	List<Monitor2> findByDashboardId(ObjectId dashboardId);

}
