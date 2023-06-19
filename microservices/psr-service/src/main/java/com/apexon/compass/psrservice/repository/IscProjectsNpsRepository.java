package com.apexon.compass.psrservice.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import com.apexon.compass.entities.IscProjectsNps;

@Repository
public interface IscProjectsNpsRepository extends MongoRepository<IscProjectsNps, String> {

}
