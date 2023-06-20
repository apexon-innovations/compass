import json
import boto3
import pymongo
from datetime import datetime
from bson.objectid import ObjectId
import os


def sonar_stats(event, context):

    # Read data from S3
    s3 = boto3.client('s3')
    obj = s3.get_object(Bucket=event["Records"][0]["s3"]["bucket"]
                        ["name"], Key=event["Records"][0]["s3"]["object"]["key"])
    eventData = json.loads(obj['Body'].read())

    print(event)

    # Convert data as per UDM
    sonarObject = create_sonar_object(eventData)

    # Connect to MongoDB Database
    sonarCollection = connect_to_database()

    # Insert Data to MongoDB
    upload_to_database(sonarCollection, sonarObject)

    return {
        'statusCode': 200,
        'body': json.dumps('Task Completed')
    }


def create_sonar_object(eventData):
    # Create Mongo Object
    sonarObject = {}
    sonarObject["sonarProjectId"] = eventData["sonarProjectId"]
    sonarObject["sonarConfigurationId"] = ObjectId(eventData["sonarConfigurationId"])
    sonarObject["iscProjectId"] = ObjectId(eventData["iscProjectId"])
    sonarObject["name"] = eventData["name"]
    sonarObject["sonarUrl"] = eventData["url"]
    sonarObject["sonarUpdatedDate"] = eventData["timestamp"]

    sonarObject["securityEfforts"] = get_value_from_metrics(
        eventData["metrics"], "name", "security_remediation_effort", "Float")
    sonarObject["technicalDebtIndex"] = get_value_from_metrics(
        eventData["metrics"], "name", "sqale_index", "Integer")
    sonarObject["newTechnicalDebt"] = get_value_from_metrics(
        eventData["metrics"], "name", "new_technical_debt", "Integer")
    sonarObject["uncoveredLines"] = get_value_from_metrics(
        eventData["metrics"], "name", "uncovered_lines", "Integer")
    sonarObject["newUncoveredLines"] = get_value_from_metrics(
        eventData["metrics"], "name", "new_uncovered_lines", "Integer")
    sonarObject["complexity"] = get_value_from_metrics(
        eventData["metrics"], "name", "complexity", "Integer")
    sonarObject["cognitiveComplexity"] = get_value_from_metrics(
        eventData["metrics"], "name", "cognitive_complexity", "Integer")

    sonarObject["duplication"] = {}
    sonarObject["duplication"]["lineDensity"] = get_value_from_metrics(
        eventData["metrics"], "name", "duplicated_lines_density", "Float")
    sonarObject["duplication"]["lines"] = get_value_from_metrics(
        eventData["metrics"], "name", "duplicated_lines", "Integer")
    sonarObject["duplication"]["blocks"] = get_value_from_metrics(
        eventData["metrics"], "name", "duplicated_blocks", "Integer")
    sonarObject["duplication"]["newLineDensity"] = get_value_from_metrics(
        eventData["metrics"], "name", "new_duplicated_lines_density", "Float")
    sonarObject["duplication"]["newLines"] = get_value_from_metrics(
        eventData["metrics"], "name", "new_duplicated_lines", "Integer")
    sonarObject["duplication"]["newBlocks"] = get_value_from_metrics(
        eventData["metrics"], "name", "new_duplicated_blocks", "Integer")
    sonarObject["duplication"]["files"] = get_value_from_metrics(
        eventData["metrics"], "name", "duplicated_files", "Integer")

    sonarObject["sqale"] = {}
    sonarObject["sqale"]["debtRatio"] = get_value_from_metrics(
        eventData["metrics"], "name", "sqale_debt_ratio", "Float")
    sonarObject["sqale"]["newDebtRatio"] = get_value_from_metrics(
        eventData["metrics"], "name", "new_sqale_debt_ratio", "Float")

    sonarObject["security"] = {}
    sonarObject["security"]["vulnerabilities"] = get_value_from_metrics(
        eventData["metrics"], "name", "vulnerabilities", "Integer")
    sonarObject["security"]["newVulnerabilities"] = get_value_from_metrics(
        eventData["metrics"], "name", "new_vulnerabilities", "Integer")

    sonarObject["violations"] = {}
    sonarObject["violations"]["blocker"] = get_value_from_metrics(
        eventData["metrics"], "name", "blocker_violations", "Integer")
    sonarObject["violations"]["major"] = get_value_from_metrics(
        eventData["metrics"], "name", "major_violations", "Integer")
    sonarObject["violations"]["critical"] = get_value_from_metrics(
        eventData["metrics"], "name", "critical_violations", "Integer")
    sonarObject["violations"]["info"] = get_value_from_metrics(
        eventData["metrics"], "name", "info_violations", "Integer")
    sonarObject["violations"]["minor"] = get_value_from_metrics(
        eventData["metrics"], "name", "minor_violations", "Integer")
    sonarObject["violations"]["total"] = get_value_from_metrics(
        eventData["metrics"], "name", "violations", "Integer")

    sonarObject["newViolations"] = {}
    sonarObject["newViolations"]["blocker"] = get_value_from_metrics(
        eventData["metrics"], "name", "new_blocker_violations", "Integer")
    sonarObject["newViolations"]["major"] = get_value_from_metrics(
        eventData["metrics"], "name", "new_major_violations", "Integer")
    sonarObject["newViolations"]["critical"] = get_value_from_metrics(
        eventData["metrics"], "name", "new_critical_violations", "Integer")
    sonarObject["newViolations"]["info"] = get_value_from_metrics(
        eventData["metrics"], "name", "new_info_violations", "Integer")
    sonarObject["newViolations"]["minor"] = get_value_from_metrics(
        eventData["metrics"], "name", "new_minor_violations", "Integer")
    sonarObject["newViolations"]["total"] = get_value_from_metrics(
        eventData["metrics"], "name", "new_violations", "Integer")

    sonarObject["codeMatrix"] = {}
    sonarObject["codeMatrix"]["classes"] = get_value_from_metrics(
        eventData["metrics"], "name", "classes", "Integer")
    sonarObject["codeMatrix"]["statements"] = get_value_from_metrics(
        eventData["metrics"], "name", "statements", "Integer")
    sonarObject["codeMatrix"]["ncloc"] = get_value_from_metrics(
        eventData["metrics"], "name", "ncloc", "Integer")
    sonarObject["codeMatrix"]["commentLines"] = get_value_from_metrics(
        eventData["metrics"], "name", "comment_lines", "Integer")
    sonarObject["codeMatrix"]["files"] = get_value_from_metrics(
        eventData["metrics"], "name", "files", "Integer")
    sonarObject["codeMatrix"]["linesToCover"] = get_value_from_metrics(
        eventData["metrics"], "name", "lines_to_cover", "Integer")
    sonarObject["codeMatrix"]["directoryCnt"] = get_value_from_metrics(
        eventData["metrics"], "name", "directories", "Integer")
    sonarObject["codeMatrix"]["lines"] = get_value_from_metrics(
        eventData["metrics"], "name", "lines", "Integer")

    sonarObject["qualityMatrix"] = {}
    sonarObject["qualityMatrix"]["bugs"] = get_value_from_metrics(
        eventData["metrics"], "name", "bugs", "Integer")
    sonarObject["qualityMatrix"]["newBugs"] = get_value_from_metrics(
        eventData["metrics"], "name", "new_bugs", "Integer")
    sonarObject["qualityMatrix"]["coverage"] = get_value_from_metrics(
        eventData["metrics"], "name", "coverage", "Float")
    sonarObject["qualityMatrix"]["newCoverage"] = get_value_from_metrics(
        eventData["metrics"], "name", "new_coverage", "Float")
    sonarObject["qualityMatrix"]["codeSmells"] = get_value_from_metrics(
        eventData["metrics"], "name", "code_smells", "Integer")
    sonarObject["qualityMatrix"]["newCodeSmells"] = get_value_from_metrics(
        eventData["metrics"], "name", "new_code_smells", "Integer")

    sonarObject["issues"] = {}
    sonarObject["issues"]["open"] = get_value_from_metrics(
        eventData["metrics"], "name", "open_issues", "Integer")
    sonarObject["issues"]["confirmed"] = get_value_from_metrics(
        eventData["metrics"], "name", "confirmed_issues", "Integer")
    sonarObject["issues"]["wontFix"] = get_value_from_metrics(
        eventData["metrics"], "name", "wont_fix_issues", "Integer")
    sonarObject["issues"]["falsePositive"] = get_value_from_metrics(
        eventData["metrics"], "name", "false_positive_issues", "Integer")
    sonarObject["issues"]["reopened"] = get_value_from_metrics(
        eventData["metrics"], "name", "reopened_issues", "Integer")

    sonarObject["reliability"] = {}
    sonarObject["reliability"]["efforts"] = get_value_from_metrics(
        eventData["metrics"], "name", "reliability_remediation_effort", "Integer")
    sonarObject["reliability"]["newEfforts"] = get_value_from_metrics(
        eventData["metrics"], "name", "new_reliability_remediation_effort", "Integer")

    sonarObject["ratings"] = {}
    sonarObject["ratings"]["reliability"] = 6.0 - get_value_from_metrics(
        eventData["metrics"], "name", "reliability_rating", "Float")
    sonarObject["ratings"]["newReliability"] = 6.0 - get_value_from_metrics(
        eventData["metrics"], "name", "new_reliability_rating", "Float")
    sonarObject["ratings"]["security"] = 6.0 - get_value_from_metrics(
        eventData["metrics"], "name", "security_rating", "Float")
    sonarObject["ratings"]["newSecurity"] = 6.0 - get_value_from_metrics(
        eventData["metrics"], "name", "new_security_rating", "Float")
    sonarObject["ratings"]["maintainability"] = 6.0 - get_value_from_metrics(
        eventData["metrics"], "name", "sqale_rating", "Float")
    sonarObject["ratings"]["newMaintainability"] = 6.0 - get_value_from_metrics(
        eventData["metrics"], "name", "new_maintainability_rating", "Float")

    return sonarObject


def get_value_from_metrics(data, findKey, valueKey, dataType):
    data = [obj for obj in data if(obj[findKey] == valueKey)]
    if len(data) > 0:
        if "value" in data[0]:
            # return data[0]["value"]
            value = data[0]["value"]
            if dataType == "Integer":
                return int(value)
            elif dataType == "Float":
                return round(float(value),1)
            else:
                return value
        else:
            if dataType == "Integer":
                return 0
            elif dataType == "Float":
                return 0.0
    else:
        if dataType == "Integer":
            return 0
        elif dataType == "Float":
            return 0.0


def get_formatted_value_from_metrics(data, findKey, valueKey):
    data = [obj for obj in data if(obj[findKey] == valueKey)]
    if len(data) > 0:
        if "formattedValue" in data[0]:
            return data[0]["formattedValue"]
        else:
            return ""
    else:
        return ""


def connect_to_database():
    MONGO_DB_CONNECTION_STRING = os.environ['MONGO_DB_CONNECTION_STRING']
    MONGO_DB_DATABASE_NAME = os.environ['MONGO_DB_DATABASE_NAME']
    MONGO_DB_COLLLECTION_NAME = os.environ['MONGO_DB_COLLLECTION_NAME']
    mongoClient = pymongo.MongoClient(MONGO_DB_CONNECTION_STRING, connect=False)
    sonarDB = mongoClient[MONGO_DB_DATABASE_NAME]
    return sonarDB[MONGO_DB_COLLLECTION_NAME]


def upload_to_database(sonarCollection, sonarObject):
    currentDate = round(datetime.now().timestamp() * 1000)
    sonarObject["createdDate"] = currentDate
    sonarCollection.insert_one(sonarObject)
