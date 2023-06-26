package com.apexon.compass.onboardservice.dto;

import java.util.List;
import java.util.Map;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeName;
import lombok.Builder;
import lombok.Data;
import static com.apexon.compass.onboardservice.constants.CommonConstants.DATA;

@Data
@Builder
@JsonTypeInfo(include = JsonTypeInfo.As.WRAPPER_OBJECT, use = JsonTypeInfo.Id.NAME)
@JsonTypeName(DATA)
public class JiraRulesResponse {

    private String id;

    private String projectId;

    private int jiraProjectId;

    private List<String> storyTypes;

    private List<String> bugTypes;

    private List<String> taskTypes;

    private List<String> definitionOfTodo;

    private List<Object> definitionOfReopen;

    private List<String> definitionOfInProgress;

    private List<String> definitionOfQaComplete;

    private List<String> definitionOfDevComplete;

    private List<String> definitionOfDone;

    private List<String> definitionOfAccepted;

    private List<String> storyPointsCalculation;

    private List<String> blockerDefinition;

    private List<String> definitionOfMajorSeverity;

    private String defaultPriority;

    private Map<String, String> burnDownCalculationCriterias;

    private List<String> definitionOfBlockerSeverity;

    private String flagDefinition;

    private List<String> definitionOfCriticalSeverity;

    private List<String> definitionOfMinorSeverity;

    private List<String> definitionOfRejected;

    private String velocityFormula;

    private String createdBy;

    private long createdAt;

    private String updatedBy;

    private long updatedAt;

}
