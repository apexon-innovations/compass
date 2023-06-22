import React from 'react'
import PropTypes from 'prop-types'
import { Col, Row } from 'react-bootstrap'
import style from './CollaborationMetricsWidget.module.scss'
import WidgetContainer from '../../components/WidgetContainer/WidgetContainer'
import useWidgetAPIWrapper from '../../components/WidgetContainer/useWidgetAPIWrapper'

const responseMap = response => {
  return response && response.data ? response.data : false
}

const CollaborationMetricsWidget = ({ apiEndPointUrl, data, callback }) => {
  const { response, error, isLoading } = useWidgetAPIWrapper({
    apiEndPointUrl,
    data,
    callback,
    responseMapFunction: responseMap,
  })
  return (
    <div className={style.collaborationMetrics}>
      <WidgetContainer isLoading={isLoading} error={error} data={response}>
        {response && (
          <Row>
            <Col>
              <AverageBox
                titleFirst={'Average Time To'}
                titleSecond={'Resolve PR'}
                value={response.average.prResolveTime.split(' ')[0]}
                label={response.average.prResolveTime.split(' ')[1]}
                status={'amber'}
              />
            </Col>
            <Col>
              <AverageBox
                titleFirst={'Average Time To'}
                titleSecond={'First Comment'}
                value={Number(response.average.firstComment.split(' ')[0]).toFixed(2)}
                label={response.average.firstComment.split(' ')[1]}
                status={'amber'}
              />
            </Col>
            <Col>
              <AverageBox
                titleFirst={'Average Number Of'}
                titleSecond={'Follow Up Commits'}
                value={response.average.recommit}
              />
            </Col>
          </Row>
        )}
      </WidgetContainer>
    </div>
  )
}

const AverageBox = ({ status = '', titleFirst, titleSecond, label, value }) => {
  return (
    <div className={[style.box, status ? style[status] : ''].join(' ')}>
      <div className={style.colabBoxTitle}>
        <div className={style.firstLine}>{titleFirst}</div>
        <div className={style.secondLine}>{titleSecond}</div>
      </div>
      <div className={style.roundBox}>
        <div className={style.number}>{value}</div>
        {label && <div className={style.value}>{label}</div>}
      </div>
    </div>
  )
}

AverageBox.propTypes = {
  status: PropTypes.any,
  titleFirst: PropTypes.string,
  titleSecond: PropTypes.string,
  label: PropTypes.string,
  value: PropTypes.any,
}

CollaborationMetricsWidget.propTypes = {
  apiEndPointUrl: PropTypes.string,
  data: PropTypes.any,
  callback: PropTypes.func,
}

export default CollaborationMetricsWidget
