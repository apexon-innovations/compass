package com.apexon.compass.aggregation.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductHealthOverviewVo {

    private ObjectId _id;

    private ObjectId projectId;

    private Integer jiraProjectId;

    private ProductHealthOverviewDataVo data;

}
