package com.apexon.compass.psrservice.dto;

import jakarta.validation.constraints.NotBlank;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OverallSatisfactionDto {

    @NotBlank
    private String key;

    private String rating;

    private String response;

}
