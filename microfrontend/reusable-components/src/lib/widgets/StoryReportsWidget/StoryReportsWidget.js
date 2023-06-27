import React from 'react'
import { Col } from 'react-bootstrap'
import PropTypes from 'prop-types'
import WidgetContainer from '../../components/WidgetContainer/WidgetContainer'
import useWidgetAPIWrapper from '../../components/WidgetContainer/useWidgetAPIWrapper'
import BracketBox from '../../components/CommonComponents/BracketBox/BracketBox'
import HalfCircleNumber from '../../components/HalfCircleNumber/HalfCircleNumber'
import OverlapGraphs from '../../components/OverlapGraphs/OverlapGraphs'
import style from './StoryReports.module.scss'

const responseMapFunction = response => {
  return response ? response.data : response
}

const StoryReportsWidget = ({ apiEndPointUrl, data, callback }) => {
  const { response, error, isLoading } = useWidgetAPIWrapper({
    apiEndPointUrl,
    data,
    callback,
    responseMapFunction,
  })

  return (
    <BracketBox boxTitle={'Story Report'}>
      <WidgetContainer isLoading={isLoading} error={error} data={response}>
        {response && Object.keys(response).length > 0 && (
          <div className={style.storyReports}>
            <Col md={3} className={[style.cols, 'pl-0 border-right'].join(' ')}>
              <div className={style.storyCompleted}>
                <HalfCircleNumber value={response.summary.completed} />
                <div className={style.completedLabel}>Completed</div>
              </div>
            </Col>
            <Col md={6} className={[style.cols, 'border-right'].join(' ')}>
              <div className={style.plannedVsCompleted}>
                <OverlapGraphs
                  graphData={[
                    {
                      value: response.summary.planned,
                      total: response.summary.planned,
                    },
                    {
                      value: response.summary.delivered,
                      total: response.summary.planned,
                      strokeWidth: 3,
                    },
                  ]}
                />
                <div className="triColoredTitle">
                  <span className="amber">Planned</span>
                  <span>vs</span>
                  <span className="green">Delivered</span>
                </div>
              </div>
            </Col>
            <Col md={3} className={[style.cols, 'pr-0'].join(' ')}>
              <div className={[style.storyFailure, style.red].join(' ')}>
                <div className={style.number}>
                  <span className={style.span}>{response.summary.blocker}</span>
                  <div className={style.highlight}></div>
                </div>
                <div className={style.label}>Blockers</div>
              </div>
            </Col>
          </div>
        )}
      </WidgetContainer>
    </BracketBox>
  )
}
StoryReportsWidget.propTypes = {
  apiEndPointUrl: PropTypes.string,
  data: PropTypes.any,
  callback: PropTypes.func,
}
export default StoryReportsWidget
