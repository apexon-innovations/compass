class AggregationAttributes:
    projectName = None
    iscProjectId = None
    jiraProjectId = None
    date = None

    openBug = 0
    openMajor = 0
    openMinor = 0
    openBlocker = 0
    openCritical = 0

    closeBug = 0
    closeMajor = 0
    closeMinor = 0
    closeBlocker = 0
    closeCritical = 0

    reopenBug = 0
    reopenMajor = 0
    reopenMinor = 0
    reopenBlocker = 0
    reopenCritical = 0

    rejectedBug = 0
    rejectedMajor = 0
    rejectedMinor = 0
    rejectedBlocker = 0
    rejectedCritical = 0

    attended = 0
    attendedMajor = 0
    attendedMinor = 0
    attendedBlocker = 0
    attendedCritical = 0

    unattended = 0
    unattendedMajor = 0
    unattendedMinor = 0
    unattendedBlocker = 0
    unattendedCritical = 0


class DefectAgeingAttributes:

    dataList = []

    def dynamicRangeCategoryList(self, rangeCategory):
        if(len(rangeCategory) == 0):
            self.dataList = [
                {
                    "min": 0,
                    "max": 3,
                    "count": 0,
                    "major": 0,
                    "minor": 0,
                    "blocker": 0,
                    "critical": 0,
                    "unattended": 0
                },
                {
                    "min": 3,
                    "max": 5,
                    "count": 0,
                    "major": 0,
                    "minor": 0,
                    "blocker": 0,
                    "critical": 0,
                    "unattended": 0
                },
                {
                    "min": 6,
                    "max": 10,
                    "count": 0,
                    "major": 0,
                    "minor": 0,
                    "blocker": 0,
                    "critical": 0,
                    "unattended": 0
                },
                {
                    "min": 11,
                    "max": 20,
                    "count": 0,
                    "major": 0,
                    "minor": 0,
                    "blocker": 0,
                    "critical": 0,
                    "unattended": 0
                },
                {
                    "min": 21,
                    "max": 30,
                    "count": 0,
                    "major": 0,
                    "minor": 0,
                    "blocker": 0,
                    "critical": 0,
                    "unattended": 0
                },
                {
                    "min": 31,
                    "max": 40,
                    "count": 0,
                    "major": 0,
                    "minor": 0,
                    "blocker": 0,
                    "critical": 0,
                    "unattended": 0
                },
                {
                    "min": 41,
                    "max": 50,
                    "count": 0,
                    "major": 0,
                    "minor": 0,
                    "blocker": 0,
                    "critical": 0,
                    "unattended": 0
                },
                {
                    "min": 51,
                    "max": 75,
                    "count": 0,
                    "major": 0,
                    "minor": 0,
                    "blocker": 0,
                    "critical": 0,
                    "unattended": 0
                },
                {
                    "min": 75,
                    "max": 100,
                    "count": 0,
                    "major": 0,
                    "minor": 0,
                    "blocker": 0,
                    "critical": 0,
                    "unattended": 0
                },
                {
                    "min": 100,
                    "max": 10000000,
                    "count": 0,
                    "major": 0,
                    "minor": 0,
                    "blocker": 0,
                    "critical": 0,
                    "unattended": 0
                }
            ]
        else:
            for dataRange in rangeCategory:
                self.dataList.append({
                    "min": dataRange["min"],
                    "max": dataRange["max"],
                    "count": 0,
                    "major": 0,
                    "minor": 0,
                    "blocker": 0,
                    "critical": 0,
                    "unattended": 0
                })

    def dataCalculation(self, daysDiff, priority, unattended):
        try:
            for data in self.dataList:
                if daysDiff >= data["min"] and daysDiff <= data["max"]:
                    data["count"] += 1
                    if(priority != None):
                        data[priority.lower()] += 1
                    else:
                        data[unattended] += 1
                    break
            return self.dataList

        except Exception as error:
            print("ERROR DefectAgeingAttributes:", error)
