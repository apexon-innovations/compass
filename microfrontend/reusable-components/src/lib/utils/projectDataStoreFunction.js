import AES from 'crypto-js/aes'
import CryptoENC from 'crypto-js/enc-utf8'
const secretKey = 'compass@12345'

export const storeProjectDataAndDispatchEvt = (
  item,
  dispatchEventManually = true,
  selectedProjectData = false,
) => {
  const existProjectData = getStoredProjectData()

  const currentProjectData = {
    projectId: item.id,
    projects: [],
  }

  if (existProjectData.projects && existProjectData.projects.length > 0) {
    const projectExist = existProjectData.projects.find(project => project.projectId === item.id)
    if (!projectExist && selectedProjectData) {
      let projectBoardData = {
        projectId: item.id,
        boards: item.boards,
        repos: item.repos,
        ...selectedProjectData,
      }
      currentProjectData.projects = [...existProjectData.projects, projectBoardData]
    } else if (projectExist && selectedProjectData) {
      currentProjectData.projects = existProjectData.projects.map(project => {
        if (project.projectId === item.id) {
          const boards = project.boards || item.boards
          const repos = project.repos || item.repos
          return {
            ...project,
            ...selectedProjectData,
            boards: boards,
            repos: repos,
          }
        }
        return project
      })
    } else {
      currentProjectData.projects = existProjectData.projects
    }
  } else if (selectedProjectData) {
    let projectBoardData = {
      projectId: item.id,
      boards: item.boards,
      repos: item.repos,
      ...selectedProjectData,
    }
    currentProjectData.projects = [projectBoardData]
  }

  storeProjectData(currentProjectData)
  if (dispatchEventManually) {
    window.dispatchEvent(new CustomEvent('currentProjectData', { detail: currentProjectData }))
  }
}

export const setProjectRepoData = (item, dispatchEventManually = true, repoIds) => {
  const selectedRepo = repoIds ? repoIds : []
  storeProjectDataAndDispatchEvt(item, dispatchEventManually, { selectedRepo: selectedRepo })
}

export const setProjectAnyData = (item, dispatchEventManually = true, updateObject) => {
  storeProjectDataAndDispatchEvt(item, dispatchEventManually, updateObject)
}

export const setProjectBoardData = (item, dispatchEventManually = true, boardId) => {
  const selectedBoard = item.boards ? item.boards.find(board => board.boardId === boardId) : null
  storeProjectDataAndDispatchEvt(item, dispatchEventManually, { selectedBoard: selectedBoard })
}

export const setInitialBoardAndRepoDataWithDispatchEvt = (item, dispatchEventManually = true) => {
  const existProjectData = getStoredProjectData()
  if (existProjectData.projects && existProjectData.projects.length > 0) {
    const projectExist = existProjectData.projects.find(project => project.projectId === item.id)
    if (projectExist && projectExist.selectedBoard) {
      storeProjectDataAndDispatchEvt(item, dispatchEventManually, {})
      return true
    }
  }
  const selectedBoard = item.boards ? item.boards[0] : null
  const selectedRepo = item.repos ? [item.repos[0].repoId] : []
  const timeDuration = 30

  storeProjectDataAndDispatchEvt(item, dispatchEventManually, {
    selectedBoard: selectedBoard,
    selectedRepo: selectedRepo,
    timeDuration: timeDuration,
  })
  return true
}

export const getFieldValue = (item, key, defaultValue = null) => {
  item[key] = item[key] && item[key] !== 'null' ? item[key] : defaultValue
  return item
}

export const storeProjectData = projectData => {
  const projectDataString = JSON.stringify(projectData)
  const encryptedProjectData = AES.encrypt(projectDataString, secretKey)
  localStorage.setItem('PROJECT_DATA', encryptedProjectData)
  return getStoredProjectData()
}

export const getStoredProjectData = () => {
  try {
    if (localStorage.getItem('PROJECT_DATA') && localStorage.getItem('PROJECT_DATA') !== 'null') {
      // Decrypt CryptoJS jwt token
      const decProjectDataString = AES.decrypt(
        localStorage.getItem('PROJECT_DATA'),
        secretKey,
      ).toString(CryptoENC)
      const storeData = JSON.parse(decProjectDataString)
      if (storeData && Object.prototype.hasOwnProperty.call(storeData, 'sprintId')) {
        localStorage.setItem('PROJECT_DATA', null)
        return {}
      }
      if (
        storeData &&
        storeData.projects &&
        storeData.projects.length > 0 &&
        !Object.prototype.hasOwnProperty.call(storeData.projects[0], 'timeDuration')
      ) {
        localStorage.setItem('PROJECT_DATA', null)
        return {}
      }
      return storeData
    }
    return {}
  } catch (e) {
    return {}
  }
}

//Defined Keys, PROJECT_CLIENT_DATA
export const storeProjectClientData = (key, data) => {
  const projectDataString = JSON.stringify(data)
  const encryptedProjectData = AES.encrypt(projectDataString, secretKey)
  localStorage.setItem(key, encryptedProjectData)
  return getStoredData(key)
}

export const getStoredData = key => {
  try {
    if (localStorage.getItem(key) && localStorage.getItem(key) !== 'null') {
      // Decrypt CryptoJS jwt token
      const decProjectDataString = AES.decrypt(localStorage.getItem(key), secretKey).toString(
        CryptoENC,
      )
      return JSON.parse(decProjectDataString)
    }
    return []
  } catch (e) {
    return []
  }
}

export const getColorCodeFromProjectName = projectName => {
  const projectData = getStoredData('PROJECT_CLIENT_DATA')
  const data = projectData.find(item => item.name === projectName)
  if (data) {
    return data.colorCode
  }
  return false
}
