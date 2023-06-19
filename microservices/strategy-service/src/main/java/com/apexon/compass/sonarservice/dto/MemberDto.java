package com.apexon.compass.sonarservice.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MemberDto {

    private String name;

    private Integer codeCommits;

    private Integer totalPrs;

    private Integer mergedPrs;

    private Integer reviewCommentsOnOthersPrs;

}
