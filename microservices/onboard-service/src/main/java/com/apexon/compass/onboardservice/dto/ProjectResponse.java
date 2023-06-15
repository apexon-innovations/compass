package com.apexon.compass.onboardservice.dto;

import static com.apexon.compass.onboardservice.constants.CommonConstants.DATA;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeName;
import java.util.List;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@JsonTypeInfo(include = JsonTypeInfo.As.WRAPPER_OBJECT, use = JsonTypeInfo.Id.NAME)
@JsonTypeName(DATA)
public class ProjectResponse {

    private String id;

    private String jiraProjectId;

    private String clientName;

    private String endDate;

    private List<ManagerDetails> dm;

    private List<Resources> resources;

    private List<ClientManager> clientPm;

    private String logoUrl;

    @JsonProperty("isDeleted")
    private boolean isDeleted;

    private String billingType;

    private String name;

    private String category;

    private List<ManagerDetails> pm;

    private String startDate;

    private String createdBy;

    private String updatedBy;

    private long createdAt;

    private long updatedAt;

}