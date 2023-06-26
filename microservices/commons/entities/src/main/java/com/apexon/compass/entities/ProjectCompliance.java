package com.apexon.compass.entities;

import static com.apexon.compass.constants.EntitiesConstants.PROJECT_COMPLIANCE;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = PROJECT_COMPLIANCE)
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProjectCompliance {

    @Id
    private String id;

    private ObjectId nestProjectId;

    private ObjectId projectId;

    private Long issueId;

    private String number;

    private String summary;

    private IssueType issueType;

    private String nonConfirmationType;

    private String projectName;

    private Long ncRaisedDate;

    private ManagerDetails projectManager;

    private String resolutionState;

    private String ncDetails;

    private Long ncDueDate;

    private ManagerDetails assignee;

    private Status status;

    private Long issueCreatedDate;

    private Long issueUpdatedDate;

    private String rootCauseAnalysis;

    private String corrections;

    private String correctiveActions;

    private String auditType;

    private String auditEntity;

    private String auditScope;

    private String auditCriteria;

    private String clientName;

    private String findings;

    private String createdBy;

    private String updatedBy;

    private Long createdDate;

    private Long updatedDate;

}
