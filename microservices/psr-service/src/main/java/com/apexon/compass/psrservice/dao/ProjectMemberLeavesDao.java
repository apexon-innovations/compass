package com.apexon.compass.psrservice.dao;

import static com.apexon.compass.constants.PsrServiceConstants.PROJECT__ID;
import static com.apexon.compass.constants.PsrServiceConstants.LEAVES_DATE;
import static com.apexon.compass.constants.PsrServiceConstants.MEMBER_ID;
import static com.apexon.compass.constants.PsrServiceConstants.NAME;
import static com.apexon.compass.constants.PsrServiceConstants.NEST_PROJECTS_ID;
import static com.apexon.compass.psrservice.constants.AggregationQuery.QUERY_FOR_PROJECT_MEMBER_LEAVE;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.match;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.newAggregation;

import com.apexon.compass.aggregation.vo.NestProjectsIdWiseProjectMemberLeaveVo;
import com.apexon.compass.entities.NestProjects;
import com.apexon.compass.entities.ProjectMemberLeaves;
import java.util.List;
import lombok.AllArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.aggregation.TypedAggregation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor(onConstructor = @__({ @Autowired }))
public class ProjectMemberLeavesDao {

    private MongoTemplate mongoTemplate;

    public List<NestProjectsIdWiseProjectMemberLeaveVo> getMemberLeaveData(long startDate, long endDate,
            ObjectId iscProjectId) {
        TypedAggregation<NestProjects> aggregation = newAggregation(NestProjects.class,
                match(Criteria.where(PROJECT__ID).is(iscProjectId)),
                new CustomAggregation(String.format(QUERY_FOR_PROJECT_MEMBER_LEAVE, startDate, endDate)));
        AggregationResults<NestProjectsIdWiseProjectMemberLeaveVo> results = mongoTemplate.aggregate(aggregation,
                NestProjectsIdWiseProjectMemberLeaveVo.class);
        return results.getMappedResults();
    }

    public List<NestProjectsIdWiseProjectMemberLeaveVo> getMemberLeavesForSprint(String iscProjectID, long startDate,
            long endDate) {
        TypedAggregation<NestProjects> aggregation = newAggregation(NestProjects.class,
                match(Criteria.where(PROJECT__ID).is(new ObjectId(iscProjectID))),
                new CustomAggregation(String.format(QUERY_FOR_PROJECT_MEMBER_LEAVE, startDate, endDate)));
        AggregationResults<NestProjectsIdWiseProjectMemberLeaveVo> results = mongoTemplate.aggregate(aggregation,
                NestProjectsIdWiseProjectMemberLeaveVo.class);
        return results.getMappedResults();
    }

    public ProjectMemberLeaves getMemberLeavebyId(List<ObjectId> nestProjectIds, long startDate, long endDate,
            String memberId) {
        return mongoTemplate.findOne(
                Query.query(Criteria.where(NEST_PROJECTS_ID)
                    .in(nestProjectIds)
                    .and(MEMBER_ID)
                    .is(memberId)
                    .andOperator(Criteria.where(LEAVES_DATE).gte(startDate), Criteria.where(LEAVES_DATE).lte(endDate))),
                ProjectMemberLeaves.class);
    }

    public ProjectMemberLeaves getMemberLeave(List<ObjectId> nestProjectIds, long startDate, long endDate,
            String name) {
        return mongoTemplate.findOne(
                Query.query(Criteria.where(NEST_PROJECTS_ID)
                    .in(nestProjectIds)
                    .and(NAME)
                    .is(name)
                    .andOperator(Criteria.where(LEAVES_DATE).gte(startDate), Criteria.where(LEAVES_DATE).lte(endDate))),
                ProjectMemberLeaves.class);
    }

}
