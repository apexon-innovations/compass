export const nestData = {
  data: {
    name: 'Parth Shah',
    accountId: '',
    email: 'parth.shah@infostretch.com',
    dp: '{{some url}}',
    designation: 'Lead UX Developer',
    leaves: [
      {
        date: '20/01/2020',
      },
      {
        date: '20/02/2020',
      },
    ],
    allocation: {
      billable: '100%',
      startDate: '13-Feb-2020',
      endDate: '15-May-2020',
      totalDays: '93',
      client: 'Office',
      project: 'DES',
    },
  },
}

export const jiraData = {
  data: {
    id: '5e7d9d1552596c3bfc0cb1a6',
    jiraProjectId: '00000',
    sprintId: '8',
    boardId: 179,
    sprintName: 'IA Sprint 6',
    member: {
      name: 'Parth Shah',
      accountId: '',
      status: {
        todo: 20,
        inProgress: 25,
        completed: 8,
        availableHours: 24,
        averageStoryPoints: 1,
        spilledOver: 1,
      },
      tasks: [
        {
          number: 'IA-206',
          name: 'Efforts to be done',
          isSpilledOver: false,
          type: 'Sub-task',
          url: 'https://intermilesapp.atlassian.net/browse/IA-206',
          status: 'To Do',
        },
        {
          number: 'IA-207',
          name: 'Efforts to be done',
          isSpilledOver: true,
          type: 'Sub-task',
          url: 'https://intermilesapp.atlassian.net/browse/IA-207',
          status: 'Completed',
        },
      ],
    },
  },
}
