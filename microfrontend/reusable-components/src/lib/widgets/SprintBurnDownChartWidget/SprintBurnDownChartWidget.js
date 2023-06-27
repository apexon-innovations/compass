import React from 'react'
import PropTypes from 'prop-types'
import config from './SprintBurnDownChartWidgetConfig.json'
import dataMapper from '../../utils/dataMapper'
import WidgetContainer from '../../components/WidgetContainer/WidgetContainer'
import useWidgetAPIWrapper from '../../components/WidgetContainer/useWidgetAPIWrapper'
import CommonLineChart from '../../components/CommonGraphs/CommonLineChart/CommonLineChart'
import CustomLegendRing from '../../components/CommonGraphSections/CustomLegendRing/CustomLegendRing'

const dateNotGreaterThanToday = item => {
  return item.date <= Date.now()
}

const responseMapFunction = response => {
  if (response.data && response.data.dailyStatus) {
    let data = response.data.dailyStatus.filter(dateNotGreaterThanToday)
    return dataMapper(data, {
      '[].completedTasks': '[].Completed Tasks',
      '[].idealCompleted': '[].Ideal Completed',
      '[].remainingEfforts': '[].Remaining Efforts',
      '[].remainingTasks': '[].Remaining Tasks',
      '[].name': '[].name',
    })
  } else {
    return []
  }
}

const SprintBurnDownChartWidget = React.memo(({ apiEndPointUrl, data, callback }) => {
  const { response, error, isLoading } = useWidgetAPIWrapper({
    apiEndPointUrl,
    data,
    callback,
    responseMapFunction,
  })
  return (
    <div>
      <WidgetContainer isLoading={isLoading} data={response} error={error}>
        <CommonLineChart
          allData={{
            config: config,
            data: response,
          }}
          customLegendFnc={CustomLegendRing}
        />
      </WidgetContainer>
    </div>
  )
})

SprintBurnDownChartWidget.propTypes = {
  apiEndPointUrl: PropTypes.string,
  data: PropTypes.any,
  callback: PropTypes.func,
}

export default SprintBurnDownChartWidget
