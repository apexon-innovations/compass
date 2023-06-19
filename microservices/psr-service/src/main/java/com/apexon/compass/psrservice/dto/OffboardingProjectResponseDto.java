package com.apexon.compass.psrservice.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.NotBlank;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OffboardingProjectResponseDto {

    @NotBlank
    private String id;

    private String name;

    private String psrLocation;

    private List<String> nestIds;

    private List<JiraDto> jira;

    private List<BitBucketDto> bitbucket;

    private List<SonarDto> sonar;

    @JsonProperty("isDeleted")
    private boolean isDeleted;

}
