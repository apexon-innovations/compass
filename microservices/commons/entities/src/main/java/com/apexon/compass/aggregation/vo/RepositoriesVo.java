package com.apexon.compass.aggregation.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RepositoriesVo {

    private String repoId;

    private String repoName;

    private String sonarProjectId;

    private String scmRepoUrl;

}
