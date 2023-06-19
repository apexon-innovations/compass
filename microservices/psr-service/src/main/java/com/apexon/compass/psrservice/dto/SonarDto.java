package com.apexon.compass.psrservice.dto;

import static com.apexon.compass.constants.PsrServiceConstants.ANDROID;
import static com.apexon.compass.constants.PsrServiceConstants.CSHARP;
import static com.apexon.compass.constants.PsrServiceConstants.HTML;
import static com.apexon.compass.constants.PsrServiceConstants.IOS;
import static com.apexon.compass.constants.PsrServiceConstants.JAVA;
import static com.apexon.compass.constants.PsrServiceConstants.JAVASCRIPT;
import static com.apexon.compass.constants.PsrServiceConstants.PYTHON;
import static com.apexon.compass.constants.PsrServiceConstants.REACTJS;
import static com.apexon.compass.constants.PsrServiceConstants.REGEX_URL;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import com.apexon.compass.entities.AcceptedValues;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SonarDto {

    @NotBlank
    private String projectKey;

    @NotBlank
    @Pattern(regexp = REGEX_URL)
    private String url;

    @NotBlank
    @AcceptedValues({ JAVA, CSHARP, PYTHON, JAVASCRIPT, ANDROID, IOS, REACTJS, HTML })
    private String language;

}
