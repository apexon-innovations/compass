import React from 'react'
import PropTypes from 'prop-types'

// Style
import style from './DefectAgeingWidget.module.scss'

const TooltipDefectAgeing = props => {
  const { active, payload } = props
  if (active) {
    return (
      <div className={['graphToolTip', style.ttDefectTrend].join(' ')}>
        <div className={style.tooltipWrapper}>
          <h5 className={style.title}>{`${props.label} `} Days</h5>
          <div className={style.projectList}>
            <ul>
              {payload.map((item, index) => {
                return !item.fillOpacity ? (
                  <li className={style.projectA} key={index}>
                    <div className={style.projectName} style={{ color: item.color }}>
                      {item.name}
                    </div>
                    <div className={style.value} style={{ color: item.color }}>
                      {item.value}
                    </div>
                  </li>
                ) : (
                  ''
                )
              })}
            </ul>
          </div>
        </div>
      </div>
    )
  }
  return null
}

TooltipDefectAgeing.propTypes = {
  active: PropTypes.any,
  payload: PropTypes.any,
  label: PropTypes.any,
}

export default TooltipDefectAgeing
