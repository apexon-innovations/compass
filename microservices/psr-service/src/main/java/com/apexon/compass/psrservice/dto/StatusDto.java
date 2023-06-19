package com.apexon.compass.psrservice.dto;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;
import com.fasterxml.jackson.annotation.JsonTypeName;
import lombok.Builder;
import lombok.Data;

import static com.apexon.compass.constants.PsrServiceConstants.DATA;

@Data
@Builder
@JsonTypeName(DATA)
@JsonTypeInfo(use = Id.NAME, include = As.WRAPPER_OBJECT)
public class StatusDto {

    private String id;

    private String jiraProjectId;

    private Integer boardId;

    private Integer sprintId;

    private String sprintName;

    private SummaryDto summary;

}
