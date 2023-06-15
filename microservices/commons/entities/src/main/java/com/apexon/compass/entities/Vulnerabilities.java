package com.apexon.compass.entities;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Vulnerabilities {

    private String description;

    private String featureversion;

    private String vulnerability;

    private String nameSpace;

    private String featureName;

    private String fixedby;

    private String link;

    private String severity;

}
