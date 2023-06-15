package com.apexon.compass.entities;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DockerImageSummary {

    private String image;

    private String unapproved;

    private Vulnerabilities vulnerabilities;

}
