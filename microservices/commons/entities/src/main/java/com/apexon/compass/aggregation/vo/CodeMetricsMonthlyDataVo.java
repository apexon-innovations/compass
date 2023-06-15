package com.apexon.compass.aggregation.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CodeMetricsMonthlyDataVo {

    private String month;

    private Integer addedLineOfCode;

    private Integer removedLineOfCode;

}
