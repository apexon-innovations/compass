import pymongo
import os
from datetime import datetime, timedelta, timezone
from aggregationAttributes import AggregationAttributes
from aggregationAttributes import DefectAgeingAttributes
from bson import ObjectId


def getJiraRules(jiraRulesCollection, jiraProjectId, iscProjectId):
    try:
        valueExits = {"iscProjectId": iscProjectId, "projectId":jiraProjectId}
        jiraRule = jiraRulesCollection.find_one(valueExits)
        bugTypes = jiraRule["bugTypes"]
        definationOfTodo = jiraRule["definitionOfTodo"]
        definitionOfInProgress = jiraRule["definitionOfInProgress"]
        definitionOfDevComplete = jiraRule["definitionOfDevComplete"]
        blockerDefintion = jiraRule["blockerDefintion"]
        definitionOfDone = jiraRule["definitionOfDone"]
        definitionOfAccepted = jiraRule["definitionOfAccepted"]
        definitionOfMajorSeverity = jiraRule["definitionOfMajorSeverity"]
        definitionOfMinorSeverity = jiraRule["definitionOfMinorSeverity"]
        definitionOfBlockerSeverity = jiraRule["definitionOfBlockerSeverity"]
        definitionOfCriticalSeverity = jiraRule["definitionOfCriticalSeverity"]

        # Added new category in jira rules collection
        definationOfReopen = jiraRule["definationOfReopen"]
        definationOfRejected = jiraRule["definationOfRejected"]

        openedStatus = (definationOfTodo + definitionOfInProgress +
                        definitionOfDevComplete + blockerDefintion)

        for status in definationOfReopen:
            if status in openedStatus:
                openedStatus.remove(status)

        closedStatus = (definitionOfDone + definitionOfAccepted)
        reOpenedStatus = definationOfReopen
        rangeCategory = jiraRule["rangeCategory"]
        return (bugTypes, openedStatus, closedStatus, reOpenedStatus, definitionOfMajorSeverity, definitionOfMinorSeverity, definitionOfBlockerSeverity, definitionOfCriticalSeverity, rangeCategory, definationOfRejected)

    except Exception as error:
        print("ERROR getJiraRules: ", error)


def getPriorityCategory(priorityName, jiraRulesData):
    priorityCategory = None
    try:
        # Major
        if(priorityName in jiraRulesData[4]):
            priorityCategory = "Major"
        # Minor
        elif(priorityName in jiraRulesData[5]):
            priorityCategory = "Minor"
        # Blocker
        elif(priorityName in jiraRulesData[6]):
            priorityCategory = "Blocker"
        # Critical
        else:
            priorityCategory = "Critical"

        return priorityCategory

    except Exception as error:
        print("ERROR getPriorityCategory: ", error)


def getBugStories(storiesCollection, projectName, iscProjectId, jiraProjectId, jiraRulesData):
    lookupList = []
    try:
        valueExits = {"iscProjectId": iscProjectId,"projectId":jiraProjectId, "type": {"$in": jiraRulesData[0]}}
        jiraStories = storiesCollection.find(valueExits)
        openedStatus = jiraRulesData[1]
        closedStatus = jiraRulesData[2]
        reOpenedStatus = jiraRulesData[3]
        rejectedStatus = jiraRulesData[9]
        for storyData in jiraStories:
            storyStatus = storyData["status"]
            priorityName = storyData["priorityName"]
            priorityCategory = getPriorityCategory(priorityName, jiraRulesData)
            changeLogs = storyData["changes"]
            sprintId = storyData["sprintId"]
            firstTimeFlag = True
            sprintDataFlag = False
            bugLogDate = None
            firstChangeObject = None
            firstTimeStatus = False

            for changeLog in reversed(changeLogs):
                timestampMillisecond = changeLog["changeCreatedDateTime"]
                time = timestampMillisecond/1000
                changeLogDateTimeFormat = datetime.utcfromtimestamp(time)
                date = changeLogDateTimeFormat.date()
                changeLogDate = datetime.combine(
                    date, datetime.min.time()).replace(tzinfo=timezone.utc)
                isAttended = False
                # add bug log date bugLogDate = currentDate - 1
                if(firstTimeFlag == True):
                    attributeObject = None
                    count = 0
                    # bugLogDate = changeLogDate - timedelta(1)
                    jiraCreatedDate = storyData["jiraCreatedDate"]
                    time = jiraCreatedDate/1000
                    changeLogDateTimeFormat = datetime.utcfromtimestamp(time)
                    date = changeLogDateTimeFormat.date()
                    bugLogDate = datetime.combine(
                        date, datetime.min.time()).replace(tzinfo=timezone.utc)
                    flag = False
                    for data in lookupList:
                        # date already available in lookup list
                        if(data.date == bugLogDate):
                            attributeObject = data

                            firstChangeObject = data
                            flag = True
                            break
                        elif(data.date > bugLogDate):
                            break
                        count += 1

                    # date not present in lookup list : new date
                    if (flag == False):
                        # create a Object of AggregationAttributes class (Note : after words call constructor directly)
                        attributeObject = AggregationAttributes()

                    attributeObject.projectName = projectName
                    attributeObject.iscProjectId = iscProjectId
                    attributeObject.jiraProjectId = jiraProjectId
                    attributeObject.date = bugLogDate

                    setattr(attributeObject, "openBug",
                            getattr(attributeObject, "openBug")+1)
                    setattr(attributeObject, "unattended",
                            getattr(attributeObject, "unattended")+1)
                    setattr(attributeObject, "open"+priorityCategory,
                            getattr(attributeObject, "open"+priorityCategory) + 1)
                    setattr(attributeObject, "unattended" +
                            priorityCategory, getattr(attributeObject, "unattended" + priorityCategory) + 1)
                    firstChangeObject = attributeObject

                    if(flag == False):
                        lookupList.insert(count, attributeObject)
                    firstTimeFlag = False

                updates = changeLog["updates"]
                for update in updates:
                    # only check if field == status
                    if(update["field"] == "status" or update["field"] == "Sprint"):
                        attributeObject = None
                        flag = False
                        count = 0
                        for data in lookupList:
                            # date present in look list
                            if(data.date == changeLogDate):
                                attributeObject = data
                                flag = True
                                break

                            elif(data.date > changeLogDate):
                                break
                            count += 1

                        # date not present in lookup list
                        if(flag == False):
                            attributeObject = AggregationAttributes()

                        fromString = update["fromString"]
                        toString = update["toString"]
                        attributeObject.projectName = projectName
                        attributeObject.iscProjectId = iscProjectId
                        attributeObject.jiraProjectId = jiraProjectId
                        attributeObject.date = changeLogDate

                        if(update["field"] == "status"):
                            fromStatus = None
                            # fromString fall in which category
                            if(fromString in reOpenedStatus):
                                fromStatus = "reopen"
                            elif(fromString in openedStatus):
                                fromStatus = "open"
                            elif(fromString in rejectedStatus):
                                fromStatus = "rejected"
                            else:
                                fromStatus = "close"

                            setattr(attributeObject, fromStatus+"Bug",
                                    getattr(attributeObject, fromStatus+"Bug")-1)
                            setattr(attributeObject, fromStatus+priorityCategory,
                                    getattr(attributeObject, fromStatus+priorityCategory)-1)

                            toStatus = None
                            # fromString fall in which category
                            if(toString in reOpenedStatus):
                                toStatus = "reopen"
                            elif(toString in openedStatus):
                                toStatus = "open"
                            elif(toString in rejectedStatus):
                                toStatus = "rejected"
                            else:
                                toStatus = "close"

                            setattr(attributeObject, toStatus+"Bug",
                                    getattr(attributeObject, toStatus+"Bug")+1)
                            setattr(attributeObject, toStatus +
                                    priorityCategory, getattr(attributeObject, toStatus+priorityCategory)+1)

                            if(firstTimeStatus == False and sprintDataFlag == False):
                                attributeObject.unattended -= 1
                                attributeObject.attended += 1
                                setattr(attributeObject, "unattended" +
                                        priorityCategory, getattr(attributeObject, "unattended"+priorityCategory)-1)
                                setattr(attributeObject, "attended" +
                                        priorityCategory, getattr(attributeObject, "attended"+priorityCategory)+1)

                            firstTimeStatus = True

                        else:
                            if(fromString == "" and firstTimeStatus == False):
                                attributeObject.unattended -= 1
                                attributeObject.attended += 1
                                setattr(attributeObject, "unattended" +
                                        priorityCategory, getattr(attributeObject, "unattended"+priorityCategory)-1)
                                setattr(attributeObject, "attended" +
                                        priorityCategory, getattr(attributeObject, "attended"+priorityCategory)+1)

                            elif(toString == ""):
                                if(sprintDataFlag == False and firstTimeStatus == False):
                                    firstChangeObject.attended += 1
                                    firstChangeObject.unattended -= 1
                                    setattr(firstChangeObject, "unattended" +
                                            priorityCategory, getattr(firstChangeObject, "unattended"+priorityCategory)-1)
                                    setattr(firstChangeObject, "attended" +
                                            priorityCategory, getattr(firstChangeObject, "attended"+priorityCategory)+1)

                                attributeObject.unattended += 1
                                attributeObject.attended -= 1
                                setattr(attributeObject, "unattended" +
                                        priorityCategory, getattr(attributeObject, "unattended"+priorityCategory)+1)
                                setattr(attributeObject, "attended" +
                                        priorityCategory, getattr(attributeObject, "attended"+priorityCategory)-1)

                            sprintDataFlag = True

                        if(flag == False):
                            lookupList.insert(
                                count, attributeObject)
            # sprint change log not found
            if(sprintDataFlag == False and firstTimeStatus == False):
                if(not sprintId is None):
                    for data in lookupList:
                        if(data.date == bugLogDate):
                            data.unattended = data.unattended - 1
                            setattr(data, "unattended" +
                                    priorityCategory, getattr(data, "unattended"+priorityCategory)-1)
                            data.attended = data.attended + 1
                            setattr(data, "attended" +
                                    priorityCategory, getattr(data, "attended"+priorityCategory)+1)

        return lookupList

    except Exception as error:
        print("ERROR getBugStories: ", error)


def dataVerification(storiesCollection, jiraProjectId):
    valueExits = {"projectId": jiraProjectId, "type": {"$in": jiraRulesData[0]}}
    jiraStories = storiesCollection.find(valueExits)

    attended = 0
    unattended = 0

    for storyData in jiraStories:
        flag = False
        changeLogs = storyData["changes"]
        sprintId = storyData["sprintId"]
        for changeLog in reversed(changeLogs):
            updates = changeLog["updates"]
            for update in updates:
                if(update["field"] == "status" or update["field"] == "Sprint"):
                    attended += 1
                    flag = True
                    break

            if(flag == True):
                break

        if(flag == False):
            if(sprintId is None):
                unattended += 1
            else:
                attended += 1

    print("attended : ", attended)
    print("unattended : ", unattended)


def lookupCalculation(lookupList):
    firstDate = None
    secondDate = None
    try:
        resultList = []
        if(len(lookupList) != 0):
            resultList.append(lookupList[0])
            for i in range(1, len(lookupList)):
                firstData = resultList[-1]
                secondData = lookupList[i]
                gapPresentBetweenDate = firstData.date + timedelta(days=1)
                if(gapPresentBetweenDate == secondData.date):
                    # print("no gap between two dates")
                    resultObj = mergeTwoObj(firstData, secondData)
                    resultObj.date = gapPresentBetweenDate
                    resultObj.iscProjectId = firstData.iscProjectId
                    resultObj.projectName = firstData.projectName
                    resultObj.jiraProjectId = firstData.jiraProjectId
                    resultList.append(resultObj)

                else:
                    # print("gap between two date")
                    firstDate = firstData.date
                    secondDate = secondData.date
                    while gapPresentBetweenDate < secondDate:
                        firstData = resultList[-1]
                        newObj = copyObject(firstData)
                        newObj.date = gapPresentBetweenDate
                        resultList.append(newObj)
                        gapPresentBetweenDate = gapPresentBetweenDate + timedelta(days=1)

                    resultObj = mergeTwoObj(resultList[-1], secondData)
                    resultObj.date = secondDate
                    resultObj.iscProjectId = firstData.iscProjectId
                    resultObj.projectName = firstData.projectName
                    resultObj.jiraProjectId = firstData.jiraProjectId
                    resultList.append(resultObj)

        return resultList

    except Exception as error:
        print("ERROR lookupCalculation: ", error)


def copyObject(firstObj):
    try:
        newObj = AggregationAttributes()
        newObj.projectName = firstObj.projectName
        newObj.iscProjectId = firstObj.iscProjectId
        newObj.jiraProjectId = firstObj.jiraProjectId
        newObj.openBug = firstObj.openBug
        newObj.openMajor = firstObj.openMajor
        newObj.openMinor = firstObj.openMinor
        newObj.openBlocker = firstObj.openBlocker
        newObj.openCritical = firstObj.openCritical
        newObj.closeBug = firstObj.closeBug
        newObj.closeMajor = firstObj.closeMajor
        newObj.closeMinor = firstObj.closeMinor
        newObj.closeBlocker = firstObj.closeBlocker
        newObj.closeCritical = firstObj.closeCritical
        newObj.reopenBug = firstObj.reopenBug
        newObj.reopenMajor = firstObj.reopenMajor
        newObj.reopenMinor = firstObj.reopenMinor
        newObj.reopenBlocker = firstObj.reopenBlocker
        newObj.reopenCritical = firstObj.reopenCritical
        newObj.rejectedBug = firstObj.rejectedBug
        newObj.rejectedMajor = firstObj.rejectedMajor
        newObj.rejectedMinor = firstObj.rejectedMinor
        newObj.rejectedBlocker = firstObj.rejectedBlocker
        newObj.rejectedCritical = firstObj.rejectedCritical
        newObj.attended = firstObj.attended
        newObj.attendedMajor = firstObj.attendedMajor
        newObj.attendedMinor = firstObj.attendedMinor
        newObj.attendedBlocker = firstObj.attendedBlocker
        newObj.attendedCritical = firstObj.attendedCritical
        newObj.unattended = firstObj.unattended
        newObj.unattendedMajor = firstObj.unattendedMajor
        newObj.unattendedMinor = firstObj.unattendedMinor
        newObj.unattendedBlocker = firstObj.unattendedBlocker
        newObj.unattendedCritical = firstObj.unattendedCritical

        return newObj

    except Exception as error:
        print("ERROR copyObject: ", error)


def mergeTwoObj(firstObj, secondObj):
    try:
        resultObj = AggregationAttributes()
        resultObj.iscProjectId = firstObj.iscProjectId
        resultObj.projectName = firstObj.projectName
        resultObj.jiraProjectId = firstObj.jiraProjectId
        resultObj.openBug = firstObj.openBug + secondObj.openBug
        resultObj.openMajor = firstObj.openMajor + secondObj.openMajor
        resultObj.openMinor = firstObj.openMinor + secondObj.openMinor
        resultObj.openBlocker = firstObj.openBlocker + secondObj.openBlocker
        resultObj.openCritical = firstObj.openCritical + secondObj.openCritical
        resultObj.closeBug = firstObj.closeBug + secondObj.closeBug
        resultObj.closeMajor = firstObj.closeMajor + secondObj.closeMajor
        resultObj.closeMinor = firstObj.closeMinor + secondObj.closeMinor
        resultObj.closeBlocker = firstObj.closeBlocker + secondObj.closeBlocker
        resultObj.closeCritical = firstObj.closeCritical + secondObj.closeCritical
        resultObj.reopenBug = firstObj.reopenBug + secondObj.reopenBug
        resultObj.reopenMajor = firstObj.reopenMajor + secondObj.reopenMajor
        resultObj.reopenMinor = firstObj.reopenMinor + secondObj.reopenMinor
        resultObj.reopenBlocker = firstObj.reopenBlocker + secondObj.reopenBlocker
        resultObj.reopenCritical = firstObj.reopenCritical + secondObj.reopenCritical
        resultObj.rejectedBug = firstObj.rejectedBug + secondObj.rejectedBug
        resultObj.rejectedMajor = firstObj.rejectedMajor + secondObj.rejectedMajor
        resultObj.rejectedMinor = firstObj.rejectedMinor + secondObj.rejectedMinor
        resultObj.rejectedBlocker = firstObj.rejectedBlocker + secondObj.rejectedBlocker
        resultObj.rejectedCritical = firstObj.rejectedCritical + secondObj.rejectedCritical
        resultObj.attended = firstObj.attended + secondObj.attended
        resultObj.attendedMajor = firstObj.attendedMajor + secondObj.attendedMajor
        resultObj.attendedMinor = firstObj.attendedMinor + secondObj.attendedMinor
        resultObj.attendedBlocker = firstObj.attendedBlocker + secondObj.attendedBlocker
        resultObj.attendedCritical = firstObj.attendedCritical + secondObj.attendedCritical
        resultObj.unattended = firstObj.unattended + secondObj.unattended
        resultObj.unattendedMajor = firstObj.unattendedMajor + secondObj.unattendedMajor
        resultObj.unattendedMinor = firstObj.unattendedMinor + secondObj.unattendedMinor
        resultObj.unattendedBlocker = (
            firstObj.unattendedBlocker + secondObj.unattendedBlocker)
        resultObj.unattendedCritical = (
            firstObj.unattendedCritical + secondObj.unattendedCritical)

        return resultObj

    except Exception as error:
        print("ERROR mergeTwoObj: ", error)


def getDefectData(storiesCollection, projectName, iscProjectId, jiraProjectId, jiraRulesData):
    try:
        valueExits = {"iscProjectId": iscProjectId,
                        "projectId":jiraProjectId,
                      "type": {"$in": jiraRulesData[0]},
                      "state": {"$in": jiraRulesData[1]}}
        jiraStories = storiesCollection.find(valueExits)
        defectAgeingAttributesObject = DefectAgeingAttributes()
        defectAgeingAttributesObject.dynamicRangeCategoryList(jiraRulesData[8])
        for storyData in jiraStories:
            priorityName = storyData["priorityName"]
            priorityCategory = getPriorityCategory(priorityName, jiraRulesData)
            todayDate = datetime.today().date()
            todayDatetime = datetime.combine(
                todayDate, datetime.min.time()).replace(tzinfo=timezone.utc)

            timestampMillisecond = storyData["jiraCreatedDate"]
            time = timestampMillisecond/1000
            changeLogDateTimeFormat = datetime.utcfromtimestamp(time)
            changeLogDate = changeLogDateTimeFormat.date()
            bugLogDatetime = datetime.combine(
                changeLogDate, datetime.min.time()).replace(tzinfo=timezone.utc)

            daysDifference = (todayDatetime - bugLogDatetime).days
            if(storyData["sprintId"] != None):
                defectAgeingAttributesObject.dataCalculation(
                    daysDifference, priorityCategory, None)
            else:
                defectAgeingAttributesObject.dataCalculation(
                    daysDifference, None, "unattended")

        projectRangeWiseData = {
            "projectName": projectName,
            "iscProjectId": iscProjectId,
            "jiraProjectId": jiraProjectId,
            "data": defectAgeingAttributesObject.dataList
        }
        return projectRangeWiseData

    except Exception as error:
        print("ERROR getDefectData: ", error)


def prepareDict(resultObj):
    _derivedStory = {}
    try:
        _derivedStory["projectName"] = resultObj.projectName
        _derivedStory["iscProjectId"] = resultObj.iscProjectId
        _derivedStory["jiraProjectId"] = resultObj.jiraProjectId
        _derivedStory["date"] = round((resultObj.date.replace(tzinfo=timezone.utc)).timestamp()*1000)
        _derivedStory["openBug"] = resultObj.openBug
        _derivedStory["openMajor"] = resultObj.openMajor
        _derivedStory["openMinor"] = resultObj.openMinor
        _derivedStory["openBlocker"] = resultObj.openBlocker
        _derivedStory["openCritical"] = resultObj.openCritical

        _derivedStory["closeBug"] = resultObj.closeBug
        _derivedStory["closeMajor"] = resultObj.closeMajor
        _derivedStory["closeMinor"] = resultObj.closeMinor
        _derivedStory["closeBlocker"] = resultObj.closeBlocker
        _derivedStory["closeCritical"] = resultObj.closeCritical

        _derivedStory["reopenBug"] = resultObj.reopenBug
        _derivedStory["reopenMajor"] = resultObj.reopenMajor
        _derivedStory["reopenMinor"] = resultObj.reopenMinor
        _derivedStory["reopenBlocker"] = resultObj.reopenBlocker
        _derivedStory["reopenCritical"] = resultObj.reopenCritical

        _derivedStory["rejectedBug"] = resultObj.rejectedBug
        _derivedStory["rejectedMajor"] = resultObj.rejectedMajor
        _derivedStory["rejectedMinor"] = resultObj.rejectedMinor
        _derivedStory["rejectedBlocker"] = resultObj.rejectedBlocker
        _derivedStory["rejectedCritical"] = resultObj.rejectedCritical

        _derivedStory["attendedBug"] = resultObj.attended
        _derivedStory["attendedMajor"] = resultObj.attendedMajor
        _derivedStory["attendedMinor"] = resultObj.attendedMinor
        _derivedStory["attendedBlocker"] = resultObj.attendedBlocker
        _derivedStory["attendedCritical"] = resultObj.attendedCritical

        _derivedStory["unattendedBug"] = resultObj.unattended
        _derivedStory["unattendedMajor"] = resultObj.unattendedMajor
        _derivedStory["unattendedMinor"] = resultObj.unattendedMinor
        _derivedStory["unattendedBlocker"] = resultObj.unattendedBlocker
        _derivedStory["unattendedCritical"] = resultObj.unattendedCritical

        return _derivedStory

    except Exception as error:
        print("ERROR prepareDict: ", error)


def uploadDataToDerivedStoriesCollection(derivedStoriesCollection, resultList, iscProjectId,jiraProjectId):
    try:
        # first remove existing collection
        query = {"iscProjectId": iscProjectId,"jiraProjectId":jiraProjectId}
        deletedEntries = derivedStoriesCollection.delete_many(query)
        
        # print(deletedEntries.deleted_count, " documents deleted.")
        derivedStories = []
        for result in resultList:
            derivedStories.append(prepareDict(result))
        derivedStoriesCollection.insert_many(derivedStories)
    except Exception as error:
        print("ERROR uploadDataToDerivedStoriesCollection", error)


def uploadDataToDefectAgeingCollection(defectAgingCollection, defectAgingData, iscProjectId,jiraProjectId):
    try:
        # first remove existing collection
        query = {"iscProjectId": iscProjectId,"jiraProjectId": jiraProjectId}
        deletedEntries = defectAgingCollection.delete_many(query)

        defectAgingCollection.insert_one(defectAgingData)
    except Exception as error:
        print("ERROR uploadDataToDefectAgeingCollection: ", error)


def aggregation(event, context):
    try:

        MONGO_URI = os.environ["MONGO_URI"]
        MONGO_DB = os.environ["MONGO_DB"]
        MONGO_COLLECTION_ISC_PROJECTS = os.environ["MONGO_COLLECTION_ISC_PROJECTS"]
        MONGO_COLLECTION_JIRA_CONFIGURATION = os.environ["MONGO_COLLECTION_JIRA_CONFIGURATION"]
        MONGO_COLLECTION_DEFECT_DATA = os.environ["MONGO_COLLECTION_DEFECT_DATA"]
        MONGO_COLLECTION_STORIES = os.environ["MONGO_COLLECTION_STORIES"]
        MONGO_COLLECTION_DERIVED_STORIES = os.environ["MONGO_COLLECTION_DERIVED_STORIES"]
        MONGO_COLLECTION_JIRA_RULES = os.environ["MONGO_COLLECTION_JIRA_RULES"]

        mongoClient = pymongo.MongoClient(MONGO_URI, connect=False)
        desData = mongoClient[MONGO_DB]
        iscProjectsCollection = desData[MONGO_COLLECTION_ISC_PROJECTS]
        jiraConfigurationCollection = desData[MONGO_COLLECTION_JIRA_CONFIGURATION]
        storiesCollection = desData[MONGO_COLLECTION_STORIES]
        defectAgingCollection = desData[MONGO_COLLECTION_DEFECT_DATA]
        derivedStoriesCollection = desData[MONGO_COLLECTION_DERIVED_STORIES]
        jiraRulesCollection = desData[MONGO_COLLECTION_JIRA_RULES]

        if isinstance(event, list):
            projectIdList = []
            for projectId in event:
                projectIdList.append(ObjectId(projectId["iscProjectId"]))

            valueExits = {'iscProjectId' : {'$in':projectIdList}}
            jiraConfigProjects = jiraConfigurationCollection.find(valueExits)
            for jiraConfigProject in jiraConfigProjects:
                try:
                    jiraProjectId = int(jiraConfigProject["jiraProjectId"])
                    iscProjectId = jiraConfigProject["iscProjectId"]
                    jiraRulesData = getJiraRules(jiraRulesCollection, jiraProjectId, iscProjectId)
                    queryData = {"_id": iscProjectId, "isDeleted": False}
                    iscProject = iscProjectsCollection.find(queryData)

                    for project in iscProject:
                        projectName = project["name"]

                    # dataVerification(storiesCollection, jiraProjectId)
                    
                    # defect trends data gathering
                    print("Started defect trends data gathering for projectId : " + str(iscProjectId))
                    lookupListForDefectTrends = getBugStories(storiesCollection, projectName, iscProjectId, jiraProjectId, jiraRulesData)
                    finalResultListForDefectTrends = lookupCalculation(lookupListForDefectTrends)
                    if(len(finalResultListForDefectTrends) != 0):
                        uploadDataToDerivedStoriesCollection(derivedStoriesCollection, finalResultListForDefectTrends, iscProjectId,jiraProjectId)
                    print("Completed defect trends data gathering for projectId : " + str(iscProjectId))

                    # defect aging data
                    print("Started defect aging data gathering for projectId : " + str(iscProjectId))
                    defectAgeingData = getDefectData(storiesCollection, projectName, iscProjectId, jiraProjectId, jiraRulesData, jiraProjectId)
                    uploadDataToDefectAgeingCollection(defectAgingCollection, defectAgeingData,  iscProjectId)
                    print("Completed defect aging data gathering for projectId : " + str(iscProjectId))

                except Exception as error:
                    print("ERROR: ", error)
                    continue

            return {
                'statusCode': 200,
                'body': 'Task Completed for '+ str(len(projectIdList)) +' project(s)'
            }

        else:
            raise Exception("Unable to parse payload")

    except Exception as error:
        print("ERROR main: ", error)
        return {
            'statusCode': 300,
            'body': error
        }