import React from 'react'
import PropTypes from 'prop-types'

// Style
import style from './CodeTechnicalDebtClientWidget.module.scss'

const TooltipTechnicalDebtOverview = props => {
  const { active, payload } = props
  if (active) {
    const data = payload[0].payload
    return (
      <div className={['graphToolTip', style.ttTechnicalDebtOverview].join(' ')}>
        <ul className={style.tooltipHead}>
          <li>{data.name}</li>
          <li>
            Total - <span className={style.totalCount}>{data.totalDays} days</span>
          </li>
        </ul>
        <ul className={style.tooltipBody}>
          {data.maintainability ? (
            <li className={style.mainatainability}>
              Maintainability - <span>{data.maintainability} days</span>
            </li>
          ) : (
            ''
          )}
          {data.reliability ? (
            <li className={style.reliability}>
              Reliability - <span>{data.reliability} days</span>
            </li>
          ) : (
            ''
          )}
          {data.security ? (
            <li className={style.security}>
              Security - <span>{data.security} days</span>
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

TooltipTechnicalDebtOverview.propTypes = {
  active: PropTypes.any,
  payload: PropTypes.any,
}

export default TooltipTechnicalDebtOverview
