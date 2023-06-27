import { getLocalStorageValues } from './loginFunction'
import { NotificationManager } from 'react-notifications'
import colorCode from '../const/colorCode'

export const getColorBasedOnCode = code => {
  let tempData = { name: 'GREY', color: 'grey', label: 'Grey' }
  switch (code) {
    case 'G':
      tempData = { name: 'GREEN', color: 'green', label: 'Green' }
      break
    case 'R':
      tempData = { name: 'RED', color: 'red', label: 'Red' }
      break
    case 'A':
      tempData = { name: 'AMBER', color: 'amber', label: 'Amber' }
      break
    default:
      break
  }
  return tempData
}

export const getHomePagePath = path => {
  let tempPath = '/'

  const userRoles = getUserRoles()

  if (userRoles.length <= 1) {
    switch (path) {
      case '/client-dashboard':
      case '/client-dashboard/:id?':
        tempPath = '/client-dashboard'
        break
      case '/operational':
      case '/operational-one/:id':
      case '/strategic-board/:id':
      case '/operational-two/:id':
        tempPath = '/operational'
        break
      case '/leadership-strategic-dashboard':
        tempPath = '/leadership-strategic-dashboard'
        break
      case '/strategic-coe-head':
        tempPath = '/strategic-coe-head'
        break
      default:
        break
    }
  } else {
    tempPath = '/home'
  }
  return tempPath
}

export const getUserRoles = () => {
  const roles = getLocalStorageValues('roles')
  try {
    if (roles) {
      return roles
    } else {
      return null
    }
  } catch (err) {
    return null
  }
}

export const arraysEqual = (arr1, arr2) => {
  if (!Array.isArray(arr1) || !Array.isArray(arr2)) {
    return false
  }

  for (let i = 0; i < arr1.length; i++) {
    for (let j = 0; j < arr2.length; j++) {
      if (arr1[i] === arr2[j]) {
        return true
      }
    }
  }
  return false
}

export const debounce = (func, wait = 250, message = 'APIs end point not working', immediate) => {
  let timeout
  return function(infoMessage, waitTime) {
    let context = this,
      args = {}
    args.message = infoMessage || message
    wait = waitTime || wait
    clearTimeout(timeout)
    timeout = setTimeout(function() {
      timeout = null
      if (!immediate) func.apply(context, [args])
    }, wait)
    if (immediate && !timeout) func.apply(context, [args])
  }
}

const NotificationManagerCallback = ({ message }) => {
  NotificationManager.error(message, '', 5000)
}

export const NotificationErrorWrapper = debounce(NotificationManagerCallback)

export const convertProjectDataToDropDData = projectData => {
  if (projectData) {
    const data = []
    for (let i = 0; i < projectData.length; i++) {
      data.push({
        label: projectData[i].name,
        value: projectData[i].id,
      })
    }
    return data
  }
  return projectData
}

export const shadeColor = (color, percent) => {
  let R = parseInt(color.substring(1, 3), 16)
  let G = parseInt(color.substring(3, 5), 16)
  let B = parseInt(color.substring(5, 7), 16)

  R = parseInt((R * (100 + percent)) / 100)
  G = parseInt((G * (100 + percent)) / 100)
  B = parseInt((B * (100 + percent)) / 100)

  R = R < 255 ? R : 255
  G = G < 255 ? G : 255
  B = B < 255 ? B : 255

  let RR = R.toString(16).length === 1 ? '0' + R.toString(16) : R.toString(16)
  let GG = G.toString(16).length === 1 ? '0' + G.toString(16) : G.toString(16)
  let BB = B.toString(16).length === 1 ? '0' + B.toString(16) : B.toString(16)

  return '#' + RR + GG + BB
}

export const colorCalculation = value => {
  if (value <= 40) {
    return { className: 'green', color: '#24ff00' }
  } else if (value > 40 && value <= 70) {
    return { className: 'amber', color: '#ffb245' }
  } else {
    return { className: 'red', color: '#e00e2c' }
  }
}

export const colorCalculationReverse = value => {
  if (value <= 40) {
    return { className: 'red', color: '#e00e2c' }
  } else if (value > 40 && value <= 70) {
    return { className: 'amber', color: '#ffb245' }
  } else {
    return { className: 'green', color: '#24ff00' }
  }
}

export const dynamicColorCode = colorIndex => {
  let remainder = colorIndex % 20
  return colorCode[remainder]
}
