package com.apexon.compass.aggregation.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CodeTechDebtMonthlyDataVo {

    private String month;

    private Integer complexity;

    private Integer issues;

    private Integer maintainability;

    private Integer reliability;

    private Double security;

    private Integer size;

    private Double coverage;

    private Double duplication;

    private Double total;

}
