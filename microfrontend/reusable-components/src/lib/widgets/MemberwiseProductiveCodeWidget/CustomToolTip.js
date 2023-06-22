/* eslint-disable */
import React from 'react'

const ToolTipView = props => {
  const { active, payload } = props
  if (active) {
    return (
      <div className="customToolTip">
        <ul className="reviewList">
          <li style={{ color: '#ffffff' }}>Name :{payload[0].payload['name']}</li>
          <li style={{ color: '#979797' }}>
            {`Legacy Refactor : ${payload[0].payload['Legacy Refactor']}`}
          </li>
          <li style={{ color: '#24ff00' }}>{`Code Churn : ${payload[0].payload['Code Churn']}`}</li>
          <li style={{ color: '#1a91eb' }}>
            {`Added Line Of Code In ${payload[0].payload['displayTimeDuration']} : ${payload[0].payload['Added Lines Of Code']}`}
          </li>
          <li style={{ color: '#ffc145' }}>
            {`Removed Line Of Code In ${payload[0].payload['displayTimeDuration']} : ${payload[0].payload['Removed Lines Of Code']}`}
          </li>
          <li style={{ color: '#3974ea' }}>
            {`Added Line Of Code : ${payload[0].payload['Added Line Of Code Till Date']}`}
          </li>
          <li style={{ color: '#f8ff73' }}>
            {`Removed Line Of Code : ${payload[0].payload['Removed Line Of Code Till Date']}`}
          </li>
        </ul>
      </div>
    )
  }
  return null
}

export default ToolTipView
