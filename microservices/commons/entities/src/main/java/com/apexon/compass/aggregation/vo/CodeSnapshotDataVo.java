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
public class CodeSnapshotDataVo {

    private String repoId;

    private String repoName;

    private Integer totalLineOfCode;

    private List<CodeSnapshotViolationsVo> violationsAndTechnicalDebt;

    private List<CodeSnapshotScmDataVo> scmData;

}
