import React, { useState } from 'react'
import PropTypes from 'prop-types'
import CodeTechnicalDebtClientWidgetConfig from './CodeTechnicalDebtClientWidgetConfig.json'
import TooltipTechnicalDebtOverview from './TooltipTechnicalDebtOverview'
import WidgetContainer from '../../components/WidgetContainer/WidgetContainer'
import useWidgetAPIWrapper from '../../components/WidgetContainer/useWidgetAPIWrapper'
import CommonLineChart from '../../components/CommonGraphs/CommonLineChart/CommonLineChart'
import PaginationArrows from '../../components/CommonComponents/PaginationArrows/PaginationArrows'
import SquareBox from '../../components/CommonComponents/SquareBox/SquareBox'
import style from './CodeTechnicalDebtClientWidget.module.scss'

const responseMap = response => {
  if (response && response.data) {
    for (let i = 0; i < response.data.length; i++) {
      response.data[i].monthData = response.data[i].monthData
        ? response.data[i].monthData.map(item => {
            return {
              name: item.month,
              ProductiveCode: item.totalDays,
              ProductiveCodeLine: item.totalDays,
              totalDays: item.totalDays,
              maintainability: item.maintainability,
              reliability: item.reliability,
              security: item.security,
            }
          })
        : []
    }
    return response.data
  }
  return false
}

const numberPRDisplay = 9

const CodeTechnicalDebtClientWidget = React.memo(
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
                      data: [...response[0].monthData].slice(
                        displayFrom,
                        displayFrom + numberPRDisplay,
                      ),
                      config: { ...CodeTechnicalDebtClientWidgetConfig },
                    }}
                    customToolTip={TooltipTechnicalDebtOverview}
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

CodeTechnicalDebtClientWidget.propTypes = {
  apiEndPointUrl: PropTypes.string,
  data: PropTypes.any,
  callback: PropTypes.func,
  repositoryName: PropTypes.string,
  requiredParams: PropTypes.any,
  errorMessage: PropTypes.string,
}

export default CodeTechnicalDebtClientWidget
