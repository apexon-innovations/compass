package com.apexon.compass.onboardservice.dao.impl;

import com.apexon.compass.entities.SonarConfigurations;
import com.apexon.compass.exception.custom.ServiceException;
import com.apexon.compass.onboardservice.dao.SonarConfigurationDao;
import com.apexon.compass.onboardservice.repository.SonarConfigurationRepository;
import com.mongodb.MongoException;
import com.mongodb.MongoTimeoutException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import static com.apexon.compass.constants.ClientDashboardServiceConstants.MONGO_EXCEPTION_MESSAGE;

@Component
@RequiredArgsConstructor
@Slf4j
public class SonarConfigurationDaoImpl implements SonarConfigurationDao {

    private final SonarConfigurationRepository sonarConfigurationRepository;

    @Override
    public SonarConfigurations addSonarConfig(SonarConfigurations sonarConfigurations) {
        log.info("Going to store sonar config in db for id {}", sonarConfigurations.getProjectId());
        try {
            return sonarConfigurationRepository.save(sonarConfigurations);
        }
        catch (MongoTimeoutException e) {
            log.error("Mongo db connection timeout", e);
            throw new ServiceException("Mongo db connection timed out after 30sec");
        }
        catch (MongoException e) {
            log.error("Could not create scm config for project id {}", sonarConfigurations.getProjectId(), e);
            throw new ServiceException(MONGO_EXCEPTION_MESSAGE);
        }
    }

}
