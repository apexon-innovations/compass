package com.apexon.compass.psrservice.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class AllClientProjectDto {

    private List<ClientProjectDto> data;

}
