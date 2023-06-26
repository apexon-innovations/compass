package com.apexon.compass.onboardservice.dto;

import com.apexon.compass.onboardservice.constants.CommonConstants;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeName;
import lombok.Builder;
import lombok.Data;
import java.util.List;
import static com.apexon.compass.onboardservice.constants.CommonConstants.DATA;

@Data
@Builder
@JsonTypeInfo(include = JsonTypeInfo.As.WRAPPER_OBJECT, use = JsonTypeInfo.Id.NAME)
@JsonTypeName(DATA)
public class ScmConfigResponse {

    private String id;

    private CommonConstants.Product product;

    private String credentials;

    private int codeChurnDelta;

    private int legacyRefactorDelta;

    private CommonConstants.Source source;

    private List<ScmRepoDetails> scm;

    private String userName;

    private String projectId;

    private String createdBy;

    private long createdAt;

    private String updatedBy;

    private long updatedAt;

}
