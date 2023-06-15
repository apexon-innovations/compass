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
public class CollectorServiceRunningStatusVo {

    private ObjectId projectId;

    private String collector;

    private String status;

    private String error;

    private Double createdDate;

    private Double updatedDate;

    private String _class;

    private AuthorCollectorServiceVo author;

}
