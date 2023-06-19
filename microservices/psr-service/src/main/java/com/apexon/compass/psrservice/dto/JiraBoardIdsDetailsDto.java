package com.apexon.compass.psrservice.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonInclude(NON_NULL)
public class JiraBoardIdsDetailsDto {

    private List<BoardDetailsDto> data;

}
