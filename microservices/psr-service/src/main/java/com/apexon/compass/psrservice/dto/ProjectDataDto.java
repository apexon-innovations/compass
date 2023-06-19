package com.apexon.compass.psrservice.dto;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;
import com.fasterxml.jackson.annotation.JsonTypeName;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
@JsonTypeName("data")
@JsonTypeInfo(use = Id.NAME, include = As.WRAPPER_OBJECT)
public class ProjectDataDto {

    private String id;

    private String name;

    private String clientName;

    private String initials;

    private String category;

    private String iconLocation;

    private String billingType;

    private String startDate;

    private String endDate;

    private ResourcesDto resources;

    private HealthMetricsDto healthMetrics;

    private List<PersonDetailsDto> projectManager;

    private List<PersonDetailsDto> deliveryManager;

    private List<PersonDetailsDto> accountManager;

    private List<PersonDetailsDto> teamMembers;

}
