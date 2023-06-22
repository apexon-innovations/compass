import React from 'react'
import PropTypes from 'prop-types'

import { Row, Col } from 'react-bootstrap'
import CustomLegend from '../../components/CommonGraphSections/CustomLegend/CustomLegend'
import useWidgetAPIWrapper from '../../components/WidgetContainer/useWidgetAPIWrapper'
import WidgetContainer from '../../components/WidgetContainer/WidgetContainer'

import ConsolidateWidgetLegends from './ConsolidateWidgetLegends'
import style from './DefectTrendSeverityWidget.module.scss'

const DefectTrendSeverityWidget = React.memo(({ apiEndPointUrl, data, callback }) => {
  const { response, error, isLoading } = useWidgetAPIWrapper({ apiEndPointUrl, data, callback })

  return (
    <React.Fragment>
      <div className={style.viewBox}>
        <WidgetContainer isLoading={isLoading} error={error} data={response}>
          <Row>
            {response.data
              ? response.data.map((item, index) => {
                  return (
                    <Col lg={6} md={6} sm={12} key={index}>
                      <div className={style.defectProject}>
                        <div className={style.titleWrapper}>
                          <h6 className={style.title}>{item.name}</h6>
                          <div className={style.totalDefects}>
                            <span>Total</span>
                            <span className={style.defectsCount}>{item.total}</span>
                          </div>
                        </div>
                        <div className={style.defectWrapper}>
                          <div className={[style.singleDefectSummary, style.openDefects].join(' ')}>
                            <div className={style.defectSummarytitle}>
                              <span className={style.defectsSummaryLabel}>Open</span>
                              <span className={style.defectSummaryCount}>
                                {item.planned.totalPlanned}
                              </span>
                            </div>
                            <DefectCircleBox
                              critical={item.planned.openCritical}
                              minor={item.planned.openMinor}
                              blocker={item.planned.openBlocker}
                              major={item.planned.openMajor}
                            />
                          </div>
                          <div
                            className={[style.singleDefectSummary, style.closeDefects].join(' ')}
                          >
                            <div className={style.defectSummarytitle}>
                              <span className={style.defectsSummaryLabel}>Closed</span>
                              <span className={style.defectSummaryCount}>
                                {item.closed.totalClosed}
                              </span>
                            </div>
                            <DefectCircleBox
                              critical={item.closed.closeCritical}
                              minor={item.closed.closeMinor}
                              blocker={item.closed.closeBlocker}
                              major={item.closed.closeMajor}
                            />
                          </div>
                          <div
                            className={[style.singleDefectSummary, style.backlogDefects].join(' ')}
                          >
                            <div className={style.defectSummarytitle}>
                              <span className={style.defectsSummaryLabel}>Backlog</span>
                              <span className={style.defectSummaryCount}>
                                {item.backlog.totalBacklog}
                              </span>
                            </div>
                            <DefectCircleBox
                              critical={item.backlog.unattendedCritical}
                              minor={item.backlog.unattendedMinor}
                              blocker={item.backlog.unattendedBlocker}
                              major={item.backlog.unattendedMajor}
                            />
                          </div>
                        </div>
                      </div>
                    </Col>
                  )
                })
              : ''}
          </Row>
          <Row className="ml-0 mr-0">
            <Col lg={12} md={12} sm={12} className="pl-0 pr-0">
              <CustomLegend payload={ConsolidateWidgetLegends} />
            </Col>
          </Row>
        </WidgetContainer>
      </div>
    </React.Fragment>
  )
})

const DefectCircleBox = ({ critical, major, minor, blocker }) => {
  const openDefectArray = sizeClassCalculation({ critical, major, minor, blocker })
  return (
    <div className={style.defectChart}>
      {openDefectArray.map((item, index) => {
        return (
          <div key={index} className={[style[item.size], style[item.color]].join(' ')}>
            {item.value}
          </div>
        )
      })}
    </div>
  )
}

const sizeClassCalculation = ({ critical, blocker, major, minor }) => {
  let tempArray = [critical, blocker, major, minor]
  tempArray.sort(function(a, b) {
    return a - b
  })
  let data = []
  let sizedClasses = [
    { size: 'defectSM', added: true },
    { size: 'defectMD', added: true },
    { size: 'defectLG', added: true },
    { size: 'defectXL', added: true },
  ]
  tempArray.map((item, index) => {
    if (item === critical && !data[0] && sizedClasses[index].added) {
      data[0] = { color: 'criticalDefect', value: item, ...sizedClasses[index] }
      sizedClasses[index].added = false
    }
    if (item === blocker && !data[1] && sizedClasses[index].added) {
      data[1] = { color: 'blockerDefect', value: item, ...sizedClasses[index] }
      sizedClasses[index].added = false
    }
    if (item === major && !data[2] && sizedClasses[index].added) {
      data[2] = { color: 'majorDefect', value: item, ...sizedClasses[index] }
      sizedClasses[index].added = false
    }
    if (item === minor && !data[3] && sizedClasses[index].added) {
      data[3] = { color: 'minorDefect', value: item, ...sizedClasses[index] }
      sizedClasses[index].added = false
    }
    return item
  })
  return data
}

DefectCircleBox.propTypes = {
  critical: PropTypes.string,
  major: PropTypes.any,
  minor: PropTypes.func,
  blocker: PropTypes.func,
}

DefectTrendSeverityWidget.propTypes = {
  apiEndPointUrl: PropTypes.string,
  data: PropTypes.any,
  callback: PropTypes.func,
}

export default DefectTrendSeverityWidget
