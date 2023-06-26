package com.apexon.compass.sonarservice.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CommitsDto {

    private Long time;

    private String revisionNumber;

    private String message;

    private String authorEmail;

    private String authorUserId;

    private String type;

}
