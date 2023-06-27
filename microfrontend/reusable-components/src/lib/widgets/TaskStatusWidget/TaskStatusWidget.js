import React from 'react'
import PropTypes from 'prop-types'
import { Row, Col } from 'react-bootstrap'
import WidgetContainer from '../../components/WidgetContainer/WidgetContainer'
import useWidgetAPIWrapper from '../../components/WidgetContainer/useWidgetAPIWrapper'
import ModernBox from '../../components/CommonComponents/ModernBox/ModernBox'
import style from './TaskStatus.module.scss'

const responseMap = response => {
  return response.data.summary
}

const TaskStatusWidget = React.memo(({ apiEndPointUrl, data, callback }) => {
  const { response, error, isLoading } = useWidgetAPIWrapper({
    apiEndPointUrl,
    data,
    callback,
    responseMapFunction: responseMap,
  })
  return (
    <ModernBox boxTitle={'Status'}>
      <WidgetContainer isLoading={isLoading} data={response} error={error}>
        <div className={style.taskStatus}>
          <Row>
            <Col md={4}>
              <div className={style.ticketStatus}>
                <div className={[style.ticketInfo].join(' ')}>
                  <div className={style.number}>
                    <span className={style.span}>{response.todo}</span>
                    {/* <span className={style.highlight}></span> */}
                  </div>
                  <div className={style.label}>To Dos</div>
                </div>
                <div className={[style.ticketInfo].join(' ')}>
                  <div className={style.number}>
                    <span className={style.span}>{response.inProgress}</span>
                    {/* <span className={style.highlight}></span> */}
                  </div>
                  <div className={style.label}>In-Progress</div>
                </div>
                <div className={[style.ticketInfo, 'mb-0'].join(' ')}>
                  <div className={[style.number, ''].join(' ')}>
                    <span className={style.span}>{response.completed}</span>
                  </div>
                  <div className={style.label}>Completed</div>
                </div>
              </div>
            </Col>
            <Col md={4}>
              <div className={style.stories}>
                <div className={[style.storiesInfo, style.amber].join(' ')}>
                  <div className={style.number}>
                    <span className={style.span}>{response.total}</span>
                    <span className={style.highlight}></span>
                  </div>
                  <div className={style.label}>Stories</div>
                </div>
              </div>
            </Col>
            <Col md={4}>
              <div className={style.ticketAssignment}>
                <div className={[style.ticketInfo].join(' ')}>
                  <div className={style.label}>Assigned</div>
                  <div className={style.number}>
                    <span className={style.span}>{response.assigned}</span>
                    {/* <span className={style.highlight}></span> */}
                  </div>
                </div>
                <div className={[style.ticketInfo, 'mb-0'].join(' ')}>
                  <div className={style.label}>Un-Assigned</div>
                  <div className={style.number}>
                    <span className={style.span}>{response.unAssigned}</span>
                    {/* <span className={style.highlight}></span> */}
                  </div>
                </div>
              </div>
            </Col>
          </Row>
        </div>
      </WidgetContainer>
    </ModernBox>
  )
})

TaskStatusWidget.propTypes = {
  apiEndPointUrl: PropTypes.string,
  data: PropTypes.any,
  callback: PropTypes.func,
}

export default TaskStatusWidget
