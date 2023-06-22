import React, { useState } from 'react'
import PropTypes from 'prop-types'
import WidgetContainer from '../../components/WidgetContainer/WidgetContainer'
import useWidgetAPIWrapper from '../../components/WidgetContainer/useWidgetAPIWrapper'
import CommonLineChart from '../../components/CommonGraphs/CommonLineChart/CommonLineChart'
import PaginationArrows from '../../components/CommonComponents/PaginationArrows/PaginationArrows'
import SquareBox from '../../components/CommonComponents/SquareBox/SquareBox'
import CodeSmellAndIssuesClientWidgetConfig from './CodeSmellAndIssuesClientWidgetConfig.json'
import style from './CodeSmellAndIssuesClientWidget.module.scss'

const responseMap = (response, lineName) => {
  if (response && response.data && lineName) {
    const firstLabel = lineName[0].toLowerCase()
    const secondLabel = lineName[1].toLowerCase()
    for (let i = 0; i < response.data.length; i++) {
      response.data[i].monthData = response.data[i].monthData
        ? response.data[i].monthData.map(item => {
            return {
              name: item.month,
              [`${lineName[0]}`]: item[firstLabel],
              [`${lineName[1]}`]: item[secondLabel],
            }
          })
        : []
    }
    return response.data
  }
  return false
}

const mapConfig = (config, lineName) => {
  config.line[0].key = lineName[0]
  config.line[1].key = lineName[1]
  return config
}

const numberPRDisplay = 9

const CodeSmellAndIssuesClientWidget = React.memo(
  ({ apiEndPointUrl, data, callback, lineName, repositoryName, requiredParams, errorMessage }) => {
    const { response, error, isLoading } = useWidgetAPIWrapper({
      apiEndPointUrl,
      data,
      callback,
      requiredParams,
      errorMessage,
    })
    let responseData
    if (response) {
      responseData = responseMap({ ...response }, lineName)
    }
    const [displayFrom, setDisplayFrom] = useState(0)

    return (
      <SquareBox boxTitle={repositoryName ? repositoryName : ''}>
        <WidgetContainer isLoading={isLoading} error={error} data={responseData}>
          {responseData ? (
            <React.Fragment>
              <div className={style.codeMetrix}>
                <div className={style.codeMetrixGraph}>
                  <CommonLineChart
                    allData={{
                      data: [...responseData[0].monthData].slice(
                        displayFrom,
                        displayFrom + numberPRDisplay,
                      ),
                      config: {
                        ...mapConfig({ ...CodeSmellAndIssuesClientWidgetConfig }, lineName),
                      },
                    }}
                  />
                  <PaginationArrows
                    pageNumber={displayFrom}
                    noOfItems={numberPRDisplay}
                    totalItems={responseData[0].monthData}
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

CodeSmellAndIssuesClientWidget.propTypes = {
  apiEndPointUrl: PropTypes.string,
  data: PropTypes.any,
  callback: PropTypes.func,
  lineName: PropTypes.any,
  repositoryName: PropTypes.string,
  requiredParams: PropTypes.any,
  errorMessage: PropTypes.string,
}

export default CodeSmellAndIssuesClientWidget
