const getColorHight = (totalProject, value) => {
  return (value / totalProject) * 100 * 2.6
}

export const getPercentage = (totalResources, value) => {
  return ((value / totalResources) * 100).toFixed(1)
}

export const calculatePercentageAndHeight = (red = 0, amber = 0, green = 0, grey = 0) => {
  const totalProject = red + amber + green + grey
  const redHeightValue = getColorHight(totalProject, red)
  const amberHeightValue = getColorHight(totalProject, amber)
  const greenHeightValue = getColorHight(totalProject, green)
  const greyHeightValue = getColorHight(totalProject, grey)
  let {
    redPercentage,
    amberPercentage,
    greenPercentage,
    greyPercentage,
  } = calculateProjectPercentage(red, amber, green, grey)
  return {
    redData: red
      ? { color: 'red', heightValue: redHeightValue, value: red, percentage: redPercentage }
      : null,
    amberData: amber
      ? {
          color: 'amber',
          heightValue: amberHeightValue,
          value: amber,
          percentage: amberPercentage,
        }
      : null,
    greenData: green
      ? {
          color: 'green',
          heightValue: greenHeightValue,
          value: green,
          percentage: greenPercentage,
        }
      : null,
    greyData: grey
      ? { color: 'grey', heightValue: greyHeightValue, value: grey, percentage: greyPercentage }
      : null,
  }
}

export const calculateProjectPercentage = (red = 0, amber = 0, green = 0, grey = 0) => {
  const totalProject = red + amber + green + grey
  let redPercentage = getPercentage(totalProject, red)
  let amberPercentage = getPercentage(totalProject, amber)
  let greenPercentage = getPercentage(totalProject, green)
  let greyPercentage = getPercentage(totalProject, grey)

  if (redPercentage + amberPercentage + greenPercentage + greyPercentage !== 100) {
    const tempArry = [redPercentage, amberPercentage, greenPercentage, greyPercentage]
    let tempIndex = tempArry.indexOf(Math.max.apply(null, tempArry))
    tempIndex = tempIndex === -1 ? 0 : tempIndex
    let tempValue = 0
    for (let i = 0; i < tempArry.length; i++) {
      if (i !== tempIndex) tempValue = Number(tempValue) + Number(tempArry[i])
    }
    tempArry[tempIndex] = (100 - tempValue).toFixed(1)
    redPercentage = tempArry[0]
    amberPercentage = tempArry[1]
    greenPercentage = tempArry[2]
    greyPercentage = tempArry[3]
  }

  return { redPercentage, amberPercentage, greenPercentage, greyPercentage }
}

export const getProjectsOverallHealth = (projects = []) => {
  let red = 0
  let amber = 0
  let green = 0
  let grey = 0
  for (let i = 0; i < projects.length; i++) {
    const element = projects[i]
    if (element.overallHealth === 'R') {
      red += 1
    } else if (element.overallHealth === 'A') {
      amber += 1
    } else if (element.overallHealth === 'G') {
      green += 1
    } else if (element.overallHealth === 'NA') {
      grey += 1
    } else {
      grey += 1
    }
  }
  return { red, amber, green, grey }
}

export const addPercentageInData = data => {
  if (data) {
    let totalResources = 0
    for (let i = 0; i < data.length; i++) {
      totalResources += data[i].resources
    }

    let tempPercentageTotal = 0
    const tempPercentageArray = []
    for (let i = 0; i < data.length; i++) {
      data[i].percentage = parseFloat(getPercentage(totalResources, data[i].resources))
      tempPercentageTotal += data[i].percentage
      tempPercentageArray.push(data[i].percentage)
    }

    if (tempPercentageTotal !== 100) {
      let tempIndex = tempPercentageArray.indexOf(Math.max.apply(null, tempPercentageArray))
      tempIndex = tempIndex === -1 ? 0 : tempIndex
      let tempValue = 0
      for (let i = 0; i < tempPercentageArray.length; i++) {
        if (i !== tempIndex) tempValue = Number(tempValue) + Number(tempPercentageArray[i])
      }
      data[tempIndex].percentage = parseFloat((100 - tempValue).toFixed(1))
    }
    return data
  }
}
