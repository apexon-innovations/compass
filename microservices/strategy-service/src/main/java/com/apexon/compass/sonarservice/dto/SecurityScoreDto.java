
package com.apexon.compass.sonarservice.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SecurityScoreDto {

    @JsonProperty("cisq")
    private String cisq;

    @JsonProperty("cwe")
    private String cwe;

    @JsonProperty("owasp")
    private String owasp;

    @JsonProperty("gdpr")
    private String gdpr;

    @JsonProperty("sqlInjections")
    private String sqlInjections;

    @JsonProperty("xss")
    private String xss;

    @JsonProperty("commandInjection")
    private String commandInjection;

    @JsonProperty("misconfiguration")
    private String misconfiguration;

}
