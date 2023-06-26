package com.apexon.compass.psrservice.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PsrFileResponseDto {

    private String fileName;

    private String fileUrl;

}
