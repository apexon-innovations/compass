package com.apexon.compass.clientdashboardservice.constants;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class AggregationQuery {

    public static final String QUERY_FOR_FIND_ALL_PROJECTS_OF_USER = """
              {$project:{
                id:1,
                name:1,
                jiraId:{
                  $toInt:"$jiraConfiguration.jiraProjectId"
                }
              }}
            """;

    public static final String QUERY_FOR_FIND_ALL_USER_WITH_ISC_PROJECTS = """
              {$project:{
                _id:1,
                jiraId:{
                  $toInt:"$jiraProjectId"
                },
                project:1
              }}
            """;

}
