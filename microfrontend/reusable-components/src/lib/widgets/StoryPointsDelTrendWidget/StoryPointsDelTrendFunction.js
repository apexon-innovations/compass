export const colorCode = [
  '#ac14b5',
  '#b78950',
  '#7d6eed',
  '#a04646',
  '#47ba7d',
  '#dcd0be',
  '#BFAF40',
  '#24ff00',
  '#D2291F',
  '#96AAD9',
  '#BEC2C8',
  '#F57900',
  '#D9C490',
]

export const mapConfigData = (config, data, selectedPointCount = 'Points') => {
  config.y.label = config.y.label =
    selectedPointCount === 'Points' ? `Story ${selectedPointCount}` : `${selectedPointCount}`
  config.line = []
  data.map(item => {
    config.line.push({
      key: item.name,
      dot: item.strokeDasharray ? false : true,
      stroke: item.stroke || colorCode[0],
      fill: item.fill || colorCode[0],
      strokeWidth: '1',
      strokeDasharray: item.strokeDasharray ? item.strokeDasharray : 0,
    })
    return item
  })
  return config
}

export const mapConfigWithDashedData = (config, data) => {
  data.map((item, index) => {
    config.line.push({
      key: item.name,
      dot: true,
      stroke: colorCode[index] || colorCode[0],
      fill: colorCode[index] || colorCode[0],
      strokeWidth: '1',
      strokeDasharray: '8, 5',
    })
    return item
  })
  return config
}

export const convertDropDownValue = projectData => {
  const data = []
  for (let i = 0; i < projectData.length; i++) {
    data.push({
      label: projectData[i].name,
      value: projectData[i].jiraProjectId,
    })
  }
  return data
}
