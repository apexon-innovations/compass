package com.apexon.compass.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Scm {

    private String repoName;

    private String repoUrl;

    private String projectLanguage;

    private String defaultBranchToScan;

}
