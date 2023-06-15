package com.apexon.compass.aggregation.vo;

import static com.apexon.compass.constants.PsrServiceConstants.ID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Field;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CodeChurnVo {

    @Field(value = ID)
    private String id;

    private Integer productiveCode;

    private Integer codeChurn;

    private Integer addedLineOfCode;

    private Integer removedLineOfCode;

}
