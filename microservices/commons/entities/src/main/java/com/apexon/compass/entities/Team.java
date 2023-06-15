package com.apexon.compass.entities;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Team {

    private String personName;

    private Integer storyPointCompleted;

}
