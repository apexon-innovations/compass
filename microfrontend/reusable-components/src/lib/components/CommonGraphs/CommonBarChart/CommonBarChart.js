import React, { PureComponent } from 'react'
import PropTypes from 'prop-types'
import { ResponsiveContainer } from 'recharts/lib/component/ResponsiveContainer'
import { XAxis } from 'recharts/lib/cartesian/XAxis'
import { YAxis } from 'recharts/lib/cartesian/YAxis'
import { CartesianGrid } from 'recharts/lib/cartesian/CartesianGrid'
import { Bar } from 'recharts/lib/cartesian/Bar'
import { BarChart } from 'recharts/lib/chart/BarChart'
import { Tooltip } from 'recharts/lib/component/Tooltip'
import { LabelList } from 'recharts/lib/component/LabelList'
import { Cell } from 'recharts/lib/component/Cell'
import { Text } from 'recharts/lib/component/Text'

export default class CommonBarChart extends PureComponent {
  render() {
    const { allData } = this.props
    const { data, config } = allData
    return (
      <ResponsiveContainer height={config.height || 500}>
        <BarChart
          data={data}
          barSize={config.barSize || 14}
          barGap={config.barGap}
          margin={config.margin}
        >
          <CartesianGrid
            horizontal={config.cartesianGrid.horizontal}
            vertical={config.cartesianGrid.vertical}
            strokeDasharray={config.cartesianGrid.strokeDasharray}
          />
          <XAxis
            dataKey="name"
            axisLine={config.x.axisLine}
            tickLine={config.x.tickLine}
            allowDataOverflow={config.x.allowDataOverflow}
            angle={config.x.angle}
            dy={config.x.dy}
            dx={config.x.dx}
            interval={0}
            textAnchor={config.x.textAnchor || 'end'}
          />
          <YAxis
            hide={config.y.hide ? config.y.hide : false}
            axisLine={config.y.axisLine}
            tickLine={config.y.tickLine}
            interval={0}
            ticks={config.y.ticks}
            tickSize={6}
            domain={config.y.domain}
            allowDataOverflow={true}
            unit={config.y.unit || ''}
            label={
              config.y.label ? (
                <Text
                  x={0}
                  y={0}
                  dx={15}
                  dy={config.y.dy || 170}
                  offset={0}
                  angle={-90}
                  fontSize={15}
                  fill="#8E8D8D"
                  fontWeight={'bold'}
                >
                  {config.y.label}
                </Text>
              ) : (
                []
              )
            }
          />
          {config.tooltip && <Tooltip cursor={false} />}
          {config.bar.map(item => {
            return (
              <Bar key={item.key} dataKey={item.key} stackId={item.stackId} fill={item.stroke}>
                {config.topLabel ? <LabelList dataKey="topLabel" position="top" /> : ''}
                {config.color
                  ? data.map((entry, index) => <Cell key={`cell-${index}`} fill={entry.color} />)
                  : ''}
              </Bar>
            )
          })}
        </BarChart>
      </ResponsiveContainer>
    )
  }
}

CommonBarChart.propTypes = {
  allData: PropTypes.object,
}
