package com.apexon.compass.sonarservice.dao;

import static com.apexon.compass.constants.ClientDashboardServiceConstants.JSON_PROCESSING_EXCEPTION_MESSAGE;
import static com.apexon.compass.constants.ClientDashboardServiceConstants.JSON_PROCESSING_LOG_MESSAGE;
import static com.apexon.compass.constants.QueryConstants.*;
import static com.apexon.compass.constants.QueryConstants.PULL_REQUESTS;
import static com.apexon.compass.constants.QueryConstants.PULL_REQUESTS_COMMITS;
import static com.apexon.compass.constants.StrategyServiceConstants.AS;
import static com.apexon.compass.constants.StrategyServiceConstants.CODE_COMMITS;
import static com.apexon.compass.constants.StrategyServiceConstants.FROM;
import static com.apexon.compass.constants.StrategyServiceConstants.ID_PRE_UNDER;
import static com.apexon.compass.constants.StrategyServiceConstants.LET;
import static com.apexon.compass.constants.StrategyServiceConstants.MERGED;
import static com.apexon.compass.constants.StrategyServiceConstants.MERGED_PRS;
import static com.apexon.compass.constants.StrategyServiceConstants.PIPELINE;
import static com.apexon.compass.constants.StrategyServiceConstants.PULL_REQUEST_ID;
import static com.apexon.compass.constants.StrategyServiceConstants.REVIEW_COMMENTS_ON_OTHER_PR;
import static com.apexon.compass.constants.StrategyServiceConstants.TOTAL_PRS;

import com.apexon.compass.aggregation.vo.MemberWiseActivityMetricsVo;
import com.apexon.compass.aggregation.vo.PullRequestReviewersVo;
import com.apexon.compass.aggregation.vo.PullRequestsCommitsVo;
import com.apexon.compass.exception.custom.RecordNotFoundException;
import com.apexon.compass.exception.custom.ServiceException;
import com.apexon.compass.sonarservice.utils.StrategyServiceUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor(onConstructor = @__({ @Autowired }))
@Slf4j
public class PullRequestsDao {

    private final MongoTemplate mongoTemplate;

    public List<PullRequestsCommitsVo> getPrData(String projectId, Long startDate, Long endDate, List<String> repoIds) {
        List<? extends Bson> pipeline = Arrays.asList(
                new Document().append(MATCH_DOLLAR,
                        new Document().append(ID_PRE_UNDER, new ObjectId(projectId)).append(IS_DELETED, false)),
                new Document().append(PROJECT_DOLLAR,
                        new Document().append(ID_PRE_UNDER, ONE_DOUBLE).append(REPOS, ONE_DOUBLE)),
                new Document().append(UNWIND_DOLLAR, REPOS_DOLLAR),
                new Document().append(MATCH_DOLLAR, new Document().append(AND_DOLLAR,
                        Arrays.asList(new Document().append(REPOS_REPOID, new Document().append(IN_DOLLAR, repoIds)),
                                new Document().append(REPOS_SCM_REPO_URL,
                                        new Document().append(EXISTS_DOLLAR, true))))),
                new Document().append(LOOKUP_DOLLAR, new Document().append(FROM, PULL_REQUESTS)
                    .append(LET,
                            new Document().append(PROJECT_ID_CAPITALIZED, ID_UNDERSCORE_DOLLAR)
                                .append(REPO_URL_CAMEL_CASE, REPOS_SCM_REPO_URL_DOLLAR))
                    .append(PIPELINE, Collections.singletonList(new Document().append(MATCH_DOLLAR,
                            new Document().append(EXPRESSION_DOLLAR, new Document().append(AND_DOLLAR,
                                    Collections.singletonList(new Document().append(OR_DOLLAR, Arrays.asList(
                                            new Document().append(
                                                    AND_DOLLAR,
                                                    Arrays.asList(
                                                            new Document().append(EQUAL_DOLLAR,
                                                                    Arrays.asList(SCMURL_DOLLAR,
                                                                            REPO_URL_DOUBLE_DOLLAR)),
                                                            new Document().append(EQUAL_DOLLAR,
                                                                    Arrays.asList(PROJECT_ID_DOLLAR,
                                                                            PROJECT_ID_DOUBLE_DOLLAR)),
                                                            new Document().append(GTE_DOLLAR,
                                                                    Arrays.asList(PR_CREATED_TIME_DOLLAR, startDate)),
                                                            new Document().append(LTE_DOLLAR,
                                                                    Arrays.asList(PR_CREATED_TIME_DOLLAR, endDate)))),
                                            new Document().append(
                                                    AND_DOLLAR,
                                                    Arrays.asList(
                                                            new Document().append(EQUAL_DOLLAR,
                                                                    Arrays.asList(SCMURL_DOLLAR,
                                                                            REPO_URL_DOUBLE_DOLLAR)),
                                                            new Document().append(EQUAL_DOLLAR,
                                                                    Arrays.asList(PROJECT_ID_DOLLAR,
                                                                            PROJECT_ID_DOUBLE_DOLLAR)),
                                                            new Document().append(GTE_DOLLAR,
                                                                    Arrays.asList(PR_MERGE_TIME_DOLLAR, startDate)),
                                                            new Document().append(LTE_DOLLAR,
                                                                    Arrays.asList(PR_MERGE_TIME_DOLLAR, endDate)))),
                                            new Document().append(
                                                    AND_DOLLAR,
                                                    Arrays.asList(
                                                            new Document().append(EQUAL_DOLLAR,
                                                                    Arrays.asList(SCMURL_DOLLAR,
                                                                            REPO_URL_DOUBLE_DOLLAR)),
                                                            new Document().append(EQUAL_DOLLAR,
                                                                    Arrays.asList(PROJECT_ID_DOLLAR,
                                                                            PROJECT_ID_DOUBLE_DOLLAR)),
                                                            new Document().append(GTE_DOLLAR,
                                                                    Arrays.asList(PR_DECLINED_TIME_DOLLAR, startDate)),
                                                            new Document().append(LTE_DOLLAR,
                                                                    Arrays.asList(PR_DECLINED_TIME_DOLLAR,
                                                                            endDate))))))))))))
                    .append(AS, PULL_REQUESTS)),
                new Document().append(UNWIND_DOLLAR, PULL_REQUESTS_DOLLAR),
                new Document().append(LOOKUP_DOLLAR, new Document().append(FROM, PULL_REQUESTS_COMMITS)
                    .append(LET, new Document().append(PULL_REQUEST_ID, PULL_REQUESTS_DOT_ID_DOLLAR))
                    .append(PIPELINE, Collections.singletonList(new Document().append(MATCH_DOLLAR,
                            new Document().append(EXPRESSION_DOLLAR,
                                    new Document().append(EQUAL_DOLLAR,
                                            Arrays.asList(PULL_REQUEST_ID_DOLLAR, PULL_REQUEST_ID_DOUBLE_DOLLAR))))))
                    .append(AS, PULL_REQUESTS_DOT_COMMITS)),
                new Document().append(PROJECT_DOLLAR,
                        new Document().append(ID_PRE_UNDER, ONE_DOUBLE).append(PULL_REQUESTS, ONE_DOUBLE)));

        ObjectMapper mapper = new ObjectMapper();
        try {
            mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            return mapper.readValue(StrategyServiceUtils.documentsToJsonConverter(getDocumentsFromProjects(pipeline)),
                    new TypeReference<>() {
                    });
        }
        catch (JsonProcessingException processingException) {
            log.error(JSON_PROCESSING_LOG_MESSAGE + processingException.getMessage());
            throw new ServiceException(JSON_PROCESSING_EXCEPTION_MESSAGE);
        }
    }

    private List<Document> getDocumentsFromProjects(List<? extends Bson> pipeline) {
        List<Document> documents = mongoTemplate.getCollection(PROJECT_S).aggregate(pipeline).into(new ArrayList<>());
        if (CollectionUtils.isEmpty(documents)) {
            throw new RecordNotFoundException("No record found for this project");
        }
        return documents;
    }

    private List<Document> getDocumentsFromProjectsForActivityMetrics(List<? extends Bson> pipeline) {
        return mongoTemplate.getCollection(PROJECT_S).aggregate(pipeline).into(new ArrayList<>());
    }

    public List<PullRequestReviewersVo> getReviewerMetrics(String projectId, Long startDate, Long endDate,
            List<String> repoIds) {
        List<? extends Bson> pipeline = Arrays.asList(
                new Document().append(MATCH_DOLLAR,
                        new Document().append(ID_PRE_UNDER, new ObjectId(projectId)).append(IS_DELETED, false)),
                new Document().append(PROJECT_DOLLAR,
                        new Document().append(ID_PRE_UNDER, ONE_DOUBLE).append(REPOS, ONE_DOUBLE)),
                new Document().append(UNWIND_DOLLAR, REPOS_DOLLAR),
                new Document().append(MATCH_DOLLAR, new Document().append(AND_DOLLAR,
                        Arrays.asList(new Document().append(REPOS_REPOID, new Document().append(IN_DOLLAR, repoIds)),
                                new Document().append(REPOS_SCM_REPO_URL,
                                        new Document().append(EXISTS_DOLLAR, true))))),
                new Document().append(LOOKUP_DOLLAR, new Document().append(FROM, PULL_REQUESTS)
                    .append(LET,
                            new Document().append(PROJECT_ID_CAPITALIZED, ID_UNDERSCORE_DOLLAR)
                                .append(REPO_URL_CAMEL_CASE, REPOS_SCM_REPO_URL_DOLLAR))
                    .append(PIPELINE, Collections.singletonList(new Document().append(MATCH_DOLLAR,
                            new Document().append(EXPRESSION_DOLLAR, new Document().append(AND_DOLLAR,
                                    Collections.singletonList(new Document().append(OR_DOLLAR, Arrays.asList(
                                            new Document().append(
                                                    AND_DOLLAR,
                                                    Arrays.asList(
                                                            new Document().append(EQUAL_DOLLAR,
                                                                    Arrays.asList(SCMURL_DOLLAR,
                                                                            REPO_URL_DOUBLE_DOLLAR)),
                                                            new Document().append(EQUAL_DOLLAR,
                                                                    Arrays.asList(PROJECT_ID_DOLLAR,
                                                                            PROJECT_ID_DOUBLE_DOLLAR)),
                                                            new Document().append(GTE_DOLLAR,
                                                                    Arrays.asList(PR_CREATED_TIME_DOLLAR, startDate)),
                                                            new Document().append(LTE_DOLLAR,
                                                                    Arrays.asList(PR_CREATED_TIME_DOLLAR, endDate)))),
                                            new Document().append(
                                                    AND_DOLLAR,
                                                    Arrays.asList(
                                                            new Document().append(EQUAL_DOLLAR,
                                                                    Arrays.asList(SCMURL_DOLLAR,
                                                                            REPO_URL_DOUBLE_DOLLAR)),
                                                            new Document().append(EQUAL_DOLLAR,
                                                                    Arrays.asList(PROJECT_ID_DOLLAR,
                                                                            PROJECT_ID_DOUBLE_DOLLAR)),
                                                            new Document().append(GTE_DOLLAR,
                                                                    Arrays.asList(PR_MERGE_TIME_DOLLAR, startDate)),
                                                            new Document().append(LTE_DOLLAR,
                                                                    Arrays.asList(PR_MERGE_TIME_DOLLAR, endDate)))),
                                            new Document().append(
                                                    AND_DOLLAR,
                                                    Arrays.asList(
                                                            new Document().append(EQUAL_DOLLAR,
                                                                    Arrays.asList(SCMURL_DOLLAR,
                                                                            REPO_URL_DOUBLE_DOLLAR)),
                                                            new Document().append(EQUAL_DOLLAR,
                                                                    Arrays.asList(PROJECT_ID_DOLLAR,
                                                                            PROJECT_ID_DOUBLE_DOLLAR)),
                                                            new Document().append(GTE_DOLLAR,
                                                                    Arrays.asList(PR_DECLINED_TIME_DOLLAR, startDate)),
                                                            new Document().append(LTE_DOLLAR,
                                                                    Arrays.asList(PR_DECLINED_TIME_DOLLAR,
                                                                            endDate))))))))))))
                    .append(AS, PULL_REQUESTS)),
                new Document().append(UNWIND_DOLLAR, PULL_REQUESTS_DOLLAR),
                new Document().append(UNWIND_DOLLAR, PULL_REQUESTS_REVIEWERS_DOLLAR),
                new Document().append(GROUP_DOLLAR, new Document().append(ID_PRE_UNDER, PULL_REQUESTS_REVIEWERS_NAME)
                    .append(TOTAL_PRS, new Document().append(SUM_DOLLAR, ONE_DOUBLE))
                    .append(TOTAL_TIME_FOR_MERGED_PR,
                            new Document().append(SUM_DOLLAR, new Document().append(COND_DOLLAR, new Document()
                                .append(IF, new Document().append(AND_DOLLAR, Arrays.asList(
                                        new Document().append(EQUAL_DOLLAR, Arrays.asList(PULL_REQUESTS_STATE, MERGED)),
                                        new Document().append(EQUAL_DOLLAR,
                                                Arrays.asList(PULL_REQUESTS_PR_MERGED_BY_DOLLAR,
                                                        PULL_REQUESTS_REVIEWERS_NAME)))))
                                .append(THEN,
                                        new Document().append(DIVIDE_DOLLAR,
                                                Arrays.asList(
                                                        new Document().append(SUBTRACT_DOLLAR,
                                                                Arrays.asList(PULL_REQUESTS_PR_MERGE_TIME,
                                                                        PULL_REQUESTS_PR_CREATED_TIME)),
                                                        THIRTY_SIX_LAKH_DOUBLE)))
                                .append(ELSE, ZERO_DOUBLE))))
                    .append(TOTAL_TIME_FOR_DECLINED_PR,
                            new Document()
                                .append(SUM_DOLLAR,
                                        new Document().append(COND_DOLLAR,
                                                new Document()
                                                    .append(IF, new Document().append(
                                                            AND_DOLLAR,
                                                            Arrays.asList(
                                                                    new Document().append(EQUAL_DOLLAR,
                                                                            Arrays.asList(PULL_REQUESTS_STATE,
                                                                                    DECLINED)),
                                                                    new Document().append(EQUAL_DOLLAR,
                                                                            Arrays.asList(PULL_REQUESTS_PR_DECLINED_BY,
                                                                                    PULL_REQUESTS_REVIEWERS_NAME)))))
                                                    .append(THEN, new Document()
                                                        .append(DIVIDE_DOLLAR, Arrays.asList(
                                                                new Document().append(
                                                                        SUBTRACT_DOLLAR,
                                                                        Arrays.asList(PULL_REQUESTS_PR_DECLINED_TIME,
                                                                                PULL_REQUESTS_PR_CREATED_TIME)),
                                                                THIRTY_SIX_LAKH_DOUBLE)))
                                                    .append(ELSE, ZERO_DOUBLE))))
                    .append(TOTAL_TIME_FOR_OPEN_PR, new Document()
                        .append(SUM_DOLLAR,
                                new Document().append(COND_DOLLAR, new Document()
                                    .append(IF,
                                            new Document().append(AND_DOLLAR,
                                                    Arrays.asList(
                                                            new Document().append(EQUAL_DOLLAR,
                                                                    Arrays.asList(PULL_REQUESTS_STATE,
                                                                            OPEN_LOWER_CASE)),
                                                            new Document().append(
                                                                    EQUAL_DOLLAR,
                                                                    Arrays.asList(PULL_REQUESTS_PR_DECLINED_TIME,
                                                                            ZERO_DOUBLE)),
                                                            new Document().append(
                                                                    EQUAL_DOLLAR,
                                                                    Arrays.asList(PULL_REQUESTS_PR_MERGE_TIME,
                                                                            ZERO_DOUBLE)),
                                                            new Document().append(OR_DOLLAR, Arrays.asList(
                                                                    new Document().append(EQUAL_DOLLAR,
                                                                            Arrays.asList(PULL_REQUESTS_COMMENTS_NAME,
                                                                                    PULL_REQUESTS_REVIEWERS_NAME)),
                                                                    new Document().append(EQUAL_DOLLAR,
                                                                            Arrays.asList(PULL_REQUESTS_APPROVERS_NAME,
                                                                                    PULL_REQUESTS_REVIEWERS_NAME)))))))
                                    .append(THEN,
                                            new Document().append(DIVIDE_DOLLAR,
                                                    Arrays.asList(
                                                            new Document().append(SUBTRACT_DOLLAR,
                                                                    Arrays.asList(ZERO_DOUBLE,
                                                                            PULL_REQUESTS_PR_CREATED_TIME)),
                                                            THIRTY_SIX_LAKH_DOUBLE)))
                                    .append(ELSE, ZERO_DOUBLE))))));

        ObjectMapper mapper = new ObjectMapper();
        try {
            mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            return mapper.readValue(StrategyServiceUtils.documentsToJsonConverter(getDocumentsFromProjects(pipeline)),
                    new TypeReference<>() {
                    });
        }
        catch (JsonProcessingException processingException) {
            log.error(JSON_PROCESSING_LOG_MESSAGE + processingException.getMessage());
            throw new ServiceException(JSON_PROCESSING_EXCEPTION_MESSAGE);
        }
    }

    public List<MemberWiseActivityMetricsVo> getMembersActivityMetrics(String projectId, Long startDate, Long endDate,
            List<String> repoIds) {
        List<? extends Bson> pipeline = Arrays.asList(
                new Document().append(MATCH_DOLLAR,
                        new Document().append(ID_PRE_UNDER, new ObjectId(projectId)).append(IS_DELETED, false)),
                new Document().append(PROJECT_DOLLAR,
                        new Document().append(ID_PRE_UNDER, ONE_DOUBLE).append(REPOS, ONE_DOUBLE)),
                new Document().append(UNWIND_DOLLAR, REPOS_DOLLAR),
                new Document().append(MATCH_DOLLAR, new Document().append(AND_DOLLAR,
                        Arrays.asList(new Document().append(REPOS_REPOID, new Document().append(IN_DOLLAR, repoIds)),
                                new Document().append(REPOS_SCM_REPO_URL,
                                        new Document().append(EXISTS_DOLLAR, true))))),
                new Document().append(LOOKUP_DOLLAR, new Document().append(FROM, PULL_REQUESTS)
                    .append(LET,
                            new Document().append(PROJECT_ID_CAPITALIZED, ID_UNDERSCORE_DOLLAR)
                                .append(REPO_URL_CAMEL_CASE, REPOS_SCM_REPO_URL_DOLLAR))
                    .append(PIPELINE, Collections.singletonList(new Document().append(MATCH_DOLLAR,
                            new Document().append(EXPRESSION_DOLLAR, new Document().append(AND_DOLLAR,
                                    Collections.singletonList(new Document().append(OR_DOLLAR, Arrays.asList(
                                            new Document().append(
                                                    AND_DOLLAR,
                                                    Arrays.asList(
                                                            new Document().append(EQUAL_DOLLAR,
                                                                    Arrays.asList(SCMURL_DOLLAR,
                                                                            REPO_URL_DOUBLE_DOLLAR)),
                                                            new Document().append(EQUAL_DOLLAR,
                                                                    Arrays.asList(PROJECT_ID_DOLLAR,
                                                                            PROJECT_ID_DOUBLE_DOLLAR)),
                                                            new Document().append(GTE_DOLLAR,
                                                                    Arrays.asList(PR_CREATED_TIME_DOLLAR, startDate)),
                                                            new Document().append(LTE_DOLLAR,
                                                                    Arrays.asList(PR_CREATED_TIME_DOLLAR, endDate)))),
                                            new Document().append(
                                                    AND_DOLLAR,
                                                    Arrays.asList(
                                                            new Document().append(EQUAL_DOLLAR,
                                                                    Arrays.asList(SCMURL_DOLLAR,
                                                                            REPO_URL_DOUBLE_DOLLAR)),
                                                            new Document().append(EQUAL_DOLLAR,
                                                                    Arrays.asList(PROJECT_ID_DOLLAR,
                                                                            PROJECT_ID_DOUBLE_DOLLAR)),
                                                            new Document().append(GTE_DOLLAR,
                                                                    Arrays.asList(PR_MERGE_TIME_DOLLAR, startDate)),
                                                            new Document().append(LTE_DOLLAR,
                                                                    Arrays.asList(PR_MERGE_TIME_DOLLAR, endDate)))),
                                            new Document().append(
                                                    AND_DOLLAR,
                                                    Arrays.asList(
                                                            new Document().append(EQUAL_DOLLAR,
                                                                    Arrays.asList(SCMURL_DOLLAR,
                                                                            REPO_URL_DOUBLE_DOLLAR)),
                                                            new Document().append(EQUAL_DOLLAR,
                                                                    Arrays.asList(PROJECT_ID_DOLLAR,
                                                                            PROJECT_ID_DOUBLE_DOLLAR)),
                                                            new Document().append(GTE_DOLLAR,
                                                                    Arrays.asList(PR_DECLINED_TIME_DOLLAR, startDate)),
                                                            new Document().append(LTE_DOLLAR,
                                                                    Arrays.asList(PR_DECLINED_TIME_DOLLAR,
                                                                            endDate))))))))))))
                    .append(AS, PULL_REQUESTS)),
                new Document().append(UNWIND_DOLLAR, PULL_REQUESTS_DOLLAR),
                new Document().append(FACET, new Document()
                    .append(PR_METRICS, Arrays.asList(
                            new Document().append(LOOKUP_DOLLAR, new Document().append(FROM, PULL_REQUESTS_COMMITS)
                                .append(LET, new Document().append(PULL_REQUEST_ID, PULL_REQUESTS_DOT_ID_DOLLAR))
                                .append(PIPELINE,
                                        Collections.singletonList(new Document().append(MATCH_DOLLAR,
                                                new Document().append(EXPRESSION_DOLLAR,
                                                        new Document().append(EQUAL_DOLLAR,
                                                                Arrays.asList(PULL_REQUEST_ID_DOLLAR,
                                                                        PULL_REQUEST_ID_DOUBLE_DOLLAR))))))
                                .append(AS, PULL_REQUESTS_DOT_COMMITS)),
                            new Document().append(GROUP_DOLLAR, new Document()
                                .append(ID_PRE_UNDER, PULL_REQUESTS_SCM_AUTHOR_NAME)
                                .append(CODE_COMMITS,
                                        new Document().append(SUM_DOLLAR,
                                                new Document().append(SIZE_DOLLAR, PULL_REQUESTS_DOT_COMMITS_DOLLAR)))
                                .append(TOTAL_PRS, new Document().append(SUM_DOLLAR, ONE_DOUBLE))
                                .append(MERGED_PRS,
                                        new Document().append(SUM_DOLLAR,
                                                new Document().append(COND_DOLLAR,
                                                        Arrays.asList(
                                                                new Document().append(IN_DOLLAR,
                                                                        Arrays.asList(PULL_REQUESTS_STATE,
                                                                                List.of(MERGED))),
                                                                ONE_DOUBLE, ZERO_DOUBLE)))))))
                    .append(REVIEW_METRICS,
                            Arrays.asList(new Document().append(UNWIND_DOLLAR, PULL_REQUESTS_COMMENTS),
                                    new Document().append(GROUP_DOLLAR,
                                            new Document().append(ID_PRE_UNDER, PULL_REQUESTS_COMMENTS_NAME)
                                                .append(REVIEW_COMMENTS_ON_OTHER_PR, new Document().append(SUM_DOLLAR,
                                                        new Document().append(COND_DOLLAR, Arrays.asList(
                                                                new Document().append(NE_DOLLAR,
                                                                        Arrays.asList(PULL_REQUESTS_COMMENTS_NAME,
                                                                                PULL_REQUESTS_SCM_AUTHOR_NAME)),
                                                                ONE_DOUBLE, ZERO_DOUBLE)))))))),
                new Document().append(PROJECT_DOLLAR,
                        new Document().append(ALL_METRICS,
                                new Document().append(SET_UNION_DOLLAR,
                                        Arrays.asList(PR_METRICS_DOLLAR, REVIEW_METRICS_DOLLAR)))),
                new Document().append(UNWIND_DOLLAR, ALL_METRICS_DOLLAR),
                new Document().append(REPLACE_ROOT_DOLLAR, new Document().append(NEW_ROOT, ALL_METRICS_DOLLAR)));

        ObjectMapper mapper = new ObjectMapper();
        try {
            mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            return mapper.readValue(
                    StrategyServiceUtils.documentsToJsonConverter(getDocumentsFromProjectsForActivityMetrics(pipeline)),
                    new TypeReference<>() {
                    });
        }
        catch (JsonProcessingException processingException) {
            log.error(JSON_PROCESSING_LOG_MESSAGE + processingException.getMessage());
            throw new ServiceException(JSON_PROCESSING_EXCEPTION_MESSAGE);
        }
    }

}
