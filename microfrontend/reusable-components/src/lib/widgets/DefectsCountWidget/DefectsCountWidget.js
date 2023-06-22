import React from 'react'
import { Row, Col } from 'react-bootstrap'
import PropTypes from 'prop-types'
import style from './DefectsCountWidget.module.scss'
import WidgetContainer from '../../components/WidgetContainer/WidgetContainer'
import useWidgetAPIWrapper from '../../components/WidgetContainer/useWidgetAPIWrapper'

const responseMap = response => {
  return response.data.bugs
}

const GetDefectsView = ({ name, value, highlight = false }) => {
  return (
    <Col>
      <div className={[style.ticketInfo, 'flex-column'].join(' ')}>
        <div className={[style.number, 'mr-0'].join(' ')}>
          <span className={style.span}>{value}</span>
          {highlight && <span className={style.highlight}></span>}
        </div>
        <div className={style.label}>{name}</div>
      </div>
    </Col>
  )
}

const DefectsCountWidget = React.memo(({ apiEndPointUrl, data, callback }) => {
  const { response, error, isLoading } = useWidgetAPIWrapper({
    apiEndPointUrl,
    data,
    callback,
    responseMapFunction: responseMap,
  })
  return (
    <Col md={7} className={[style.defectsOuter, 'border-right'].join(' ')}>
      <WidgetContainer isLoading={isLoading} data={response} error={error}>
        <div className={style.defects}>
          <div className={[style.defectsInfo, style.red].join(' ')}>
            <div className={style.label}>Total Count</div>
            <div className={style.number}>
              <span className={style.span}>{response.total}</span>
            </div>
          </div>
        </div>
        <Row className="mb-4 w-100">
          <GetDefectsView name={'Pending'} value={response.pending} />
          <GetDefectsView name={'In-Progress'} value={response.inProgress} />
          <GetDefectsView name={'Completed'} highlight={false} value={response.completed} />
        </Row>
      </WidgetContainer>
    </Col>
  )
})

DefectsCountWidget.propTypes = {
  apiEndPointUrl: PropTypes.string,
  data: PropTypes.any,
  callback: PropTypes.func,
}

GetDefectsView.propTypes = {
  name: PropTypes.string,
  value: PropTypes.any,
  highlight: PropTypes.bool,
}

export default DefectsCountWidget
