package com.apexon.compass.onboardservice.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ScmRepoDetails {

    @NotBlank
    private String repoUrl;

    @NotBlank
    private String projectLanguage;

    @NotBlank
    private String defaultBranchToScan;

    @NotBlank
    private String repoName;

}