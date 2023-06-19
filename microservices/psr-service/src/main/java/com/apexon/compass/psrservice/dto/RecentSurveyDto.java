package com.apexon.compass.psrservice.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class RecentSurveyDto {

    private String duration;

    private List<NpsReportsValueDto> value;

}
