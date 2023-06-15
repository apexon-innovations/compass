package com.apexon.compass.entities;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Duplication {

    private String lineDensity;

    private String lines;

    private String blocks;

    private String newLineDensity;

    private String newLines;

    private String newBlocks;

}
