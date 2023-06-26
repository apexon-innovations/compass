package com.apexon.compass.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CodeState {

    private Long value;

    private String developerName;

    private Long addedLineOfCode;

    private Long removedLineOfCode;

    private Long totalLineOfCode;

}
