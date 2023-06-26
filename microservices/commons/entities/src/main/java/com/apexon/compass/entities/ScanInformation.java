package com.apexon.compass.entities;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ScanInformation {

    private String dependencyCheckVersion;

    private String reportGenerationOn;

    private String totalDependenciesScanned;

    private long vulnerableDependencies;

    private long vulnerabilitiesFound;

    private long suppressedVulnerabilities;

    private long cveCheckedOn;

    private long cveModifiedOn;

    private long versionCheckedOn;

}
