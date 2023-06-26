package com.apexon.compass.psrservice.dao;

import static com.apexon.compass.constants.EntitiesConstants.PROJECTS;
import static com.apexon.compass.constants.PsrServiceConstants.AM_EMAIL;
import static com.apexon.compass.constants.PsrServiceConstants.CLIENT_PM_EMAIL;
import static com.apexon.compass.constants.PsrServiceConstants.DM_EMAIL;
import static com.apexon.compass.constants.PsrServiceConstants.ID;
import static com.apexon.compass.constants.PsrServiceConstants.IS_DELETED;
import static com.apexon.compass.constants.PsrServiceConstants.PM_EMAIL;
import static com.apexon.compass.psrservice.constants.AggregationQuery.QUERY_FOR_ALL_ACTIVE_PROJECT_WISE_OVERALL_HEALTH;
import static com.apexon.compass.psrservice.constants.AggregationQuery.QUERY_FOR_GET_NPS_REPORT_BY_PROJECT_ID;
import static com.apexon.compass.psrservice.constants.AggregationQuery.QUERY_FOR_PROJECT_DETAILS_WITH_WEEKLY;
import static com.apexon.compass.psrservice.constants.AggregationQuery.QUERY_FOR_PROJECT_DETAILS_WITH_ONETIME;
import static java.lang.Boolean.FALSE;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.match;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.newAggregation;
import java.util.Arrays;
import java.util.List;

import com.apexon.compass.aggregation.vo.*;
import com.apexon.compass.entities.Project;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.aggregation.TypedAggregation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;
import com.apexon.compass.exception.custom.ServiceException;
import lombok.AllArgsConstructor;

@Component
@AllArgsConstructor(onConstructor = @__({ @Autowired }))
public class ProjectDetailsDao {

    public static final String PLEASE_ENTER_VALID_PROJECT_ID = "Please enter valid project id";

    private final MongoTemplate mongoTemplate;

    public ProjectDetailsWithWeeklyVo getProjectDetailsWithWeekly(String projectId) {
        TypedAggregation<Project> aggregation = newAggregation(Project.class,
                match(Criteria.where(ID).is(projectId).andOperator(Criteria.where(IS_DELETED).is(Boolean.FALSE))),
                new CustomAggregation(String.format(QUERY_FOR_PROJECT_DETAILS_WITH_WEEKLY.toString(), projectId)));
        AggregationResults<ProjectDetailsWithWeeklyVo> results = mongoTemplate.aggregate(aggregation,
                ProjectDetailsWithWeeklyVo.class);
        return results.getUniqueMappedResult();
    }

    public ProjectDetailsWithOneTimeVo getProjectDetailsWithOneTime(String projectId) {
        try {
            TypedAggregation<Project> aggregation = newAggregation(Project.class, match(
                    Criteria.where("_id").is(projectId).andOperator(Criteria.where(IS_DELETED).is(Boolean.FALSE))),
                    new CustomAggregation(String.format(QUERY_FOR_PROJECT_DETAILS_WITH_ONETIME.toString(), projectId)));
            AggregationResults<ProjectDetailsWithOneTimeVo> results = mongoTemplate.aggregate(aggregation,
                    ProjectDetailsWithOneTimeVo.class);
            return results.getUniqueMappedResult();
        }
        catch (IllegalArgumentException e) {
            throw new ServiceException(PLEASE_ENTER_VALID_PROJECT_ID);
        }
    }

    public List<AllActiveProjectWiseOverAllHealthVo> getProjectDetailsWithoverallHealth(String email) {
        try {
            TypedAggregation<Project> aggregation = newAggregation(Project.class,
                    match(Criteria.where(IS_DELETED)
                        .in(Arrays.asList(null, Boolean.FALSE))
                        .orOperator(Criteria.where(PM_EMAIL).is(email), Criteria.where(DM_EMAIL).is(email),
                                Criteria.where(AM_EMAIL).is(email), Criteria.where(CLIENT_PM_EMAIL).is(email))),
                    new CustomAggregation(QUERY_FOR_ALL_ACTIVE_PROJECT_WISE_OVERALL_HEALTH));
            AggregationResults<AllActiveProjectWiseOverAllHealthVo> results = mongoTemplate.aggregate(aggregation,
                    AllActiveProjectWiseOverAllHealthVo.class);
            return results.getMappedResults();
        }
        catch (IllegalArgumentException e) {
            throw new ServiceException(PLEASE_ENTER_VALID_PROJECT_ID);
        }
    }

    public Project findAssociatedUsersWithProject(String id, String email) {
        Criteria criteria = new Criteria();
        criteria
            .orOperator(Criteria.where(PM_EMAIL).is(email), Criteria.where(DM_EMAIL).is(email),
                    Criteria.where(AM_EMAIL).is(email), Criteria.where(CLIENT_PM_EMAIL).is(email))
            .and(ID)
            .is(new ObjectId(id))
            .and(IS_DELETED)
            .in(Arrays.asList(FALSE, null));
        Query query = new Query(criteria);
        return mongoTemplate.findOne(query, Project.class, PROJECTS);
    }

    public NpsReportsByProjectIdVo getNpsReport(String iscprojectId, Integer limit) {
        TypedAggregation<Project> aggregation = newAggregation(Project.class,
                match(Criteria.where(ID).is(iscprojectId).andOperator(Criteria.where(IS_DELETED).is(Boolean.FALSE))),
                new CustomAggregation(
                        String.format(QUERY_FOR_GET_NPS_REPORT_BY_PROJECT_ID.toString(), iscprojectId, limit)));
        AggregationResults<NpsReportsByProjectIdVo> results = mongoTemplate.aggregate(aggregation,
                NpsReportsByProjectIdVo.class);
        return results.getUniqueMappedResult();
    }

    public List<AllActiveClientProjectVo> getClientProjectDetails(String email) {
        try {
            TypedAggregation<Project> aggregation = newAggregation(Project.class, match(
                    Criteria.where(IS_DELETED).in(Arrays.asList(null, Boolean.FALSE)).and(CLIENT_PM_EMAIL).is(email)),
                    new CustomAggregation(QUERY_FOR_ALL_ACTIVE_PROJECT_WISE_OVERALL_HEALTH));
            AggregationResults<AllActiveClientProjectVo> results = mongoTemplate.aggregate(aggregation,
                    AllActiveClientProjectVo.class);
            return results.getMappedResults();
        }
        catch (IllegalArgumentException e) {
            throw new ServiceException(PLEASE_ENTER_VALID_PROJECT_ID);
        }
    }

}
