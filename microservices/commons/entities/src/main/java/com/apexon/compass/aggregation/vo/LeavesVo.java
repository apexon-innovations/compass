package com.apexon.compass.aggregation.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class LeavesVo {

    private Long date;

    private Double hour;

    private String day;

    private String type;

}
