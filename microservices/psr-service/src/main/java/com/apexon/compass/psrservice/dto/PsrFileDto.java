package com.apexon.compass.psrservice.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PsrFileDto {

    @NotBlank
    private String fileName;

    @NotBlank
    private String file;

}
