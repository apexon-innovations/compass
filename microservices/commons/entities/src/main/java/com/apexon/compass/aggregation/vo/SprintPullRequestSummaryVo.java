package com.apexon.compass.aggregation.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SprintPullRequestSummaryVo {

    private Integer _id;

    private Double total;

    private Double merged;

    private Double open;

    private Double declined;

    private Double unattended;

}
