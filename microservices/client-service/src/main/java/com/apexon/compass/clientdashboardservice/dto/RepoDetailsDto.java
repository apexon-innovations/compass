package com.apexon.compass.clientdashboardservice.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@JsonInclude(Include.NON_NULL)
public class RepoDetailsDto {

    private String repoId;

    private String repoName;

    private Integer totalViolations;

    private Integer totalLineOfCode;

    private Integer codeChurn;

    private Integer legacyRefactor;

    private String technicalDebt;

    private Double compliance;

}
