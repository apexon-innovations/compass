package com.apexon.compass.psrservice.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ReportSummaryDto {

    private Integer delivered;

    private Integer planned;

    private Integer completed;

    private Integer blocker;

}
