package com.apexon.compass.psrservice.dao;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;
import com.apexon.compass.entities.SprintChart;
import lombok.AllArgsConstructor;

@Component
@AllArgsConstructor(onConstructor = @__({ @Autowired }))
public class DerivedSprintsDao {

    private final MongoTemplate mongoTemplate;

    public List<String> findDistinctTeamMembersByProjectId(String jiraProjectId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("jiraProjectId").is(jiraProjectId));
        return mongoTemplate.findDistinct(query, "statusData.team.personName", SprintChart.class, String.class);
    }

}
