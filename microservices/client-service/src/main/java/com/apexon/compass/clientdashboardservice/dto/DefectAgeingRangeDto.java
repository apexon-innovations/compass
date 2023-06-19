package com.apexon.compass.clientdashboardservice.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DefectAgeingRangeDto {

    private String value;

    private Integer count;

}
