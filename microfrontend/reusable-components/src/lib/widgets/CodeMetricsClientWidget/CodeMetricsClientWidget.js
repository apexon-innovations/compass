import React, { useState } from 'react'
import PropTypes from 'prop-types'
import dataMapper from '../../utils/dataMapper'
import WidgetContainer from '../../components/WidgetContainer/WidgetContainer'
import useWidgetAPIWrapper from '../../components/WidgetContainer/useWidgetAPIWrapper'
import CommonLineChart from '../../components/CommonGraphs/CommonLineChart/CommonLineChart'
import PaginationArrows from '../../components/CommonComponents/PaginationArrows/PaginationArrows'
import SquareBox from '../../components/CommonComponents/SquareBox/SquareBox'
import CodeHealthMatricsConfig from './CodeHealthMatricsConfig.json'
import style from './CodeMetricsClientWidget.module.scss'

const responseMap = response => {
  if (response && response.data) {
    for (let i = 0; i < response.data.length; i++) {
      response.data[i].monthData = response.data[i].monthData
        ? dataMapper(response.data[i].monthData, {
            '[].month': '[].name',
            '[].addedLineOfCode': '[].Added Lines Of Code',
            '[].removedLineOfCode': '[].Removed Lines Of Code',
          })
        : []
    }
    return response.data
  }
  return false
}

const numberPRDisplay = 9

const CodeMetricsClientWidget = React.memo(
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
                <ul className={style.codeMetrixList}>
                  {/* <li className={style.newLine}>
                  New Line of Code <span>{response[0].repoName}</span>
                </li> */}
                  <li className={style.churn}>
                    Churn <span>{response[0].codeChurn}</span>
                  </li>
                  <li className={style.lagecy}>
                    Legacy <span>{response[0].legacyRefactor}</span>
                  </li>
                </ul>
                <div className={style.codeMetrixGraph}>
                  <CommonLineChart
                    allData={{
                      data: response[0].monthData.slice(displayFrom, displayFrom + numberPRDisplay),
                      config: CodeHealthMatricsConfig,
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

CodeMetricsClientWidget.propTypes = {
  apiEndPointUrl: PropTypes.string,
  data: PropTypes.any,
  callback: PropTypes.func,
  repositoryName: PropTypes.string,
  requiredParams: PropTypes.any,
  errorMessage: PropTypes.string,
}

export default CodeMetricsClientWidget
