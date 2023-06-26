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
public class ConsolidatedDefectSanpshotVo {

    @Id
    private ObjectId _id;

    private ObjectId projectId;

    private Integer jiraProjectId;

    private Double totalDefects;

    private ConsolidatedDefectSnapshotDataVo data;

}
