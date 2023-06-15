package com.apexon.compass.onboardservice.dto;

import java.util.List;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class SonarConfigRequest {

    @Size(min = 1)
    @Valid
    private List<ProjectsMetaData> projects;

    @NotBlank
    private String credentials;

    @NotBlank
    private String userName;

    @NotBlank
    private String projectId;

    @NotBlank
    private String version;

    @NotBlank
    private String url;

}
