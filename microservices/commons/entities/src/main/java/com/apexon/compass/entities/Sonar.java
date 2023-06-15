package com.apexon.compass.entities;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Sonar {

    private String key;

    private String url;

    private String language;

}
