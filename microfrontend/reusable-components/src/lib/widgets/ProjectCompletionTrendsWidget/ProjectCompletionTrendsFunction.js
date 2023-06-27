export const mapConfigData = (config, data, selectedPointCount = 'Points') => {
  config.y.label = `Story ${selectedPointCount}`
  config.line = []
  data.map(item => {
    config.line.push({
      type: 'linear',
      key: item.name,
      dot: {
        stroke: item.stroke,
        strokeWidth: 4,
        r: 4,
      },
      stroke: item.stroke || colorCode[0],
      fill: item.fill || colorCode[0],
      strokeWidth: '1',
    })
    return item
  })
  return config
}

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

export const convertDropDownValue = projectData => {
  const data = []
  for (let i = 0; i < projectData.length; i++) {
    data.push({
      label: projectData[i].name,
      value: projectData[i].id,
    })
  }
  return data
}
