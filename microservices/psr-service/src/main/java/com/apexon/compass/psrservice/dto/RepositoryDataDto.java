package com.apexon.compass.psrservice.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RepositoryDataDto {

    private String repoId;

    private String repoName;

    private Boolean isScmAvailable;

    private Boolean isSonarAvailable;

    private Long totalLineOfCode;

}
