import json
import boto3
import pymongo
from pymongo import ReturnDocument
from bson.objectid import ObjectId
import os
import copy

def github_stats(event, context):

    # Read data from S3
    s3 = boto3.client('s3')
    obj = s3.get_object(Bucket=event["Records"][0]["s3"]["bucket"]
                        ["name"], Key=event["Records"][0]["s3"]["object"]["key"])
    eventData = json.loads(obj['Body'].read())

    print(event)

    # Convert data as per UDM
    pullRequestsUDM = get_UDM_structure(eventData)

    # Connect to MongoDB Database
    (pullrequestsDB, pullrequestCommitsDB) = connect_to_database()

    # Insert Data to MongoDB
    upload_to_database(pullrequestsDB, pullrequestCommitsDB, pullRequestsUDM)

    return {
        'statusCode': 200,
        'body': json.dumps('Task Completed')
    }


def get_UDM_structure(eventData):

    print("total data: " + str(len(eventData)))
    
    errorCount = 0
    keyErrorCount = 0
    pullrequest_commits = []
    pullrequests = []


    for _hygieiaPullRequest in eventData:

        try:
            commit = {}
            pullrequest = {}
            

            #parse pullrequest
            pullrequest["scmConfigurationId"] = ObjectId(_hygieiaPullRequest["scmConfigurationId"])
            pullrequest["iscProjectId"] = ObjectId(_hygieiaPullRequest["iscProjectId"])
            
            pullrequest["scmUrl"] = _hygieiaPullRequest["scmUrl"]
            pullrequest["prNumber"] = _hygieiaPullRequest["number"]

            pullrequest["sourceRepoId"] = _hygieiaPullRequest["sourceRepoId"]
            pullrequest["sourceBranch"] = _hygieiaPullRequest["sourceBranch"]
            pullrequest["targetRepoId"] = _hygieiaPullRequest["targetRepoId"]
            pullrequest["targetBranch"] = _hygieiaPullRequest["targetBranch"]
            
            pullrequest["revisionNumber"] = _hygieiaPullRequest["scmRevisionNumber"]
            pullrequest["commitLog"] = _hygieiaPullRequest["scmCommitLog"]
            
            pullrequest["prLastUpdatedTime"] = _hygieiaPullRequest["updatedAt"]
            pullrequest["prCreatedTime"] = _hygieiaPullRequest["createdAt"]
            pullrequest["prDeclinedTime"] = _hygieiaPullRequest["closedAt"]
            pullrequest["prDeclinedBy"] = _hygieiaPullRequest["closedAuthor"]
            pullrequest["prMergeTime"] = _hygieiaPullRequest["mergedAt"]
            pullrequest["prMergedBy"] = _hygieiaPullRequest["mergeAuthor"]
            pullrequest["state"] = _hygieiaPullRequest["state"]
            
            
            #parse scmauthor
            pullrequest["scmAuthor"] = {}
            pullrequest["scmAuthor"]["id"] = _hygieiaPullRequest["userId"]
            pullrequest["scmAuthor"]["name"] = _hygieiaPullRequest["scmAuthor"]
            
            
            #parse comments
            pullrequest["comments"] = []
            if "comments" in _hygieiaPullRequest:
                for _comment in _hygieiaPullRequest["comments"]:
                    prComment = {}
                    prComment["name"] = _comment["user"]
                    prComment["commentedOn"] = _comment["createdAt"]
                    prComment["commentUpdatedOn"] = _comment["updatedAt"]
                    prComment["message"] = _comment["body"]
                    prComment["status"] = _comment["status"]
                    pullrequest["comments"].append(prComment)


            #parse reviewers
            pullrequest["reviewers"] = []
            if "reviewers" in _hygieiaPullRequest:
                for _reviewer in _hygieiaPullRequest["reviewers"]:
                    prReviewer = {}
                    prReviewer["id"] = _reviewer["reviewerId"]
                    prReviewer["name"] = _reviewer["name"]
                    pullrequest["reviewers"].append(prReviewer)
            
            
            #parse approvers
            pullrequest["approvers"] = []
            if "approvers" in _hygieiaPullRequest:
                for _approver in _hygieiaPullRequest["approvers"]:
                    prApprover = {}
                    prApprover["id"] = _approver["approverId"]
                    prApprover["name"] = _approver["approverName"]
                    prApprover["at"] = _approver["approvedAt"]
                    pullrequest["approvers"].append(prApprover)

            if "commits" in _hygieiaPullRequest:
                for _commit in _hygieiaPullRequest["commits"]:
                    prCommit = {}
                    prCommit["prNumber"] = pullrequest["prNumber"]
                    prCommit["iscProjectId"] = ObjectId(_hygieiaPullRequest["iscProjectId"])
                    prCommit["isFirstEverCommit"] = _commit["firstEverCommit"]
                    prCommit["time"] = _commit["scmCommitTimestamp"]
                    prCommit["scmUrl"] = _commit["scmUrl"]
                    prCommit["revisionNumber"] = _commit["scmRevisionNumber"]
                    prCommit["message"] = _commit["scmCommitLog"]
                    prCommit["parentRevisionNumber"] = _commit["scmParentRevisionNumbers"]
                    prCommit["type"] = _commit["type"]

                    prCommit["authorEmail"] = _commit["scmAuthor"]
                    prCommit["authorUserId"] = _commit["scmAuthorLogin"]
                    
                    pullrequest_commits.append(prCommit)

            # Add pullrequest to total pullrequests
            pullrequests.append(pullrequest)

        except Exception as error:
            if type(error) is KeyError:
                keyErrorCount += 1
            errorCount += 1
            continue

    print("total error count: " + str(errorCount))
    print("key error count: " + str(keyErrorCount))
    print("parsed pullrequests: " + str(len(pullrequests)))

    udmStructure = {
        "pullrequests": pullrequests,
        "pullrequest_commits": pullrequest_commits
    }

    return udmStructure


def connect_to_database():
    MONGO_DB_CONNECTION_STRING = os.environ['MONGO_DB_CONNECTION_STRING']
    MONGO_DB_DATABASE_NAME = os.environ['MONGO_DB_DATABASE_NAME']
    MONGO_DB_PULL_REQUESTS_COLLLECTION_NAME = os.environ['MONGO_DB_PULL_REQUESTS_COLLLECTION_NAME']
    MONGO_DB_PULL_REQUEST_COMMITS_COLLLECTION_NAME = os.environ['MONGO_DB_PULL_REQUEST_COMMITS_COLLLECTION_NAME']
    mongoClient = pymongo.MongoClient(MONGO_DB_CONNECTION_STRING, connect=False)
    
    database = mongoClient[MONGO_DB_DATABASE_NAME]
    return (database[MONGO_DB_PULL_REQUESTS_COLLLECTION_NAME], database[MONGO_DB_PULL_REQUEST_COMMITS_COLLLECTION_NAME])


def upload_to_database(pullrequestsDB, pullrequestCommitsDB, pullRequestsUDM):

    pullrequests = pullRequestsUDM["pullrequests"]
    pullrequestCommits = pullRequestsUDM["pullrequest_commits"]

    # insert/update pullrequests
    for _pullrequest in pullrequests:
        try:
            pullrequestResult = pullrequestsDB.find_one_and_replace(
                {
                    "prNumber": _pullrequest["prNumber"],
                    "iscProjectId": _pullrequest["iscProjectId"],
                    "scmUrl": _pullrequest["scmUrl"]
                },
                _pullrequest,
                upsert=True,
                return_document=ReturnDocument.AFTER
            )

            # insert/update pullrequest commits
            commits = list(filter(lambda _commit: _commit["prNumber"] == _pullrequest["prNumber"] and _commit["iscProjectId"] == _pullrequest["iscProjectId"] and _commit["scmUrl"] == _pullrequest["scmUrl"], copy.deepcopy(pullrequestCommits)))
            for _commit in commits:
                del _commit["prNumber"]
                _commit["pullRequestId"] = pullrequestResult["_id"]
                pullrequestCommitsDB.find_one_and_replace({
                    "revisionNumber": _commit["revisionNumber"],
                    "pullRequestId": _commit["pullRequestId"],
                    "scmUrl": _commit["scmUrl"]
                },
                _commit,
                upsert=True
                )
            
        except Exception as error:
            print(error)
            continue
        
