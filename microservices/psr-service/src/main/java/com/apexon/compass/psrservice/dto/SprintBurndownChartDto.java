package com.apexon.compass.psrservice.dto;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeName;
import lombok.Builder;
import lombok.Data;

import java.util.List;

import static com.apexon.compass.constants.PsrServiceConstants.DATA;

@Data
@Builder
@JsonTypeName(DATA)
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.WRAPPER_OBJECT)
public class SprintBurndownChartDto {

    private String jiraProjectId;

    private Integer sprintId;

    private String sprintName;

    private Integer boardId;

    private List<SprintBurndownDataDto> dailyStatus;

}
