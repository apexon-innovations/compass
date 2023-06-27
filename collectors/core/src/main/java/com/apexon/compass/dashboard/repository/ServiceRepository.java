package com.apexon.compass.dashboard.repository;

import com.apexon.compass.dashboard.model.Service;
import org.bson.types.ObjectId;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

/**
 * A {@link Service} repository
 */
public interface ServiceRepository extends PagingAndSortingRepository<Service, ObjectId> {

	/**
	 * Find all {@link Service}s for a given
	 * {@link com.apexon.compass.dashboard.model.Dashboard}.
	 * @param dashboardId dashboard id
	 * @return list of {@link Service}s
	 */
	List<Service> findByDashboardId(ObjectId dashboardId);

	/**
	 * Find all dependent {@link Service}s for a given
	 * {@link com.apexon.compass.dashboard.model.Dashboard}.
	 * @param dashboardId dashboard id
	 * @return list of {@link Service}s
	 */
	List<Service> findByDependedBy(ObjectId dashboardId);

}
