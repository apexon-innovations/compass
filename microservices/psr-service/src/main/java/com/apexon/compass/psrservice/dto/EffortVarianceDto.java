package com.apexon.compass.psrservice.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class EffortVarianceDto {

    private PercentageTimeDto sprint;

    private PercentageTimeDto overall;

}
