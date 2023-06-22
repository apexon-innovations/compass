import React, { Component } from 'react'
import { withRouter } from 'reusable-components/dist/components/withRouter/withRouter'
import { ReactSVG } from 'react-svg'
import PropTypes from 'prop-types'
import { IMG_DASHBOARD_SWITCH } from '../../const/imgConst.js'
import { getMenuLink } from './MenuLinks'

//Style
import style from './DashboardSwitch.module.scss'

class DashboardSwitch extends Component {
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
    // Here we are checking user is not click on selected link
    if (selectedElement !== 1) {
      for (let i = 0; i < menuLink.length; i++) {
        if (menuLink[i].elementPosition === selectedElement) {
          menuLink[i].elementPosition = 1
        } else {
          // If user selected upr link or lower link condition check
          if (selectedElement === 0) {
            menuLink[i].elementPosition =
              menuLink[i].elementPosition + 1 <= menuLink.length - 1 ? menuLink[i].elementPosition + 1 : 0
          } else {
            menuLink[i].elementPosition =
              menuLink[i].elementPosition - 1 < 0 ? menuLink.length - 1 : menuLink[i].elementPosition - 1
          }
        }
      }

      this.setState({
        menuLink: menuLink,
        rotateClass: selectedElement ? 'rotateReverse' : 'rotate',
        currentSelect: selectedElement,
      })

      //Redirect user baded on click
      if (link && this.props.projectId) this.props.navigate(link)

      // Remove animation class after animation completed
      setTimeout(() => {
        this.setState({
          rotateClass: '',
        })
      }, 500)
    }
  }

  componentDidMount() {
    //Some wired behavior we got undefined in string so I am checking here
    if (this.props.projectId && this.props.projectId !== 'undefined') {
      this.setState(
        {
          menuLink: getMenuLink(this.props.projectId),
        },
        () => {
          if (this.props.basePath === 'operational') {
            this.onPageChange(0)
          } else if (this.props.basePath === 'strategic') {
            this.onPageChange(2)
          }
        },
      )
    }
  }

  renderWheelLink(elementPosition, imageSrc, name, link, key) {
    return (
      <div
        key={key}
        className={[style.stripe, style[`stripe${elementPosition}`]].join(' ')}
        onClick={() => {
          this.onPageChange(elementPosition, link)
        }}
      >
        <div>
          <div className={[style.btn, style.btn1].join(' ')}>
            <div className={[style.ic, style.active].join(' ')}>
              <ReactSVG src={imageSrc} />
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
                    {this.renderWheelLink(item.elementPosition, item.imageSrc, item.name, item.link, key)}
                  </React.Fragment>
                )
              })}
          </div>
          <ReactSVG
            className={[style.switchWheel, style[this.state.rotateClass]].join(' ')}
            src={`${IMG_DASHBOARD_SWITCH}`}
          />
        </div>
        <div id="switchNavOverlay" className={[style.overLay].join(' ')} />
      </div>
    )
  }
}

DashboardSwitch.propTypes = {
  projectId: PropTypes.string,
  basePath: PropTypes.string,
  navigate: PropTypes.any,
}

export default withRouter(DashboardSwitch)
