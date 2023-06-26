package com.apexon.compass.dashboard.repository;

import com.apexon.compass.dashboard.model.AuditResult;
import com.apexon.compass.dashboard.model.AuditType;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.Optional;

public interface AuditResultRepository extends PagingAndSortingRepository<AuditResult, ObjectId> {

	Optional<AuditResult> findById(ObjectId id);

	Page<AuditResult> findByAuditType(AuditType auditType, Pageable pageable);

	Iterable<AuditResult> findByDashboardTitle(String dashboardTitle);

	Iterable<AuditResult> findByDashboardTitleAndAuditType(String dashboardTitle, AuditType auditType);

	Iterable<AuditResult> findByConfigItemBusServNameAndConfigItemBusAppName(String configItemBusServName,
			String configItemBusAppName);

	Iterable<AuditResult> findByConfigItemBusServNameAndConfigItemBusAppNameAndAuditType(String configItemBusServName,
			String configItemBusAppName, AuditType auditType);

	Page<AuditResult> findByLineOfBusiness(String lineOfBusiness, Pageable pageable);

	Page<AuditResult> findByLineOfBusinessAndAuditType(String lineOfBusiness, AuditType auditType, Pageable pageable);

}