package com.apexon.compass.psrservice.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonInclude(NON_NULL)
public class CompletedSprintDto {

    private Long lastUpdated;

    private Integer remainingSprint;

    private Integer jiraProjectId;

    private String projectName;

    private String projectId;

    private Integer totalSprint;

    private String ratio;

}
