import React, { useState } from 'react'
import PropTypes from 'prop-types'
import { withRouter } from '../../../components/withRouter/withRouter'
import config from './StoryPointsDefectRatioGraphConfig.json'
import WidgetContainer from '../../../components/WidgetContainer/WidgetContainer'
import useWidgetAPIWrapper from '../../../components/WidgetContainer/useWidgetAPIWrapper'
import BracketBox from '../../../components/CommonComponents/BracketBox/BracketBox'
import PaginationArrows from '../../../components/CommonComponents/PaginationArrows/PaginationArrows'
import CustomLegend from '../../../components/CommonGraphSections/CustomLegend/CustomLegend'
import CommonAreaChart from '../../../components/CommonGraphs/CommonAreaChart/CommonAreaChart'
import style from './StoryPointsDefectRatioGraphWidget.module.scss'

const responseMap = response => {
  const graphData = []
  const tempResponse = response.data

  for (let i = 0; i < tempResponse.sprintData.length; i++) {
    graphData.push({
      name: `Prev sprint ${i + 1}`,
      'Defect Ratio': parseInt(tempResponse.sprintData[i].ratio),
      'Defect Opened': parseInt(tempResponse.sprintData[i].defectOpened),
      'Delivered Stories': parseInt(tempResponse.sprintData[i].deliveredStories),
      Average: tempResponse.averageRatio,
    })
  }

  return { data: tempResponse, graphData }
}

const mapConfig = (config, data) => {
  config.referenceLine[0].y = data.averageRatio
  return config
}

const renderToolTipStoryPoint = props => {
  const { active, payload } = props
  if (active) {
    const data = payload[payload.length - 1] ? payload[payload.length - 1].payload : {}
    return (
      <div className="customToolTip sprintPoint text-center">
        <div className="content flex-column">
          <ul>
            <li>{`${data.name}`}</li>
            <li>{`Defect Ratio: ${data['Defect Ratio']}%`}</li>
            <li>{`Average: ${data['Average']}%`}</li>
          </ul>
        </div>
      </div>
    )
  }
  return null
}

const StoryPointsDefectRatioGraphWidget = ({
  apiEndPointUrl,
  showHideEnable,
  data,
  callback,
  title,
  needBracketBox,
}) => {
  const { response, error, isLoading } = useWidgetAPIWrapper({
    apiEndPointUrl,
    data,
    callback,
    responseMapFunction: responseMap,
  })

  const [btnShow, setBtnShow] = useState()
  const [displayFrom, setDisplayFrom] = useState(0)
  const numberPerDisplay = 10

  return (
    <div className={style.defectRatio}>
      <BracketBox
        hideShow={btnShow ? 'hide' : ''}
        needBracketBox={needBracketBox}
        boxTitle={title || 'Story Point Defect Ratio'}
      >
        <WidgetContainer isLoading={isLoading} error={error} data={response}>
          {response ? (
            <React.Fragment>
              {showHideEnable ? (
                <React.Fragment>
                  <div className="btnHideShow" onClick={() => setBtnShow(!btnShow)}>
                    {btnShow ? 'Show' : 'Hide'}
                  </div>
                  {/* <div className={style.totalStoryPoint}>
                    <div className={style.label}>Total story points</div>
                    <div className={style.number}>
                      <span>140</span>
                    </div>
                  </div> */}
                </React.Fragment>
              ) : (
                <div className="controlRow">
                  <div className="graphNavBox">
                    <div className="caption">{response.data.projectName}</div>
                  </div>
                  {/* <div className={style.totalStoryPoint}>
                    <div className={style.label}>Total story points</div>
                    <div className={style.number}>
                      <span>140</span>
                    </div>
                  </div> */}
                </div>
              )}
              <div className="toggleThings">
                <div className="clientWidgetInner">
                  <CommonAreaChart
                    allData={{
                      config: mapConfig(config, response.data),
                      data: showHideEnable
                        ? [...response.graphData].splice(displayFrom, numberPerDisplay)
                        : response.graphData,
                    }}
                    customToolTip={renderToolTipStoryPoint}
                  />
                  <CustomLegend
                    payload={[
                      {
                        name: 'Defect Ratio',
                        label: 'Defect Ratio',
                        value: 'Defect Ratio',
                        stroke: '#e1cc50',
                        fill: '#e1cc50',
                        type: 'circle',
                        hasBorder: false,
                        borderType: 'solid',
                        align: 'left',
                      },
                      {
                        name: 'Average',
                        label: 'Average',
                        value: 'Average',
                        stroke: '#73ff9e',
                        fill: '#73ff9e',
                        type: 'circle',
                        hasBorder: true,
                        borderType: 'dashed',
                        align: 'left',
                      },
                    ]}
                  />
                  {showHideEnable ? (
                    <PaginationArrows
                      pageNumber={displayFrom}
                      noOfItems={numberPerDisplay}
                      totalItems={response.graphData}
                      setPagingCallback={setDisplayFrom}
                    />
                  ) : (
                    ''
                  )}
                </div>
              </div>
            </React.Fragment>
          ) : (
            ''
          )}
        </WidgetContainer>
      </BracketBox>
    </div>
  )
}

StoryPointsDefectRatioGraphWidget.propTypes = {
  detailsUrl: PropTypes.string,
  apiEndPointUrl: PropTypes.string,
  showHideEnable: PropTypes.bool,
  data: PropTypes.any,
  callback: PropTypes.func,
  title: PropTypes.string,
  needBracketBox: PropTypes.bool,
}

export default withRouter(StoryPointsDefectRatioGraphWidget)
