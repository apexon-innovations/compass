import React from 'react'
import PropTypes from 'prop-types'
import dayjs from 'dayjs'
import utc from 'dayjs/plugin/utc'
import advancedFormat from 'dayjs/plugin/advancedFormat'
import pieChartConfig from './chartConfig.json'
import PieChartWithMiddleFill from '../../components/CommonGraphs/PieChart/PieChartWithMiddleFill'
import WidgetContainer from '../../components/WidgetContainer/WidgetContainer'
import useWidgetAPIWrapper from '../../components/WidgetContainer/useWidgetAPIWrapper'

dayjs.extend(advancedFormat)
dayjs.extend(utc)

const responseMap = response => {
  const tempPieChartDta = []
  response.data.merged &&
    tempPieChartDta.push({ name: 'Merged', value: response.data.merged, fill: '#10e80a' })
  response.data.unattended &&
    tempPieChartDta.push({ name: 'Unattended', value: response.data.unattended, fill: '#ffbb15' })
  response.data.open &&
    tempPieChartDta.push({ name: 'Open', value: response.data.open, fill: '#00d2ff' })
  response.data.declined &&
    tempPieChartDta.push({ name: 'Declined', value: response.data.declined, fill: '#e00e2c' })

  return {
    title: 'Total',
    value: response.data.total,
    lastUpdatedDate: response.data.lastUpdatedDate,
    pieChartData: tempPieChartDta,
  }
}

const PRsMgmtPieChartWidget = React.memo(({ apiEndPointUrl, data, callback }) => {
  const { response, error, isLoading } = useWidgetAPIWrapper({
    apiEndPointUrl,
    data,
    callback,
    responseMapFunction: responseMap,
  })

  return (
    <WidgetContainer isLoading={isLoading} data={response} error={error}>
      {response && (
        <React.Fragment>
          <PieChartWithMiddleFill
            allData={{
              config: pieChartConfig,
              data: response,
            }}
            customToolTip={CustomToolTip}
          />
          <p className="text-center">
            As On{' '}
            {dayjs(response.lastUpdatedDate)
              .utc()
              .format('Do MMMM')}
          </p>
        </React.Fragment>
      )}
    </WidgetContainer>
  )
})

const CustomToolTip = props => {
  const { active, payload } = props
  if (active) {
    return (
      <div className="customToolTip">
        <p
          style={{ color: payload[0].payload.fill }}
          className="title mb-0"
        >{`${payload[0].name}: ${payload[0].value}`}</p>
      </div>
    )
  }
  return null
}

PRsMgmtPieChartWidget.propTypes = {
  apiEndPointUrl: PropTypes.string,
  sprintId: PropTypes.string,
  data: PropTypes.any,
  callback: PropTypes.func,
}

CustomToolTip.propTypes = {
  props: PropTypes.any,
  active: PropTypes.bool,
  payload: PropTypes.any,
}

export default PRsMgmtPieChartWidget
