package com.apexon.compass.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Leaves {

    private Long date;

    private Double hour;

    private String day;

    private String type;

}
