package com.apexon.compass.sonarservice.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AuthorReviewerDto {

    private String id;

    private String name;

}
