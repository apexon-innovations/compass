package com.apexon.compass.psrservice.dto;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonInclude(NON_NULL)
public class SprintProgressDto {

    private Integer sprintId;

    private Long lastUpdated;

    private Integer jiraProjectId;

    private List<SprintStatusDto> sprintStatus;

    private Long endDate;

    private List<FeaturesDto> featureStatus;

    private String sprintName;

    private Integer remainingDays;

    private String projectName;

    private String projectId;

    private Long startDate;

}
