import React from 'react'
import PropTypes from 'prop-types'
import { Row, Col } from 'react-bootstrap'
import useWidgetAPIWrapper from '../../components/WidgetContainer/useWidgetAPIWrapper'
import { colorCalculationReverse } from '../../utils/commonFunction'
import WidgetContainer from '../../components/WidgetContainer/WidgetContainer'
import style from './DefectTrendsAcceptanceWidget.module.scss'

const DefectTrendsAcceptanceWidget = React.memo(({ apiEndPointUrl, data, callback }) => {
  const { response, error, isLoading } = useWidgetAPIWrapper({ apiEndPointUrl, data, callback })

  return (
    <div className={style.viewBox}>
      <WidgetContainer isLoading={isLoading} error={error} data={response}>
        <Row>
          {response.data
            ? response.data.map((item, index) => {
                return (
                  <Col lg={3} md={3} sm={4} key={index}>
                    <div
                      className={[
                        style.defectAcceptance,
                        style[colorCalculationReverse(item.ratio).className],
                      ].join(' ')}
                    >
                      <div className={style.titleArea}>
                        <div className={style.title}>{item.projectName}</div>
                        <div className={style.percentage}>{item.ratio}%</div>
                      </div>
                      <div className={style.statistics}>
                        <div className={style.box}>
                          <div className={style.value}>{item.actedUpon}</div>
                          <div className={style.label}>Total Logged</div>
                        </div>
                        <div className={style.box}>
                          <div className={style.value}>{item.accepted}</div>
                          <div className={style.label}>Accepted</div>
                        </div>
                      </div>
                    </div>
                  </Col>
                )
              })
            : ''}
        </Row>
      </WidgetContainer>
    </div>
  )
})

DefectTrendsAcceptanceWidget.propTypes = {
  apiEndPointUrl: PropTypes.string,
  data: PropTypes.any,
  callback: PropTypes.func,
}

export default DefectTrendsAcceptanceWidget
