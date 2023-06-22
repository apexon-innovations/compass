import React, { PureComponent } from 'react'
import PropTypes from 'prop-types'
import { IC_CLOSE } from '../../const/imgConst.js'
import style from './ModalContainer.module.scss'

class ModalContainer extends PureComponent {
  constructor(props) {
    super(props)
    this.state = {
      show: this.props.isShow,
      popUpDta: {},
    }
  }

  escFunction = event => {
    if (this.state.show) {
      if (event.keyCode === 27) {
        this.handleModelToggle()
      }
    }
  }

  handleModelToggle = data => {
    this.setState({
      show: !this.state.show,
      popUpDta: data,
    })
  }

  componentDidMount() {
    document.addEventListener('keydown', this.escFunction, false)
  }

  componentWillUnmount() {
    document.removeEventListener('keydown', this.escFunction, false)
  }

  render() {
    const children = React.Children.map(this.props.children, child => {
      return React.cloneElement(child, {
        popUpData: this.state.popUpDta,
      })
    })
    return (
      <div
        className={[style.popUpMain, this.state.show ? style.showPopup : style.hidePopup].join(' ')}
      >
        {this.state.show && (
          <div className={[style.modalBox, style[this.props.size]].join(' ')}>
            <div className={style.close} onClick={this.handleModelToggle}>
              <img src={IC_CLOSE} title="" alt="" />
            </div>
            <div className={style.modalBoxInner}>{children}</div>
            <div className={[style.decoration, style.active].join(' ')}>
              <div className={[style.corner, style.TL].join(' ')}></div>
              <div className={[style.corner, style.TR].join(' ')}></div>
              <div className={[style.corner, style.BL].join(' ')}></div>
              <div className={[style.corner, style.BR].join(' ')}></div>
            </div>
          </div>
        )}
        <div className={style.modalOverlay} onClick={this.handleModelToggle}></div>
      </div>
    )
  }
}
ModalContainer.propTypes = {
  children: PropTypes.object,
  size: PropTypes.string,
  isShow: PropTypes.bool,
}

// Specifies the default values for props:
ModalContainer.defaultProps = {
  size: 'large',
  isShow: false,
}

export default ModalContainer
