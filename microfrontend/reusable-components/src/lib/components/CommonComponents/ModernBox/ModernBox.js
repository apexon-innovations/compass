import React from 'react'
import PropTypes from 'prop-types'

// Style
import style from './ModernBox.module.scss'

const ModernBox = React.memo(props => {
  return (
    <div className={[style.modernBox, 'animated fadeInUp', props.animStatus].join(' ')}>
      <div className={style.modernBoxContent}>
        <div className={[style.boxTitle, style[props.boxTitleColor]].join(' ')}>
          <h2>
            <span>{props.boxTitle}</span>
          </h2>
        </div>
        {props.children}
      </div>
      <div className={style.decoration}>
        <div className={[style.corner, style.TL].join(' ')}></div>
        <div className={[style.corner, style.TR].join(' ')}></div>
        <div className={[style.corner, style.BL].join(' ')}></div>
        <div className={[style.corner, style.BR].join(' ')}></div>
      </div>
    </div>
  )
})

ModernBox.propTypes = {
  children: PropTypes.any,
  animStatus: PropTypes.string,
  boxTitle: PropTypes.string,
  boxTitleColor: PropTypes.string,
}

// Specifies the default values for props:
ModernBox.defaultProps = {
  animStatus: '',
  boxTitle: 'No Title',
  boxTitleColor: 'blue',
}

export default ModernBox
