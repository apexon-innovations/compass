import { getProjectsPsrOverallHealthDetails } from 'reusable-components/dist/services/getProjectDetailsAPI'

import { setInitialBoardAndRepoDataWithDispatchEvt } from 'reusable-components/dist/utils/projectDataStoreFunction'

export const updateProjectData = (obj, state, callback) => {
  const projectData = obj.detail
  let updateState
  if (state.projectId) {
    updateState = projectData.projects.find(project => project.projectId === state.projectId)
  } else if (projectData.projectId) {
    updateState = projectData.projects.find(project => project.projectId === projectData.projectId)
  }

  const updateObject = {}

  if (updateState && updateState.projectId && state.projectId !== updateObject.projectId) {
    updateObject['projectId'] = updateState.projectId
  }

  if (
    updateState &&
    updateState.selectedBoard &&
    updateState.selectedBoard.boardId &&
    state.boardId !== updateState.selectedBoard.boardId
  ) {
    updateObject['boardId'] = updateState.selectedBoard.boardId
    updateObject['sprintId'] = updateState.selectedBoard.sprintId
  }

  if (Object.keys(updateObject).length > 0) {
    callback({ ...updateObject, isLoading: false })
  }
}

export const getAllProjectApi = (state, props, callback) => {
  const baseUrl = process.env.REACT_APP_API_END_POINT
  getProjectsPsrOverallHealthDetails(`${baseUrl}/psr-service/project/all`)
    .then(responseData => {
      if (responseData.success) {
        if (responseData.data.data && responseData.data.data.length > 0) {
          if (state.projectId && state.projectId.length > 20) {
            const isExist = responseData.data.data.find(item => {
              return item.id === state.projectId
            })
            if (isExist) {
              props.navigate(`/pmo/overview/${state.projectId}`)
              callback({ isLoading: false })
            } else {
              setInitialBoardAndRepoDataWithDispatchEvt(responseData.data.data[0], false)
              props.navigate(`/pmo/overview/${responseData.data.data[0].id}`)
            }
          } else {
            setInitialBoardAndRepoDataWithDispatchEvt(responseData.data.data[0], false)
            props.navigate(`/pmo/overview/${responseData.data.data[0].id}`)
          }
        }
      }
      callback({ isLoading: false })
    })
    .catch(() => {
      callback({ isLoading: false })
    })
}
