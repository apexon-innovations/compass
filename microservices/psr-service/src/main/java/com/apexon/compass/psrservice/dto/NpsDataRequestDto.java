package com.apexon.compass.psrservice.dto;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;
import static com.apexon.compass.constants.PsrServiceConstants.REGEX_FILENAME;

import java.util.List;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

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
public class NpsDataRequestDto {

    @NotBlank
    @Pattern(regexp = REGEX_FILENAME, message = "Must be .xls or .xlsx file")
    private String fileName;

    @NotBlank
    private String submissionDate;

    @NotBlank
    private String submissionPeriod;

    @NotBlank
    private String teamSize;

    @NotBlank
    private String duration;

    @NotBlank
    private String conclusionRemarks;

    @Valid
    private CustomerDto customer;

    @Valid
    private List<SurveyDto> survey;

    @Valid
    private List<OverallSatisfactionDto> overallSatisfaction;

}
