package com.apexon.compass.onboardservice.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class ProjectsMetaData {

    @NotBlank
    private String language;

    @NotBlank
    private String key;

    @NotBlank
    private String url;

}
