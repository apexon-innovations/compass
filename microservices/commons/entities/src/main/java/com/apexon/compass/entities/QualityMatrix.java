package com.apexon.compass.entities;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class QualityMatrix {

    private String bugs;

    private String newBugs;

    private String coverage;

    private String newCoverage;

    private String codeSmells;

    private String newCodeSmells;

}
