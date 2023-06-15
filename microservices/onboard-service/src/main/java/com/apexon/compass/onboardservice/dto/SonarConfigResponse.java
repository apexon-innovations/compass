package com.apexon.compass.onboardservice.dto;

import static com.apexon.compass.onboardservice.constants.CommonConstants.DATA;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeName;
import java.util.List;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@JsonTypeInfo(include = JsonTypeInfo.As.WRAPPER_OBJECT, use = JsonTypeInfo.Id.NAME)
@JsonTypeName(DATA)
public class SonarConfigResponse {

    private String id;

    private List<ProjectsMetaData> projects;

    private String credentials;

    private String userName;

    private String projectId;

    private String version;

    private String url;

    private List<String> metricsFields;

    private String createdBy;

    private long createdAt;

    private String updatedBy;

    private long updatedAt;

}
