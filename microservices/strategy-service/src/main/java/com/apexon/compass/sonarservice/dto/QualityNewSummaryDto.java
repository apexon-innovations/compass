package com.apexon.compass.sonarservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class QualityNewSummaryDto extends QualityMetricsDto {

    private Integer newLineOfCodes;

    private Integer duplicationLines;

}
