package com.apexon.compass.psrservice.dto;

import static com.apexon.compass.constants.PsrServiceConstants.REGEX_URL;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BitBucketDto {

    @NotBlank
    private String name;

    @NotBlank
    @Pattern(regexp = REGEX_URL)
    private String url;

}
