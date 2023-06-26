package com.apexon.compass.aggregation.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.Date;
import java.util.List;

/**
 * @author utthan.dharwa
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EwsIssuesVo {

    @Id
    private ObjectId id;

    @Field("ProjectId")
    private String projectId;

    @Field(value = "Account")
    private String account;

    private List<EwsIssueTypeVo> issues;

    @Field("ProjectName")
    private String projectName;

    private List<EwsEngagementTypeVo> engagementType;

    @Field("IssueDescription")
    private String issue;

    private List<EwsImpactLevelVo> impactLevel;

    @Field(value = "MitigateRecommendation")
    private String recommendation;

    @Field("RaisedBy")
    private String raisedBy;

    @Field("RaisedOn")
    private Date raisedOn;

    @Field("ProjectCompletionDate")
    private Date toBeCompletedBy;

    private List<EwsStatusVo> status;

    @Field("AssignedTo")
    private String assignedTo;

    @Field("ProjectEscalationDate")
    private Date escalateOn;

    @Field("StatusUpdatedComments")
    private String comments;

    @Field("ModifiedOn")
    private Date modifiedOn;

    @Field("ModifiedBy")
    private String modifiedBy;

}
