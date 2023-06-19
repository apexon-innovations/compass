package com.apexon.compass.clientdashboardservice.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DefectAgeingTrendsDto {

    private String id;

    private String name;

    private Integer jiraProjectId;

    private List<DefectAgeingRangeDto> range;

}
