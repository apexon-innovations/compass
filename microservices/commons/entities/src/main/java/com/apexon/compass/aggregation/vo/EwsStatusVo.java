package com.apexon.compass.aggregation.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 * @author utthan.dharwa
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EwsStatusVo {

    @Field("name")
    private String status;

}
