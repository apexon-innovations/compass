package com.apexon.compass.clientdashboardservice.dto;

import java.util.List;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CodeHealthDataDto {

    private List<CodeHealthDto> data;

}
