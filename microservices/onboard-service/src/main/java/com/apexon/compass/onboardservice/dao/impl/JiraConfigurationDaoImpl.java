package com.apexon.compass.onboardservice.dao.impl;

import com.apexon.compass.entities.JiraConfiguration;
import com.apexon.compass.exception.custom.ServiceException;
import com.apexon.compass.onboardservice.dao.JiraConfigurationDao;
import com.apexon.compass.onboardservice.repository.JiraConfigurationRepository;
import com.mongodb.MongoException;
import com.mongodb.MongoTimeoutException;
import com.mongodb.MongoWriteException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import static com.apexon.compass.constants.ClientDashboardServiceConstants.MONGO_EXCEPTION_MESSAGE;

@Component
@RequiredArgsConstructor
@Slf4j
public class JiraConfigurationDaoImpl implements JiraConfigurationDao {

    private final JiraConfigurationRepository jiraConfigurationRepository;

    @Override
    public JiraConfiguration saveJiraConfiguration(JiraConfiguration jiraConfiguration) {
        log.info("Going to store jira config in db for id {}", jiraConfiguration.getProjectId());
        try {
            return jiraConfigurationRepository.save(jiraConfiguration);
        }
        catch (MongoTimeoutException e) {
            log.error("Mongo db connection timeout", e);
            throw new ServiceException("Mongo db connection timed out after 30sec");
        }
        catch (MongoException e) {
            log.error("Could not create jira configuration for project id {}", jiraConfiguration.getJiraProjectId(), e);
            throw new ServiceException(MONGO_EXCEPTION_MESSAGE);
        }
    }

}
