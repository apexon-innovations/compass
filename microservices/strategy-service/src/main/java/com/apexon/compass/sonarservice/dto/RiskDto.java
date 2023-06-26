
package com.apexon.compass.sonarservice.dto;

import java.util.Map;
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
public class RiskDto {

    @JsonProperty("risk")
    Map<String, Object> risk;

    @JsonProperty("id")
    private String id;

    @JsonProperty("name")
    private String name;

}
