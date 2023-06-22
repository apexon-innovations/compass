import React, { useState } from 'react'
import PropTypes from 'prop-types'
import dataMapper from '../../utils/dataMapper'
import WidgetContainer from '../../components/WidgetContainer/WidgetContainer'
import useWidgetAPIWrapper from '../../components/WidgetContainer/useWidgetAPIWrapper'
import CommonLineChart from '../../components/CommonGraphs/CommonLineChart/CommonLineChart'
import SquareBox from '../../components/CommonComponents/SquareBox/SquareBox'
import CodeViolationClientWidgetConfig from './CodeViolationClientWidgetConfig.json'
import style from './CodeViolationClientWidget.module.scss'

const responseMap = response => {
  if (response && response.data) {
    for (let i = 0; i < response.data.length; i++) {
      response.data[i].monthData = response.data[i].monthData
        ? dataMapper(response.data[i].monthData, {
            '[].month': '[].name',
            '[].violations.total': '[].Violations',
            '[].violations.criticalAdded': '[].Added',
            '[].violations.criticalRemoved': '[].Removed',
          })
        : []
    }
    return response.data
  }
  return false
}

const numberPRDisplay = 9

const CodeViolationClientWidget = React.memo(
  ({ apiEndPointUrl, data, callback, repositoryName, requiredParams, errorMessage }) => {
    const { response, error, isLoading } = useWidgetAPIWrapper({
      apiEndPointUrl,
      data,
      callback,
      requiredParams,
      errorMessage,
      responseMapFunction: responseMap,
    })
    const [displayFroms] = useState(0)
    return (
      <SquareBox boxTitle={repositoryName ? repositoryName : ''}>
        <WidgetContainer isLoading={isLoading} error={error} data={response}>
          {response && response[0] ? (
            <React.Fragment>
              <div className={style.codeMetrix}>
                <ul className={style.codeMetrixList}></ul>
                <div className={style.codeMetrixGraph}>
                  <CommonLineChart
                    allData={{
                      data: [...response[0].monthData].slice(
                        displayFroms,
                        displayFroms + numberPRDisplay,
                      ),
                      config: CodeViolationClientWidgetConfig,
                    }}
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

CodeViolationClientWidget.propTypes = {
  apiEndPointUrl: PropTypes.string,
  data: PropTypes.any,
  callback: PropTypes.func,
  repositoryName: PropTypes.string,
  requiredParams: PropTypes.any,
  errorMessage: PropTypes.string,
}

export default CodeViolationClientWidget
