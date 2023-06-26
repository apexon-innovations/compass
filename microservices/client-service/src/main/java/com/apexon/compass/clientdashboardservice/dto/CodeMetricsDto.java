package com.apexon.compass.clientdashboardservice.dto;

import java.util.List;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CodeMetricsDto {

    private String repoId;

    private String repoName;

    private List<CodeMetricsMonthlyDataDto> monthData;

    private Integer codeChurn;

    private Integer legacyRefactor;

}
