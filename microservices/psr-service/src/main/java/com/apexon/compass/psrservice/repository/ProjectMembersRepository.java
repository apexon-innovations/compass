package com.apexon.compass.psrservice.repository;

import com.apexon.compass.entities.ProjectMembers;
import java.util.List;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ProjectMembersRepository extends MongoRepository<ProjectMembers, String> {

    List<ProjectMembers> findLocationByNestId(Integer nestId);

    List<ProjectMembers> findByNestIdIn(List<Integer> nestIds);

}
