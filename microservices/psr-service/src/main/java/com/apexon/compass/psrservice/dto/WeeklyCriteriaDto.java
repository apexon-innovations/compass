package com.apexon.compass.psrservice.dto;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;
import static com.apexon.compass.constants.PsrServiceConstants.NA;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(NON_NULL)
public class WeeklyCriteriaDto {

    private Integer red;

    private Integer green;

    @JsonProperty(NA)
    private Integer na;

    private String name;

    private Integer amber;

}
