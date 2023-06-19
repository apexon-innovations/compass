package com.apexon.compass.clientdashboardservice.dto;

import static com.fasterxml.jackson.annotation.JsonTypeInfo.As.WRAPPER_OBJECT;
import static com.fasterxml.jackson.annotation.JsonTypeInfo.Id.NAME;
import static com.apexon.compass.constants.PsrServiceConstants.DATA;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeName;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonTypeName(DATA)
@JsonTypeInfo(use = NAME, include = WRAPPER_OBJECT)
public class DefectAgeingStatsDto {

    private String id;

    private String name;

    private Integer jiraProjectId;

    private List<DefectAgeingStatsRangeDto> range;

}
