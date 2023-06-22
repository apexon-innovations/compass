const data = {
  data: {
    id: '5fa3b04a28bf219417878bb6',
    name: 'InterMiles',
    jiraProjectId: 10003,
    range: [
      { value: '0-3', critical: 1, blocker: 0, major: 2, minor: 0, unattended: 3 },
      { value: '3-5', critical: 0, blocker: 0, major: 0, minor: 0, unattended: 0 },
      { value: '6-10', critical: 4, blocker: 0, major: 3, minor: 0, unattended: 6 },
      { value: '11-20', critical: 1, blocker: 1, major: 4, minor: 0, unattended: 11 },
      { value: '21-30', critical: 0, blocker: 0, major: 1, minor: 0, unattended: 3 },
      { value: '31-40', critical: 0, blocker: 0, major: 1, minor: 0, unattended: 6 },
      { value: '41-50', critical: 1, blocker: 1, major: 0, minor: 0, unattended: 5 },
      { value: '51-75', critical: 1, blocker: 1, major: 13, minor: 0, unattended: 16 },
      { value: '75-100', critical: 1, blocker: 2, major: 4, minor: 0, unattended: 21 },
      { value: '>100', critical: 25, blocker: 10, major: 63, minor: 0, unattended: 79 },
    ],
  },
}

export default data

export const toolTipData = {
  cursor: false,
  active: true,
  allowEscapeViewBox: {
    x: false,
    y: false,
  },
  offset: 10,
  viewBox: {
    brushBottom: 80,
    top: 10,
    bottom: 80,
    left: 60,
    right: 30,
    width: 434,
    height: 260,
    x: 60,
    y: 10,
  },
  coordinate: {
    x: 277,
    y: 245,
  },
  cursorStyle: {},
  separator: ' : ',
  wrapperStyle: {},
  contentStyle: {},
  itemStyle: {},
  labelStyle: {},
  isAnimationActive: true,
  animationEasing: 'ease',
  animationDuration: 400,
  filterNull: true,
  useTranslate3d: false,
  label: 'Dec-20',
  payload: [
    {
      stroke: '#01ffff',
      strokeWidth: '1',
      strokeDasharray: '',
      fillOpacity: 0.5,
      fill: 'url(#storyPointDelVsAcc)',
      dataKey: 'Defects',
      name: 'Defects',
      color: '#01ffff',
      value: 94.06,
      payload: {
        name: 'Dec-20',
        'Acted Upon': 3585,
        Accepted: 3372,
        Defects: 94.06,
      },
    },
  ],
}
