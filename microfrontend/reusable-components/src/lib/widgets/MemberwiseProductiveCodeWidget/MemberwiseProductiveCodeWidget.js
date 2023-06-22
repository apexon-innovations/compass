import React, { useState } from 'react'
import PropTypes from 'prop-types'
import dataMapper from '../../utils/dataMapper'
import MultiSelect from '@khanacademy/react-multi-select'
import { sortFunction } from '../../utils/sortingFunction'
import ToolTipView from './CustomToolTip'
import config from './MemberwiseProductiveCodeWidgetConfig.json'
import WidgetContainer from '../../components/WidgetContainer/WidgetContainer'
import useWidgetAPIWrapper from '../../components/WidgetContainer/useWidgetAPIWrapper'
import CommonLineChart from '../../components/CommonGraphs/CommonLineChart/CommonLineChart'
import PaginationArrows from '../../components/CommonComponents/PaginationArrows/PaginationArrows'
import BracketBox from '../../components/CommonComponents/BracketBox/BracketBox'
import { timeDurationOptions } from '../../const/dataConst'
import style from './MemberwiseProductiveCodeWidget.module.scss'

const filterMembersByName = (data, names) => {
  let tempData = JSON.parse(JSON.stringify(data))
  return tempData.filter(item => {
    if (names.indexOf(item.name) > -1 || names.length === 0) return true
    return false
  })
}

const responseMapFunction = (response, paramToPassInMapFunction) => {
  if (response && response.data) {
    let tempData = response.data.members

    tempData = dataMapper(tempData, {
      '[].productiveCode': '[].Productive Code',
      '[].codeChurn': '[].Code Churn',
      '[].legacyRefactor': '[].Legacy Refactor',
      '[].addedLineOfCode': '[].Added Lines Of Code',
      '[].removedLineOfCode': '[].Removed Lines Of Code',
      '[].addedLineOfCodeTillDate': `[].Added Line Of Code Till Date`,
      '[].removedLineOfCodeTillDate': `[].Removed Line Of Code Till Date`,
      '[].name': '[].name',
    })

    const tempDataObj = tempData.map(function(el) {
      var o = Object.assign({}, el)
      o.displayTimeDuration = paramToPassInMapFunction
      return o
    })

    response.data.members = sortFunction(tempDataObj, 'Added Lines Of Code')

    return response.data
  }
  return false
}

const MemberwiseProductiveCodeWidget = React.memo(
  ({ apiEndPointUrl, data, callback, timeDuration }) => {
    let displayTimeDuration = ''

    if (timeDuration && timeDurationOptions) {
      displayTimeDuration = timeDurationOptions.find(x => x.key === timeDuration).displayName
    }

    const { response, error, isLoading } = useWidgetAPIWrapper({
      apiEndPointUrl,
      data,
      callback,
      responseMapFunction: responseMapFunction,
      paramToPassInMapFunction: displayTimeDuration,
    })

    const [displayFrom, setDisplayFrom] = useState(0)
    const numberPerDisplay = 10
    const [selected, setSelected] = useState([])

    let members = []
    let responseFiltered = []
    if (response) {
      response.members.map(item => {
        members.push({ value: item.name, label: item.name })
        return item
      })

      responseFiltered = filterMembersByName(response.members, selected)
    }
    const renderHeadings = (title, value, color) => {
      return (
        <div className={style.box}>
          <div className={style.title}>{title}</div>
          <div className={[style.number, style[color] ? style[color] : ''].join(' ')}>{value}</div>
        </div>
      )
    }

    return (
      <div className={style.MemberwiseProductiveCode}>
        <BracketBox animStatus="" boxTitle={'Code Churn & Legacy Refactor'}>
          <div className={['transparentMultiSelect'].join(' ')}>
            {members.length > 0 && (
              <MultiSelect
                options={members}
                selected={selected}
                onSelectedChanged={selected => {
                  setSelected(selected)
                  setDisplayFrom(0)
                }}
                valueRenderer={selected => {
                  return selected.length === 0 ? 'Select Members' : `(${selected.length})`
                }}
                overrideStrings={{
                  selectSomeItems: 'Select Members',
                  allItemsAreSelected: 'All Members are Selected',
                  selectAll: 'Select All Members',
                  search: 'Search Members',
                }}
              />
            )}
          </div>
          <div className="w-100 d-flex h-100 flex-column">
            {response && response.overall ? (
              <div className={style.codeNumbers}>
                {response.overall.lineOfCode || response.overall.lineOfCode === 0
                  ? renderHeadings('Total Line Of Code', response.overall.lineOfCode, 'yellow')
                  : ''}
                {renderHeadings(
                  `Added Line of Code In ${displayTimeDuration}`,
                  response.overall['addedLineOfCode'],
                  'excelBlue',
                )}
                {renderHeadings(
                  `Removed Line Of Code In ${displayTimeDuration}`,
                  response.overall['removedLineOfCode'],
                  'amber',
                )}
                {response.overall.codeChurn || response.overall.codeChurn === 0
                  ? renderHeadings('Legacy Refactor', response.overall.legacyRefactor, 'grey')
                  : ''}
                {response.overall.codeChurn || response.overall.codeChurn === 0
                  ? renderHeadings('Code Churn', response.overall.codeChurn, 'green')
                  : ''}
              </div>
            ) : (
              ''
            )}
            <WidgetContainer isLoading={isLoading} error={error} data={response}>
              {response && responseFiltered && responseFiltered.length > 0 ? (
                <div className={style.chartBox}>
                  <CommonLineChart
                    allData={{
                      data: responseFiltered.slice(displayFrom, displayFrom + numberPerDisplay),
                      config: config,
                    }}
                    customToolTip={ToolTipView}
                  />
                </div>
              ) : (
                ''
              )}
              <PaginationArrows
                pageNumber={displayFrom}
                noOfItems={numberPerDisplay}
                totalItems={responseFiltered}
                setPagingCallback={setDisplayFrom}
              />
            </WidgetContainer>
          </div>
        </BracketBox>
      </div>
    )
  },
)

MemberwiseProductiveCodeWidget.propTypes = {
  apiEndPointUrl: PropTypes.string,
  data: PropTypes.any,
  timeDuration: PropTypes.any,
  callback: PropTypes.func,
}

export default MemberwiseProductiveCodeWidget
