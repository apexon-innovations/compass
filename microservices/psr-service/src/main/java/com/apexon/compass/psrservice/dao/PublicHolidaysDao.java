package com.apexon.compass.psrservice.dao;

import static com.apexon.compass.constants.PsrServiceConstants.DATE;
import static com.apexon.compass.constants.PsrServiceConstants.LOCATION;

import com.apexon.compass.entities.PublicHolidays;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor(onConstructor = @__({ @Autowired }))
public class PublicHolidaysDao {

    private final MongoTemplate mongoTemplate;

    public List<PublicHolidays> getProjectMemberLocationWisePublicHolidays(long startDate, long endDate,
            List<String> location) {
        return mongoTemplate.find(
                Query.query(Criteria.where(LOCATION)
                    .in(location)
                    .andOperator(Criteria.where(DATE).gte(startDate), Criteria.where(DATE).lte(endDate))),
                PublicHolidays.class);
    }

    public List<Long> findDistinctHolidayByDate(long startDate, long endDate, List<String> locations) {
        return mongoTemplate.findDistinct(
                Query.query(Criteria.where(DATE).gte(startDate).lt(endDate).and(LOCATION).in(locations)), DATE,
                PublicHolidays.class, Long.class);
    }

}
