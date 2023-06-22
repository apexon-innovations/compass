import React from 'react'
import PropTypes from 'prop-types'

import style from './CustomLegendRing.module.scss'
const CustomLegendRing = props => {
  const { payload } = props
  return (
    <ul className={style.custom_legend_circles}>
      {payload &&
        payload.map((entry, index) => (
          <li key={`item-${index}`}>
            <span
              style={{ borderColor: '' + payload[index].color + '' }}
              className={style.legendNewCircle}
            ></span>
            <span style={{ color: '' + payload[index].color + '' }}>
              {payload[index].payload ? payload[index].payload.dataKey : ''}
            </span>
          </li>
        ))}
    </ul>
  )
}

CustomLegendRing.propTypes = {
  payload: PropTypes.any,
}

export default CustomLegendRing
