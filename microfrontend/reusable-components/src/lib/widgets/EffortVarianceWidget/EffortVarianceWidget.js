import React from 'react'
import PropTypes from 'prop-types'
import OverlapGraphs from '../../components/OverlapGraphs/OverlapGraphs'
import WidgetContainer from '../../components/WidgetContainer/WidgetContainer'
import useWidgetAPIWrapper from '../../components/WidgetContainer/useWidgetAPIWrapper'
import BracketBox from '../../components/CommonComponents/BracketBox/BracketBox'

const responseMap = response => {
  return response && response.data ? response.data.ev : {}
}

const EffortVarianceWidget = React.memo(({ apiEndPointUrl, data, callback }) => {
  const { response, error, isLoading } = useWidgetAPIWrapper({
    apiEndPointUrl,
    data,
    callback,
    responseMapFunction: responseMap,
  })

  return (
    <BracketBox animStatus="" boxTitle={'Effort Variance'}>
      <WidgetContainer isLoading={isLoading} data={response} error={error}>
        {response.overall && (
          <div className="blurAll">
            <OverlapGraphs
              graphData={[
                {
                  value: response.overall.percentage.replace('%', ''),
                  title: 'Overall',
                  valueLabel: response.overall.percentage,
                  total: response.overall.percentage.replace('%', ''),
                  line: Number(response.overall.percentage.replace('%', '')) === 0,
                },
                {
                  value: response.sprint.percentage.replace('%', ''),
                  title: 'Sprint',
                  strokeWidth: 3,
                  valueLabel: response.sprint.percentage,
                  total: response.overall.percentage.replace('%', ''),
                  line: Number(response.overall.percentage.replace('%', '')) === 0,
                },
              ]}
            />
          </div>
        )}
      </WidgetContainer>
    </BracketBox>
  )
})

EffortVarianceWidget.propTypes = {
  apiEndPointUrl: PropTypes.string,
  data: PropTypes.any,
  callback: PropTypes.func,
}

export default EffortVarianceWidget
