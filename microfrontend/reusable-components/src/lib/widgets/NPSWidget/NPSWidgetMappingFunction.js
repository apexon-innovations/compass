export const chartLines = [
  {
    key: 'score1',
    dot: true,
    stroke: '#ecbaf4',
    strokeWidth: '1',
  },
  {
    key: 'score2',
    dot: false,
    stroke: '#10e80a',
    strokeWidth: '1',
  },
  {
    key: 'score3',
    dot: false,
    stroke: '#7849ea',
    strokeWidth: '1',
  },
  {
    key: 'score4',
    dot: false,
    stroke: '#91ebf2',
    strokeWidth: '1',
  },
  {
    key: 'score5',
    dot: false,
    stroke: '#f8ff73',
    strokeWidth: '1',
  },
]

export const mappingNPSKeys = [
  { name: 'Project Understanding', key: 'requirements' },
  { name: 'Would Recommend', key: 'overall' },
  { name: 'Completeness and quality', key: 'qualityAndCompleteness' },
  { name: 'Input based Improvements', key: 'improvementEfforts' },
  { name: 'Additional value added', key: 'valueAddition' },
  { name: 'Team Communication', key: 'communication' },
  { name: 'Team Proactiveness', key: 'proactiveness' },
  { name: 'AM responsiveness', key: 'responsiveness' },
  { name: 'AM industry Knowledge', key: 'businessKnowledge' },
  { name: 'Ease of doing Business', key: 'easeOfDoingBusiness' },
]

export const recentNPSDataMappingFnc = response => {
  let recentDataArray = []
  if (response.data.survey && response.data.survey.value) {
    recentDataArray = mappingNPSKeys
    mappingNPSKeys.map((item, index) => {
      response.data.survey.value.map(item2 => {
        if (item.key === item2.key) {
          recentDataArray[index]['Rating'] = item2.rating
        }
        return item2
      })
      return item
    })
  }
  return recentDataArray
}

export const lastFiveNPSDataMappingFnc = response => {
  let lastFiveArray = []

  if (response.data.survey && response.data.survey.length > 0) {
    lastFiveArray = mappingNPSKeys
    mappingNPSKeys.map((item, index) => {
      response.data.survey.map(item2 => {
        item2.value.map(item3 => {
          if (item3.key === item.key) {
            lastFiveArray[index][item2.duration] = item3.rating
          }
          return item3
        })
        return item2
      })
      return item
    })
  }
  return lastFiveArray
}

export const lastFiveNPSConfigMappingFnc = (config, response) => {
  config.line = []
  if (response.data.survey && response.data.survey.length > 0) {
    response.data.survey.map((item, index) => {
      if (chartLines[index]) {
        chartLines[index].key = item.duration
        config.line.push(chartLines[index])
      }
      return item
    })
  }
  config.referenceLine[0].y = Number(response.data.averageNps).toFixed(1)
  config.referenceLine[0].label.value = Number(response.data.averageNps).toFixed(1)
  return config
}

export const recentNPSConfigMappingFnc = (config, response) => {
  config.referenceLine[0].y = Number(response).toFixed(1)
  config.referenceLine[0].label.value = Number(response).toFixed(1)
  return config
}
