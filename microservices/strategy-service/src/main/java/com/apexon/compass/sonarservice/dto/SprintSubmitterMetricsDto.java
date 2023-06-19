package com.apexon.compass.sonarservice.dto;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.List;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@JsonInclude(NON_NULL)
public class SprintSubmitterMetricsDto {

    private Double averagePrs;

    private List<SubmitterMetricsDto> submitters;

}
