import json
import boto3
import pymongo
import datetime
from dateutil.parser import parse
from bson.objectid import ObjectId
import os
from datetime import timezone


def jira_stats(event, context):

    # Connect to MongoDB Database
    MONGO_DB_CONNECTION_STRING = os.environ['MONGO_DB_CONNECTION_STRING']
    MONGO_DB_DATABASE_NAME = os.environ['MONGO_DB_DATABASE_NAME']
    MONGO_DB_PROJECT_COLLLECTION_NAME = os.environ['MONGO_DB_PROJECT_COLLLECTION_NAME']
    MONGO_DB_BOARDS_COLLLECTION_NAME = os.environ['MONGO_DB_BOARDS_COLLLECTION_NAME']
    MONGO_DB_SPRINTS_COLLLECTION_NAME = os.environ['MONGO_DB_SPRINTS_COLLLECTION_NAME']
    MONGO_DB_EPICS_COLLLECTION_NAME = os.environ['MONGO_DB_EPICS_COLLLECTION_NAME']
    MONGO_DB_STORIES_COLLLECTION_NAME = os.environ['MONGO_DB_STORIES_COLLLECTION_NAME']
    MONGO_DB_DERIVED_SPRINTS_COLLLECTION_NAME = os.environ['MONGO_DB_DERIVED_SPRINTS_COLLLECTION_NAME']
    
    mongoClient = pymongo.MongoClient(MONGO_DB_CONNECTION_STRING, connect=False)
    desDB = mongoClient[MONGO_DB_DATABASE_NAME]
    projectCollection = desDB[MONGO_DB_PROJECT_COLLLECTION_NAME]
    boardCollection = desDB[MONGO_DB_BOARDS_COLLLECTION_NAME]
    sprintCollection = desDB[MONGO_DB_SPRINTS_COLLLECTION_NAME]
    epicCollection = desDB[MONGO_DB_EPICS_COLLLECTION_NAME]
    storyCollection = desDB[MONGO_DB_STORIES_COLLLECTION_NAME]
    derivedSprintCollection = desDB[MONGO_DB_DERIVED_SPRINTS_COLLLECTION_NAME]

    # Read data from S3
    s3 = boto3.client('s3')
    obj = s3.get_object(Bucket=event["Records"][0]["s3"]["bucket"]["name"], Key=event["Records"][0]["s3"]["object"]["key"])
    eventData = json.loads(obj['Body'].read())
    print(event)

    # Convert data as per UDM
    udmStructure = get_UDM_structure(eventData, desDB)

    # Upload this data on mongo.
    upload_Stories(storyCollection, udmStructure["stories"])
    upload_projects_if_required(projectCollection, udmStructure["projects"])
    upload_boards_if_required(boardCollection, udmStructure["boards"])
    upload_sprints_if_required(sprintCollection, udmStructure["sprints"])
    upload_epics_if_required(epicCollection, udmStructure["epics"])
    for _board in udmStructure["boards"]:
            for _project in udmStructure["projects"]:
                derivedSprints = derive_jira_sprint_data(_board["boardId"], _project["projectId"], _project["iscProjectId"], desDB)
                upload_derived_sprints_if_required(derivedSprintCollection, derivedSprints)

    project_array = []
    for project in udmStructure["projects"]:
        project["iscProjectId"] = str(project["iscProjectId"])
        project_array.append(project)
    DEFAULT_ENV = os.environ["DEFAULT_ENV"]
    functionName = "jira-" + DEFAULT_ENV + "-aggregationLayer"
    print("calling function ", functionName)
    lambda_client = boto3.client('lambda')
    invoke_response = lambda_client.invoke(FunctionName=functionName, InvocationType='RequestResponse', Payload=json.dumps(project_array))
    print(json.loads(invoke_response['Payload'].read()))
    print("Processing Completed")

    return {
        'statusCode': 200,
        'body': json.dumps('Task Completed')
    }


def get_UDM_structure(eventData, desDB):

    projects = []
    boards = []
    sprints = []
    epics = []
    stories = []

    currentDate = round(datetime.datetime.now().timestamp() * 1000)

    print("total Stories: " + str(len(eventData)))
    if len(eventData) <= 0:
        udmStructure = {
            "projects": projects,
            "boards": boards,
            "sprints": sprints,
            "epics": epics,
            "stories": stories
        }
        return udmStructure

    errorCount = 0
    count = 0

    #collect jira rules
    jiraRulesProjectId = int(eventData[0]["sProjectID"])
    jiraRulesIscProjectId = ObjectId(eventData[0]["iscProjectId"])
    jiraRulesCollection = desDB["jira_rules"]
    jiraRules = jiraRulesCollection.find_one({"projectId": jiraRulesProjectId, "iscProjectId": jiraRulesIscProjectId})
    defaultSeverity = ""
    if "defaultSeverity" in jiraRules:
        defaultSeverity = jiraRules["defaultSeverity"]

    for _hygieiaStory in eventData:

        try:
            # Get story Data
            # remaining fields: sprintID, sOwnersShortName, subtask.statusCategoryKey
            _story = {}
            _story["projectId"] = int(_hygieiaStory["sProjectID"])
            _story["boardId"] = int(_hygieiaStory["sTeamID"])
            _story["iscProjectId"] = ObjectId(_hygieiaStory["iscProjectId"])
            _story["sprintId"] = None
            _story["epicId"] = None
            _story["storyId"] = int(_hygieiaStory["sId"])
            _story["number"] = _hygieiaStory["sNumber"]
            _story["name"] = _hygieiaStory["sName"]
            _story["status"] = _hygieiaStory["sStatus"]
            _story["state"] = _hygieiaStory["sState"]
            _story["estimate"] = 0
            if _hygieiaStory["sEstimate"].isnumeric():
                _story["estimate"] = int(_hygieiaStory["sEstimate"])
            _story["estimatedTime"] = _hygieiaStory["sEstimateTime"]
            _story["url"] = _hygieiaStory["sUrl"]
            _story["jiraUpdatedDate"] = get_timestamp(_hygieiaStory["changeDate"])
            _story["jiraCreatedDate"] = get_timestamp(_hygieiaStory["createdDate"])
            _story["isDeleted"] = bool(_hygieiaStory["isDeleted"])
            _story["ownersDeleted"] = _hygieiaStory["sOwnersIsDeleted"]
            _story["ownersChangeDate"] = _hygieiaStory["sOwnersChangeDate"]
            _story["ownersState"] = _hygieiaStory["sOwnersState"]
            _story["sprintJourney"] = _hygieiaStory["sprintJourney"]
            if _hygieiaStory["sPriorityId"] != '' and _hygieiaStory["sPriorityId"].isnumeric():
                _story["priorityId"] = int(_hygieiaStory["sPriorityId"])
                _story["priorityName"] = _hygieiaStory["sPriorityName"]
            else:
                _story["priorityId"] = 0
                _story["priorityName"] = defaultSeverity

            changes = []
            for change in _hygieiaStory["changes"]:
                change["changeCreatedDateTime"] = get_timestamp(change["changeCreatedDateTime"])
                changes.append(change)

            _story["changes"] = changes

            _story["type"] = None
            if "sTypeName" in _hygieiaStory:
                _story["type"] = _hygieiaStory["sTypeName"]

            _story["typeId"] = None
            if "sTypeId" in _hygieiaStory:
                _story["typeId"] = int(_hygieiaStory["sTypeId"])

            _story["ownerIds"] = None
            if "sOwnersID" in _hygieiaStory:
                _story["ownerIds"] = _hygieiaStory["sOwnersID"]

            _story["ownerUserNames"] = None
            if "sOwnersUsername" in _hygieiaStory:
                _story["ownerUserNames"] = _hygieiaStory["sOwnersUsername"]

            _story["ownerFullNames"] = None
            if "sOwnersFullName" in _hygieiaStory:
                _story["ownerFullNames"] = _hygieiaStory["sOwnersFullName"]

            # Proccessing of subTasks data
            _story["subTasks"] = None
            if len(_hygieiaStory["subTasks"]) > 0:
                _subTasks = []
                for _hygieiaSubTask in _hygieiaStory["subTasks"]:
                    # remaining fields: subtask.statusCategoryKey
                    _subTask = {}
                    _subTask["id"] = int(_hygieiaSubTask["issueId"])
                    _subTask["number"] = _hygieiaSubTask["issueKey"]
                    _subTask["url"] = _hygieiaSubTask["issueUrl"]
                    _subTask["summary"] = _hygieiaSubTask["issueSummary"]
                    _subTask["statusId"] = int(_hygieiaSubTask["statusId"])
                    _subTask["status"] = _hygieiaSubTask["statusName"]
                    _subTask["categoryId"] = int(_hygieiaSubTask["statusCategoryId"])
                    _subTask["categoryKey"] = _hygieiaSubTask["statusCategoryKey"]
                    _subTask["categoryName"] = _hygieiaSubTask["statusCategoryName"]
                    if _hygieiaSubTask["priorityId"] != '' and _hygieiaSubTask["priorityId"].isnumeric():
                        _subTask["priorityId"] = int(_hygieiaSubTask["priorityId"])
                        _subTask["priorityName"] = _hygieiaSubTask["priorityName"]
                    else:
                        _subTask["priorityId"] = 0
                        _subTask["priorityName"] = defaultSeverity
                    _subTask["issueTypeName"] = _hygieiaSubTask["issueTypeName"]
                    _subTask["issueTypeId"] = _hygieiaSubTask["issueTypeId"]
                    _subTasks.append(_subTask)
                _story["subTasks"] = _subTasks

            # Proccessing of issueLinks data
            _story["issueLinks"] = None
            if len(_hygieiaStory["issueLinks"]) > 0:
                _issueLinks = []
                for _hygieiaIssueLink in _hygieiaStory["issueLinks"]:
                    # remaining fields: subtask.statusCategoryKey
                    _issueLink = {}
                    _issueLink["number"] = _hygieiaIssueLink["targetIssueKey"]
                    _issueLink["url"] = _hygieiaIssueLink["targetIssueUri"]
                    _issueLink["name"] = _hygieiaIssueLink["issueLinkName"]
                    _issueLink["type"] = _hygieiaIssueLink["issueLinkType"]
                    _issueLink["linkDirection"] = _hygieiaIssueLink["issueLinkDirection"]
                    _issueLinks.append(_issueLink)
                _story["issueLinks"] = _issueLinks

            # Get epic Data
            if "sEpicID" in _hygieiaStory and not not _hygieiaStory["sEpicID"]:

                _story["epicId"] = int(_hygieiaStory["sEpicID"])

                _epic = {}
                _epic["projectId"] = int(_hygieiaStory["sProjectID"])
                _epic["boardId"] = int(_hygieiaStory["sTeamID"])
                _epic["iscProjectId"] = ObjectId(_hygieiaStory["iscProjectId"])
                _epic["epicId"] = int(_hygieiaStory["sEpicID"])
                _epic["number"] = _hygieiaStory["sEpicNumber"]
                _epic["url"] = _hygieiaStory["sEpicUrl"]
                _epic["name"] = _hygieiaStory["sEpicName"]
                _epic["startDate"] = get_timestamp(
                    _hygieiaStory["sEpicBeginDate"])
                _epic["endDate"] = get_timestamp(_hygieiaStory["sEpicEndDate"])
                _epic["type"] = _hygieiaStory["sEpicType"]
                _epic["state"] = _hygieiaStory["sEpicAssetState"]
                _epic["jiraChangeDate"] = get_timestamp(
                    _hygieiaStory["sEpicChangeDate"])
                _epic["isDeleted"] = bool(_hygieiaStory["sEpicIsDeleted"])
                _epic["createdDate"] = currentDate

                didFoundEpicInList = False
                for i, _epicInList in enumerate(epics):
                    if _epicInList["epicId"] == _epic["epicId"]:
                        didFoundEpicInList = True
                        epics[i] = _epic
                        break

                if didFoundEpicInList == False:
                    epics.append(_epic)

            # Get Sprint Data
            if "sSprintID" in _hygieiaStory and not not _hygieiaStory["sSprintID"]:

                _story["sprintId"] = int(_hygieiaStory["sSprintID"])

                # missing field: sSprintUrl
                _sprint = {}
                _sprint["projectId"] = int(_hygieiaStory["sProjectID"])
                _sprint["boardId"] = int(_hygieiaStory["sTeamID"])
                _sprint["sprintId"] = int(_hygieiaStory["sSprintID"])
                _sprint["iscProjectId"] = ObjectId(_hygieiaStory["iscProjectId"])
                _sprint["name"] = _hygieiaStory["sSprintName"]
                _sprint["startDate"] = get_timestamp(
                    _hygieiaStory["sSprintBeginDate"])
                _sprint["endDate"] = get_timestamp(
                    _hygieiaStory["sSprintEndDate"])
                _sprint["state"] = _hygieiaStory["sSprintAssetState"]
                _sprint["jiraChangeDate"] = get_timestamp(
                    _hygieiaStory["sSprintChangeDate"])
                _sprint["isDeleted"] = bool(_hygieiaStory["sSprintIsDeleted"])
                _sprint["createdDate"] = currentDate

                didFoundSprintInList = False
                for i, _sprintInList in enumerate(sprints):
                    if _sprintInList["sprintId"] == _sprint["sprintId"]:
                        didFoundSprintInList = True
                        sprints[i] = _sprint
                        break

                if didFoundSprintInList == False:
                    sprints.append(_sprint)

            # Get board Data
            _board = {}
            _board["projectId"] = int(_hygieiaStory["sProjectID"])
            _board["boardId"] = int(_hygieiaStory["sTeamID"])
            _board["iscProjectId"] = ObjectId(_hygieiaStory["iscProjectId"])
            _board["name"] = _hygieiaStory["sTeamName"]
            _board["jiraUpdatedDate"] = get_timestamp(
                _hygieiaStory["sSprintChangeDate"])
            _board["state"] = _hygieiaStory["sTeamAssetState"]
            _board["isDeleted"] = _hygieiaStory["sTeamIsDeleted"]
            _board["createdDate"] = currentDate

            didFoundBoardInList = False
            for i, _boardInList in enumerate(boards):
                if _boardInList["boardId"] == _board["boardId"]:
                    didFoundBoardInList = True
                    boards[i] = _board
                    break

            if didFoundBoardInList == False:
                boards.append(_board)

            # Get project Data
            _project = {}
            _project["projectId"] = int(_hygieiaStory["sProjectID"])
            _project["iscProjectId"] = ObjectId(_hygieiaStory["iscProjectId"])
            _project["name"] = _hygieiaStory["sProjectName"]
            _project["startDate"] = get_timestamp(
                _hygieiaStory["sProjectBeginDate"])
            _project["endDate"] = get_timestamp(
                _hygieiaStory["sProjectEndDate"])
            _project["jiraUpdatedDate"] = get_timestamp(
                _hygieiaStory["sProjectChangeDate"])
            _project["state"] = _hygieiaStory["sProjectState"]
            _project["isDeleted"] = _hygieiaStory["sProjectIsDeleted"]
            _project["path"] = _hygieiaStory["sProjectPath"]
            _project["createdDate"] = currentDate

            didFoundProjectInList = False
            for i, _projectInList in enumerate(projects):
                if _projectInList["projectId"] == _project["projectId"]:
                    didFoundProjectInList = True
                    projects[i] = _project
                    break

            if didFoundProjectInList == False:
                projects.append(_project)

            _story["createdDate"] = currentDate
            stories.append(_story)

        except Exception as error:
            if type(error) is KeyError:
                errorCount += 1
            count += 1
            # print(error)
            continue

    print("total error count: " + str(count))
    print("key error count: " + str(errorCount))
    print("parsed Stories: " + str(len(stories)))

    udmStructure = {
        "projects": projects,
        "boards": boards,
        "sprints": sprints,
        "epics": epics,
        "stories": stories
    }

    return udmStructure

def upload_Stories(storyCollection, jiraStories):
    for _story in jiraStories:
        try:
            result = storyCollection.find_one_and_replace(
                {
                    "storyId": _story["storyId"],
                    "projectId": _story["projectId"],
                    "boardId": _story["boardId"],
                    "iscProjectId": _story["iscProjectId"]
                },
                _story,
                upsert=True
            )
            # storyCollection.insert_many(jiraStories)
        except Exception as error:
            print(error)
            continue

def upload_projects_if_required(projectCollection, jiraProjects):
    for _project in jiraProjects:
        try:
            result = projectCollection.find_one_and_replace(
                {
                    "projectId": _project["projectId"],
                    "iscProjectId": _project["iscProjectId"]
                },
                _project,
                upsert=True
            )
        except Exception as error:
            print(error)
            continue

def upload_boards_if_required(boardCollection, jiraBoards):
    for _board in jiraBoards:
        try:
            boardCollection.find_one_and_replace(
                {
                    "boardId": _board["boardId"],
                    "projectId": _board["projectId"],
                    "iscProjectId": _board["iscProjectId"]
                },
                _board,
                upsert=True
            )
        except Exception as error:
            print(error)
            continue

def upload_sprints_if_required(sprintCollection, jiraSprints):
    for _sprint in jiraSprints:
        try:
            result = sprintCollection.find_one_and_replace(
                {
                    "sprintId": _sprint["sprintId"],
                    "projectId": _sprint["projectId"],
                    "boardId": _sprint["boardId"],
                    "iscProjectId": _sprint["iscProjectId"]
                },
                _sprint,
                upsert=True
            )
        except Exception as error:
            print(error)
            continue

def upload_derived_sprints_if_required(derivedSprintCollection, derivedSprints):
    for _sprint in derivedSprints:
        try:
            result = derivedSprintCollection.find_one_and_replace(
                {
                    "sprintId": _sprint["sprintId"],
                    "projectId": _sprint["projectId"],
                    "boardId": _sprint["boardId"],
                    "iscProjectId": _sprint["iscProjectId"]
                },
                _sprint,
                upsert=True
            )
        except Exception as error:
            print(error)
            continue

def upload_epics_if_required(epicCollection, jiraEpics):
    for _epic in jiraEpics:
        try:
            epicCollection.find_one_and_replace(
                {
                    "epicId": _epic["epicId"],
                    "projectId": _epic["projectId"],
                    "iscProjectId": _epic["iscProjectId"]
                },
                _epic,
                upsert=True
            )
        except Exception as error:
            print(error)
            continue

def derive_jira_sprint_data(boardId, projectId, iscProjectId, desDB):
    
    # Default Values
    #on what issue types we are we are calculating sprint charts eg: Story, Task, Bug
    calculationCriteria = []
    
    #basis of calculation for sprint burn down
    isSprintChange = "Sprint"
    isStatusChange = "status"
    isEstimationChange = "StoryPoints"
    isAssigneeChange = "assignee"

    fieldBaseKey = "field"
    calculationBaseCriteria = "estimate"
    possibleToDoStatuses = []
    possibleBlockerStatuses = []
    possibleInProgressStatuses = []
    possibleDoneStatuses = []
    possibleAcceptedTaskStatuses = []

    #necessary initialization
    sprintCollection = desDB['sprints']
    jira_sprints = list(sprintCollection.find({"projectId": projectId, "boardId": boardId, "iscProjectId": iscProjectId}))
    derived_sprints = []

    #collect jira rules
    jiraRulesCollection = desDB["jira_rules"]
    jiraRules = jiraRulesCollection.find_one({"projectId": projectId, "iscProjectId": iscProjectId})

    if jiraRules == None:
        return

    possibleToDoStatuses = jiraRules["definitionOfTodo"]
    possibleBlockerStatuses = jiraRules["blockerDefintion"]
    possibleInProgressStatuses = jiraRules["definitionOfInProgress"] + jiraRules["definitionOfDevComplete"] + jiraRules["definitionOfQaComplete"]
    possibleDoneStatuses = jiraRules["definitionOfDone"] + jiraRules["definitionOfAccepted"]
    possibleAcceptedTaskStatuses = jiraRules["definitionOfAccepted"]
    calculationCriteria = jiraRules["storyPointsCalculation"]
    isSprintChange = jiraRules["burnDownCalculationCriterias"]["sprintFieldValue"]
    isStatusChange = jiraRules["burnDownCalculationCriterias"]["statusFieldValue"]
    isEstimationChange = jiraRules["burnDownCalculationCriterias"]["estimationFieldValue"]
    isAssigneeChange = jiraRules["burnDownCalculationCriterias"]["assigneeFieldValue"]


    #calculate each sprints
    for _sprint in jira_sprints:

        if None == _sprint["startDate"] or None == _sprint["endDate"]:
            continue

        sprintStartDate = datetime.datetime.utcfromtimestamp(_sprint["startDate"]/1000)
        sprintEndDate = datetime.datetime.utcfromtimestamp(_sprint["endDate"]/1000)

        startDelta = sprintStartDate.replace(hour=0, minute=0, second=0, microsecond=0)
        dayDelta = datetime.timedelta(days=1) #we give sprint status every day, not hour, minute, or second

        derivedSprintCollection = {
            "projectId": _sprint["projectId"],
            "boardId": _sprint["boardId"],
            "iscProjectId": _sprint["iscProjectId"],
            "sprintId": _sprint["sprintId"],
            "name": _sprint["name"],
            "startDate": _sprint["startDate"],
            "endDate": _sprint["endDate"],
            "state": _sprint["state"],
            "totalEfforts": 0,
            "totalTasks": 0,
            "statusData": []
        }      

        dailySprintStatusData = []

        while startDelta <= sprintEndDate:
            dailySprintStatusData.append({
                "date": startDelta,
                "remainingEfforts": 0,
                "completedEfforts": 0,
                "newlyAddedEfforts": 0,
                "reopenEfforts": 0,
                "toDoTasks": [],
                "inProgressTasks": [],
                "completedTasks": [],
                "blockerTasks": [],
                "newlyAddedTasks": [],
                "reopenTasks": [],
                "team": []
            })
            startDelta += dayDelta

        ########################################################################
        ######################## Collect Stories ###############################
        ########################################################################
        storiesCollection = desDB['stories']
        sprint_stories = list(storiesCollection.find({
            "projectId": _sprint["projectId"],
            "sprintId": _sprint["sprintId"],
            "boardId": _sprint["boardId"],
            "iscProjectId": _sprint["iscProjectId"],
            "type": {"$in": calculationCriteria}
        }))

        removed_stories = list(storiesCollection.find({
            "projectId": _sprint["projectId"],
            "boardId": _sprint["boardId"],
            "iscProjectId": _sprint["iscProjectId"],
            'changes.updates' : { "$elemMatch": {
                'fieldId': 'SprintData', 
                'from': str(_sprint["sprintId"])
            }},
            "type": {"$in": calculationCriteria}
        }))

        regex = "(?=.*" + str(_sprint["sprintId"]) + ".*)(.*,.*)"
        removed_stories_with_more_then_one_sprints = list(storiesCollection.find({
            "projectId": _sprint["projectId"],
            "boardId": _sprint["boardId"],
            "iscProjectId": _sprint["iscProjectId"],
            'changes.updates' : { "$elemMatch": {
                'fieldId': 'SprintData', 
                'from': {"$regex": regex}
            }},
            "type": {"$in": calculationCriteria}
        }))

        removed_sprint_stories = removed_stories + removed_stories_with_more_then_one_sprints
        
        ########################################################################
        ######################## Parse removed issues from sprints #############
        ########################################################################

        for _story in removed_sprint_stories:
            issueIncludedAtSprintStart = False
            changeStartTime = []
            changeEndTime = []
            changeLogs = []
            issue_assignee = {}

            if (_story["ownerFullNames"] is not None) and (len(_story["ownerFullNames"]) > 0):
                issue_assignee[_story["number"]] = _story["ownerFullNames"][0]

            for _change in _story["changes"]:
                for _update in _change["updates"]:
                    if _update[fieldBaseKey] == isSprintChange:    
                        if _update["from"] == str(_sprint["sprintId"]) and _update["to"] == str(_sprint["sprintId"]):
                            continue
                        
                        if str(_sprint["sprintId"]) in _update["from"].split(", ") and str(_sprint["sprintId"]) in _update["to"].split(", "):
                            changeDateTime = datetime.datetime.utcfromtimestamp(_change["changeCreatedDateTime"]/1000)
                            if changeDateTime > sprintEndDate and sprintEndDate not in changeEndTime:
                                changeEndTime.append(sprintEndDate)
                            # if changeDateTime < sprintStartDate and sprintStartDate not in changeStartTime:
                            #     changeStartTime.append(sprintStartDate)
                            continue

                        if str(_sprint["sprintId"]) in _update["from"].split(", "):
                            removedDate = datetime.datetime.utcfromtimestamp(_change["changeCreatedDateTime"]/1000)
                            if removedDate > sprintEndDate:
                                removedDate = sprintEndDate
                            changeEndTime.append(removedDate)
                        if str(_sprint["sprintId"]) in _update["to"].split(", "):
                            addedDate = datetime.datetime.utcfromtimestamp(_change["changeCreatedDateTime"]/1000)
                            if addedDate <= sprintStartDate:
                                issueIncludedAtSprintStart = True
                                addedDate = sprintStartDate 
                            changeStartTime.append(addedDate)
                            if addedDate > sprintStartDate and addedDate < sprintEndDate:
                                issueIncludedAtSprintStart = False
                                issueChangeStartDate = round(addedDate.replace(tzinfo = timezone.utc).timestamp() * 1000)
                                #newly added tasks / efforts
                                issueChangeStartDateDelta = addedDate.replace(hour=0,minute=0,second=0,microsecond=0)
                                _index = next((index for (index, d) in enumerate(dailySprintStatusData) if d["date"] == issueChangeStartDateDelta), None)
                                if _index:
                                    dailySprintStatusData[_index]["newlyAddedEfforts"] += _story[calculationBaseCriteria]
                                    dailySprintStatusData[_index]["newlyAddedTasks"].append(_story["number"])
                                break


            if len(changeStartTime) == len(changeEndTime):
                for i in range(0, len(changeStartTime)):
                    changeLogs.extend([_change for _change in _story["changes"] if (changeStartTime[i].timestamp()*1000) <= _change["changeCreatedDateTime"] <= (changeEndTime[i].timestamp()*1000)])
            else:
                continue
                
            #evaluate daily changes
            if issueIncludedAtSprintStart:
                # if (_story[calculationBaseCriteria] > 0):
                #     print(_story["number"] + "-------" + str(_story[calculationBaseCriteria]))
                derivedSprintCollection["totalEfforts"] += _story[calculationBaseCriteria]
                derivedSprintCollection["totalTasks"] += 1

                # status = ""
                # if _story["status"] in possibleToDoStatuses:
                #     status = "toDoTasks"
                # if _story["status"] in possibleBlockerStatuses:
                #     status = "blockerTasks"
                # if _story["status"] in possibleInProgressStatuses:
                #     status = "inProgressTasks"
                # if _story["status"] in possibleDoneStatuses:
                #     status = "completedTasks"

                # for i, dailyStatus in enumerate(dailySprintStatusData):
                #     dailySprintStatusData[i][status].append(_story["number"])
                #     if status != "completedTasks":
                #         dailySprintStatusData[i]["remainingEfforts"] += _story[calculationBaseCriteria]

            #traverse all change logs that can affect the sprint outcome oldest to newest
            for _change in reversed(changeLogs):
                currentChangeDateTime = datetime.datetime.utcfromtimestamp(_change["changeCreatedDateTime"]/1000)
                currentChangeDateTime = currentChangeDateTime.replace(hour=0, minute=0, second=0, microsecond=0)
                for _update in _change["updates"]:
                    
                    #check if sprint lifecycle is changed
                    if _update[fieldBaseKey] == isSprintChange:
                        #check if issue is added to the sprint
                        if (str(_sprint["sprintId"]) not in _update["from"].split(", ")) and \
                            (str(_sprint["sprintId"]) in _update["to"].split(", ")):
                            
                            #find status of issue
                            status = ""
                            if _story["status"] in possibleToDoStatuses:
                                status = "toDoTasks"
                            if _story["status"] in possibleBlockerStatuses:
                                status = "blockerTasks"
                            if _story["status"] in possibleInProgressStatuses:
                                status = "inProgressTasks"
                            if _story["status"] in possibleDoneStatuses:
                                status = "completedTasks"

                            #if issues is added then add the remaining efforts into the respective parts 
                            # of daily status
                            for i, dailyStatus in enumerate(dailySprintStatusData):
                                if dailyStatus["date"] >= currentChangeDateTime:
                                    if (_story["number"] not in dailyStatus["toDoTasks"]) and \
                                        (_story["number"] not in dailyStatus["blockerTasks"]) and \
                                        (_story["number"] not in dailyStatus["inProgressTasks"]) and \
                                        (_story["number"] not in dailyStatus["completedTasks"]):

                                        dailyStatus[status].append(_story["number"])
                                        if status == "completedTasks":
                                            dailyStatus["completedEfforts"] += _story[calculationBaseCriteria]
                                        else:
                                            dailyStatus["remainingEfforts"] += _story[calculationBaseCriteria]
                                        
                                        dailySprintStatusData[i] = dailyStatus                    


                        #check if issue is removed from the sprint
                        if (str(_sprint["sprintId"]) in _update["from"].split(", ")) and \
                            (str(_sprint["sprintId"]) not in _update["to"].split(", ")):
                            
                            for i, _dayStatus in enumerate(dailySprintStatusData):
                                if _dayStatus["date"] >= currentChangeDateTime:
                                    # remove from before arrays like todo and inprogress
                                    if _story["number"] in _dayStatus["toDoTasks"]:
                                        _dayStatus["toDoTasks"].remove(_story["number"])
                                        _dayStatus["remainingEfforts"] -= _story[calculationBaseCriteria]
                                    
                                    elif _story["number"] in _dayStatus["blockerTasks"]: 
                                        _dayStatus["blockerTasks"].remove(_story["number"])
                                        _dayStatus["remainingEfforts"] -= _story[calculationBaseCriteria]

                                    elif _story["number"] in _dayStatus["inProgressTasks"]: 
                                        _dayStatus["inProgressTasks"].remove(_story["number"])
                                        _dayStatus["remainingEfforts"] -= _story[calculationBaseCriteria]
                                    
                                    elif _story["number"] in _dayStatus["completedTasks"]: 
                                        _dayStatus["completedTasks"].remove(_story["number"])
                                        _dayStatus["completedEfforts"] -= _story[calculationBaseCriteria]
                                    
                                    dailySprintStatusData[i] = _dayStatus                  

                    #check if status is changed
                    if _update[fieldBaseKey] == isStatusChange:
                        #check what is the status changing from to to.
                        #if it is moving done then we should decrease estimate
                        beforeStatus = ""
                        afterStatus = ""
                        teamCompletionStats = []

                        if _update["fromString"] in possibleToDoStatuses:
                            beforeStatus = "toDoTasks"
                        elif _update["fromString"] in possibleBlockerStatuses:
                            beforeStatus = "blockerTasks"
                        elif _update["fromString"] in possibleInProgressStatuses:
                            beforeStatus = "inProgressTasks"
                        elif _update["fromString"] in possibleDoneStatuses:
                            beforeStatus = "completedTasks"

                        if _update["toString"] in possibleToDoStatuses:
                            afterStatus = "toDoTasks"
                        elif _update["toString"] in possibleBlockerStatuses:
                            afterStatus = "blockerTasks"
                        elif _update["toString"] in possibleInProgressStatuses:
                            afterStatus = "inProgressTasks"
                        elif _update["toString"] in possibleDoneStatuses:
                            afterStatus = "completedTasks"

                        
                        #if the status is moving into or from same stage then do nothing
                        if afterStatus == beforeStatus or beforeStatus == "" or afterStatus == "":
                            continue
                        

                        #calculate reopen tasks / efforts
                        if beforeStatus == "completedTasks":
                            _index = next((index for (index, d) in enumerate(dailySprintStatusData) if d["date"] == currentChangeDateTime), None)
                            if _index:
                                dailySprintStatusData[_index]["reopenEfforts"] += _story[calculationBaseCriteria]
                                dailySprintStatusData[_index]["reopenTasks"].append(_story["number"])

                        #if status is moving into done status then add the assignee person
                        if afterStatus == "completedTasks":
                            if _story["number"] in issue_assignee:
                                teamCompletionStats.append({
                                    "number": _story["number"],
                                    "personName": issue_assignee[_story["number"]],
                                    "storyPointCompleted": _story[calculationBaseCriteria],
                                })
                            elif (_story["ownerFullNames"] is not None) and (len(_story["ownerFullNames"]) > 0):
                                teamCompletionStats.append({
                                    "number": _story["number"],
                                    "personName": _story["ownerFullNames"][0],
                                    "storyPointCompleted": _story[calculationBaseCriteria],
                                })

                        #insert daily status of issues
                        for i, dailyStatus in enumerate(dailySprintStatusData):
                            if dailyStatus["date"] < currentChangeDateTime:
                                #do nothinng if beforeStatus is not known
                                # if beforeStatus == "":
                                #     continue
                                if (_story["number"] not in dailyStatus["toDoTasks"]) and \
                                    (_story["number"] not in dailyStatus["blockerTasks"]) and \
                                    (_story["number"] not in dailyStatus["inProgressTasks"]) and \
                                    (_story["number"] not in dailyStatus["completedTasks"]):
                                    
                                    dailyStatus[beforeStatus].append(_story["number"])
                                    if beforeStatus == "completedTasks":
                                        dailyStatus["completedEfforts"] += _story[calculationBaseCriteria]
                                    else:
                                        dailyStatus["remainingEfforts"] += _story[calculationBaseCriteria]
                                    
                                    dailySprintStatusData[i] = dailyStatus
                            
                            elif dailyStatus["date"] >= currentChangeDateTime:
                                if _story["number"] in dailyStatus["toDoTasks"]:
                                    dailyStatus["toDoTasks"].remove(_story["number"])
                                    dailyStatus["remainingEfforts"] -= _story[calculationBaseCriteria]
                                elif _story["number"] in dailyStatus["inProgressTasks"]:
                                    dailyStatus["inProgressTasks"].remove(_story["number"])
                                    dailyStatus["remainingEfforts"] -= _story[calculationBaseCriteria]
                                elif _story["number"] in dailyStatus["completedTasks"]:
                                    dailyStatus["completedTasks"].remove(_story["number"])
                                    dailyStatus["completedEfforts"] -= _story[calculationBaseCriteria]

                                dailyStatus[afterStatus].append(_story["number"])
                                if afterStatus == "completedTasks":
                                    dailyStatus["completedEfforts"] += _story[calculationBaseCriteria]
                                else:
                                    dailyStatus["remainingEfforts"] += _story[calculationBaseCriteria]


                            if len(teamCompletionStats) > 0 and dailyStatus["date"] == currentChangeDateTime:
                                dailyStatus["team"].extend(teamCompletionStats)

                            dailySprintStatusData[i] = dailyStatus
                            # if _story["number"] in _dayStatus["toDoTasks"]:
                            #     _dayStatus["toDoTasks"].remove(_story["number"])
                            #     _dayStatus[afterStatus].append(_story["number"])
                                
                            #     if afterStatus == "completedTasks":
                            #         _dayStatus["remainingEfforts"] -= _story[calculationBaseCriteria]
                            #     # elif beforeStatus == "completedTasks":
                            #     #     _dayStatus["remainingEfforts"] += _story[calculationBaseCriteria]
                            
                            # elif _story["number"] in _dayStatus["blockerTasks"]:
                            #     _dayStatus["blockerTasks"].remove(_story["number"])
                            #     _dayStatus[afterStatus].append(_story["number"])
                                
                            #     if afterStatus == "completedTasks":
                            #         _dayStatus["remainingEfforts"] -= _story[calculationBaseCriteria]
                            #     # elif beforeStatus == "completedTasks":
                            #     #     _dayStatus["remainingEfforts"] += _story[calculationBaseCriteria]

                            # elif _story["number"] in _dayStatus["inProgressTasks"]:
                            #     _dayStatus["inProgressTasks"].remove(_story["number"])
                            #     _dayStatus[afterStatus].append(_story["number"])
                                
                            #     #if the issue is moving into done status then we should remove remaining efforts
                            #     if afterStatus == "completedTasks":
                            #         _dayStatus["remainingEfforts"] -= _story[calculationBaseCriteria]
                            #     # elif beforeStatus == "completedTasks":
                            #     #     _dayStatus["remainingEfforts"] += _story[calculationBaseCriteria]
                            
                            # elif _story["number"] in _dayStatus["completedTasks"]:
                            #     _dayStatus["completedTasks"].remove(_story["number"])
                            #     _dayStatus[afterStatus].append(_story["number"])
                                
                            #     #if the issue is moving out of done status then we should add remaining efforts
                            #     if afterStatus != "completedTasks":
                            #         _dayStatus["remainingEfforts"] += _story[calculationBaseCriteria]
                                    

                    #check if story points/hours is changed
                    if _update[fieldBaseKey] == isEstimationChange:
                        if issueIncludedAtSprintStart:
                            if _update["fromString"] == None:
                                derivedSprintCollection["totalEfforts"] -= _story[calculationBaseCriteria]
                            elif _update["toString"].isnumeric():
                                derivedSprintCollection["totalEfforts"] -= (_story[calculationBaseCriteria] - int(float(_update["fromString"])))

                            if derivedSprintCollection["totalEfforts"] < 0:
                                derivedSprintCollection["totalEfforts"] = 0

                        for i, _dayStatus in enumerate(dailySprintStatusData):
                            if _dayStatus["date"] < currentChangeDateTime:
                                if _update["fromString"] == None:
                                    if (_story["number"] in _dayStatus["toDoTasks"]) or \
                                        (_story["number"] in _dayStatus["blockerTasks"]) or \
                                        (_story["number"] in _dayStatus["inProgressTasks"]):
                                        _dayStatus["remainingEfforts"] -= _story[calculationBaseCriteria]
                                    
                                    elif _story["number"] in _dayStatus["completedTasks"]:
                                        _dayStatus["completedEfforts"] -= _story[calculationBaseCriteria]

                                    if _story["number"] in _dayStatus["reopenTasks"]:
                                        _dayStatus["reopenEfforts"] -= _story[calculationBaseCriteria]

                                    if _story["number"] in _dayStatus["newlyAddedTasks"]:
                                        _dayStatus["newlyAddedEfforts"] -= _story[calculationBaseCriteria]

                                elif _update["toString"].isnumeric():
                                    if (_story["number"] in _dayStatus["toDoTasks"]) or \
                                        (_story["number"] in _dayStatus["blockerTasks"]) or \
                                        (_story["number"] in _dayStatus["inProgressTasks"]):
                                        _dayStatus["remainingEfforts"] -= (_story[calculationBaseCriteria] - int(float(_update["fromString"])))
                                    
                                    elif _story["number"] in _dayStatus["completedTasks"]:
                                        _dayStatus["completedEfforts"] -= (_story[calculationBaseCriteria] - int(float(_update["fromString"])))

                                    if _story["number"] in _dayStatus["reopenTasks"]:
                                        _dayStatus["reopenEfforts"] -= (_story[calculationBaseCriteria] - int(float(_update["fromString"])))

                                    if _story["number"] in _dayStatus["newlyAddedTasks"]:
                                        _dayStatus["newlyAddedEfforts"] -= (_story[calculationBaseCriteria] - int(float(_update["fromString"])))


                    #check if assignee is changed
                    if _update[fieldBaseKey] == isAssigneeChange:
                        issue_assignee[_story["number"]] = _update["toString"]

        ########################################################################
        ######################## Parse other issues in sprints #################
        ########################################################################

        for _story in sprint_stories:
            #if the issue is processed then ignore
            if any(_story["number"] in removed_story["number"] for removed_story in removed_sprint_stories):
                continue

            #Basic Initialization
            issue_assignee = {}
            changeLogs = []
            issueIncludedAtSprintStart = True
            issueChangeStartDate = _sprint["startDate"]
            issueChangeEndDate = _sprint["endDate"]
            isStoryEvaluatedBetweenTheSprint = False
            
            
            if (_story["ownerFullNames"] is not None) and (len(_story["ownerFullNames"]) > 0):
                issue_assignee[_story["number"]] = _story["ownerFullNames"][0]

            #check if story is added after the sprint has started
            for _change in _story["changes"]:
                for _update in _change["updates"]:
                    if _update[fieldBaseKey] == isSprintChange:
                        if _update["from"] == str(_sprint["sprintId"]) and _update["to"] == str(_sprint["sprintId"]):
                            continue

                        if str(_sprint["sprintId"]) in _update["to"].split(", "):
                            addedDate = datetime.datetime.utcfromtimestamp(_change["changeCreatedDateTime"]/1000)
                            if addedDate > sprintStartDate and addedDate < sprintEndDate:
                                issueIncludedAtSprintStart = False
                                issueChangeStartDate = round(addedDate.replace(tzinfo = timezone.utc).timestamp() * 1000)
                                #newly added tasks / efforts
                                issueChangeStartDateDelta = addedDate.replace(hour=0,minute=0,second=0,microsecond=0)
                                _index = next((index for (index, d) in enumerate(dailySprintStatusData) if d["date"] == issueChangeStartDateDelta), None)
                                if _index:
                                    dailySprintStatusData[_index]["newlyAddedEfforts"] += _story[calculationBaseCriteria]
                                    dailySprintStatusData[_index]["newlyAddedTasks"].append(_story["number"])
                                break
                    if not issueIncludedAtSprintStart:
                        break
            
            if issueIncludedAtSprintStart:
                # if (_story[calculationBaseCriteria] > 0):
                #     print(_story["number"] + "-------" + str(_story[calculationBaseCriteria]))
                derivedSprintCollection["totalEfforts"] += _story[calculationBaseCriteria]
                derivedSprintCollection["totalTasks"] += 1
            
            # issueChangeEndDate = round(datetime.datetime.utcfromtimestamp(_sprint["endDate"]/1000).replace(hour=23,minute=59,second=59,microsecond=0).replace(tzinfo = timezone.utc).timestamp()*1000)
            # changeLogs = [_change for _change in _story["changes"] if issueChangeStartDate <= _change["changeCreatedDateTime"] <= issueChangeEndDate]
            
            # get only after start date of sprint
            changeLogs = _story["changes"] 

            for _change in reversed(changeLogs):
                currentChangeDateTime = datetime.datetime.utcfromtimestamp(_change["changeCreatedDateTime"]/1000)
                currentChangeDateTime = currentChangeDateTime.replace(hour=0, minute=0, second=0, microsecond=0)
                
                for _update in _change["updates"]:

                    #check if sprint lifecycle is changed
                    #TODO: check if this is useful or not
                    # becuase we are checking the issue is being added in between
                    # then no need to update this.
                    # Only keep a check of start date while changing the status
                    if _update[fieldBaseKey] == isSprintChange:
                        
                        # if story being added or removed outside sprint start and end dates
                        # then ignore those changes.
                        if issueChangeStartDate > _change["changeCreatedDateTime"] or _change["changeCreatedDateTime"] > issueChangeEndDate:
                            continue

                        # Story added from between the sprint
                        if (_update["from"] is None or (str(_sprint["sprintId"]) not in _update["from"].split(", "))) and \
                            (str(_sprint["sprintId"]) in _update["to"].split(", ")):

                            # todo check this.
                            # isStoryEvaluatedBetweenTheSprint = True

                            for i, _dayStatus in enumerate(dailySprintStatusData):
                                if _dayStatus["date"] < currentChangeDateTime:
                                    if _story["number"] in _dayStatus["toDoTasks"]:
                                        _dayStatus["toDoTasks"].remove(_story["number"])
                                        _dayStatus["remainingEfforts"] -= _story[calculationBaseCriteria]
                                    
                                    elif _story["number"] in _dayStatus["blockerTasks"]: 
                                        _dayStatus["blockerTasks"].remove(_story["number"])
                                        _dayStatus["remainingEfforts"] -= _story[calculationBaseCriteria]

                                    elif _story["number"] in _dayStatus["inProgressTasks"]: 
                                        _dayStatus["inProgressTasks"].remove(_story["number"])
                                        _dayStatus["remainingEfforts"] -= _story[calculationBaseCriteria]
                                    
                                    elif _story["number"] in _dayStatus["completedTasks"]: 
                                        _dayStatus["completedTasks"].remove(_story["number"])
                                        _dayStatus["completedEfforts"] -= _story[calculationBaseCriteria]
                                    
                                    dailySprintStatusData[i] = _dayStatus 

                    #check if status is changed
                    if _update[fieldBaseKey] == isStatusChange:

                        #if story status changed outside sprint start and end dates
                        # then ignore those changes.
                        if issueChangeStartDate > _change["changeCreatedDateTime"] or _change["changeCreatedDateTime"] > issueChangeEndDate:
                            continue

                        #check what is the status changing from to to.
                        #if it is moving done then we should decrease estimate
                        beforeStatus = ""
                        afterStatus = ""
                        teamCompletionStats = []

                        if _update["fromString"] in possibleToDoStatuses:
                            beforeStatus = "toDoTasks"
                        elif _update["fromString"] in possibleBlockerStatuses:
                            beforeStatus = "blockerTasks"
                        elif _update["fromString"] in possibleInProgressStatuses:
                            beforeStatus = "inProgressTasks"
                        elif _update["fromString"] in possibleDoneStatuses:
                            beforeStatus = "completedTasks"

                        if _update["toString"] in possibleToDoStatuses:
                            afterStatus = "toDoTasks"
                        elif _update["toString"] in possibleBlockerStatuses:
                            afterStatus = "blockerTasks"
                        elif _update["toString"] in possibleInProgressStatuses:
                            afterStatus = "inProgressTasks"
                        elif _update["toString"] in possibleDoneStatuses:
                            afterStatus = "completedTasks"

                        
                        #if the status is moving into or from same stage then do nothing
                        if afterStatus == beforeStatus or beforeStatus == "" or afterStatus == "":
                            continue

                        isStoryEvaluatedBetweenTheSprint = True

                        #calculate reopen tasks / efforts
                        if beforeStatus == "completedTasks":
                            _index = next((index for (index, d) in enumerate(dailySprintStatusData) if d["date"] == currentChangeDateTime), None)
                            if _index:
                                dailySprintStatusData[_index]["reopenEfforts"] += _story[calculationBaseCriteria]
                                dailySprintStatusData[_index]["reopenTasks"].append(_story["number"])

                        #if status is moving into done status then add the assignee person
                        if afterStatus == "completedTasks":
                            if _story["number"] in issue_assignee:
                                teamCompletionStats.append({
                                    "number": _story["number"],
                                    "personName": issue_assignee[_story["number"]],
                                    "storyPointCompleted": _story[calculationBaseCriteria],
                                })
                            elif (_story["ownerFullNames"] is not None) and (len(_story["ownerFullNames"]) > 0):
                                teamCompletionStats.append({
                                    "number": _story["number"],
                                    "personName": _story["ownerFullNames"][0],
                                    "storyPointCompleted": _story[calculationBaseCriteria],
                                })

                        #insert daily status of issues
                        for i, dailyStatus in enumerate(dailySprintStatusData):
                            if dailyStatus["date"] < currentChangeDateTime:
                                if (_story["number"] not in dailyStatus["toDoTasks"]) and \
                                    (_story["number"] not in dailyStatus["blockerTasks"]) and \
                                    (_story["number"] not in dailyStatus["inProgressTasks"]) and \
                                    (_story["number"] not in dailyStatus["completedTasks"]):
                                    
                                    dailyStatus[beforeStatus].append(_story["number"])
                                    if beforeStatus == "completedTasks":
                                        dailyStatus["completedEfforts"] += _story[calculationBaseCriteria]
                                    else:
                                        dailyStatus["remainingEfforts"] += _story[calculationBaseCriteria]
                                    
                                    dailySprintStatusData[i] = dailyStatus
                            
                            elif dailyStatus["date"] >= currentChangeDateTime:
                                if _story["number"] in dailyStatus["toDoTasks"]:
                                    dailyStatus["toDoTasks"].remove(_story["number"])
                                    dailyStatus["remainingEfforts"] -= _story[calculationBaseCriteria]
                                elif _story["number"] in dailyStatus["inProgressTasks"]:
                                    dailyStatus["inProgressTasks"].remove(_story["number"])
                                    dailyStatus["remainingEfforts"] -= _story[calculationBaseCriteria]
                                elif _story["number"] in dailyStatus["blockerTasks"]:
                                    dailyStatus["blockerTasks"].remove(_story["number"])
                                    dailyStatus["remainingEfforts"] -= _story[calculationBaseCriteria]
                                elif _story["number"] in dailyStatus["completedTasks"]:
                                    dailyStatus["completedTasks"].remove(_story["number"])
                                    dailyStatus["completedEfforts"] -= _story[calculationBaseCriteria]

                                dailyStatus[afterStatus].append(_story["number"])
                                if afterStatus == "completedTasks":
                                    dailyStatus["completedEfforts"] += _story[calculationBaseCriteria]
                                else:
                                    dailyStatus["remainingEfforts"] += _story[calculationBaseCriteria]


                            if len(teamCompletionStats) > 0 and dailyStatus["date"] == currentChangeDateTime:
                                dailyStatus["team"].extend(teamCompletionStats)

                            dailySprintStatusData[i] = dailyStatus

                    #check if story points/hours is changed
                    if _update[fieldBaseKey] == isEstimationChange:
                        
                        # If there are story points changes before the sprint start then ignore those 
                        # and entertain all the story points changes after the sprint end date to find original story point estimate 
                        if issueChangeStartDate > _change["changeCreatedDateTime"]:
                            continue

                        isStoryEvaluatedBetweenTheSprint = True

                        if issueIncludedAtSprintStart:
                            if _update["fromString"] == None:
                                derivedSprintCollection["totalEfforts"] -= _story[calculationBaseCriteria]
                            elif _update["toString"].isnumeric():
                                derivedSprintCollection["totalEfforts"] -= (_story[calculationBaseCriteria] - int(float(_update["fromString"])))

                            if derivedSprintCollection["totalEfforts"] < 0:
                                derivedSprintCollection["totalEfforts"] = 0

                        for i, _dayStatus in enumerate(dailySprintStatusData):
                            if _dayStatus["date"] < currentChangeDateTime:
                                if _update["fromString"] == None:
                                    if (_story["number"] in _dayStatus["toDoTasks"]) or \
                                        (_story["number"] in _dayStatus["blockerTasks"]) or \
                                        (_story["number"] in _dayStatus["inProgressTasks"]):
                                        _dayStatus["remainingEfforts"] -= _story[calculationBaseCriteria]
                                    
                                    elif _story["number"] in _dayStatus["completedTasks"]:
                                        _dayStatus["completedEfforts"] -= _story[calculationBaseCriteria]

                                    if _story["number"] in _dayStatus["reopenTasks"]:
                                        _dayStatus["reopenEfforts"] -= _story[calculationBaseCriteria]

                                    if _story["number"] in _dayStatus["newlyAddedTasks"]:
                                        _dayStatus["newlyAddedEfforts"] -= _story[calculationBaseCriteria]

                                elif _update["toString"].isnumeric():
                                    if (_story["number"] in _dayStatus["toDoTasks"]) or \
                                        (_story["number"] in _dayStatus["blockerTasks"]) or \
                                        (_story["number"] in _dayStatus["inProgressTasks"]):
                                        _dayStatus["remainingEfforts"] -= (_story[calculationBaseCriteria] - int(float(_update["fromString"])))
                                    
                                    elif _story["number"] in _dayStatus["completedTasks"]:
                                        _dayStatus["completedEfforts"] -= (_story[calculationBaseCriteria] - int(float(_update["fromString"])))

                                    if _story["number"] in _dayStatus["reopenTasks"]:
                                        _dayStatus["reopenEfforts"] -= (_story[calculationBaseCriteria] - int(float(_update["fromString"])))

                                    if _story["number"] in _dayStatus["newlyAddedTasks"]:
                                        _dayStatus["newlyAddedEfforts"] -= (_story[calculationBaseCriteria] - int(float(_update["fromString"])))


                    #check if assignee is changed
                    if _update[fieldBaseKey] == isAssigneeChange:
                        issue_assignee[_story["number"]] = _update["toString"]

            # check if story is not changed between the sprint 
            # then make the current status as default status
            if not isStoryEvaluatedBetweenTheSprint:
                _status = ""
                if _story["status"] in possibleToDoStatuses:
                    _status = "toDoTasks"
                elif _story["status"] in possibleBlockerStatuses:
                    _status = "blockerTasks"
                elif _story["status"] in possibleInProgressStatuses:
                    _status = "inProgressTasks"
                elif _story["status"] in possibleDoneStatuses:
                    _status = "completedTasks"
                else:
                    continue

                if issueIncludedAtSprintStart:
                    for i, dailyStatus in enumerate(dailySprintStatusData):
                        if (_story["number"] not in dailyStatus["toDoTasks"]) and \
                            (_story["number"] not in dailyStatus["blockerTasks"]) and \
                            (_story["number"] not in dailyStatus["inProgressTasks"]) and \
                            (_story["number"] not in dailyStatus["completedTasks"]):
                            
                            dailyStatus[_status].append(_story["number"])
                            if _status == "completedTasks":
                                dailyStatus["completedEfforts"] += _story[calculationBaseCriteria]
                            else:
                                dailyStatus["remainingEfforts"] += _story[calculationBaseCriteria]
                            
                            dailySprintStatusData[i] = dailyStatus
                else:
                    #change this
                    for i, dailyStatus in enumerate(dailySprintStatusData):
                        if (_story["number"] not in dailyStatus["toDoTasks"]) and \
                            (_story["number"] not in dailyStatus["blockerTasks"]) and \
                            (_story["number"] not in dailyStatus["inProgressTasks"]) and \
                            (_story["number"] not in dailyStatus["completedTasks"]):
                            
                            dailyStatus[_status].append(_story["number"])
                            if _status == "completedTasks":
                                dailyStatus["completedEfforts"] += _story[calculationBaseCriteria]
                            else:
                                dailyStatus["remainingEfforts"] += _story[calculationBaseCriteria]
                            
                            dailySprintStatusData[i] = dailyStatus



        ########################################################################
        ######################## Insert daily status to derived sprints ########
        ########################################################################
        currentDate = round(datetime.datetime.now().timestamp() * 1000)
        for _dayStatus in dailySprintStatusData:
            finalStatus = {}
            finalStatus["date"] = round(_dayStatus["date"].replace(tzinfo=timezone.utc).timestamp())*1000
            finalStatus["remainingEfforts"] = _dayStatus["remainingEfforts"]
            finalStatus["completedEfforts"] = _dayStatus["completedEfforts"]
            finalStatus["newlyAddedEfforts"] = _dayStatus["newlyAddedEfforts"]
            finalStatus["reopenEfforts"] = _dayStatus["reopenEfforts"]
            finalStatus["completedTasks"] = len(_dayStatus["completedTasks"])
            finalStatus["remainingTasks"] = (len(_dayStatus["toDoTasks"]) + len(_dayStatus["inProgressTasks"]) + len(_dayStatus["blockerTasks"]))
            finalStatus["toDoTasks"] = len(_dayStatus["toDoTasks"])
            finalStatus["inProgressTasks"] = len(_dayStatus["inProgressTasks"])
            finalStatus["blockerTasks"] = len(_dayStatus["blockerTasks"])
            finalStatus["newlyAddedTasks"] = len(_dayStatus["newlyAddedTasks"])
            finalStatus["reopenTasks"] = len(_dayStatus["reopenTasks"])
            finalStatus["team"] = _dayStatus["team"]
            finalStatus["createdDate"] = currentDate
            derivedSprintCollection["statusData"].append(finalStatus)

        derived_sprints.append(derivedSprintCollection)
            
    return derived_sprints

def get_timestamp(dateString):
    try:
        return round(parse(dateString).timestamp() * 1000)
    except Exception as error:
        return None