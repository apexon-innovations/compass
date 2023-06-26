package com.apexon.compass.onboardservice.dto;

import java.util.List;
import java.util.Map;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class JiraRulesRequest {

    @NotBlank
    private String projectId;

    private int jiraProjectId;

    @Size(min = 1)
    private List<String> storyTypes;

    @Size(min = 1)
    private List<String> bugTypes;

    @Size(min = 1)
    private List<String> taskTypes;

    @Size(min = 1)
    private List<String> definitionOfTodo;

    @Size(min = 1)
    private List<Object> definitionOfReopen;

    @Size(min = 1)
    private List<String> definitionOfInProgress;

    @Size(min = 1)
    private List<String> definitionOfQaComplete;

    @Size(min = 1)
    private List<String> definitionOfDevComplete;

    @Size(min = 1)
    private List<String> definitionOfDone;

    @Size(min = 1)
    private List<String> definitionOfAccepted;

    @Size(min = 1)
    private List<String> storyPointsCalculation;

    @Size(min = 1)
    private List<String> blockerDefinition;

    @Size(min = 1)
    private List<String> definitionOfMajorSeverity;

    @NotBlank
    private String defaultPriority;

    private Map<String, String> burnDownCalculationCriterias;

    @Size(min = 1)
    private List<String> definitionOfBlockerSeverity;

    @NotBlank
    private String flagDefinition;

    @Size(min = 1)
    private List<String> definitionOfCriticalSeverity;

    @Size(min = 1)
    private List<String> definitionOfMinorSeverity;

    @Size(min = 1)
    private List<String> definitionOfRejected;

    @Size(min = 1)
    private List<String> jiraStages;

}
