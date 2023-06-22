import React from 'react'
import PropTypes from 'prop-types'

import { colorCalculationReverse } from '../../utils/commonFunction'

// Style
import style from './DefectTrendAcceptanceProjectWiseWidget.module.scss'

const TooltipDefectTypeAcceptance = props => {
  const { active, payload, label } = props
  if (active) {
    return (
      <div className={['graphToolTip', style.ttTechnicalDebtOverview].join(' ')}>
        <ul className={style.tooltipHead}>
          <li>{label}</li>
          <li style={{ color: colorCalculationReverse(payload[0].value).color }}>
            {payload[0].value}%
          </li>
        </ul>
        <ul className={style.tooltipBody}>
          <li>Accepted: {payload[0].payload['Accepted']}</li>
          <li>Acted Upon: {payload[0].payload['Acted Upon']}</li>
        </ul>
      </div>
    )
  }
  return null
}

TooltipDefectTypeAcceptance.propTypes = {
  active: PropTypes.any,
  payload: PropTypes.any,
  label: PropTypes.any,
}

export default TooltipDefectTypeAcceptance
