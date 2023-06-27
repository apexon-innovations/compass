const PieChartWithMiddleFillData = {
  config: {
    activeIndex: [0, 1, 2, 3],
    width: 350,
    height: 350,
    responsiveContainerHeight: 250,
    cx: '50%',
    cy: '42%',
    innerRadius: 35,
    outerRadius: 50,
    fill: '#82ca9d',
    dataKey: 'value',
    stroke: 'none',
    tooltip: true,
    legend: true,
    legendIconType: 'circle',
    legendHeight: '40px',
  },
  data: {
    title: 'Total',
    value: 176,
    lastUpdatedDate: 1611210518245,
    pieChartData: [
      {
        name: 'Merged',
        value: 150,
        fill: '#10e80a',
      },
      {
        name: 'Unattended',
        value: 9,
        fill: '#ffbb15',
      },
      {
        name: 'Open',
        value: 7,
        fill: '#00d2ff',
      },
      {
        name: 'Declined',
        value: 10,
        fill: '#e00e2c',
      },
    ],
  },
}

export default PieChartWithMiddleFillData

export const PieChartHoverData = {
  config: {
    activeIndex: [0, 1, 2, 3],
    width: 350,
    height: 350,
    responsiveContainerHeight: 250,
    cx: '50%',
    cy: '42%',
    innerRadius: 35,
    outerRadius: 50,
    fill: '#82ca9d',
    dataKey: 'value',
    stroke: 'none',
    tooltip: true,
    legend: false,
    legendIconType: 'circle',
    legendHeight: '40px',
    margin: {
      top: '10',
      left: '10',
    },
    color: ['#ffffff', '#fffffc', '#fffffb', '#fffffa'],
  },
  data: [
    {
      name: 'Merged',
      value: 25,
      percentage: 150,
      fill: '#10e80a',
    },
    {
      name: 'Unattended',
      value: 9,
      percentage: 25,
      fill: '#ffbb15',
    },
    {
      name: 'Open',
      value: 7,
      percentage: 25,
      fill: '#00d2ff',
    },
    {
      name: 'Declined',
      value: 10,
      percentage: 25,
      fill: '#e00e2c',
    },
  ],
}
