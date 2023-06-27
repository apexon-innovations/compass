import React, { useState } from 'react'
import config from './SubmitterMetricsConfig.json'
import dataMapper from '../../utils/dataMapper'
import { filterData } from '../../utils/sortingFunction'
import PropTypes from 'prop-types'
import WidgetContainer from '../../components/WidgetContainer/WidgetContainer'
import useWidgetAPIWrapper from '../../components/WidgetContainer/useWidgetAPIWrapper'
import CustomLineDot from '../../components/CommonGraphSections/CustomLineDot/CustomLineDot'
import CommonLineChart from '../../components/CommonGraphs/CommonLineChart/CommonLineChart'
import PaginationArrows from '../../components/CommonComponents/PaginationArrows/PaginationArrows'
import CustomDropdown from '../../components/CommonComponents/CustomDropdown'
import RenderToolTipData from './ToolTipSubmitterMetrics.js'
import style from './SubmitterMetrics.module.scss'

const dropDownValue = [
  { displayName: 'Top Submitter', key: 'Total PRs' },
  { displayName: 'Member wise submission', key: 'Name' },
  { displayName: 'Most comments & reviews', key: 'Reviewer Comments' },
  { displayName: 'Most Recommits', key: 'Recommits' },
]
const defaultSelectedValue = dropDownValue[0]

const responseMap = response => {
  if (response && response.data && response.data.submitters.length > 0) {
    response.data.submitters = dataMapper(response.data.submitters, {
      '[].declinedPrs': '[].Declined PRs',
      '[].mergedPrs': '[].Merged PRs',
      '[].name': '[].Name',
      '[].openPrs': '[].Open PRs',
      '[].recommits': '[].Recommits',
      '[].reviewerComments': '[].Reviewer Comments',
      '[].totalPrs': '[].Total PRs',
      '[].averagePrCycleTime': '[].Average Per Cycle Time',
    })
    return response.data
  }
  return false
}

export const submitterMetricConfigMappingFnc = (configuration, averagePrs) => {
  configuration.referenceLine[0].y = averagePrs
  configuration.referenceLine[0].label.value = averagePrs
  configuration.line[0].dot = <CustomLineDot />
  return configuration
}

const SubmitterMetricsWidget = React.memo(({ apiEndPointUrl, data, callback }) => {
  const { response, error, isLoading } = useWidgetAPIWrapper({
    apiEndPointUrl,
    data,
    callback,
    responseMapFunction: responseMap,
  })
  const [selectedDropdownValue, setSelectedDropdownValue] = useState(defaultSelectedValue)
  const [displayFrom, setDisplayFrom] = useState(0)
  const numberPerDisplay = 10

  const onSelectCallback = e => {
    setSelectedDropdownValue(dropDownValue[e])
  }
  return (
    <div className={style.submitterMetrics}>
      <WidgetContainer isLoading={isLoading} data={response} error={error}>
        <CustomDropdown
          align={'alignRight'}
          items={dropDownValue}
          selectedOption={selectedDropdownValue.displayName}
          onSelectCallback={onSelectCallback}
        />
        {response && response.submitters ? (
          <React.Fragment>
            <CommonLineChart
              key={selectedDropdownValue.key}
              allData={{
                config: submitterMetricConfigMappingFnc(config, response.averagePrs),
                data: [...filterData(response.submitters, selectedDropdownValue)].splice(
                  displayFrom,
                  numberPerDisplay,
                ),
              }}
              customToolTip={RenderToolTipData}
            />
            <PaginationArrows
              pageNumber={displayFrom}
              noOfItems={numberPerDisplay}
              totalItems={response.submitters}
              setPagingCallback={setDisplayFrom}
            />
          </React.Fragment>
        ) : (
          ''
        )}
      </WidgetContainer>
    </div>
  )
})

SubmitterMetricsWidget.propTypes = {
  apiEndPointUrl: PropTypes.string,
  data: PropTypes.any,
  callback: PropTypes.func,
}

export default SubmitterMetricsWidget
