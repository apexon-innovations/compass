//Filter Function
export const sortFunction = (data, key) => {
  if (data) {
    data.sort((a, b) => (a[key] < b[key] ? 1 : b[key] < a[key] ? -1 : 0))
  }
  return data
}

export const sortByNameFunction = (data, key) => {
  return data.sort(function(a, b) {
    const nameA = a[key].toUpperCase()
    const nameB = b[key].toUpperCase()
    let comparison = 0
    if (nameA > nameB) {
      comparison = 1
    } else if (nameA < nameB) {
      comparison = -1
    }
    return comparison
  })
}

export const filterData = (data, defaultSelectedValue) => {
  if (data) {
    data =
      defaultSelectedValue.key === 'Name'
        ? sortByNameFunction(data, defaultSelectedValue.key)
        : sortFunction(data, defaultSelectedValue.key)
  }
  return data
}
