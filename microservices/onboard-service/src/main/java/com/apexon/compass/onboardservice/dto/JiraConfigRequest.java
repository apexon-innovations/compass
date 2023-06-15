package com.apexon.compass.onboardservice.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import java.util.List;
import java.util.Map;
import lombok.Data;

@Data
public class JiraConfigRequest {

    @NotBlank
    private String projectId;

    @NotBlank
    @Pattern(regexp = "\\d+", message = "Value must be numeric")
    private String jiraProjectId;

    @Size(min = 1)
    private List<@NotBlank @Pattern(regexp = "\\d+", message = "All value must be numeric") String> jiraBoardId;

    private String jiraUrl;

    @NotBlank
    private String jiraApiKey;

    @Size(min = 1, message = "List cant be empty")
    private List<@NotBlank String> issueTypes;

    @NotBlank
    private String sprintDataFieldName;

    @NotBlank
    private String jiraEpicIdFieldName;

    @NotBlank
    private String jiraStoryPointsFieldName;

    private Map<String, @Pattern(regexp = "\\d+", message = "All value must be numeric") String> issueTypesId;

}
