import React, { useState } from 'react'
import PropTypes from 'prop-types'
import dataMapper from '../../utils/dataMapper'
import WidgetContainer from '../../components/WidgetContainer/WidgetContainer'
import useWidgetAPIWrapper from '../../components/WidgetContainer/useWidgetAPIWrapper'
import CommonLineChart from '../../components/CommonGraphs/CommonLineChart/CommonLineChart'
import PaginationArrows from '../../components/CommonComponents/PaginationArrows/PaginationArrows'
import SquareBox from '../../components/CommonComponents/SquareBox/SquareBox'
import CodeScoreClientWidgetConfig from './CodeScoreClientWidgetConfig.json'
import style from './CodeScoreClientWidget.module.scss'

const responseMap = response => {
  if (response && response.data) {
    for (let i = 0; i < response.data.length; i++) {
      response.data[i].monthData = response.data[i].monthData
        ? dataMapper(response.data[i].monthData, {
            '[].month': '[].name',
            '[].efficiency': '[].Efficiency',
            '[].robustness': '[].Robustness',
            '[].security': '[].Security',
          })
        : []
    }
    return response.data
  }
  return false
}

const numberPRDisplay = 9

const CodeScoreClientWidget = React.memo(
  ({ apiEndPointUrl, data, callback, repositoryName, requiredParams, errorMessage }) => {
    const { response, error, isLoading } = useWidgetAPIWrapper({
      apiEndPointUrl,
      data,
      callback,
      requiredParams,
      errorMessage,
      responseMapFunction: responseMap,
    })
    const [displayFrom, setDisplayFrom] = useState(0)

    return (
      <SquareBox boxTitle={repositoryName ? repositoryName : ''}>
        <WidgetContainer isLoading={isLoading} error={error} data={response}>
          {response ? (
            <React.Fragment>
              <div className={style.codeMetrix}>
                <div className={style.codeMetrixGraph}>
                  <CommonLineChart
                    allData={{
                      data: response[0].monthData.slice(displayFrom, displayFrom + numberPRDisplay),
                      config: CodeScoreClientWidgetConfig,
                    }}
                  />
                  <PaginationArrows
                    pageNumber={displayFrom}
                    noOfItems={numberPRDisplay}
                    totalItems={response[0].monthData}
                    setPagingCallback={setDisplayFrom}
                  />
                </div>
              </div>
            </React.Fragment>
          ) : (
            ''
          )}
        </WidgetContainer>
      </SquareBox>
    )
  },
)

CodeScoreClientWidget.propTypes = {
  apiEndPointUrl: PropTypes.string,
  data: PropTypes.any,
  callback: PropTypes.func,
  repositoryName: PropTypes.string,
  requiredParams: PropTypes.any,
  errorMessage: PropTypes.string,
}

export default CodeScoreClientWidget
