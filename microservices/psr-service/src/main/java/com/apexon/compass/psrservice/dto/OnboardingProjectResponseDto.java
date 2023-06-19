package com.apexon.compass.psrservice.dto;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(NON_NULL)
public class OnboardingProjectResponseDto {

    private String id;

    private String name;

    private String clientName;

    private List<String> nestIds;

    private String logo;

    private List<JiraDto> jira;

    private List<BitBucketDto> bitbucket;

    private List<SonarDto> sonar;

    private PsrFileResponseDto psrFile;

}
