package com.apexon.compass.aggregation.vo;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CodeTechDebtVo {

    private String _id;

    private String repoId;

    private String repoName;

    private List<CodeTechDebtMonthlyDataVo> monthData;

}
