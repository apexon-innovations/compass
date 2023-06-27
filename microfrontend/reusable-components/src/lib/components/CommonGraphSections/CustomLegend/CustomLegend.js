import React from 'react'
import PropTypes from 'prop-types'
import { withRouter } from '../../withRouter/withRouter'
import style from './CustomLegend.module.scss'

const CustomLegend = React.memo(({ payload, navigate, callback, activeLegendName }) => {
  return (
    <ul className={style.custom_legends}>
      {payload &&
        payload.map((data, index) => {
          let props = {}
          if (data.redirectURL) {
            props.onClick = () => {
              navigate(data.redirectURL, data.redirectData ? data.redirectData : {})
            }
          } else if (callback) {
            props.onClick = () => {
              callback(data)
            }
          }
          return (
            <li
              key={`item-${index}`}
              className={[
                data.align === 'right' ? 'ml-auto' : '',
                data.redirectURL ? style.pointer : '',
                callback ? style.pointer : '',
                activeLegendName && activeLegendName === data.name ? style.active : '',
              ].join(' ')}
              {...props}
            >
              {data.hasBorder ? (
                <div
                  className={[
                    style.icon,
                    data.type === 'circle' ? style.circle : style.square,
                    data.hasBorder ? style.hasBorder : '',
                    data.borderType === 'dashed' ? style.dashed : '',
                  ].join(' ')}
                  style={{ borderColor: `${data.fill}` }}
                />
              ) : (
                <div
                  className={[
                    style.icon,
                    data.type === 'circle' ? style.circle : style.square,
                    data.hasBorder ? style.hasBorder : '',
                    data.borderType === 'dashed' ? style.dashed : '',
                  ].join(' ')}
                  style={{ background: `${data.fill}` }}
                />
              )}
              <div className={style.text} style={{ color: '' + data.fill + '' }}>
                {data.name}
              </div>
            </li>
          )
        })}
    </ul>
  )
})

CustomLegend.propTypes = {
  payload: PropTypes.any,
  navigate: PropTypes.any,
  callback: PropTypes.any,
  activeLegendName: PropTypes.any,
}

export default withRouter(CustomLegend)
