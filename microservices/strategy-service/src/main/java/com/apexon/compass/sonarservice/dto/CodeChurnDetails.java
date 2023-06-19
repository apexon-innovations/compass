package com.apexon.compass.sonarservice.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CodeChurnDetails {

    private Double productiveCode;

    private Double codeChurn;

    private Double addedLineOfCode;

    private Double removedLineOfCode;

}
