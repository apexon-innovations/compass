import React from 'react'
import PropTypes from 'prop-types'
import { Tooltip } from 'recharts/lib/component/Tooltip'

const customTooltipFnc = (type = '') => {
  return props => {
    const { active, payload, label } = props
    if (active) {
      return (
        <div className="customToolTip">
          <p className="title">{`${label}`}</p>
          <ul className="reviewList">
            {payload &&
              payload.map((item, index) => {
                return (
                  <li key={index} style={{ color: item.fill === '#fff' ? item.color : item.fill }}>
                    {type === 'color-square' && <span className="square"></span>}
                    {`${item.dataKey} : ${item.value}`}
                  </li>
                )
              })}
          </ul>
        </div>
      )
    }
  }
}

const toolTipWrapper = (customFnc, lineIndex) => {
  return props => {
    let newObj = props
    newObj = { ...newObj, lineIndex }
    return customFnc(newObj)
  }
}

const toolTipComponent = (props, config, lineIndex) => {
  if (props.customToolTip) {
    config.customToolTipFnc = config.activeDot
      ? toolTipWrapper(props.customToolTip, lineIndex)
      : props.customToolTip
  } else if (config.toolTipType !== 'default') {
    config.customToolTipFnc = customTooltipFnc(config.toolTipType)
  }

  return (
    config.tooltip && (
      <Tooltip cursor={false} content={config.customToolTipFnc ? config.customToolTipFnc : null} />
    )
  )
}

customTooltipFnc.propTypes = {
  active: PropTypes.any,
  payload: PropTypes.any,
  label: PropTypes.any,
}

toolTipComponent.propTypes = {
  props: PropTypes.any,
  customToolTip: PropTypes.any,
  config: PropTypes.any,
}

export default toolTipComponent
