package com.apexon.compass.aggregation.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TicketStatusVo {

    @JsonProperty
    private Double _id;

    @JsonProperty
    private Double delivered;

    @JsonProperty
    private Double opened;

}
