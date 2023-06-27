package com.apexon.compass.dashboard.repository;

import com.apexon.compass.dashboard.model.AuditReport;
import com.apexon.compass.dashboard.model.AuditType;
import com.apexon.compass.dashboard.model.EvaluationStatus;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuditReportRepositoryTest {

	private AuditReportRepository auditReportRepository = Mockito.mock(AuditReportRepository.class);

	@Test
	void findByAuditTypeAndEvaluationStatus() throws IOException {
		doNothing().when(auditReportRepository).deleteAll();

		List<AuditReport> itemList = new ArrayList<AuditReport>();
		AuditReport audit = new AuditReport();
		audit.setAuditType(AuditType.CONTAINER_SCAN);
		audit.setEvaluationStatus(EvaluationStatus.PENDING);
		itemList.add(audit);
		when(auditReportRepository.findByAuditTypeAndEvaluationStatus(AuditType.CONTAINER_SCAN,
				EvaluationStatus.PENDING))
			.thenReturn(itemList);

		Assertions.assertEquals(1, itemList.size());
	}

}