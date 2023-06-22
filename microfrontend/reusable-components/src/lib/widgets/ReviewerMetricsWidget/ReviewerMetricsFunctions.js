export const filterByColorData = (response, key) => {
  let data = [...response]
  data = data
    .filter(item => {
      return item[key] !== undefined ? true : false
    })
    .slice(0, 10)
  return data
}

export const mapConfingWithResponse = (averagePrTime, config) => {
  config.referenceLine[0].y = Number(averagePrTime)
  config.referenceLine[0].label.value = Number(averagePrTime)
  return config
}
