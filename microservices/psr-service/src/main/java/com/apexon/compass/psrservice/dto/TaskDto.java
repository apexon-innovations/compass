package com.apexon.compass.psrservice.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TaskDto {

    private String number;

    private String name;

    private Boolean isSpilledOver;

    private String type;

    private String url;

    private String status;

}
