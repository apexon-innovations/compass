package com.apexon.compass.sonarservice.dto;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@JsonInclude(NON_NULL)
public class SprintPullRequestSummaryDataDto {

    private String repoId;

    private String repoName;

    private Integer total;

    private Integer unattended;

    private Integer open;

    private Integer merged;

    private Integer declined;

    private Long lastUpdatedDate;

}
