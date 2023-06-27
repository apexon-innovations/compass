import React, { PureComponent } from 'react'
import PropTypes from 'prop-types'
import { Circle } from '@tiaanduplessis/react-progressbar'
import style from './OverlapGraphs.module.scss'

const graphClass = ['firstGraph', 'secondGraph', 'thirdGraph', 'fourthGraph']
const graphColor = ['#ffb245', '#24ff00', '#f8ff37', '#d00010']
const rotation = ['-90deg', '90deg', '0deg', '180deg']

const getGraph = (item, index) => {
  return (
    <div
      key={index}
      className={[style[graphClass[index]], item.line ? style.noLeadLines : ''].join(' ')}
    >
      <Circle
        svgStyle={{
          transform: `rotate(${rotation[index]})`,
        }}
        progress={item.value / item.total}
        color={item.color || graphColor[index]}
        trailColor={item.trailColor || 'transparent'}
        strokeWidth={item.strokeWidth || 1}
        duration={item.duration || 2000}
        easing={item.easing || 'easeInOut'}
      />
      <div className={style.textBox}>
        {item.title && <div className={style.title}>{item.title}</div>}
        <div className={style.number}>{item.valueLabel || item.value}</div>
        {item.label && <div className={style.title}>{item.label}</div>}
      </div>
    </div>
  )
}

// Overlap Circle Graphs
// Parameters are
// color, rotation, strokeWidth, duration, easing, title, valueLabel, label
class OverlapGraphs extends PureComponent {
  render() {
    const graphData = this.props.graphData || []

    return graphData.length > 0 ? (
      <div className={[style.overlapGraphs, graphData.length > 2 ? style.moreSpace : ''].join(' ')}>
        <div className={style.graphWraps}>
          {this.props.graphData.map((item, index) => {
            return getGraph(item, index)
          })}
        </div>
      </div>
    ) : (
      ''
    )
  }
}

OverlapGraphs.propTypes = {
  graphData: PropTypes.array,
}

export default OverlapGraphs
