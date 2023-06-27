export const getSprintStatusColor = data => {
  if (data.state === 'merged') {
    return 'green'
  } else if (data.state === 'declined') {
    return 'red'
  } else if (data.state === 'unattended') {
    return 'amber'
  } else {
    return 'blue'
  }
}

export const getFilterData = (data, filter) => {
  const tempData = []
  data.map(item => {
    const currentSprintStatus = getSprintStatusColor(item)
    if (filter.indexOf(currentSprintStatus) > -1) {
      tempData.push(item)
    }
    return item
  })

  return tempData
}

export const responseMap = response => {
  return response.data.pullRequests
}
