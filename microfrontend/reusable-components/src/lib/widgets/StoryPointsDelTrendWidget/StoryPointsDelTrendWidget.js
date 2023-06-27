import React, { useState } from 'react'
import PropTypes from 'prop-types'
import { withRouter } from '../../components/withRouter/withRouter'
import MultiSelect from '@khanacademy/react-multi-select'
import WidgetContainer from '../../components/WidgetContainer/WidgetContainer'
import useWidgetAPIWrapper from '../../components/WidgetContainer/useWidgetAPIWrapper'
import BracketBox from '../../components/CommonComponents/BracketBox/BracketBox'
import ArrowComponent from '../../components/CommonComponents/ArrowComponent/ArrowComponent'
import CommonLineChart from '../../components/CommonGraphs/CommonLineChart/CommonLineChart'
import PaginationArrows from '../../components/CommonComponents/PaginationArrows/PaginationArrows'
import CustomLegend from '../../components/CommonGraphSections/CustomLegend/CustomLegend'
import ZoomBox from '../../components/CommonComponents/ZoomBox/ZoomBox'
import config from './StoryPointsDelTrendWidgetConfig'
import colorCode from '../../const/colorCode'
import { sortByNameFunction } from '../../utils/sortingFunction'
import RenderToolTipStoryPoint from './ToolTipStoryPoint.js'
import { mapConfigData, convertDropDownValue } from './StoryPointsDelTrendFunction'
import { getStoredData, getColorCodeFromProjectName } from '../../utils/projectDataStoreFunction'
import style from './StoryPointsDelTrendWidget.module.scss'

const responseMap = (response, selectedPointCount = 'Points', projectDetailsLinkUrl, viewType) => {
  if (response.data) {
    const data = []
    const projectList = []
    response.data.map((item, index) => {
      projectList.push({
        name: item.projectName,
        label: item.projectName,
        value: item.iscProjectId,
        stroke: getColorCodeFromProjectName(item.projectName) || colorCode[index],
        fill: getColorCodeFromProjectName(item.projectName) || colorCode[index],
        type: 'circle',
        hasBorder: false,
        borderType: 'solid',
        align: 'left',
        redirectURL: projectDetailsLinkUrl ? `${projectDetailsLinkUrl}${item.iscProjectId}` : '',
        redirectData: { viewType },
      })
      item.dayWiseData.map(itemDayWise => {
        let exist = false
        data.map(dataItem => {
          if (dataItem.name === `Day ${itemDayWise.day}`) {
            dataItem[item.projectName] = itemDayWise[`openTillNow${selectedPointCount}`]
            dataItem['completed'] = itemDayWise[`completed${selectedPointCount}`]
            dataItem['openTillNow'] = itemDayWise[`openTillNow${selectedPointCount}`]
            dataItem['reopen'] = itemDayWise[`reopen${selectedPointCount}`]
            dataItem['newlyAdded'] = itemDayWise[`newlyAdded${selectedPointCount}`]
            dataItem[`${item.projectName}Data`] = dataItem
            exist = true
          }
          return dataItem
        })

        if (!exist) {
          let temp = {
            name: `Day ${itemDayWise.day}`,
            [item.projectName]: itemDayWise[`openTillNow${selectedPointCount}`],
            completed: itemDayWise[`completed${selectedPointCount}`],
            openTillNow: itemDayWise[`openTillNow${selectedPointCount}`],
            reopen: itemDayWise[`reopen${selectedPointCount}`],
            newlyAdded: itemDayWise[`newlyAdded${selectedPointCount}`],
          }
          data.push({ ...temp, [`${item.projectName}Data`]: temp })
        }
        return itemDayWise
      })
      return item
    })
    return { projectList: sortByNameFunction(projectList, 'name'), data }
  } else {
    return []
  }
}

const InfoValue = () => {
  return (
    <div className="infoContent">
      <p>Story points delivery trend of multiple projects for current sprint. </p>
    </div>
  )
}

const StoryPointsDelTrendWidget = React.memo(
  ({
    apiEndPointUrl,
    data,
    callback,
    zoomEnable,
    isFilter,
    selectedPointCount,
    projectDetailsLinkUrl,
    viewType,
  }) => {
    const projectData = convertDropDownValue(getStoredData('PROJECT_CLIENT_DATA'))
    const defaultSelectedSprint = 0
    const [widgetParamter, setWidgetParamter] = useState({
      apiEndPointUrlOfWidget: `${apiEndPointUrl}?iscProjectIds=all&sprintNumber=${defaultSelectedSprint}`,
      selectedOption: [],
      selectedSprint: defaultSelectedSprint,
    })

    const { response, error, isLoading } = useWidgetAPIWrapper({
      apiEndPointUrl: widgetParamter.apiEndPointUrlOfWidget,
      data,
      callback,
    })

    const [displayFrom, setDisplayFrom] = useState(0)
    const numberPerDisplay = 14

    const callbackSetSelectedSprint = selectedSprintValue => {
      setWidgetParamter({
        ...widgetParamter,
        apiEndPointUrlOfWidget: `${apiEndPointUrl}?iscProjectIds=${
          widgetParamter.selectedOption && widgetParamter.selectedOption.length > 0
            ? widgetParamter.selectedOption.toString()
            : 'all'
        }&sprintNumber=${selectedSprintValue}`,
        selectedSprint: selectedSprintValue,
      })
      setDisplayFrom(0)
    }

    let responseData
    if (response) {
      responseData = responseMap(response, selectedPointCount, projectDetailsLinkUrl, viewType)
    }

    return (
      <div className={style.storyPointsDelTrend}>
        <BracketBox
          boxTitle={'Story Point Delivery Trends'}
          needBracketBox={zoomEnable ? true : false}
          infoEnable={true}
          toolTipId="infoSPDT"
          infoValue={<InfoValue />}
        >
          <WidgetContainer isLoading={isLoading} error={error} data={responseData}>
            {responseData && responseData.projectList ? (
              <React.Fragment>
                <div className={style.storyPointsDelTrendInner}>
                  {zoomEnable ? (
                    <ZoomBox
                      zoomEnable={zoomEnable}
                      redirectURL={'/client/client-delivery-trends'}
                    />
                  ) : (
                    ''
                  )}
                  {isFilter ? (
                    <div className="controlRow">
                      <ArrowComponent
                        selectedValue={widgetParamter.selectedSprint}
                        callback={callbackSetSelectedSprint}
                      />
                      <div className={['transparentMultiSelect', style.zeroPadding].join(' ')}>
                        <MultiSelect
                          options={projectData}
                          selected={widgetParamter.selectedOption}
                          onSelectedChanged={selected => {
                            setWidgetParamter({
                              ...widgetParamter,
                              apiEndPointUrlOfWidget: `${apiEndPointUrl}?iscProjectIds=${
                                selected && selected.length > 0 ? selected.toString() : 'all'
                              }&sprintNumber=${widgetParamter.selectedSprint}`,
                              selectedOption: selected,
                            })
                          }}
                          valueRenderer={selected => {
                            return selected.length === 0 ? 'Select Project' : `(${selected.length})`
                          }}
                          overrideStrings={{
                            selectSomeItems: 'Select Project',
                            allItemsAreSelected: 'All Project are Selected',
                            selectAll: 'Select All Project',
                            search: 'Search Project',
                          }}
                        />
                      </div>
                    </div>
                  ) : (
                    ''
                  )}
                  <CommonLineChart
                    allData={{
                      config: mapConfigData(config, responseData.projectList, selectedPointCount),
                      data: [...responseData.data].splice(displayFrom, numberPerDisplay),
                    }}
                    customToolTip={RenderToolTipStoryPoint}
                  />
                  <CustomLegend payload={[...responseData.projectList]} />
                  <PaginationArrows
                    pageNumber={displayFrom}
                    noOfItems={numberPerDisplay}
                    totalItems={responseData.data}
                    setPagingCallback={setDisplayFrom}
                  />
                </div>
              </React.Fragment>
            ) : (
              <WidgetContainer error={{ errorCode: 404 }} />
            )}
          </WidgetContainer>
        </BracketBox>
      </div>
    )
  },
)

StoryPointsDelTrendWidget.propTypes = {
  zoomEnable: PropTypes.bool,
  isFilter: PropTypes.bool,
  apiEndPointUrl: PropTypes.string,
  data: PropTypes.any,
  callback: PropTypes.func,
  selectedSprint: PropTypes.any,
  toolTipCustom: PropTypes.any,
  selectedPointCount: PropTypes.string,
  projectDetailsLinkUrl: PropTypes.string,
  viewType: PropTypes.any,
}

export default withRouter(StoryPointsDelTrendWidget)
