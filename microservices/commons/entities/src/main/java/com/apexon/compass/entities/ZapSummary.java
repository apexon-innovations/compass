package com.apexon.compass.entities;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ZapSummary {

    private Integer highCount;

    private Integer mediumCount;

    private Integer lowCount;

    private Integer informationalCount;

}
