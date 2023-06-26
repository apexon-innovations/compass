package com.apexon.compass.sonarservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class QualityMetricsDto {

    private Integer bugs;

    private Integer vulnerabilities;

    private String codeCoverage;

    private String duplications;

}
