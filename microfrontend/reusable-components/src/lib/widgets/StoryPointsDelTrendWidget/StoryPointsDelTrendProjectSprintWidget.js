import React, { useState } from 'react'
import PropTypes from 'prop-types'
import config from './StoryPointsDelTrendProjectWidgetConfig.json'
import WidgetContainer from '../../components/WidgetContainer/WidgetContainer'
import useWidgetAPIWrapper from '../../components/WidgetContainer/useWidgetAPIWrapper'
import BracketBox from '../../components/CommonComponents/BracketBox/BracketBox'
import BackArrow from '../../components/CommonComponents/BackArrow/BackArrow'
import CommonLineChart from '../../components/CommonGraphs/CommonLineChart/CommonLineChart'
import PaginationArrows from '../../components/CommonComponents/PaginationArrows/PaginationArrows'
import CustomLegend from '../../components/CommonGraphSections/CustomLegend/CustomLegend'
import RenderToolTipStoryPoint from './ToolTipStoryPoint.js'
import { mapConfigData, colorCode } from './StoryPointsDelTrendFunction'

const responseMap = (
  response,
  selectedPointCount = 'Points',
  idealLine,
  boxTitle,
  colorCodeIndex = false,
) => {
  if (response.data) {
    const data = []
    const sprintList = []
    const loopData = idealLine ? response.data : response.data.sprintData
    loopData.map((item, index) => {
      const sprintName = boxTitle
        ? boxTitle[index]
        : index === 0
        ? 'Current Sprint'
        : `Prev Sprint ${index}`
      sprintList.push({
        name: sprintName,
        label: sprintName,
        value: item.sprintId,
        stroke: colorCode[colorCodeIndex ? colorCodeIndex : index + 1] || colorCode[0],
        fill: colorCode[colorCodeIndex ? colorCodeIndex : index + 1] || colorCode[0],
        hasBorder: true,
        borderType: 'solid',
        align: 'left',
      })

      item.dayWiseData.map(itemDayWise => {
        let exist = false
        if (itemDayWise) {
          data.map(dataItem => {
            if (dataItem.name === `Day ${itemDayWise.day}`) {
              dataItem[sprintName] = dataItem[sprintName]
                ? dataItem[sprintName] + itemDayWise[`openTillNow${selectedPointCount}`]
                : itemDayWise[`openTillNow${selectedPointCount}`]
              let temp = {
                [sprintName]: itemDayWise[`openTillNow${selectedPointCount}`]
                  ? itemDayWise[`openTillNow${selectedPointCount}`]
                  : 0,
                openTillNow: itemDayWise[`openTillNow${selectedPointCount}`]
                  ? itemDayWise[`openTillNow${selectedPointCount}`]
                  : 0,
                completed: itemDayWise[`completed${selectedPointCount}`]
                  ? itemDayWise[`completed${selectedPointCount}`]
                  : 0,

                reopen: itemDayWise[`reopen${selectedPointCount}`]
                  ? itemDayWise[`reopen${selectedPointCount}`]
                  : 0,

                newlyAdded: itemDayWise[`newlyAdded${selectedPointCount}`]
                  ? itemDayWise[`newlyAdded${selectedPointCount}`]
                  : 0,
              }
              if (idealLine) {
                dataItem['ideal'] = dataItem['ideal']
                  ? dataItem['ideal'] + itemDayWise[`idealCompleted${selectedPointCount}`]
                  : itemDayWise[`idealCompleted${selectedPointCount}`]
              }
              dataItem[`${sprintName}Data`] = temp
              exist = true
            }
            return dataItem
          })

          if (!exist) {
            let temp = {
              name: `Day ${itemDayWise.day}`,
              [sprintName]: itemDayWise[`openTillNow${selectedPointCount}`],
              completed: itemDayWise[`completed${selectedPointCount}`],
              openTillNow: itemDayWise[`openTillNow${selectedPointCount}`],
              reopen: itemDayWise[`reopen${selectedPointCount}`],
              newlyAdded: itemDayWise[`newlyAdded${selectedPointCount}`],
            }
            if (idealLine) {
              temp['ideal'] = itemDayWise[`idealCompleted${selectedPointCount}`]
                ? itemDayWise[`idealCompleted${selectedPointCount}`]
                : 0
            }
            data.push({ ...temp, [`${sprintName}Data`]: temp })
          }
        }
        return itemDayWise
      })
      return item
    })

    if (idealLine) {
      sprintList.push({
        name: 'ideal',
        label: 'ideal',
        value: 'ideal',
        stroke: colorCode[0],
        fill: colorCode[0],
        hasBorder: true,
        borderType: 'solid',
        align: 'right',
        strokeDasharray: '8 8',
      })
    }
    return {
      sprintList,
      data,
      projectName:
        idealLine && response.data.length > 0
          ? response.data[0].projectName
          : response.data.projectName,
    }
  } else {
    return {}
  }
}

const StoryPointsDelTrendProjectSprintWidget = React.memo(
  ({
    apiEndPointUrl,
    data,
    callback,
    selectedPointCount,
    showHideButton,
    idealLine,
    boxTitle,
    backLink,
    colorCodeIndex,
  }) => {
    const { response, error, isLoading } = useWidgetAPIWrapper({ apiEndPointUrl, data, callback })
    let responseData
    if (response) {
      responseData = responseMap(response, selectedPointCount, idealLine, boxTitle, colorCodeIndex)
    }
    const [btnShow1, setBtnShow1] = useState()

    const [displayFrom, setDisplayFrom] = useState(0)
    const numberPerDisplay = 14
    return (
      <BracketBox
        hideShow={btnShow1 ? 'hide' : ''}
        animStatus=""
        boxTitle={boxTitle ? boxTitle[0] : responseData ? responseData.projectName : ''}
        needBracketBox={showHideButton ? true : false}
      >
        <WidgetContainer isLoading={isLoading} error={error} data={responseData}>
          {responseData && responseData.data ? (
            <React.Fragment>
              {showHideButton && (
                <div className="btnHideShow" onClick={() => setBtnShow1(!btnShow1)}>
                  {btnShow1 ? 'Show' : 'Hide'}
                </div>
              )}
              {backLink && (
                <BackArrow
                  title={responseData ? responseData.projectName : ''}
                  redirectURL={'/client/client-delivery-trends'}
                />
              )}
              <div className="toggleThings">
                <div className="clientWidgetInner">
                  {responseData && (
                    <CommonLineChart
                      allData={{
                        config: mapConfigData(config, responseData.sprintList, selectedPointCount),
                        data: [...responseData.data].splice(displayFrom, numberPerDisplay),
                      }}
                      customToolTip={!idealLine ? RenderToolTipStoryPoint : false}
                    />
                  )}
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

StoryPointsDelTrendProjectSprintWidget.propTypes = {
  zoomEnable: PropTypes.bool,
  isFilter: PropTypes.bool,
  apiEndPointUrl: PropTypes.string,
  data: PropTypes.any,
  selectedPointCount: PropTypes.any,
  callback: PropTypes.func,
  showHideButton: PropTypes.any,
  idealLine: PropTypes.any,
  boxTitle: PropTypes.any,
  backLink: PropTypes.any,
  colorCodeIndex: PropTypes.any,
}

export default StoryPointsDelTrendProjectSprintWidget
