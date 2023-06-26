package com.apexon.compass.sonarservice.dto;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@JsonInclude(NON_NULL)
public class SubmitterMetricsDto {

    private String name;

    private Integer totalPrs;

    private Integer openPrs;

    private Integer mergedPrs;

    private Integer declinedPrs;

    private Integer reviewerComments;

    private Integer recommits;

    private String averagePrCycleTime;

}
