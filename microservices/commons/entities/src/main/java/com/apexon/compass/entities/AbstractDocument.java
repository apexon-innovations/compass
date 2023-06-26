package com.apexon.compass.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AbstractDocument {

    private String id;

    @CreatedDate
    private Long createdDate;

    private Long updatedDate;

    @CreatedBy
    private String createdBy;

    private String updatedBy;

}
