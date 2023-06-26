package com.apexon.compass.psrservice.constants;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class AggregationQuery {

    public static final StringBuilder QUERY_FOR_PROJECT_DETAILS_WITH_ONETIME = new StringBuilder(
            " { $lookup:{\n from: \"psr_project_one_time_criteria\",\n  pipeline: [{ $match: {\"project_id\": ObjectId('%s'), \"isArchive\": false} }\n],\n as: \"project_one_time_details\"\n}}");

    public static final StringBuilder QUERY_FOR_PROJECT_DETAILS_WITH_WEEKLY = new StringBuilder(
            "{ $lookup:{\n from : \"psr_project_weekly_data\",\n pipeline:[{ $match: {\"project_id\": ObjectId('%s')}},{ $sort: { date: -1 } },{ $limit:1}\n]\n,as: \"project_weekly_details\"\n}\n }");

    public static final String QUERY_FOR_ALL_ACTIVE_PROJECT_WISE_OVERALL_HEALTH = """
            { $lookup: {
             from: "psr_project_weekly_data",let: { projectId: "$_id"},pipeline: [{$match: {$expr: {$eq: ["$project_id", "$$projectId"]}} },{$sort: {date: -1}},{$limit: 1},{$project: {"overall_health": 1,"project_id": 1,"_id": 0}}],as: "overall_health"
             }
            }""";

    public static final StringBuilder QUERY_FOR_GET_NPS_REPORT_BY_PROJECT_ID = new StringBuilder(
            "{ $lookup:{\n from : \"isc_projects_nps\",\n pipeline:[{ $match: {\"projectId\": ObjectId('%s')}},{ $sort: { submissionPeriod: -1 } },{$limit:%d}\n]\n,as: \"nps_score\"\n}\n }");

    public static final StringBuilder QUERY_FOR_FIND_PROJECT_MEMBERS = new StringBuilder(
            "{\n $lookup: { from: \"project_members\", localField: \"_id\",foreignField: \"nestProjectId\",as: \"project_members\" } }");

    public static final StringBuilder QUERY_FOR_PROJECT_MEMBERS_DETAILS_FROM_NEST_PROJECT = new StringBuilder(
            "{ $lookup: {\n from: \"project_members\",\n let: { nestId: \"$nestId\"\n }, pipeline: [{ $match: { $expr: { $eq:[\"$nestId\",\"$$nestId\"] }}}],as: \"project_members\"\n }}");

    public static final StringBuilder QUERY_FOR_CURRENT_SPRINT_FROM_JIRA_CONFIGURATION = new StringBuilder(
            "{$lookup: { from: \"sprints\", let: { jiraProjectId: { $toInt: \"$jiraProjectId\" },projectID: \"$projectID\"}, pipeline: [{ $match: { \"state\": '%s', $expr: { $and: [{ $eq: [\"$jiraProjectId\", \"$$jiraProjectId\"] }, { $eq: [\"$projectId\", \"$$projectId\"] }] }}}, {$project: {\"sprintId\": 1,\"name\": 1,\"boardId\": 1,\"_id\": 0}}], as: \"current_sprint\"}}");

    public static final StringBuilder QUERY_FOR_BOARDS_FROM_JIRA_CONFIGURATION = new StringBuilder(
            "{  $lookup: { from: \"boards\", let: { jiraProjectId: { $toInt: \"$jiraProjectId\" }, projectId: \"$projectId\", jiraBoardId:\"$jiraBoardId\"}, pipeline: [{ $match: { $expr: { $and: [{ $eq: [\"$jiraProjectId\", \"$$jiraProjectId\"] },{ $eq: [\"$projectId\", \"$$projectId\"] },{$in:[{\"$toString\":\"$boardId\"},\"$$jiraBoardId\"]}] }}}, { $project: { \"boardId\": 1, \"name\": 1, \"_id\": 0}}],as: \"boards\"} }");

    public static final String QUERY_FOR_SPRINT_MEMBER_DETAILS_BY_MEMBER_ID = """
            {
                    $lookup: {
                        from: "project_members",
                        let: { nestProjectId: "$_id"},
                        pipeline: [
                            {
                              $match: {
                                  "memberId": '%s',
                                  $expr: {$eq: ["$nestProjectId", "$$nestProjectId"]}
                              }
                            }
                        ],
                        as: "project_members"
                    }
                }""";

    public static final String QUERY_FOR_PROJECT_MEMBER_LEAVE = """
             {
                    $lookup: {
                        from: "project_member_leaves",
                        let: { nestProjectId: "$_id"},
                        pipeline: [
                            {
                              $match: {
                                  "leaves.date": {
                                    $gte: %d,
                                    $lt: %d\s
                                  },
                                  $expr: {$eq: ["$nestProjectId", "$$nestProjectId"]}
                              }
                            }
                        ],
                        as: "project_member_leaves"
                    }
                }\
            """;

}
