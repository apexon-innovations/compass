package com.apexon.compass.sonarservice.dao;

import static com.apexon.compass.constants.ClientDashboardServiceConstants.JSON_PROCESSING_EXCEPTION_MESSAGE;
import static com.apexon.compass.constants.ClientDashboardServiceConstants.JSON_PROCESSING_LOG_MESSAGE;
import static com.apexon.compass.constants.EntitiesConstants.PROJECTS;
import static com.apexon.compass.constants.QueryConstants.*;
import static com.apexon.compass.constants.StrategyServiceConstants.ASC;
import static com.apexon.compass.constants.StrategyServiceConstants.DESC;
import static com.apexon.compass.constants.StrategyServiceConstants.MERGED;

import com.apexon.compass.sonarservice.enums.SubmitterMetricsSortFilterEnum;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.apexon.compass.aggregation.vo.SprintSubmitterMetricsVo;
import com.apexon.compass.exception.custom.RecordNotFoundException;
import com.apexon.compass.exception.custom.ServiceException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import com.apexon.compass.sonarservice.utils.StrategyServiceUtils;
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
public class SprintSubmitterMetricsDao {

    private final MongoTemplate mongoTemplate;

    public List<SprintSubmitterMetricsVo> getSprintSubmitterMetrics(String projectId, Long startDate, Long endDate,
            SubmitterMetricsSortFilterEnum sortBy, List<String> repoIds) {
        List<? extends Bson> pipeline = Arrays.asList(
                new Document(MATCH_DOLLAR,
                        new Document(ID_PRE_UNDER, new ObjectId(projectId)).append(IS_DELETED, false)),
                new Document(PROJECT_DOLLAR, new Document(ID_PRE_UNDER, 1L).append(REPOS, 1L)),
                new Document(UNWIND_DOLLAR, REPOS_DOLLAR),
                new Document(MATCH_DOLLAR,
                        new Document(AND_DOLLAR,
                                Arrays.asList(new Document(REPOS_REPOID, new Document(IN_DOLLAR, repoIds)),
                                        new Document(REPOS_SCM_REPO_URL, new Document(EXISTS_DOLLAR, true))))),
                new Document(LOOKUP_DOLLAR, new Document(FROM, PULL_REQUESTS)
                    .append(LET,
                            new Document(PROJECT_ID, ID_UNDERSCORE_DOLLAR).append(REPO_URL_CAMEL_CASE,
                                    REPOS_SCM_REPO_URL_DOLLAR))
                    .append(PIPELINE, List.of(new Document(MATCH_DOLLAR,
                            new Document(EXPRESSION_DOLLAR, new Document(AND_DOLLAR, List.of(new Document(OR_DOLLAR,
                                    Arrays.asList(
                                            new Document(AND_DOLLAR, Arrays.asList(
                                                    new Document(EQUAL_DOLLAR,
                                                            Arrays.asList(SCMURL_DOLLAR, REPO_URL_DOUBLE_DOLLAR)),
                                                    new Document(EQUAL_DOLLAR,
                                                            Arrays.asList(PROJECT_ID_DOLLAR, PROJECT_ID_DOUBLE_DOLLAR)),
                                                    new Document(GTE_DOLLAR,
                                                            Arrays.asList(PR_CREATED_TIME_DOLLAR, startDate)),
                                                    new Document(
                                                            LTE_DOLLAR,
                                                            Arrays.asList(PR_CREATED_TIME_DOLLAR, endDate)))),
                                            new Document(AND_DOLLAR, Arrays.asList(
                                                    new Document(EQUAL_DOLLAR,
                                                            Arrays.asList(SCMURL_DOLLAR, REPO_URL_DOUBLE_DOLLAR)),
                                                    new Document(EQUAL_DOLLAR,
                                                            Arrays.asList(PROJECT_ID_DOLLAR, PROJECT_ID_DOUBLE_DOLLAR)),
                                                    new Document(GTE_DOLLAR,
                                                            Arrays.asList(PR_MERGE_TIME_DOLLAR, startDate)),
                                                    new Document(LTE_DOLLAR,
                                                            Arrays.asList(PR_MERGE_TIME_DOLLAR, endDate)))),
                                            new Document(AND_DOLLAR, Arrays.asList(new Document(EQUAL_DOLLAR,
                                                    Arrays.asList(SCMURL_DOLLAR, REPO_URL_DOUBLE_DOLLAR)),
                                                    new Document(EQUAL_DOLLAR,
                                                            Arrays.asList(PROJECT_ID_DOLLAR, PROJECT_ID_DOUBLE_DOLLAR)),
                                                    new Document(GTE_DOLLAR,
                                                            Arrays.asList(PR_DECLINED_TIME_DOLLAR, startDate)),
                                                    new Document(LTE_DOLLAR,
                                                            Arrays.asList(PR_DECLINED_TIME_DOLLAR, endDate))))))))))))
                    .append(AS, PULL_REQUESTS)),
                new Document(UNWIND_DOLLAR, PULL_REQUESTS_DOLLAR), new Document(LOOKUP_DOLLAR, new Document(FROM,
                        PULL_REQUESTS_COMMITS)
                    .append(LET, new Document(PULL_REQUEST_ID, PULL_REQUESTS_DOT_ID_DOLLAR))
                    .append(PIPELINE, List.of(new Document(MATCH_DOLLAR,
                            new Document(EXPRESSION_DOLLAR,
                                    new Document(
                                            EQUAL_DOLLAR,
                                            Arrays.asList(PULL_REQUEST_ID_DOLLAR, PULL_REQUEST_ID_DOUBLE_DOLLAR))))))
                    .append(AS, PULL_REQUESTS_DOT_COMMITS)),
                new Document(GROUP_DOLLAR,
                        new Document(ID_PRE_UNDER, PULL_REQUESTS_SCM_AUTHOR_NAME)
                            .append(TOTAL_PRS, new Document(SUM_DOLLAR, 1L))
                            .append(MERGED_PRS, new Document(SUM_DOLLAR, new Document(COND_DOLLAR,
                                    Arrays.asList(new Document(IN_DOLLAR,
                                            Arrays.asList(PULL_REQUESTS_STATE, List.of(MERGED))), 1L, 0L))))
                            .append(OPEN_PRS,
                                    new Document(
                                            SUM_DOLLAR,
                                            new Document(COND_DOLLAR,
                                                    Arrays.asList(new Document(IN_DOLLAR,
                                                            Arrays.asList(PULL_REQUESTS_STATE,
                                                                    List.of(OPEN_LOWER_CASE))),
                                                            1L, 0L))))
                            .append(DECLINED_PRS,
                                    new Document(SUM_DOLLAR,
                                            new Document(COND_DOLLAR,
                                                    Arrays.asList(new Document(
                                                            IN_DOLLAR,
                                                            Arrays.asList(PULL_REQUESTS_STATE, List.of(DECLINED))), 1L,
                                                            0L))))
                            .append(REVIEWER_COMMENTS,
                                    new Document(SUM_DOLLAR, new Document(COND_DOLLAR, Arrays.asList(
                                            new Document(AND_DOLLAR,
                                                    Arrays.asList(
                                                            new Document(GT_DOLLAR,
                                                                    Arrays.asList(new Document(SIZE_DOLLAR,
                                                                            PULL_REQUESTS_COMMENTS), 0L)),
                                                            new Document(NE_DOLLAR,
                                                                    Arrays.asList(PULL_REQUESTS_COMMENTS_NAME,
                                                                            PULL_REQUESTS_SCM_AUTHOR_NAME)))),
                                            1L, 0L))))
                            .append(RECOMMITS,
                                    new Document(SUM_DOLLAR,
                                            new Document(SIZE_DOLLAR,
                                                    new Document(FILTER_DOLLAR,
                                                            new Document(INPUT, PULL_REQUESTS_DOT_COMMITS_DOLLAR)
                                                                .append(AS, COMMIT)
                                                                .append(COND, new Document(GT_DOLLAR,
                                                                        Arrays.asList(COMMIT_TIME_DOUBLE_DOLLAR,
                                                                                PULL_REQUESTS_PR_CREATED_TIME)))))))
                            .append(TOTAL_TIME_FOR_MERGED_PR,
                                    new Document().append(SUM_DOLLAR,
                                            new Document()
                                                .append(COND_DOLLAR, new Document()
                                                    .append(IF,
                                                            new Document().append(EQUAL_DOLLAR,
                                                                    Arrays.asList(PULL_REQUESTS_STATE, MERGED)))
                                                    .append(THEN,
                                                            new Document().append(DIVIDE_DOLLAR, Arrays.asList(
                                                                    new Document().append(SUBTRACT_DOLLAR,
                                                                            Arrays.asList(PULL_REQUESTS_PR_MERGE_TIME,
                                                                                    PULL_REQUESTS_PR_CREATED_TIME)),
                                                                    THIRTY_SIX_LAKH_DOUBLE)))
                                                    .append(ELSE, ZERO_DOUBLE))))
                            .append(TOTAL_TIME_FOR_DECLINED_PR,
                                    new Document().append(SUM_DOLLAR,
                                            new Document().append(COND_DOLLAR, new Document()
                                                .append(IF,
                                                        new Document().append(EQUAL_DOLLAR,
                                                                Arrays.asList(PULL_REQUESTS_STATE, DECLINED)))
                                                .append(THEN,
                                                        new Document().append(DIVIDE_DOLLAR, Arrays.asList(
                                                                new Document().append(SUBTRACT_DOLLAR,
                                                                        Arrays.asList(PULL_REQUESTS_PR_DECLINED_TIME,
                                                                                PULL_REQUESTS_PR_CREATED_TIME)),
                                                                THIRTY_SIX_LAKH_DOUBLE)))
                                                .append(ELSE, ZERO_DOUBLE))))),
                new Document(SORT_DOLLAR,
                        new Document(sortBy.getValue(), Double.parseDouble(((Objects.nonNull(sortBy.getValue())
                                && sortBy.getValue().equals(SubmitterMetricsSortFilterEnum.MEMBERWISE.getValue())) ? ASC
                                        : DESC)))));

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
        List<Document> documents = mongoTemplate.getCollection(PROJECTS).aggregate(pipeline).into(new ArrayList<>());
        if (CollectionUtils.isEmpty(documents)) {
            throw new RecordNotFoundException("No record found for this project");
        }
        return documents;
    }

}
