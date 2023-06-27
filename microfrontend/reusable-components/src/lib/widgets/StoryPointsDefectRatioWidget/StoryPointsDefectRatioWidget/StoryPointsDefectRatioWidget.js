import React from 'react'
import PropTypes from 'prop-types'
import { withRouter } from '../../../components/withRouter/withRouter'
import { Row, Col } from 'react-bootstrap'
import WidgetContainer from '../../../components/WidgetContainer/WidgetContainer'
import useWidgetAPIWrapper from '../../../components/WidgetContainer/useWidgetAPIWrapper'
import BracketBox from '../../../components/CommonComponents/BracketBox/BracketBox'
import ZoomBox from '../../../components/CommonComponents/ZoomBox/ZoomBox'
import style from './StoryPointsDefectRatioWidget.module.scss'

const renderDefectBlock = (data, detailsUrl, navigate) => {
  return (
    <Col md={4} sm={4} className="pl-0 col">
      <div
        className={[
          style.box,
          data.sprintData.ratio > 70
            ? style.darkRed
            : data.sprintData.ratio > 50
            ? style.amber
            : style.darkGreen,
        ].join(' ')}
        onClick={() => {
          if (detailsUrl) navigate(`${detailsUrl}/${data.iscProjectId}`)
        }}
      >
        <h3 className={style.projectName}>{data.projectName}</h3>
        <div className={style.progress}>
          <div className={style.percentage}>{data.sprintData.ratio}%</div>
        </div>
        <div className={style.status}>
          <div className={style.data}>
            <div className={style.value}>{data.sprintData.deliveredStories}</div>
            <div className={style.label}>Deliver</div>
          </div>
          <div className={style.data}>
            <div className={style.value}>{data.sprintData.defectOpened}</div>
            <div className={style.label}>Opened</div>
          </div>
        </div>
      </div>
    </Col>
  )
}

const responseMap = response => {
  return response.data
}

const InfoValue = () => {
  return (
    <div className="infoContent">
      <p>
        A consolidated story point defect ratio of multiple projects, which depicts development
        story points and Defects Points with the defect ratio (%) for prev. (5) sprints.{' '}
      </p>
    </div>
  )
}

const StoryPointsDefectRatioWidget = ({
  apiEndPointUrl,
  zoomEnable,
  data,
  callback,
  detailsUrl,
  navigate,
}) => {
  const { response, error, isLoading } = useWidgetAPIWrapper({
    apiEndPointUrl,
    data,
    callback,
    responseMapFunction: responseMap,
  })

  return (
    <div className={style.defectRatio}>
      <BracketBox
        verticalAlignment="alignTop"
        needBracketBox={zoomEnable}
        boxTitle={'Story Point Defect Ratio'}
        infoEnable={true}
        toolTipId="infoSPDR"
        infoValue={<InfoValue />}
      >
        <WidgetContainer isLoading={isLoading} error={error} data={response}>
          {response ? (
            <ZoomBox zoomEnable={zoomEnable} redirectURL={'/client/client-defect-ratio'}>
              <div className={style.defectRatioInner}>
                <div className={zoomEnable ? '' : style.collapsibleBox}>
                  <div className={style.projectBoxes}>
                    <Row>
                      {response
                        .slice(
                          0,
                          zoomEnable
                            ? response.length > 6
                              ? 6
                              : response.length
                            : response.length,
                        )
                        .map((projectData, index) => {
                          return (
                            <React.Fragment key={index}>
                              {renderDefectBlock(projectData, detailsUrl, navigate)}
                            </React.Fragment>
                          )
                        })}
                    </Row>
                    {response.length > 6 ? (
                      <div className="moreBtn">+{response.length - 6}</div>
                    ) : (
                      ''
                    )}
                  </div>
                </div>
              </div>
            </ZoomBox>
          ) : (
            ''
          )}
        </WidgetContainer>
      </BracketBox>
    </div>
  )
}

StoryPointsDefectRatioWidget.propTypes = {
  navigate: PropTypes.any,
  detailsUrl: PropTypes.string,
  apiEndPointUrl: PropTypes.string,
  data: PropTypes.any,
  callback: PropTypes.func,
  zoomEnable: PropTypes.bool,
}

export default withRouter(StoryPointsDefectRatioWidget)
