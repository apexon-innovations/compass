import React, { useState } from 'react'
import PropTypes from 'prop-types'
import MultiSelect from '@khanacademy/react-multi-select'
import WidgetContainer from '../../components/WidgetContainer/WidgetContainer'
import useWidgetAPIWrapper from '../../components/WidgetContainer/useWidgetAPIWrapper'
import CommonLineChart from '../../components/CommonGraphs/CommonLineChart/CommonLineChart'
import CustomLegend from '../../components/CommonGraphSections/CustomLegend/CustomLegend'
import ZoomBox from '../../components/CommonComponents/ZoomBox/ZoomBox'
import colorCode from '../../const/colorCode'
import { convertProjectDataToDropDData } from '../../utils/commonFunction'
import { sortByNameFunction } from '../../utils/sortingFunction'
import { getStoredData, getColorCodeFromProjectName } from '../../utils/projectDataStoreFunction'
import config from './VelocityTrendWidgetConfig'
import style from './VelocityTrendsWidget.module.scss'

const responseMap = response => {
  const projectList = []
  const averageLine = []
  const tempResponse = response.data
  for (let i = 0; i < tempResponse.length; i++) {
    if (tempResponse[i].iscProjectId) {
      let projectData = {
        name: tempResponse[i].projectName,
        label: tempResponse[i].projectName,
        value: tempResponse[i].iscProjectId,
        stroke: getColorCodeFromProjectName(tempResponse[i].projectName) || colorCode[i],
        fill: getColorCodeFromProjectName(tempResponse[i].projectName) || colorCode[i],
        type: 'circle',
        hasBorder: false,
        borderType: 'solid',
        align: 'left',
      }
      projectList.push(projectData)
    } else if (tempResponse.length > 2 && tempResponse[i].iscProjectId === 0) {
      averageLine.push({
        name: tempResponse[i].projectName,
        label: tempResponse[i].projectName,
        value: tempResponse[i].iscProjectId,
        stroke: '#FFB245',
        fill: '#FFB245',
        type: 'circle',
        borderType: 'dashed',
        hasBorder: true,
        align: 'right',
      })
    }
  }
  return { data: tempResponse, projectList: sortByNameFunction(projectList, 'name'), averageLine }
}

const graphDataMap = (response, selectedPointCount) => {
  const data = []
  for (let i = 0; i < response.length; i++) {
    if (response[i].sprints) {
      const allSprintData = response[i].sprints.reverse()
      for (let j = 0; j < allSprintData.length; j++) {
        const sprintData = {}
        const selectedValue = selectedPointCount === 'Points' ? 'completedPoints' : 'completedCount'
        sprintData[`${response[i].projectName}`] = parseInt(allSprintData[j][selectedValue])
        sprintData['name'] = `Prev sprint ${j + 1}`
        data[j] = {
          ...data[j],
          ...sprintData,
        }
      }
    }
  }

  return data.reverse()
}

const mapConfig = (config, projectList, selectedPointCount) => {
  config.y.label =
    selectedPointCount === 'Points'
      ? `Velocity (Iin Story ${selectedPointCount})`
      : `Velocity (In ${selectedPointCount})`
  config.line = []
  for (let i = 0; i < projectList.length; i++) {
    if (projectList[i].value) {
      let lineConfig = {
        key: projectList[i].name,
        dot: true,
        stroke: projectList[i].stroke,
        fill: projectList[i].fill,
        strokeWidth: '1',
      }
      config.line.push(lineConfig)
    } else if (projectList[i].value === 0 && projectList.length > 2) {
      let lineConfig = {
        key: projectList[i].name,
        stroke: projectList[i].stroke,
        fill: projectList[i].fill,
        strokeWidth: '1',
        dot: false,
        strokeDasharray: '5 5',
      }
      config.line.push(lineConfig)
    }
  }
  return config
}

const VelocityTrendsWidget = React.memo(
  ({
    apiEndPointUrl,
    data,
    callback,
    isFilter,
    zoomEnable,
    selectedSprint,
    selectedPointCount,
  }) => {
    const [widgetParamter, setWidgetParamter] = useState({
      apiEndPointUrlOfWidget: `${apiEndPointUrl}/velocityTrends?iscProjectIds=all&sprintCount=${selectedSprint}`,
      selectedOption: [],
    })

    const { response, error, isLoading } = useWidgetAPIWrapper({
      apiEndPointUrl: widgetParamter.apiEndPointUrlOfWidget,
      data,
      callback,
      responseMapFunction: responseMap,
    })

    const projectData = convertProjectDataToDropDData(getStoredData('PROJECT_CLIENT_DATA'))

    return (
      <React.Fragment>
        <div className={style.velocityTrend}>
          <WidgetContainer isLoading={isLoading} error={error} data={response}>
            {response ? (
              <ZoomBox zoomEnable={zoomEnable} redirectURL={'/client/client-velocity-detail'}>
                {isFilter ? (
                  <React.Fragment>
                    <div className={style.controlRow}>
                      <div className="graphNavBox">
                        <div className="caption">Consolidated view</div>
                      </div>
                      <div className="transparentMultiSelect">
                        <MultiSelect
                          options={projectData}
                          selected={widgetParamter.selectedOption}
                          onSelectedChanged={selected => {
                            setWidgetParamter({
                              apiEndPointUrlOfWidget: `${apiEndPointUrl}/velocityTrends?iscProjectIds=${
                                selected && selected.length > 0 ? selected.toString() : 'all'
                              }&sprintCount=${selectedSprint}`,
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
                  </React.Fragment>
                ) : (
                  ''
                )}
                <div className="clientWidgetInner">
                  <CommonLineChart
                    allData={{
                      config: mapConfig(
                        config,
                        [...response.projectList, ...response.averageLine],
                        selectedPointCount,
                      ),
                      data: graphDataMap(response.data, selectedPointCount),
                    }}
                  />
                  <CustomLegend payload={[...response.projectList, ...response.averageLine]} />
                </div>
              </ZoomBox>
            ) : (
              ''
            )}
          </WidgetContainer>
        </div>
      </React.Fragment>
    )
  },
)

VelocityTrendsWidget.propTypes = {
  zoomEnable: PropTypes.bool,
  isFilter: PropTypes.bool,
  apiEndPointUrl: PropTypes.string,
  data: PropTypes.any,
  callback: PropTypes.func,
  selectedSprint: PropTypes.any,
  selectedPointCount: PropTypes.any,
}

export default VelocityTrendsWidget
