package com.apexon.compass.psrservice.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MemberStatusDto {

    private Integer todo;

    private Integer inProgress;

    private Integer completed;

    private Integer availableHours;

    private Integer averageStoryPoints;

    private Integer spilledOver;

}
