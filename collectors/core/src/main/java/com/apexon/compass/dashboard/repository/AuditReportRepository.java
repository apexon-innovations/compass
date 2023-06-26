package com.apexon.compass.dashboard.repository;

import com.apexon.compass.dashboard.model.AuditReport;
import com.apexon.compass.dashboard.model.AuditType;
import com.apexon.compass.dashboard.model.EvaluationStatus;
import org.bson.types.ObjectId;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface AuditReportRepository extends CrudRepository<AuditReport, ObjectId> {

	AuditReport findTop1ByBusinessApplicationAndBusinessServiceAndAuditTypeAndIdentifierNameAndIdentifierVersionAndIdentifierUrlOrderByTimestampDesc(
			String businessApplication, String businessService, AuditType auditType, String identifierName,
			String identifierVersion, String identifierUrl);

	AuditReport findTop1ByAuditTypeAndIdentifierNameAndIdentifierVersionAndIdentifierUrlOrderByTimestampDesc(
			AuditType auditType, String identifierName, String identifierVersion, String identifierUrl);

	List<AuditReport> findByBusinessApplicationAndBusinessServiceAndAuditTypeAndIdentifierNameAndIdentifierVersionAndIdentifierUrl(
			String businessApplication, String businessService, AuditType auditType, String identifierName,
			String identifierVersion, String identifierUrl);

	AuditReport findTopByBusinessApplicationAndBusinessServiceAndAuditTypeAndIdentifierNameAndIdentifierVersionAndIdentifierUrlAndTestResultsUrlAndEvaluationStatusOrderByTimestampDesc(
			String businessApplication, String businessService, AuditType auditType, String identifierName,
			String identifierVersion, String identifierUrl, String testResultsUrl, EvaluationStatus evaluationStatus);

	List<AuditReport> findByAuditTypeAndIdentifierNameAndIdentifierVersionAndIdentifierUrl(AuditType auditType,
			String identifierName, String identifierVersion, String identifierUrl);

	List<AuditReport> findByAuditTypeAndAuditResponseIsNull(AuditType auditType);

	List<AuditReport> findByTimestampIsAfterAndAuditTypeAndTestResultsUrlIsNotNull(Long timestamp, AuditType auditType);

	List<AuditReport> findByAuditTypeAndEvaluationStatus(AuditType auditType, EvaluationStatus evaluationStatus);

	List<AuditReport> findByAuditTypeAndImageIdExists(AuditType auditType, boolean exists);

	AuditReport findByImageId(String imageId);

	AuditReport findTopByIdentifierUrlAndAuditType(String identifierUrl, AuditType auditType);

}
