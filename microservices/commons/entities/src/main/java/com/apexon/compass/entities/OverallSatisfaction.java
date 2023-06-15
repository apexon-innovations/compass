package com.apexon.compass.entities;

import jakarta.validation.constraints.NotBlank;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OverallSatisfaction {

    @NotBlank
    private String key;

    private String rating;

    private String response;

}
