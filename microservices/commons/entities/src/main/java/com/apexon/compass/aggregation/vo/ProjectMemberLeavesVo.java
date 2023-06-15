package com.apexon.compass.aggregation.vo;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProjectMemberLeavesVo {

    private ObjectId id;

    private ObjectId nestProjectId;

    private String memberId;

    private String name;

    private List<LeavesVo> leaves;

}
