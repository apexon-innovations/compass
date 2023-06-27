import React from 'react'
import PropTypes from 'prop-types'
import SayDoRatioMemberWiseGraphConfig from './SayDoRatioMemberWiseWidgetConfig.json'
import SayDoRatioMemberWiseWidgetData from './SayDoRatioMemberWiseWidgetData'
import WidgetContainer from '../../components/WidgetContainer/WidgetContainer'
import useWidgetAPIWrapper from '../../components/WidgetContainer/useWidgetAPIWrapper'
import CustomLegendRing from '../../components/CommonGraphSections/CustomLegendRing/CustomLegendRing'
import CommonLineChart from '../../components/CommonGraphs/CommonLineChart/CommonLineChart'

const responseMapFunction = response => {
  return response.data
}

const SayDoRatioMemberWiseWidget = React.memo(({ apiEndPointUrl, data, callback }) => {
  // eslint-disable-next-line no-unused-vars
  const { response, error, isLoading } = useWidgetAPIWrapper({
    apiEndPointUrl,
    data,
    callback,
    responseMapFunction,
  })
  return (
    <div className="blurAll">
      <WidgetContainer isLoading={isLoading} data={response} error={error}>
        <CommonLineChart
          allData={{
            config: SayDoRatioMemberWiseGraphConfig,
            data: SayDoRatioMemberWiseWidgetData,
          }}
          customLegendFnc={CustomLegendRing}
        />
      </WidgetContainer>
    </div>
  )
})

SayDoRatioMemberWiseWidget.propTypes = {
  apiEndPointUrl: PropTypes.string,
  data: PropTypes.any,
  callback: PropTypes.func,
}

export default SayDoRatioMemberWiseWidget
