const data = {
  data: {
    averagePrs: 5,
    submitters: [
      {
        name: 'Parth Shah',
        totalPrs: 11,
        openPrs: 3,
        mergedPrs: 6,
        declinedPrs: 2,
        reviewerComments: 14,
        recommits: 8,
      },
      {
        name: 'Anurag Bombarde',
        totalPrs: 10,
        openPrs: 3,
        mergedPrs: 6,
        declinedPrs: 1,
        reviewerComments: 12,
        recommits: 9,
      },
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
    brushBottom: 100,
    top: 10,
    bottom: 100,
    left: 60,
    right: 10,
    width: 570,
    height: 220,
    x: 60,
    y: 10,
  },
  coordinate: {
    x: 145.5,
    y: 218,
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
  label: 'Nisarg Thakkar',
  payload: [
    {
      stroke: '#24ff00',
      fill: '#fff',
      strokeWidth: 1,
      strokeDasharray: '',
      dataKey: 'Total PRs',
      name: 'Total PRs',
      color: '#24ff00',
      value: 59,
      payload: {
        'Declined PRs': 0,
        'Merged PRs': 58,
        Name: 'Nisarg Thakkar',
        'Open PRs': 1,
        Recommits: 6,
        'Reviewer Comments': 0,
        'Total PRs': 59,
      },
    },
  ],
}
