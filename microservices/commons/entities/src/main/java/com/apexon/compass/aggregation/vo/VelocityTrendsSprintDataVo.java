package com.apexon.compass.aggregation.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class VelocityTrendsSprintDataVo {

    private ProjectAndJiraIdVo _id;

    private SprintDetailsVo sprintDetails;

    private List<VelocityTrendsSprintPointsCountsDataVo> data;

}
