package com.apexon.compass.aggregation.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SprintSubmitterMetricsVo {

    private String _id;

    private Double totalPrs;

    private Double mergedPrs;

    private Double openPrs;

    private Double declinedPrs;

    private Double reviewerComments;

    private Double recommits;

    private Double totalTimeForMergedPr;

    private Double totalTimeForDeclinedPr;

}
