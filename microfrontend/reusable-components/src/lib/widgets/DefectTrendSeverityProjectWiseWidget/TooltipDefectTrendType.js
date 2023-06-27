import React from 'react'
import PropTypes from 'prop-types'
import style from './DefectTrendSeverityProjectWiseWidget.module.scss'

const TooltipTechnicalDebtOverview = props => {
  const { active, payload } = props
  if (active) {
    return (
      <div className={['graphToolTip', style.ttTechnicalDebtOverview].join(' ')}>
        <ul className={style.tooltipHead}>
          {payload[0] && payload[0].payload ? <li>{payload[0].payload.name}</li> : ''}
        </ul>
        <ul className={style.tooltipBody}>
          {payload.map(item => {
            return (
              <li
                key={item.value}
                style={{ color: item.fill === 'transparent' ? item.stroke : item.fill }}
              >
                {item.name.replace('.', ' ')} - <span>{item.value}</span>
              </li>
            )
          })}
        </ul>
      </div>
    )
  }
  return null
}

TooltipTechnicalDebtOverview.propTypes = {
  payload: PropTypes.any,
  active: PropTypes.any,
}

export default TooltipTechnicalDebtOverview
