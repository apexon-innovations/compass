import React from 'react'
import PropTypes from 'prop-types'
import { Row, Col } from 'react-bootstrap'

import useWidgetAPIWrapper from '../../components/WidgetContainer/useWidgetAPIWrapper'
import WidgetContainer from '../../components/WidgetContainer/WidgetContainer'

// Style
import style from './DefectTrendTypeWidget.module.scss'

const DefectTrendTypeWidget = React.memo(({ apiEndPointUrl, data, callback }) => {
  const { response, error, isLoading } = useWidgetAPIWrapper({ apiEndPointUrl, data, callback })

  return (
    <div className={style.viewBox}>
      <WidgetContainer isLoading={isLoading} error={error} data={response}>
        {response ? (
          <React.Fragment>
            <Row>
              {response.data
                ? response.data.map((item, index) => {
                    return (
                      <Col key={index} lg={3} md={3} sm={4}>
                        <div className={style.defectTrend}>
                          <div className={style.titleWrapper}>
                            <h6 className={style.title}>{item.name}</h6>
                            <div className={style.totalDefects}>
                              <span>Total</span>
                              <span className={style.defectsCount}>{item.totalDefects}</span>
                            </div>
                          </div>
                          <div className={style.defectBox}>
                            <ProjectSummaryBox
                              acceptedDefects={item.acceptedDefects}
                              unattendedDefects={item.unattendedDefects}
                              rejectedDefects={item.rejectedDefects}
                              acceptedDefectsStatus={item.acceptedDefectsStatus}
                              unattendedDefectsStatus={item.unattendedDefectsStatus}
                              rejectedDefectsStatus={item.rejectedDefectsStatus}
                            />
                            <div className={style.detailsArea}>
                              <ul>
                                <li className={style.green}>
                                  <div className={style.info}>
                                    <div className={style.dot}></div>
                                    <div className={style.label}>Accepted</div>
                                    <div className={style.value}>{item.acceptedDefects}</div>
                                  </div>
                                  <ul>
                                    <li>
                                      <div className={style.label}>Open</div>
                                      <div className={style.value}>{item.openDefects}</div>
                                    </li>
                                    <li>
                                      <div className={style.label}>Reopened</div>
                                      <div className={style.value}>{item.reopenedDefects}</div>
                                    </li>
                                    <li>
                                      <div className={style.label}>Closed</div>
                                      <div className={style.value}>{item.closedDefects}</div>
                                    </li>
                                  </ul>
                                </li>
                                <li className={style.red}>
                                  <div className={style.info}>
                                    <div className={style.dot}></div>
                                    <div className={style.label}>Unattended</div>
                                    <div className={style.value}>{item.unattendedDefects}</div>
                                  </div>
                                </li>
                                <li className={style.blue}>
                                  <div className={style.info}>
                                    <div className={style.dot}></div>
                                    <div className={style.label}>Rejected</div>
                                    <div className={style.value}>{item.rejectedDefects}</div>
                                  </div>
                                </li>
                              </ul>
                            </div>
                          </div>
                        </div>
                      </Col>
                    )
                  })
                : ''}
            </Row>
            <div className={style.defectStatusLegend}>
              <ul>
                <li className={style.defectGreen}>Accepted</li>
                <li className={style.defectRed}>Unattended</li>
                <li className={style.defectBlue}>Rejected</li>
              </ul>
            </div>
          </React.Fragment>
        ) : (
          ''
        )}
      </WidgetContainer>
    </div>
  )
})

const ProjectSummaryBox = ({
  acceptedDefects,
  unattendedDefects,
  rejectedDefects,
  acceptedDefectsStatus,
  unattendedDefectsStatus,
  rejectedDefectsStatus,
}) => {
  const openDefectArray = sizeClassCalculation({
    accepted: acceptedDefects,
    unattended: unattendedDefects,
    rejected: rejectedDefects,
    acceptedDefectsStatus: acceptedDefectsStatus,
    unattendedDefectsStatus: unattendedDefectsStatus,
    rejectedDefectsStatus: rejectedDefectsStatus,
  })

  return (
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
  acceptedDefects: PropTypes.string,
  unattendedDefects: PropTypes.any,
  rejectedDefects: PropTypes.any,
  acceptedDefectsStatus: PropTypes.any,
  unattendedDefectsStatus: PropTypes.any,
  rejectedDefectsStatus: PropTypes.any,
}

DefectTrendTypeWidget.propTypes = {
  apiEndPointUrl: PropTypes.string,
  data: PropTypes.any,
  callback: PropTypes.func,
}

export default DefectTrendTypeWidget
