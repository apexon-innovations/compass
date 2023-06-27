import React from 'react'
import { Row, Col } from 'react-bootstrap'

const NoDataAvailable = React.memo(() => {
  return (
    <div className="chartArea">
      <Row>
        <Col>
          <div className="noDataGraphText">No data available...!</div>
        </Col>
      </Row>
    </div>
  )
})

export default NoDataAvailable
