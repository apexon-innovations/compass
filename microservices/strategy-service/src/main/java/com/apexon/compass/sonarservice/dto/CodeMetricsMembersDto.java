package com.apexon.compass.sonarservice.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CodeMetricsMembersDto {

    private String name;

    private Long lineOfCode;

    private Long codeChurn;

    private Long legacyRefactor;

    private Long addedLineOfCode;

    private Long removedLineOfCode;

    private Long addedLineOfCodeTillDate;

    private Long removedLineOfCodeTillDate;

}
