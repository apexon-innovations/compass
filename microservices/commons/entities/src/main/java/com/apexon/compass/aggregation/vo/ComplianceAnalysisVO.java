package com.apexon.compass.aggregation.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ComplianceAnalysisVO {

    @Id
    private ObjectId id;

    private Double testCodeCoverage;

    private Double security;

    private Double efficiency;

    private Double robustness;

    private Double vulnerabilities;

    private Double compliance;

}
