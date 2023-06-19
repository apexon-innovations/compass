package com.apexon.compass.psrservice.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DailyStatusDto {

    private Long date;

    private Integer todo;

    private Integer inprogress;

    private Integer remaining;

    private Integer completed;

}
