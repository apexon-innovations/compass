package com.apexon.compass.psrservice.dto;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;
import com.fasterxml.jackson.annotation.JsonTypeName;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@JsonTypeName("data")
@JsonTypeInfo(use = Id.NAME, include = As.WRAPPER_OBJECT)
public class ProjectDataWithBlockersDto {

    private String id;

    private String jiraProjectId;

    private String sprintId;

    private String sprintName;

    private Integer boardId;

    private BlockersDto blockers;

}
