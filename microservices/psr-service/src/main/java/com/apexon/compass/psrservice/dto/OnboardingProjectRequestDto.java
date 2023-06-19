package com.apexon.compass.psrservice.dto;

import static com.apexon.compass.constants.PsrServiceConstants.IMAGE_FILE_EXT_CONTAIN_REGX;
import static com.apexon.compass.constants.PsrServiceConstants.IMAGE_FILE_EXT_REGX;
import static com.apexon.compass.constants.PsrServiceConstants.REGEX_PROJECT_NAME;

import java.util.List;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OnboardingProjectRequestDto {

    @NotBlank
    @Size(max = 255)
    @Pattern(regexp = REGEX_PROJECT_NAME)
    private String name;

    @NotBlank
    private String nestId;

    @NotBlank
    @Size(max = 255)
    @Pattern(regexp = IMAGE_FILE_EXT_REGX)
    private String fileName;

    @NotBlank
    @Pattern(regexp = IMAGE_FILE_EXT_CONTAIN_REGX)
    private String file;

    @Valid
    private PsrFileDto psrFile;

    @Valid
    private List<JiraDto> jira;

    @Valid
    private List<SonarDto> sonar;

    @Valid
    private List<BitBucketDto> bitbucket;

}
