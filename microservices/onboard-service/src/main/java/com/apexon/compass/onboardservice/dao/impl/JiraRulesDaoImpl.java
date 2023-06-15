package com.apexon.compass.onboardservice.dao.impl;

import com.apexon.compass.entities.JiraRules;
import com.apexon.compass.exception.custom.ServiceException;
import com.apexon.compass.onboardservice.dao.JiraRulesDao;
import com.apexon.compass.onboardservice.repository.JiraRulesRepository;
import com.mongodb.MongoException;
import com.mongodb.MongoTimeoutException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import static com.apexon.compass.constants.ClientDashboardServiceConstants.MONGO_EXCEPTION_MESSAGE;

@Component
@RequiredArgsConstructor
@Slf4j
public class JiraRulesDaoImpl implements JiraRulesDao {

    private final JiraRulesRepository jiraRulesRepository;

    @Override
    public JiraRules addJiraRules(JiraRules jiraRules) {
        log.info("Going to store jira rules config in db for id {}", jiraRules.getProjectId());
        try {
            return jiraRulesRepository.save(jiraRules);
        }
        catch (MongoTimeoutException e) {
            log.error("Mongo db connection timeout", e);
            throw new ServiceException("Mongo db connection timed out after 30sec");
        }
        catch (MongoException e) {
            log.error("Could not create jira rules for project id {}", jiraRules.getJiraProjectId(), e);
            throw new ServiceException(MONGO_EXCEPTION_MESSAGE);
        }
    }

}
