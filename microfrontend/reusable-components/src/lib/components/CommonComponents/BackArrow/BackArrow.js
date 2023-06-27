import React from 'react'
import PropTypes from 'prop-types'
import { ReactSVG } from 'react-svg'
import { withRouter } from '../../withRouter/withRouter'
import { IC_ARR_LFT_BLUE } from '../../../const/imgConst'

const BackArrow = React.memo(props => {
  return (
    <React.Fragment>
      <div className="graphNavBox">
        <div
          className="prev"
          onClick={() => {
            if (props.onClick) {
              props.onClick()
            } else {
              props.navigate(props.redirectURL)
            }
          }}
        >
          <ReactSVG src={`${IC_ARR_LFT_BLUE}`} alt="IC_ARR_LFT_BLUE" />
        </div>

        <div className="caption">{props.title}</div>
      </div>
    </React.Fragment>
  )
})

BackArrow.propTypes = {
  onClick: PropTypes.func,
  redirectURL: PropTypes.string,
  title: PropTypes.string,
  navigate: PropTypes.any,
}

export default withRouter(BackArrow)
