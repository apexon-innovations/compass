package com.apexon.compass.psrservice.dto;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(NON_NULL)
public class NpsDataResponseDto {

    private String id;

    private CustomerDto customer;

    private String teamSize;

    private String duration;

    private List<SurveyDto> survey;

    private List<OverallSatisfactionDto> overallSatisfaction;

    private String conclusionRemarks;

    private String submissionPeriod;

    private Long submissionDate;

}
