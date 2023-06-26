
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
public class ViolationsDto {

    @JsonProperty("blocker")
    private Integer blocker;

    @JsonProperty("total")
    private Integer total;

    @JsonProperty("major")
    private Integer major;

    @JsonProperty("critical")
    private Integer critical;

    @JsonProperty("minor")
    private Integer minor;

    @JsonProperty("info")
    private Integer info;

}
