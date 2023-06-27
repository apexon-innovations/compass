import React, { useState } from 'react'
import PropTypes from 'prop-types'
import CustomLegendRing from '../../components/CommonGraphSections/CustomLegendRing/CustomLegendRing'
import WidgetContainer from '../../components/WidgetContainer/WidgetContainer'
import useWidgetAPIWrapper from '../../components/WidgetContainer/useWidgetAPIWrapper'
import CustomDropdown from '../../components/CommonComponents/CustomDropdown'
import CommonLineChart from '../../components/CommonGraphs/CommonLineChart/CommonLineChart'
import BracketBox from '../../components/CommonComponents/BracketBox/BracketBox'
import NPSWidgetRecentConfig from './NPSWidgetRecentConfig.json'
import NPSWidgetRecentFiveDataConfig from './NPSWidgetRecentFiveData.json'
import style from './NPSWidget.module.scss'
import {
  recentNPSDataMappingFnc,
  lastFiveNPSDataMappingFnc,
  lastFiveNPSConfigMappingFnc,
  recentNPSConfigMappingFnc,
} from './NPSWidgetMappingFunction'

const NPSWidget = React.memo(({ apiEndPointUrl, data, callback }) => {
  const dropdownOptions = [
    { displayName: 'Last Five', type: 'historical' },
    { displayName: 'Last Received Review', type: 'recent' },
  ]

  let defaultSelectedValue = dropdownOptions[1].displayName

  const [widgetParamter, setWidgetParamter] = useState({
    apiEndPointUrlOfWidget: apiEndPointUrl + '?reportType=recent',
    selectedOption: defaultSelectedValue,
  })

  const { response, error, isLoading } = useWidgetAPIWrapper({
    apiEndPointUrl: widgetParamter.apiEndPointUrlOfWidget,
    data,
    callback,
  })

  const onSelect = index => {
    setWidgetParamter({
      apiEndPointUrlOfWidget: apiEndPointUrl + '?reportType=' + dropdownOptions[index].type,
      selectedOption: dropdownOptions[index].displayName,
    })
  }

  return (
    <BracketBox verticalAlignment="alignTop" animStatus="" boxTitle={'NPS'}>
      <div className={[style.NPSGraph, 'blurAll'].join(' ')}>
        <CustomDropdown
          items={dropdownOptions}
          selectedOption={widgetParamter.selectedOption}
          onSelectCallback={onSelect}
        />
        <React.Fragment>
          {response && response.data && (
            <div className="d-flex align-items-center ">
              <div className={style.average_nps}>Average NPS</div>
              <div className={style.npsNumber}>
                <div className={style.number}>
                  <span className={[style.number_color, style.green].join(' ')}>
                    {response.data.averageNps}
                  </span>
                </div>
              </div>
            </div>
          )}
          <React.Fragment>
            <WidgetContainer isLoading={isLoading} data={response} error={error}>
              {widgetParamter.selectedOption === defaultSelectedValue
                ? response && (
                    <CommonLineChart
                      allData={{
                        config: recentNPSConfigMappingFnc(
                          NPSWidgetRecentConfig,
                          response.data.averageNps,
                        ),
                        data: recentNPSDataMappingFnc(response),
                      }}
                    />
                  )
                : response && (
                    <CommonLineChart
                      allData={{
                        config: lastFiveNPSConfigMappingFnc(
                          NPSWidgetRecentFiveDataConfig,
                          response,
                        ),
                        data: lastFiveNPSDataMappingFnc(response),
                      }}
                      customLegendFnc={CustomLegendRing}
                    />
                  )}
            </WidgetContainer>
          </React.Fragment>
        </React.Fragment>
      </div>
    </BracketBox>
  )
})

NPSWidget.propTypes = {
  apiEndPointUrl: PropTypes.string,
  data: PropTypes.any,
  callback: PropTypes.func,
  projectId: PropTypes.string,
}

export default NPSWidget
