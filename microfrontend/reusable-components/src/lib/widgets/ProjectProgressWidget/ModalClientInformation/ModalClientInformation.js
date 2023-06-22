import React, { Component } from 'react'
import dayjs from 'dayjs'
import utc from 'dayjs/plugin/utc'
import PropTypes from 'prop-types'
import { Scrollbars } from 'react-custom-scrollbars'
import { Row, Col } from 'react-bootstrap'
import { NotificationManager } from 'react-notifications'
import MembersSlider from '../MembersSlider/MembersSlider'
import { getColorBasedOnCode } from '../../../utils/commonFunction'
import { putApiCall } from '../../../utils/apiCall'
import { USER_DUMMY, IC_EDIT } from '../../../const/imgConst'
import Slider from 'react-slick'
import { Tooltip as ReactTooltip } from 'react-tooltip'
import Loader from '../../../components/Loader/Loader'
import FileUploader from '../../../components/FileUploader/FileUploader'
import style from './ModalClientInformation.module.scss'
import 'slick-carousel/slick/slick.css'

dayjs.extend(utc)

class ModalClientInformation extends Component {
  constructor(props) {
    super(props)
    this.state = {
      isLoading: false,
    }
    this.inputFileRef = React.createRef()
  }

  getSettings(ownerLength, name) {
    return {
      infinite:
        name === 'Delivery Manager'
          ? ownerLength > 1
            ? true
            : false
          : ownerLength > 2
          ? true
          : false,
      speed: 800,
      focusOnSelect: false,
      dots: false,
      slidesToShow: name === 'Delivery Manager' ? 1 : 2,
      slidesToScroll: 1,
    }
  }

  renderMainInfo(name, value) {
    return (
      <div className={style.box}>
        <div className={style.label}>{name}</div>
        <div className={style.value}>{value}</div>
      </div>
    )
  }

  renderMangerDetails(name, data) {
    if (data) {
      const slides = []
      const toolTip = []
      for (let i = 0; i < data.length; i++) {
        slides.push(
          <div className={style.ownersBox} key={i} data-tip data-tooltip-id={`${name}-${i}`}>
            <div className={[style.userImg]}>
              <img src={data[i].dp ? data[i].dp : USER_DUMMY} title="" alt="" />
            </div>
            <div className={style.designation}>{name}</div>
            <div className={style.name}>{data[i].name}</div>
          </div>,
        )
        if (data[i].nestProjects) {
          toolTip.push(
            <ReactTooltip
              className="customThemeToolTip"
              key={`${name}-${i}`}
              id={`${name}-${i}`}
              border={true}
              delayHide={300}
              delayUpdate={300}
              borderColor="rgb(124, 180, 235)"
              arrowColor="rgba(2, 7, 27, 1)"
            >
              <h3>Managing Projects</h3>
              <ul>
                <Scrollbars autoHeight autoHeightMax={`115px`}>
                  {data[i].nestProjects.map((item, key) => {
                    return (
                      <React.Fragment key={`nest-${key}`}>
                        <li>{item}</li>
                      </React.Fragment>
                    )
                  })}
                </Scrollbars>
              </ul>
            </ReactTooltip>,
          )
        }
      }
      return (
        <React.Fragment>
          <div className={style.ownerSlider}>
            <div
              className={[style.sliderWrap, name === 'Delivery Manager' ? style.dm : ''].join(' ')}
            >
              <Slider {...this.getSettings(data.length, name)}>{slides}</Slider>
              {toolTip}
            </div>
          </div>
        </React.Fragment>
      )
    } else {
      return null
    }
  }

  onFileUpload(data) {
    const requestData = {
      fileName: data.name,
      file: data.base64,
    }
    this.setState({
      isLoading: true,
    })

    putApiCall(this.props.fileUploaderUrl, requestData)
      .then(responseData => {
        if (responseData.success) {
          NotificationManager.success('Project Logo Uploaded Successfully', '', 5000)
          window.dispatchEvent(new CustomEvent('updateProjectIcon', { detail: new Date() }), 1000)
        } else {
          NotificationManager.error(responseData.data.message, '', 5000)
        }
        this.setState({ isLoading: false })
      })
      .catch(() => {
        this.setState({ isLoading: false })
        NotificationManager.error('Something went wrong Please try after sometime', '', 5000)
      })
  }

  render() {
    const { data } = this.props
    const { isLoading } = this.state
    return (
      <div className={style.clientsInfo}>
        <Row>
          <Col md={5} sm={5} className="border-right pl-0">
            <Row>
              <Col md={5} sm={5} className="pl-0">
                <div
                  className={[
                    style.companyInfo,
                    style[
                      getColorBasedOnCode(data.healthMetrics ? data.healthMetrics.overall : 'NA')
                        .color
                    ],
                  ].join(' ')}
                >
                  <div className={[style.icon, data.iconLocation ? style.whiteBg : ''].join(' ')}>
                    {isLoading ? (
                      <Loader />
                    ) : (
                      <React.Fragment>
                        {data.iconLocation ? (
                          <img src={data.iconLocation} title={data.name} alt={data.name} />
                        ) : (
                          <h6>{data.initials}</h6>
                        )}
                        <div
                          className={style.editBox}
                          onClick={() => {
                            this.inputFileRef.current.fileInput.current.click()
                          }}
                        >
                          <img src={IC_EDIT} title="" alt="" />
                        </div>
                      </React.Fragment>
                    )}

                    <FileUploader
                      inputClassName="hide"
                      ref={this.inputFileRef}
                      onCompleted={e => {
                        this.onFileUpload(e)
                      }}
                      onError={e => {
                        NotificationManager.error(e.msg, '', 5000)
                      }}
                    />
                  </div>
                  <div className={style.ring}></div>
                </div>
              </Col>
              <Col md={7} sm={7} className="pl-0">
                <div className={style.companyDetails}>
                  {this.renderMainInfo('Client', data.clientName)}
                  {this.renderMainInfo('Project', data.name)}
                  {this.renderMainInfo('Billing Type', data.billingType)}
                  {this.renderMainInfo(
                    'Start Date',
                    dayjs(data.startDate)
                      .utc()
                      .format('MM-DD-YYYY'),
                  )}
                  {this.renderMainInfo(
                    'Completion Date',
                    dayjs(data.endDate)
                      .utc()
                      .format('MM-DD-YYYY'),
                  )}
                </div>
              </Col>
            </Row>
          </Col>
          <Col md={7} sm={7}>
            <div className={style.ownersInfo}>
              {this.renderMangerDetails('Delivery Manager', data.deliveryManager)}
              {this.renderMangerDetails('Project Manager', data.projectManager)}
            </div>
            <div className={style.membersInfo}>
              <Row>
                <Col md={3}>
                  <div className={style.membersCount}>
                    <div className={style.numberBox}>
                      <div className={style.number}>{data.resources.actual}</div>
                    </div>
                    <div className={style.label}>Members</div>
                  </div>
                </Col>
                <Col md={9}>
                  <div className={style.membersSlider}>
                    <MembersSlider data={data.teamMembers} />
                  </div>
                </Col>
              </Row>
            </div>
          </Col>
        </Row>
      </div>
    )
  }
}

ModalClientInformation.propTypes = {
  data: PropTypes.object,
  fileUploaderUrl: PropTypes.string,
}

export default ModalClientInformation
