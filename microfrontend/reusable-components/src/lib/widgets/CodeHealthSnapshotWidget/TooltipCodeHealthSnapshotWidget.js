import React from 'react'
import PropTypes from 'prop-types'

// Style
import style from './CodeHealthSnapshotWidget.module.scss'

const TooltipCodeHealthSnapshotWidget = props => {
  const { active, payload } = props
  if (active && payload && payload[0]) {
    const data = payload[0].payload
    return (
      <div className={['graphToolTip', style.ttCodeHealthSnapshot].join(' ')}>
        <div className={style.tooltipHead}>
          <div>
            <span className={style.sprintNumber}>{data.name}</span>
          </div>
        </div>
        <ul className={style.tooltipBody}>
          {data.totalLineOfCode || data.totalLineOfCode > -1 ? (
            <li className={style.info1}>
              <span>Line of Code</span>
              {data.totalLineOfCode}
            </li>
          ) : (
            ''
          )}
          {data.codeChurn || data.codeChurn > -1 ? (
            <li className={style.info4}>
              <span>Churn</span>
              {data.codeChurn}
            </li>
          ) : (
            ''
          )}
          {data.legacyRefactor || data.legacyRefactor > -1 ? (
            <li className={style.info3}>
              <span>Lagacy Refector</span>
              {data.legacyRefactor}
            </li>
          ) : (
            ''
          )}
          {data.compliance || data.compliance > -1 ? (
            <li className={style.info5}>
              <span>Compliance</span>
              {data.compliance}
            </li>
          ) : (
            ''
          )}
          {data.totalViolations || data.totalViolations > -1 ? (
            <li className={style.info2}>
              <span>Violations</span>
              {data.totalViolations}
            </li>
          ) : (
            ''
          )}
          {data.technicalDebt || data.technicalDebt > -1 ? (
            <li className={style.info6}>
              <span>Technical Debt</span>
              {data.technicalDebt}
            </li>
          ) : (
            ''
          )}
        </ul>
      </div>
    )
  }
  return null
}
TooltipCodeHealthSnapshotWidget.propTypes = {
  active: PropTypes.any,
  payload: PropTypes.any,
}

export default TooltipCodeHealthSnapshotWidget
