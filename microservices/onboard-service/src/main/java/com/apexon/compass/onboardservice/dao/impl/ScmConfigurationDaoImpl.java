package com.apexon.compass.onboardservice.dao.impl;

import com.apexon.compass.entities.ScmConfiguration;
import com.apexon.compass.exception.custom.ServiceException;
import com.apexon.compass.onboardservice.dao.ScmConfigurationDao;
import com.apexon.compass.onboardservice.repository.ScmConfigurationRepository;
import com.mongodb.MongoException;
import com.mongodb.MongoTimeoutException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import static com.apexon.compass.constants.ClientDashboardServiceConstants.MONGO_EXCEPTION_MESSAGE;

@Component
@RequiredArgsConstructor
@Slf4j
public class ScmConfigurationDaoImpl implements ScmConfigurationDao {

    private final ScmConfigurationRepository scmConfigurationRepository;

    @Override
    public ScmConfiguration addScmConfiguration(ScmConfiguration scmConfiguration) {
        log.info("Going to store scm config in db for id {}", scmConfiguration.getProjectId());
        try {
            return scmConfigurationRepository.save(scmConfiguration);
        }
        catch (MongoTimeoutException e) {
            log.error("Mongo db connection timeout", e);
            throw new ServiceException("Mongo db connection timed out after 30sec");
        }
        catch (MongoException e) {
            log.error("Could not create scm config for project id {}", scmConfiguration.getProjectId(), e);
            throw new ServiceException(MONGO_EXCEPTION_MESSAGE);
        }
    }

}
