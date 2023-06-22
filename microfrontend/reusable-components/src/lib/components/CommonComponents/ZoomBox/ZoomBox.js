import React from 'react'
import PropTypes from 'prop-types'
import { withRouter } from '../../withRouter/withRouter'
import { IC_EXPAND_VIEW } from '../../../const/imgConst'

const ZoomBox = React.memo(props => {
  if (props.zoomEnable) {
    return (
      <React.Fragment>
        <div className="upRightControls">
          <div
            className="popButton"
            onClick={() => {
              if (props.onClick) {
                props.onClick()
              } else {
                props.navigate(props.redirectURL)
              }
            }}
          >
            <img src={IC_EXPAND_VIEW} alt="popup Button" />
          </div>
        </div>
        {props.children}
      </React.Fragment>
    )
  } else {
    return <React.Fragment>{props.children}</React.Fragment>
  }
})

ZoomBox.propTypes = {
  children: PropTypes.any,
  onClick: PropTypes.func,
  zoomEnable: PropTypes.bool,
  redirectURL: PropTypes.string,
  navigate: PropTypes.any,
}

export default withRouter(ZoomBox)
