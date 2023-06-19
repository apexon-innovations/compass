package com.apexon.compass.psrservice.dao;

import static com.apexon.compass.constants.PsrServiceConstants.PROJECT__ID;
import static com.apexon.compass.psrservice.constants.AggregationQuery.QUERY_FOR_SPRINT_MEMBER_DETAILS_BY_MEMBER_ID;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.match;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.newAggregation;

import java.util.List;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.aggregation.TypedAggregation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Component;
import com.apexon.compass.aggregation.vo.NestMemberDetailsByMemberIdVo;
import com.apexon.compass.entities.NestProjects;
import lombok.AllArgsConstructor;

@Component
@AllArgsConstructor(onConstructor = @__({ @Autowired }))
public class NestProjectMemberDao {

    private final MongoTemplate mongoTemplate;

    public List<NestMemberDetailsByMemberIdVo> getMemberDetailsByMemberId(String iscProjectId, String memberId) {
        TypedAggregation<NestProjects> typedAggregation = newAggregation(NestProjects.class,
                match(Criteria.where(PROJECT__ID).is(new ObjectId(iscProjectId))),
                new CustomAggregation(String.format(QUERY_FOR_SPRINT_MEMBER_DETAILS_BY_MEMBER_ID, memberId)));
        AggregationResults<NestMemberDetailsByMemberIdVo> results = mongoTemplate.aggregate(typedAggregation,
                NestMemberDetailsByMemberIdVo.class);
        return results.getMappedResults();
    }

}
