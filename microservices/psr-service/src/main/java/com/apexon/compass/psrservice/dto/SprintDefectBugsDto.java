package com.apexon.compass.psrservice.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SprintDefectBugsDto {

    private Integer logged;

    private Integer accepted;

}