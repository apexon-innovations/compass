package com.apexon.compass.entities;

import org.springframework.data.mongodb.core.mapping.Field;
import static com.apexon.compass.constants.EntitiesConstants.PACKAGE;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Summary {

    private String dependencyName;

    private String vulnerabilityIds;

    @Field(name = PACKAGE)
    private String pkg;

    private String gav;

    private String severity;

    private Integer cveCount;

    private String confidence;

    private Integer evidenceCount;

}
