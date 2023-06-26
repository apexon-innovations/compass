
package com.apexon.compass.sonarservice.dto;

import java.util.Map;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder(toBuilder = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@AllArgsConstructor
@NoArgsConstructor
public class SearchParametersDto {

    @JsonProperty("name")
    private String name;

    @JsonProperty("measures")
    private MeasuresDto measures;

    @JsonProperty("risk")
    Map<String, Object> risk;

    @JsonProperty("id")
    private String id;

    @JsonProperty("newViolations")
    private NewViolationsDto newViolations;

    @JsonProperty("violations")
    private ViolationsDto violations;

}
