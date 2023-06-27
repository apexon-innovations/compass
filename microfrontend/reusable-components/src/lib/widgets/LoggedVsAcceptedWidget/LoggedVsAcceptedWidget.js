import React from 'react'
import PropTypes from 'prop-types'
import { Col } from 'react-bootstrap'
import OverlapGraphs from '../../components/OverlapGraphs/OverlapGraphs'
import WidgetContainer from '../../components/WidgetContainer/WidgetContainer'
import useWidgetAPIWrapper from '../../components/WidgetContainer/useWidgetAPIWrapper'

const responseMap = response => {
  return response.data ? response.data.bugs : {}
}

const LoggedVsAcceptedWidget = React.memo(({ apiEndPointUrl, data, callback }) => {
  const { response, error, isLoading } = useWidgetAPIWrapper({
    apiEndPointUrl,
    data,
    callback,
    responseMapFunction: responseMap,
  })

  return (
    <Col md={5} className="d-flex flex-column justify-content-center">
      <WidgetContainer isLoading={isLoading} data={response} error={error}>
        <div className="triColoredTitle">
          <span className="amber">Logged</span>
          <span>vs</span>
          <span className="green">Accepted</span>
        </div>
        <OverlapGraphs
          graphData={[
            { value: response.logged, total: response.logged },
            { value: response.accepted, total: response.logged, strokeWidth: 3 },
          ]}
        />
      </WidgetContainer>
    </Col>
  )
})

LoggedVsAcceptedWidget.propTypes = {
  apiEndPointUrl: PropTypes.string,
  data: PropTypes.any,
  callback: PropTypes.func,
}

export default LoggedVsAcceptedWidget
