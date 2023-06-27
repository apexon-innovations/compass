import React from 'react'
import PropTypes from 'prop-types'

const ToolTipStoryPoint = props => {
  const { active, payload, label } = props
  if (active && props.lineIndex) {
    const data = payload[props.lineIndex] ? payload[props.lineIndex] : false
    if (data) {
      const headerTitle = data.dataKey
      const showData = data.payload[`${data.dataKey}Data`]
      return (
        <div className="customToolTip sprintPoint text-center">
          <div className="header">
            <div className="projectName">{headerTitle}</div>
            <div className="sprintName">{label}</div>
          </div>
          <div className="content">
            <ul>
              <li>
                <span className="label">Open till now </span>
                <span className="value">{showData.openTillNow || '0'}</span>
              </li>
              <li>
                <span className="label">Completed</span>
                <span className="value">{showData.completed || '0'}</span>
              </li>
              <li>
                <span className="label">Reopened</span>
                <span className="value">{showData.reopen || '0'}</span>
              </li>
              <li>
                <span className="label">Newly Added</span>
                <span className="value">{showData.newlyAdded || '0'}</span>
              </li>
            </ul>
          </div>
        </div>
      )
    }
    return null
  }
  return null
}

ToolTipStoryPoint.propTypes = {
  active: PropTypes.bool,
  payload: PropTypes.any,
  label: PropTypes.any,
  lineIndex: PropTypes.any,
}

export default ToolTipStoryPoint
