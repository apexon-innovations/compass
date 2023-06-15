package com.apexon.compass.onboardservice.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ManagerDetails {

    @NotBlank
    private String image;

    @NotBlank
    private String name;

    @NotBlank
    private String email;

}