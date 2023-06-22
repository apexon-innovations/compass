import React from 'react'
import PropTypes from 'prop-types'
import StoryPointsDelvsAccConfig from './StoryPointsDelvsAccConfig.json'
import WidgetContainer from '../../components/WidgetContainer/WidgetContainer'
import useWidgetAPIWrapper from '../../components/WidgetContainer/useWidgetAPIWrapper'
import ModernBox from '../../components/CommonComponents/ModernBox/ModernBox'
import CommonAreaChart from '../../components/CommonGraphs/CommonAreaChart/CommonAreaChart'
import dataMapper from '../../utils/dataMapper'
import style from './StoryPointsDelvsAcc.module.scss'

const responseMapFunction = response => {
  if (response.data) {
    const data = dataMapper(response.data, {
      '[].sprintName': '[].name',
      '[].summary.totalDone': '[].Delivered',
      '[].summary.totalClosed': '[].Accepted',
    })
    let dataExist = false
    data.map(item => {
      // eslint-disable-next-line
      if (item.hasOwnProperty('Delivered') && item.hasOwnProperty('Accepted')) {
        dataExist = true
      }
      return item
    })
    return dataExist ? data : { errorDisplay: true }
  } else {
    return []
  }
}

const StoryPointsDelvsAccWidget = React.memo(({ apiEndPointUrl, data, callback }) => {
  const { response, error, isLoading } = useWidgetAPIWrapper({
    apiEndPointUrl,
    data,
    callback,
    responseMapFunction,
  })

  return (
    <ModernBox animStatus="" boxTitle={'Story Points - Delivered vs Accepted'}>
      <WidgetContainer isLoading={isLoading} error={error} data={response}>
        {response && Array.isArray(response) && response.length > 0 ? (
          <div className={style.storyPointsDelvsAcc}>
            <CommonAreaChart
              allData={{
                data: response,
                config: StoryPointsDelvsAccConfig,
              }}
            />
          </div>
        ) : (
          <WidgetContainer error={{ message: 'Sorry, No Data Available.', isMsgPassed: true }} />
        )}
      </WidgetContainer>
    </ModernBox>
  )
})

StoryPointsDelvsAccWidget.propTypes = {
  apiEndPointUrl: PropTypes.string,
  data: PropTypes.any,
  callback: PropTypes.func,
}

export default StoryPointsDelvsAccWidget
