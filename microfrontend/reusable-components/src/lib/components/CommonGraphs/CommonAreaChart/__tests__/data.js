const data = {
  data: [
    {
      name: 'Saint Bernard',
      Delivered: 131,
      Accepted: 131,
    },
    {
      name: 'Tosa',
      Delivered: 104,
      Accepted: 104,
    },
    {
      name: 'Utonagan',
      Delivered: 144,
      Accepted: 144,
    },
    {
      name: 'Vizsla',
      Delivered: 84,
      Accepted: 84,
    },
    {
      name: 'Whippet',
      Delivered: 207,
      Accepted: 207,
    },
    {
      name: 'Xoloitzcuintli',
      Delivered: 70,
      Accepted: 70,
    },
    {
      name: 'Yorkipoo',
      Delivered: 201,
      Accepted: 201,
    },
    {
      name: 'Zuchon',
      Delivered: 185,
      Accepted: 185,
    },
    {
      name: 'QueenslandHeelerFromQuarantine',
      Delivered: 96,
      Accepted: 96,
    },
    {
      name: 'Athens',
      Delivered: 78,
      Accepted: 53,
    },
  ],
  config: {
    height: 300,
    areaChart: {
      width: 730,
      height: 300,
      margin: {
        top: 30,
        right: 10,
        left: 0,
        bottom: 0,
      },
      padding: { bottom: 20 },
    },
    x: {
      stroke: '#01ffff',
      dataKey: 'name',
      height: 100,
      angle: false,
      interval: 0,
      allowDataOverflow: true,
      tick: {
        fill: '#01ffff',
        angle: -90,
        textAnchor: 'end',
        verticalAnchor: 'middle',
        width: 90,
      },
    },
    y: {
      stroke: '#01ffff',
      height: 65,
      dy: 160,
      angle: -90,
      fontSize: 13,
      fill: '#01ffff',
      interval: 0,
      allowDataOverflow: true,
      label: 'Story Point',
      tick: {
        fill: '#01ffff',
        angle: 0,
        textAnchor: 'end',
        verticalAnchor: 'middle',
        width: 50,
      },
    },
    cartesianGrid: {
      horizontal: true,
      vertical: false,
      stroke: '#01ffff',
      fillOpacity: 0.5,
      fill: false,
      id: 'storyPointDelVsAcc',
    },
    linearGradient: {
      id: 'storyPointDelVsAcc',
      x1: '0',
      y1: '0',
      x2: '0',
      y2: '1',
      stop: [
        {
          offset: '5%',
          stopColor: '#01ffff',
          stopOpacity: 0.8,
        },
        {
          offset: '95%',
          stopColor: '#01ffff',
          stopOpacity: 0,
        },
      ],
    },
    tooltip: true,
    toolTipType: 'color',
    legend: true,
    area: [
      {
        key: 'Delivered',
        type: 'monotone',
        dot: {
          stroke: '#0c7fff',
          strokeWidth: 2,
          r: 2,
        },
        stroke: '#00d2ff',
        strokeWidth: '2',
        fillOpacity: 0.5,
        fill: '#0c7fff',
        id: 'storyPointDelVsAcc',
      },
      {
        key: 'Accepted',
        type: 'linear',
        dot: {
          stroke: '#6cff96',
          strokeWidth: 2,
          r: 2,
        },
        stroke: '#00d2ff',
        strokeWidth: '2',
        fillOpacity: 0.5,
        fill: '',
        id: 'storyPointDelVsAcc',
      },
    ],
    referenceLine: [
      {
        stroke: '#00d2ff',
        label: 'test',
        strokeDasharray: [],
      },
    ],
    scatter: [
      {
        name: 'test',
        dataKey: 1,
        color: '#ffffff',
      },
    ],
  },
}

export default data

export const mockConfigData = {
  data: [
    {
      name: 'Saint Bernard',
      Delivered: 131,
      Accepted: 131,
    },
    {
      name: 'Tosa',
      Delivered: 104,
      Accepted: 104,
    },
    {
      name: 'Utonagan',
      Delivered: 144,
      Accepted: 144,
    },
    {
      name: 'Vizsla',
      Delivered: 84,
      Accepted: 84,
    },
    {
      name: 'Whippet',
      Delivered: 207,
      Accepted: 207,
    },
    {
      name: 'Xoloitzcuintli',
      Delivered: 70,
      Accepted: 70,
    },
    {
      name: 'Yorkipoo',
      Delivered: 201,
      Accepted: 201,
    },
    {
      name: 'Zuchon',
      Delivered: 185,
      Accepted: 185,
    },
    {
      name: 'QueenslandHeelerFromQuarantine',
      Delivered: 96,
      Accepted: 96,
    },
    {
      name: 'Athens',
      Delivered: 78,
      Accepted: 53,
    },
  ],
  config: {
    height: 0,
    areaChart: {
      width: 730,
      height: 300,
      margin: {
        top: 30,
        right: 10,
        left: 0,
        bottom: 0,
      },
      padding: { bottom: 20 },
    },
    x: {
      tickLine: false,
      stroke: '',
      dataKey: '',
      height: 0,
      angle: false,
      interval: false,
      allowDataOverflow: true,
      tick: {
        fill: '#01ffff',
        angle: -90,
        textAnchor: 'end',
        verticalAnchor: 'middle',
        width: 90,
      },
    },
    y: {
      stroke: '#01ffff',
      height: 65,
      dy: 0,
      angle: 0,
      fontSize: 0,
      fill: '',
      interval: 0,
      allowDataOverflow: true,
      label: 'Story Point',
      tick: {
        fill: '#01ffff',
        angle: 0,
        textAnchor: 'end',
        verticalAnchor: 'middle',
        width: 50,
      },
    },
    cartesianGrid: {
      horizontal: true,
      vertical: false,
      stroke: '#01ffff',
      fill: true,
      id: 'storyPointDelVsAcc',
    },
    linearGradient: false,
    tooltip: true,
    toolTipType: 'color',
    legend: true,
    area: [
      {
        key: 'Delivered',
        type: 'monotone',
        stroke: '#00d2ff',
        strokeWidth: '2',
        fillOpacity: 0.5,
        fill: '#0c7fff',
        id: 'storyPointDelVsAcc',
      },
      {
        key: 'Accepted',
        type: 'linear',
        dot: {
          stroke: '#6cff96',
          strokeWidth: 2,
          r: 2,
        },
        stroke: '#00d2ff',
        strokeWidth: '',
        fillOpacity: 0.5,
        fill: '#6cff96',
        id: '',
      },
    ],
    referenceLine: [
      {
        stroke: '#00d2ff',
        label: 'test',
        strokeDasharray: [],
      },
    ],
    scatter: [
      {
        name: 'test',
        dataKey: 1,
        color: '#ffffff',
      },
    ],
  },
}
