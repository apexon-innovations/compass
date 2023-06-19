package com.apexon.compass.psrservice.repository;

import com.apexon.compass.entities.Boards;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BoardsRepository extends MongoRepository<Boards, String> {

    List<Boards> findByJiraProjectId(Integer jiraProjectId);

}
