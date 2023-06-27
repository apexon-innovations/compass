import React from 'react'
import PropTypes from 'prop-types'

const CustomLineDot = props => {
  const { cx, cy } = props
  return <circle cx={cx} cy={cy} r={5} fill={props.stroke} />
}

CustomLineDot.propTypes = {
  cx: PropTypes.any,
  cy: PropTypes.any,
  stroke: PropTypes.any,
}

export default CustomLineDot
