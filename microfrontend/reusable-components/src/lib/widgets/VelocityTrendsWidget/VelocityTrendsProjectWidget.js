import React, { useState } from 'react'
import PropTypes from 'prop-types'
import config from './VelocityTrendProjectWidgetConfig.json'
import colorCode from '../../const/colorCode'
import WidgetContainer from '../../components/WidgetContainer/WidgetContainer'
import useWidgetAPIWrapper from '../../components/WidgetContainer/useWidgetAPIWrapper'
import BracketBox from '../../components/CommonComponents/BracketBox/BracketBox'
import CommonLineChart from '../../components/CommonGraphs/CommonLineChart/CommonLineChart'
import PaginationArrows from '../../components/CommonComponents/PaginationArrows/PaginationArrows'
import CustomLegend from '../../components/CommonGraphSections/CustomLegend/CustomLegend'

const graphLegend = selectedPointCount => [
  {
    name: `Completed ${selectedPointCount.displayName}`,
    value: `Completed ${selectedPointCount.displayName}`,
    stroke: colorCode[0],
    fill: colorCode[0],
    type: 'circle',
    hasBorder: false,
    borderType: 'solid',
    align: 'left',
  },
  {
    name: `Committed ${selectedPointCount.displayName}`,
    value: `Committed ${selectedPointCount.displayName}`,
    stroke: colorCode[1],
    fill: colorCode[1],
    type: 'circle',
    borderType: 'solid',
    hasBorder: false,
  },
]

const graphConfig = (config, selectedPointCount, graphLegend) => {
  config.y.label =
    selectedPointCount.displayName === 'Points'
      ? `Velocity (In Story ${selectedPointCount.displayName})`
      : `Velocity (In ${selectedPointCount.displayName})`
  config.line[0].key = `Completed ${selectedPointCount.displayName}`
  config.line[0].stroke = graphLegend[0].stroke
  config.line[0].fill = graphLegend[0].fill
  config.line[1].key = `Committed ${selectedPointCount.displayName}`
  config.line[1].stroke = graphLegend[1].stroke
  config.line[1].fill = graphLegend[1].fill
  return config
}

const responseMap = response => {
  const graphData = []
  const tempResponse = response.data
  if (tempResponse[0].sprints) {
    for (let j = 0; j < tempResponse[0].sprints.length; j++) {
      const sprintData = {}
      sprintData['Completed Points'] = parseInt(tempResponse[0].sprints[j].completedPoints)
      sprintData['Committed Points'] = parseInt(tempResponse[0].sprints[j].committedPoints)
      sprintData['Committed Count'] = parseInt(tempResponse[0].sprints[j].committedCount)
      sprintData['Completed Count'] = parseInt(tempResponse[0].sprints[j].completedCount)
      sprintData['name'] = `Prev sprint ${tempResponse[0].sprints.length - j}`
      graphData.push(sprintData)
    }
  }
  return { data: tempResponse, graphData }
}

const VelocityTrendsProjectWidget = React.memo(
  ({ apiEndPointUrl, data, callback, selectedPointCount }) => {
    const { response, error, isLoading } = useWidgetAPIWrapper({
      apiEndPointUrl,
      data,
      callback,
      responseMapFunction: responseMap,
    })
    const [btnShow1, setBtnShow1] = useState()

    const [displayFrom, setDisplayFrom] = useState(0)
    const numberPerDisplay = 10

    return (
      <BracketBox
        hideShow={btnShow1 ? 'hide' : ''}
        animStatus=""
        boxTitle={response ? response.data[0].projectName : ''}
      >
        <WidgetContainer isLoading={isLoading} error={error} data={response}>
          {response ? (
            <React.Fragment>
              <div className="btnHideShow" onClick={() => setBtnShow1(!btnShow1)}>
                {btnShow1 ? 'Show' : 'Hide'}
              </div>
              <div className="toggleThings">
                <div className="clientWidgetInner">
                  <CommonLineChart
                    allData={{
                      config: graphConfig(
                        config,
                        selectedPointCount,
                        graphLegend(selectedPointCount),
                      ),
                      data: [...response.graphData].splice(displayFrom, numberPerDisplay),
                    }}
                  />
                  <CustomLegend payload={graphLegend(selectedPointCount)} />
                  <PaginationArrows
                    pageNumber={displayFrom}
                    noOfItems={numberPerDisplay}
                    totalItems={response.graphData}
                    setPagingCallback={setDisplayFrom}
                  />
                </div>
              </div>
            </React.Fragment>
          ) : (
            ''
          )}
        </WidgetContainer>
      </BracketBox>
    )
  },
)

VelocityTrendsProjectWidget.propTypes = {
  zoomEnable: PropTypes.bool,
  isFilter: PropTypes.bool,
  apiEndPointUrl: PropTypes.string,
  data: PropTypes.any,
  selectedPointCount: PropTypes.any,
  callback: PropTypes.func,
}

export default VelocityTrendsProjectWidget
