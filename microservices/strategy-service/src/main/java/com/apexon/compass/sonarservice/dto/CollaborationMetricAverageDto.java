package com.apexon.compass.sonarservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CollaborationMetricAverageDto {

    private String prResolveTime;

    private String firstComment;

    private Integer recommit;

}