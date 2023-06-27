import React from 'react'
import { Row, Col } from 'react-bootstrap'
import PropTypes from 'prop-types'
import { withRouter } from '../../components/withRouter/withRouter'
import WidgetContainer from '../../components/WidgetContainer/WidgetContainer'
import useWidgetAPIWrapper from '../../components/WidgetContainer/useWidgetAPIWrapper'
import BracketBox from '../../components/CommonComponents/BracketBox/BracketBox'
import ZoomBox from '../../components/CommonComponents/ZoomBox/ZoomBox'
import style from './DefectTrendsWidget.module.scss'

const InfoValue = () => {
  return (
    <div className="infoContent">
      <p>
        A consolidated defect snapshot, which depicts the product defect status by type and severity
        for last (6) months.
      </p>
    </div>
  )
}

const DefectTrendsWidget = React.memo(
  ({
    apiEndPointUrl,
    data,
    callback,
    zoomEnable = true,
    limit = true,
    navigate,
    bracketEnable,
  }) => {
    const { response, error, isLoading } = useWidgetAPIWrapper({ apiEndPointUrl, data, callback })
    return (
      <div className={style.defectSummary}>
        <BracketBox
          boxTitle={'Open Defect Trends'}
          needBracketBox={bracketEnable ? true : false}
          verticalAlignment="alignTop"
          infoEnable={true}
          toolTipId="infoDS"
          infoValue={<InfoValue />}
        >
          <WidgetContainer isLoading={isLoading} error={error} data={response}>
            {response ? (
              <React.Fragment>
                {zoomEnable ? (
                  <ZoomBox zoomEnable={zoomEnable} redirectURL={'/client/defect-trends-details'} />
                ) : (
                  ''
                )}
                <div className={style.defectSummaryInner}>
                  <Row>
                    {response.data.map((item, index) => {
                      return (limit && index <= 3) || !limit ? (
                        <Col lg={limit ? 6 : 3} md={limit ? 6 : 3} key={index}>
                          <ProjectSummaryBox data={item} />
                        </Col>
                      ) : (
                        ''
                      )
                    })}
                  </Row>
                  <div className={style.defectStatusLegend}>
                    <ul>
                      <li className={style.defectGreen}>Accepted</li>
                      <li className={style.defectRed}>Unattended</li>
                      <li className={style.defectBlue}>Rejected</li>
                    </ul>
                    {response.data.length > 4 && limit ? (
                      <div
                        className="moreBtn noHover"
                        onClick={() => {
                          navigate('/client/defect-trends-details')
                        }}
                      >
                        +{response.data.length - 4}
                      </div>
                    ) : (
                      ''
                    )}
                  </div>
                </div>
              </React.Fragment>
            ) : (
              <WidgetContainer error={{ errorCode: 404 }} />
            )}
          </WidgetContainer>
        </BracketBox>
      </div>
    )
  },
)

const ProjectSummaryBox = ({ data }) => {
  const openDefectArray = sizeClassCalculation({
    accepted: data.acceptedDefects,
    unattended: data.unattendedDefects,
    rejected: data.rejectedDefects,
    acceptedDefectsStatus: data.acceptedDefectsStatus,
    unattendedDefectsStatus: data.unattendedDefectsStatus,
    rejectedDefectsStatus: data.rejectedDefectsStatus,
  })
  return (
    <div className={style.defectTrend}>
      <div className={style.titleWrapper}>
        <h6 className={style.title}>{data.name}</h6>
        <div className={style.totalDefects}>
          <span>Total</span>
          <span className={style.defectsCount}>{data.totalDefects}</span>
        </div>
      </div>
      <div className={style.defectBox}>
        <div className={style.graphArea}>
          <div className={style.graphAreaInner}>
            {openDefectArray.map((item, index) => {
              return (
                <div
                  key={index}
                  className={[style.circle, style[item.size], style[item.color]].join(' ')}
                >
                  <div className={style.number}>
                    <span
                      className={[
                        style.arrow,
                        item.status === 'high' ? style.up : '',
                        item.status === 'low' ? style.down : '',
                      ].join(' ')}
                    ></span>
                    {item.value}
                  </div>
                </div>
              )
            })}
          </div>
        </div>

        <div className={style.detailsArea}>
          <ul>
            <li className={style.green}>
              <div className={style.info}>
                <div className={style.dot}></div>
                <div className={style.label}>Accepted</div>
                <div className={style.value}>{data.acceptedDefects}</div>
              </div>
              <ul>
                <li>
                  <div className={style.label}>Open</div>
                  <div className={style.value}>{data.openDefects}</div>
                </li>
                <li>
                  <div className={style.label}>Reopened</div>
                  <div className={style.value}>{data.reopenedDefects}</div>
                </li>
                <li>
                  <div className={style.label}>Closed</div>
                  <div className={style.value}>{data.closedDefects}</div>
                </li>
              </ul>
            </li>
            <li className={style.red}>
              <div className={style.info}>
                <div className={style.dot}></div>
                <div className={style.label}>Unattended</div>
                <div className={style.value}>{data.unattendedDefects}</div>
              </div>
            </li>
            <li className={style.blue}>
              <div className={style.info}>
                <div className={style.dot}></div>
                <div className={style.label}>Rejected</div>
                <div className={style.value}>{data.rejectedDefects}</div>
              </div>
            </li>
          </ul>
        </div>
      </div>
    </div>
  )
}

const sizeClassCalculation = ({
  accepted,
  unattended,
  rejected,
  acceptedDefectsStatus,
  unattendedDefectsStatus,
  rejectedDefectsStatus,
}) => {
  let tempArray = [accepted, unattended, rejected]
  tempArray.sort(function(a, b) {
    return a - b
  })
  let data = []
  let sizedClasses = [
    { size: 'small', added: true },
    { size: 'medium', added: true },
    { size: 'large', added: true },
  ]
  tempArray.map((item, index) => {
    if (item === accepted && !data[0] && sizedClasses[index].added) {
      data[0] = {
        color: 'green',
        value: item,
        ...sizedClasses[index],
        status: acceptedDefectsStatus,
      }
      sizedClasses[index].added = false
    }
    if (item === unattended && !data[1] && sizedClasses[index].added) {
      data[1] = {
        color: 'red',
        value: item,
        ...sizedClasses[index],
        status: unattendedDefectsStatus,
      }
      sizedClasses[index].added = false
    }
    if (item === rejected && !data[2] && sizedClasses[index].added) {
      data[2] = {
        color: 'blue',
        value: item,
        ...sizedClasses[index],
        status: rejectedDefectsStatus,
      }
      sizedClasses[index].added = false
    }
    return item
  })
  return data
}

ProjectSummaryBox.propTypes = {
  data: PropTypes.any,
}

DefectTrendsWidget.propTypes = {
  navigate: PropTypes.any,
  apiEndPointUrl: PropTypes.string,
  data: PropTypes.any,
  callback: PropTypes.func,
  zoomEnable: PropTypes.bool,
  bracketEnable: PropTypes.bool,
  limit: PropTypes.bool,
}

export default withRouter(DefectTrendsWidget)
