package com.apexon.compass.clientdashboardservice.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ViolationsDto {

    private Integer criticalAdded;

    private Integer criticalRemoved;

    private Integer total;

}
