package com.apexon.compass.psrservice.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import java.util.List;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@JsonInclude(Include.NON_NULL)
public class PersonDetailsDto {

    private String email;

    private String name;

    private String dp;

    private List<String> nestProjects;

}
