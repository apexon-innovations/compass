package com.apexon.compass.psrservice.dao;

import static com.apexon.compass.constants.ClientDashboardServiceConstants.MONGO_EXCEPTION_MESSAGE;
import static com.apexon.compass.constants.ClientDashboardServiceConstants.TYPE_DOLLAR_SIGN;
import static com.apexon.compass.constants.ClientDashboardServiceConstants.TO_INT_DOLLAR;
import static com.apexon.compass.constants.ClientDashboardServiceConstants.JIRA_PROJECT_ID_DOLLAR;
import static com.apexon.compass.constants.ClientDashboardServiceConstants.PROJECT_ID_DOLLAR;
import static com.apexon.compass.constants.ClientDashboardServiceConstants.STATE_DOLLAR_SIGN;
import static com.apexon.compass.constants.ClientDashboardServiceConstants.END_DATE;
import static com.apexon.compass.constants.ClientDashboardServiceConstants.SPRINT_ID_DOLLAR_SIGN;
import static com.apexon.compass.constants.ClientDashboardServiceConstants.SPRINT_DATA_DOLLAR;
import static com.apexon.compass.constants.ClientDashboardServiceConstants.SPRINT_DATA;
import static com.apexon.compass.constants.ClientDashboardServiceConstants.END_DATE_SPRINTDATA_ENDDATE;
import static com.apexon.compass.constants.ClientDashboardServiceConstants.ONE_DOUBLE;
import static com.apexon.compass.constants.ClientDashboardServiceConstants.ZERO_DOUBLE;
import static com.apexon.compass.constants.ClientDashboardServiceConstants.ZERO;
import static com.apexon.compass.constants.ClientDashboardServiceConstants.ESTIMATE_DOLLAR;
import static com.apexon.compass.constants.EntitiesConstants.JIRA_CONFIGURATION;
import static com.apexon.compass.constants.PsrServiceConstants.*;
import static com.apexon.compass.constants.QueryConstants.PROJECT_ID_DOUBLE_DOLLAR;
import static com.apexon.compass.constants.StrategyServiceConstants.MATCH;
import static com.apexon.compass.constants.StrategyServiceConstants.PROJECT;
import static com.apexon.compass.constants.StrategyServiceConstants.LET;
import static com.apexon.compass.constants.StrategyServiceConstants.PIPELINE;
import static com.apexon.compass.constants.StrategyServiceConstants.EXPRESSION;
import static com.apexon.compass.constants.StrategyServiceConstants.AS;
import static com.apexon.compass.constants.StrategyServiceConstants.AND;
import static com.apexon.compass.constants.StrategyServiceConstants.EQUAL;
import static com.apexon.compass.constants.StrategyServiceConstants.IN;
import static com.apexon.compass.constants.StrategyServiceConstants.UNWIND;
import static com.apexon.compass.constants.StrategyServiceConstants.GROUP;
import static com.apexon.compass.constants.StrategyServiceConstants.SUM;
import static com.apexon.compass.constants.StrategyServiceConstants.BUGS;
import static com.apexon.compass.constants.StrategyServiceConstants.COND;
import static com.apexon.compass.constants.StrategyServiceConstants.NE;
import static com.apexon.compass.psrservice.constants.AggregationQuery.*;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.apexon.compass.aggregation.vo.*;
import com.apexon.compass.constants.PsrServiceConstants;
import com.apexon.compass.exception.custom.ServiceException;
import com.mongodb.MongoException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.ListUtils;
import org.bson.BsonNull;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.aggregation.TypedAggregation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Component;

import com.apexon.compass.entities.JiraConfiguration;
import com.apexon.compass.psrservice.service.impl.helper.JiraRulesHelper;

import lombok.AllArgsConstructor;

@Component
@AllArgsConstructor(onConstructor = @__({ @Autowired }))
@Slf4j
public class SprintDao {

    public static final String JIRA_PROJECT_ID = "jira_project_id";

    public static final String JIRA_PROJECT_ID_DOC = "$$" + JIRA_PROJECT_ID;

    private final MongoTemplate mongoTemplate;

    private JiraRulesHelper jiraRulesHelper;

    public ProjectWiseStorySummaryVo getStories(Integer sprintId, String projectId, Integer boardId) {
        List<? extends Bson> pipeline = Arrays.asList(
                new Document().append(MATCH, new Document().append(PROJECT__ID, new ObjectId(projectId))),
                new Document().append(PROJECT,
                        new Document().append(PROJECT__ID, ONE_DOUBLE)
                            .append(JIRAPROJECTID, new Document().append(TO_INT_DOLLAR, JIRA_PROJECT_ID_DOLLAR))
                            .append(ID, ZERO_DOUBLE)),
                new Document().append(LOOKUP, new Document().append(FROM, SPRINTS)
                    .append(LET,
                            new Document().append(JIRAPROJECTID, JIRA_PROJECT_ID_DOLLAR)
                                .append(PROJECT__ID, PROJECT_ID_DOLLAR))
                    .append(PIPELINE, Collections.singletonList(new Document().append(MATCH,
                            new Document().append(EXPRESSION, new Document().append(AND, Arrays.asList(
                                    new Document().append(EQUAL,
                                            Arrays.asList(PROJECT_ID_DOLLAR, PROJECT_ID_DOUBLE_DOLLAR)),
                                    new Document().append(EQUAL,
                                            Arrays.asList(JIRA_PROJECT_ID_DOLLAR, JIRA_PROJECT_ID_DOUBLE_DOLLAR)),
                                    new Document().append(EQUAL, Arrays.asList(STATE_DOLLAR_SIGN, ACTIVE)),
                                    new Document().append(EQUAL, Arrays.asList(SPRINT_ID_DOLLAR_SIGN, sprintId)),
                                    new Document().append(IN,
                                            Arrays.asList(BOARD_ID_DOLLAR, Collections.singletonList(boardId)))))))))
                    .append(AS, SPRINT_DATA)),
                new Document().append(UNWIND, SPRINT_DATA_DOLLAR),
                new Document().append(LOOKUP, new Document().append(FROM, STORIES)
                    .append(LET,
                            new Document().append(JIRAPROJECTID, JIRA_PROJECT_ID_DOLLAR)
                                .append(PROJECT__ID, PROJECT_ID_DOLLAR))
                    .append(PIPELINE, Arrays.asList(
                            new Document().append(MATCH, new Document()
                                .append(EXPRESSION, new Document().append(AND, Arrays.asList(
                                        new Document().append(EQUAL,
                                                Arrays.asList(PROJECT_ID_DOLLAR, PROJECT_ID_DOUBLE_DOLLAR)),
                                        new Document().append(EQUAL,
                                                Arrays.asList(JIRA_PROJECT_ID_DOLLAR, JIRA_PROJECT_ID_DOUBLE_DOLLAR)),
                                        new Document().append(EQUAL, Arrays.asList(SPRINT_ID_DOLLAR_SIGN, sprintId)),
                                        new Document().append(IN,
                                                Arrays.asList(BOARD_ID_DOLLAR, Collections.singletonList(boardId))),
                                        new Document().append(IN,
                                                Arrays.asList(TYPE_DOLLAR_SIGN,
                                                        jiraRulesHelper.getStoryTaskTypesByProjectIdInList(
                                                                new ObjectId(projectId)))))))),
                            new Document().append(GROUP, new Document().append(ID, ONE_DOUBLE)
                                .append(TOTAL, new Document().append(SUM, ONE_DOUBLE))
                                .append(TOTAL_COMPLETED, new Document()
                                    .append(SUM, new Document().append(COND, Arrays.asList(
                                            new Document().append(IN, Arrays.asList(STATE_DOLLAR_SIGN, ListUtils.union(
                                                    jiraRulesHelper.getDefinitionOfDoneinList(new ObjectId(projectId)),
                                                    jiraRulesHelper
                                                        .getDefinitionOfAcceptedinList(new ObjectId(projectId))))),
                                            ONE_DOUBLE, ZERO_DOUBLE))))
                                .append(TOTAL_TO_DO,
                                        new Document().append(SUM,
                                                new Document().append(COND, Arrays.asList(
                                                        new Document().append(IN,
                                                                Arrays.asList(STATE_DOLLAR_SIGN,
                                                                        jiraRulesHelper.getDefinitionOfToDoInList(
                                                                                new ObjectId(projectId)))),
                                                        ONE_DOUBLE, ZERO_DOUBLE))))
                                .append(TOTAL_IN_PROGRESS,
                                        new Document().append(SUM,
                                                new Document().append(COND, Arrays.asList(
                                                        new Document().append(IN,
                                                                Arrays.asList(STATE_DOLLAR_SIGN,
                                                                        jiraRulesHelper.getInProgressByProjectIdInList(
                                                                                new ObjectId(projectId)))),
                                                        ONE_DOUBLE, ZERO_DOUBLE))))
                                .append(TOTAL_ASSIGNED,
                                        new Document()
                                            .append(SUM,
                                                    new Document()
                                                        .append(COND,
                                                                Arrays.asList(
                                                                        new Document().append(NE,
                                                                                Arrays.asList(OWNERS_ID,
                                                                                        new BsonNull())),
                                                                        ONE_DOUBLE, ZERO_DOUBLE))))
                                .append(TOTAL_UNASSIGNED,
                                        new Document()
                                            .append(SUM,
                                                    new Document()
                                                        .append(COND,
                                                                Arrays.asList(
                                                                        new Document().append(EQUAL,
                                                                                Arrays.asList(OWNERS_ID,
                                                                                        new BsonNull())),
                                                                        ONE_DOUBLE, ZERO_DOUBLE)))))))
                    .append(AS, STORIES)),
                new Document().append(PROJECT,
                        new Document().append(PsrServiceConstants.JIRA_PROJECT_ID, PROJECT_ID_SPRINT_DATA_DOLLAR)
                            .append(BOARD_ID, BOARD_ID_SPRINT_DATA_DOLLAR)
                            .append(SPRINT_ID, SPRINT_ID_SPRINT_DATA_DOLLAR)
                            .append(PROJECT__ID, ISC_PROJECT_ID_SPRINT_DATA_DOLLAR)
                            .append(NAME, NAME_SPRINT_DATA_DOLLAR)
                            .append(START_DATE, START_DATE_SPRINT_DATA_DOLLAR)
                            .append(END_DATE, END_DATE_SPRINTDATA_ENDDATE)
                            .append(STATE, STATE_SPRINT_DATA_DOLLAR)
                            .append(JIRA_CHANGE_DATE, JIRA_CHANGE_DATE_SPRINT_DATA_DOLLAR)
                            .append(IS_DELETED, IS_DELETED_SPRINT_DATA_DOLLAR)
                            .append(CREATED_DATE, CREATED_DATE_SPRINT_DATA_DOLLAR)
                            .append(STORIES, ONE_DOUBLE)));

        try {
            return getDocuments(JIRA_CONFIGURATION, pipeline, ProjectWiseStorySummaryVo.class).get(ZERO);
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

    public SprintWithBugsVo getBugs(Integer sprintId, String projectId, Integer boardId) {
        List<? extends Bson> pipeline = Arrays.asList(
                new Document().append(MATCH, new Document().append(PROJECT__ID, new ObjectId(projectId))),
                new Document().append(PROJECT,
                        new Document().append(PROJECT__ID, ONE_DOUBLE)
                            .append(JIRAPROJECTID, new Document().append(TO_INT_DOLLAR, JIRA_PROJECT_ID_DOLLAR))
                            .append(ID, ZERO_DOUBLE)),
                new Document().append(LOOKUP, new Document().append(FROM, SPRINTS)
                    .append(LET,
                            new Document().append(JIRAPROJECTID, JIRA_PROJECT_ID_DOLLAR)
                                .append(PROJECT__ID, PROJECT_ID_DOLLAR))
                    .append(PIPELINE, Collections.singletonList(new Document().append(MATCH,
                            new Document().append(EXPRESSION, new Document().append(AND, Arrays.asList(
                                    new Document().append(EQUAL,
                                            Arrays.asList(PROJECT_ID_DOLLAR, PROJECT_ID_DOUBLE_DOLLAR)),
                                    new Document().append(EQUAL,
                                            Arrays.asList(JIRA_PROJECT_ID_DOLLAR, JIRA_PROJECT_ID_DOUBLE_DOLLAR)),
                                    new Document().append(EQUAL, Arrays.asList(STATE_DOLLAR_SIGN, ACTIVE)),
                                    new Document().append(EQUAL, Arrays.asList(SPRINT_ID_DOLLAR_SIGN, sprintId)),
                                    new Document().append(IN,
                                            Arrays.asList(BOARD_ID_DOLLAR, Collections.singletonList(boardId)))))))))
                    .append(AS, SPRINT_DATA)),
                new Document().append(UNWIND, SPRINT_DATA_DOLLAR),
                new Document().append(LOOKUP, new Document().append(FROM, STORIES)
                    .append(LET,
                            new Document().append(JIRAPROJECTID, JIRA_PROJECT_ID_DOLLAR)
                                .append(PROJECT__ID, PROJECT_ID_DOLLAR))
                    .append(PIPELINE, Arrays.asList(
                            new Document().append(MATCH, new Document()
                                .append(EXPRESSION, new Document().append(AND, Arrays.asList(
                                        new Document().append(EQUAL,
                                                Arrays.asList(PROJECT_ID_DOLLAR, PROJECT_ID_DOUBLE_DOLLAR)),
                                        new Document().append(EQUAL,
                                                Arrays.asList(JIRA_PROJECT_ID_DOLLAR, JIRA_PROJECT_ID_DOUBLE_DOLLAR)),
                                        new Document().append(EQUAL, Arrays.asList(SPRINT_ID_DOLLAR_SIGN, sprintId)),
                                        new Document().append(IN,
                                                Arrays.asList(BOARD_ID_DOLLAR, Collections.singletonList(boardId))),
                                        new Document().append(IN,
                                                Arrays.asList(TYPE_DOLLAR_SIGN,
                                                        jiraRulesHelper.getBugTypesByProjectIdInList(
                                                                new ObjectId(projectId)))))))),
                            new Document().append(GROUP, new Document().append(ID, ONE_DOUBLE)
                                .append(TOTAL, new Document().append(SUM, ONE_DOUBLE))
                                .append(TOTAL_COMPLETED, new Document()
                                    .append(SUM, new Document().append(COND, Arrays.asList(
                                            new Document().append(IN, Arrays.asList(STATE_DOLLAR_SIGN, ListUtils.union(
                                                    jiraRulesHelper.getDefinitionOfDoneinList(new ObjectId(projectId)),
                                                    jiraRulesHelper
                                                        .getDefinitionOfAcceptedinList(new ObjectId(projectId))))),
                                            ONE_DOUBLE, ZERO_DOUBLE))))
                                .append(TOTAL_OPEN,
                                        new Document().append(SUM,
                                                new Document().append(COND, Arrays.asList(
                                                        new Document().append(IN,
                                                                Arrays.asList(STATE_DOLLAR_SIGN,
                                                                        jiraRulesHelper.getDefinitionOfToDoInList(
                                                                                new ObjectId(projectId)))),
                                                        ONE_DOUBLE, ZERO_DOUBLE))))
                                .append(TOTAL_IN_PROGRESS,
                                        new Document().append(SUM,
                                                new Document().append(COND, Arrays.asList(
                                                        new Document().append(IN,
                                                                Arrays.asList(STATE_DOLLAR_SIGN,
                                                                        jiraRulesHelper.getInProgressByProjectIdInList(
                                                                                new ObjectId(projectId)))),
                                                        ONE_DOUBLE, ZERO_DOUBLE)))))))
                    .append(AS, BUGS)),
                new Document().append(PROJECT,
                        new Document().append(PsrServiceConstants.JIRA_PROJECT_ID, PROJECT_ID_SPRINT_DATA_DOLLAR)
                            .append(BOARD_ID, BOARD_ID_SPRINT_DATA_DOLLAR)
                            .append(SPRINT_ID, SPRINT_ID_SPRINT_DATA_DOLLAR)
                            .append(PROJECT__ID, ISC_PROJECT_ID_SPRINT_DATA_DOLLAR)
                            .append(NAME, NAME_SPRINT_DATA_DOLLAR)
                            .append(START_DATE, START_DATE_SPRINT_DATA_DOLLAR)
                            .append(END_DATE, END_DATE_SPRINTDATA_ENDDATE)
                            .append(STATE, STATE_SPRINT_DATA_DOLLAR)
                            .append(JIRA_CHANGE_DATE, JIRA_CHANGE_DATE_SPRINT_DATA_DOLLAR)
                            .append(IS_DELETED, IS_DELETED_SPRINT_DATA_DOLLAR)
                            .append(CREATED_DATE, CREATED_DATE_SPRINT_DATA_DOLLAR)
                            .append(BUGS, ONE_DOUBLE)));
        try {
            return getDocuments(JIRA_CONFIGURATION, pipeline, SprintWithBugsVo.class).get(ZERO);
        }
        catch (MongoException e) {
            log.error(e.getMessage());
            throw new ServiceException(MONGO_EXCEPTION_MESSAGE);
        }
    }

    public SprintWithBlockersVo getBlockers(Integer sprintId, String projectId, Integer boardId) {
        List<? extends Bson> pipeline = Arrays.asList(
                new Document().append(MATCH, new Document().append(PROJECT__ID, new ObjectId(projectId))),
                new Document().append(PROJECT,
                        new Document().append(PROJECT__ID, ONE_DOUBLE)
                            .append(JIRAPROJECTID, new Document().append(TO_INT_DOLLAR, JIRA_PROJECT_ID_DOLLAR))
                            .append(ID, ZERO_DOUBLE)),
                new Document().append(LOOKUP, new Document().append(FROM, SPRINTS)
                    .append(LET,
                            new Document().append(JIRAPROJECTID, JIRA_PROJECT_ID_DOLLAR)
                                .append(PROJECT__ID, PROJECT_ID_DOLLAR))
                    .append(PIPELINE, Collections.singletonList(new Document().append(MATCH,
                            new Document().append(EXPRESSION, new Document().append(AND, Arrays.asList(
                                    new Document().append(EQUAL,
                                            Arrays.asList(PROJECT_ID_DOLLAR, PROJECT_ID_DOUBLE_DOLLAR)),
                                    new Document().append(EQUAL,
                                            Arrays.asList(JIRA_PROJECT_ID_DOLLAR, JIRA_PROJECT_ID_DOUBLE_DOLLAR)),
                                    new Document().append(EQUAL, Arrays.asList(STATE_DOLLAR_SIGN, ACTIVE)),
                                    new Document().append(EQUAL, Arrays.asList(SPRINT_ID_DOLLAR_SIGN, sprintId)),
                                    new Document().append(IN,
                                            Arrays.asList(BOARD_ID_DOLLAR, Collections.singletonList(boardId)))))))))
                    .append(AS, SPRINT_DATA)),
                new Document().append(UNWIND, SPRINT_DATA_DOLLAR),
                new Document().append(LOOKUP, new Document().append(FROM, STORIES)
                    .append(LET,
                            new Document().append(JIRAPROJECTID, JIRA_PROJECT_ID_DOLLAR)
                                .append(PROJECT__ID, PROJECT_ID_DOLLAR))
                    .append(PIPELINE, Collections.singletonList(new Document()
                        .append(MATCH, new Document().append(EXPRESSION, new Document().append(AND, Arrays.asList(
                                new Document().append(EQUAL,
                                        Arrays.asList(PROJECT_ID_DOLLAR, PROJECT_ID_DOUBLE_DOLLAR)),
                                new Document().append(EQUAL,
                                        Arrays.asList(JIRA_PROJECT_ID_DOLLAR, JIRA_PROJECT_ID_DOUBLE_DOLLAR)),
                                new Document().append(EQUAL, Arrays.asList(SPRINT_ID_DOLLAR_SIGN, sprintId)),
                                new Document().append(IN,
                                        Arrays.asList(BOARD_ID_DOLLAR, Collections.singletonList(boardId))),
                                new Document().append(IN,
                                        Arrays.asList(STATUS_DOLLAR,
                                                jiraRulesHelper.getDefinitionOfBlockerInList(new ObjectId(projectId)))),
                                new Document().append(IN,
                                        Arrays.asList(TYPE_DOLLAR_SIGN,
                                                jiraRulesHelper.getStoryTaskBugTypesByProjectIdInList(
                                                        new ObjectId(projectId))))))))))
                    .append(AS, BLOCKERS)),
                new Document().append(PROJECT,
                        new Document().append(PsrServiceConstants.JIRA_PROJECT_ID, PROJECT_ID_SPRINT_DATA_DOLLAR)
                            .append(BOARD_ID, BOARD_ID_SPRINT_DATA_DOLLAR)
                            .append(SPRINT_ID, SPRINT_ID_SPRINT_DATA_DOLLAR)
                            .append(PROJECT__ID, ISC_PROJECT_ID_SPRINT_DATA_DOLLAR)
                            .append(NAME, NAME_SPRINT_DATA_DOLLAR)
                            .append(START_DATE, START_DATE_SPRINT_DATA_DOLLAR)
                            .append(END_DATE, END_DATE_SPRINTDATA_ENDDATE)
                            .append(STATE, STATE_SPRINT_DATA_DOLLAR)
                            .append(JIRA_CHANGE_DATE, JIRA_CHANGE_DATE_SPRINT_DATA_DOLLAR)
                            .append(IS_DELETED, IS_DELETED_SPRINT_DATA_DOLLAR)
                            .append(CREATED_DATE, CREATED_DATE_SPRINT_DATA_DOLLAR)
                            .append(BLOCKERS, ONE_DOUBLE)));

        try {
            return getDocuments(JIRA_CONFIGURATION, pipeline, SprintWithBlockersVo.class).get(ZERO);
        }
        catch (MongoException e) {
            log.error(e.getMessage());
            throw new ServiceException(MONGO_EXCEPTION_MESSAGE);
        }
    }

    public SprintLoggedVsAcceptedBugsVo getLoggedVsAcceptedBugs(Integer sprintId, String projectId, Integer boardId) {
        List<? extends Bson> pipeline = Arrays.asList(
                new Document().append(MATCH, new Document().append(PROJECT__ID, new ObjectId(projectId))),
                new Document().append(PROJECT,
                        new Document().append(PROJECT__ID, ONE_DOUBLE)
                            .append(JIRAPROJECTID, new Document().append(TO_INT_DOLLAR, JIRA_PROJECT_ID_DOLLAR))
                            .append(ID, ZERO_DOUBLE)),
                new Document().append(LOOKUP, new Document().append(FROM, SPRINTS)
                    .append(LET,
                            new Document().append(JIRAPROJECTID, JIRA_PROJECT_ID_DOLLAR)
                                .append(PROJECT__ID, PROJECT_ID_DOLLAR))
                    .append(PIPELINE, Collections.singletonList(new Document().append(MATCH,
                            new Document().append(EXPRESSION, new Document().append(AND, Arrays.asList(
                                    new Document().append(EQUAL,
                                            Arrays.asList(PROJECT_ID_DOLLAR, PROJECT_ID_DOUBLE_DOLLAR)),
                                    new Document().append(EQUAL,
                                            Arrays.asList(JIRA_PROJECT_ID_DOLLAR, JIRA_PROJECT_ID_DOUBLE_DOLLAR)),
                                    new Document().append(EQUAL, Arrays.asList(STATE_DOLLAR_SIGN, ACTIVE)),
                                    new Document().append(EQUAL, Arrays.asList(SPRINT_ID_DOLLAR_SIGN, sprintId)),
                                    new Document().append(IN,
                                            Arrays.asList(BOARD_ID_DOLLAR, Collections.singletonList(boardId)))))))))
                    .append(AS, SPRINT_DATA)),
                new Document().append(UNWIND, SPRINT_DATA_DOLLAR),
                new Document().append(LOOKUP, new Document().append(FROM, STORIES)
                    .append(LET,
                            new Document().append(JIRAPROJECTID, JIRA_PROJECT_ID_DOLLAR)
                                .append(PROJECT__ID, PROJECT_ID_DOLLAR))
                    .append(PIPELINE, Arrays.asList(
                            new Document().append(MATCH, new Document()
                                .append(EXPRESSION, new Document().append(AND, Arrays.asList(
                                        new Document().append(EQUAL,
                                                Arrays.asList(PROJECT_ID_DOLLAR, PROJECT_ID_DOUBLE_DOLLAR)),
                                        new Document().append(EQUAL,
                                                Arrays.asList(JIRA_PROJECT_ID_DOLLAR, JIRA_PROJECT_ID_DOUBLE_DOLLAR)),
                                        new Document().append(EQUAL, Arrays.asList(SPRINT_ID_DOLLAR_SIGN, sprintId)),
                                        new Document().append(IN,
                                                Arrays.asList(BOARD_ID_DOLLAR, Collections.singletonList(boardId))),
                                        new Document().append(IN,
                                                Arrays.asList(TYPE_DOLLAR_SIGN,
                                                        jiraRulesHelper.getBugTypesByProjectIdInList(
                                                                new ObjectId(projectId)))))))),
                            new Document().append(GROUP,
                                    new Document().append(ID, ONE_DOUBLE)
                                        .append("totalLogged", new Document().append(SUM, ONE_DOUBLE))
                                        .append(TOTAL_COMPLETED,
                                                new Document().append(SUM, new Document().append(COND, Arrays.asList(
                                                        new Document().append(IN,
                                                                Arrays.asList(STATE_DOLLAR_SIGN,
                                                                        jiraRulesHelper.getDefinitionOfAcceptedinList(
                                                                                new ObjectId(projectId)))),
                                                        ONE_DOUBLE, ZERO_DOUBLE)))))))
                    .append(AS, BUGS)),
                new Document().append(PROJECT,
                        new Document().append(PsrServiceConstants.JIRA_PROJECT_ID, PROJECT_ID_SPRINT_DATA_DOLLAR)
                            .append(BOARD_ID, BOARD_ID_SPRINT_DATA_DOLLAR)
                            .append(SPRINT_ID, SPRINT_ID_SPRINT_DATA_DOLLAR)
                            .append(PROJECT__ID, ISC_PROJECT_ID_SPRINT_DATA_DOLLAR)
                            .append(NAME, NAME_SPRINT_DATA_DOLLAR)
                            .append(START_DATE, START_DATE_SPRINT_DATA_DOLLAR)
                            .append(END_DATE, END_DATE_SPRINTDATA_ENDDATE)
                            .append(STATE, STATE_SPRINT_DATA_DOLLAR)
                            .append(JIRA_CHANGE_DATE, JIRA_CHANGE_DATE_SPRINT_DATA_DOLLAR)
                            .append(IS_DELETED, IS_DELETED_SPRINT_DATA_DOLLAR)
                            .append(CREATED_DATE, CREATED_DATE_SPRINT_DATA_DOLLAR)
                            .append(BUGS, ONE_DOUBLE)));
        try {
            return getDocuments(JIRA_CONFIGURATION, pipeline, SprintLoggedVsAcceptedBugsVo.class).get(ZERO);
        }
        catch (MongoException e) {
            log.error(e.getMessage());
            throw new ServiceException(MONGO_EXCEPTION_MESSAGE);
        }
    }

    public SprintWithStoriesVo getSprintVariance(Integer sprintId, Integer boardId, String projectId) {
        List<? extends Bson> pipeline = Arrays.asList(
                new Document().append(MATCH, new Document().append(PROJECT__ID, new ObjectId(projectId))),
                new Document().append(PROJECT,
                        new Document().append(PROJECT__ID, ONE_DOUBLE)
                            .append(JIRAPROJECTID, new Document().append(TO_INT_DOLLAR, JIRA_PROJECT_ID_DOLLAR))
                            .append(ID, ZERO_DOUBLE)),
                new Document().append(LOOKUP, new Document().append(FROM, SPRINTS)
                    .append(LET,
                            new Document().append(JIRAPROJECTID, JIRA_PROJECT_ID_DOLLAR)
                                .append(PROJECT__ID, PROJECT_ID_DOLLAR))
                    .append(PIPELINE, Collections.singletonList(new Document().append(MATCH,
                            new Document().append(EXPRESSION, new Document().append(AND, Arrays.asList(
                                    new Document().append(EQUAL,
                                            Arrays.asList(PROJECT_ID_DOLLAR, PROJECT_ID_DOUBLE_DOLLAR)),
                                    new Document().append(EQUAL,
                                            Arrays.asList(JIRA_PROJECT_ID_DOLLAR, JIRA_PROJECT_ID_DOUBLE_DOLLAR)),
                                    new Document().append(EQUAL, Arrays.asList(STATE_DOLLAR_SIGN, ACTIVE)),
                                    new Document().append(EQUAL, Arrays.asList(SPRINT_ID_DOLLAR_SIGN, sprintId)),
                                    new Document().append(IN,
                                            Arrays.asList(BOARD_ID_DOLLAR, Collections.singletonList(boardId)))))))))
                    .append(AS, SPRINT_DATA)),
                new Document().append(UNWIND, SPRINT_DATA_DOLLAR),
                new Document().append(LOOKUP, new Document().append(FROM, STORIES)
                    .append(LET,
                            new Document().append(JIRAPROJECTID, JIRA_PROJECT_ID_DOLLAR)
                                .append(PROJECT__ID, PROJECT_ID_DOLLAR))
                    .append(PIPELINE, Collections.singletonList(new Document().append(MATCH,
                            new Document().append(EXPRESSION, new Document().append(AND, Arrays.asList(
                                    new Document().append(EQUAL,
                                            Arrays.asList(PROJECT_ID_DOLLAR, PROJECT_ID_DOUBLE_DOLLAR)),
                                    new Document().append(EQUAL,
                                            Arrays.asList(JIRA_PROJECT_ID_DOLLAR, JIRA_PROJECT_ID_DOUBLE_DOLLAR)),
                                    new Document().append(EQUAL, Arrays.asList(SPRINT_ID_DOLLAR_SIGN, sprintId)),
                                    new Document().append(IN,
                                            Arrays.asList(BOARD_ID_DOLLAR, Collections.singletonList(boardId)))))))))
                    .append(AS, SPRINT_TICKETS)),
                new Document().append(PROJECT,
                        new Document().append(PsrServiceConstants.JIRA_PROJECT_ID, PROJECT_ID_SPRINT_DATA_DOLLAR)
                            .append(BOARD_ID, BOARD_ID_SPRINT_DATA_DOLLAR)
                            .append(SPRINT_ID, SPRINT_ID_SPRINT_DATA_DOLLAR)
                            .append(PROJECT__ID, ISC_PROJECT_ID_SPRINT_DATA_DOLLAR)
                            .append(NAME, NAME_SPRINT_DATA_DOLLAR)
                            .append(START_DATE, START_DATE_SPRINT_DATA_DOLLAR)
                            .append(END_DATE, END_DATE_SPRINTDATA_ENDDATE)
                            .append(STATE, STATE_SPRINT_DATA_DOLLAR)
                            .append(JIRA_CHANGE_DATE, JIRA_CHANGE_DATE_SPRINT_DATA_DOLLAR)
                            .append(IS_DELETED, IS_DELETED_SPRINT_DATA_DOLLAR)
                            .append(CREATED_DATE, CREATED_DATE_SPRINT_DATA_DOLLAR)
                            .append(SPRINT_TICKETS, ONE_DOUBLE)));
        try {
            return getDocuments(JIRA_CONFIGURATION, pipeline, SprintWithStoriesVo.class).get(ZERO);
        }
        catch (MongoException e) {
            log.error(e.getMessage());
            throw new ServiceException(MONGO_EXCEPTION_MESSAGE);
        }
    }

    public SprintWithOldTicketsVo getOverallVariance(Integer sprintId, Integer boardId, String projectId) {
        List<? extends Bson> pipeline = Arrays.asList(
                new Document().append(MATCH, new Document().append(PROJECT__ID, new ObjectId(projectId))),
                new Document().append(PROJECT,
                        new Document().append(PROJECT__ID, ONE_DOUBLE)
                            .append(JIRAPROJECTID, new Document().append(TO_INT_DOLLAR, JIRA_PROJECT_ID_DOLLAR))
                            .append(ID, ZERO_DOUBLE)),
                new Document().append(LOOKUP, new Document().append(FROM, SPRINTS)
                    .append(LET,
                            new Document().append(JIRAPROJECTID, JIRA_PROJECT_ID_DOLLAR)
                                .append(PROJECT__ID, PROJECT_ID_DOLLAR))
                    .append(PIPELINE, Collections.singletonList(new Document().append(MATCH,
                            new Document().append(EXPRESSION, new Document().append(AND, Arrays.asList(
                                    new Document().append(EQUAL,
                                            Arrays.asList(PROJECT_ID_DOLLAR, PROJECT_ID_DOUBLE_DOLLAR)),
                                    new Document().append(EQUAL,
                                            Arrays.asList(JIRA_PROJECT_ID_DOLLAR, JIRA_PROJECT_ID_DOUBLE_DOLLAR)),
                                    new Document().append(EQUAL, Arrays.asList(STATE_DOLLAR_SIGN, ACTIVE)),
                                    new Document().append(EQUAL, Arrays.asList(SPRINT_ID_DOLLAR_SIGN, sprintId)),
                                    new Document().append(IN,
                                            Arrays.asList(BOARD_ID_DOLLAR, Collections.singletonList(boardId)))))))))
                    .append(AS, SPRINT_DATA)),
                new Document().append(UNWIND, SPRINT_DATA_DOLLAR),
                new Document().append(LOOKUP,
                        new Document().append(FROM, STORIES)
                            .append(LET,
                                    new Document().append(JIRAPROJECTID, JIRA_PROJECT_ID_DOLLAR)
                                        .append(PROJECT__ID, PROJECT_ID_DOLLAR))
                            .append(PIPELINE,
                                    Arrays.asList(
                                            new Document()
                                                .append(MATCH, new Document()
                                                    .append(EXPRESSION,
                                                            new Document().append(AND, Arrays.asList(
                                                                    new Document().append(EQUAL,
                                                                            Arrays.asList(PROJECT_ID_DOLLAR,
                                                                                    PROJECT_ID_DOUBLE_DOLLAR)),
                                                                    new Document().append(EQUAL,
                                                                            Arrays.asList(JIRA_PROJECT_ID_DOLLAR,
                                                                                    JIRA_PROJECT_ID_DOUBLE_DOLLAR)))))
                                                    .append(SPRINT_ID, new Document().append(NE, sprintId))
                                                    .append(BOARD_ID, boardId)),
                                            new Document().append(PROJECT,
                                                    new Document().append("estimatedTime", ONE_DOUBLE))))
                            .append(AS, OLD_TICKETS)),
                new Document().append(PROJECT,
                        new Document().append(PsrServiceConstants.JIRA_PROJECT_ID, PROJECT_ID_SPRINT_DATA_DOLLAR)
                            .append(BOARD_ID, BOARD_ID_SPRINT_DATA_DOLLAR)
                            .append(SPRINT_ID, SPRINT_ID_SPRINT_DATA_DOLLAR)
                            .append(PROJECT__ID, ISC_PROJECT_ID_SPRINT_DATA_DOLLAR)
                            .append(NAME, NAME_SPRINT_DATA_DOLLAR)
                            .append(START_DATE, START_DATE_SPRINT_DATA_DOLLAR)
                            .append(END_DATE, END_DATE_SPRINTDATA_ENDDATE)
                            .append(STATE, STATE_SPRINT_DATA_DOLLAR)
                            .append(JIRA_CHANGE_DATE, JIRA_CHANGE_DATE_SPRINT_DATA_DOLLAR)
                            .append(IS_DELETED, IS_DELETED_SPRINT_DATA_DOLLAR)
                            .append(CREATED_DATE, CREATED_DATE_SPRINT_DATA_DOLLAR)
                            .append(OLD_TICKETS, ONE_DOUBLE)));
        try {
            return getDocuments(JIRA_CONFIGURATION, pipeline, SprintWithOldTicketsVo.class).get(ZERO);
        }
        catch (MongoException e) {
            log.error(e.getMessage());
            throw new ServiceException(MONGO_EXCEPTION_MESSAGE);
        }
    }

    public List<SprintAcceptedVsDeliveredVo> getAcceptedVsDelivered(Integer boardId, String projectId) {
        List<? extends Bson> pipeline = Arrays.asList(
                new Document().append(MATCH, new Document().append(PROJECT__ID, new ObjectId(projectId))),
                new Document().append(PROJECT,
                        new Document().append(PROJECT__ID, ONE_DOUBLE)
                            .append(JIRAPROJECTID, new Document().append(TO_INT_DOLLAR, JIRA_PROJECT_ID_DOLLAR))
                            .append(ID, ZERO_DOUBLE)),
                new Document().append(LOOKUP, new Document().append(FROM, SPRINTS)
                    .append(LET,
                            new Document().append(PROJECT__ID, PROJECT_ID_DOLLAR)
                                .append(JIRA_PROJECT_ID, JIRA_PROJECT_ID_DOLLAR))
                    .append(PIPELINE, Collections.singletonList(new Document().append(MATCH,
                            new Document().append(EXPRESSION, new Document().append(AND, Arrays.asList(
                                    new Document().append(EQUAL,
                                            Arrays.asList(PROJECT_ID_DOLLAR, PROJECT_ID_DOUBLE_DOLLAR)),
                                    new Document().append(EQUAL,
                                            Arrays.asList(JIRA_PROJECT_ID_DOLLAR, JIRA_PROJECT_ID_DOC)),
                                    new Document().append(NE, Arrays.asList(STATE_DOLLAR_SIGN, "future")),
                                    new Document().append(NE, Arrays.asList(SPRINT_ID_DOLLAR_SIGN, new BsonNull())),
                                    new Document().append(IN,
                                            Arrays.asList(BOARD_ID_DOLLAR, Collections.singletonList(boardId)))))))))
                    .append(AS, "data")),
                new Document().append(UNWIND, "$data"),
                new Document()
                    .append(LOOKUP, new Document().append(FROM, STORIES)
                        .append(LET,
                                new Document().append(PROJECT__ID, PROJECT_ID_DOLLAR)
                                    .append(JIRA_PROJECT_ID, JIRA_PROJECT_ID_DOLLAR)
                                    .append("sprint_id", SPRINT_ID_DATA_DOLLAR))
                        .append(PIPELINE, Arrays.asList(
                                new Document().append(MATCH,
                                        new Document()
                                            .append(EXPRESSION, new Document().append(
                                                    AND,
                                                    Arrays.asList(
                                                            new Document().append(EQUAL,
                                                                    Arrays.asList(SPRINT_ID_DOLLAR_SIGN,
                                                                            "$$sprint_id")),
                                                            new Document().append(
                                                                    EQUAL,
                                                                    Arrays.asList(PROJECT_ID_DOLLAR,
                                                                            PROJECT_ID_DOUBLE_DOLLAR)),
                                                            new Document().append(EQUAL,
                                                                    Arrays.asList(JIRA_PROJECT_ID_DOLLAR,
                                                                            JIRA_PROJECT_ID_DOC)))))
                                            .append(STATE,
                                                    new Document().append(IN, ListUtils.union(
                                                            jiraRulesHelper
                                                                .getDefinitionOfAcceptedinList(new ObjectId(projectId)),
                                                            jiraRulesHelper
                                                                .getDefinitionOfDoneinList(new ObjectId(projectId)))))
                                            .append("type", new Document().append(
                                                    IN,
                                                    jiraRulesHelper.getStoryPointsCalculationIssuesByProjectIdInList(
                                                            new ObjectId(projectId))))),
                                new Document().append(GROUP, new Document().append(ID, SPRINT_ID_DOLLAR_SIGN)
                                    .append(TOTAL, new Document().append(SUM, ESTIMATE_DOLLAR))
                                    .append("totalDone",
                                            new Document().append(SUM, new Document().append(COND, Arrays.asList(
                                                    new Document().append(IN,
                                                            Arrays.asList(STATE_DOLLAR_SIGN, ListUtils.union(
                                                                    jiraRulesHelper.getDefinitionOfDoneinList(
                                                                            new ObjectId(projectId)),
                                                                    jiraRulesHelper.getDefinitionOfAcceptedinList(
                                                                            new ObjectId(projectId))))),
                                                    ESTIMATE_DOLLAR, ZERO_DOUBLE))))
                                    .append("totalClosed",
                                            new Document().append(SUM, new Document().append(COND, Arrays.asList(
                                                    new Document().append(IN,
                                                            Arrays.asList(STATE_DOLLAR_SIGN,
                                                                    jiraRulesHelper.getDefinitionOfAcceptedinList(
                                                                            new ObjectId(projectId)))),
                                                    ESTIMATE_DOLLAR, ZERO_DOUBLE)))))))
                        .append(AS, STORIES)),
                new Document().append("$sort", new Document().append("data.startDate", -ONE_DOUBLE)),
                new Document().append("$limit", 10.0),
                new Document().append(PROJECT,
                        new Document().append(PsrServiceConstants.JIRA_PROJECT_ID, "$data.jiraProjectId")
                            .append(BOARD_ID, BOARD_ID_DATA_DOLLAR)
                            .append(SPRINT_ID, SPRINT_ID_DATA_DOLLAR)
                            .append(PROJECT__ID, ISC_PROJECT_ID_DATA_DOLLAR)
                            .append(NAME, NAME_DATA_DOLLAR)
                            .append(START_DATE, START_DATE_DATA_DOLLAR)
                            .append(END_DATE, END_DATE_DATA_DOLLAR)
                            .append(STATE, STATE_DATA_DOLLAR)
                            .append(JIRA_CHANGE_DATE, "$data.jiraChangeDate")
                            .append(IS_DELETED, "$data.isDeleted")
                            .append(CREATED_DATE, "$data.createdDate")
                            .append(STORIES, ONE_DOUBLE)));

        try {
            return getDocuments(JIRA_CONFIGURATION, pipeline, SprintAcceptedVsDeliveredVo.class);
        }
        catch (MongoException e) {
            log.error(e.getMessage());
            throw new ServiceException(MONGO_EXCEPTION_MESSAGE);
        }
    }

    public SprintMemberStatusVo getMemberStatus(Integer sprintId, Integer boardId, String projectId) {
        List<? extends Bson> pipeline = Arrays.asList(
                new Document().append(MATCH, new Document().append(PROJECT__ID, new ObjectId(projectId))),
                new Document().append(PROJECT,
                        new Document().append(PROJECT__ID, ONE_DOUBLE)
                            .append(JIRAPROJECTID, new Document().append(TO_INT_DOLLAR, JIRA_PROJECT_ID_DOLLAR))
                            .append(ID, ZERO_DOUBLE)),
                new Document().append(LOOKUP, new Document().append(FROM, SPRINTS)
                    .append(LET,
                            new Document().append(JIRAPROJECTID, JIRA_PROJECT_ID_DOLLAR)
                                .append(PROJECT__ID, PROJECT_ID_DOLLAR))
                    .append(PIPELINE, Collections.singletonList(new Document().append(MATCH,
                            new Document().append(EXPRESSION, new Document().append(AND, Arrays.asList(
                                    new Document().append(EQUAL,
                                            Arrays.asList(PROJECT_ID_DOLLAR, PROJECT_ID_DOUBLE_DOLLAR)),
                                    new Document().append(EQUAL,
                                            Arrays.asList(JIRA_PROJECT_ID_DOLLAR, JIRA_PROJECT_ID_DOUBLE_DOLLAR)),
                                    new Document().append(EQUAL, Arrays.asList(STATE_DOLLAR_SIGN, ACTIVE)),
                                    new Document().append(EQUAL, Arrays.asList(SPRINT_ID_DOLLAR_SIGN, sprintId)),
                                    new Document().append(IN,
                                            Arrays.asList(BOARD_ID_DOLLAR, Collections.singletonList(boardId)))))))))
                    .append(AS, SPRINT_DATA)),
                new Document().append(UNWIND, SPRINT_DATA_DOLLAR),
                new Document().append(LOOKUP, new Document().append(FROM, STORIES)
                    .append(LET,
                            new Document().append(JIRAPROJECTID, JIRA_PROJECT_ID_DOLLAR)
                                .append(PROJECT__ID, PROJECT_ID_DOLLAR))
                    .append(PIPELINE, Collections.singletonList(new Document().append(MATCH,
                            new Document().append(EXPRESSION, new Document().append(AND, Arrays.asList(
                                    new Document().append(EQUAL,
                                            Arrays.asList(PROJECT_ID_DOLLAR, PROJECT_ID_DOUBLE_DOLLAR)),
                                    new Document().append(EQUAL,
                                            Arrays.asList(JIRA_PROJECT_ID_DOLLAR, JIRA_PROJECT_ID_DOUBLE_DOLLAR)),
                                    new Document().append(EQUAL, Arrays.asList(SPRINT_ID_DOLLAR_SIGN, sprintId)),
                                    new Document().append(IN,
                                            Arrays.asList(BOARD_ID_DOLLAR, Collections.singletonList(boardId))),
                                    new Document().append(IN,
                                            Arrays.asList(STATE_DOLLAR_SIGN,
                                                    jiraRulesHelper
                                                        .getAllStatesByProjectIdInList(new ObjectId(projectId))))))))))
                    .append(AS, STORIES)),
                new Document().append(PROJECT,
                        new Document().append(PsrServiceConstants.JIRA_PROJECT_ID, PROJECT_ID_SPRINT_DATA_DOLLAR)
                            .append(BOARD_ID, BOARD_ID_SPRINT_DATA_DOLLAR)
                            .append(SPRINT_ID, SPRINT_ID_SPRINT_DATA_DOLLAR)
                            .append(PROJECT__ID, ISC_PROJECT_ID_SPRINT_DATA_DOLLAR)
                            .append(NAME, NAME_SPRINT_DATA_DOLLAR)
                            .append(START_DATE, START_DATE_SPRINT_DATA_DOLLAR)
                            .append(END_DATE, END_DATE_SPRINTDATA_ENDDATE)
                            .append(STATE, STATE_SPRINT_DATA_DOLLAR)
                            .append(JIRA_CHANGE_DATE, JIRA_CHANGE_DATE_SPRINT_DATA_DOLLAR)
                            .append(IS_DELETED, IS_DELETED_SPRINT_DATA_DOLLAR)
                            .append(CREATED_DATE, CREATED_DATE_SPRINT_DATA_DOLLAR)
                            .append(STORIES, ONE_DOUBLE)));
        try {
            return getDocuments(JIRA_CONFIGURATION, pipeline, SprintMemberStatusVo.class).get(ZERO);
        }
        catch (MongoException e) {
            log.error(e.getMessage());
            throw new ServiceException(MONGO_EXCEPTION_MESSAGE);
        }
    }

    public SprintMemberStatusByIdVo getMemberStatusByMemberId(Integer sprintId, String memberId, Integer boardId,
            String projectId) {
        List<? extends Bson> pipeline = Arrays.asList(
                new Document().append(MATCH, new Document().append(PROJECT__ID, new ObjectId(projectId))),
                new Document().append(PROJECT,
                        new Document().append(PROJECT__ID, ONE_DOUBLE)
                            .append(JIRAPROJECTID, new Document().append(TO_INT_DOLLAR, JIRA_PROJECT_ID_DOLLAR))
                            .append(ID, ZERO_DOUBLE)),
                new Document().append(LOOKUP, new Document().append(FROM, SPRINTS)
                    .append(LET,
                            new Document().append(JIRAPROJECTID, JIRA_PROJECT_ID_DOLLAR)
                                .append(PROJECT__ID, PROJECT_ID_DOLLAR))
                    .append(PIPELINE, Collections.singletonList(new Document().append(MATCH,
                            new Document().append(EXPRESSION, new Document().append(AND, Arrays.asList(
                                    new Document().append(EQUAL,
                                            Arrays.asList(PROJECT_ID_DOLLAR, PROJECT_ID_DOUBLE_DOLLAR)),
                                    new Document().append(EQUAL,
                                            Arrays.asList(JIRA_PROJECT_ID_DOLLAR, JIRA_PROJECT_ID_DOUBLE_DOLLAR)),
                                    new Document().append(EQUAL, Arrays.asList(STATE_DOLLAR_SIGN, ACTIVE)),
                                    new Document().append(EQUAL, Arrays.asList(SPRINT_ID_DOLLAR_SIGN, sprintId)),
                                    new Document().append(IN,
                                            Arrays.asList(BOARD_ID_DOLLAR, Collections.singletonList(boardId)))))))))
                    .append(AS, SPRINT_DATA)),
                new Document().append(UNWIND, SPRINT_DATA_DOLLAR),
                new Document()
                    .append(LOOKUP,
                            new Document().append(FROM, STORIES)
                                .append(LET,
                                        new Document().append(JIRAPROJECTID, JIRA_PROJECT_ID_DOLLAR)
                                            .append(PROJECT__ID, PROJECT_ID_DOLLAR))
                                .append(PIPELINE,
                                        Collections.singletonList(
                                                new Document().append(MATCH,
                                                        new Document()
                                                            .append(EXPRESSION, new Document()
                                                                .append(AND, Arrays.asList(
                                                                        new Document().append(EQUAL,
                                                                                Arrays.asList(PROJECT_ID_DOLLAR,
                                                                                        PROJECT_ID_DOUBLE_DOLLAR)),
                                                                        new Document().append(EQUAL,
                                                                                Arrays.asList(JIRA_PROJECT_ID_DOLLAR,
                                                                                        JIRA_PROJECT_ID_DOUBLE_DOLLAR)),
                                                                        new Document()
                                                                            .append(EQUAL,
                                                                                    Arrays.asList(SPRINT_ID_DOLLAR_SIGN,
                                                                                            sprintId)),
                                                                        new Document().append(
                                                                                IN,
                                                                                Arrays.asList(
                                                                                        BOARD_ID_DOLLAR,
                                                                                        Collections
                                                                                            .singletonList(boardId))),
                                                                        new Document().append(IN, Arrays
                                                                            .asList(STATE_DOLLAR_SIGN, jiraRulesHelper
                                                                                .getAllStatesByProjectIdInList(
                                                                                        new ObjectId(projectId)))))))
                                                            .append("ownerIds", memberId))))
                                .append(AS, "tasks")),
                new Document().append(PROJECT,
                        new Document().append(PsrServiceConstants.JIRA_PROJECT_ID, PROJECT_ID_SPRINT_DATA_DOLLAR)
                            .append(BOARD_ID, BOARD_ID_SPRINT_DATA_DOLLAR)
                            .append(SPRINT_ID, SPRINT_ID_SPRINT_DATA_DOLLAR)
                            .append(PROJECT__ID, ISC_PROJECT_ID_SPRINT_DATA_DOLLAR)
                            .append(NAME, NAME_SPRINT_DATA_DOLLAR)
                            .append(START_DATE, START_DATE_SPRINT_DATA_DOLLAR)
                            .append(END_DATE, END_DATE_SPRINTDATA_ENDDATE)
                            .append(STATE, STATE_SPRINT_DATA_DOLLAR)
                            .append(JIRA_CHANGE_DATE, JIRA_CHANGE_DATE_SPRINT_DATA_DOLLAR)
                            .append(IS_DELETED, IS_DELETED_SPRINT_DATA_DOLLAR)
                            .append(CREATED_DATE, CREATED_DATE_SPRINT_DATA_DOLLAR)
                            .append("tasks", ONE_DOUBLE)));
        try {
            return getDocuments(JIRA_CONFIGURATION, pipeline, SprintMemberStatusByIdVo.class).get(ZERO);
        }
        catch (MongoException e) {
            log.error(e.getMessage());
            throw new ServiceException(MONGO_EXCEPTION_MESSAGE);
        }
    }

    public SprintWithStoryReportVo getStoryReport(Integer sprintId, Integer boardId, String projectId) {
        List<? extends Bson> pipeline = Arrays.asList(
                new Document().append(MATCH, new Document().append(PROJECT__ID, new ObjectId(projectId))),
                new Document().append(PROJECT,
                        new Document().append(PROJECT__ID, ONE_DOUBLE)
                            .append(JIRAPROJECTID, new Document().append(TO_INT_DOLLAR, JIRA_PROJECT_ID_DOLLAR))
                            .append(ID, ZERO_DOUBLE)),
                new Document().append(LOOKUP, new Document().append(FROM, SPRINTS)
                    .append(LET,
                            new Document().append(PROJECT__ID, PROJECT_ID_DOLLAR)
                                .append(PsrServiceConstants.JIRA_PROJECT_ID, JIRA_PROJECT_ID_DOLLAR))
                    .append(PIPELINE, Collections.singletonList(new Document().append(MATCH,
                            new Document().append(EXPRESSION, new Document().append(AND, Arrays.asList(
                                    new Document().append(EQUAL,
                                            Arrays.asList(PROJECT_ID_DOLLAR, PROJECT_ID_DOUBLE_DOLLAR)),
                                    new Document().append(EQUAL,
                                            Arrays.asList(JIRA_PROJECT_ID_DOLLAR, JIRA_PROJECT_ID_DOUBLE_DOLLAR)),
                                    new Document().append(EQUAL, Arrays.asList(STATE_DOLLAR_SIGN, ACTIVE)),
                                    new Document().append(EQUAL, Arrays.asList(SPRINT_ID_DOLLAR_SIGN, sprintId)),
                                    new Document().append(IN,
                                            Arrays.asList(BOARD_ID_DOLLAR, Collections.singletonList(boardId)))))))))
                    .append(AS, "data")),
                new Document().append(UNWIND, "$data"),
                new Document().append(LOOKUP, new Document().append(FROM, STORIES)
                    .append(LET,
                            new Document().append(PROJECT__ID, PROJECT_ID_DOLLAR)
                                .append(PsrServiceConstants.JIRA_PROJECT_ID, JIRA_PROJECT_ID_DOLLAR)
                                .append("sprint_id", SPRINT_ID_DATA_DOLLAR))
                    .append(PIPELINE,
                            Arrays.asList(
                                    new Document()
                                        .append(MATCH, new Document()
                                            .append("type",
                                                    new Document().append(IN,
                                                            jiraRulesHelper.getStoryTaskTypesByProjectIdInList(
                                                                    new ObjectId(projectId))))
                                            .append(EXPRESSION,
                                                    new Document().append(AND, Arrays.asList(
                                                            new Document().append(EQUAL,
                                                                    Arrays.asList(SPRINT_ID_DOLLAR_SIGN,
                                                                            "$$sprint_id")),
                                                            new Document().append(
                                                                    EQUAL,
                                                                    Arrays.asList(PROJECT_ID_DOLLAR,
                                                                            PROJECT_ID_DOUBLE_DOLLAR)),
                                                            new Document().append(
                                                                    EQUAL,
                                                                    Arrays.asList(JIRA_PROJECT_ID_DOLLAR,
                                                                            JIRA_PROJECT_ID_DOUBLE_DOLLAR)))))
                                            .append(BOARD_ID,
                                                    new Document().append(IN, Collections.singletonList(boardId)))),
                                    new Document().append(GROUP, new Document().append(ID, ONE_DOUBLE)
                                        .append(TOTAL, new Document().append(SUM, ONE_DOUBLE))
                                        .append(TOTAL_COMPLETED,
                                                new Document().append(SUM, new Document().append(COND, Arrays.asList(
                                                        new Document().append(IN,
                                                                Arrays.asList(STATE_DOLLAR_SIGN, ListUtils.union(
                                                                        jiraRulesHelper.getDefinitionOfDoneinList(
                                                                                new ObjectId(projectId)),
                                                                        jiraRulesHelper.getDefinitionOfAcceptedinList(
                                                                                new ObjectId(projectId))))),
                                                        ONE_DOUBLE, ZERO_DOUBLE))))
                                        .append("totalDelivered",
                                                new Document().append(SUM, new Document().append(COND, Arrays.asList(
                                                        new Document().append(IN,
                                                                Arrays.asList(STATE_DOLLAR_SIGN,
                                                                        jiraRulesHelper.getDefinitionOfAcceptedinList(
                                                                                new ObjectId(projectId)))),
                                                        ONE_DOUBLE, ZERO_DOUBLE))))
                                        .append("totalBlocked",
                                                new Document().append(SUM, new Document().append(COND, Arrays.asList(
                                                        new Document().append(IN,
                                                                Arrays.asList(STATE_DOLLAR_SIGN,
                                                                        jiraRulesHelper.getDefinitionOfBlockerInList(
                                                                                new ObjectId(projectId)))),
                                                        ONE_DOUBLE, ZERO_DOUBLE))))
                                        .append("totalPlanned",
                                                new Document().append(SUM, new Document().append(COND, Arrays.asList(
                                                        new Document().append(IN,
                                                                Arrays.asList(STATE_DOLLAR_SIGN,
                                                                        jiraRulesHelper.getAllStatesByProjectIdInList(
                                                                                new ObjectId(projectId)))),
                                                        ONE_DOUBLE, ZERO_DOUBLE)))))))
                    .append(AS, STORY_REPORT)),
                new Document().append(PROJECT,
                        new Document().append(PsrServiceConstants.JIRA_PROJECT_ID, "$data.jiraProjectId")
                            .append(BOARD_ID, BOARD_ID_DATA_DOLLAR)
                            .append(SPRINT_ID, SPRINT_ID_DATA_DOLLAR)
                            .append(PROJECT__ID, ISC_PROJECT_ID_DATA_DOLLAR)
                            .append(NAME, NAME_DATA_DOLLAR)
                            .append(START_DATE, START_DATE_DATA_DOLLAR)
                            .append(END_DATE, END_DATE_DATA_DOLLAR)
                            .append(STATE, STATE_DATA_DOLLAR)
                            .append(JIRA_CHANGE_DATE, "$data.jiraChangeDate")
                            .append(IS_DELETED, "$data.isDeleted")
                            .append(CREATED_DATE, "$data.createdDate")
                            .append(STORY_REPORT, ONE_DOUBLE)));
        try {
            return getDocuments(JIRA_CONFIGURATION, pipeline, SprintWithStoryReportVo.class).get(ZERO);
        }
        catch (MongoException e) {
            log.error(e.getMessage());
            throw new ServiceException(MONGO_EXCEPTION_MESSAGE);
        }
    }

    public JiraConfigurationWiseCurrentSprintVo getSprintByJiraConfiguration(String projectId) {
        TypedAggregation<JiraConfiguration> typedAggregation = newAggregation(JiraConfiguration.class,
                match(Criteria.where(PROJECT__ID).is(new ObjectId(projectId))),
                new CustomAggregation(
                        String.format(QUERY_FOR_CURRENT_SPRINT_FROM_JIRA_CONFIGURATION.toString(), ACTIVE)),
                new CustomAggregation(String.format(QUERY_FOR_BOARDS_FROM_JIRA_CONFIGURATION.toString())));

        AggregationResults<JiraConfigurationWiseCurrentSprintVo> results = mongoTemplate.aggregate(typedAggregation,
                JiraConfigurationWiseCurrentSprintVo.class);
        return results.getUniqueMappedResult();
    }

}
