package com.apexon.compass.clientdashboardservice.dto;

import java.util.List;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TechDebtRepoDataDto {

    private String repoId;

    private String repoName;

    private List<TechDebtMonthlyDataDto> monthData;

}
