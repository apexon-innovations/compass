package com.apexon.compass.psrservice.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PercentageTimeDto {

    private String percentage;

    private Integer time;

}