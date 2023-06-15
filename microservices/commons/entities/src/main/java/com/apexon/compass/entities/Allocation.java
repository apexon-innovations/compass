package com.apexon.compass.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Allocation {

    private String startDate;

    private String endDate;

    private String days;

    private String allocation;

    private String isBillable;

    private String projectRole;

}