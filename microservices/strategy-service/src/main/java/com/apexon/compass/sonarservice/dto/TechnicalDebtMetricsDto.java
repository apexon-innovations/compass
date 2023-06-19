package com.apexon.compass.sonarservice.dto;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;
import static com.apexon.compass.constants.PsrServiceConstants.DATA;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(NON_NULL)
@JsonTypeName(DATA)
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.WRAPPER_OBJECT)
public class TechnicalDebtMetricsDto {

    private double complexity;

    private double duplications;

    private double issues;

    private String maintainability;

    private String reliability;

    private String security;

    private double size;

    private double coverage;

}
