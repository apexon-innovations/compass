import React from 'react'
import PropTypes from 'prop-types'

// Style
import style from './SquareBox.module.scss'

const SquareBox = React.memo(props => {
  return (
    <div className={[style.squareBox].join(' ')}>
      <div className={style.boxTitle}>
        <h2>
          <span>{props.boxTitle}</span>
        </h2>
      </div>
      {props.children}
    </div>
  )
})

SquareBox.propTypes = {
  children: PropTypes.any,
  animStatus: PropTypes.string,
  boxTitle: PropTypes.string,
  hideShow: PropTypes.string,
}

// Specifies the default values for props:
SquareBox.defaultProps = {
  boxTitle: 'No Title',
}

export default SquareBox
