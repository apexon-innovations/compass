package com.apexon.compass.clientdashboardservice.dao;

import static com.apexon.compass.constants.ClientDashboardRouteConstants.PROJECT_ID;
import static com.apexon.compass.constants.ClientDashboardRouteConstants.SPRINT_ID;
import static com.apexon.compass.constants.ClientDashboardServiceConstants.*;
import static com.apexon.compass.constants.ClientDashboardServiceConstants.ACTIVE;
import static com.apexon.compass.constants.ClientDashboardServiceConstants.BLOCKER;
import static com.apexon.compass.constants.ClientDashboardServiceConstants.BUG_TYPES;
import static com.apexon.compass.constants.ClientDashboardServiceConstants.COMPLETED_TASKS;
import static com.apexon.compass.constants.ClientDashboardServiceConstants.CRITICAL;
import static com.apexon.compass.constants.ClientDashboardServiceConstants.DATA;
import static com.apexon.compass.constants.ClientDashboardServiceConstants.DATE;
import static com.apexon.compass.constants.ClientDashboardServiceConstants.ID;
import static com.apexon.compass.constants.ClientDashboardServiceConstants.IS_DELETED;
import static com.apexon.compass.constants.ClientDashboardServiceConstants.PROJECT_NAME;
import static com.apexon.compass.constants.ClientDashboardServiceConstants.REMAINING_EFFORTS;
import static com.apexon.compass.constants.ClientDashboardServiceConstants.REMAINING_TASKS;
import static com.apexon.compass.constants.ClientDashboardServiceConstants.STATE;
import static com.apexon.compass.constants.ClientDashboardServiceConstants.TOTAL;
import static com.apexon.compass.constants.EntitiesConstants.DERIVED_SPRINTS;
import static com.apexon.compass.constants.EntitiesConstants.PROJECTS;
import static com.apexon.compass.constants.EntitiesConstants.JIRA_CONFIGURATION;
import static com.apexon.compass.constants.EntitiesConstants.JIRA_RULES;
import static com.apexon.compass.constants.EntitiesConstants.MONTH;
import static com.apexon.compass.constants.EntitiesConstants.SCM_CODE_STATISTICAL_ANALYSIS;
import static com.apexon.compass.constants.EntitiesConstants.SCM_CODE_STATISTICAL_ANALYSIS_MONTH;
import static com.apexon.compass.constants.EntitiesConstants.SPRINTS;
import static com.apexon.compass.constants.EntitiesConstants.STATUS_DATA;
import static com.apexon.compass.constants.EntitiesConstants.STORIES;
import static com.apexon.compass.constants.PsrServiceConstants.*;
import static com.apexon.compass.constants.QueryConstants.DERIVED_STORIES;
import static com.apexon.compass.constants.QueryConstants.MATCH_DOLLAR;
import static com.apexon.compass.constants.StrategyServiceConstants.ADD;
import static com.apexon.compass.constants.StrategyServiceConstants.ADDED_LINE_OF_CODE;
import static com.apexon.compass.constants.StrategyServiceConstants.AND;
import static com.apexon.compass.constants.StrategyServiceConstants.AS;
import static com.apexon.compass.constants.StrategyServiceConstants.BRANCHES;
import static com.apexon.compass.constants.StrategyServiceConstants.CASE;
import static com.apexon.compass.constants.StrategyServiceConstants.CODE_CHURN;
import static com.apexon.compass.constants.StrategyServiceConstants.COMPLEXITY;
import static com.apexon.compass.constants.StrategyServiceConstants.COND;
import static com.apexon.compass.constants.StrategyServiceConstants.CONGNITIVE_COMPLEXITY;
import static com.apexon.compass.constants.StrategyServiceConstants.CREATED_DATE;
import static com.apexon.compass.constants.StrategyServiceConstants.DEFAULT;
import static com.apexon.compass.constants.StrategyServiceConstants.DOLLAR_SIGN;
import static com.apexon.compass.constants.StrategyServiceConstants.DUPLICATION;
import static com.apexon.compass.constants.StrategyServiceConstants.EFFICIENCY;
import static com.apexon.compass.constants.StrategyServiceConstants.EQUAL;
import static com.apexon.compass.constants.StrategyServiceConstants.EXPRESSION;
import static com.apexon.compass.constants.StrategyServiceConstants.FACET;
import static com.apexon.compass.constants.StrategyServiceConstants.FROM;
import static com.apexon.compass.constants.StrategyServiceConstants.GROUP;
import static com.apexon.compass.constants.StrategyServiceConstants.GTE;
import static com.apexon.compass.constants.StrategyServiceConstants.IN;
import static com.apexon.compass.constants.StrategyServiceConstants.ISSUES;
import static com.apexon.compass.constants.StrategyServiceConstants.IS_ARCHIVE;
import static com.apexon.compass.constants.StrategyServiceConstants.LEGACY_REFACTOR;
import static com.apexon.compass.constants.StrategyServiceConstants.LET;
import static com.apexon.compass.constants.StrategyServiceConstants.LIMIT;
import static com.apexon.compass.constants.StrategyServiceConstants.LOOKUP;
import static com.apexon.compass.constants.StrategyServiceConstants.LTE;
import static com.apexon.compass.constants.StrategyServiceConstants.MAINTAINABILITY;
import static com.apexon.compass.constants.StrategyServiceConstants.MAJOR;
import static com.apexon.compass.constants.StrategyServiceConstants.MATCH;
import static com.apexon.compass.constants.StrategyServiceConstants.MINOR;
import static com.apexon.compass.constants.StrategyServiceConstants.MULTIPLY;
import static com.apexon.compass.constants.StrategyServiceConstants.NAME;
import static com.apexon.compass.constants.StrategyServiceConstants.NE;
import static com.apexon.compass.constants.StrategyServiceConstants.OR;
import static com.apexon.compass.constants.StrategyServiceConstants.PIPELINE;
import static com.apexon.compass.constants.StrategyServiceConstants.PROJECT;
import static com.apexon.compass.constants.StrategyServiceConstants.RATINGS_MAINTAINABILITY;
import static com.apexon.compass.constants.StrategyServiceConstants.RATINGS_RELIABILITY;
import static com.apexon.compass.constants.StrategyServiceConstants.RATINGS_SECURITY;
import static com.apexon.compass.constants.StrategyServiceConstants.RELIABILITY;
import static com.apexon.compass.constants.StrategyServiceConstants.REMOVED_LINE_OF_CODE;
import static com.apexon.compass.constants.StrategyServiceConstants.REPO_ID;
import static com.apexon.compass.constants.StrategyServiceConstants.ROBUSTNESS;
import static com.apexon.compass.constants.StrategyServiceConstants.SECURITY;
import static com.apexon.compass.constants.StrategyServiceConstants.SIZE;
import static com.apexon.compass.constants.StrategyServiceConstants.SONAR_PROJECT_ID;
import static com.apexon.compass.constants.StrategyServiceConstants.SORT;
import static com.apexon.compass.constants.StrategyServiceConstants.START_DATE;
import static com.apexon.compass.constants.StrategyServiceConstants.SUM;
import static com.apexon.compass.constants.StrategyServiceConstants.SWITCH;
import static com.apexon.compass.constants.StrategyServiceConstants.THEN;
import static com.apexon.compass.constants.StrategyServiceConstants.UNWIND;
import static com.apexon.compass.constants.StrategyServiceConstants.VIOLATIONS;
import static com.apexon.compass.constants.StrategyServiceConstants.VULNERABILITIES;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.group;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.match;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.newAggregation;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.sort;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.unwind;

import com.apexon.compass.constants.ClientDashboardServiceConstants;
import com.apexon.compass.constants.QueryConstants;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.apexon.compass.aggregation.vo.ClientCodeMetricsVo;
import com.apexon.compass.aggregation.vo.CodeScoreVo;
import com.apexon.compass.aggregation.vo.CodeSnapshotVo;
import com.apexon.compass.aggregation.vo.CodeTechDebtVo;
import com.apexon.compass.aggregation.vo.CodeViolationsVo;
import com.apexon.compass.aggregation.vo.ConsolidatedDefectRatioVo;
import com.apexon.compass.aggregation.vo.ConsolidatedDefectSanpshotVo;
import com.apexon.compass.aggregation.vo.ProductCompletionTrendsVo;
import com.apexon.compass.aggregation.vo.ProductHealthOverviewVo;
import com.apexon.compass.aggregation.vo.StoryPointDefectRatioVo;
import com.apexon.compass.aggregation.vo.StoryPointsDeliveryVo;
import com.apexon.compass.aggregation.vo.StoryPointsVo;
import com.apexon.compass.aggregation.vo.VelocityTrendsVo;
import com.apexon.compass.clientdashboardservice.serviceImpl.helper.JiraRulesHelper;
import com.apexon.compass.clientdashboardservice.utils.ClientDashboardServiceUtils;
import com.apexon.compass.constants.EntitiesConstants;
import com.apexon.compass.entities.DefectTrendsType;
import com.apexon.compass.entities.SprintChart;
import com.apexon.compass.exception.custom.RecordNotFoundException;
import com.apexon.compass.exception.custom.ServiceException;
import com.mongodb.MongoException;
import com.mongodb.client.MongoCollection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bson.BsonNull;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.aggregation.TypedAggregation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

@Component
@AllArgsConstructor(onConstructor = @__({ @Autowired }))
@Slf4j
public class ClientDao {

    private final MongoTemplate mongoTemplate;

    private JiraRulesHelper jiraRulesHelper;

    public List<VelocityTrendsVo> getVelocityTrends(List<String> projectIds, Integer sprintCount) {
        List<ObjectId> projectIdsObjectId = projectIds.stream().map(ObjectId::new).toList();
        List<? extends Bson> pipeline = Arrays
            .asList(new Document().append(MATCH,
                    new Document().append(PROJECT_ID, new Document().append(IN, projectIdsObjectId))),
                    new Document().append(PROJECT,
                            new Document().append(PROJECT_ID, 1.0)
                                .append(JIRAPROJECTID, new Document().append(TO_INT_DOLLAR, JIRA_PROJECT_ID_DOLLAR))
                                .append(ID, 0.0)),
                    new Document().append(LOOKUP, new Document().append(FROM, SPRINTS)
                        .append(LET,
                                new Document().append(PROJECT_ID, PROJECT_ID_DOLLAR)
                                    .append(JIRA_PROJECT_ID, JIRA_PROJECT_ID_DOLLAR))
                        .append(PIPELINE, Collections.singletonList(new Document().append(MATCH,
                                new Document().append(EXPRESSION, new Document().append(AND, Arrays.asList(
                                        new Document().append(EQUAL,
                                                Arrays.asList(PROJECT_ID_DOLLAR,
                                                        QueryConstants.PROJECT_ID_DOUBLE_DOLLAR)),
                                        new Document().append(EQUAL,
                                                Arrays.asList(JIRA_PROJECT_ID_DOLLAR, JIRA_PROJECT_ID_DOUBLE_DOLLAR)),
                                        new Document().append(NE, Arrays.asList(STATE_DOLLAR_SIGN, ACTIVE))))))))
                        .append(AS, SPRINT_DATA)),
                    new Document().append(UNWIND, SPRINT_DATA_DOLLAR),
                    new Document().append(GROUP,
                            new Document()
                                .append(ID,
                                        new Document().append(PROJECT_ID, PROJECT_ID_DOLLAR)
                                            .append(JIRAPROJECTID, JIRA_PROJECT_ID_DOLLAR)
                                            .append(SPRINT_ID, QueryConstants.SPRINT_DATA_SPRINT_ID))
                                .append(SPRINT_ID, new Document().append(FIRST, QueryConstants.SPRINT_DATA_SPRINT_ID))
                                .append(NAME, new Document().append(FIRST, NAME_SPRINT_DATA_DOLLAR))
                                .append(START_DATE, new Document().append(FIRST, START_DATE_SPRINT_DATA_DOLLAR))),
                    new Document().append(SORT, new Document().append(START_DATE, -1.0)),
                    new Document().append(GROUP,
                            new Document()
                                .append(ID,
                                        new Document().append(PROJECT_ID, QueryConstants.ID_ISC_PROJECT_ID)
                                            .append(JIRAPROJECTID, QueryConstants.ID_JIRA_PROJECT_ID))
                                .append(SPRINT_IDS,
                                        new Document().append(PUSH_DOLLAR,
                                                new Document().append(SPRINT_ID, SPRINT_ID_DOLLAR_SIGN)
                                                    .append(NAME, NAME_DOLLAR)))),
                    new Document().append(
                            PROJECT,
                            new Document()
                                .append(ID, 1.0)
                                .append("sprintDetails",
                                        new Document().append(SLICE_DOLLAR,
                                                Arrays.asList(SPRINT_IDS_DOLLAR, sprintCount)))),
                    new Document().append(UNWIND, "$sprintDetails"),
                    new Document()
                        .append(LOOKUP, new Document().append(FROM, STORIES)
                            .append(LET, new Document()
                                .append(PROJECT_ID, QueryConstants.ID_ISC_PROJECT_ID)
                                .append(JIRA_PROJECT_ID, QueryConstants.ID_JIRA_PROJECT_ID)
                                .append(SPRINTID, "$sprintDetails.sprintId"))
                            .append(PIPELINE, Arrays.asList(
                                    new Document().append(MATCH, new Document().append("$or", Arrays.asList(
                                            new Document().append(
                                                    EXPRESSION,
                                                    new Document().append(AND, Arrays.asList(
                                                            new Document().append(EQUAL,
                                                                    Arrays.asList(PROJECT_ID_DOLLAR,
                                                                            QueryConstants.PROJECT_ID_DOUBLE_DOLLAR)),
                                                            new Document().append(
                                                                    EQUAL,
                                                                    Arrays.asList(JIRA_PROJECT_ID_DOLLAR,
                                                                            JIRA_PROJECT_ID_DOUBLE_DOLLAR)),
                                                            new Document()
                                                                .append(EQUAL,
                                                                        Arrays.asList(SPRINT_ID_DOLLAR_SIGN,
                                                                                SPRINT_ID_DOUBLE_DOLLAR_SIGN))))),
                                            new Document()
                                                .append(EXPRESSION, new Document().append(AND, Arrays.asList(
                                                        new Document().append(
                                                                EQUAL,
                                                                Arrays.asList(PROJECT_ID_DOLLAR,
                                                                        QueryConstants.PROJECT_ID_DOUBLE_DOLLAR)),
                                                        new Document().append(EQUAL,
                                                                Arrays.asList(JIRA_PROJECT_ID_DOLLAR,
                                                                        JIRA_PROJECT_ID_DOUBLE_DOLLAR)),
                                                        new Document().append(IN,
                                                                Arrays.asList(
                                                                        new Document().append(TO_STRING_DOLLAR,
                                                                                SPRINT_ID_DOUBLE_DOLLAR_SIGN),
                                                                        "$sprintJourney")))))))),
                                    new Document().append(GROUP,
                                            new Document().append(ID, 1.0)
                                                .append("plannedPoints", new Document().append(SUM, ESTIMATE_DOLLAR))
                                                .append(COMPLETED_POINTS, new Document().append(SUM,
                                                        new Document().append(COND, Arrays.asList(
                                                                new Document().append(IN,
                                                                        Arrays.asList(STATE_DOLLAR_SIGN,
                                                                                jiraRulesHelper.getCompletedByProjectId(
                                                                                        projectIdsObjectId))),
                                                                ESTIMATE_DOLLAR, 0.0))))
                                                .append("plannedCounts", new Document().append(SUM, 1.0))
                                                .append(COMPLETED_COUNTS, new Document().append(SUM,
                                                        new Document().append(COND, Arrays.asList(
                                                                new Document().append(IN,
                                                                        Arrays.asList(STATE_DOLLAR_SIGN,
                                                                                jiraRulesHelper.getCompletedByProjectId(
                                                                                        projectIdsObjectId))),
                                                                1.0, 0.0)))))))
                            .append(AS, DATA)),
                    new Document().append(GROUP,
                            new Document()
                                .append(ID,
                                        new Document().append(PROJECT_ID, QueryConstants.ID_ISC_PROJECT_ID)
                                            .append(JIRAPROJECTID, QueryConstants.ID_JIRA_PROJECT_ID))
                                .append(DATA, new Document().append(PUSH_DOLLAR, ROOT_DOUBLE_DOLLAR))));

        try {
            return getDocuments(JIRA_CONFIGURATION, pipeline, VelocityTrendsVo.class);
        }
        catch (MongoException e) {
            log.error(e.getMessage());
            throw new ServiceException(MONGO_EXCEPTION_MESSAGE);
        }
    }

    public DefectTrendsType getDefectTrendsType(Long date, ObjectId projectId) {
        List<? extends Bson> pipeline = Arrays.asList(
                new Document(MATCH_DOLLAR,
                        new Document(PROJECT_ID_CAPITALIZED, projectId).append(DATE, new Document(LTE_DOLLAR, date))),
                new Document(SORT_DOLLAR, new Document(DATE, -1L)), new Document(LIMIT_DOLLAR, 1L));
        try {
            return getDocuments(DERIVED_STORIES, pipeline, DefectTrendsType.class).get(0);
        }
        catch (MongoException e) {
            log.error(e.getMessage());
            throw new ServiceException(MONGO_EXCEPTION_MESSAGE);
        }
    }

    private <T> List<T> getDocuments(String collection, List<? extends Bson> pipeline, Class<T> clazz) {
        return mongoTemplate.getCollection(collection)
            .aggregate(pipeline)
            .map(document -> mongoTemplate.getConverter().read(clazz, document))
            .into(new ArrayList<>());
    }

    public List<ConsolidatedDefectRatioVo> getConsolidatedDefectRatio(List<String> projectIds, Integer sprintCount) {

        List<? extends Bson> pipeline = Arrays.asList(
                new Document().append(MATCH,
                        new Document().append(ClientDashboardServiceConstants.PROJECT_ID,
                                new Document().append(IN, projectIds.stream().map(ObjectId::new).toList()))),
                new Document().append(PROJECT,
                        new Document().append(ClientDashboardServiceConstants.PROJECT_ID, 1.0)
                            .append(JIRAPROJECTID, new Document().append(TO_INT_DOLLAR, JIRA_PROJECT_ID_DOLLAR))
                            .append(ID, 0.0)),
                new Document()
                    .append(LOOKUP, new Document().append(FROM, SPRINTS)
                        .append(LET,
                                new Document().append(ClientDashboardServiceConstants.PROJECT_ID, PROJECT_ID_DOLLAR)
                                    .append(JIRA_PROJECT_ID, JIRA_PROJECT_ID_DOLLAR))
                        .append(PIPELINE, Collections.singletonList(new Document().append(MATCH,
                                new Document().append(EXPRESSION, new Document().append(AND, Arrays.asList(
                                        new Document().append(EQUAL,
                                                Arrays.asList(PROJECT_ID_DOLLAR,
                                                        QueryConstants.PROJECT_ID_DOUBLE_DOLLAR)),
                                        new Document().append(EQUAL,
                                                Arrays.asList(JIRA_PROJECT_ID_DOLLAR, JIRA_PROJECT_ID_DOUBLE_DOLLAR)),
                                        new Document().append(NE, Arrays.asList(STATE_DOLLAR_SIGN, ACTIVE))))))))
                        .append(AS, SPRINT_DATA)),
                new Document().append(UNWIND, SPRINT_DATA_DOLLAR),
                new Document().append(GROUP,
                        new Document()
                            .append(ID,
                                    new Document().append(ClientDashboardServiceConstants.PROJECT_ID, PROJECT_ID_DOLLAR)
                                        .append(JIRAPROJECTID, JIRA_PROJECT_ID_DOLLAR)
                                        .append(SPRINT_ID, QueryConstants.SPRINT_DATA_SPRINT_ID))
                            .append(SPRINT_ID, new Document().append(FIRST, QueryConstants.SPRINT_DATA_SPRINT_ID))
                            .append(NAME, new Document().append(FIRST, NAME_SPRINT_DATA_DOLLAR))
                            .append(START_DATE, new Document().append(FIRST, START_DATE_SPRINT_DATA_DOLLAR))),
                new Document()
                    .append(LOOKUP,
                            new Document().append(FROM, "jira_rules")
                                .append(LET, new Document().append(
                                        ClientDashboardServiceConstants.PROJECT_ID, QueryConstants.ID_ISC_PROJECT_ID)
                                    .append(JIRA_PROJECT_ID, QueryConstants.ID_JIRA_PROJECT_ID))
                                .append(PIPELINE, Arrays.asList(
                                        new Document().append(MATCH, new Document().append(
                                                EXPRESSION,
                                                new Document().append(AND, Arrays.asList(
                                                        new Document().append(EQUAL,
                                                                Arrays.asList(JIRA_PROJECT_ID_DOLLAR,
                                                                        JIRA_PROJECT_ID_DOUBLE_DOLLAR)),
                                                        new Document().append(EQUAL,
                                                                Arrays.asList(PROJECT_ID_DOLLAR,
                                                                        QueryConstants.PROJECT_ID_DOUBLE_DOLLAR)))))),
                                        new Document().append(PROJECT,
                                                new Document().append(BUG_TYPES, 1.0)
                                                    .append(DEFINITION_OF_DONE_ONLY, 1.0)
                                                    .append(ClientDashboardServiceConstants.DEFINITION_OF_ACCEPTED, 1.0)
                                                    .append(ID, 0.0))))
                                .append(AS, "jiraRules")),
                new Document().append(UNWIND, "$jiraRules"),
                new Document().append(SORT, new Document().append(START_DATE, -1.0)),
                new Document().append(GROUP,
                        new Document()
                            .append(ID,
                                    new Document()
                                        .append(ClientDashboardServiceConstants.PROJECT_ID,
                                                QueryConstants.ID_ISC_PROJECT_ID)
                                        .append(JIRA_PROJECT_ID, QueryConstants.ID_JIRA_PROJECT_ID))
                            .append(SPRINT_IDS, new Document().append(PUSH_DOLLAR, SPRINT_ID_DOLLAR_SIGN))
                            .append(BUG_TYPES, new Document().append(FIRST, "$jiraRules.bugTypes"))
                            .append(DEFINITION_OF_DONE_ONLY,
                                    new Document().append(FIRST, "$jiraRules.definitionOfDone"))
                            .append(ClientDashboardServiceConstants.DEFINITION_OF_ACCEPTED,
                                    new Document().append(FIRST, "$jiraRules.definitionOfAccepted"))),
                new Document().append(PROJECT,
                        new Document().append(ID, 1.0)
                            .append(SPRINT_ID,
                                    new Document().append(SLICE_DOLLAR, Arrays.asList(SPRINT_IDS_DOLLAR, sprintCount)))
                            .append(BUG_TYPES, 1.0)
                            .append(DEFINITION_OF_DONE_ONLY, 1.0)
                            .append(ClientDashboardServiceConstants.DEFINITION_OF_ACCEPTED, 1.0)),
                new Document().append(UNWIND, SPRINT_ID_DOLLAR_SIGN),
                new Document().append(LOOKUP, new Document().append(FROM, STORIES)
                    .append(LET,
                            new Document().append(JIRA_PROJECT_ID, ID_DOT_JIRA_PROJECTID)
                                .append(ClientDashboardServiceConstants.PROJECT_ID, QueryConstants.ID_ISC_PROJECT_ID)
                                .append(SPRINTID, SPRINT_ID_DOLLAR_SIGN)
                                .append(BUG__TYPES, BUG_TYPES_DOLLAR_SIGN)
                                .append("definition_of_done", ClientDashboardServiceConstants.DEFINITION_OF_DONE)
                                .append("definition_of_accepted", QueryConstants.DEFINATION_OF_ACCEPTED_DOLLAR))
                    .append(PIPELINE, Arrays.asList(new Document().append(MATCH, new Document()
                        .append(EXPRESSION,
                                new Document().append(AND, Arrays.asList(new Document().append(EQUAL,
                                        Arrays.asList(JIRA_PROJECT_ID_DOLLAR, JIRA_PROJECT_ID_DOUBLE_DOLLAR)),
                                        new Document().append(EQUAL,
                                                Arrays.asList(PROJECT_ID_DOLLAR, PROJECT_ID_DOUBLE_DOLLAR)),
                                        new Document().append(EQUAL,
                                                Arrays.asList(SPRINT_ID_DOLLAR_SIGN, SPRINT_ID_DOUBLE_DOLLAR_SIGN)))))),
                            new Document().append(GROUP,
                                    new Document().append(ID, 1.0)
                                        .append("delivered", new Document().append(SUM, new Document().append(COND,
                                                Arrays.asList(
                                                        new Document().append("$or",
                                                                Arrays.asList(
                                                                        new Document().append(IN,
                                                                                Arrays.asList(STATE_DOLLAR_SIGN,
                                                                                        "$$definition_of_done")),
                                                                        new Document().append(IN,
                                                                                Arrays.asList(STATE_DOLLAR_SIGN,
                                                                                        "$$definition_of_accepted")))),
                                                        ESTIMATE_DOLLAR, 0.0))))
                                        .append("opened",
                                                new Document().append(SUM,
                                                        new Document().append(COND,
                                                                Arrays.asList(
                                                                        new Document().append(IN,
                                                                                Arrays.asList(TYPE_DOLLAR_SIGN,
                                                                                        BUG_TYPE_DOUBLE_DOLLAR_SIGN)),
                                                                        1.0, 0.0)))))))
                    .append(AS, DATA)));
        try {
            return getDocuments(JIRA_CONFIGURATION, pipeline, ConsolidatedDefectRatioVo.class);
        }
        catch (MongoException e) {
            log.error(e.getMessage());
            throw new ServiceException(MONGO_EXCEPTION_MESSAGE);
        }
    }

    private List<Document> getDocumentsFromJiraRules(List<? extends Bson> pipeline) {
        List<Document> documents = mongoTemplate.getCollection(JIRA_RULES).aggregate(pipeline).into(new ArrayList<>());
        if (CollectionUtils.isEmpty(documents)) {
            throw new RecordNotFoundException("No record found for this project");
        }
        return documents;
    }

    private List<Document> getDocumentsFromProjects(List<? extends Bson> pipeline) {
        return mongoTemplate.getCollection(PROJECTS).aggregate(pipeline).into(new ArrayList<>());
    }

    private List<Document> getDocumentsFromDerivedSprints(List<? extends Bson> pipeline) {
        List<Document> documents = mongoTemplate.getCollection(DERIVED_SPRINTS)
            .aggregate(pipeline)
            .into(new ArrayList<>());
        if (CollectionUtils.isEmpty(documents)) {
            throw new RecordNotFoundException("No record found for this project");
        }
        return documents;
    }

    public List<StoryPointsDeliveryVo> getStoryPointsDeliveryTrendForProject(String projectId, Integer sprintId) {
        TypedAggregation<SprintChart> aggregation = newAggregation(SprintChart.class,
                match(Criteria.where(PROJECT_ID).is(new ObjectId(projectId)).and(SPRINT_ID).is(sprintId)),
                unwind(STATUS_DATA),
                group(STATUS_DATA + FULL_STOP + DATE).first(JIRA_PROJECT_ID)
                    .as(JIRA_PROJECT_ID)
                    .first(PROJECT_ID)
                    .as(PROJECT_ID)
                    .first(SPRINT_ID)
                    .as(SPRINT_ID)
                    .first(NAME)
                    .as(NAME)
                    .first(START_DATE)
                    .as(START_DATE)
                    .first(END_DATE)
                    .as(END_DATE)
                    .sum(TOTAL_EFFORTS)
                    .as(TOTAL_EFFORTS)
                    .sum(TOTAL_TASKS)
                    .as(TOTAL_TASKS)
                    .sum(STATUS_DATA + FULL_STOP + REMAINING_EFFORTS)
                    .as(OPEN_TILL_NOW_POINTS)
                    .sum(STATUS_DATA + FULL_STOP + COMPLETED_EFFORTS)
                    .as(COMPLETED_POINTS)
                    .sum(STATUS_DATA + FULL_STOP + NEWLY_ADDED_EFFORTS)
                    .as(NEWLY_ADDED_POINTS)
                    .sum(STATUS_DATA + FULL_STOP + REOPEN_EFFORTS)
                    .as(REOPEN_POINTS)
                    .sum(STATUS_DATA + FULL_STOP + REMAINING_TASKS)
                    .as(OPEN_TILL_NOW_COUNTS)
                    .sum(STATUS_DATA + FULL_STOP + COMPLETED_TASKS)
                    .as(COMPLETED_COUNTS)
                    .sum(STATUS_DATA + FULL_STOP + NEWLY_ADDED_TASKS)
                    .as(NEWLY_ADDED_COUNTS)
                    .sum(STATUS_DATA + FULL_STOP + REOPEN_TASKS)
                    .as(REOPEN_COUNTS),
                sort(Sort.by(ID)));
        AggregationResults<StoryPointsDeliveryVo> results = mongoTemplate.aggregate(aggregation,
                StoryPointsDeliveryVo.class);
        return results.getMappedResults();
    }

    public List<StoryPointsVo> getStoryPointsDelivered(List<String> projectIdList,
            HashMap<String, Integer> sprintIdList) {

        List<? extends Bson> pipeline = Arrays.asList(
                new Document().append(MATCH,
                        new Document().append("$or", getDocumentList(projectIdList, sprintIdList))),
                new Document().append(UNWIND, QueryConstants.DOLLAR_STATUS_DATA),
                new Document().append(GROUP,
                        new Document()
                            .append(ID,
                                    new Document().append(PROJECT_ID, PROJECT_ID_DOLLAR)
                                        .append(JIRA_PROJECT_ID, JIRA_PROJECT_ID_DOLLAR)
                                        .append("day", "$statusData.date"))
                            .append(PROJECT_ID, new Document().append(FIRST, PROJECT_ID_DOLLAR))
                            .append(JIRA_PROJECT_ID, new Document().append(FIRST, JIRA_PROJECT_ID_DOLLAR))
                            .append(SPRINT_ID, new Document().append(FIRST, SPRINT_ID_DOLLAR_SIGN))
                            .append(NAME, new Document().append(FIRST, NAME_DOLLAR))
                            .append(START_DATE, new Document().append(FIRST, QueryConstants.START_DATE_DOLLAR))
                            .append(END_DATE, new Document().append(FIRST, QueryConstants.DOLLAR_END_DATE))
                            .append("totalEfforts", new Document().append(SUM, "$totalEfforts"))
                            .append("totalTasks", new Document().append(SUM, "$totalTasks"))
                            .append("openTillNowPoints", new Document().append(SUM, "$statusData.remainingEfforts"))
                            .append(COMPLETED_POINTS, new Document().append(SUM, "$statusData.completedEfforts"))
                            .append("newlyAddedPoints", new Document().append(SUM, "$statusData.newlyAddedEfforts"))
                            .append("reopenPoints", new Document().append(SUM, "$statusData.reopenEfforts"))
                            .append("openTillNowCounts", new Document().append(SUM, "$statusData.remainingTasks"))
                            .append(COMPLETED_COUNTS, new Document().append(SUM, "$statusData.completedTasks"))
                            .append("newlyAddedCounts", new Document().append(SUM, "$statusData.newlyAddedTasks"))
                            .append("reopenCounts", new Document().append(SUM, "$statusData.reopenTasks"))),
                new Document().append(SORT, new Document().append("_id.day", 1.0)),
                new Document().append(GROUP,
                        new Document()
                            .append(ID,
                                    new Document()
                                        .append(PROJECT_ID,
                                                new Document().append(TO_STRING_DOLLAR,
                                                        QueryConstants.ID_ISC_PROJECT_ID))
                                        .append(JIRA_PROJECT_ID, ID_DOT_JIRA_PROJECTID))
                            .append(PROJECT_ID,
                                    new Document().append(FIRST,
                                            new Document().append(TO_STRING_DOLLAR, PROJECT_ID_DOLLAR)))
                            .append(JIRA_PROJECT_ID, new Document().append(FIRST, JIRA_PROJECT_ID_DOLLAR))
                            .append(SPRINT_ID, new Document().append(FIRST, SPRINT_ID_DOLLAR_SIGN))
                            .append(NAME, new Document().append(FIRST, NAME_DOLLAR))
                            .append(START_DATE, new Document().append(FIRST, QueryConstants.START_DATE_DOLLAR))
                            .append(END_DATE, new Document().append(FIRST, QueryConstants.DOLLAR_END_DATE))
                            .append("totalEfforts", new Document().append(FIRST, "$totalEfforts"))
                            .append("totalTasks", new Document().append(FIRST, "$totalTasks"))
                            .append("statusData",
                                    new Document().append(PUSH_DOLLAR,
                                            new Document().append("date", "$_id.day")
                                                .append("openTillNowPoints", "$openTillNowPoints")
                                                .append(COMPLETED_POINTS, "$completedPoints")
                                                .append("newlyAddedPoints", "$newlyAddedPoints")
                                                .append("reopenPoints", "$reopenPoints")
                                                .append("openTillNowCounts", "$openTillNowCounts")
                                                .append(COMPLETED_COUNTS, "$completedCounts")
                                                .append("newlyAddedCounts", "$newlyAddedCounts")
                                                .append("reopenCounts", "$reopenCounts")))));

        ObjectMapper mapper = new ObjectMapper();
        try {
            mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            return mapper.readValue(
                    ClientDashboardServiceUtils.documentsToJsonConverter(getDocumentsFromDerivedSprints(pipeline)),
                    new TypeReference<>() {
                    });
        }
        catch (JsonProcessingException processingException) {
            log.error(JSON_PROCESSING_LOG_MESSAGE + processingException.getMessage());
            throw new ServiceException(JSON_PROCESSING_EXCEPTION_MESSAGE);
        }
    }

    private List<Document> getDocumentList(List<String> projectIdList, HashMap<String, Integer> sprintIdList) {
        List<Document> documents = new ArrayList<>();
        for (String projectId : projectIdList) {
            documents.add(new Document().append(AND,
                    Arrays.asList(new Document().append(PROJECT_ID, new ObjectId(projectId)), new Document().append(
                            SPRINT_ID,
                            new Document().append(IN, Collections.singletonList(sprintIdList.get(projectId)))))));
        }
        return documents;
    }

    public List<StoryPointDefectRatioVo> getStoryPointDefectRatio(String projectId, Integer sprintCount) {

        List<? extends Bson> pipeline = Arrays
            .asList(new Document().append(MATCH,
                    new Document().append(ClientDashboardServiceConstants.PROJECT_ID, new ObjectId(projectId))),
                    new Document().append(PROJECT,
                            new Document().append(ClientDashboardServiceConstants.PROJECT_ID, 1.0)
                                .append(JIRAPROJECTID, new Document().append(TO_INT_DOLLAR, JIRA_PROJECT_ID_DOLLAR))
                                .append(ID, 0.0)),
                    new Document().append(LOOKUP, new Document().append(FROM, SPRINTS)
                        .append(LET,
                                new Document().append(ClientDashboardServiceConstants.PROJECT_ID, PROJECT_ID_DOLLAR)
                                    .append(JIRAPROJECTID, JIRA_PROJECT_ID_DOLLAR))
                        .append(PIPELINE,
                                Collections.singletonList(new Document().append(MATCH,
                                        new Document().append(EXPRESSION,
                                                new Document().append(AND, Arrays.asList(
                                                        new Document().append(EQUAL,
                                                                Arrays.asList(PROJECT_ID_DOLLAR,
                                                                        PROJECT_ID_DOUBLE_DOLLAR)),
                                                        new Document().append(EQUAL,
                                                                Arrays.asList(JIRA_PROJECT_ID_DOLLAR,
                                                                        JIRA_PROJECT_ID_DOUBLE_DOLLAR)),
                                                        new Document().append("$not",
                                                                new Document().append(IN,
                                                                        Arrays.asList(STATE_DOLLAR_SIGN,
                                                                                Arrays.asList(ACTIVE, "future"))))))))))
                        .append(AS, SPRINT_DATA)),
                    new Document().append(UNWIND, SPRINT_DATA_DOLLAR),
                    new Document()
                        .append(GROUP, new Document()
                            .append(ID,
                                    new Document().append(ClientDashboardServiceConstants.PROJECT_ID, PROJECT_ID_DOLLAR)
                                        .append(JIRAPROJECTID, JIRA_PROJECT_ID_DOLLAR)
                                        .append(SPRINT_ID, QueryConstants.SPRINT_DATA_SPRINT_ID))
                            .append(JIRAPROJECTID, new Document().append(FIRST, "$sprintData.jiraProjectId"))
                            .append(SPRINT_ID, new Document().append(FIRST, QueryConstants.SPRINT_DATA_SPRINT_ID))
                            .append(NAME, new Document().append(FIRST, NAME_SPRINT_DATA_DOLLAR))
                            .append(START_DATE, new Document().append(FIRST, START_DATE_SPRINT_DATA_DOLLAR))),
                    new Document().append(SORT, new Document().append(START_DATE, -1.0)),
                    new Document().append("$limit", sprintCount),
                    new Document()
                        .append(LOOKUP, new Document().append(FROM, "jira_rules")
                            .append(LET,
                                    new Document().append(ClientDashboardServiceConstants.PROJECT_ID,
                                            QueryConstants.ID_ISC_PROJECT_ID)
                                        .append(JIRAPROJECTID, QueryConstants.ID_JIRA_PROJECT_ID))
                            .append(PIPELINE, Arrays.asList(
                                    new Document().append(MATCH,
                                            new Document().append(EXPRESSION,
                                                    new Document().append(AND, Arrays.asList(
                                                            new Document().append(EQUAL,
                                                                    Arrays.asList(PROJECT_ID_DOLLAR,
                                                                            QueryConstants.PROJECT_ID_DOUBLE_DOLLAR)),
                                                            new Document().append(EQUAL,
                                                                    Arrays.asList(JIRA_PROJECT_ID_DOLLAR,
                                                                            JIRA_PROJECT_ID_DOUBLE_DOLLAR)))))),
                                    new Document().append(PROJECT,
                                            new Document().append(BUG_TYPES, 1.0)
                                                .append(DEFINITION_OF_DONE_ONLY, 1.0)
                                                .append(ClientDashboardServiceConstants.DEFINITION_OF_ACCEPTED, 1.0)
                                                .append(ID, 0.0))))
                            .append(AS, "jiraRules")),
                    new Document().append(UNWIND, "$jiraRules"),
                    new Document().append(GROUP,
                            new Document()
                                .append(ID,
                                        new Document()
                                            .append(ClientDashboardServiceConstants.PROJECT_ID,
                                                    QueryConstants.ID_ISC_PROJECT_ID)
                                            .append(JIRAPROJECTID, QueryConstants.ID_JIRA_PROJECT_ID)
                                            .append(SPRINT_ID, SPRINT_ID_DOLLAR_SIGN))
                                .append(SPRINT_ID, new Document().append(FIRST, SPRINT_ID_DOLLAR_SIGN))
                                .append(NAME, new Document().append(FIRST, NAME_DOLLAR))
                                .append(START_DATE, new Document().append(FIRST, QueryConstants.START_DATE_DOLLAR))
                                .append(BUG_TYPES, new Document().append(FIRST, "$jiraRules.bugTypes"))
                                .append(DEFINITION_OF_DONE_ONLY,
                                        new Document().append(FIRST, "$jiraRules.definitionOfDone"))
                                .append(ClientDashboardServiceConstants.DEFINITION_OF_ACCEPTED,
                                        new Document().append(FIRST, "$jiraRules.definitionOfAccepted"))),
                    new Document()
                        .append(LOOKUP, new Document().append(FROM, STORIES)
                            .append(LET, new Document()
                                .append(ClientDashboardServiceConstants.PROJECT_ID, QueryConstants.ID_ISC_PROJECT_ID)
                                .append(JIRAPROJECTID, QueryConstants.ID_JIRA_PROJECT_ID)
                                .append(SPRINTID, "$_id.sprintId")
                                .append(BUG__TYPES, BUG_TYPES_DOLLAR_SIGN)
                                .append("definition_of_done", ClientDashboardServiceConstants.DEFINITION_OF_DONE)
                                .append("definition_of_accepted", QueryConstants.DEFINATION_OF_ACCEPTED_DOLLAR))
                            .append(PIPELINE, Arrays.asList(
                                    new Document().append(MATCH,
                                            new Document().append(EXPRESSION, new Document().append(AND, Arrays.asList(
                                                    new Document().append(EQUAL,
                                                            Arrays
                                                                .asList(SPRINT_ID_DOLLAR_SIGN,
                                                                        SPRINT_ID_DOUBLE_DOLLAR_SIGN)),
                                                    new Document().append(EQUAL,
                                                            Arrays.asList(PROJECT_ID_DOLLAR, "$$projectId")),
                                                    new Document().append(EQUAL,
                                                            Arrays.asList(JIRA_PROJECT_ID_DOLLAR,
                                                                    "$$jiraProjectId")))))),
                                    new Document().append(GROUP, new Document().append(ID, 1.0)
                                        .append("delivered", new Document().append(SUM, new Document().append(COND,
                                                Arrays.asList(
                                                        new Document().append("$or",
                                                                Arrays.asList(
                                                                        new Document().append(IN,
                                                                                Arrays.asList(STATE_DOLLAR_SIGN,
                                                                                        "$$definition_of_done")),
                                                                        new Document().append(IN,
                                                                                Arrays.asList(STATE_DOLLAR_SIGN,
                                                                                        "$$definition_of_accepted")))),
                                                        ESTIMATE_DOLLAR, 0.0))))
                                        .append("opened",
                                                new Document().append(SUM,
                                                        new Document().append(COND,
                                                                Arrays.asList(
                                                                        new Document().append(IN,
                                                                                Arrays.asList(TYPE_DOLLAR_SIGN,
                                                                                        BUG_TYPE_DOUBLE_DOLLAR_SIGN)),
                                                                        1.0, 0.0)))))))
                            .append(AS, DATA)),
                    new Document().append(SORT, new Document().append(START_DATE, -1.0)));
        try {
            return getDocuments(JIRA_CONFIGURATION, pipeline, StoryPointDefectRatioVo.class);
        }
        catch (MongoException e) {
            log.error(e.getMessage());
            throw new ServiceException(MONGO_EXCEPTION_MESSAGE);
        }
    }

    public List<ProductCompletionTrendsVo> getProductCompletionTrends(List<String> projectId, Integer sprintCount) {
        try {
            MongoCollection<Document> collection = mongoTemplate.getCollection(DERIVED_SPRINTS);
            List<? extends Bson> pipeline = Arrays.asList(
                    new Document().append(MATCH,
                            new Document().append(ClientDashboardServiceConstants.PROJECT_ID,
                                    new Document().append(IN, projectId.stream().map(ObjectId::new).toList()))),
                    new Document().append(PROJECT,
                            new Document().append(ClientDashboardServiceConstants.PROJECT_ID, 1.0)
                                .append(JIRAPROJECTID, 1.0)
                                .append("boardId", 1.0)
                                .append(SPRINT_ID, 1.0)
                                .append(NAME, 1.0)
                                .append(START_DATE, 1.0)
                                .append(END_DATE, 1.0)
                                .append("statusData",
                                        new Document().append(SLICE_DOLLAR,
                                                Arrays.asList(QueryConstants.DOLLAR_STATUS_DATA, -1.0)))),
                    new Document().append(UNWIND, QueryConstants.DOLLAR_STATUS_DATA),
                    new Document().append(GROUP, new Document()
                        .append(ID,
                                new Document().append(ClientDashboardServiceConstants.PROJECT_ID, PROJECT_ID_DOLLAR)
                                    .append(JIRAPROJECTID, JIRA_PROJECT_ID_DOLLAR)
                                    .append(SPRINT_ID, SPRINT_ID_DOLLAR_SIGN))
                        .append(SPRINT_ID, new Document().append(FIRST, SPRINT_ID_DOLLAR_SIGN))
                        .append("totalSquads", new Document().append(SUM, 1.0))
                        .append(NAME, new Document().append(FIRST, NAME_DOLLAR))
                        .append(START_DATE, new Document().append(FIRST, QueryConstants.START_DATE_DOLLAR))
                        .append(END_DATE, new Document().append(FIRST, QueryConstants.DOLLAR_END_DATE))
                        .append(COMPLETED_POINTS, new Document().append(SUM, "$statusData.completedEfforts"))
                        .append(COMPLETED_COUNTS, new Document().append(SUM, "$statusData.completedTasks"))),
                    new Document().append(SORT, new Document().append(START_DATE, -1.0)),
                    new Document().append(PROJECT,
                            new Document().append(ID, 0.0)
                                .append(ClientDashboardServiceConstants.PROJECT_ID, QueryConstants.ID_ISC_PROJECT_ID)
                                .append(JIRAPROJECTID, ID_DOT_JIRA_PROJECTID)
                                .append(SPRINT_ID, 1.0)
                                .append("totalSquads", 1.0)
                                .append(NAME, 1.0)
                                .append(COMPLETED_POINTS, 1.0)
                                .append(COMPLETED_COUNTS, 1.0)),
                    new Document().append(GROUP,
                            new Document()
                                .append(ID,
                                        new Document()
                                            .append(ClientDashboardServiceConstants.PROJECT_ID,
                                                    new Document(TO_STRING_DOLLAR, PROJECT_ID_DOLLAR))
                                            .append(JIRAPROJECTID, JIRA_PROJECT_ID_DOLLAR))
                                .append(DATA, new Document().append(PUSH_DOLLAR, ROOT_DOUBLE_DOLLAR))),
                    new Document().append(PROJECT, new Document().append(SPRINT_DATA, new Document().append(
                            "$reverseArray",
                            new Document().append(SLICE_DOLLAR, Arrays.asList(DATA_WITH_DOLLAR_SIGN, sprintCount))))));

            List<Document> documents = collection.aggregate(pipeline).into(new ArrayList<>());
            ObjectMapper mapper = new ObjectMapper();
            mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            return mapper.readValue(ClientDashboardServiceUtils.documentsToJsonConverter(documents),
                    new TypeReference<>() {
                    });
        }
        catch (JsonProcessingException processingException) {
            log.error(JSON_PROCESSING_LOG_MESSAGE + processingException.getMessage());
            throw new ServiceException(JSON_PROCESSING_EXCEPTION_MESSAGE);
        }
        catch (MongoException e) {
            log.error(e.getMessage());
            throw new ServiceException(MONGO_EXCEPTION_MESSAGE);
        }
    }

    public List<ConsolidatedDefectSanpshotVo> getConsolidatedDefectSnapshot(List<String> projectIds) {

        List<? extends Bson> pipeline = Arrays.asList(
                new Document().append(MATCH,
                        new Document().append(ClientDashboardServiceConstants.PROJECT_ID, new Document().append(IN,
                                projectIds.stream().map(ObjectId::new).toList()))),
                new Document().append(PROJECT,
                        new Document().append(ClientDashboardServiceConstants.PROJECT_ID, 1.0)
                            .append(JIRA_PROJECT_ID, 1.0)
                            .append(BUG_TYPES, 1.0)
                            .append("openedStatus",
                                    new Document().append(CONCAT_ARRAYS,
                                            Arrays.asList("$definitionOfTodo", "$definitionOfInProgress",
                                                    "$definitionOfDevComplete", "$blockerDefintion")))
                            .append("definitionOfMajorSeverity", 1.0)
                            .append("definitionOfMinorSeverity", 1.0)
                            .append("definitionOfBlockerSeverity", 1.0)
                            .append("definitionOfCriticalSeverity", 1.0)),
                new Document().append(LOOKUP, new Document().append(FROM, STORIES)
                    .append(LET,
                            new Document().append(ClientDashboardServiceConstants.PROJECT_ID, PROJECT_ID_DOLLAR)
                                .append(JIRA_PROJECT_ID, JIRA_PROJECT_ID_DOLLAR)
                                .append(BUG__TYPES, BUG_TYPES_DOLLAR_SIGN)
                                .append("opened_status", "$openedStatus")
                                .append(CRITICAL, "$definitionOfCriticalSeverity")
                                .append(BLOCKER, "$definitionOfBlockerSeverity")
                                .append(MAJOR, "$definitionOfMajorSeverity")
                                .append(MINOR, "$definitionOfMinorSeverity"))
                    .append(PIPELINE, Arrays.asList(
                            new Document().append(MATCH, new Document().append(EXPRESSION, new Document().append(AND,
                                    Arrays.asList(
                                            new Document().append(EQUAL,
                                                    Arrays.asList(JIRA_PROJECT_ID_DOLLAR,
                                                            JIRA_PROJECT_ID_DOUBLE_DOLLAR)),
                                            new Document()
                                                .append(EQUAL,
                                                        Arrays.asList(PROJECT_ID_DOLLAR,
                                                                QueryConstants.PROJECT_ID_DOUBLE_DOLLAR)),
                                            new Document().append(IN,
                                                    Arrays.asList(TYPE_DOLLAR_SIGN, BUG_TYPE_DOUBLE_DOLLAR_SIGN)))))),
                            new Document().append("$facet", new Document()
                                .append(TOTAL_DEFECTS, Arrays.asList(
                                        new Document().append(MATCH, new Document()
                                            .append(EXPRESSION, new Document().append("$or", Arrays.asList(
                                                    new Document().append(IN,
                                                            Arrays.asList(STATE_DOLLAR_SIGN,
                                                                    OPENED_STATUS_DOUBLE_DOLLAR_SIGN)),
                                                    new Document().append(EQUAL,
                                                            Arrays.asList(SPRINT_ID_DOLLAR_SIGN, new BsonNull())))))),
                                        new Document().append(GROUP,
                                                new Document().append(ID, 1.0)
                                                    .append("total", new Document().append(SUM, 1.0)))))
                                .append("plannedOpen", Arrays.asList(
                                        new Document().append(MATCH,
                                                new Document().append(EXPRESSION,
                                                        new Document().append(AND, Arrays.asList(
                                                                new Document().append(IN,
                                                                        Arrays.asList(STATE_DOLLAR_SIGN,
                                                                                OPENED_STATUS_DOUBLE_DOLLAR_SIGN)),
                                                                new Document().append(NE,
                                                                        Arrays.asList(SPRINT_ID_DOLLAR_SIGN,
                                                                                new BsonNull())))))),
                                        new Document()
                                            .append(GROUP,
                                                    new Document().append(ID, 1.0)
                                                        .append("totalOpened", new Document().append(SUM, 1.0))
                                                        .append(CRITICAL,
                                                                new Document().append(SUM,
                                                                        new Document().append(COND, Arrays.asList(
                                                                                new Document().append(IN,
                                                                                        Arrays.asList(PRIORITY_NAME,
                                                                                                "$$critical")),
                                                                                1.0, 0.0))))
                                                        .append(BLOCKER,
                                                                new Document().append(SUM,
                                                                        new Document().append(COND, Arrays.asList(
                                                                                new Document().append(IN,
                                                                                        Arrays.asList(PRIORITY_NAME,
                                                                                                "$$blocker")),
                                                                                1.0, 0.0))))
                                                        .append(MAJOR,
                                                                new Document().append(SUM,
                                                                        new Document().append(COND, Arrays.asList(
                                                                                new Document().append(IN,
                                                                                        Arrays.asList(PRIORITY_NAME,
                                                                                                "$$major")),
                                                                                1.0, 0.0))))
                                                        .append(MINOR,
                                                                new Document().append(SUM,
                                                                        new Document().append(COND, Arrays.asList(
                                                                                new Document().append(IN,
                                                                                        Arrays.asList(PRIORITY_NAME,
                                                                                                "$$minor")),
                                                                                1.0, 0.0)))))))
                                .append("backlogOpen", Arrays.asList(
                                        new Document().append(MATCH,
                                                new Document().append(EXPRESSION,
                                                        new Document().append(EQUAL,
                                                                Arrays.asList(SPRINT_ID_DOLLAR_SIGN, new BsonNull())))),
                                        new Document()
                                            .append(GROUP,
                                                    new Document().append(ID, 1.0)
                                                        .append("totalOpened", new Document().append(SUM, 1.0))
                                                        .append(CRITICAL,
                                                                new Document().append(SUM,
                                                                        new Document().append(COND, Arrays.asList(
                                                                                new Document().append(IN,
                                                                                        Arrays.asList(PRIORITY_NAME,
                                                                                                "$$critical")),
                                                                                1.0, 0.0))))
                                                        .append(BLOCKER,
                                                                new Document().append(SUM,
                                                                        new Document().append(COND, Arrays.asList(
                                                                                new Document().append(IN,
                                                                                        Arrays.asList(PRIORITY_NAME,
                                                                                                "$$blocker")),
                                                                                1.0, 0.0))))
                                                        .append(MAJOR,
                                                                new Document().append(SUM,
                                                                        new Document().append(COND, Arrays.asList(
                                                                                new Document().append(IN,
                                                                                        Arrays.asList(PRIORITY_NAME,
                                                                                                "$$major")),
                                                                                1.0, 0.0))))
                                                        .append(MINOR,
                                                                new Document().append(SUM,
                                                                        new Document().append(COND, Arrays.asList(
                                                                                new Document().append(IN,
                                                                                        Arrays.asList(PRIORITY_NAME,
                                                                                                "$$minor")),
                                                                                1.0, 0.0))))))))))
                    .append(AS, DATA)),
                new Document().append(UNWIND, DATA_WITH_DOLLAR_SIGN),
                new Document().append(UNWIND, "$data.totalDefects"), new Document().append(UNWIND, "$data.plannedOpen"),
                new Document().append(UNWIND, "$data.backlogOpen"),
                new Document().append(PROJECT,
                        new Document().append(ClientDashboardServiceConstants.PROJECT_ID, 1.0)
                            .append(JIRA_PROJECT_ID, 1.0)
                            .append(TOTAL_DEFECTS, "$data.totalDefects.total")
                            .append("data.plannedOpen", 1.0)
                            .append("data.backlogOpen", 1.0)));

        ObjectMapper mapper = new ObjectMapper();
        try {
            mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            return mapper.readValue(
                    ClientDashboardServiceUtils.documentsToJsonConverter(getDocumentsFromJiraRules(pipeline)),
                    new TypeReference<>() {
                    });
        }
        catch (JsonProcessingException processingException) {
            log.error(JSON_PROCESSING_LOG_MESSAGE + processingException.getMessage());
            throw new ServiceException(JSON_PROCESSING_EXCEPTION_MESSAGE);
        }
    }

    public List<ProductHealthOverviewVo> getProductHealthOverview(List<String> projectIds) {
        List<? extends Bson> pipeline = Arrays.asList(
                new Document().append(MATCH,
                        new Document().append(ClientDashboardServiceConstants.PROJECT_ID,
                                new Document().append(IN, projectIds.stream().map(ObjectId::new).toList()))),
                new Document().append(PROJECT,
                        new Document().append(PROJECT_ID, 1.0)
                            .append(JIRA_PROJECT_ID, 1.0)
                            .append("storyTypes", 1.0)
                            .append(BUG_TYPES, 1.0)
                            .append("openedStatus",
                                    new Document().append(CONCAT_ARRAYS,
                                            Arrays.asList("$definitionOfTodo", "$definitionOfInProgress",
                                                    "$definitionOfDevComplete", "$blockerDefintion")))
                            .append("closedStatus",
                                    new Document().append(CONCAT_ARRAYS,
                                            Arrays.asList(ClientDashboardServiceConstants.DEFINITION_OF_DONE,
                                                    QueryConstants.DEFINATION_OF_ACCEPTED_DOLLAR)))),
                new Document()
                    .append(LOOKUP,
                            new Document().append(FROM, STORIES)
                                .append(LET,
                                        new Document().append(ClientDashboardServiceConstants.PROJECT_ID,
                                                PROJECT_ID_DOLLAR)
                                            .append(JIRA_PROJECT_ID, JIRA_PROJECT_ID_DOLLAR)
                                            .append("story_types", "$storyTypes")
                                            .append("bug_types", BUG_TYPES_DOLLAR_SIGN)
                                            .append("opened_status", "$openedStatus")
                                            .append("closed_status", "$closedStatus"))
                                .append(PIPELINE, Arrays.asList(
                                        new Document().append(MATCH, new Document().append(AND, Arrays.asList(
                                                new Document().append(EXPRESSION,
                                                        new Document().append(EQUAL,
                                                                Arrays.asList(JIRA_PROJECT_ID_DOUBLE_DOLLAR,
                                                                        JIRA_PROJECT_ID_DOUBLE_DOLLAR))),
                                                new Document().append(
                                                        EXPRESSION,
                                                        new Document().append(EQUAL,
                                                                Arrays.asList(PROJECT_ID_DOLLAR,
                                                                        QueryConstants.PROJECT_ID_DOUBLE_DOLLAR))),
                                                new Document().append(EXPRESSION,
                                                        new Document().append("$or", Arrays.asList(
                                                                new Document().append(IN,
                                                                        Arrays.asList(TYPE_DOLLAR_SIGN, "$$bug_types")),
                                                                new Document().append(IN,
                                                                        Arrays.asList(
                                                                                TYPE_DOLLAR_SIGN,
                                                                                "$$story_types")))))))),
                                        new Document().append("$facet", new Document()
                                            .append(STORIES, Arrays.asList(new Document().append(
                                                    MATCH,
                                                    new Document().append(EXPRESSION,
                                                            new Document().append(IN,
                                                                    Arrays.asList(TYPE_DOLLAR_SIGN, "$$story_types")))),
                                                    new Document().append(GROUP, new Document().append(ID, 1.0)
                                                        .append("totalStories", new Document().append(SUM, 1.0))
                                                        .append("completedStories", new Document().append(SUM,
                                                                new Document().append(COND, Arrays
                                                                    .asList(new Document().append(AND, Arrays.asList(
                                                                            new Document().append(IN,
                                                                                    Arrays.asList(STATE_DOLLAR_SIGN,
                                                                                            "$$closed_status")),
                                                                            new Document().append(NE,
                                                                                    Arrays.asList(SPRINT_ID_DOLLAR_SIGN,
                                                                                            new BsonNull())))),
                                                                            1.0, 0.0))))
                                                        .append("inProgressStories", new Document().append(SUM,
                                                                new Document().append(COND, Arrays.asList(new Document()
                                                                    .append(AND, Arrays.asList(new Document().append(IN,
                                                                            Arrays.asList(STATE_DOLLAR_SIGN,
                                                                                    OPENED_STATUS_DOUBLE_DOLLAR_SIGN)),
                                                                            new Document().append(NE,
                                                                                    Arrays.asList(SPRINT_ID_DOLLAR_SIGN,
                                                                                            new BsonNull())))),
                                                                        1.0, 0.0))))
                                                        .append("backlogStories", new Document().append(SUM,
                                                                new Document().append(COND, Arrays.asList(
                                                                        new Document().append(EQUAL,
                                                                                Arrays.asList(SPRINT_ID_DOLLAR_SIGN,
                                                                                        new BsonNull())),
                                                                        1.0, 0.0)))))))
                                            .append("defects", Arrays
                                                .asList(new Document().append(
                                                        MATCH,
                                                        new Document().append(EXPRESSION,
                                                                new Document().append(IN,
                                                                        Arrays.asList(TYPE_DOLLAR_SIGN,
                                                                                "$$bug_types")))),
                                                        new Document().append(GROUP, new Document().append(ID, 1.0)
                                                            .append(TOTAL_DEFECTS, new Document().append(SUM, 1.0))
                                                            .append("resolvedDefects", new Document()
                                                                .append(SUM, new Document().append(COND, Arrays
                                                                    .asList(new Document().append(AND, Arrays.asList(
                                                                            new Document().append(IN,
                                                                                    Arrays.asList(STATE_DOLLAR_SIGN,
                                                                                            "$$closed_status")),
                                                                            new Document().append(NE,
                                                                                    Arrays.asList(SPRINT_ID_DOLLAR_SIGN,
                                                                                            new BsonNull())))),
                                                                            1.0, 0.0))))
                                                            .append("inProgressDefects", new Document()
                                                                .append(SUM, new Document().append(COND, Arrays
                                                                    .asList(new Document().append(AND, Arrays.asList(
                                                                            new Document().append(IN, Arrays.asList(
                                                                                    STATE_DOLLAR_SIGN,
                                                                                    OPENED_STATUS_DOUBLE_DOLLAR_SIGN)),
                                                                            new Document().append(NE,
                                                                                    Arrays.asList(SPRINT_ID_DOLLAR_SIGN,
                                                                                            new BsonNull())))),
                                                                            1.0, 0.0))))
                                                            .append("backlogDefects", new Document().append(SUM,
                                                                    new Document().append(COND, Arrays.asList(
                                                                            new Document().append(EQUAL,
                                                                                    Arrays.asList(SPRINT_ID_DOLLAR_SIGN,
                                                                                            new BsonNull())),
                                                                            1.0, 0.0))))))))))
                                .append(AS, DATA)),
                new Document().append(UNWIND, DATA_WITH_DOLLAR_SIGN), new Document().append(UNWIND, "$data.stories"),
                new Document().append(UNWIND, "$data.defects"),
                new Document().append(PROJECT,
                        new Document().append(PROJECT_ID_CAPITALIZED, 1.0)
                            .append(PROJECT_ID_CAPITALIZED, 1.0)
                            .append(TOTAL_DEFECTS, "$data.totalDefects.total")
                            .append("data.stories", 1.0)
                            .append("data.defects", 1.0)));
        ObjectMapper mapper = new ObjectMapper();
        try {
            mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            return mapper.readValue(
                    ClientDashboardServiceUtils.documentsToJsonConverter(getDocumentsFromJiraRules(pipeline)),
                    new TypeReference<>() {
                    });
        }
        catch (JsonProcessingException processingException) {
            log.error(JSON_PROCESSING_LOG_MESSAGE + processingException.getMessage());
            throw new ServiceException(JSON_PROCESSING_EXCEPTION_MESSAGE);
        }
    }

    public List<CodeScoreVo> getCodeScore(String projectId, boolean isAllRepo, String repoId, Long startDate,
            Long endDate) {
        List<? extends Bson> pipeline = Arrays.asList(
                new Document().append(MATCH,
                        new Document().append(ID, new ObjectId(projectId)).append(IS_DELETED, false)),
                new Document().append(PROJECT, new Document().append(REPOS, 1.0)),
                new Document().append(UNWIND, new Document().append(PATH, QueryConstants.REPOS_DOLLAR)),
                new Document().append(MATCH, new Document().append(OR, Arrays.asList(
                        new Document().append(QueryConstants.REPOS_SONAR_PROJECTID,
                                new Document().append(QueryConstants.EXISTS_DOLLAR, isAllRepo)),
                        new Document().append(AND,
                                Arrays.asList(new Document().append(QueryConstants.REPOS_REPOID, repoId),
                                        new Document().append(QueryConstants.REPOS_SONAR_PROJECTID,
                                                new Document().append(QueryConstants.EXISTS_DOLLAR, !isAllRepo))))))),
                new Document().append(LOOKUP, new Document().append(FROM, "sonar_stats")
                    .append(LET,
                            new Document().append(ClientDashboardServiceConstants.SONAR_PROJECT_ID,
                                    QueryConstants.REPOS_SONAR_PROJECTID_DOLLAR))
                    .append(PIPELINE, Arrays.asList(
                            new Document().append(MATCH,
                                    new Document().append(EXPRESSION, new Document().append(AND, Arrays.asList(
                                            new Document().append(EQUAL,
                                                    Arrays.asList(QueryConstants.SONAR_PROJECT_ID_QUERY_PRAM,
                                                            QueryConstants.DOUBLE_DOLLAR_SONAR_PROJECT_ID)),
                                            new Document().append(GTE,
                                                    Arrays.asList(QueryConstants.DOLLAR_CREATED_DATE, startDate)),
                                            new Document().append(LTE,
                                                    Arrays.asList(QueryConstants.DOLLAR_CREATED_DATE, endDate)))))),
                            new Document().append(GROUP, new Document()
                                .append(ID,
                                        new Document()
                                            .append(MONTH,
                                                    new Document().append(QueryConstants.DOLLAR_MONTH,
                                                            new Document().append(QueryConstants.DOLLAR_TO_DATE,
                                                                    QueryConstants.DOLLAR_CREATED_DATE)))
                                            .append("year",
                                                    new Document().append(QueryConstants.DOLLAR_YEAR,
                                                            new Document().append(QueryConstants.DOLLAR_TO_DATE,
                                                                    QueryConstants.DOLLAR_CREATED_DATE))))
                                .append(QueryConstants.MONTH, new Document().append(PUSH_DOLLAR, ROOT_DOUBLE_DOLLAR))),
                            new Document().append(UNWIND, new Document().append(PATH, QueryConstants.DOLLAR_MONTH)),
                            new Document().append(SORT,
                                    new Document().append(QueryConstants.MONTH_CREATED_DATE, -1.0))))
                    .append(AS, DATA)),
                new Document().append(UNWIND, new Document().append(PATH, DATA_WITH_DOLLAR_SIGN)),
                new Document().append(GROUP,
                        new Document()
                            .append(ID,
                                    new Document().append(QueryConstants.MONTH, QueryConstants.DATA_ID_MONTH)
                                        .append(REPO_ID, QueryConstants.REPOS_REPOID_DOLLAR)
                                        .append("year", QueryConstants.DATA_ID_YEAR))
                            .append(REPOS, new Document().append(FIRST, QueryConstants.REPOS_DOLLAR))
                            .append(SECURITY, new Document().append(FIRST, "$data.month.ratings.security"))
                            .append(EFFICIENCY, new Document().append(FIRST, "$data.month.ratings.reliability"))
                            .append(ROBUSTNESS, new Document().append(FIRST, "$data.month.ratings.maintainability"))),
                new Document().append(SORT, new Document().append(ID, 1)),
                new Document().append(PROJECT, new Document().append(REPO_ID, QueryConstants.REPOS_REPOID_DOLLAR)
                    .append(REPO_NAME, QueryConstants.REPOS_REPONAME_DOLLAR)
                    .append(QueryConstants.MONTH, new Document().append(QueryConstants.DOLLAR_CONCAT,
                            Arrays.asList(new Document().append(TO_STRING_DOLLAR, QueryConstants.DOLLAR_ID_MONTH), "-",
                                    new Document().append(TO_STRING_DOLLAR, QueryConstants.DOLLAR_ID_YEAR))))
                    .append(SECURITY, 1.0)
                    .append(EFFICIENCY, 1.0)
                    .append(ROBUSTNESS, 1.0)),
                new Document().append(GROUP,
                        new Document().append(ID, QueryConstants.DOLLAR_REPO_ID)
                            .append(REPO_ID, new Document().append(FIRST, QueryConstants.DOLLAR_REPO_ID))
                            .append(REPO_NAME, new Document().append(FIRST, QueryConstants.DOLLAR_REPO_NAME))
                            .append(QueryConstants.MONTH_DATA,
                                    new Document().append(PUSH_DOLLAR,
                                            new Document().append(QueryConstants.MONTH, QueryConstants.DOLLAR_MONTH)
                                                .append(SECURITY, "$security")
                                                .append(EFFICIENCY, "$efficiency")
                                                .append(ROBUSTNESS, "$robustness")))));
        ObjectMapper mapper = new ObjectMapper();
        try {
            mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            return mapper.readValue(
                    ClientDashboardServiceUtils.documentsToJsonConverter(getDocumentsFromProjects(pipeline)),
                    new TypeReference<List<CodeScoreVo>>() {
                    });
        }
        catch (JsonProcessingException processingException) {
            log.error(JSON_PROCESSING_LOG_MESSAGE + processingException.getMessage());
            throw new ServiceException(JSON_PROCESSING_EXCEPTION_MESSAGE);
        }
    }

    private List<Document> getListOfDocumentDefectSnapshot() {
        return Arrays.asList(new Document().append(MATCH, new Document().append(EXPRESSION,
                new Document().append(AND, Arrays.asList(
                        new Document().append(IN, Arrays.asList(DOLLAR_SIGN + STATE, OPENED_STATUS_DOUBLE_DOLLAR_SIGN)),
                        new Document().append(NE, Arrays.asList(SPRINT_ID_DOLLAR_SIGN, null)))))),
                new Document()
                    .append(GROUP,
                            new Document().append(ID, 1.0)
                                .append(TOTAL_OPENED, new Document().append(SUM, 1.0))
                                .append(CRITICAL,
                                        new Document()
                                            .append(SUM,
                                                    new Document()
                                                        .append(COND,
                                                                Arrays.asList(
                                                                        new Document().append(IN,
                                                                                Arrays.asList(PRIORITY_NAME,
                                                                                        DOLLAR_SIGN + DOLLAR_SIGN
                                                                                                + CRITICAL)),
                                                                        1.0, 0.0))))
                                .append(BLOCKER,
                                        new Document()
                                            .append(SUM,
                                                    new Document()
                                                        .append(COND,
                                                                Arrays.asList(
                                                                        new Document().append(
                                                                                IN,
                                                                                Arrays.asList(PRIORITY_NAME,
                                                                                        DOLLAR_SIGN + DOLLAR_SIGN
                                                                                                + BLOCKER)),
                                                                        1.0, 0.0))))
                                .append(MAJOR,
                                        new Document().append(
                                                SUM,
                                                new Document().append(COND,
                                                        Arrays.asList(
                                                                new Document().append(IN,
                                                                        Arrays.asList(PRIORITY_NAME,
                                                                                DOLLAR_SIGN + DOLLAR_SIGN + MAJOR)),
                                                                1.0, 0.0))))
                                .append(MINOR,
                                        new Document()
                                            .append(SUM,
                                                    new Document()
                                                        .append(COND,
                                                                Arrays.asList(
                                                                        new Document().append(IN,
                                                                                Arrays.asList(PRIORITY_NAME,
                                                                                        DOLLAR_SIGN + DOLLAR_SIGN
                                                                                                + MINOR)),
                                                                        1.0, 0.0))))));
    }

    public List<ClientCodeMetricsVo> getCodeMetrics(String projectId, boolean isAllRepo, String repoId, Long startDate,
            Long endDate) {
        List<? extends Bson> pipeline = Arrays.asList(
                new Document().append(MATCH,
                        new Document().append(ID, new ObjectId(projectId)).append(IS_DELETED, false)),
                new Document().append(PROJECT, new Document().append(REPOS, 1.0)),
                new Document().append(UNWIND, new Document().append(PATH, QueryConstants.REPOS_DOLLAR)),
                getCodeMetricsMatchStage(isAllRepo, repoId),
                new Document().append(LOOKUP, new Document().append(FROM, "scm_code_statistical_analysis_month")
                    .append(LET, new Document().append("repo_url", "$repos.scmRepoUrl"))
                    .append(PIPELINE, Arrays.asList(
                            new Document().append(MATCH, new Document().append(EXPRESSION, new Document().append(AND,
                                    Arrays.asList(new Document().append(EQUAL, Arrays.asList("$repoUrl", "$$repo_url")),
                                            new Document().append(GTE, Arrays.asList(DOLLAR_SIGN + DATE, startDate)),
                                            new Document().append(LTE, Arrays.asList(DOLLAR_SIGN + DATE, endDate)))))),
                            getCodeMetricsGroupStage(), new Document().append(SORT, new Document().append(ID, 1)),
                            new Document().append(PROJECT,
                                    new Document().append(ID, 0.0)
                                        .append(MONTH, 1.0)
                                        .append(ADDED_LINE_OF_CODE, 1.0)
                                        .append(REMOVED_LINE_OF_CODE, 1.0))))
                    .append(AS, QueryConstants.MONTH_DATA)),
                getCodeMetricsLookupStage(),
                new Document().append(UNWIND, new Document().append(PATH, DOLLAR_SIGN + DATA)),
                new Document().append(PROJECT,
                        new Document().append(REPO_ID, QueryConstants.REPOS_REPOID_DOLLAR)
                            .append(REPO_NAME, QueryConstants.REPOS_REPONAME_DOLLAR)
                            .append(QueryConstants.MONTH_DATA, 1.0)
                            .append(CODE_CHURN, DOLLAR_SIGN + DATA + FULL_STOP + CODE_CHURN)
                            .append(LEGACY_REFACTOR, DOLLAR_SIGN + DATA + FULL_STOP + LEGACY_REFACTOR)));
        ObjectMapper mapper = new ObjectMapper();
        try {
            List<Document> documents = mongoTemplate.getCollection(PROJECTS)
                .aggregate(pipeline)
                .into(new ArrayList<>());
            mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            return mapper.readValue(ClientDashboardServiceUtils.documentsToJsonConverter(documents),
                    new TypeReference<>() {
                    });
        }
        catch (JsonProcessingException processingException) {
            log.error(JSON_PROCESSING_LOG_MESSAGE + processingException.getMessage());
            throw new ServiceException(JSON_PROCESSING_EXCEPTION_MESSAGE);
        }
    }

    private Document getCodeMetricsGroupStage() {
        return new Document().append(GROUP,
                new Document().append(ID, DOLLAR_SIGN + DATE)
                    .append(MONTH, new Document().append(FIRST,
                            new Document().append(QueryConstants.DOLLAR_CONCAT, Arrays.asList(
                                    new Document().append(TO_STRING_DOLLAR,
                                            new Document().append(DOLLAR_SIGN + MONTH,
                                                    new Document()
                                                        .append(QueryConstants.DOLLAR_TO_DATE, DOLLAR_SIGN + DATE))),
                                    "-",
                                    new Document().append(TO_STRING_DOLLAR,
                                            new Document().append(QueryConstants.DOLLAR_YEAR,
                                                    new Document().append(QueryConstants.DOLLAR_TO_DATE,
                                                            DOLLAR_SIGN + DATE)))))))
                    .append(ADDED_LINE_OF_CODE, new Document().append(FIRST, DOLLAR_SIGN + ADDED_LINE_OF_CODE))
                    .append(REMOVED_LINE_OF_CODE, new Document().append(FIRST, DOLLAR_SIGN + REMOVED_LINE_OF_CODE)));
    }

    private Document getCodeMetricsLookupStage() {
        return new Document()
            .append(LOOKUP,
                    new Document().append(FROM, SCM_CODE_STATISTICAL_ANALYSIS)
                        .append(LET, new Document().append("repo_url", "$repos.scmRepoUrl"))
                        .append(PIPELINE,
                                Arrays.asList(
                                        new Document().append(MATCH,
                                                new Document().append(EXPRESSION,
                                                        new Document().append(AND,
                                                                Arrays.asList(
                                                                        new Document().append(EQUAL,
                                                                                Arrays.asList("$repoUrl",
                                                                                        "$$repo_url")),
                                                                        new Document().append(EQUAL,
                                                                                Arrays.asList(DOLLAR_SIGN + IS_ARCHIVE,
                                                                                        false)))))),
                                        new Document().append(PROJECT,
                                                new Document().append(CODE_CHURN, "$overall.codeChurn")
                                                    .append(LEGACY_REFACTOR, "$overall.legacyRefactor"))))
                        .append(AS, DATA));
    }

    private Document getCodeMetricsMatchStage(boolean isAllRepo, String repoId) {
        return new Document().append(MATCH,
                new Document().append(OR, Arrays.asList(
                        new Document().append("repos.scmRepoUrl",
                                new Document().append(QueryConstants.EXISTS_DOLLAR, isAllRepo)),
                        new Document().append(AND,
                                Arrays.asList(new Document().append(QueryConstants.REPOS_REPOID, repoId),
                                        new Document().append("repos.scmRepoUrl",
                                                new Document().append(QueryConstants.EXISTS_DOLLAR, !isAllRepo)))))));
    }

    public List<CodeViolationsVo> getCodeViolations(String projectId, boolean isAllRepo, String repoId, Long startDate,
            Long endDate) {
        List<? extends Bson> pipeline = Arrays.asList(
                new Document().append(MATCH,
                        new Document().append(ID, new ObjectId(projectId)).append(IS_DELETED, false)),
                new Document().append(PROJECT, new Document().append(REPOS, 1.0)),
                new Document().append(UNWIND, new Document().append(PATH, QueryConstants.REPOS_DOLLAR)),
                new Document().append(MATCH, new Document().append(OR, Arrays.asList(
                        new Document().append(QueryConstants.REPOS_SONAR_PROJECTID,
                                new Document().append(QueryConstants.EXISTS_DOLLAR, isAllRepo)),
                        new Document().append(AND,
                                Arrays.asList(new Document().append(QueryConstants.REPOS_REPOID, repoId),
                                        new Document().append(QueryConstants.REPOS_SONAR_PROJECTID,
                                                new Document().append(QueryConstants.EXISTS_DOLLAR, !isAllRepo))))))),
                new Document().append(LOOKUP, new Document().append(FROM, EntitiesConstants.SONAR_STATS)
                    .append(LET,
                            new Document().append(ClientDashboardServiceConstants.SONAR_PROJECT_ID,
                                    QueryConstants.REPOS_SONAR_PROJECTID_DOLLAR))
                    .append(PIPELINE, Arrays.asList(
                            new Document().append(MATCH,
                                    new Document().append(EXPRESSION, new Document().append(AND, Arrays.asList(
                                            new Document().append(EQUAL,
                                                    Arrays.asList(QueryConstants.SONAR_PROJECT_ID_QUERY_PRAM,
                                                            QueryConstants.DOUBLE_DOLLAR_SONAR_PROJECT_ID)),
                                            new Document().append(GTE,
                                                    Arrays.asList(QueryConstants.DOLLAR_CREATED_DATE, startDate)),
                                            new Document().append(LTE,
                                                    Arrays.asList(QueryConstants.DOLLAR_CREATED_DATE, endDate)))))),
                            new Document().append(GROUP,
                                    new Document()
                                        .append(ID,
                                                new Document()
                                                    .append(QueryConstants.MONTH,
                                                            new Document().append(QueryConstants.DOLLAR_MONTH,
                                                                    new Document().append(QueryConstants.DOLLAR_TO_DATE,
                                                                            QueryConstants.DOLLAR_CREATED_DATE)))
                                                    .append("year",
                                                            new Document().append(QueryConstants.DOLLAR_YEAR,
                                                                    new Document().append(QueryConstants.DOLLAR_TO_DATE,
                                                                            QueryConstants.DOLLAR_CREATED_DATE))))
                                        .append(QueryConstants.MONTH,
                                                new Document().append(PUSH_DOLLAR, ROOT_DOUBLE_DOLLAR))),
                            new Document().append(UNWIND, new Document().append(PATH, QueryConstants.DOLLAR_MONTH)),
                            new Document().append(SORT,
                                    new Document().append(QueryConstants.MONTH_CREATED_DATE, -1.0))))
                    .append(AS, DATA)),
                new Document().append(UNWIND, new Document().append(PATH, DATA_WITH_DOLLAR_SIGN)),
                new Document().append(GROUP,
                        new Document()
                            .append(ID,
                                    new Document().append(QueryConstants.MONTH, QueryConstants.DATA_ID_MONTH)
                                        .append(REPO_ID, QueryConstants.REPOS_REPOID_DOLLAR)
                                        .append("year", QueryConstants.DATA_ID_YEAR))
                            .append(REPOS, new Document().append(FIRST, QueryConstants.REPOS_DOLLAR))
                            .append("violations", new Document().append(FIRST, "$data.month.violations"))),
                new Document().append(SORT, new Document().append(ID, 1)),
                new Document().append(PROJECT, new Document().append(ID, 0.0)
                    .append(REPO_ID, QueryConstants.REPOS_REPOID_DOLLAR)
                    .append(REPO_NAME, QueryConstants.REPOS_REPONAME_DOLLAR)
                    .append(QueryConstants.MONTH, new Document().append(QueryConstants.DOLLAR_CONCAT,
                            Arrays.asList(new Document().append(TO_STRING_DOLLAR, QueryConstants.DOLLAR_ID_MONTH), "-",
                                    new Document().append(TO_STRING_DOLLAR, QueryConstants.DOLLAR_ID_YEAR))))
                    .append("violations.total", 1.0)
                    .append("violations.critical", 1.0)),
                new Document().append(GROUP,
                        new Document().append(ID, QueryConstants.DOLLAR_REPO_ID)
                            .append(REPO_ID, new Document().append(FIRST, QueryConstants.DOLLAR_REPO_ID))
                            .append(REPO_NAME, new Document().append(FIRST, QueryConstants.DOLLAR_REPO_NAME))
                            .append(QueryConstants.MONTH_DATA,
                                    new Document().append(PUSH_DOLLAR,
                                            new Document().append(QueryConstants.MONTH, QueryConstants.DOLLAR_MONTH)
                                                .append("violations", "$violations")))));
        ObjectMapper mapper = new ObjectMapper();
        try {
            List<Document> documents = mongoTemplate.getCollection(PROJECTS)
                .aggregate(pipeline)
                .into(new ArrayList<>());
            mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            return mapper.readValue(ClientDashboardServiceUtils.documentsToJsonConverter(documents),
                    new TypeReference<>() {
                    });
        }
        catch (JsonProcessingException processingException) {
            log.error(JSON_PROCESSING_LOG_MESSAGE + processingException.getMessage());
            throw new ServiceException(JSON_PROCESSING_EXCEPTION_MESSAGE);
        }
    }

    public List<CodeSnapshotVo> getCodeHealthSnapshot(List<ObjectId> projectIds, long date) {
        List<? extends Bson> pipeline = Arrays.asList(
                new Document().append(MATCH,
                        new Document().append(ID, new Document().append(IN, projectIds)).append(IS_DELETED, false)),
                new Document().append(PROJECT, new Document().append(NAME, 1.0).append(REPOS, 1.0)),
                new Document().append(UNWIND, DOLLAR_SIGN + REPOS),
                new Document().append(MATCH, new Document().append(OR, Arrays.asList(
                        new Document().append(REPOS + FULL_STOP + SCM_REPO_URL, new Document().append(EXISTS, true)),
                        new Document().append(REPOS + FULL_STOP + SONAR_PROJECT_ID,
                                new Document().append(EXISTS, true))))),
                new Document().append(FACET,
                        new Document()
                            .append(BOTH_DATA, Arrays.asList(
                                    new Document().append(
                                            MATCH,
                                            new Document().append(AND, Arrays.asList(new Document().append(
                                                    REPOS + FULL_STOP + SCM_REPO_URL,
                                                    new Document().append(EXISTS, true)),
                                                    new Document().append(REPOS
                                                            + FULL_STOP + SONAR_PROJECT_ID,
                                                            new Document().append(EXISTS, true))))),
                                    new Document().append(LOOKUP, new Document()
                                        .append(FROM, SCM_CODE_STATISTICAL_ANALYSIS_MONTH)
                                        .append(LET,
                                                new Document().append(REPO_URL,
                                                        DOLLAR_SIGN + REPOS + FULL_STOP + SCM_REPO_URL))
                                        .append(PIPELINE, Arrays.asList(
                                                new Document().append(MATCH, new Document().append(EXPRESSION,
                                                        new Document().append(AND, Arrays.asList(
                                                                new Document().append(EQUAL,
                                                                        Arrays.asList(DOLLAR_SIGN + REPO_URL_CAMEL_CASE,
                                                                                DOLLAR_SIGN + DOLLAR_SIGN + REPO_URL)),
                                                                new Document().append(EQUAL,
                                                                        Arrays.asList(DOLLAR_SIGN + DATE, date)))))),
                                                new Document().append(
                                                        PROJECT,
                                                        new Document()
                                                            .append(ID, 0.0)
                                                            .append(TOTAL_LINE_OF_CODE, 1.0))))
                                        .append(AS, SCM_MONTH)),
                                    new Document().append(LOOKUP, new Document().append(FROM,
                                            SCM_CODE_STATISTICAL_ANALYSIS)
                                        .append(LET,
                                                new Document().append(REPO_URL,
                                                        DOLLAR_SIGN + REPOS + FULL_STOP + SCM_REPO_URL))
                                        .append(PIPELINE, Arrays.asList(
                                                new Document().append(MATCH, new Document()
                                                    .append(EXPRESSION, new Document().append(AND, Arrays.asList(
                                                            new Document().append(EQUAL,
                                                                    Arrays.asList(DOLLAR_SIGN + REPO_URL_CAMEL_CASE,
                                                                            DOLLAR_SIGN + DOLLAR_SIGN + REPO_URL)),
                                                            new Document().append(EQUAL,
                                                                    Arrays.asList(DOLLAR_SIGN + IS_ARCHIVE, false)))))),
                                                new Document().append(PROJECT,
                                                        new Document().append(ID, 0.0)
                                                            .append(CODE_CHURN,
                                                                    DOLLAR_SIGN + OVERALL + FULL_STOP + CODE_CHURN)
                                                            .append(LEGACY_REFACTOR,
                                                                    DOLLAR_SIGN + OVERALL + FULL_STOP
                                                                            + LEGACY_REFACTOR))))
                                        .append(AS, SCM)),
                                    new Document().append(LOOKUP, new Document()
                                        .append(FROM, EntitiesConstants.SONAR_STATS)
                                        .append(LET,
                                                new Document().append(ClientDashboardServiceConstants.SONAR_PROJECT_ID,
                                                        DOLLAR_SIGN + REPOS + FULL_STOP + SONAR_PROJECT_ID))
                                        .append(PIPELINE, Arrays.asList(
                                                new Document().append(MATCH, new Document()
                                                    .append(EXPRESSION, new Document().append(EQUAL,
                                                            Arrays.asList(
                                                                    DOLLAR_SIGN + SONAR_PROJECT_ID,
                                                                    QueryConstants.DOUBLE_DOLLAR_SONAR_PROJECT_ID)))),
                                                new Document().append(SORT, new Document().append(CREATED_DATE, -1.0)),
                                                new Document().append(LIMIT, 1.0),
                                                new Document().append(PROJECT, new Document()
                                                    .append(TOTAL_VIOLATION,
                                                            DOLLAR_SIGN + VIOLATIONS + FULL_STOP + TOTAL)
                                                    .append(TECHNICAL_DEBT,
                                                            new Document().append(ADD,
                                                                    Arrays.asList(DOLLAR_SIGN + TECHNICAL_DEBT_INDEX,
                                                                            RELIABILITY_EFFORTS, SECURITY_EFFORTS)))
                                                    .append(TEST_CODE_COVERAGE,
                                                            new Document().append(MULTIPLY,
                                                                    Arrays.asList(QUALITY_MATRIX_COVERAGE, 0.2)))
                                                    .append(SECURITY,
                                                            new Document().append(MULTIPLY,
                                                                    Arrays.asList(DOLLAR_SIGN + RATINGS_SECURITY, 20.0,
                                                                            0.2)))
                                                    .append(EFFICIENCY,
                                                            new Document().append(
                                                                    MULTIPLY,
                                                                    Arrays.asList(DOLLAR_SIGN + RATINGS_RELIABILITY,
                                                                            20.0, 0.2)))
                                                    .append(ROBUSTNESS, new Document().append(
                                                            MULTIPLY,
                                                            Arrays.asList(DOLLAR_SIGN + RATINGS_MAINTAINABILITY, 20.0,
                                                                    0.2)))
                                                    .append(VULNERABILITIES, new Document().append(MULTIPLY, Arrays
                                                        .asList(new Document().append(SWITCH, new Document()
                                                            .append(BRANCHES, Arrays.asList(new Document()
                                                                .append(CASE, new Document().append(AND, Arrays.asList(
                                                                        new Document().append(GTE, Arrays.asList(
                                                                                new Document().append(SUM, Collections
                                                                                    .singletonList(
                                                                                            VIOLATIONS_BLOCKER_VIOLATIONS_CRITICAL)),
                                                                                0.0)),
                                                                        new Document().append(LTE, Arrays.asList(
                                                                                new Document().append(SUM,
                                                                                        Collections.singletonList(
                                                                                                VIOLATIONS_BLOCKER_VIOLATIONS_CRITICAL)),
                                                                                10.0)))))
                                                                .append(THEN, 100.0),
                                                                    new Document()
                                                                        .append(CASE, new Document().append(AND, Arrays
                                                                            .asList(new Document().append(GTE, Arrays
                                                                                .asList(new Document().append(
                                                                                        SUM,
                                                                                        Collections.singletonList(
                                                                                                VIOLATIONS_BLOCKER_VIOLATIONS_CRITICAL)),
                                                                                        11.0)),
                                                                                    new Document().append(LTE,
                                                                                            Arrays.asList(new Document()
                                                                                                .append(SUM,
                                                                                                        Collections
                                                                                                            .singletonList(
                                                                                                                    VIOLATIONS_BLOCKER_VIOLATIONS_CRITICAL)),
                                                                                                    25.0)))))
                                                                        .append(THEN, 90.0),
                                                                    new Document().append(CASE, new Document()
                                                                        .append(AND, Arrays.asList(new Document()
                                                                            .append(GTE, Arrays.asList(new Document()
                                                                                .append(SUM, Collections.singletonList(
                                                                                        VIOLATIONS_BLOCKER_VIOLATIONS_CRITICAL)),
                                                                                    26.0)),
                                                                                new Document().append(LTE, Arrays
                                                                                    .asList(new Document().append(
                                                                                            SUM,
                                                                                            Collections.singletonList(
                                                                                                    VIOLATIONS_BLOCKER_VIOLATIONS_CRITICAL)),
                                                                                            50.0)))))
                                                                        .append(THEN, 80.0),
                                                                    new Document().append(CASE, new Document()
                                                                        .append(AND, Arrays.asList(new Document()
                                                                            .append(GTE, Arrays.asList(new Document()
                                                                                .append(SUM, Collections.singletonList(
                                                                                        VIOLATIONS_BLOCKER_VIOLATIONS_CRITICAL)),
                                                                                    51.0)),
                                                                                new Document().append(LTE, Arrays
                                                                                    .asList(new Document().append(
                                                                                            SUM,
                                                                                            Collections.singletonList(
                                                                                                    VIOLATIONS_BLOCKER_VIOLATIONS_CRITICAL)),
                                                                                            100.0)))))
                                                                        .append(THEN, 70.0),
                                                                    new Document().append(CASE, new Document()
                                                                        .append(AND, Arrays.asList(new Document()
                                                                            .append(GTE, Arrays.asList(new Document()
                                                                                .append(SUM, Collections.singletonList(
                                                                                        VIOLATIONS_BLOCKER_VIOLATIONS_CRITICAL)),
                                                                                    101.0)),
                                                                                new Document().append(LTE, Arrays
                                                                                    .asList(new Document().append(
                                                                                            SUM,
                                                                                            Collections.singletonList(
                                                                                                    VIOLATIONS_BLOCKER_VIOLATIONS_CRITICAL)),
                                                                                            200.0)))))
                                                                        .append(THEN, 60.0),
                                                                    new Document().append(CASE, new Document()
                                                                        .append(AND, Arrays.asList(new Document()
                                                                            .append(GTE, Arrays.asList(new Document()
                                                                                .append(SUM, Collections.singletonList(
                                                                                        VIOLATIONS_BLOCKER_VIOLATIONS_CRITICAL)),
                                                                                    201.0)),
                                                                                new Document().append(LTE, Arrays
                                                                                    .asList(new Document().append(
                                                                                            SUM,
                                                                                            Collections.singletonList(
                                                                                                    VIOLATIONS_BLOCKER_VIOLATIONS_CRITICAL)),
                                                                                            300.0)))))
                                                                        .append(THEN, 50.0),
                                                                    new Document().append(CASE, new Document()
                                                                        .append(GTE, Arrays.asList(new Document()
                                                                            .append(SUM, Collections.singletonList(
                                                                                    VIOLATIONS_BLOCKER_VIOLATIONS_CRITICAL)),
                                                                                301.0)))
                                                                        .append(THEN, 0.0)))
                                                            .append(DEFAULT, 0.0)), 0.2)))),
                                                new Document()
                                                    .append(PROJECT, new Document().append(TOTAL_VIOLATION, 1.0)
                                                        .append(TECHNICAL_DEBT, 1.0)
                                                        .append(COMPLIANCE,
                                                                new Document().append(ADD, Arrays.asList(
                                                                        new Document().append(MULTIPLY, Arrays.asList(
                                                                                DOLLAR_SIGN + TEST_CODE_COVERAGE, 0.2)),
                                                                        new Document().append(MULTIPLY,
                                                                                Arrays.asList(DOLLAR_SIGN + SECURITY,
                                                                                        0.2)),
                                                                        new Document().append(MULTIPLY,
                                                                                Arrays.asList(DOLLAR_SIGN + EFFICIENCY,
                                                                                        0.2)),
                                                                        new Document().append(MULTIPLY,
                                                                                Arrays.asList(DOLLAR_SIGN + ROBUSTNESS,
                                                                                        0.2)),
                                                                        new Document().append(MULTIPLY,
                                                                                Arrays.asList(
                                                                                        DOLLAR_SIGN + VULNERABILITIES,
                                                                                        0.2))))))))
                                        .append(AS, VIOLATIONS))))
                            .append(SCM_DATA, Arrays.asList(
                                    new Document().append(MATCH,
                                            new Document().append(AND, Arrays.asList(
                                                    new Document().append(REPOS + FULL_STOP + SCM_REPO_URL,
                                                            new Document().append(EXISTS, true)),
                                                    new Document().append(REPOS + FULL_STOP + SONAR_PROJECT_ID,
                                                            new Document().append(EXISTS, false))))),
                                    new Document().append(LOOKUP, new Document()
                                        .append(FROM, SCM_CODE_STATISTICAL_ANALYSIS_MONTH)
                                        .append(LET,
                                                new Document().append(REPO_URL,
                                                        DOLLAR_SIGN + REPOS + FULL_STOP + SCM_REPO_URL))
                                        .append(PIPELINE, Arrays.asList(
                                                new Document().append(MATCH, new Document().append(EXPRESSION,
                                                        new Document().append(AND, Arrays.asList(
                                                                new Document().append(EQUAL,
                                                                        Arrays.asList(DOLLAR_SIGN + REPO_URL_CAMEL_CASE,
                                                                                DOLLAR_SIGN + DOLLAR_SIGN + REPO_URL)),
                                                                new Document().append(EQUAL,
                                                                        Arrays.asList(DOLLAR_SIGN + DATE, date)))))),
                                                new Document().append(PROJECT,
                                                        new Document().append(ID, 0.0)
                                                            .append(TOTAL_LINE_OF_CODE, 1.0))))
                                        .append(AS, SCM_MONTH)),
                                    new Document().append(LOOKUP, new Document()
                                        .append(FROM, SCM_CODE_STATISTICAL_ANALYSIS)
                                        .append(LET,
                                                new Document().append(REPO_URL,
                                                        DOLLAR_SIGN + REPOS + FULL_STOP + SCM_REPO_URL))
                                        .append(PIPELINE,
                                                Arrays.asList(
                                                        new Document()
                                                            .append(MATCH, new Document().append(EXPRESSION,
                                                                    new Document().append(AND, Arrays.asList(
                                                                            new Document().append(EQUAL, Arrays.asList(
                                                                                    DOLLAR_SIGN + REPO_URL_CAMEL_CASE,
                                                                                    DOLLAR_SIGN + DOLLAR_SIGN
                                                                                            + REPO_URL)),
                                                                            new Document().append(EQUAL,
                                                                                    Arrays.asList(DOLLAR_SIGN
                                                                                            + IS_ARCHIVE, false)))))),
                                                        new Document().append(PROJECT, new Document().append(ID, 0.0)
                                                            .append(CODE_CHURN,
                                                                    DOLLAR_SIGN + OVERALL + FULL_STOP + CODE_CHURN)
                                                            .append(LEGACY_REFACTOR,
                                                                    DOLLAR_SIGN + OVERALL + FULL_STOP
                                                                            + LEGACY_REFACTOR))))
                                        .append(AS, SCM))))
                            .append(VIOLATION_DATA, Arrays.asList(
                                    new Document().append(MATCH,
                                            new Document().append(AND, Arrays.asList(
                                                    new Document().append(REPOS + FULL_STOP + SCM_REPO_URL,
                                                            new Document().append(EXISTS, false)),
                                                    new Document().append(REPOS + FULL_STOP + SONAR_PROJECT_ID,
                                                            new Document().append(EXISTS, true))))),
                                    new Document().append(LOOKUP, new Document()
                                        .append(FROM, EntitiesConstants.SONAR_STATS)
                                        .append(LET,
                                                new Document().append(ClientDashboardServiceConstants.SONAR_PROJECT_ID,
                                                        DOLLAR_SIGN + REPOS + FULL_STOP + SONAR_PROJECT_ID))
                                        .append(PIPELINE, Arrays.asList(
                                                new Document().append(MATCH, new Document()
                                                    .append(EXPRESSION, new Document().append(EQUAL,
                                                            Arrays.asList(DOLLAR_SIGN
                                                                    + SONAR_PROJECT_ID,
                                                                    QueryConstants.DOUBLE_DOLLAR_SONAR_PROJECT_ID)))),
                                                new Document().append(SORT, new Document().append(CREATED_DATE, -1.0)),
                                                new Document().append(LIMIT, 1.0),
                                                new Document().append(PROJECT, new Document()
                                                    .append(TOTAL_VIOLATION,
                                                            DOLLAR_SIGN + VIOLATIONS + FULL_STOP + TOTAL)
                                                    .append(TECHNICAL_DEBT,
                                                            new Document().append(ADD,
                                                                    Arrays.asList(TECHNICAL_DEBT_INDEX,
                                                                            RELIABILITY_EFFORTS, SECURITY_EFFORTS)))
                                                    .append(TEST_CODE_COVERAGE,
                                                            new Document().append(MULTIPLY,
                                                                    Arrays.asList(QUALITY_MATRIX_COVERAGE, 0.2)))
                                                    .append(SECURITY,
                                                            new Document().append(MULTIPLY,
                                                                    Arrays.asList(DOLLAR_SIGN + RATINGS_SECURITY, 20.0,
                                                                            0.2)))
                                                    .append(EFFICIENCY,
                                                            new Document().append(MULTIPLY,
                                                                    Arrays.asList(DOLLAR_SIGN + RATINGS_RELIABILITY,
                                                                            20.0, 0.2)))
                                                    .append(ROBUSTNESS, new Document().append(
                                                            MULTIPLY,
                                                            Arrays.asList(DOLLAR_SIGN + RATINGS_MAINTAINABILITY, 20.0,
                                                                    0.2)))
                                                    .append(VULNERABILITIES, new Document()
                                                        .append(MULTIPLY, Arrays.asList(new Document().append(
                                                                SWITCH, new Document().append(BRANCHES, Arrays.asList(
                                                                        new Document().append(CASE, new Document()
                                                                            .append(AND, Arrays.asList(new Document()
                                                                                .append(GTE, Arrays.asList(
                                                                                        new Document()
                                                                                            .append(SUM, Collections
                                                                                                .singletonList(
                                                                                                        VIOLATIONS_BLOCKER_VIOLATIONS_CRITICAL)),
                                                                                        0.0)),
                                                                                    new Document().append(LTE, Arrays
                                                                                        .asList(new Document().append(
                                                                                                SUM,
                                                                                                Collections
                                                                                                    .singletonList(
                                                                                                            VIOLATIONS_BLOCKER_VIOLATIONS_CRITICAL)),
                                                                                                10.0)))))
                                                                            .append(THEN, 100.0),
                                                                        new Document().append(CASE, new Document()
                                                                            .append(AND, Arrays.asList(new Document()
                                                                                .append(GTE, Arrays
                                                                                    .asList(new Document().append(SUM,
                                                                                            Collections.singletonList(
                                                                                                    VIOLATIONS_BLOCKER_VIOLATIONS_CRITICAL)),
                                                                                            11.0)),
                                                                                    new Document().append(LTE, Arrays
                                                                                        .asList(new Document().append(
                                                                                                SUM,
                                                                                                Collections
                                                                                                    .singletonList(
                                                                                                            VIOLATIONS_BLOCKER_VIOLATIONS_CRITICAL)),
                                                                                                25.0)))))
                                                                            .append(THEN, 90.0),
                                                                        new Document().append(CASE, new Document()
                                                                            .append(AND, Arrays.asList(new Document()
                                                                                .append(GTE, Arrays
                                                                                    .asList(new Document().append(SUM,
                                                                                            Collections.singletonList(
                                                                                                    VIOLATIONS_BLOCKER_VIOLATIONS_CRITICAL)),
                                                                                            26.0)),
                                                                                    new Document().append(LTE, Arrays
                                                                                        .asList(new Document().append(
                                                                                                SUM,
                                                                                                Collections
                                                                                                    .singletonList(
                                                                                                            VIOLATIONS_BLOCKER_VIOLATIONS_CRITICAL)),
                                                                                                50.0)))))
                                                                            .append(THEN, 80.0),
                                                                        new Document().append(CASE, new Document()
                                                                            .append(AND, Arrays.asList(new Document()
                                                                                .append(GTE, Arrays
                                                                                    .asList(new Document().append(SUM,
                                                                                            Collections.singletonList(
                                                                                                    VIOLATIONS_BLOCKER_VIOLATIONS_CRITICAL)),
                                                                                            51.0)),
                                                                                    new Document().append(LTE, Arrays
                                                                                        .asList(new Document().append(
                                                                                                SUM,
                                                                                                Collections
                                                                                                    .singletonList(
                                                                                                            VIOLATIONS_BLOCKER_VIOLATIONS_CRITICAL)),
                                                                                                100.0)))))
                                                                            .append(THEN, 70.0),
                                                                        new Document().append(CASE, new Document()
                                                                            .append(AND, Arrays.asList(new Document()
                                                                                .append(GTE, Arrays
                                                                                    .asList(new Document().append(SUM,
                                                                                            Collections.singletonList(
                                                                                                    VIOLATIONS_BLOCKER_VIOLATIONS_CRITICAL)),
                                                                                            101.0)),
                                                                                    new Document().append(LTE, Arrays
                                                                                        .asList(new Document().append(
                                                                                                SUM,
                                                                                                Collections
                                                                                                    .singletonList(
                                                                                                            VIOLATIONS_BLOCKER_VIOLATIONS_CRITICAL)),
                                                                                                200.0)))))
                                                                            .append(THEN, 60.0),
                                                                        new Document().append(CASE, new Document()
                                                                            .append(AND, Arrays
                                                                                .asList(new Document().append(
                                                                                        GTE,
                                                                                        Arrays.asList(new Document()
                                                                                            .append(SUM, Collections
                                                                                                .singletonList(
                                                                                                        VIOLATIONS_BLOCKER_VIOLATIONS_CRITICAL)),
                                                                                                201.0)),
                                                                                        new Document().append(
                                                                                                LTE, Arrays
                                                                                                    .asList(new Document()
                                                                                                        .append(SUM,
                                                                                                                Collections
                                                                                                                    .singletonList(
                                                                                                                            VIOLATIONS_BLOCKER_VIOLATIONS_CRITICAL)),
                                                                                                            300.0)))))
                                                                            .append(THEN, 50.0),
                                                                        new Document().append(CASE, new Document()
                                                                            .append(GTE, Arrays.asList(new Document()
                                                                                .append(SUM, Collections.singletonList(
                                                                                        VIOLATIONS_BLOCKER_VIOLATIONS_CRITICAL)),
                                                                                    301.0)))
                                                                            .append(THEN, 0.0)))
                                                                    .append(DEFAULT, 0.0)),
                                                                0.2)))),
                                                new Document()
                                                    .append(PROJECT, new Document().append(TOTAL_VIOLATION, 1.0)
                                                        .append(TECHNICAL_DEBT, 1.0)
                                                        .append(COMPLIANCE, new Document().append(ADD, Arrays.asList(
                                                                new Document().append(MULTIPLY,
                                                                        Arrays.asList(DOLLAR_SIGN + TEST_CODE_COVERAGE,
                                                                                0.2)),
                                                                new Document().append(MULTIPLY,
                                                                        Arrays.asList(DOLLAR_SIGN + SECURITY, 0.2)),
                                                                new Document().append(MULTIPLY,
                                                                        Arrays.asList(DOLLAR_SIGN + EFFICIENCY, 0.2)),
                                                                new Document().append(MULTIPLY,
                                                                        Arrays.asList(DOLLAR_SIGN + ROBUSTNESS, 0.2)),
                                                                new Document().append(MULTIPLY,
                                                                        Arrays.asList(DOLLAR_SIGN + VULNERABILITIES,
                                                                                0.2))))))))
                                        .append(AS, VIOLATIONS))))),
                new Document().append(PROJECT,
                        new Document().append(DATA,
                                new Document().append(CONCAT_ARRAYS,
                                        Arrays.asList(DOLLAR_SIGN + SCM_DATA, DOLLAR_SIGN + VIOLATION_DATA,
                                                DOLLAR_SIGN + BOTH_DATA)))),
                new Document().append(UNWIND, DOLLAR_SIGN + DATA),
                new Document().append(PROJECT, new Document().append("id", DOLLAR_SIGN + DATA + FULL_STOP + ID)
                    .append(PROJECT_NAME, DOLLAR_SIGN + DATA + FULL_STOP + NAME)
                    .append(REPO_ID, DOLLAR_SIGN + DATA + FULL_STOP + REPOS + FULL_STOP + REPO_ID)
                    .append(REPO_NAME, DOLLAR_SIGN + DATA + FULL_STOP + REPOS + FULL_STOP + REPO_NAME)
                    .append(TOTAL_LINE_OF_CODE,
                            new Document().append(IF_NULL,
                                    Arrays.asList(DOLLAR_SIGN + DATA + FULL_STOP + SCM_MONTH, Collections.emptyList())))
                    .append(VIOLATIONS,
                            new Document().append(IF_NULL,
                                    Arrays.asList(DOLLAR_SIGN + DATA + FULL_STOP + VIOLATIONS,
                                            Collections.emptyList())))
                    .append(SCM_DATA,
                            new Document().append(IF_NULL,
                                    Arrays.asList(DOLLAR_SIGN + DATA + FULL_STOP + SCM, Collections.emptyList())))),
                new Document().append(UNWIND, DOLLAR_SIGN + TOTAL_LINE_OF_CODE),
                new Document().append(SORT,
                        new Document().append(TOTAL_LINE_OF_CODE + FULL_STOP + TOTAL_LINE_OF_CODE, -1.0)),
                new Document().append(GROUP,
                        new Document().append(ID, new Document().append(TO_STRING_DOLLAR, "$id"))
                            .append(PROJECT_NAME, new Document().append(FIRST, DOLLAR_SIGN + PROJECT_NAME))
                            .append(DATA, new Document().append(PUSH_DOLLAR,
                                    new Document().append(REPO_ID, DOLLAR_SIGN + REPO_ID)
                                        .append(REPO_NAME, DOLLAR_SIGN + REPO_NAME)
                                        .append(TOTAL_LINE_OF_CODE,
                                                DOLLAR_SIGN + TOTAL_LINE_OF_CODE + FULL_STOP + TOTAL_LINE_OF_CODE)
                                        .append(VIOLATIONS_AND_TECH_DEBT, DOLLAR_SIGN + VIOLATIONS)
                                        .append(SCM_DATA, DOLLAR_SIGN + SCM_DATA)))));

        ObjectMapper mapper = new ObjectMapper();
        try {
            List<Document> documents = mongoTemplate.getCollection(PROJECTS)
                .aggregate(pipeline)
                .into(new ArrayList<>());
            mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            return mapper.readValue(ClientDashboardServiceUtils.documentsToJsonConverter(documents),
                    new TypeReference<>() {
                    });
        }
        catch (JsonProcessingException processingException) {
            log.error(JSON_PROCESSING_LOG_MESSAGE + processingException.getMessage());
            throw new ServiceException(JSON_PROCESSING_EXCEPTION_MESSAGE);
        }
    }

    public List<CodeTechDebtVo> getTechnicalDebt(String projectId, boolean isAllRepo, String repoId, Long startDate,
            Long endDate) {
        List<? extends Bson> pipeline = Arrays.asList(
                new Document().append(MATCH,
                        new Document().append(ID, new ObjectId(projectId)).append(IS_DELETED, false)),
                new Document().append(PROJECT, new Document().append(REPOS, 1.0)),
                new Document().append(UNWIND, new Document().append(PATH, QueryConstants.REPOS_DOLLAR)),
                new Document().append(MATCH, new Document().append(OR, Arrays.asList(
                        new Document().append(QueryConstants.REPOS_SONAR_PROJECTID,
                                new Document().append(QueryConstants.EXISTS_DOLLAR, isAllRepo)),
                        new Document().append(AND,
                                Arrays.asList(new Document().append(QueryConstants.REPOS_REPOID, repoId),
                                        new Document().append(QueryConstants.REPOS_SONAR_PROJECTID,
                                                new Document().append(QueryConstants.EXISTS_DOLLAR, !isAllRepo))))))),
                getTechnicalDebtLookupStage(startDate, endDate),
                new Document().append(UNWIND, new Document().append(PATH, DOLLAR_SIGN + DATA)),
                getTechnicalDebtGroupStage(), getTechnicalDebtProjectStage(),
                new Document().append(SORT, new Document().append("_id.month", 1.0).append("_id.year", 1.0)),
                new Document().append(GROUP,
                        new Document().append(ID, QueryConstants.DOLLAR_REPO_ID)
                            .append(REPO_ID, new Document().append(FIRST, QueryConstants.DOLLAR_REPO_ID))
                            .append(REPO_NAME, new Document().append(FIRST, QueryConstants.DOLLAR_REPO_NAME))
                            .append(QueryConstants.MONTH_DATA,
                                    new Document().append(PUSH_DOLLAR, new Document().append(MONTH, DOLLAR_SIGN + MONTH)
                                        .append(COMPLEXITY, DOLLAR_SIGN + COMPLEXITY)
                                        .append(ISSUES, DOLLAR_SIGN + ISSUES)
                                        .append(MAINTAINABILITY, DOLLAR_SIGN + MAINTAINABILITY)
                                        .append(RELIABILITY, DOLLAR_SIGN + RELIABILITY)
                                        .append(SECURITY, DOLLAR_SIGN + SECURITY)
                                        .append(SIZE, DOLLAR_SIGN + SIZE)
                                        .append(QueryConstants.COVERAGE, "$coverage")
                                        .append(DUPLICATION, DOLLAR_SIGN + DUPLICATION)
                                        .append(TOTAL, new Document().append(SUM, Arrays.asList(DOLLAR_SIGN + SECURITY,
                                                DOLLAR_SIGN + MAINTAINABILITY, DOLLAR_SIGN + RELIABILITY)))))));
        ObjectMapper mapper = new ObjectMapper();
        try {
            List<Document> documents = mongoTemplate.getCollection(PROJECTS)
                .aggregate(pipeline)
                .into(new ArrayList<>());
            mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            return mapper.readValue(ClientDashboardServiceUtils.documentsToJsonConverter(documents),
                    new TypeReference<>() {
                    });
        }
        catch (JsonProcessingException processingException) {
            log.error(JSON_PROCESSING_LOG_MESSAGE + processingException.getMessage());
            throw new ServiceException(JSON_PROCESSING_EXCEPTION_MESSAGE);
        }
    }

    private Document getTechnicalDebtLookupStage(Long startDate, Long endDate) {
        return new Document().append(LOOKUP, new Document().append(FROM, EntitiesConstants.SONAR_STATS)
            .append(LET,
                    new Document().append(ClientDashboardServiceConstants.SONAR_PROJECT_ID,
                            QueryConstants.REPOS_SONAR_PROJECTID_DOLLAR))
            .append(PIPELINE, Arrays.asList(
                    new Document().append(MATCH,
                            new Document().append(EXPRESSION, new Document().append(AND, Arrays.asList(
                                    new Document().append(EQUAL,
                                            Arrays.asList(QueryConstants.SONAR_PROJECT_ID_QUERY_PRAM,
                                                    QueryConstants.DOUBLE_DOLLAR_SONAR_PROJECT_ID)),
                                    new Document().append(GTE, Arrays.asList(DOLLAR_SIGN + CREATED_DATE, startDate)),
                                    new Document().append(LTE, Arrays.asList(DOLLAR_SIGN + CREATED_DATE, endDate)))))),
                    new Document().append(GROUP,
                            new Document()
                                .append(ID,
                                        new Document()
                                            .append(MONTH,
                                                    new Document().append(DOLLAR_SIGN + MONTH,
                                                            new Document().append(QueryConstants.DOLLAR_TO_DATE,
                                                                    DOLLAR_SIGN + CREATED_DATE)))
                                            .append("year",
                                                    new Document().append(QueryConstants.DOLLAR_YEAR,
                                                            new Document().append(QueryConstants.DOLLAR_TO_DATE,
                                                                    DOLLAR_SIGN + CREATED_DATE))))
                                .append(MONTH, new Document().append(PUSH_DOLLAR, ROOT_DOUBLE_DOLLAR))),
                    new Document().append(UNWIND, new Document().append(PATH, DOLLAR_SIGN + MONTH)),
                    new Document().append(SORT, new Document().append(QueryConstants.MONTH_CREATED_DATE, -1.0))))
            .append(AS, DATA));
    }

    private Document getTechnicalDebtGroupStage() {
        return new Document().append(GROUP,
                new Document()
                    .append(ID,
                            new Document().append(MONTH, QueryConstants.DATA_ID_MONTH)
                                .append(REPO_ID, QueryConstants.REPOS_REPOID_DOLLAR)
                                .append("year", QueryConstants.DATA_ID_YEAR))
                    .append(REPOS, new Document().append(FIRST, QueryConstants.REPOS_DOLLAR))
                    .append(MAINTAINABILITY, new Document().append(FIRST, "$data.month.technicalDebtIndex"))
                    .append(RELIABILITY, new Document().append(FIRST, "$data.month.reliability.efforts"))
                    .append(SECURITY, new Document().append(FIRST, "$data.month.securityEfforts"))
                    .append(COMPLEXITY, new Document().append(FIRST, "$data.month.complexity"))
                    .append(CONGNITIVE_COMPLEXITY, new Document().append(FIRST, "$data.month.cognitiveComplexity"))
                    .append(QueryConstants.COVERAGE, new Document().append(FIRST, "$data.month.qualityMatrix.coverage"))
                    .append(ISSUES, new Document().append(FIRST, "$data.month.issues"))
                    .append("duplicateLines", new Document().append(FIRST, "$data.month.duplication.lines"))
                    .append(SIZE, new Document().append(FIRST, "$data.month.codeMatrix.ncloc")));
    }

    private Document getTechnicalDebtProjectStage() {
        return new Document()
            .append(PROJECT,
                    new Document().append(REPO_ID, QueryConstants.REPOS_REPOID_DOLLAR)
                        .append(REPO_NAME, QueryConstants.REPOS_REPONAME_DOLLAR)
                        .append(MONTH, new Document().append(QueryConstants.DOLLAR_CONCAT,
                                Arrays.asList(new Document().append(TO_STRING_DOLLAR, QueryConstants.DOLLAR_ID_MONTH),
                                        "-", new Document().append(TO_STRING_DOLLAR, QueryConstants.DOLLAR_ID_YEAR))))
                        .append(COMPLEXITY,
                                new Document().append(SUM,
                                        Arrays.asList(DOLLAR_SIGN + COMPLEXITY, DOLLAR_SIGN + CONGNITIVE_COMPLEXITY)))
                        .append(ISSUES,
                                new Document().append(SUM,
                                        Arrays.asList("$issues.open", "$issues.confirmed", "$issues.reopened")))
                        .append(MAINTAINABILITY, 1.0)
                        .append(RELIABILITY, 1.0)
                        .append(SECURITY, 1.0)
                        .append(SIZE, 1.0)
                        .append(QueryConstants.COVERAGE, 1.0)
                        .append(DUPLICATION,
                                new Document()
                                    .append(COND,
                                            Arrays
                                                .asList(new Document().append(EQUAL,
                                                        Arrays.asList(DOLLAR_SIGN + SIZE, 0.0)), 0.0,
                                                        new Document().append(MULTIPLY,
                                                                Arrays.asList(
                                                                        new Document().append("$divide",
                                                                                Arrays.asList("$duplicateLines",
                                                                                        DOLLAR_SIGN + SIZE)),
                                                                        100.0))))));
    }

}
