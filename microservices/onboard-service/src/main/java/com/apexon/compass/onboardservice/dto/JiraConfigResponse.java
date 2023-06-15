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
public class JiraConfigResponse {

    private String id;

    private String projectId;

    private String jiraProjectId;

    private List<String> jiraBoardId;

    private String jiraUrl;

    private String jiraApiKey;

    private List<String> issueTypes;

    private String sprintDataFieldName;

    private String jiraEpicIdFieldName;

    private String jiraStoryPointsFieldName;

    private Map<String, String> issueTypesId;

    private String queryEndPoint;

    private String createdBy;

    private long createdAt;

    private String updatedBy;

    private long updatedAt;

    private long projectStartDate;

    private boolean jiraBoardAsTeam;

}
