package com.apexon.compass.psrservice.dao;

import static com.apexon.compass.constants.PsrServiceConstants.PROJECT__ID;
import static com.apexon.compass.psrservice.constants.AggregationQuery.QUERY_FOR_FIND_PROJECT_MEMBERS;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.match;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.newAggregation;

import com.apexon.compass.aggregation.vo.NestProjectWiseProjectMembersVo;
import com.apexon.compass.entities.NestProjects;
import java.util.List;
import lombok.AllArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.aggregation.TypedAggregation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor(onConstructor = @__({ @Autowired }))
public class ProjectMembersDao {

    private MongoTemplate mongoTemplate;

    public List<NestProjectWiseProjectMembersVo> findProjectMemberForSprint(String iscProjectId) {
        TypedAggregation<NestProjects> aggregation = newAggregation(NestProjects.class,
                match(Criteria.where(PROJECT__ID).is(new ObjectId(iscProjectId))),
                new CustomAggregation(QUERY_FOR_FIND_PROJECT_MEMBERS.toString()));
        AggregationResults<NestProjectWiseProjectMembersVo> results = mongoTemplate.aggregate(aggregation,
                NestProjectWiseProjectMembersVo.class);
        return results.getMappedResults();
    }

}
