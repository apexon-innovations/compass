import React from 'react'
import PropTypes from 'prop-types'
import style from './CustomLegendRing.module.scss'

const CustomLegendSquare = props => {
  const { payload } = props
  return (
    <ul className={style.custom_legend_square}>
      {payload &&
        payload.map(
          (entry, index) =>
            payload[index].payload.dataKey.includes('sprint1.') && (
              <li key={`item-${index}`}>
                <span
                  style={{ background: '' + payload[index].color + '' }}
                  className={style.legendNewSquare}
                ></span>
                {payload[index].payload
                  ? payload[index].payload.dataKey.replace('sprint1.', '')
                  : ''}
              </li>
            ),
        )}
    </ul>
  )
}

CustomLegendSquare.propTypes = {
  payload: PropTypes.any,
}

export default CustomLegendSquare
