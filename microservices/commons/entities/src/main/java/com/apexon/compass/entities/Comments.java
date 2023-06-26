package com.apexon.compass.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Comments {

    private String name;

    private Long commentedOn;

    private Long commentUpdatedOn;

    private String message;

    private String status;

}
