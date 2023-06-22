import React from 'react'
import PropTypes from 'prop-types'

const ProjectCompletionTrendsToolTip = props => {
  const { active, payload, label } = props

  if (active && props.lineIndex) {
    const data = payload[props.lineIndex] ? payload[props.lineIndex] : false
    if (data) {
      const showData = data.payload[`${data.dataKey}Data`]
      return (
        <div className="customToolTip sprintPoint text-center">
          <div className="header">
            <div className="sprintName">{label}</div>
          </div>
          <div className="content">
            <ul>
              <li>
                <span className="label">{showData.label}</span>
                <span className="value">{showData.completed || '0'}</span>
              </li>
              <li>
                <span className="label">Squads</span>
                <span className="value">{showData.squads || '0'}</span>
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

ProjectCompletionTrendsToolTip.propTypes = {
  active: PropTypes.bool,
  payload: PropTypes.any,
  label: PropTypes.any,
  lineIndex: PropTypes.any,
}

export default ProjectCompletionTrendsToolTip
