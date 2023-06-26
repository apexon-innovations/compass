package com.apexon.compass.entities;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Issues {

    private String open;

    private String confirmed;

    private String wontFix;

    private String falsePositive;

    private String reOpened;

}
