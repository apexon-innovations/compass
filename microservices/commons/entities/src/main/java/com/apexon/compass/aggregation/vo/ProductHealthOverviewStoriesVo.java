package com.apexon.compass.aggregation.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductHealthOverviewStoriesVo {

    private Double _id;

    private Double totalStories;

    private Double completedStories;

    private Double inProgressStories;

    private Double backlogStories;

}
