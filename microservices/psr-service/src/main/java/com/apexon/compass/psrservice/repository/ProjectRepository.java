package com.apexon.compass.psrservice.repository;

import com.apexon.compass.entities.Project;
import com.apexon.compass.entities.Jira;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProjectRepository extends MongoRepository<Project, String> {

    Optional<Project> findByIdAndIsDeletedIn(String projectId, List<Boolean> b);

    List<Project> findByProjectManagerEmailAndIsDeletedInOrDeliveryManagerEmailAndIsDeletedIn(String pmEmailAddress,
            List<Boolean> isDeletedIn, String dmEmailAddress, List<Boolean> isDeleted);

    List<Project> findByClientProjectManagerEmailAndIsDeletedIn(String emailAddress, List<Boolean> isDeleted);

    List<Project> findByIsDeletedNullOrIsDeletedFalse();

    List<Project> findByNameAndIsDeletedFalseOrNestIdsInAndIsDeletedFalse(String name, List<String> nestIds);

}
