package com.apexon.compass.sonarservice.dao;

import static com.apexon.compass.constants.ClientDashboardServiceConstants.IS_DELETED;
import static com.apexon.compass.constants.ClientDashboardServiceConstants.TOTAL;
import static com.apexon.compass.constants.EntitiesConstants.PROJECTS;
import static com.apexon.compass.constants.QueryConstants.*;
import static com.apexon.compass.constants.StrategyServiceConstants.MERGED;
import static com.apexon.compass.constants.StrategyServiceConstants.RECORD_NOT_FOUND;

import com.apexon.compass.aggregation.vo.SprintPullRequestSummaryVo;
import com.apexon.compass.exception.custom.RecordNotFoundException;
import com.apexon.compass.exception.custom.ServiceException;
import com.mongodb.MongoException;
import com.mongodb.client.MongoCollection;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import lombok.AllArgsConstructor;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor(onConstructor = @__({ @Autowired }))
public class PrSummaryDao {

    private final MongoTemplate mongoTemplate;

    public SprintPullRequestSummaryVo getSprintPullRequestSummary(String projectId, List<String> repoIds,
            Long startDate, Long endDate, Long lastUpdatedTime) {
        MongoCollection<Document> collection = mongoTemplate.getCollection(PROJECTS);
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
                            new Document().append(PROJECT_ID, ID_UNDERSCORE_DOLLAR)
                                .append(REPO_URL_CAMEL_CASE, REPOS_SCM_REPO_URL_DOLLAR))
                    .append(PIPELINE, Arrays.asList(
                            new Document().append(MATCH_DOLLAR, new Document().append(EXPRESSION_DOLLAR, new Document()
                                .append(AND_DOLLAR, Collections.singletonList(new Document().append(OR_DOLLAR,
                                        Arrays.asList(
                                                new Document()
                                                    .append(AND_DOLLAR, Arrays.asList(
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
                                                                        Arrays.asList(PR_DECLINED_TIME_DOLLAR,
                                                                                startDate)),
                                                                new Document().append(LTE_DOLLAR,
                                                                        Arrays.asList(PR_DECLINED_TIME_DOLLAR,
                                                                                endDate)))))))))),
                            new Document().append(GROUP_DOLLAR,
                                    new Document().append(ID_PRE_UNDER, ONE_DOUBLE)
                                        .append(TOTAL, new Document().append(SUM_DOLLAR, ONE_DOUBLE))
                                        .append(MERGED,
                                                new Document().append(
                                                        SUM_DOLLAR,
                                                        new Document().append(
                                                                COND_DOLLAR,
                                                                Arrays.asList(new Document().append(
                                                                        IN_DOLLAR,
                                                                        Arrays.asList(STATE_DOLLAR, List.of(MERGED))),
                                                                        ONE_DOUBLE, ZERO_DOUBLE))))
                                        .append(OPEN_LOWER_CASE, new Document()
                                            .append(SUM_DOLLAR, new Document().append(COND_DOLLAR, Arrays.asList(
                                                    new Document().append(
                                                            AND_DOLLAR,
                                                            Arrays.asList(
                                                                    new Document().append(EQUAL_DOLLAR,
                                                                            Arrays.asList(STATE_DOLLAR,
                                                                                    OPEN_LOWER_CASE)),
                                                                    new Document().append(EQUAL_DOLLAR,
                                                                            Arrays.asList(PR_DECLINED_TIME_DOLLAR,
                                                                                    ZERO_DOUBLE)),
                                                                    new Document().append(
                                                                            EQUAL_DOLLAR,
                                                                            Arrays.asList(PR_MERGE_TIME_DOLLAR,
                                                                                    ZERO_DOUBLE)),
                                                                    new Document().append(OR_DOLLAR, Arrays
                                                                        .asList(new Document().append(
                                                                                GT_DOLLAR,
                                                                                Arrays.asList(new Document().append(
                                                                                        SIZE_DOLLAR, COMMENTS_DOLLAR),
                                                                                        ZERO_DOUBLE)),
                                                                                new Document().append(GT_DOLLAR,
                                                                                        Arrays.asList(new Document()
                                                                                            .append(SIZE_DOLLAR,
                                                                                                    APPROVERS_DOLLAR),
                                                                                                ZERO_DOUBLE)),
                                                                                new Document().append(GT_DOLLAR,
                                                                                        Arrays.asList(
                                                                                                PR_CREATED_TIME_DOLLAR,
                                                                                                lastUpdatedTime)))))),
                                                    ONE_DOUBLE, ZERO_DOUBLE))))
                                        .append(DECLINED, new Document()
                                            .append(SUM_DOLLAR,
                                                    new Document().append(COND_DOLLAR,
                                                            Arrays
                                                                .asList(new Document().append(IN_DOLLAR,
                                                                        Arrays.asList(STATE_DOLLAR, List.of(DECLINED))),
                                                                        ONE_DOUBLE, ZERO_DOUBLE))))
                                        .append(UNATTENDED, new Document().append(SUM_DOLLAR, new Document()
                                            .append(COND_DOLLAR, Arrays.asList(
                                                    new Document().append(
                                                            AND_DOLLAR,
                                                            Arrays.asList(
                                                                    new Document().append(EQUAL_DOLLAR,
                                                                            Arrays.asList(STATE_DOLLAR,
                                                                                    OPEN_LOWER_CASE)),
                                                                    new Document()
                                                                        .append(EQUAL_DOLLAR,
                                                                                Arrays.asList(PR_DECLINED_TIME_DOLLAR,
                                                                                        ZERO_DOUBLE)),
                                                                    new Document().append(EQUAL_DOLLAR,
                                                                            Arrays.asList(PR_MERGE_TIME_DOLLAR,
                                                                                    ZERO_DOUBLE)),
                                                                    new Document().append(
                                                                            EQUAL_DOLLAR,
                                                                            Arrays.asList(
                                                                                    new Document().append(SIZE_DOLLAR,
                                                                                            COMMENTS_DOLLAR),
                                                                                    ZERO_DOUBLE)),
                                                                    new Document().append(EQUAL_DOLLAR,
                                                                            Arrays.asList(
                                                                                    new Document().append(SIZE_DOLLAR,
                                                                                            APPROVERS_DOLLAR),
                                                                                    ZERO_DOUBLE)),
                                                                    new Document().append(LT_DOLLAR,
                                                                            Arrays.asList(PR_CREATED_TIME_DOLLAR,
                                                                                    lastUpdatedTime)))),
                                                    ONE_DOUBLE, ZERO_DOUBLE)))))))
                    .append(AS, PULL_REQUESTS)),
                new Document().append(UNWIND_DOLLAR, PULL_REQUESTS_DOLLAR),
                new Document().append(GROUP_DOLLAR, new Document().append(ID_PRE_UNDER, ID_UNDERSCORE_DOLLAR)
                    .append(TOTAL, new Document().append(SUM_DOLLAR, PULL_REQUESTS_DOT_TOTAL_DOLLAR))
                    .append(MERGED, new Document().append(SUM_DOLLAR, PULL_REQUESTS_DOT_MERGED_DOLLAR))
                    .append(OPEN_LOWER_CASE, new Document().append(SUM_DOLLAR, PULL_REQUESTS_DOT_OPEN_DOLLAR))
                    .append(DECLINED, new Document().append(SUM_DOLLAR, PULL_REQUESTS_DOT_DECLINED_DOLLAR))
                    .append(UNATTENDED, new Document().append(SUM_DOLLAR, PULL_REQUESTS_DOT_UNATTENDED_DOLLAR))));

        try {
            Document document = collection.aggregate(pipeline).first();
            if (Objects.isNull(document)) {
                throw new RecordNotFoundException(RECORD_NOT_FOUND);
            }
            return SprintPullRequestSummaryVo.builder()
                .total(document.getDouble(TOTAL))
                .declined(document.getDouble("declined"))
                .merged(document.getDouble(MERGED))
                .open(document.getDouble("open"))
                .unattended(document.getDouble("unattended"))
                .build();
        }
        catch (MongoException ex) {
            throw new ServiceException("Something went wrong while performing DB operation");
        }
    }

}
