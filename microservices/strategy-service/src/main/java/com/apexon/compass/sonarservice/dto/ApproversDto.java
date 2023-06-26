package com.apexon.compass.sonarservice.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ApproversDto {

    private String id;

    private String name;

    private Long time;

}
