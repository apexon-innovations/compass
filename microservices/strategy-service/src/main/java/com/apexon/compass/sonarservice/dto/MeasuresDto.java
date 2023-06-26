
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
public class MeasuresDto {

    @JsonProperty("technicalDebt")
    private String technicalDebt;

    @JsonProperty("security")
    private Double security;

    @JsonProperty("efficiency")
    private Double efficiency;

    @JsonProperty("robustness")
    private Double robustness;

}
