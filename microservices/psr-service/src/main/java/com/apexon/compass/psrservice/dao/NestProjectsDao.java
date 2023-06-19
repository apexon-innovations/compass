package com.apexon.compass.psrservice.dao;

import static com.apexon.compass.constants.PsrServiceConstants.PROJECT__ID;
import static com.apexon.compass.psrservice.constants.AggregationQuery.QUERY_FOR_PROJECT_MEMBERS_DETAILS_FROM_NEST_PROJECT;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.match;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.newAggregation;

import com.apexon.compass.aggregation.vo.NestProjectWiseProjectMembersVo;
import com.apexon.compass.entities.NestProjects;
import lombok.AllArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.aggregation.TypedAggregation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@AllArgsConstructor(onConstructor = @__({ @Autowired }))
public class NestProjectsDao {

    private final MongoTemplate mongoTemplate;

    public List<NestProjectWiseProjectMembersVo> getProjectMembers(String id) {

        TypedAggregation<NestProjects> aggregation = newAggregation(NestProjects.class,
                match(Criteria.where(PROJECT__ID).is(new ObjectId(id))),
                new CustomAggregation(QUERY_FOR_PROJECT_MEMBERS_DETAILS_FROM_NEST_PROJECT.toString()));
        AggregationResults<NestProjectWiseProjectMembersVo> results = mongoTemplate.aggregate(aggregation,
                NestProjectWiseProjectMembersVo.class);
        return results.getMappedResults();
    }

}
