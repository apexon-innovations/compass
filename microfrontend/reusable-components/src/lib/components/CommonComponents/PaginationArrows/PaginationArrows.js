import React from 'react'
import PropTypes from 'prop-types'
import { IC_ARR_LFT, IC_ARR_RGT } from '../../../const/imgConst'

// Style
import style from './PaginationArrows.module.scss'

const PaginationArrows = React.memo(({ pageNumber, noOfItems, totalItems, setPagingCallback }) => {
  const disablePrevArrow = pageNumber > 0
  const disableNextArrow = totalItems && totalItems.length > pageNumber + noOfItems
  return (
    <div className={style.paginationArrows}>
      <div
        className={[style.arrow, disablePrevArrow ? '' : style.disabled].join(' ')}
        onClick={() => {
          disablePrevArrow && setPagingCallback(pageNumber - noOfItems)
        }}
      >
        <img src={IC_ARR_LFT} title="" alt="" />
      </div>

      <div
        className={[style.arrow, disableNextArrow ? '' : style.disabled].join(' ')}
        onClick={() => {
          disableNextArrow && setPagingCallback(pageNumber + noOfItems)
        }}
      >
        <img src={IC_ARR_RGT} title="" alt="" />
      </div>
    </div>
  )
})

PaginationArrows.propTypes = {
  pageNumber: PropTypes.any,
  noOfItems: PropTypes.any,
  totalItems: PropTypes.any,
  setPagingCallback: PropTypes.any,
}

export default PaginationArrows
