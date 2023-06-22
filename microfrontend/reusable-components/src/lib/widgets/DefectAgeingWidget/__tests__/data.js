const data = {
  data: [
    {
      id: '5fa3b04a28bf219417878bb6',
      name: 'InterMiles',
      jiraProjectId: 10003,
      range: [
        { value: '0-3', count: 6 },
        { value: '3-5', count: 0 },
        { value: '6-10', count: 13 },
        { value: '11-20', count: 17 },
        { value: '21-30', count: 4 },
        { value: '31-40', count: 7 },
        { value: '41-50', count: 7 },
        { value: '51-75', count: 31 },
        { value: '75-100', count: 28 },
        { value: '>100', count: 177 },
      ],
    },
    {
      id: '5fa3b07928bf219417878bb7',
      name: 'RxSense PBM Admin',
      jiraProjectId: 12301,
      range: [
        { value: '0-3', count: 10 },
        { value: '3-5', count: 3 },
        { value: '6-10', count: 16 },
        { value: '11-20', count: 10 },
        { value: '21-30', count: 23 },
        { value: '31-40', count: 10 },
        { value: '41-50', count: 12 },
        { value: '51-75', count: 25 },
        { value: '75-100', count: 46 },
        { value: '>100', count: 214 },
      ],
    },
  ],
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
    brushBottom: 45,
    top: 5,
    bottom: 45,
    left: 65,
    right: 5,
    width: 570,
    height: 300,
    x: 65,
    y: 5,
  },
  coordinate: {
    x: 207.5,
    y: 294,
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
  label: '51-75',
  payload: [
    {
      fill: '#a3cc6b',
      dataKey: 'InterMiles',
      name: 'InterMiles',
      color: '#a3cc6b',
      value: 18,
      payload: {
        name: '51-75',
        InterMiles: 18,
        'RxSense PBM Admin': 32,
      },
    },
    {
      fill: '#d3b2f8',
      dataKey: 'RxSense PBM Admin',
      name: 'RxSense PBM Admin',
      color: '#d3b2f8',
      value: 32,
      payload: {
        name: '51-75',
        InterMiles: 18,
        'RxSense PBM Admin': 32,
      },
    },
  ],
}
