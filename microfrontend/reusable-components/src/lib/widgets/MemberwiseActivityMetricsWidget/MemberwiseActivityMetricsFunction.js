export const responseMap = response => {
  const months = response.data.months
  const responseData = { memberNames: [], data: [] }
  // Function for make and return response as expected graph
  // Example:-{Label: 'Member Name', sprint1:{}, sprint2:{}}
  months.map(month => {
    if (month?.members && month?.members.length > 0) {
      month.members.map(member => {
        let des = {
          'Month Name': month.dateRange,
          'Code Commits': member.codeCommits,
          'Total PRs': member.totalPrs,
          'Merged PRs': member.mergedPrs,
          'Review Comments': member.reviewCommentsOnOthersPrs,
        }
        // For check if member already exist
        let exist = false
        responseData.data.map((response, index) => {
          if (response.Label === member.name) {
            exist = true
            responseData.data[index] = { ...responseData.data[index], sprint2: des }
          }
          return response
        })

        if (!exist) {
          responseData.data.push({ Label: member.name, sprint1: des })
          responseData.memberNames.push({ label: member.name, value: member.name })
        }
        return member
      })
    }
    return month
  })
  return responseData
}

export const filterMembersByCommit = (data, filterKey) => {
  let tempData = JSON.parse(JSON.stringify(data))
  return tempData.map(item => {
    if (item.sprint1 && filterKey) {
      // Remove exist value from sprint one ex:- Code Commit, PR Commit.
      // Only keep one key data ex:- Merge Commit
      let temp = item.sprint1[filterKey]
      let Name = item.sprint1['Month Name']
      item.sprint1 = {}
      item.sprint1[filterKey] = temp
      item.sprint1['Month Name'] = Name
    }

    if (item.sprint2 && filterKey) {
      // Remove exist value from sprint second ex:- Code Commit, PR Commit.
      // Only keep one key data ex:- Merge Commit
      let temp = item.sprint2[filterKey]
      let Name = item.sprint2['Month Name']

      item.sprint2 = {}
      item.sprint2[filterKey] = temp
      item.sprint2['Month Name'] = Name
    }
    return item
  })
}

export const filterMembersByName = (data, names) => {
  let tempData = JSON.parse(JSON.stringify(data))
  return tempData.filter(item => {
    if (names.indexOf(item.Label) > -1 || names.length === 0) return true
    return false
  })
}
