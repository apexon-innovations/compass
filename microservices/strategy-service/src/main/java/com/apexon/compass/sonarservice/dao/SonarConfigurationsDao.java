package com.apexon.compass.sonarservice.dao;

import static com.apexon.compass.constants.ClientDashboardServiceConstants.IS_DELETED;
import static com.apexon.compass.constants.ClientDashboardServiceConstants.MONGO_EXCEPTION_MESSAGE;
import static com.apexon.compass.constants.EntitiesConstants.*;
import static com.apexon.compass.constants.EntitiesConstants.PROJECTS;
import static com.apexon.compass.constants.PsrServiceConstants.ID;
import static com.apexon.compass.constants.QueryConstants.*;
import static com.apexon.compass.constants.QueryConstants.SONAR_STATS;
import static com.apexon.compass.constants.StrategyServiceConstants.ADD;
import static com.apexon.compass.constants.StrategyServiceConstants.AND;
import static com.apexon.compass.constants.StrategyServiceConstants.AS;
import static com.apexon.compass.constants.StrategyServiceConstants.AVG;
import static com.apexon.compass.constants.StrategyServiceConstants.BRANCHES;
import static com.apexon.compass.constants.StrategyServiceConstants.CASE;
import static com.apexon.compass.constants.StrategyServiceConstants.COMPLEXITY;
import static com.apexon.compass.constants.StrategyServiceConstants.CREATED_DATE;
import static com.apexon.compass.constants.StrategyServiceConstants.DEFAULT;
import static com.apexon.compass.constants.StrategyServiceConstants.DUPLICATION;
import static com.apexon.compass.constants.StrategyServiceConstants.EFFICIENCY;
import static com.apexon.compass.constants.StrategyServiceConstants.EQUAL;
import static com.apexon.compass.constants.StrategyServiceConstants.EXPRESSION;
import static com.apexon.compass.constants.StrategyServiceConstants.FROM;
import static com.apexon.compass.constants.StrategyServiceConstants.GROUP;
import static com.apexon.compass.constants.StrategyServiceConstants.GTE;
import static com.apexon.compass.constants.StrategyServiceConstants.ID_PRE_UNDER;
import static com.apexon.compass.constants.StrategyServiceConstants.ISSUES;
import static com.apexon.compass.constants.StrategyServiceConstants.LET;
import static com.apexon.compass.constants.StrategyServiceConstants.LIMIT;
import static com.apexon.compass.constants.StrategyServiceConstants.LOOKUP;
import static com.apexon.compass.constants.StrategyServiceConstants.LTE;
import static com.apexon.compass.constants.StrategyServiceConstants.MAINTAINABILITY;
import static com.apexon.compass.constants.StrategyServiceConstants.MATCH;
import static com.apexon.compass.constants.StrategyServiceConstants.MULTIPLY;
import static com.apexon.compass.constants.StrategyServiceConstants.PIPELINE;
import static com.apexon.compass.constants.StrategyServiceConstants.PROJECT;
import static com.apexon.compass.constants.StrategyServiceConstants.PROJECTKEY;
import static com.apexon.compass.constants.StrategyServiceConstants.PROJECTKEY_QUERY_PRAM;
import static com.apexon.compass.constants.StrategyServiceConstants.PROJECT_KEY;
import static com.apexon.compass.constants.StrategyServiceConstants.QUAILTY_MATRIX_COVERAGE;
import static com.apexon.compass.constants.StrategyServiceConstants.QUALITY_MEASUREMENTS;
import static com.apexon.compass.constants.StrategyServiceConstants.QUALITY_MEASUREMENTS_QUALITY_MATRIX_COVERAGE;
import static com.apexon.compass.constants.StrategyServiceConstants.QUALITY_MEASUREMENTS_RATINGS_MAINTAINABILITY;
import static com.apexon.compass.constants.StrategyServiceConstants.QUALITY_MEASUREMENTS_RATINGS_RELIABILITY;
import static com.apexon.compass.constants.StrategyServiceConstants.QUALITY_MEASUREMENTS_RATINGS_SECURITY;
import static com.apexon.compass.constants.StrategyServiceConstants.QUALITY_MEASUREMENT_VIOLATION_BLOCKER_AND_CRITICAL;
import static com.apexon.compass.constants.StrategyServiceConstants.RATINGS_MAINTAINABILITY;
import static com.apexon.compass.constants.StrategyServiceConstants.RATINGS_RELIABILITY;
import static com.apexon.compass.constants.StrategyServiceConstants.RATINGS_SECURITY;
import static com.apexon.compass.constants.StrategyServiceConstants.RELIABILITY;
import static com.apexon.compass.constants.StrategyServiceConstants.ROBUSTNESS;
import static com.apexon.compass.constants.StrategyServiceConstants.SECURITY;
import static com.apexon.compass.constants.StrategyServiceConstants.SIZE;
import static com.apexon.compass.constants.StrategyServiceConstants.SONARPROJECTID;
import static com.apexon.compass.constants.StrategyServiceConstants.SONAR_PROJECT_ID;
import static com.apexon.compass.constants.StrategyServiceConstants.SONAR_PROJECT_ID_QUERY_PRAM;
import static com.apexon.compass.constants.StrategyServiceConstants.SORT;
import static com.apexon.compass.constants.StrategyServiceConstants.SUM;
import static com.apexon.compass.constants.StrategyServiceConstants.SWITCH;
import static com.apexon.compass.constants.StrategyServiceConstants.TECHNICAL_DEBT_SUMMARY;
import static com.apexon.compass.constants.StrategyServiceConstants.TECHNICAL_DEBT_SUMMARY_SONAR_CONFIGURATION_ID;
import static com.apexon.compass.constants.StrategyServiceConstants.TECH_DEBT_SUMMARY_CODE_MATRIX_LINES;
import static com.apexon.compass.constants.StrategyServiceConstants.TECH_DEBT_SUMMARY_DUPLICATION_LINES;
import static com.apexon.compass.constants.StrategyServiceConstants.TECH_DEBT_SUMMARY_ISSUES_CONFIRMED;
import static com.apexon.compass.constants.StrategyServiceConstants.TECH_DEBT_SUMMARY_ISSUES_OPEN;
import static com.apexon.compass.constants.StrategyServiceConstants.TECH_DEBT_SUMMARY_ISSUES_REOPENED;
import static com.apexon.compass.constants.StrategyServiceConstants.TECH_DEBT_SUMMARY_QUALITY_MATRICES_BUGS;
import static com.apexon.compass.constants.StrategyServiceConstants.TECH_DEBT_SUMMARY_QUALITY_MATRICES_CODE_SMELLS;
import static com.apexon.compass.constants.StrategyServiceConstants.TECH_DEBT_SUMMARY_QUALITY_MATRICES_COVERAGE;
import static com.apexon.compass.constants.StrategyServiceConstants.TECH_DEBT_SUMMARY_QUERY_PARAM;
import static com.apexon.compass.constants.StrategyServiceConstants.TECH_DEBT_SUMMARY_QUERY_PARAM_COMPLEXITY;
import static com.apexon.compass.constants.StrategyServiceConstants.TECH_DEBT_SUMMARY_QUERY_PARAM_CONGNITIVE_COMPLEXITY;
import static com.apexon.compass.constants.StrategyServiceConstants.TECH_DEBT_SUMMARY_SECURITY_NEW_VULNERABILITIES;
import static com.apexon.compass.constants.StrategyServiceConstants.TECH_DEBT_SUMMARY_SECURITY_VULNERABILITIES;
import static com.apexon.compass.constants.StrategyServiceConstants.TESTS;
import static com.apexon.compass.constants.StrategyServiceConstants.TEST_CODE_COVERAGE;
import static com.apexon.compass.constants.StrategyServiceConstants.THEN;
import static com.apexon.compass.constants.StrategyServiceConstants.VIOLATIONS_BLOCKER;
import static com.apexon.compass.constants.StrategyServiceConstants.VIOLATIONS_CRITICAL;
import static com.apexon.compass.constants.StrategyServiceConstants.VULNERABILITIES;
import static com.apexon.compass.sonarservice.constants.NumericConstants.EIGHTY_DECIMAL_ZERO;
import static com.apexon.compass.sonarservice.constants.NumericConstants.ELEVEN_DECIMAL_ZERO;
import static com.apexon.compass.sonarservice.constants.NumericConstants.FIFTY_DECIMAL_ZERO;
import static com.apexon.compass.sonarservice.constants.NumericConstants.FIFTY_ONE_DECIMAL_ZERO;
import static com.apexon.compass.sonarservice.constants.NumericConstants.HUNDRED_DECIMAL_ZERO;
import static com.apexon.compass.sonarservice.constants.NumericConstants.NEGATIVE_ONE_DECIMAL_ZERO;
import static com.apexon.compass.sonarservice.constants.NumericConstants.NINTY_DECIMAL_ZERO;
import static com.apexon.compass.sonarservice.constants.NumericConstants.ONE_DECIMAL_ZERO;
import static com.apexon.compass.sonarservice.constants.NumericConstants.ONE_HUNDRED_ONE_DECIMAL_ZERO;
import static com.apexon.compass.sonarservice.constants.NumericConstants.SEVENTY_DECIMAL_ZERO;
import static com.apexon.compass.sonarservice.constants.NumericConstants.SIXTY_DECIMAL_ZERO;
import static com.apexon.compass.sonarservice.constants.NumericConstants.TEN_DECIMAL_ZERO;
import static com.apexon.compass.sonarservice.constants.NumericConstants.THREE_HUNDRED_DECIMAL_ZERO;
import static com.apexon.compass.sonarservice.constants.NumericConstants.THREE_HUNDRED_ONE_DECIMAL_ZERO;
import static com.apexon.compass.sonarservice.constants.NumericConstants.TWENTY_DECIMAL_ZERO;
import static com.apexon.compass.sonarservice.constants.NumericConstants.TWENTY_FIVE_DECIMAL_ZERO;
import static com.apexon.compass.sonarservice.constants.NumericConstants.TWENTY_SIX_DECIMAL_ZERO;
import static com.apexon.compass.sonarservice.constants.NumericConstants.TWO_HUNDRED_DECIMAL_ZERO;
import static com.apexon.compass.sonarservice.constants.NumericConstants.TWO_HUNDRED_ONE_DECIMAL_ZERO;
import static com.apexon.compass.sonarservice.constants.NumericConstants.ZERO_DECIMAL_TWO;
import static com.apexon.compass.sonarservice.constants.NumericConstants.ZERO_DECIMAL_ZERO;

import com.apexon.compass.aggregation.vo.*;
import com.apexon.compass.constants.QueryConstants;
import com.apexon.compass.exception.custom.RecordNotFoundException;
import com.apexon.compass.exception.custom.ServiceException;
import com.mongodb.MongoException;
import com.mongodb.client.MongoCollection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
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
public class SonarConfigurationsDao {

    private final MongoTemplate mongoTemplate;

    public RiskMeasurementsVo getRiskMeasurements(String projectId, List<String> repoIds, Long startDate,
            Long endDate) {
        log.info("=====getRiskMeasurements started with projectId- {}, repoIds- {}," + "startDate- {}, endDate- {}",
                projectId, repoIds, startDate, endDate);
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
                            new Document().append(PROJECT_ID_CAPITALIZED, ID_UNDERSCORE_DOLLAR)
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
                            new Document().append(PROJECT_DOLLAR,
                                    new Document().append(VIOLATIONS_CRITICAL, ONE_DOUBLE)
                                        .append(CREATED_DATE, ONE_DOUBLE)
                                        .append(SONAR_PROJECT_ID, PROJECTKEY_QUERY_PRAM))))
                    .append(AS, RISK_MEASUREMENTS)),
                new Document().append(UNWIND_DOLLAR, RISK_MEASUREMENTS_DOLLAR),
                new Document()
                    .append(MATCH_DOLLAR,
                            new Document().append(AND_DOLLAR,
                                    Arrays.asList(
                                            new Document().append(EXPRESSION_DOLLAR,
                                                    new Document().append(LTE_DOLLAR,
                                                            Arrays.asList(RISK_MEASUREMENTS_CREATED_DATE_DOLLAR,
                                                                    endDate))),
                                            new Document().append(EXPRESSION_DOLLAR,
                                                    new Document().append(GTE_DOLLAR,
                                                            Arrays.asList(RISK_MEASUREMENTS_CREATED_DATE_DOLLAR,
                                                                    startDate)))))),
                new Document().append(SORT_DOLLAR, new Document().append(RISK_MEASUREMENTS_CREATED_DATE, -ONE_DOUBLE)),
                new Document().append(GROUP_DOLLAR,
                        new Document().append(ID_PRE_UNDER, REPOS_REPOID_DOLLAR)
                            .append(PROJECT_ID_CAPITALIZED, new Document().append(FIRST_DOLLAR, ID_UNDERSCORE_DOLLAR))
                            .append(DATA, new Document().append(PUSH_DOLLAR, RISK_MEASUREMENTS_DOLLAR))),
                new Document().append(PROJECT_DOLLAR, new Document().append(ID_PRE_UNDER, ONE_DOUBLE)
                    .append(PROJECT_ID_CAPITALIZED, ONE_DOUBLE)
                    .append(CURRENT_DATA,
                            new Document().append(ARRAY_ELEM_AT_DOLLAR, Arrays.asList(DATA_WITH_DOLLAR_SIGN, 0.0)))
                    .append(PREVIOUS_DATA,
                            new Document().append(ARRAY_ELEM_AT_DOLLAR,
                                    Arrays.asList(DATA_WITH_DOLLAR_SIGN, -ONE_DOUBLE)))),
                new Document().append(GROUP_DOLLAR,
                        new Document().append(ID_PRE_UNDER, PROJECT_ID_DOLLAR)
                            .append(PROJECT_CAPS,
                                    new Document().append(PUSH_DOLLAR,
                                            new Document().append(CURRENT_DATA, CURRENT_DATA_DOLLAR)
                                                .append(PREVIOUS_DATA, PREVIOUS_DATA_DOLLAR)))));

        try {
            return getDocuments(PROJECTS, pipeline, RiskMeasurementsVo.class).get(0);
        }
        catch (MongoException e) {
            log.error(e.getMessage());
            throw new ServiceException(MONGO_EXCEPTION_MESSAGE);
        }
    }

    private <T> List<T> getDocuments(String collection, List<? extends Bson> pipeline, Class<T> clazz) {
        List<T> documents = mongoTemplate.getCollection(collection)
            .aggregate(pipeline)
            .map(document -> mongoTemplate.getConverter().read(clazz, document))
            .into(new ArrayList<>());
        if (CollectionUtils.isEmpty(documents)) {
            throw new RecordNotFoundException("Data is not available");
        }
        return documents;
    }

    public SonarStateMeasuresAverageCalculationVo findQualityMeasurements(String projectId, List<String> repoIds) {
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
                            new Document().append(PROJECT_ID_CAPITALIZED, ID_UNDERSCORE_DOLLAR)
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
                            new Document().append(LIMIT_DOLLAR, ONE_DOUBLE),
                            new Document().append(PROJECT_DOLLAR,
                                    new Document().append(RATINGS_SECURITY, ONE_DOUBLE)
                                        .append(RATINGS_MAINTAINABILITY, ONE_DOUBLE)
                                        .append(RATINGS_RELIABILITY, ONE_DOUBLE)
                                        .append(SONAR_PROJECT_ID, PROJECTKEY_QUERY_PRAM))))
                    .append(AS, QUALITY_MEASUREMENTS)),
                new Document().append(UNWIND_DOLLAR, QUALITY_MEASUREMENTS_PARAM),
                new Document()
                    .append(PROJECT_DOLLAR,
                            new Document().append(SECURITY, QUALITY_MEASUREMENTS_RATINGS_SECURITY)
                                .append(EFFICIENCY, QUALITY_MEASUREMENTS_RATINGS_RELIABILITY)
                                .append(ROBUSTNESS, QUALITY_MEASUREMENTS_RATINGS_MAINTAINABILITY)),
                new Document().append(GROUP_DOLLAR,
                        new Document().append(ID_PRE_UNDER, ID_UNDERSCORE_DOLLAR)
                            .append(COUNT, new Document().append(SUM_DOLLAR, ONE_DOUBLE))
                            .append(SECURITY, new Document().append(SUM_DOLLAR, SECURITY_DOLLAR))
                            .append(EFFICIENCY, new Document().append(SUM_DOLLAR, EFFICIENCY_DOLLAR))
                            .append(ROBUSTNESS, new Document().append(SUM_DOLLAR, ROBUSTNESS_DOLLAR))),
                new Document()
                    .append(PROJECT_DOLLAR,
                            new Document()
                                .append(SECURITY,
                                        new Document().append(DIVIDE_DOLLAR,
                                                Arrays.asList(SECURITY_DOLLAR, COUNT_DOLLAR)))
                                .append(EFFICIENCY,
                                        new Document().append(DIVIDE_DOLLAR,
                                                Arrays.asList(EFFICIENCY_DOLLAR, COUNT_DOLLAR)))
                                .append(ROBUSTNESS, new Document().append(DIVIDE_DOLLAR,
                                        Arrays.asList(ROBUSTNESS_DOLLAR, COUNT_DOLLAR)))));

        try {
            Document document = mongoTemplate.getCollection(PROJECTS).aggregate(pipeline).first();
            if (Objects.isNull(document)) {
                throw new RecordNotFoundException(RECORD_NOT_FOUND);
            }
            return SonarStateMeasuresAverageCalculationVo.builder()
                .efficiency(document.getDouble(EFFICIENCY))
                .security(document.getDouble(SECURITY))
                .robustness(document.getDouble(ROBUSTNESS))
                .build();
        }
        catch (MongoException ex) {
            throw new ServiceException(MONGO_EXCEPTION_MESSAGE);
        }
    }

    public TechnicalDebtVo getTechnicalDebtSummary(String projectId, List<String> repoIds) {
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
                            new Document().append(PROJECT_ID_CAPITALIZED, ID_UNDERSCORE_DOLLAR)
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
                    .append(AS, TECHNICAL_DEBT_SUMMARY)),
                new Document().append(UNWIND_DOLLAR, TECH_DEBT_SUMMARY_QUERY_PARAM),
                new Document().append(PROJECT_DOLLAR, new Document()
                    .append(COMPLEXITY,
                            new Document().append(ADD_DOLLAR,
                                    Arrays.asList(TECH_DEBT_SUMMARY_DOT_COMPLEXITY,
                                            TECH_DEBT_SUMMARY_DOT_CONG_COMPLEXITY_DOLLAR)))
                    .append(DUPLICATION,
                            new Document().append(COND_DOLLAR, Arrays.asList(
                                    new Document().append(EQUAL_DOLLAR,
                                            Arrays.asList(TECH_DEBT_SUMMARY_CODE_MATRIX_LINES, ZERO_DOUBLE)),
                                    ZERO_DOUBLE,
                                    new Document().append(MULTIPLY_DOLLAR, Arrays.asList(
                                            new Document().append(DIVIDE_DOLLAR,
                                                    Arrays.asList(TECH_DEBT_SUMMARY_QUERY_PARAM_DUPLICATION_LINES,
                                                            TECH_DEBT_SUMMARY_CODE_MATRIX_LINES)),
                                            ONE_HUNDRED_DOUBLE)))))
                    .append(ISSUES,
                            new Document().append(ADD_DOLLAR,
                                    Arrays.asList(TECH_DEBT_SUMMARY_ISSUES_OPEN, TECH_DEBT_SUMMARY_ISSUES_CONFIRMED,
                                            TECH_DEBT_SUMMARY_ISSUES_REOPENED)))
                    .append(MAINTAINABILITY, TECH_DEBT_SUMMARY_TECH_DEPT_INDEX)
                    .append(RELIABILITY, TECH_DEBT_SUMMARY_RELIABILITY_EFFORTS)
                    .append(SECURITY, TECH_DEBT_SUMMARY_SECURITY_EFFORTS)
                    .append(SIZE, TECH_DEBT_SUMMARY_CODE_MATRIX_LINES)
                    .append(TESTS, TECH_DEBT_SUMMARY_QUALITY_MATRICES_COVERAGE)
                    .append(TEST_CALCULATION,
                            new Document().append(MULTIPLY_DOLLAR,
                                    Arrays.asList(TECH_DEBT_SUMMARY_DOT_CODEMATRIX_DOT_NCLOC_DOLLAR,
                                            TECH_DEBT_SUMMARY_QUALITY_MATRICES_COVERAGE)))),
                new Document().append(GROUP_DOLLAR,
                        new Document().append(ID_PRE_UNDER, ID_UNDERSCORE_DOLLAR)
                            .append(COMPLEXITY, new Document().append(SUM_DOLLAR, COMPLEXITY_DOLLAR))
                            .append(DUPLICATION, new Document().append(AVG, DUPLICATION_DOLLAR))
                            .append(ISSUES, new Document().append(SUM_DOLLAR, ISSUES_DOLLAR))
                            .append(MAINTAINABILITY, new Document().append(SUM_DOLLAR, MAINTAINABILITY_DOLLAR))
                            .append(RELIABILITY, new Document().append(SUM_DOLLAR, RELIABILITY_DOLLAR))
                            .append(SECURITY, new Document().append(SUM_DOLLAR, SECURITY_DOLLAR))
                            .append(SIZE, new Document().append(SUM_DOLLAR, SIZE_DOLLAR))
                            .append(TEST_CALCULATION, new Document().append(SUM_DOLLAR, TEST_CALCULATION_DOLLAR))),
                new Document().append(PROJECT_DOLLAR,
                        new Document().append(COMPLEXITY, ONE_DOUBLE)
                            .append(DUPLICATION, ONE_DOUBLE)
                            .append(ISSUES, ONE_DOUBLE)
                            .append(MAINTAINABILITY, ONE_DOUBLE)
                            .append(RELIABILITY, ONE_DOUBLE)
                            .append(SECURITY, ONE_DOUBLE)
                            .append(SIZE, ONE_DOUBLE)
                            .append(TESTS, new Document().append(DIVIDE_DOLLAR,
                                    Arrays.asList(TEST_CALCULATION_DOLLAR, SIZE_DOLLAR)))));

        try {
            MongoCollection<Document> collection = mongoTemplate.getCollection(PROJECTS);
            Document document = collection.aggregate(pipeline).first();
            if (Objects.isNull(document)) {
                throw new RecordNotFoundException("No record found");
            }
            return TechnicalDebtVo.builder()
                .complexity(document.getInteger("complexity"))
                .duplication(document.getDouble("duplication"))
                .issues(document.getInteger("issues"))
                .maintainability(document.getInteger("maintainability"))
                .reliability(document.getInteger("reliability"))
                .security(document.getDouble("security"))
                .size(document.getInteger("size"))
                .tests(document.getDouble("tests"))
                .build();
        }
        catch (MongoException ex) {
            throw new ServiceException("Something went wrong while performing DB operation");
        }
    }

    private Document getLookupStageForMeasuresCalculation() {
        return new Document()
            .append(LOOKUP,
                    new Document().append(FROM, SONAR_STATS)
                        .append(LET, new Document().append(PROJECTKEY, PROJECT_KEY))
                        .append(PIPELINE,
                                Arrays.asList(
                                        getMatchStage(EXPRESSION,
                                                new Document().append(EQUAL,
                                                        Arrays.asList(SONAR_PROJECT_ID_QUERY_PRAM,
                                                                PROJECTKEY_QUERY_PRAM))),
                                        new Document().append(SORT,
                                                new Document().append(CREATED_DATE, NEGATIVE_ONE_DECIMAL_ZERO)),
                                        new Document().append(LIMIT, ONE_DECIMAL_ZERO),
                                        new Document().append(PROJECT,
                                                new Document().append(RATINGS_SECURITY, ONE_DECIMAL_ZERO)
                                                    .append(RATINGS_MAINTAINABILITY, ONE_DECIMAL_ZERO)
                                                    .append(RATINGS_RELIABILITY, ONE_DECIMAL_ZERO)
                                                    .append(SONARPROJECTID, PROJECTKEY_QUERY_PRAM))))
                        .append(AS, QUALITY_MEASUREMENTS));
    }

    private Document getGroupStageForMeasuresCalculation() {
        return new Document().append(GROUP,
                new Document().append(ID, ONE_DECIMAL_ZERO)
                    .append(SECURITY, new Document().append(AVG, QUALITY_MEASUREMENTS_RATINGS_SECURITY))
                    .append(EFFICIENCY, new Document().append(AVG, QUALITY_MEASUREMENTS_RATINGS_RELIABILITY))
                    .append(ROBUSTNESS, new Document().append(AVG, QUALITY_MEASUREMENTS_RATINGS_MAINTAINABILITY)));
    }

    private Document getLookupStageForTechnicalDebt() {
        return new Document()
            .append(LOOKUP,
                    new Document().append(FROM, SONAR_STATS)
                        .append(LET, new Document().append(PROJECTKEY, PROJECT_KEY))
                        .append(PIPELINE,
                                Arrays.asList(
                                        getMatchStage(EXPRESSION,
                                                new Document().append(EQUAL,
                                                        Arrays.asList(SONAR_PROJECT_ID_QUERY_PRAM,
                                                                PROJECTKEY_QUERY_PRAM))),
                                        new Document().append(SORT,
                                                new Document().append(CREATED_DATE, NEGATIVE_ONE_DECIMAL_ZERO)),
                                        new Document().append(LIMIT, ONE_DECIMAL_ZERO)))
                        .append(AS, TECHNICAL_DEBT_SUMMARY));
    }

    private Document getGroupStageForTechnicalDebt() {
        return new Document().append(GROUP,
                new Document().append(ID, TECHNICAL_DEBT_SUMMARY_SONAR_CONFIGURATION_ID)
                    .append(COMPLEXITY,
                            getAddStage(Arrays.asList(TECH_DEBT_SUMMARY_QUERY_PARAM_COMPLEXITY,
                                    TECH_DEBT_SUMMARY_QUERY_PARAM_CONGNITIVE_COMPLEXITY)))
                    .append(DUPLICATION, new Document().append(SUM, TECH_DEBT_SUMMARY_DUPLICATION_LINES))
                    .append(ISSUES,
                            getAddStage(Arrays.asList(TECH_DEBT_SUMMARY_ISSUES_OPEN, TECH_DEBT_SUMMARY_ISSUES_CONFIRMED,
                                    TECH_DEBT_SUMMARY_ISSUES_REOPENED)))
                    .append(MAINTAINABILITY, new Document().append(SUM, TECH_DEBT_SUMMARY_QUALITY_MATRICES_CODE_SMELLS))
                    .append(RELIABILITY, new Document().append(SUM, TECH_DEBT_SUMMARY_QUALITY_MATRICES_BUGS))
                    .append(SECURITY,
                            getAddStage(Arrays.asList(TECH_DEBT_SUMMARY_SECURITY_VULNERABILITIES,
                                    TECH_DEBT_SUMMARY_SECURITY_NEW_VULNERABILITIES)))
                    .append(SIZE, new Document().append(SUM, TECH_DEBT_SUMMARY_CODE_MATRIX_LINES))
                    .append(TESTS, new Document().append(SUM, TECH_DEBT_SUMMARY_QUALITY_MATRICES_COVERAGE)));
    }

    public ComplianceAnalysisVO getComplianceAnalysis(String projectId, List<String> repoIds) {
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
                            new Document().append(QueryConstants.PROJECT_ID, ID_UNDERSCORE_DOLLAR)
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
                            new Document().append(LIMIT_DOLLAR, ONE_DOUBLE),
                            new Document().append(PROJECT_DOLLAR,
                                    new Document().append(VIOLATIONS_BLOCKER, ONE_DOUBLE)
                                        .append(VIOLATIONS_CRITICAL, ONE_DOUBLE)
                                        .append(QUAILTY_MATRIX_COVERAGE, ONE_DOUBLE)
                                        .append(RATINGS_SECURITY, ONE_DOUBLE)
                                        .append(RATINGS_MAINTAINABILITY, ONE_DOUBLE)
                                        .append(RATINGS_RELIABILITY, ONE_DOUBLE)
                                        .append(SONAR_PROJECT_ID, PROJECTKEY_QUERY_PRAM))))
                    .append(AS, QUALITY_MEASUREMENTS)),
                new Document().append(UNWIND_DOLLAR, QUALITY_MEASUREMENTS_PARAM),
                new Document().append(PROJECT_DOLLAR,
                        new Document()
                            .append(TEST_CODE_COVERAGE,
                                    new Document().append(MULTIPLY_DOLLAR,
                                            Arrays.asList(QUALITY_MEASUREMENTS_QUALITY_MATRIX_COVERAGE,
                                                    ZERO_DOT_TWO_DOUBLE)))
                            .append(SECURITY,
                                    new Document().append(MULTIPLY_DOLLAR,
                                            Arrays.asList(QUALITY_MEASUREMENTS_RATINGS_SECURITY, TWENTY_DOUBLE,
                                                    ZERO_DOT_TWO_DOUBLE)))
                            .append(EFFICIENCY,
                                    new Document().append(MULTIPLY_DOLLAR,
                                            Arrays.asList(QUALITY_MEASUREMENTS_RATINGS_RELIABILITY, TWENTY_DOUBLE,
                                                    ZERO_DOT_TWO_DOUBLE)))
                            .append(ROBUSTNESS, new Document().append(
                                    MULTIPLY_DOLLAR,
                                    Arrays.asList(QUALITY_MEASUREMENTS_RATINGS_MAINTAINABILITY, TWENTY_DOUBLE,
                                            ZERO_DOT_TWO_DOUBLE)))
                            .append(VULNERABILITIES, new Document()
                                .append(MULTIPLY_DOLLAR, Arrays.asList(new Document().append(SWITCH, new Document()
                                    .append(BRANCHES, Arrays.asList(
                                            new Document().append(CASE, new Document().append(AND_DOLLAR, Arrays
                                                .asList(new Document()
                                                    .append(GTE_DOLLAR, Arrays.asList(new Document()
                                                        .append(SUM_DOLLAR, Arrays.asList(
                                                                QUALITY_MEASUREMENT_VIOLATION_BLOCKER_AND_CRITICAL)),
                                                            ZERO_DOUBLE)),
                                                        new Document().append(LTE_DOLLAR, Arrays
                                                            .asList(new Document().append(SUM_DOLLAR, Arrays.asList(
                                                                    QUALITY_MEASUREMENT_VIOLATION_BLOCKER_AND_CRITICAL)),
                                                                    TEN_DOUBLE)))))
                                                .append(THEN, ONE_HUNDRED_DOUBLE),
                                            new Document().append(CASE, new Document().append(AND_DOLLAR, Arrays
                                                .asList(new Document().append(GTE_DOLLAR,
                                                        Arrays.asList(new Document().append(SUM_DOLLAR, Arrays.asList(
                                                                QUALITY_MEASUREMENT_VIOLATION_BLOCKER_AND_CRITICAL)),
                                                                11.0)),
                                                        new Document().append(LTE_DOLLAR, Arrays
                                                            .asList(new Document().append(SUM_DOLLAR, Arrays.asList(
                                                                    QUALITY_MEASUREMENT_VIOLATION_BLOCKER_AND_CRITICAL)),
                                                                    25.0)))))
                                                .append(THEN, 90.0),
                                            new Document().append(CASE, new Document().append(AND_DOLLAR, Arrays
                                                .asList(new Document().append(GTE_DOLLAR,
                                                        Arrays.asList(new Document().append(SUM_DOLLAR, Arrays.asList(
                                                                QUALITY_MEASUREMENT_VIOLATION_BLOCKER_AND_CRITICAL)),
                                                                26.0)),
                                                        new Document().append(LTE_DOLLAR, Arrays
                                                            .asList(new Document().append(SUM_DOLLAR, Arrays.asList(
                                                                    QUALITY_MEASUREMENT_VIOLATION_BLOCKER_AND_CRITICAL)),
                                                                    FIFTY_DOUBLE)))))
                                                .append(THEN, 80.0),
                                            new Document().append(CASE, new Document().append(AND_DOLLAR, Arrays
                                                .asList(new Document().append(GTE_DOLLAR,
                                                        Arrays.asList(new Document().append(SUM_DOLLAR, Arrays.asList(
                                                                QUALITY_MEASUREMENT_VIOLATION_BLOCKER_AND_CRITICAL)),
                                                                51.0)),
                                                        new Document().append(LTE_DOLLAR, Arrays
                                                            .asList(new Document().append(SUM_DOLLAR, Arrays.asList(
                                                                    QUALITY_MEASUREMENT_VIOLATION_BLOCKER_AND_CRITICAL)),
                                                                    ONE_HUNDRED_DOUBLE)))))
                                                .append(THEN, 70.0),
                                            new Document().append(CASE, new Document().append(AND_DOLLAR, Arrays
                                                .asList(new Document().append(GTE_DOLLAR,
                                                        Arrays.asList(new Document().append(SUM_DOLLAR, Arrays.asList(
                                                                QUALITY_MEASUREMENT_VIOLATION_BLOCKER_AND_CRITICAL)),
                                                                101.0)),
                                                        new Document().append(LTE_DOLLAR, Arrays
                                                            .asList(new Document().append(SUM_DOLLAR, Arrays.asList(
                                                                    QUALITY_MEASUREMENT_VIOLATION_BLOCKER_AND_CRITICAL)),
                                                                    200.0)))))
                                                .append(THEN, 60.0),
                                            new Document().append(CASE, new Document().append(AND_DOLLAR, Arrays
                                                .asList(new Document().append(GTE_DOLLAR,
                                                        Arrays.asList(new Document().append(SUM_DOLLAR, Arrays.asList(
                                                                QUALITY_MEASUREMENT_VIOLATION_BLOCKER_AND_CRITICAL)),
                                                                201.0)),
                                                        new Document().append(LTE_DOLLAR, Arrays
                                                            .asList(new Document().append(SUM_DOLLAR, Arrays.asList(
                                                                    QUALITY_MEASUREMENT_VIOLATION_BLOCKER_AND_CRITICAL)),
                                                                    300.0)))))
                                                .append(THEN, FIFTY_DOUBLE),
                                            new Document().append(CASE, new Document().append(GTE_DOLLAR,
                                                    Arrays.asList(new Document().append(SUM_DOLLAR, Arrays
                                                        .asList(QUALITY_MEASUREMENT_VIOLATION_BLOCKER_AND_CRITICAL)),
                                                            THREE_HUNDRED_ONE_DOUBLE)))
                                                .append(THEN, ZERO_DOUBLE)))
                                    .append(DEFAULT, ZERO_DOUBLE)), ZERO_DOT_TWO_DOUBLE)))),
                new Document()
                    .append(PROJECT_DOLLAR,
                            new Document().append(TEST_CODE_COVERAGE, ONE_DOUBLE)
                                .append(SECURITY, ONE_DOUBLE)
                                .append(EFFICIENCY, ONE_DOUBLE)
                                .append(ROBUSTNESS, ONE_DOUBLE)
                                .append(VULNERABILITIES, ONE_DOUBLE)
                                .append(COMPLIANCE, new Document().append(ADD_DOLLAR,
                                        Arrays.asList(TEST_CODE_COVERAGE_DOLLAR, SECURITY_DOLLAR, EFFICIENCY_DOLLAR,
                                                ROBUSTNESS_DOLLAR, VULNERABILITIES_DOLLAR)))),
                new Document().append(GROUP_DOLLAR,
                        new Document().append(ID_PRE_UNDER, ID_UNDERSCORE_DOLLAR)
                            .append(COUNT, new Document().append(SUM_DOLLAR, ONE_DOUBLE))
                            .append(TEST_CODE_COVERAGE, new Document().append(SUM_DOLLAR, TEST_CODE_COVERAGE_DOLLAR))
                            .append(SECURITY, new Document().append(SUM_DOLLAR, SECURITY_DOLLAR))
                            .append(EFFICIENCY, new Document().append(SUM_DOLLAR, EFFICIENCY_DOLLAR))
                            .append(ROBUSTNESS, new Document().append(SUM_DOLLAR, ROBUSTNESS_DOLLAR))
                            .append(VULNERABILITIES, new Document().append(SUM_DOLLAR, VULNERABILITIES_DOLLAR))
                            .append(COMPLIANCE, new Document().append(SUM_DOLLAR, COMPLIANCE_DOLLAR))),
                new Document().append(PROJECT_DOLLAR, new Document().append(ID_PRE_UNDER, ONE_DOUBLE)
                    .append(TEST_CODE_COVERAGE,
                            new Document().append(DIVIDE_DOLLAR,
                                    Arrays.asList(TEST_CODE_COVERAGE_DOLLAR, COUNT_DOLLAR)))
                    .append(SECURITY,
                            new Document().append(DIVIDE_DOLLAR, Arrays.asList(SECURITY_DOLLAR, COUNT_DOLLAR)))
                    .append(EFFICIENCY,
                            new Document().append(DIVIDE_DOLLAR, Arrays.asList(EFFICIENCY_DOLLAR, COUNT_DOLLAR)))
                    .append(ROBUSTNESS,
                            new Document().append(DIVIDE_DOLLAR, Arrays.asList(ROBUSTNESS_DOLLAR, COUNT_DOLLAR)))
                    .append(VULNERABILITIES,
                            new Document().append(DIVIDE_DOLLAR, Arrays.asList(VULNERABILITIES_DOLLAR, COUNT_DOLLAR)))
                    .append(COMPLIANCE,
                            new Document().append(DIVIDE_DOLLAR, Arrays.asList(COMPLIANCE_DOLLAR, COUNT_DOLLAR)))));

        try {
            Document document = mongoTemplate.getCollection(PROJECTS).aggregate(pipeline).first();
            if (Objects.isNull(document)) {
                throw new RecordNotFoundException("No record available");
            }
            return ComplianceAnalysisVO.builder()
                .testCodeCoverage(document.getDouble(TEST_CODE_COVERAGE))
                .security(document.getDouble(SECURITY))
                .efficiency(document.getDouble(EFFICIENCY))
                .robustness(document.getDouble(ROBUSTNESS))
                .vulnerabilities(document.getDouble(VULNERABILITIES))
                .compliance(document.getDouble(COMPLIANCE))
                .build();
        }
        catch (MongoException ex) {
            throw new ServiceException("Something went wrong while performing DB operation");
        }
    }

    private Document getLookupStageForComplianceAnalysis() {
        return new Document()
            .append(LOOKUP,
                    new Document().append(FROM, SONAR_STATS)
                        .append(LET, new Document().append(PROJECTKEY, PROJECT_KEY))
                        .append(PIPELINE,
                                Arrays.asList(
                                        getMatchStage(EXPRESSION,
                                                new Document().append(EQUAL,
                                                        Arrays.asList(SONAR_PROJECT_ID_QUERY_PRAM,
                                                                PROJECTKEY_QUERY_PRAM))),
                                        new Document().append(SORT,
                                                new Document().append(CREATED_DATE, NEGATIVE_ONE_DECIMAL_ZERO)),
                                        new Document().append(LIMIT, ONE_DECIMAL_ZERO),
                                        new Document().append(PROJECT,
                                                new Document().append(VIOLATIONS_BLOCKER, ONE_DECIMAL_ZERO)
                                                    .append(VIOLATIONS_CRITICAL, ONE_DECIMAL_ZERO)
                                                    .append(QUAILTY_MATRIX_COVERAGE, ONE_DECIMAL_ZERO)
                                                    .append(RATINGS_SECURITY, ONE_DECIMAL_ZERO)
                                                    .append(RATINGS_MAINTAINABILITY, ONE_DECIMAL_ZERO)
                                                    .append(RATINGS_RELIABILITY, ONE_DECIMAL_ZERO)
                                                    .append(SONAR_PROJECT_ID, PROJECTKEY_QUERY_PRAM))))
                        .append(AS, QUALITY_MEASUREMENTS));
    }

    private Document getGroupStageForComplianceAnalysis() {
        return new Document().append(GROUP, new Document().append(ID_PRE_UNDER, ONE_DECIMAL_ZERO)
            .append(TEST_CODE_COVERAGE,
                    getAvgAndMultiplyStage(
                            Arrays.asList(QUALITY_MEASUREMENTS_QUALITY_MATRIX_COVERAGE, ZERO_DECIMAL_TWO)))
            .append(SECURITY,
                    getAvgAndMultiplyStage(Arrays.asList(QUALITY_MEASUREMENTS_RATINGS_SECURITY, TWENTY_DECIMAL_ZERO,
                            ZERO_DECIMAL_TWO)))
            .append(EFFICIENCY,
                    getAvgAndMultiplyStage(Arrays.asList(QUALITY_MEASUREMENTS_RATINGS_RELIABILITY, TWENTY_DECIMAL_ZERO,
                            ZERO_DECIMAL_TWO)))
            .append(ROBUSTNESS,
                    getAvgAndMultiplyStage(Arrays.asList(QUALITY_MEASUREMENTS_RATINGS_MAINTAINABILITY,
                            TWENTY_DECIMAL_ZERO, ZERO_DECIMAL_TWO)))
            .append(VULNERABILITIES,
                    getAvgAndMultiplyStage(Arrays.asList(getSwitchStageForComplianceAnalysis(), ZERO_DECIMAL_TWO))));
    }

    private Document getSwitchStageForComplianceAnalysis() {
        return new Document().append(SWITCH,
                new Document()
                    .append(BRANCHES,
                            Arrays.asList(
                                    getCaseStageForComplienceAnalysis(
                                            Arrays.asList(QUALITY_MEASUREMENT_VIOLATION_BLOCKER_AND_CRITICAL),
                                            ZERO_DECIMAL_ZERO,
                                            Arrays.asList(QUALITY_MEASUREMENT_VIOLATION_BLOCKER_AND_CRITICAL),
                                            TEN_DECIMAL_ZERO)
                                        .append(THEN, HUNDRED_DECIMAL_ZERO),
                                    getCaseStageForComplienceAnalysis(
                                            Arrays.asList(QUALITY_MEASUREMENT_VIOLATION_BLOCKER_AND_CRITICAL),
                                            ELEVEN_DECIMAL_ZERO,
                                            Arrays.asList(QUALITY_MEASUREMENT_VIOLATION_BLOCKER_AND_CRITICAL),
                                            TWENTY_FIVE_DECIMAL_ZERO)
                                        .append(THEN, NINTY_DECIMAL_ZERO),
                                    getCaseStageForComplienceAnalysis(
                                            Arrays.asList(QUALITY_MEASUREMENT_VIOLATION_BLOCKER_AND_CRITICAL),
                                            TWENTY_SIX_DECIMAL_ZERO,
                                            Arrays.asList(QUALITY_MEASUREMENT_VIOLATION_BLOCKER_AND_CRITICAL),
                                            FIFTY_DECIMAL_ZERO)
                                        .append(THEN, EIGHTY_DECIMAL_ZERO),
                                    getCaseStageForComplienceAnalysis(
                                            Arrays.asList(QUALITY_MEASUREMENT_VIOLATION_BLOCKER_AND_CRITICAL),
                                            FIFTY_ONE_DECIMAL_ZERO,
                                            Arrays.asList(QUALITY_MEASUREMENT_VIOLATION_BLOCKER_AND_CRITICAL),
                                            HUNDRED_DECIMAL_ZERO)
                                        .append(THEN, SEVENTY_DECIMAL_ZERO),
                                    getCaseStageForComplienceAnalysis(
                                            Arrays.asList(QUALITY_MEASUREMENT_VIOLATION_BLOCKER_AND_CRITICAL),
                                            ONE_HUNDRED_ONE_DECIMAL_ZERO,
                                            Arrays.asList(QUALITY_MEASUREMENT_VIOLATION_BLOCKER_AND_CRITICAL),
                                            TWO_HUNDRED_DECIMAL_ZERO)
                                        .append(THEN, SIXTY_DECIMAL_ZERO),
                                    getCaseStageForComplienceAnalysis(
                                            Arrays.asList(QUALITY_MEASUREMENT_VIOLATION_BLOCKER_AND_CRITICAL),
                                            TWO_HUNDRED_ONE_DECIMAL_ZERO,
                                            Arrays.asList(QUALITY_MEASUREMENT_VIOLATION_BLOCKER_AND_CRITICAL),
                                            THREE_HUNDRED_DECIMAL_ZERO)
                                        .append(THEN, FIFTY_DECIMAL_ZERO),
                                    new Document()
                                        .append(CASE,
                                                getStageGte(Arrays.asList(
                                                        getSumStage(Arrays.asList(
                                                                QUALITY_MEASUREMENT_VIOLATION_BLOCKER_AND_CRITICAL)),
                                                        THREE_HUNDRED_ONE_DECIMAL_ZERO)))
                                        .append(THEN, ZERO_DECIMAL_ZERO)))
                    .append(DEFAULT, ZERO_DECIMAL_ZERO));
    }

    private List<Document> getDocuments(List<? extends Bson> pipeline) {
        List<Document> documents = mongoTemplate.getCollection(SONAR_CONFIGURATIONS)
            .aggregate(pipeline)
            .into(new ArrayList<>());
        if (CollectionUtils.isEmpty(documents)) {
            throw new RecordNotFoundException("No record found for this project");
        }
        return documents;
    }

    private Document getMatchStage(String key, Object value) {
        return new Document().append(MATCH, new Document().append(key, value));
    }

    private Document getAddStage(List<String> values) {
        return new Document().append(SUM, new Document().append(ADD, values));
    }

    private Document getCaseStageForComplienceAnalysis(List gteSum, double gte, List lteSum, double lte) {
        return getCaseStage(Arrays.asList(getStageGte(Arrays.asList(getSumStage(gteSum), gte)),
                getStageLte(Arrays.asList(getSumStage(lteSum), lte))));
    }

    private Document getAvgAndMultiplyStage(List list) {
        return new Document().append(AVG, new Document().append(MULTIPLY, list));
    }

    private Document getCaseStage(List list) {
        return new Document().append(CASE, new Document().append(AND, list));
    }

    private Document getStageGte(List list) {
        return new Document().append(GTE, list);
    }

    private Document getStageLte(List list) {
        return new Document().append(LTE, list);
    }

    private Document getSumStage(List list) {
        return new Document().append(SUM, list);
    }

}
