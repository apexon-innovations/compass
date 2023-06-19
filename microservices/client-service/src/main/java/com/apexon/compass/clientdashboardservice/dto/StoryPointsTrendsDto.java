package com.apexon.compass.clientdashboardservice.dto;

import static com.fasterxml.jackson.annotation.JsonTypeInfo.As.WRAPPER_OBJECT;
import static com.fasterxml.jackson.annotation.JsonTypeInfo.Id.NAME;
import static com.apexon.compass.constants.PsrServiceConstants.DATA;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeName;
import java.util.List;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@JsonTypeName(DATA)
@JsonTypeInfo(use = NAME, include = WRAPPER_OBJECT)
public class StoryPointsTrendsDto {

    private String projectId;

    private String projectName;

    private List<SprintDayWiseDataDto> sprintData;

}
