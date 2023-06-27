import React from 'react'
import PropTypes from 'prop-types'
import WidgetContainer from '../../components/WidgetContainer/WidgetContainer'
import useWidgetAPIWrapper from '../../components/WidgetContainer/useWidgetAPIWrapper'
import CommonLineChart from '../../components/CommonGraphs/CommonLineChart/CommonLineChart'
import CustomLegendRing from '../../components/CommonGraphSections/CustomLegendRing/CustomLegendRing'
import TeamVelocityWidgetData from './TeamVelocityWidgetData'
import TeamVelocityGraphConfig from './TeamVelocityWidgetConfig.json'

const responseMapFunction = response => {
  return response.data
}

const TeamVelocityWidget = React.memo(({ apiEndPointUrl, data, callback }) => {
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
            config: TeamVelocityGraphConfig,
            data: TeamVelocityWidgetData,
          }}
          customLegendFnc={CustomLegendRing}
        />
      </WidgetContainer>
    </div>
  )
})

TeamVelocityWidget.propTypes = {
  apiEndPointUrl: PropTypes.string,
  data: PropTypes.any,
  callback: PropTypes.func,
}

export default TeamVelocityWidget
