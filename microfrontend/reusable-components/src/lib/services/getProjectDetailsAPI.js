import { getApiCall } from '../utils/apiCall'

export const getProjectDetails = projectId => {
  return getApiCall(`/psr-service/project-details/${projectId}`)
}

export const getClientProjectDetails = apiEndPoint => {
  return getApiCall(apiEndPoint)
}

export const getProjectsPsrOverallHealthDetails = apiEndPoint => {
  return getApiCall(apiEndPoint)
}
