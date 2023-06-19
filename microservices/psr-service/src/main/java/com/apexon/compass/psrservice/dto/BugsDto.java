package com.apexon.compass.psrservice.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BugsDto {

    private Integer total;

    private Integer pending;

    private Integer inProgress;

    private Integer completed;

}
