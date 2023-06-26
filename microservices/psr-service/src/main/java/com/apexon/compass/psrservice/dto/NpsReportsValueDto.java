package com.apexon.compass.psrservice.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class NpsReportsValueDto {

    private String key;

    private String rating;

}
