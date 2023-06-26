package com.apexon.compass.onboardservice.dto;

import java.util.List;
import com.apexon.compass.onboardservice.constants.CommonConstants;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ScmConfigRequest {

    private CommonConstants.Product product;

    @NotBlank
    private String credentials;

    private int codeChurnDelta;

    private int legacyRefactorDelta;

    private CommonConstants.Source source;

    @Size(min = 1)
    @Valid
    private List<ScmRepoDetails> scm;

    @NotBlank
    private String userName;

    @NotBlank
    private String projectId;

}
