import React, { Component } from 'react'
import { ReactSVG } from 'react-svg'
import LOGO_COMPASS_NEW from '../../images/LogoCompass.svg'
import { ReactComponent as LogoText } from '../../images/logotext.svg'
import './LogoAnimation.scss'

class LogoAnimation extends Component {
  afterTimeLineComplete() {
    var topDistanceLogo = document.getElementById('landingLogoAnimation')
    topDistanceLogo.classList.add('topDistance')

    setTimeout(() => {
      let animationTextClass = document.getElementsByClassName('animationText')
      if (animationTextClass && animationTextClass[0]) {
        animationTextClass[0].classList.add('loadText')
      }
      document.getElementById('logoTextColor').style.fill = '#FFF'
    }, 250)
  }

  fillColor() {
    setTimeout(() => {
      var element = document.getElementsByTagName('stop')
      var path = document.getElementsByTagName('path')
      var i

      for (i = 0; i < element.length; i++) {
        element[i].classList.add('op1')
      }

      for (i = 0; i < path.length; i++) {
        path[i].classList.add('stock0')
      }
      const animationLogoEle = document.getElementById('animationLogo')
      if (typeof animationLogoEle != 'undefined' && animationLogoEle != null) {
        animationLogoEle.classList.remove('animationLogoBegin')
        animationLogoEle.classList.add('animationLogoCompleted')
      }
      this.afterTimeLineComplete()
    }, 1500)
  }

  render() {
    return (
      <div id="landingLogoAnimation" className="landing_logo_animation">
        <div id="animationLogoWrapper">
          <ReactSVG src={LOGO_COMPASS_NEW} callback={this.fillColor()} />
          <LogoText />
        </div>
      </div>
    )
  }
}

export default LogoAnimation
