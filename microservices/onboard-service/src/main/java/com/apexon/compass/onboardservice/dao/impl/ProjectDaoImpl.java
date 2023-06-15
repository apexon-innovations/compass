package com.apexon.compass.onboardservice.dao.impl;

import com.apexon.compass.entities.Project;
import com.apexon.compass.exception.custom.ServiceException;
import com.apexon.compass.onboardservice.dao.ProjectDao;
import com.apexon.compass.onboardservice.repository.ProjectRepository;
import com.mongodb.MongoException;
import com.mongodb.MongoTimeoutException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import static com.apexon.compass.constants.ClientDashboardServiceConstants.MONGO_EXCEPTION_MESSAGE;

@Component
@RequiredArgsConstructor
@Slf4j
public class ProjectDaoImpl implements ProjectDao {

    private final ProjectRepository projectRepository;

    @Override
    public Project addProject(Project project) {
        try {
            return projectRepository.save(project);
        }
        catch (MongoTimeoutException e) {
            log.error("Mongo db connection timeout", e);
            throw new ServiceException("Mongo db connection timed out after 30sec");
        }
        catch (MongoException e) {
            log.error("Could not create project {}", project.getName(), e);
            throw new ServiceException(MONGO_EXCEPTION_MESSAGE);
        }
    }

}
