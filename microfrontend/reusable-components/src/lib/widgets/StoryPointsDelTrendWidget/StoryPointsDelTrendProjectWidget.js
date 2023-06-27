import React, { useState } from 'react'
import PropTypes from 'prop-types'
import config from './StoryPointsDelTrendProjectWidgetConfig.json'
import colorCode from '../../const/colorCode'
import WidgetContainer from '../../components/WidgetContainer/WidgetContainer'
import useWidgetAPIWrapper from '../../components/WidgetContainer/useWidgetAPIWrapper'
import BracketBox from '../../components/CommonComponents/BracketBox/BracketBox'
import CommonLineChart from '../../components/CommonGraphs/CommonLineChart/CommonLineChart'
import PaginationArrows from '../../components/CommonComponents/PaginationArrows/PaginationArrows'
import CustomLegend from '../../components/CommonGraphSections/CustomLegend/CustomLegend'
import { mapConfigData } from './StoryPointsDelTrendFunction'

const responseMap = (response, selectedPointCount = 'Points') => {
  if (response.data) {
    const data = []
    const sprintList = []
    response.data.sprintData.map((item, index) => {
      const sprintName = index === 0 ? 'Current Sprint' : `Prev Sprint ${index}`
      sprintList.push({
        name: sprintName,
        label: sprintName,
        value: item.sprintId,
        stroke: colorCode[index] || colorCode[0],
        fill: colorCode[index] || colorCode[0],
        type: 'circle',
        hasBorder: false,
        borderType: 'solid',
        align: 'left',
      })

      item.dayWiseData.map(itemDayWise => {
        let exist = false
        data.map(dataItem => {
          if (dataItem.name === `Day ${itemDayWise.day}`) {
            dataItem[sprintName] = dataItem[sprintName]
              ? dataItem[sprintName] + itemDayWise[`openTillNow${selectedPointCount}`]
              : itemDayWise[`openTillNow${selectedPointCount}`]
            exist = true
          }
          return dataItem
        })

        if (!exist) {
          data.push({
            name: `Day ${itemDayWise.day}`,
            [sprintName]: itemDayWise[`openTillNow${selectedPointCount}`],
          })
        }
        return itemDayWise
      })
      return item
    })
    return { sprintList, data, projectName: response.data.projectName }
  }
}

const StoryPointsDelTrendProjectWidget = React.memo(
  ({ apiEndPointUrl, data, callback, selectedPointCount }) => {
    const { response, error, isLoading } = useWidgetAPIWrapper({ apiEndPointUrl, data, callback })
    let responseData
    if (response) {
      responseData = responseMap(response, selectedPointCount)
    }
    const [btnShow1, setBtnShow1] = useState()

    const [displayFrom, setDisplayFrom] = useState(0)
    const numberPerDisplay = 14
    return (
      <BracketBox
        hideShow={btnShow1 ? 'hide' : ''}
        animStatus=""
        boxTitle={responseData ? responseData.projectName : ''}
      >
        <WidgetContainer isLoading={isLoading} error={error} data={responseData}>
          {responseData && responseData.data ? (
            <React.Fragment>
              <div className="btnHideShow" onClick={() => setBtnShow1(!btnShow1)}>
                {btnShow1 ? 'Show' : 'Hide'}
              </div>
              <div className="toggleThings">
                <div className="clientWidgetInner">
                  <CommonLineChart
                    allData={{
                      config: mapConfigData(config, responseData.sprintList, selectedPointCount),
                      data: [...responseData.data].splice(displayFrom, numberPerDisplay),
                    }}
                  />
                  <CustomLegend payload={responseData.sprintList} />
                  <PaginationArrows
                    pageNumber={displayFrom}
                    noOfItems={numberPerDisplay}
                    totalItems={responseData.data}
                    setPagingCallback={setDisplayFrom}
                  />
                </div>
              </div>
            </React.Fragment>
          ) : (
            <WidgetContainer error={{ errorCode: 404 }} />
          )}
        </WidgetContainer>
      </BracketBox>
    )
  },
)

StoryPointsDelTrendProjectWidget.propTypes = {
  zoomEnable: PropTypes.bool,
  isFilter: PropTypes.bool,
  apiEndPointUrl: PropTypes.string,
  data: PropTypes.any,
  selectedPointCount: PropTypes.any,
  callback: PropTypes.func,
}

export default StoryPointsDelTrendProjectWidget
