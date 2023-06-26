package com.apexon.compass.sonarservice.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CommentsDto {

    private String by;

    private Long time;

    private String message;

}
