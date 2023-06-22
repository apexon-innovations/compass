import React, { useState } from 'react'
import PropTypes from 'prop-types'
import MultiSelect from '@khanacademy/react-multi-select'
import { sortByNameFunction } from '../../utils/sortingFunction'
import BackArrow from '../../components/CommonComponents/BackArrow/BackArrow'
import WidgetContainer from '../../components/WidgetContainer/WidgetContainer'
import useWidgetAPIWrapper from '../../components/WidgetContainer/useWidgetAPIWrapper'
import CommonLineChart from '../../components/CommonGraphs/CommonLineChart/CommonLineChart'
import CustomLegend from '../../components/CommonGraphSections/CustomLegend/CustomLegend'
import ZoomBox from '../../components/CommonComponents/ZoomBox/ZoomBox'
import style from './ProjectCompletionTrends.module.scss'
import config from './ProjectCompletionTrendsConfig.json'
import colorCode from '../../const/colorCode'
import { mapConfigData, convertDropDownValue } from './ProjectCompletionTrendsFunction'
import { getStoredData, getColorCodeFromProjectName } from '../../utils/projectDataStoreFunction'
import ProjectCompletionTrendsToolTip from './ProjectCompletionTrendsToolTip'
import BracketBox from '../../components/CommonComponents/BracketBox/BracketBox'

const responseMap = (response, selectedPointCount = 'Points', boxTitle = false) => {
  if (response.data) {
    let data = []
    let projectList = []
    response.data.map((item, index) => {
      projectList.push({
        name: item.name,
        label: item.name,
        value: item.projectId,
        stroke: getColorCodeFromProjectName(item.name) || colorCode[index],
        fill: getColorCodeFromProjectName(item.name) || colorCode[index],
        type: 'circle',
        hasBorder: false,
        borderType: 'solid',
        align: 'left',
      })

      item.sprints.map((itemDayWise, sprintIndex) => {
        const sprintName = boxTitle
          ? boxTitle[sprintIndex]
          : sprintIndex === item.sprints.length - 1
          ? 'Current Sprint'
          : `Prev Sprint ${item.sprints.length - sprintIndex - 1}`
        let exist = false
        if (data.length > 0) {
          data = data.map(existItem => {
            if (existItem.name === sprintName) {
              exist = true
              existItem[item.name] = itemDayWise[`completed${selectedPointCount}`]
              existItem[`${item.name}Data`] = {
                completed: itemDayWise[`completed${selectedPointCount}`],
                label: `Story ${selectedPointCount}`,
                name: itemDayWise.name,
                squads: itemDayWise[`squads`],
              }
            }
            return existItem
          })
        }
        if (!exist) {
          let temp = {
            name: sprintName,
          }
          temp[item.name] = itemDayWise[`completed${selectedPointCount}`]
          temp[`${item.name}Data`] = {
            completed: itemDayWise[`completed${selectedPointCount}`],
            label: `Story ${selectedPointCount}`,
            name: item.name,
            squads: itemDayWise[`squads`],
          }
          data.push(temp)
        }
        return itemDayWise
      })
      return item
    })

    return { projectList: sortByNameFunction(projectList, 'name'), data: data }
  } else {
    return []
  }
}

const InfoValue = ({ count }) => {
  return (
    <div className="infoContent">
      <p>A consolidated story-points completion trend for the last ({count}) months. </p>
    </div>
  )
}

const ProjectCompletionTrendsWidget = ({
  apiEndPointUrl,
  data,
  callback,
  zoomEnable,
  isFilter,
  bracketBox = true,
  showHideButton,
  selectedPointCount,
  sprintCount,
  projectId,
}) => {
  const projectData = convertDropDownValue(getStoredData('PROJECT_CLIENT_DATA'))

  const [widgetParamter, setWidgetParamter] = useState({
    apiEndPointUrlOfWidget: `${apiEndPointUrl}${
      projectId
        ? `?iscProjectIds=${projectId}${sprintCount ? `&sprintCount=${sprintCount}` : ``}`
        : `${sprintCount ? `?iscProjectIds=all&sprintCount=${sprintCount}` : `?iscProjectIds=all`}`
    }`,
    selectedOption: [],
  })

  const { response, error, isLoading } = useWidgetAPIWrapper({
    apiEndPointUrl: widgetParamter.apiEndPointUrlOfWidget,
    data,
    callback,
  })
  const [btnShow1, setBtnShow1] = useState()

  let responseData = false
  if (response) {
    responseData = responseMap(response, selectedPointCount)
  }

  return (
    <React.Fragment>
      <div className={style.projectCompletionTrends}>
        <BracketBox
          hideShow={btnShow1 ? 'hide' : ''}
          boxTitle={''}
          needBracketBox={bracketBox ? true : false}
        >
          <WidgetContainer isLoading={isLoading} error={error} data={responseData}>
            {responseData ? (
              <div className={style.projectCompletionTrendsInner}>
                {showHideButton && (
                  <div className="btnHideShow" onClick={() => setBtnShow1(!btnShow1)}>
                    {btnShow1 ? 'Show' : 'Hide'}
                  </div>
                )}
                <ZoomBox zoomEnable={zoomEnable} redirectURL={'/client/project-completion-detail'}>
                  {isFilter ? (
                    <div className="controlRow">
                      <BackArrow title={'Consolidated View'} redirectURL={'/client/overview'} />
                      <div className={['transparentMultiSelect', style.zeroPadding].join(' ')}>
                        <MultiSelect
                          options={projectData}
                          selected={widgetParamter.selectedOption}
                          onSelectedChanged={selected => {
                            if (selected.length > 0) {
                              setWidgetParamter({
                                ...widgetParamter,
                                apiEndPointUrlOfWidget: `${apiEndPointUrl}${
                                  selected && selected.length > 0
                                    ? `?iscProjectIds=${selected.toString()}${
                                        sprintCount ? `&sprintCount=${sprintCount}` : ``
                                      }`
                                    : sprintCount
                                    ? `?sprintCount=${sprintCount}`
                                    : ``
                                }`,
                                selectedOption: selected,
                              })
                            }
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
                  <div className="toggleThings">
                    <div className="clientWidgetInner">
                      <CommonLineChart
                        allData={{
                          data: responseData.data,
                          config: mapConfigData(
                            JSON.parse(JSON.stringify(config)),
                            JSON.parse(JSON.stringify(responseData.projectList)),
                            selectedPointCount,
                          ),
                        }}
                        customToolTip={!showHideButton ? ProjectCompletionTrendsToolTip : false}
                      />
                      <CustomLegend payload={[...responseData.projectList]} />
                    </div>
                  </div>
                </ZoomBox>
              </div>
            ) : (
              <WidgetContainer error={{ errorCode: 404 }} />
            )}
          </WidgetContainer>
        </BracketBox>
      </div>
    </React.Fragment>
  )
}

InfoValue.propTypes = {
  count: PropTypes.number,
}
ProjectCompletionTrendsWidget.propTypes = {
  zoomEnable: PropTypes.bool,
  isFilter: PropTypes.bool,
  callback: PropTypes.func,
  boxTitle: PropTypes.any,
  data: PropTypes.any,
  showHideButton: PropTypes.bool,
  bracketBox: PropTypes.bool,
  apiEndPointUrl: PropTypes.string,
  sprintCount: PropTypes.any,
  projectId: PropTypes.string,
  selectedPointCount: PropTypes.string,
}

export default ProjectCompletionTrendsWidget
