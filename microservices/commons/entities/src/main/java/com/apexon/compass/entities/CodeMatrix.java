package com.apexon.compass.entities;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CodeMatrix {

    private String classes;

    private String statements;

    private String ncloc;

    private String commentLines;

    private String files;

    private String lineToCover;

    private String directoryCnt;

    private String lines;

}
