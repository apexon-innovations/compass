/* eslint-disable react/prop-types */
import React from 'react'
import PropTypes from 'prop-types'
import { ResponsiveContainer } from 'recharts/lib/component/ResponsiveContainer'
import { Cell } from 'recharts/lib/component/Cell'
import { Sector } from 'recharts/lib/shape/Sector'
import { PieChart } from 'recharts/lib/chart/PieChart'
import { Pie } from 'recharts/lib/polar/Pie'
import { Legend } from 'recharts/lib/component/Legend'
import { Tooltip } from 'recharts/lib/component/Tooltip'

const PieChartWithMiddleFill = React.memo(({ allData, customToolTip }) => {
  const { config, data } = allData

  const renderActiveShape = props => {
    const { cx, cy, innerRadius, outerRadius, startAngle, endAngle, fill } = props

    return (
      <g>
        <Sector
          cx={cx}
          cy={cy}
          innerRadius={innerRadius}
          outerRadius={outerRadius}
          startAngle={startAngle}
          endAngle={endAngle}
          fill={fill}
        />
        <circle cx={'50%'} cy={'calc(50% - 19px)'} r="35" fill="#28354a"></circle>
        <text width="100" textAnchor="middle" fill="#00d2ff" x={cx} y={cy} dy={-5} fontSize="14">
          {data.title}
        </text>
        <text width="100" textAnchor="middle" fill="#ffc145" x={cx} y={cy} dy={15} fontSize="22">
          {data.value}
        </text>
      </g>
    )
  }

  return (
    <ResponsiveContainer height={config.responsiveContainerHeight}>
      <PieChart width={config.width} height={config.height}>
        <Pie
          activeIndex={config.activeIndex || [0, 1, 2, 3]}
          activeShape={renderActiveShape}
          data={data.pieChartData}
          cx={config.cx || '50%'}
          cy={config.cy || '50%'}
          innerRadius={config.innerRadius || 35}
          outerRadius={config.outerRadius || 50}
          fill={config.fill || '#82ca9d'}
          dataKey={config.dataKey || 'name'}
          stroke={config.stroke || 'none'}
        >
          {data.pieChartData.map((entry, index) => {
            return <Cell key={`cell-${index}`} fill={data.pieChartData[index].fill || '#82ca9d'} />
          })}
        </Pie>
        {config.tooltip && <Tooltip content={customToolTip ? customToolTip : ''} />}
        {config.legend && (
          <Legend
            content={config.customLegendFnc || ''}
            iconType={config.legendIconType || 'circle'}
            align={config.legendAlign || 'left'}
            margin={config.legendMargin || {}}
            height={config.legendHeight || 20}
          />
        )}
      </PieChart>
    </ResponsiveContainer>
  )
})

PieChartWithMiddleFill.propTypes = {
  allData: PropTypes.object,
}

export default PieChartWithMiddleFill
