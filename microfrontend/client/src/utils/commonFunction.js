import { storeProjectClientData } from 'reusable-components/dist/utils/projectDataStoreFunction'
import { getProjectsPsrOverallHealthDetails } from 'reusable-components/dist/services/getProjectDetailsAPI'
import { dynamicColorCode } from 'reusable-components/dist/utils/commonFunction'
import { sortFunction } from 'reusable-components/dist/utils/sortingFunction'

//For bind parent scope hence defined default function instead of arrow function
//Because arrow function create it's own scope
export function allProjectAPIWrapper() {
  const baseUrl = process.env.REACT_APP_API_END_POINT
  return getProjectsPsrOverallHealthDetails(`${baseUrl}/psr-service/project/client/all`)
    .then(responseData => {
      if (responseData.success && responseData.data.data && responseData.data.data.length > 0) {
        const projectData = responseData.data.data.map((item, i) => {
          return {
            boards: item.boards,
            repoIds: sortFunction(item.repos, 'totalLineOfCode'),
            currentSprintId: item.currentSprintId,
            currentSprintName: item.currentSprintName,
            id: item.id,
            jiraProjectId: item.jiraProjectId,
            name: item.name,
            colorCode: dynamicColorCode(i),
          }
        })
        storeProjectClientData('PROJECT_CLIENT_DATA', projectData)
      }
    })
    .catch(() => {})
}
