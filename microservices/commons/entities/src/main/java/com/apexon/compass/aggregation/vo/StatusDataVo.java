package com.apexon.compass.aggregation.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StatusDataVo {

    private Long date;

    private Integer openTillNowPoints;

    private Integer completedPoints;

    private Integer newlyAddedPoints;

    private Integer reopenPoints;

    private Integer openTillNowCounts;

    private Integer completedCounts;

    private Integer newlyAddedCounts;

    private Integer reopenCounts;

}
