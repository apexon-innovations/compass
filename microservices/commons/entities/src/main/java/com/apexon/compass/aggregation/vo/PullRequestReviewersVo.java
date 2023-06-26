package com.apexon.compass.aggregation.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PullRequestReviewersVo {

    private String _id;

    private Double totalPrs;

    private Double totalTimeForMergedPr;

    private Double totalTimeForDeclinedPr;

    private Double totalTimeForOpenPr;

}
