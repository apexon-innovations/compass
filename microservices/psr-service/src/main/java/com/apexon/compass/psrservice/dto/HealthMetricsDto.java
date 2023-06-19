package com.apexon.compass.psrservice.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(NON_NULL)
public class HealthMetricsDto {

    private String overall;

    private String periodicReview;

    private String scope;

    private String deliverables;

    private String customer;

    private String schedule;

    private String financials;

    private String people;

    private String compliance;

    private String startUp;

    private String process;

    private String planning;

}
