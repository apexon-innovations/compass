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
public class ScmCodeAnalysisMonthRepoVo {

    private String repoId;

    private String repoName;

    private String sonarProjectId;

    private String scmRepoUrl;

    private Boolean scm;

    private Boolean sonar;

    private List<Integer> changedLoc;

}
