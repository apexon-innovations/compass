package com.apexon.compass.aggregation.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StoryBugsVo {

    private Double _id;

    private Double total;

    private Double totalCompleted;

    private Double totalOpen;

    private Double totalInProgress;

}
