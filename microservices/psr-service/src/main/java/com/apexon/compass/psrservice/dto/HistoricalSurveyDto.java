package com.apexon.compass.psrservice.dto;

import java.util.List;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class HistoricalSurveyDto {

    private String duration;

    private Double average;

    private List<NpsReportsValueDto> value;

}
