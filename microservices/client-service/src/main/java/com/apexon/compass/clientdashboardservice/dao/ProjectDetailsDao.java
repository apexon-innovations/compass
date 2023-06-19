package com.apexon.compass.clientdashboardservice.dao;

import static com.apexon.compass.clientdashboardservice.constants.AggregationQuery.QUERY_FOR_FIND_ALL_PROJECTS_OF_USER;
import static com.apexon.compass.clientdashboardservice.constants.AggregationQuery.QUERY_FOR_FIND_ALL_USER_WITH_ISC_PROJECTS;
import static com.apexon.compass.constants.ClientDashboardServiceConstants.AM_EMAIL;
import static com.apexon.compass.constants.ClientDashboardServiceConstants.CLIENT_PM_EMAIL;
import static com.apexon.compass.constants.ClientDashboardServiceConstants.DM_EMAIL;
import static com.apexon.compass.constants.ClientDashboardServiceConstants.ID;
import static com.apexon.compass.constants.ClientDashboardServiceConstants.PROJECT;
import static com.apexon.compass.constants.ClientDashboardServiceConstants.PROJECTID;
import static com.apexon.compass.constants.ClientDashboardServiceConstants.IS_DELETED;
import static com.apexon.compass.constants.ClientDashboardServiceConstants.JIRACONFIGURATION;
import static com.apexon.compass.constants.ClientDashboardServiceConstants.JIRA_ID;
import static com.apexon.compass.constants.ClientDashboardServiceConstants.PM_EMAIL;
import static com.apexon.compass.constants.EntitiesConstants.PROJECTS;
import static com.apexon.compass.constants.EntitiesConstants.JIRA_CONFIGURATION;
import static com.apexon.compass.constants.EntitiesConstants.SPRINTS;
import static com.apexon.compass.constants.PsrServiceConstants.JIRA_PROJECT_ID;
import static java.lang.Boolean.FALSE;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.lookup;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.match;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.newAggregation;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.unwind;

import com.apexon.compass.aggregation.vo.ProjectsWithJiraConfigurationVo;
import com.apexon.compass.constants.QueryConstants;
import com.apexon.compass.entities.Project;
import com.apexon.compass.entities.JiraConfiguration;
import java.util.Arrays;
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
public class ProjectDetailsDao {

    private final MongoTemplate mongoTemplate;

    public Project findAssociatedUsersWithProject(String id, String email) {
        Query query = new Query(Criteria.where(CLIENT_PM_EMAIL)
            .is(email)
            .and(ID)
            .is(new ObjectId(id))
            .and(IS_DELETED)
            .in(Arrays.asList(FALSE, null)));
        return mongoTemplate.findOne(query, Project.class, PROJECTS);
    }

    public List<Project> findAssociatedUsersWithProjects(List<ObjectId> ids, String email) {
        Query query = new Query(Criteria.where(CLIENT_PM_EMAIL)
            .is(email)
            .and(ID)
            .in(ids)
            .and(IS_DELETED)
            .in(Arrays.asList(FALSE, null)));
        return mongoTemplate.find(query, Project.class, PROJECTS);
    }

    public List<ProjectsWithJiraConfigurationVo> findAllProjectsOfUserWithProjects(List<ObjectId> projectIds,
            String email) {
        TypedAggregation<JiraConfiguration> aggregation = newAggregation(JiraConfiguration.class,
                match(Criteria.where(PROJECTID).in(projectIds)), lookup(PROJECTS, PROJECTID, ID, PROJECT),
                unwind(PROJECT), match(Criteria.where(QueryConstants.PROJECT_CLIENT_PM_EMAIL).is(email)),
                new CustomAggregation(QUERY_FOR_FIND_ALL_USER_WITH_ISC_PROJECTS),
                lookup(SPRINTS, JIRA_ID, JIRA_PROJECT_ID, SPRINTS));
        AggregationResults<ProjectsWithJiraConfigurationVo> results = mongoTemplate.aggregate(aggregation,
                ProjectsWithJiraConfigurationVo.class);
        return results.getMappedResults();
    }

    public List<ProjectsWithJiraConfigurationVo> findAllProjectsOfUserWithProjectAndEmail(String projectId,
            String email) {
        TypedAggregation<JiraConfiguration> aggregation = newAggregation(JiraConfiguration.class,
                match(Criteria.where(PROJECTID).is(new ObjectId(projectId))), lookup(PROJECTS, PROJECTID, ID, PROJECT),
                unwind(PROJECT), match(Criteria.where(QueryConstants.PROJECT_CLIENT_PM_EMAIL).is(email)),
                new CustomAggregation(QUERY_FOR_FIND_ALL_USER_WITH_ISC_PROJECTS),
                lookup(SPRINTS, JIRA_ID, JIRA_PROJECT_ID, SPRINTS));
        AggregationResults<ProjectsWithJiraConfigurationVo> results = mongoTemplate.aggregate(aggregation,
                ProjectsWithJiraConfigurationVo.class);
        return results.getMappedResults();
    }

    public List<ProjectsWithJiraConfigurationVo> findAllProjectsOfUser(String email) {
        TypedAggregation<Project> aggregation = newAggregation(Project.class,
                match(Criteria.where(CLIENT_PM_EMAIL).is(email).and(IS_DELETED).in(Arrays.asList(FALSE, null))),
                lookup(JIRA_CONFIGURATION, ID, PROJECTID, JIRACONFIGURATION), unwind(JIRACONFIGURATION),
                new CustomAggregation(QUERY_FOR_FIND_ALL_PROJECTS_OF_USER),
                lookup(SPRINTS, JIRA_ID, JIRA_PROJECT_ID, SPRINTS));
        AggregationResults<ProjectsWithJiraConfigurationVo> results = mongoTemplate.aggregate(aggregation,
                ProjectsWithJiraConfigurationVo.class);
        return results.getMappedResults();
    }

    public List<ProjectsWithJiraConfigurationVo> findAllProjectsOfLoggedInUser(String email) {
        TypedAggregation<Project> aggregation = newAggregation(Project.class,
                match(Criteria.where(CLIENT_PM_EMAIL).is(email).and(IS_DELETED).in(Arrays.asList(FALSE, null))),
                lookup(JIRA_CONFIGURATION, ID, PROJECTID, JIRACONFIGURATION), unwind(JIRACONFIGURATION),
                new CustomAggregation(QUERY_FOR_FIND_ALL_PROJECTS_OF_USER));
        AggregationResults<ProjectsWithJiraConfigurationVo> results = mongoTemplate.aggregate(aggregation,
                ProjectsWithJiraConfigurationVo.class);
        return results.getMappedResults();
    }

    public List<ProjectsWithJiraConfigurationVo> findAllProjectsOfUserBasedOnProjectId(List<ObjectId> projectIds,
            String email) {
        TypedAggregation<JiraConfiguration> aggregation = newAggregation(JiraConfiguration.class,
                match(Criteria.where(PROJECTID).in(projectIds)), lookup(PROJECTS, PROJECTID, ID, PROJECT),
                unwind(PROJECT), match(Criteria.where(QueryConstants.PROJECT_CLIENT_PM_EMAIL).is(email)),
                new CustomAggregation(QUERY_FOR_FIND_ALL_USER_WITH_ISC_PROJECTS));
        AggregationResults<ProjectsWithJiraConfigurationVo> results = mongoTemplate.aggregate(aggregation,
                ProjectsWithJiraConfigurationVo.class);
        return results.getMappedResults();
    }

    public List<Project> findAllProjectsOfClient(String email) {
        Criteria criteria = new Criteria();
        criteria
            .orOperator(Criteria.where(PM_EMAIL).is(email), Criteria.where(DM_EMAIL).is(email),
                    Criteria.where(AM_EMAIL).is(email), Criteria.where(CLIENT_PM_EMAIL).is(email))
            .and(IS_DELETED)
            .in(Arrays.asList(FALSE, null));
        Query query = new Query(criteria);
        return mongoTemplate.find(query, Project.class, PROJECTS);
    }

}
