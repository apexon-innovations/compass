import React, { useState } from 'react'
import PropTypes from 'prop-types'
import MultiSelect from '@khanacademy/react-multi-select'
import { withRouter } from '../../components/withRouter/withRouter'
import { convertProjectDataToDropDData } from '../../utils/commonFunction'
import DefectAgeingConfig from './DefectAgeingConfig.json'
import TooltipDefectAgeing from './TooltipDefectAgeing'
import { getColorCodeFromProjectName, getStoredData } from '../../utils/projectDataStoreFunction'
import colorCode from '../../const/colorCode'
import WidgetContainer from '../../components/WidgetContainer/WidgetContainer'
import useWidgetAPIWrapper from '../../components/WidgetContainer/useWidgetAPIWrapper'
import ZoomBox from '../../components/CommonComponents/ZoomBox/ZoomBox'
import CommonAreaChart from '../../components/CommonGraphs/CommonAreaChart/CommonAreaChart'
import CustomLegend from '../../components/CommonGraphSections/CustomLegend/CustomLegend'
import BracketBox from '../../components/CommonComponents/BracketBox/BracketBox'
import style from './DefectAgeingWidget.module.scss'

const responseMapFunction = response => {
  if (response && response.data) {
    const range = [
      { name: '>100' },
      { name: '75-100' },
      { name: '51-75' },
      { name: '41-50' },
      { name: '31-40' },
      { name: '21-30' },
      { name: '11-20' },
      { name: '6-10' },
      { name: '3-5' },
      { name: '0-3' },
    ]
    const projectData = []
    for (let i = 0; i < response.data.length; i++) {
      projectData.push({
        name: response.data[i].name,
        fill: getColorCodeFromProjectName(response.data[i].name) || colorCode[i],
        dataKey: response.data[i].name,
        color: getColorCodeFromProjectName(response.data[i].name) || colorCode[i],
        type: 'circle',
      })

      response.data[i].range.map(item => {
        range.map(rangeItem => {
          if (rangeItem.name === item.value) {
            rangeItem[`${response.data[i].name}`] = item.count
          }
          return rangeItem
        })
        return item
      })
    }
    return { data: range, projectData: projectData }
  }
  return false
}

const DefectAgeingWidget = React.memo(
  ({
    apiEndPointUrl,
    projectIds,
    data,
    callback,
    zoomEnable = true,
    isFilter = false,
    areaChart = false,
  }) => {
    const [widgetParamter, setWidgetParamter] = useState({
      apiEndPointUrlOfWidget: `${apiEndPointUrl}${projectIds}`,
      selectedOption: [],
      selectProjectName: [],
    })

    const [projectName, setProjectName] = useState(false)

    const { response, error, isLoading } = useWidgetAPIWrapper({
      apiEndPointUrl: widgetParamter.apiEndPointUrlOfWidget,
      data,
      callback,
      responseMapFunction: responseMapFunction,
    })

    if (response && response.projectData) {
      DefectAgeingConfig.scatter = response.projectData
      if (areaChart && !projectName) {
        setProjectName(response.projectData[0].name)
      } else if (
        areaChart &&
        projectName &&
        widgetParamter.selectedOption.length === 1 &&
        response.projectData[0].name !== projectName
      ) {
        setProjectName(response.projectData[0].name)
      }

      DefectAgeingConfig.area = [
        {
          key: projectName,
          stroke: '#7142b4',
          fill: '#7142b4',
          strokeWidth: '1',
          dot: true,
          id: 'dev',
        },
      ]
    }

    const projectData = convertProjectDataToDropDData(getStoredData('PROJECT_CLIENT_DATA'))

    const setActiveProject = data => {
      setProjectName(data.name)
    }

    return (
      <div className={style.defectAgeing}>
        <BracketBox needBracketBox={zoomEnable} animStatus="" boxTitle={'Defect Ageing'}>
          <WidgetContainer isLoading={isLoading} error={error} data={response}>
            {zoomEnable ? (
              <ZoomBox zoomEnable={true} redirectURL={'/client/defect-ageing'}></ZoomBox>
            ) : (
              ''
            )}
            {isFilter ? (
              <div className="controlRow">
                <div className={['transparentMultiSelect', style.zeroPadding].join(' ')}>
                  <MultiSelect
                    options={projectData}
                    selected={widgetParamter.selectedOption}
                    onSelectedChanged={selected => {
                      setWidgetParamter({
                        ...widgetParamter,
                        apiEndPointUrlOfWidget: `${apiEndPointUrl}${
                          selected && selected.length > 0
                            ? `${selected.join(',')}`
                            : `${projectIds}`
                        }`,
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
            {response.data ? (
              <div className={style.defectAgeingInner}>
                <CommonAreaChart
                  allData={{
                    data: response.data,
                    config: DefectAgeingConfig,
                  }}
                  customToolTip={TooltipDefectAgeing}
                />
                <CustomLegend
                  payload={response.projectData}
                  callback={areaChart ? setActiveProject : false}
                  activeLegendName={projectName}
                />
              </div>
            ) : (
              ''
            )}
          </WidgetContainer>
        </BracketBox>
      </div>
    )
  },
)

DefectAgeingWidget.propTypes = {
  apiEndPointUrl: PropTypes.any,
  data: PropTypes.any,
  callback: PropTypes.any,
  zoomEnable: PropTypes.any,
  projectIds: PropTypes.any,
  isFilter: PropTypes.any,
  areaChart: PropTypes.any,
}

export default withRouter(DefectAgeingWidget)
