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
public class StoryReportVo {

    @Id
    private Double id;

    private Double total;

    private Double totalCompleted;

    private Double totalDelivered;

    private Double totalBlocked;

    private Double totalPlanned;

}
