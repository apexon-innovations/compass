import React from 'react'
import PropTypes from 'prop-types'
import config from './SayDoRatioWidgetConfig.json'
import dataMapper from '../../utils/dataMapper'
import WidgetContainer from '../../components/WidgetContainer/WidgetContainer'
import useWidgetAPIWrapper from '../../components/WidgetContainer/useWidgetAPIWrapper'
import CommonLineChart from '../../components/CommonGraphs/CommonLineChart/CommonLineChart'
import CustomLegendRing from '../../components/CommonGraphSections/CustomLegendRing/CustomLegendRing'

const responseMapFunction = response => {
  if (response.data && response.data.sprints && response.data.sprints.length > 0) {
    return dataMapper(response.data.sprints, {
      '[].totalExpected': '[].Expected',
      '[].totalCompleted': '[].Actual',
      '[].name': '[].name',
    })
  } else {
    return []
  }
}

const SayDoRatioWidget = React.memo(({ apiEndPointUrl, data, callback }) => {
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

SayDoRatioWidget.propTypes = {
  apiEndPointUrl: PropTypes.string,
  data: PropTypes.any,
  callback: PropTypes.func,
}

export default SayDoRatioWidget
