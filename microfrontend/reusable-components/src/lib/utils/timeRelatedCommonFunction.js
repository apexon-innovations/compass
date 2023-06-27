export const getQuarterOfYear = (date = new Date()) => {
  const month = date.getMonth() + 1
  return Math.ceil(month / 3)
}

export const getYearByData = (date = new Date()) => {
  return date.getFullYear()
}

export const generateArrayOfYears = (numberOfYearsBack = 5) => {
  const max = getYearByData()
  const min = max - numberOfYearsBack
  const years = []

  for (let i = max; i >= min; i--) {
    years.push(i)
  }
  return years
}
