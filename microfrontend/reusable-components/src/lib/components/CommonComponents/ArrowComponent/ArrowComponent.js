import React from 'react'
import PropTypes from 'prop-types'
import { ReactSVG } from 'react-svg'
import { IC_ARR_LFT_BLUE, IC_ARR_RGT_BLUE } from '../../../const/imgConst'

const ArrowComponent = React.memo(({ callback, selectedValue = 0 }) => {
  const options = [
    { name: 'Current Sprint', value: 0 },
    { name: 'Prev Sprint 1', value: 1 },
    { name: 'Prev Sprint 2', value: 2 },
    { name: 'Prev Sprint 3', value: 3 },
    { name: 'Prev Sprint 4', value: 4 },
  ]
  return (
    <div className="graphNavBox">
      {selectedValue < 4 && (
        <div
          className="prev"
          onClick={() => {
            callback(selectedValue + 1)
          }}
        >
          <ReactSVG src={`${IC_ARR_LFT_BLUE}`} />
        </div>
      )}
      <div className="caption">{options[selectedValue] ? options[selectedValue].name : ''}</div>
      {selectedValue >= 1 && (
        <div
          className="prev"
          onClick={() => {
            callback(selectedValue - 1)
          }}
        >
          <ReactSVG src={`${IC_ARR_RGT_BLUE}`} />
        </div>
      )}
    </div>
  )
})

ArrowComponent.propTypes = {
  callback: PropTypes.func,
  selectedValue: PropTypes.oneOfType([PropTypes.string, PropTypes.number]),
}

export default ArrowComponent
