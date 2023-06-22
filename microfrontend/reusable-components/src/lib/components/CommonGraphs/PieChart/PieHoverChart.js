/* eslint-disable react/prop-types */
import React, { PureComponent } from 'react'
import PropTypes from 'prop-types'
import { ResponsiveContainer } from 'recharts/lib/component/ResponsiveContainer'
import { Cell } from 'recharts/lib/component/Cell'
import { Sector } from 'recharts/lib/shape/Sector'
import { PieChart } from 'recharts/lib/chart/PieChart'
import { Pie } from 'recharts/lib/polar/Pie'
import { Tooltip } from 'recharts/lib/component/Tooltip'
import { addPercentageInData } from '../../../utils/projectPercentageCalculationFuntion'

const renderActiveShape = props => {
  const RADIAN = Math.PI / 180
  const {
    cx,
    cy,
    midAngle,
    innerRadius,
    outerRadius,
    startAngle,
    endAngle,
    fill,
    value,
    name,
    roundLine,
  } = props
  const sin = Math.sin(-RADIAN * midAngle)
  const cos = Math.cos(-RADIAN * midAngle)
  const sx = cx + (outerRadius + 10) * cos
  const sy = cy + (outerRadius + 10) * sin
  const mx = cx + (outerRadius + 30) * cos
  const my = cy + (outerRadius + 30) * sin
  const ex = mx + (cos >= 0 ? 1 : -1) * 22
  const ey = my
  const textAnchor = cos >= 0 ? 'start' : 'end'

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
      {roundLine && (
        <Sector
          cx={cx}
          cy={cy}
          startAngle={startAngle}
          endAngle={endAngle}
          innerRadius={outerRadius + 6}
          outerRadius={outerRadius + 10}
          fill={fill}
        />
      )}
      <path d={`M${sx},${sy}L${mx},${my}L${ex},${ey}`} stroke={fill} fill="none" />
      <circle cx={ex} cy={ey} r={2} fill={fill} stroke="none" />
      <text
        x={ex + (cos >= 0 ? 1 : -1) * 12}
        y={ey}
        textAnchor={textAnchor}
        fill="#333"
      >{`${name}`}</text>
      <text x={ex + (cos >= 0 ? 1 : -1) * 12} y={ey} dy={18} textAnchor={textAnchor} fill="#999">
        {`Projects: ${value}`}
      </text>
    </g>
  )
}

const renderActiveLabel = ({ cx, cy, midAngle, outerRadius, fill, label, color, description }) => {
  const RADIAN = Math.PI / 180
  const sin = Math.sin(-RADIAN * midAngle)
  const cos = Math.cos(-RADIAN * midAngle)
  const sx = cx + (outerRadius + 10) * cos
  const sy = cy + (outerRadius + 10) * sin
  const mx = cx + (outerRadius + 30) * cos
  const my = cy + (outerRadius + 30) * sin
  const ex = mx + (cos >= 0 ? 1 : -1) * 22
  const ey = my
  const textAnchor = cos >= 0 ? 'start' : 'end'
  return (
    <g>
      <path d={`M${sx},${sy}L${mx},${my}L${ex},${ey}`} stroke={fill} fill="none" />
      <circle cx={ex} cy={ey} r={2} fill={fill} stroke="none" />
      <text
        x={ex + (cos >= 0 ? 1 : -1) * 12}
        y={ey}
        textAnchor={textAnchor}
        fill={color}
      >{`${label}`}</text>
      {description ? (
        <text x={ex + (cos >= 0 ? 1 : -1) * 12} y={ey} dy={18} textAnchor={textAnchor} fill="#999">
          {description}
        </text>
      ) : (
        ''
      )}
    </g>
  )
}

renderActiveShape.propTypes = {
  cx: PropTypes.any,
  cy: PropTypes.any,
  midAngle: PropTypes.any,
  innerRadius: PropTypes.any,
  outerRadius: PropTypes.any,
  startAngle: PropTypes.any,
  endAngle: PropTypes.any,
  fill: PropTypes.any,
  label: PropTypes.any,
  color: PropTypes.any,
  description: PropTypes.any,
}

export default class PieHoverChart extends PureComponent {
  constructor(props) {
    super(props)
    this.state = { activeIndex: 0 }
    this.onPieEnter = this.onPieEnter.bind(this)
  }

  onPieEnter(_data, index) {
    this.setState({
      activeIndex: index,
    })
  }

  render() {
    const { allData } = this.props
    const { config } = allData
    const data = addPercentageInData(allData.data)
    return (
      <ResponsiveContainer height={310}>
        <PieChart margin={{ left: config.margin.left, top: config.margin.top }}>
          <Pie
            activeIndex={this.state.activeIndex}
            activeShape={config.activeShape ? renderActiveShape : undefined}
            data={data}
            cx={config.cx}
            cy={config.cy}
            innerRadius={config.innerRadius}
            outerRadius={config.outerRadius}
            fill={config.fill}
            dataKey={config.dataKey}
            paddingAngle={config.paddingAngle}
            onMouseEnter={config.onMouseEnter ? this.onPieEnter : undefined}
            label={config.label ? renderActiveLabel : undefined}
          >
            {data.map((_entry, index) => (
              <Cell
                key={`cell-${index}`}
                roundLine={config.roundLine}
                fill={config.dataColor ? _entry.color : config.color[index % config.color.length]}
              />
            ))}
          </Pie>
          {config.tooltip && (
            <Tooltip
              formatter={(value, key, props) => {
                return props.payload.resources
                  ? `Total resources: ${props.payload.resources} (${props.payload.percentage}%)`
                  : value
              }}
            />
          )}
        </PieChart>
      </ResponsiveContainer>
    )
  }
}

PieHoverChart.propTypes = {
  allData: PropTypes.object,
}
