package com.apexon.compass.entities;

import static com.apexon.compass.constants.EntitiesConstants.JIRA_RULES;

import java.util.List;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = JIRA_RULES)
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class JiraRules {

    @Id
    private String id;

    private ObjectId projectId;

    private String jiraProjectId;

    private List<String> storyTypes;

    private List<String> bugTypes;

    private List<String> taskTypes;

    private List<String> jiraStages;

    private List<String> definitionOfTodo;

    private List<String> definitionOfReopen;

    private List<String> definitionOfInProgress;

    private List<String> definitionOfQaComplete;

    private List<String> definitionOfDevComplete;

    private List<String> definitionOfDone;

    private List<String> definitionOfAccepted;

    private List<String> storyPointsCalculation;

    private String velocityFormula;

    private List<String> blockerDefinition;

    private List<String> definitionOfMajorSeverity;

    private List<String> definitionOfMinorSeverity;

    private List<String> definitionOfBlockerSeverity;

    private List<String> definitionOfCriticalSeverity;

    private List<String> definitionOfRejected;

    private List<String> rangeCategory;

    private String flagDefinition;

    private Map<String, String> burnDownCalculationCriterias;

    private String estimationType;

    private String createdBy;

    private Long createdDate;

    private String updatedBy;

    private Long updatedDate;

    private String defaultPriority;

}
