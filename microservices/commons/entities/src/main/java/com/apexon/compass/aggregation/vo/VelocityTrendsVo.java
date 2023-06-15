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
public class VelocityTrendsVo {

    private ProjectAndJiraIdVo _id;

    private List<VelocityTrendsSprintDataVo> data;

}
