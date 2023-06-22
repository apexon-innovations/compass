import React, { PureComponent } from 'react'
import PropTypes from 'prop-types'
import { ResponsiveContainer } from 'recharts/lib/component/ResponsiveContainer'
import { Line } from 'recharts/lib/cartesian/Line'
import { Area } from 'recharts/lib/cartesian/Area'
import { XAxis } from 'recharts/lib/cartesian/XAxis'
import { YAxis } from 'recharts/lib/cartesian/YAxis'
import { CartesianGrid } from 'recharts/lib/cartesian/CartesianGrid'
import { ReferenceLine } from 'recharts/lib/cartesian/ReferenceLine'
import { Scatter } from 'recharts/lib/cartesian/Scatter'
import { Bar } from 'recharts/lib/cartesian/Bar'
import { ComposedChart } from 'recharts/lib/chart/ComposedChart'
import { Legend } from 'recharts/lib/component/Legend'
import { LabelList } from 'recharts/lib/component/LabelList'
import { Cell } from 'recharts/lib/component/Cell'
import { Text } from 'recharts/lib/component/Text'

import toolTipComponent from '../../CommonGraphSections/CustomToolTip/CustomToolTip'

export default class CommonLineChart extends PureComponent {
  constructor(props) {
    super(props)
    this.state = { lineIndex: false }
  }

  mouseOverCallback = (e, lineIndex) => {
    this.setState({ lineIndex: lineIndex.toString() })
  }

  mouseLeaveCallback = () => {
    this.setState({ lineIndex: false })
  }

  render() {
    const { allData } = this.props
    const { config, data } = allData
    if (this.props.customTickFnc) {
      config.x.tick = this.props.customTickFnc
    }
    if (this.props.customLegendFnc) {
      config.customLegendFnc = this.props.customLegendFnc
    }

    if (!config.x.tick) {
      config.x.tick = {}
    }

    return (
      <ResponsiveContainer height={config.height || 500}>
        <ComposedChart
          height={config.height}
          data={data}
          margin={config.margin}
          padding={config.padding}
        >
          <defs>
            <linearGradient id="rGraphBgLineChart" x1="0" y1="0" x2="0" y2="1">
              <stop offset="30%" stopColor="#01ffff" stopOpacity={0} />
              <stop offset="100%" stopColor="#01ffff" stopOpacity={0.8} />
            </linearGradient>
          </defs>
          <XAxis
            axisLine={config.x.axisLine}
            stroke={config.x.stroke}
            tick={({ x, y, payload }) => {
              return (
                <Text
                  x={x}
                  y={y}
                  width={config.x.tick.width || 60}
                  verticalAnchor={config.x.tick.verticalAnchor || 'middle'}
                  fill={config.x.tick.fill || '#01ffff'}
                  angle={config.x.angle || -90}
                  textAnchor={config.x.textAnchor || 'end'}
                  fontSize={config.x.tick.fontSize || '13'}
                >
                  {payload.value}
                </Text>
              )
            }}
            dataKey={config.x.dataKey || 'name'}
            tickLine={config.x.tickLine}
            hide={config.x.hide || false}
            allowDataOverflow={config.x.allowDataOverflow}
            angle={config.x.angle}
            dy={config.x.dy}
            dx={config.x.dx}
            height={config.x.height}
            interval={0}
            textAnchor={'end'}
          />
          <YAxis
            tickCount={config.y.tickCount || 6}
            tick={config.y.tick || {}}
            hide={config.y.hide || false}
            axisLine={config.y.axisLine}
            stroke={config.y.stroke}
            tickLine={config.y.tickLine}
            width={config.y.width || 60}
            allowDecimals={config.allowDecimals === false ? false : true}
            allowDataOverflow={config.y.allowDataOverflow}
            interval={config.y.interval || 0}
            domain={config.y.domain || [0, 'auto']}
            unit={config.y.unit || ''}
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
          <CartesianGrid
            vertical={config.cartesianGrid.vertical}
            horizontal={config.cartesianGrid.horizontal}
            fillOpacity={config.cartesianGrid.fillOpacity || 0.5}
            strokeWidth={config.cartesianGrid.strokeWidth || 0.3}
            stroke={config.cartesianGrid.stroke}
            fill={config.cartesianGrid.fill ? 'url(#rGraphBgLineChart)' : ''}
          />
          {config.line &&
            config.line.map((item, index) => {
              //Implementation condition for show tooltip, on hover on particular active dot
              const lineProps = {}
              if (config.activeDot && this.props.customToolTip) {
                lineProps.activeDot = {
                  onMouseOver: e => {
                    this.mouseOverCallback(e, index)
                  },
                  onMouseLeave: this.mouseLeaveCallback,
                }
              }
              return (
                <Line
                  {...lineProps}
                  type={item.type || 'linear'}
                  dot={item.dot || false}
                  key={`line-${index}`}
                  dataKey={item.key}
                  stroke={item.stroke}
                  fill={item.fill}
                  strokeWidth={item.strokeWidth || 1}
                  strokeDasharray={item.strokeDasharray || ''}
                />
              )
            })}
          {config.bar &&
            config.bar.map((item, index) => {
              return (
                <Bar
                  key={`bar-${index}`}
                  barSize={item.barSize}
                  dataKey={item.key}
                  onMouseOver={item.onMouseOver ? item.onMouseOver : () => {}}
                  stackId={item.stackId}
                  fill={item.stroke}
                  stroke={item.strokeColor}
                  strokeDasharray={item.strokeDasharray || ''}
                >
                  {config.topLabel ? <LabelList dataKey="topLabel" position="top" /> : ''}
                  {config.color
                    ? data.map((entry, index) => <Cell key={`cell-${index}`} fill={entry.color} />)
                    : ''}
                </Bar>
              )
            })}
          {config.scatter &&
            config.scatter.map((item, index) => (
              <Scatter key={index} name={item.name} dataKey={item.dataKey} fill={item.color} />
            ))}
          {config.area &&
            config.area.map(item => (
              <Area
                dataKey={item.key}
                key={item.key}
                dot={item.dot || false}
                stroke={item.stroke}
                strokeWidth={item.strokeWidth || 2}
                strokeDasharray={item.strokeDasharray || ''}
                fillOpacity={item.fillOpacity}
                fill={item.fill}
              />
            ))}
          {/* Function to return Tooltip component, here functional component will not work */}
          {toolTipComponent(this.props, config, this.state.lineIndex)}
        </ComposedChart>
      </ResponsiveContainer>
    )
  }
}

CommonLineChart.propTypes = {
  allData: PropTypes.object,
  customTickFnc: PropTypes.func,
  customLegendFnc: PropTypes.func,
  customToolTip: PropTypes.oneOfType([PropTypes.func, PropTypes.bool]),
}
