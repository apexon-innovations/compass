package com.apexon.compass.aggregation.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CurrentDataVo {

    @Id
    private ObjectId id;

    private ViolationsVO violations;

    private Long createdDate;

    private String sonarProjectId;

}
