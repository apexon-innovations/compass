import React, { Component } from 'react'
import { ReactSVG } from 'react-svg'
import PropTypes from 'prop-types'
import { IMG_DASHBOARD_SWITCH_TWO } from 'reusable-components/dist/const/imgConst'
import { getMenuLink } from './MenuLinks'
import style from './DashboardSwitchForTwo.module.scss'

class DashboardSwitchForTwo extends Component {
  constructor(props) {
    super(props)
    this.state = {
      currentSelect: 1,
      rotateClass: '',
      menuLink: [],
    }
  }

  onPageChange(selectedElement, link) {
    const menuLink = [...this.state.menuLink]

    menuLink.map((item, index) => {
      menuLink[index].activeClass = false
      return menuLink[index]
    })
    menuLink[selectedElement].activeClass = true

    this.setState({
      menuLink: menuLink,
    })

    //Redirect user baded on click
    if (link) this.props.navigate(link)
  }

  componentDidMount() {
    this.setState(
      {
        menuLink: getMenuLink(),
      },
      () => {
        if (this.props.basePath === 'business-overview') {
          this.onPageChange(0)
        } else if (this.props.basePath === 'overview') {
          this.onPageChange(1)
        }
      },
    )
  }

  renderWheelLink(elementPosition, activeClass, imageSrc, name, link, key) {
    return (
      <div
        key={key}
        className={[
          style.stripe,
          activeClass ? style.activeClass : '',
          style[`stripe${elementPosition}`],
        ].join(' ')}
        onClick={() => {
          this.onPageChange(elementPosition, link)
        }}
      >
        <div>
          <div className={[style.btn, style.btn1].join(' ')}>
            <div className={[style.ic, activeClass ? style.active : ''].join(' ')}>
              <ReactSVG src={imageSrc.default} />
            </div>
            <div className={style.label}>{name}</div>
          </div>
        </div>
      </div>
    )
  }

  render() {
    return (
      <div className={style.switchDashboard}>
        <div id="switchNav" className={[style.switchNav].join(' ')}>
          <div className={style.switchNavElements}>
            {this.state.menuLink &&
              this.state.menuLink.map((item, key) => {
                return (
                  <React.Fragment key={key}>
                    {this.renderWheelLink(
                      key,
                      item.activeClass,
                      item.imageSrc,
                      item.name,
                      item.link,
                      key,
                    )}
                  </React.Fragment>
                )
              })}
          </div>
          <ReactSVG
            className={[style.switchWheel, style[this.state.rotateClass]].join(' ')}
            src={IMG_DASHBOARD_SWITCH_TWO}
          />
        </div>
        <div id="switchNavOverlay" className={[style.overLay].join(' ')} />
      </div>
    )
  }
}

DashboardSwitchForTwo.propTypes = {
  navigate: PropTypes.any,
  projectId: PropTypes.string,
  basePath: PropTypes.string,
}

export default DashboardSwitchForTwo
