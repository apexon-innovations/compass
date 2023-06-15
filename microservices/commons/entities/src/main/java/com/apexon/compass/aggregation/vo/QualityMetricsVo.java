package com.apexon.compass.aggregation.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class QualityMetricsVo {

    private Integer overallBugs;

    private Integer overallVulnerabilities;

    private Double overallCodeCoverage;

    private Double overallDuplications;

    private Integer overallDuplicationBlocks;

    private Integer newBugs;

    private Integer newVulnerabilities;

    private Double newCodeCoverage;

    private Integer newNewLineOfCodes;

    private Double newDuplications;

    private Integer newDuplicationLines;

}
