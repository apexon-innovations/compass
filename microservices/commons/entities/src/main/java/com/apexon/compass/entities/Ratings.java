package com.apexon.compass.entities;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Ratings {

    private Double reliability;

    private Double newReliability;

    private Double security;

    private Double newSecurity;

    private Double maintainability;

    private Double newMaintainability;

}
