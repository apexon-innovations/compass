import React from 'react'
import PropTypes from 'prop-types'
import { ReactSVG } from 'react-svg'
import { Tooltip as ReactTooltip } from 'react-tooltip'
import { IC_INFO } from '../../../const/imgConst'

// Style
import style from './BracketBox.module.scss'

const BracketBox = React.memo(props => {
  if (props.needBracketBox) {
    return (
      <div
        className={[
          style.bracketBox,
          'animated fadeInUp',
          style[props.hideShow],
          props.animStatus,
          style[props.theme],
        ].join(' ')}
      >
        <div className={[style.bracketBoxContent, style[props.verticalAlignment]].join(' ')}>
          <div className={[style.boxTitle, style[props.boxTitleColor]].join(' ')}>
            <h2>
              <span>{props.boxTitle}</span>
              {props.infoEnable && props.toolTipId && (
                <React.Fragment>
                  <div
                    className="info"
                    data-tooltip-id={props.toolTipId}
                    data-tooltip-place="bottom"
                  >
                    <ReactSVG src={`${IC_INFO}`} />
                  </div>
                  <ReactTooltip
                    className="simpleTooltip"
                    id={props.toolTipId}
                    border={true}
                    borderColor="#35445f"
                    arrowColor="#040a23"
                  >
                    {props.infoValue}
                  </ReactTooltip>
                </React.Fragment>
              )}
            </h2>
          </div>
          {props.children}
        </div>
        <div className={style.decoration}>
          <div className={[style.corner, style.lhs].join(' ')} />
          <div className={[style.corner, style.rhs].join(' ')} />
        </div>
      </div>
    )
  } else {
    return <React.Fragment>{props.children}</React.Fragment>
  }
})

BracketBox.propTypes = {
  children: PropTypes.any,
  animStatus: PropTypes.string,
  theme: PropTypes.string,
  boxTitle: PropTypes.string,
  boxTitleColor: PropTypes.string,
  verticalAlignment: PropTypes.string,
  hideShow: PropTypes.string,
  infoEnable: PropTypes.bool,
  infoValue: PropTypes.any,
  toolTipId: PropTypes.string,
  needBracketBox: PropTypes.bool,
}

// Specifies the default values for props:
BracketBox.defaultProps = {
  animStatus: '',
  boxTitle: 'No Title',
  boxTitleColor: '',
  verticalAlignment: '',
  theme: '',
  hideShow: '',
  needBracketBox: true,
  infoEnable: false,
  infoValue: '',
  toolTipId: '',
}

export default BracketBox
