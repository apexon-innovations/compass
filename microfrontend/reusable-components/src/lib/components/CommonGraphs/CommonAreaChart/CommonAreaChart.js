import React, { PureComponent } from 'react'
import PropTypes from 'prop-types'
import { ResponsiveContainer } from 'recharts/lib/component/ResponsiveContainer'
import { Area } from 'recharts/lib/cartesian/Area'
import { XAxis } from 'recharts/lib/cartesian/XAxis'
import { YAxis } from 'recharts/lib/cartesian/YAxis'
import { CartesianGrid } from 'recharts/lib/cartesian/CartesianGrid'
import { ReferenceLine } from 'recharts/lib/cartesian/ReferenceLine'
import { Scatter } from 'recharts/lib/cartesian/Scatter'
import { ComposedChart } from 'recharts/lib/chart/ComposedChart'
import { Legend } from 'recharts/lib/component/Legend'
import { Text } from 'recharts/lib/component/Text'

import toolTipComponent from '../../CommonGraphSections/CustomToolTip/CustomToolTip'

export default class CommonAreaChart extends PureComponent {
  render() {
    const { allData } = this.props
    const { config, data } = allData

    return (
      <ResponsiveContainer height={config.height || 200}>
        <ComposedChart
          width={config.areaChart.width}
          height={config.areaChart.height}
          data={data}
          margin={config.areaChart.margin}
          padding={config.areaChart.padding}
        >
          <defs>
            {config.linearGradient ? (
              <linearGradient
                id={`${config.linearGradient.id}`}
                x1={`${config.linearGradient.x1}`}
                y1={`${config.linearGradient.y1}`}
                x2={`${config.linearGradient.x2}`}
                y2={`${config.linearGradient.y2}`}
              >
                {config.linearGradient.stop &&
                  config.linearGradient.stop.map((item, index) => {
                    return (
                      <stop
                        key={index}
                        offset={`${item.offset}`}
                        stopColor={`${item.stopColor}`}
                        stopOpacity={`${item.stopOpacity}`}
                      />
                    )
                  })}
              </linearGradient>
            ) : (
              ''
            )}
          </defs>
          <XAxis
            stroke={config.x.stroke || '#01ffff'}
            allowDataOverflow={config.x.interval === false ? false : true}
            dataKey={config.x.dataKey || 'name'}
            interval={config.x.interval || 0}
            height={config.x.height || 50}
            textAnchor={config.x.textAnchor || 'inherit'}
            tickLine={config.x.tickLine === false ? false : true}
            angle={config.x.angle || 0}
            tick={({ x, y, payload }) => {
              return (
                <Text
                  x={x}
                  y={y}
                  fontSize={'13'}
                  width={config.x.tick && config.x.tick.width ? config.x.tick.width : 50}
                  verticalAnchor={
                    config.x.tick && config.x.tick.verticalAnchor
                      ? config.x.tick.verticalAnchor
                      : 'middle'
                  }
                  fill={config.x.tick && config.x.tick.fill ? config.x.tick.fill : '#01ffff'}
                  angle={config.x.tick && config.x.tick.angle ? config.x.tick.angle : 0}
                  textAnchor={
                    config.x.tick && config.x.tick.textAnchor ? config.x.tick.textAnchor : 'end'
                  }
                >
                  {payload.value}
                </Text>
              )
            }}
          />
          <YAxis
            stroke={config.y.stroke}
            hide={config.y.hide || false}
            allowDataOverflow={config.y.allowDataOverflow}
            angle={config.y.angle || 0}
            interval={config.y.interval || 0}
            tick={({ x, y, payload }) => {
              return (
                <Text
                  x={x}
                  y={y}
                  width={config.y.tick && config.y.tick.width ? config.y.tick.width : 50}
                  verticalAnchor={
                    config.y.tick && config.y.tick.verticalAnchor
                      ? config.y.tick.verticalAnchor
                      : 'middle'
                  }
                  fill={config.y.tick && config.y.tick.fill ? config.y.tick.fill : '#01ffff'}
                  angle={config.y.tick && config.y.tick.angle ? config.y.tick.angle : 0}
                  textAnchor={
                    config.y.tick && config.y.tick.textAnchor ? config.y.tick.textAnchor : 'end'
                  }
                >
                  {`${payload.value}${config.y.tick.tickUnit || ''}`}
                </Text>
              )
            }}
            label={
              config.y.label ? (
                <Text
                  x={0}
                  y={0}
                  dx={config.y.dx || 15}
                  dy={config.y.dy || 170}
                  offset={0}
                  angle={config.y.angle || 0}
                  fontSize={config.y.fontSize || 12}
                  fill={config.y.fill || ''}
                >
                  {config.y.label}
                </Text>
              ) : (
                []
              )
            }
          />
          <CartesianGrid
            horizontal={config.cartesianGrid.horizontal}
            vertical={config.cartesianGrid.vertical}
            stroke={config.cartesianGrid.stroke}
            strokeWidth={config.cartesianGrid.strokeWidth || 0.3}
            fillOpacity={config.cartesianGrid.fillOpacity || 0.5}
            fill={config.cartesianGrid.fill ? `url(#${config.cartesianGrid.id})` : ''}
          />
          {config.referenceLine &&
            config.referenceLine.map((item, index) => {
              return (
                <ReferenceLine
                  key={index}
                  alwaysShow={true}
                  y={item.y}
                  stroke={item.stroke}
                  label={item.label}
                  strokeDasharray={item.strokeDasharray}
                />
              )
            })}
          {config.legend && (
            <Legend
              content={config.customLegendFnc || ''}
              iconType={config.legendIconType || 'circle'}
              align={config.legendAlign || 'left'}
              margin={config.legendMargin || {}}
              height={config.legendHeight || ''}
            />
          )}
          {config.scatter &&
            config.scatter.map((item, index) => (
              <Scatter key={index} name={item.name} dataKey={item.dataKey} fill={item.color} />
            ))}

          {config.area &&
            config.area.map(item => (
              <Area
                dataKey={item.key}
                type={item.type}
                key={item.key}
                dot={item.dot || false}
                stroke={item.stroke}
                strokeWidth={item.strokeWidth || 2}
                strokeDasharray={item.strokeDasharray || ''}
                fillOpacity={item.fillOpacity}
                fill={item.fill ? `url(#${item.id})` : ''}
              />
            ))}
          {toolTipComponent(this.props, config)}
        </ComposedChart>
      </ResponsiveContainer>
    )
  }
}

CommonAreaChart.propTypes = {
  allData: PropTypes.object,
  customToolTip: PropTypes.func,
}
