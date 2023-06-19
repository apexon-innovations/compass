
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
public class SecurityDto {

    @JsonProperty("securityScore")
    private SecurityScoreDto securityScore;

    @JsonProperty("name")
    private String name;

    @JsonProperty("id")
    private String id;

}
