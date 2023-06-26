package com.apexon.compass.sonarservice.dao;

import static com.apexon.compass.constants.EntitiesConstants.PROJECTS;
import static com.apexon.compass.constants.PsrServiceConstants.AM_EMAIL;
import static com.apexon.compass.constants.PsrServiceConstants.CLIENT_PM_EMAIL;
import static com.apexon.compass.constants.PsrServiceConstants.DM_EMAIL;
import static com.apexon.compass.constants.PsrServiceConstants.ID;
import static com.apexon.compass.constants.PsrServiceConstants.IS_DELETED;
import static com.apexon.compass.constants.PsrServiceConstants.PM_EMAIL;
import static java.lang.Boolean.FALSE;

import com.apexon.compass.entities.Project;
import java.util.Arrays;
import lombok.AllArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor(onConstructor = @__({ @Autowired }))
public class ProjectDao {

    private final MongoTemplate mongoTemplate;

    public Project findAssociatedUsersWithProject(String projectId, String email) {
        Criteria criteria = new Criteria();
        criteria
            .orOperator(Criteria.where(PM_EMAIL).is(email), Criteria.where(DM_EMAIL).is(email),
                    Criteria.where(AM_EMAIL).is(email), Criteria.where(CLIENT_PM_EMAIL).is(email))
            .and(ID)
            .is(new ObjectId(projectId))
            .and(IS_DELETED)
            .in(Arrays.asList(FALSE, null));
        Query query = new Query(criteria);
        return mongoTemplate.findOne(query, Project.class, PROJECTS);
    }

}
