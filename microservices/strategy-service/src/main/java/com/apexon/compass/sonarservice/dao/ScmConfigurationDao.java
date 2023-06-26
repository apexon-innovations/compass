package com.apexon.compass.sonarservice.dao;

import com.apexon.compass.aggregation.vo.CodeMetricsVo;
import com.apexon.compass.aggregation.vo.LanguageSpecificViolationsVo;
import com.apexon.compass.aggregation.vo.QualityMetricsVo;
import com.apexon.compass.exception.custom.RecordNotFoundException;
import com.apexon.compass.exception.custom.ServiceException;
import com.apexon.compass.sonarservice.utils.StrategyServiceUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.MongoException;
import com.mongodb.client.MongoCollection;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;

import java.util.*;

import static com.apexon.compass.constants.EntitiesConstants.PROJECTS;
import static com.apexon.compass.constants.QueryConstants.*;
import static com.apexon.compass.constants.StrategyServiceConstants.ADDED_LINE_OF_CODE_TILL_DATE;
import static com.apexon.compass.constants.StrategyServiceConstants.AS;
import static com.apexon.compass.constants.StrategyServiceConstants.CODE_ANALYSIS;
import static com.apexon.compass.constants.StrategyServiceConstants.CODE_ANALYSIS_ADDED_LINE_OF_CODE;
import static com.apexon.compass.constants.StrategyServiceConstants.CODE_ANALYSIS_AUTHORS;
import static com.apexon.compass.constants.StrategyServiceConstants.CODE_ANALYSIS_AUTHORS_LEGACY_REFACTOR;
import static com.apexon.compass.constants.StrategyServiceConstants.CODE_ANALYSIS_CODE_CHURN;
import static com.apexon.compass.constants.StrategyServiceConstants.CODE_ANALYSIS_DEVELOPER_NAME;
import static com.apexon.compass.constants.StrategyServiceConstants.CODE_ANALYSIS_REMOVED_LINE_OF_CODE;
import static com.apexon.compass.constants.StrategyServiceConstants.CODE_ANALYSIS_TOTAL_LOC;
import static com.apexon.compass.constants.StrategyServiceConstants.CODE_CHURN;
import static com.apexon.compass.constants.StrategyServiceConstants.FROM;
import static com.apexon.compass.constants.StrategyServiceConstants.ID_PRE_UNDER;
import static com.apexon.compass.constants.StrategyServiceConstants.IS_ARCHIVE;
import static com.apexon.compass.constants.StrategyServiceConstants.LEGACY_REFACTOR;
import static com.apexon.compass.constants.StrategyServiceConstants.LET;
import static com.apexon.compass.constants.StrategyServiceConstants.LINE_OF_CODE;
import static com.apexon.compass.constants.StrategyServiceConstants.PIPELINE;
import static com.apexon.compass.constants.StrategyServiceConstants.REMOVED_LINE_OF_CODE_TILL_DATE;

@Component
@AllArgsConstructor(onConstructor = @__({ @Autowired }))
@Slf4j
public class ScmConfigurationDao {

    private final MongoTemplate mongoTemplate;

    public List<CodeMetricsVo> getMemberWiseCodeMetrics(String projectId, List<String> repoIds) {
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
                new Document().append(LOOKUP_DOLLAR, new Document().append(FROM, SCM_CODE_STAT_ANALYSIS)
                    .append(LET,
                            new Document().append(PROJECT_ID, ID_UNDERSCORE_DOLLAR)
                                .append(SCM_REPO_URL, REPOS_SCM_REPO_URL_DOLLAR))
                    .append(PIPELINE, Collections.singletonList(new Document()
                        .append(MATCH_DOLLAR, new Document().append(AND_DOLLAR, Arrays.asList(
                                new Document().append(EXPRESSION_DOLLAR,
                                        new Document().append(AND_DOLLAR, Arrays.asList(
                                                new Document().append(EQUAL_DOLLAR,
                                                        Arrays.asList(REPO_URL_CAMEL_CASE_DOLLAR,
                                                                SCM_REPO_URL_DOUBLE_DOLLAR)),
                                                new Document().append(EQUAL_DOLLAR,
                                                        Arrays.asList(PROJECT_ID_DOLLAR, PROJECT_ID_DOUBLE_DOLLAR))))),
                                new Document().append(IS_ARCHIVE, false))))))
                    .append(AS, CODE_ANALYSIS)),
                new Document().append(UNWIND_DOLLAR, CODE_ANALYSIS_DOLLAR),
                new Document().append(UNWIND_DOLLAR, CODE_ANALYSIS_AUTHORS),
                new Document()
                    .append(FACET,
                            new Document()
                                .append(OVERALL_DATA,
                                        Collections.singletonList(new Document().append(
                                                GROUP_DOLLAR,
                                                new Document()
                                                    .append(ID_PRE_UNDER, REPOS_REPOID_DOLLAR)
                                                    .append(TOTAL_LINE_OF_CODE,
                                                            new Document().append(FIRST_DOLLAR,
                                                                    CODE_ANALYSIS_TOTAL_LINE_CODE)))))
                                .append(AUTHORS_DATA, Collections
                                    .singletonList(new Document().append(GROUP_DOLLAR, new Document()
                                        .append(ID_PRE_UNDER, CODE_ANALYSIS_DEVELOPER_NAME)
                                        .append(LINE_OF_CODE,
                                                new Document().append(SUM_DOLLAR, CODE_ANALYSIS_TOTAL_LOC))
                                        .append(CODE_CHURN, new Document().append(SUM_DOLLAR, CODE_ANALYSIS_CODE_CHURN))
                                        .append(LEGACY_REFACTOR,
                                                new Document().append(SUM_DOLLAR,
                                                        CODE_ANALYSIS_AUTHORS_LEGACY_REFACTOR))
                                        .append(ADDED_LOC_BY_AUTHOR_7_DAYS,
                                                new Document().append(SUM_DOLLAR,
                                                        CODE_ANALYSIS_ADDED_LOC_AUTHOR_7_DAYS))
                                        .append(REMOVED_LOC_BY_AUTHOR_7_DAYS,
                                                new Document().append(SUM_DOLLAR,
                                                        CODE_ANALYSIS_REMOVED_LOC_AUTHOR_7_DAYS))
                                        .append(ADDED_LOC_BY_AUTHOR_15_DAYS,
                                                new Document().append(SUM_DOLLAR,
                                                        CODE_ANALYSIS_ADDED_LOC_AUTHOR_15_DAYS))
                                        .append(REMOVED_LOC_BY_AUTHOR_15_DAYS,
                                                new Document().append(SUM_DOLLAR,
                                                        CODE_ANALYSIS_REMOVED_LOC_AUTHOR_15_DAYS))
                                        .append(ADDED_LOC_BY_AUTHOR_30_DAYS,
                                                new Document().append(SUM_DOLLAR,
                                                        CODE_ANALYSIS_ADDED_LOC_AUTHOR_30_DAYS))
                                        .append(REMOVED_LOC_BY_AUTHOR_30_DAYS,
                                                new Document().append(SUM_DOLLAR,
                                                        CODE_ANALYSIS_REMOVED_LOC_AUTHOR_30_DAYS))
                                        .append(ADDED_LOC_BY_AUTHOR_90_DAYS,
                                                new Document().append(SUM_DOLLAR,
                                                        CODE_ANALYSIS_ADDED_LOC_AUTHOR_90_DAYS))
                                        .append(REMOVED_LOC_BY_AUTHOR_90_DAYS,
                                                new Document().append(SUM_DOLLAR,
                                                        CODE_ANALYSIS_REMOVED_LOC_AUTHOR_90_DAYS))
                                        .append(ADDED_LINE_OF_CODE_TILL_DATE,
                                                new Document().append(SUM_DOLLAR, CODE_ANALYSIS_ADDED_LINE_OF_CODE))
                                        .append(REMOVED_LINE_OF_CODE_TILL_DATE,
                                                new Document().append(SUM_DOLLAR,
                                                        CODE_ANALYSIS_REMOVED_LINE_OF_CODE)))))),
                new Document().append(UNWIND_DOLLAR, OVERALL_DATA_DOLLAR),
                new Document().append(GROUP_DOLLAR,
                        new Document().append(ID_PRE_UNDER, ONE_DOUBLE)
                            .append(TOTAL_LINE_OF_CODE, new Document().append(SUM_DOLLAR, OVERALL_DATA_TOTAL_LOC))
                            .append(AUTHORS_DATA, new Document().append(FIRST_DOLLAR, AUTHORS_DATA_DOLLAR))));
        ObjectMapper mapper = new ObjectMapper();
        try {
            mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            return mapper.readValue(StrategyServiceUtils.documentsToJsonConverter(getDocuments(pipeline)),
                    new TypeReference<>() {
                    });
        }
        catch (JsonProcessingException processingException) {
            log.error(JSON_PROCESSING_LOG_MESSAGE + processingException.getMessage());
            throw new ServiceException(JSON_PROCESSING_EXCEPTION_MESSAGE);
        }
    }

    private List<Document> getDocuments(List<? extends Bson> pipeline) {
        List<Document> documents = mongoTemplate.getCollection(PROJECTS).aggregate(pipeline).into(new ArrayList<>());
        if (CollectionUtils.isEmpty(documents)) {
            throw new RecordNotFoundException("No record found for this project");
        }
        return documents;
    }

    public QualityMetricsVo getQualityMetrics(String projectId, List<String> repoIds) {
        List<? extends Bson> pipeline = Arrays.asList(
                new Document().append(MATCH_DOLLAR,
                        new Document().append(ID_PRE_UNDER, new ObjectId(projectId)).append(IS_DELETED, false)),
                new Document().append(PROJECT_DOLLAR,
                        new Document().append(ID_PRE_UNDER, ONE_DOUBLE).append(REPOS, ONE_DOUBLE)),
                new Document().append(UNWIND_DOLLAR, REPOS_DOLLAR),
                new Document().append(MATCH_DOLLAR, new Document().append(AND_DOLLAR,
                        Arrays.asList(new Document().append(REPOS_REPOID, new Document().append(IN_DOLLAR, repoIds)),
                                new Document().append(REPOS_SONAR_PROJECTID,
                                        new Document().append(EXISTS_DOLLAR, true))))),
                new Document().append(LOOKUP_DOLLAR, new Document().append(FROM, SONAR_STATS)
                    .append(LET,
                            new Document().append(PROJECT_ID, ID_UNDERSCORE_DOLLAR)
                                .append(PROJECTKEY, REPOS_SONAR_PROJECTID_DOLLAR))
                    .append(PIPELINE, Arrays.asList(
                            new Document().append(MATCH_DOLLAR,
                                    new Document().append(EXPRESSION_DOLLAR, new Document().append(AND_DOLLAR,
                                            Arrays.asList(
                                                    new Document().append(EQUAL_DOLLAR,
                                                            Arrays.asList(SONAR_PROJECT_ID_QUERY_PRAM,
                                                                    PROJECTKEY_QUERY_PRAM)),
                                                    new Document().append(EQUAL_DOLLAR,
                                                            Arrays.asList(PROJECT_ID_DOLLAR,
                                                                    PROJECT_ID_DOUBLE_DOLLAR)))))),
                            new Document().append(SORT_DOLLAR, new Document().append(CREATED_DATE, -ONE_DOUBLE)),
                            new Document().append(LIMIT_DOLLAR, ONE_DOUBLE)))
                    .append(AS, QUALITY_METRICS_SUMMARY)),
                new Document().append(UNWIND_DOLLAR, QUALITY_METRICS_SUMMARY_DOLLAR),
                new Document().append(PROJECT_DOLLAR,
                        new Document().append(ID_PRE_UNDER, ZERO_DOUBLE)
                            .append(OVERALL_BUGS, QUALITY_METRICS_SUMMARY_QUALITY_METRICS_BUGS)
                            .append(OVERALL_VULNERABILITIES, QUALITY_METRICS_SUMMARY_VIOLATIONS_TOTAL)
                            .append(OVERALL_CODE_COVERAGE, QUALITY_METRICS_SUMMARY_QM_COVERAGE)
                            .append(OVERALL_DUPLICATIONS, QUALITY_METRICS_SUMMARY_DOLLAR_DUPLICATION_LINEDENSITY)
                            .append(OVERALL_DUPLICATIONS_BLOCKS, QUALITY_METRICS_SUMMARY_DUPLICATION_BLOCKS)
                            .append(NEW_BUGS, QUALITY_METRICS_SUMMARY_QUALITY_MATRIX_NEW_BUGS)
                            .append(NEW_VULNERABILITIES, QUALITY_METRICS_SUMMARY_NEW_VIOLATIONS_TOTAL)
                            .append(NEW_CODE_COVERAGE, QUALITY_METRICS_SUMMARY_QUALITY_MATRIX_NEW_COVERAGE)
                            .append(NEW_NEW_LINE_OF_CODES, QUALITY_METRICS_SUMMARY_CODE_MATRIX_NCLOC)
                            .append(NEW_DUPLICATIONS, QUALITY_METRICS_SUMMARY_CODE_DUP_NEW_LINE_DENSITY)
                            .append(NEW_DUPLICATIONS_LINES, QUALITY_METRICS_SUMMARY_CODE_DUP_NEW_LINES)
                            .append(OVERALL_CODE_CALCULATION,
                                    new Document().append(MULTIPLY_DOLLAR,
                                            Arrays.asList(QUALITY_METRICS_SUMMARY_CODE_MATRIX_NCLOC,
                                                    QUALITY_METRICS_SUMMARY_QM_COVERAGE)))
                            .append(NEW_CODE_CODE_CALCULATION,
                                    new Document().append(MULTIPLY_DOLLAR,
                                            Arrays.asList(QUALITY_METRICS_SUMMARY_CODE_MATRIX_NCLOC,
                                                    QUALITY_METRICS_SUMMARY_QUALITY_MATRIX_NEW_COVERAGE)))),
                new Document().append(GROUP_DOLLAR, new Document().append(ID_PRE_UNDER, ONE_DOUBLE)
                    .append(OVERALL_BUGS, new Document().append(SUM_DOLLAR, OVERALL_BUGS_DOLLAR))
                    .append(OVERALL_VULNERABILITIES, new Document().append(SUM_DOLLAR, OVERALL_VULNERABILITIES_DOLLAR))
                    .append(OVERALL_DUPLICATIONS, new Document().append(AVG, OVERALL_DUPLICATIONS_DOLLAR))
                    .append(OVERALL_DUPLICATIONS_BLOCKS,
                            new Document().append(SUM_DOLLAR, OVERALL_DUPLICATIONS_BLOCKS_DOLLAR))
                    .append(NEW_BUGS, new Document().append(SUM_DOLLAR, NEW_BUGS_DOLLAR))
                    .append(NEW_VULNERABILITIES, new Document().append(SUM_DOLLAR, NEW_VULNERABILITIES_DOLLAR))
                    .append(NEW_NEW_LINE_OF_CODES, new Document().append(SUM_DOLLAR, NEW_NEW_LINE_OF_CODES_DOLLAR))
                    .append(NEW_DUPLICATIONS, new Document().append(AVG, NEW_DUPLICATIONS_DOLLAR))
                    .append(NEW_DUPLICATIONS_LINES, new Document().append(SUM_DOLLAR, NEW_DUPLICATIONS_LINES_DOLLAR))
                    .append(OVERALL_CODE_CALCULATION,
                            new Document().append(SUM_DOLLAR, OVERALL_CODE_CALCULATION_DOLLAR))
                    .append(NEW_CODE_CODE_CALCULATION,
                            new Document().append(SUM_DOLLAR, NEW_CODE_CODE_CALCULATION_DOLLAR))),
                new Document().append(PROJECT_DOLLAR, new Document().append(OVERALL_BUGS, ONE_DOUBLE)
                    .append(OVERALL_VULNERABILITIES, ONE_DOUBLE)
                    .append(OVERALL_CODE_COVERAGE,
                            new Document().append(DIVIDE_DOLLAR,
                                    Arrays.asList(OVERALL_CODE_CALCULATION_DOLLAR, NEW_NEW_LINE_OF_CODES_DOLLAR)))
                    .append(OVERALL_DUPLICATIONS, ONE_DOUBLE)
                    .append(OVERALL_DUPLICATIONS_BLOCKS, ONE_DOUBLE)
                    .append(NEW_BUGS, ONE_DOUBLE)
                    .append(NEW_VULNERABILITIES, ONE_DOUBLE)
                    .append(NEW_CODE_COVERAGE,
                            new Document().append(DIVIDE_DOLLAR,
                                    Arrays.asList(NEW_CODE_CODE_CALCULATION_DOLLAR, NEW_NEW_LINE_OF_CODES_DOLLAR)))
                    .append(NEW_NEW_LINE_OF_CODES, ONE_DOUBLE)
                    .append(NEW_DUPLICATIONS, ONE_DOUBLE)
                    .append(NEW_DUPLICATIONS_LINES, ONE_DOUBLE)));

        try {
            MongoCollection<Document> collection = mongoTemplate.getCollection(PROJECTS);
            Document document = collection.aggregate(pipeline).first();
            if (Objects.isNull(document)) {
                throw new RecordNotFoundException("No record found");
            }
            return QualityMetricsVo.builder()
                .overallBugs(document.getInteger("overallBugs"))
                .overallVulnerabilities(document.getInteger("overallVulnerabilities"))
                .overallCodeCoverage(document.getDouble("overallCodeCoverage"))
                .overallDuplications(document.getDouble("overallDuplications"))
                .overallDuplicationBlocks(document.getInteger("overallDuplicationBlocks"))
                .newBugs(document.getInteger("newBugs"))
                .newVulnerabilities(document.getInteger("newVulnerabilities"))
                .newCodeCoverage(document.getDouble("newCodeCoverage"))
                .newDuplicationLines(document.getInteger("newDuplicationLines"))
                .newDuplications(document.getDouble("newDuplications"))
                .newNewLineOfCodes(document.getInteger("newNewLineOfCodes"))
                .build();
        }
        catch (MongoException ex) {
            throw new ServiceException("Something went wrong while performing DB operation");
        }
    }

    public LanguageSpecificViolationsVo getViolationsSummary(String projectId, List<String> repoIds) {
        List<? extends Bson> pipeline = Arrays.asList(
                new Document().append(MATCH_DOLLAR,
                        new Document().append(ID_PRE_UNDER, new ObjectId(projectId)).append(IS_DELETED, false)),
                new Document().append(PROJECT_DOLLAR,
                        new Document().append(ID_PRE_UNDER, ONE_DOUBLE).append(REPOS, ONE_DOUBLE)),
                new Document().append(UNWIND_DOLLAR, REPOS_DOLLAR),
                new Document().append(MATCH_DOLLAR, new Document().append(AND_DOLLAR,
                        Arrays.asList(new Document().append(REPOS_REPOID, new Document().append(IN_DOLLAR, repoIds)),
                                new Document().append(REPOS_SONAR_PROJECTID,
                                        new Document().append(EXISTS_DOLLAR, true))))),
                new Document().append(LOOKUP_DOLLAR, new Document().append(FROM, SONAR_CONFIGURATION)
                    .append(LET, new Document().append("projectId", ID_UNDERSCORE_DOLLAR))
                    .append(PIPELINE, Collections.singletonList(new Document().append(MATCH_DOLLAR,
                            new Document().append(EXPRESSION_DOLLAR,
                                    new Document().append(EQUAL_DOLLAR, Arrays.asList("$projectId", "$$projectId"))))))
                    .append(AS, SONAR_CONFIG_DATA)),
                new Document().append(UNWIND_DOLLAR, SONAR_CONFIG_DATA_DOLLAR),
                new Document().append(UNWIND_DOLLAR, SONAR_CONFIG_DATA_PROJECTS),
                new Document().append(PROJECT_DOLLAR, new Document().append(REPOS, ONE_DOUBLE)
                    .append(SONAR_CONFIG_DATA, ONE_DOUBLE)
                    .append(CMP,
                            new Document().append(CMP_DOLLAR,
                                    Arrays.asList(REPOS_SONAR_PROJECTID_DOLLAR, SONAR_CONFIG_DATA_PROJECTS_KEY)))),
                new Document().append(MATCH_DOLLAR, new Document().append(CMP, ZERO_DOUBLE)),
                new Document().append(LOOKUP_DOLLAR, new Document().append(FROM, SONAR_STATS)
                    .append(LET,
                            new Document().append("projectId", ID_UNDERSCORE_DOLLAR)
                                .append(PROJECTKEY, REPOS_SONAR_PROJECTID_DOLLAR))
                    .append(PIPELINE,
                            Arrays.asList(
                                    new Document().append(MATCH_DOLLAR,
                                            new Document().append(EXPRESSION_DOLLAR,
                                                    new Document().append(AND_DOLLAR, Arrays.asList(
                                                            new Document().append(EQUAL_DOLLAR,
                                                                    Arrays.asList(SONAR_PROJECT_ID_QUERY_PRAM,
                                                                            PROJECTKEY_QUERY_PRAM)),
                                                            new Document().append(EQUAL_DOLLAR,
                                                                    Arrays.asList("$projectId", "$$projectId")))))),
                                    new Document().append("$sort", new Document().append("createdDate", -ONE_DOUBLE)),
                                    new Document().append("$limit", ONE_DOUBLE),
                                    new Document().append(PROJECT_DOLLAR,
                                            new Document().append(VIOLATIONS, VIOLATIONS_TOTAL))))
                    .append(AS, VIOLATIONS)),
                new Document().append(UNWIND_DOLLAR, VIOLATIONS_DOLLAR),
                new Document().append(PROJECT_DOLLAR,
                        new Document().append(REPO_ID, REPOS_REPOID_DOLLAR)
                            .append(REPO_NAME, REPOS_REPONAME_DOLLAR)
                            .append(LANGUAGE, SONAR_CONFIG_DATA_PROJECTS_LANG)
                            .append(VIOLATIONS, VIOLATIONS_DOT_VIOLATIONS)),
                new Document().append(GROUP_DOLLAR, new Document().append(ID_PRE_UNDER, ID_UNDERSCORE_DOLLAR)
                    .append(REPOS_LANGUAGE,
                            new Document().append(PUSH_DOLLAR, new Document().append(LANGUAGE, LANGUAGE_DOLLAR)))
                    .append(VIOLATIONS, new Document().append(SUM_DOLLAR, VIOLATIONS_DOLLAR))));

        try {
            MongoCollection<Document> collection = mongoTemplate.getCollection(PROJECTS);
            Document document = collection.aggregate(pipeline).first();
            if (Objects.isNull(document)) {
                throw new RecordNotFoundException("No record found");
            }
            return LanguageSpecificViolationsVo.builder()
                .language(document.getString("language"))
                .violations(document.getInteger("violations"))
                .build();
        }
        catch (MongoException ex) {
            throw new ServiceException("Something went wrong while performing DB operation");
        }
    }

}
