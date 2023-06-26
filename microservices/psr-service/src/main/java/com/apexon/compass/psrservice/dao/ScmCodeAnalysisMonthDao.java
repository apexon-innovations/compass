package com.apexon.compass.psrservice.dao;

import static com.apexon.compass.constants.ClientDashboardServiceConstants.FIRST;
import static com.apexon.compass.constants.ClientDashboardServiceConstants.JSON_PROCESSING_EXCEPTION_MESSAGE;
import static com.apexon.compass.constants.ClientDashboardServiceConstants.JSON_PROCESSING_LOG_MESSAGE;
import static com.apexon.compass.constants.ClientDashboardServiceConstants.PUSH_DOLLAR;
import static com.apexon.compass.constants.EntitiesConstants.DATE;
import static com.apexon.compass.constants.EntitiesConstants.PROJECTS;
import static com.apexon.compass.constants.EntitiesConstants.SCM_CODE_STATISTICAL_ANALYSIS_MONTH;
import static com.apexon.compass.constants.PsrServiceConstants.ID;
import static com.apexon.compass.constants.PsrServiceConstants.IS_DELETED;
import static com.apexon.compass.constants.StrategyServiceConstants.AS;
import static com.apexon.compass.constants.StrategyServiceConstants.COND;
import static com.apexon.compass.constants.StrategyServiceConstants.EQUAL;
import static com.apexon.compass.constants.StrategyServiceConstants.EXPRESSION;
import static com.apexon.compass.constants.StrategyServiceConstants.FROM;
import static com.apexon.compass.constants.StrategyServiceConstants.GROUP;
import static com.apexon.compass.constants.StrategyServiceConstants.LET;
import static com.apexon.compass.constants.StrategyServiceConstants.LOOKUP;
import static com.apexon.compass.constants.StrategyServiceConstants.MATCH;
import static com.apexon.compass.constants.StrategyServiceConstants.NAME;
import static com.apexon.compass.constants.StrategyServiceConstants.PIPELINE;
import static com.apexon.compass.constants.StrategyServiceConstants.PROJECT;
import static com.apexon.compass.constants.StrategyServiceConstants.SORT;
import static com.apexon.compass.constants.StrategyServiceConstants.SUM;
import static com.apexon.compass.constants.StrategyServiceConstants.UNWIND;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.apexon.compass.aggregation.vo.ScmCodeAnalysisMonthRepoSortingVo;
import com.apexon.compass.exception.custom.ServiceException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bson.BsonNull;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor(onConstructor = @__({ @Autowired }))
@Slf4j
public class ScmCodeAnalysisMonthDao {

    public static final String REPOS_SCM_REPO_URL = "$repos.scmRepoUrl";

    private final MongoTemplate mongoTemplate;

    public List<ScmCodeAnalysisMonthRepoSortingVo> getScmCodeAnalysisMonthSumOfAddedRemovedLines(ObjectId projectId,
            Long date) {
        List<? extends Bson> pipeline = Arrays.asList(
                new Document().append(MATCH, new Document().append(ID, projectId)
                    .append(IS_DELETED, false)),
                new Document().append(UNWIND, "$repos"),
                new Document()
                    .append(LOOKUP, new Document().append(FROM, SCM_CODE_STATISTICAL_ANALYSIS_MONTH)
                        .append(LET, new Document().append("scmUrl", REPOS_SCM_REPO_URL))
                        .append(PIPELINE, Arrays.asList(
                                new Document()
                                    .append(MATCH,
                                            new Document()
                                                .append(EXPRESSION,
                                                        new Document().append(EQUAL,
                                                                Arrays.asList("$repoUrl", "$$scmUrl")))
                                                .append(DATE, date)),
                                new Document().append(PROJECT,
                                        new Document().append(ID, 0.0)
                                            .append("loc",
                                                    new Document().append(SUM,
                                                            Arrays.asList("$addedLineOfCode", "$removedLineOfCode"))))))
                        .append(AS, "scm_data")),
                new Document().append(SORT, new Document().append("scm_data.loc", -1.0)),
                new Document()
                    .append(GROUP,
                            new Document().append(ID, "$_id")
                                .append(NAME, new Document().append(FIRST, "$name"))
                                .append("repos",
                                        new Document().append(PUSH_DOLLAR,
                                                new Document().append("repoId", "$repos.repoId")
                                                    .append("repoName", "$repos.repoName")
                                                    .append("sonarProjectId", "$repos.sonarProjectId")
                                                    .append("scmRepoUrl", REPOS_SCM_REPO_URL)
                                                    .append("scm",
                                                            new Document().append(COND,
                                                                    Arrays.asList(
                                                                            new Document().append("$gt",
                                                                                    Arrays.asList(REPOS_SCM_REPO_URL,
                                                                                            new BsonNull())),
                                                                            true, false)))
                                                    .append("sonar",
                                                            new Document().append(COND,
                                                                    Arrays.asList(new Document().append("$gt",
                                                                            Arrays.asList("$repos.sonarProjectId",
                                                                                    new BsonNull())),
                                                                            true, false)))
                                                    .append("changedLoc", "$scm_data.loc")))));
        ObjectMapper mapper = new ObjectMapper();
        try {
            List<Document> documents = mongoTemplate.getCollection(PROJECTS)
                .aggregate(pipeline)
                .into(new ArrayList<>());
            mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            return mapper.readValue(documentsToJsonConverter(documents),
                    new TypeReference<List<ScmCodeAnalysisMonthRepoSortingVo>>() {
                    });
        }
        catch (JsonProcessingException processingException) {
            log.error(JSON_PROCESSING_LOG_MESSAGE + processingException.getMessage());
            throw new ServiceException(JSON_PROCESSING_EXCEPTION_MESSAGE);
        }
    }

    public static String documentsToJsonConverter(List<Document> documents) {
        Gson gson = new Gson();
        return gson.toJson(documents);
    }

}
