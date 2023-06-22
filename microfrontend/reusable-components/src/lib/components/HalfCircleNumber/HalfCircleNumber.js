import React from 'react'
import PropTypes from 'prop-types'
import { ReactSVG } from 'react-svg'
import { IC_BLOCKER_OUTER_RING } from '../../const/imgConst.js'
import style from './HalfCircleNumber.module.scss'

const HalfCircleNumber = React.memo(props => {
  const className = props.className

  return (
    <div className={[style.halfCircleNumber, className].join(' ')}>
      <div className={[style.number, 'mr-0'].join(' ')}>
        <span className={style.span}>{props.value}</span>
        {/* <span className={style.highlight}></span> */}
      </div>
      <div className={style.ring}>
        <ReactSVG src={`${IC_BLOCKER_OUTER_RING}`} />
      </div>
    </div>
  )
})

HalfCircleNumber.propTypes = {
  children: PropTypes.any,
  className: PropTypes.any,
  value: PropTypes.any,
}

// Specifies the default values for props:
HalfCircleNumber.defaultProps = {
  className: '',
  value: '00',
}

export default HalfCircleNumber
