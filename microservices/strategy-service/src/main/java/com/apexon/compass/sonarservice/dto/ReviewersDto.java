package com.apexon.compass.sonarservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReviewersDto {

    private String name;

    private String totalPrs;

    private String totalReviewTime;

}
