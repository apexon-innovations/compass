package com.apexon.compass.aggregation.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductHealthOverviewDataVo {

    private ProductHealthOverviewStoriesVo stories;

    private ProductHealthOverviewDefectsVo defects;

}
