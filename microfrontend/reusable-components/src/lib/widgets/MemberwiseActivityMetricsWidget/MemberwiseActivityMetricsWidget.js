import React, { useState } from 'react'
import PropTypes from 'prop-types'
import MultiSelect from '@khanacademy/react-multi-select'
import config from './MemberwiseActivityMetricsWidgetConfig.json'
import CustomLegendSquare from '../../components/CommonGraphSections/CustomLegendRing/CustomLegendSquare'
import WidgetContainer from '../../components/WidgetContainer/WidgetContainer'
import useWidgetAPIWrapper from '../../components/WidgetContainer/useWidgetAPIWrapper'
import CommonLineChart from '../../components/CommonGraphs/CommonLineChart/CommonLineChart'
import PaginationArrows from '../../components/CommonComponents/PaginationArrows/PaginationArrows'
import BracketBox from '../../components/CommonComponents/BracketBox/BracketBox'
import {
  responseMap,
  filterMembersByCommit,
  filterMembersByName,
} from './MemberwiseActivityMetricsFunction'
import style from './MemberwiseActivityMetricsWidget.module.scss'

const MemberwiseActivityMetricsWidget = React.memo(({ apiEndPointUrl, data, callback }) => {
  const { response, error, isLoading } = useWidgetAPIWrapper({
    apiEndPointUrl,
    data,
    callback,
    responseMapFunction: responseMap,
  })

  const [commitFilter, setCommitFilter] = useState(false)
  const [displayFrom, setDisplayFrom] = useState(0)
  const [selected, setSelected] = useState([])
  const numberPerDisplay = 10

  const filterMemberActivityData = color => {
    if (color === commitFilter) {
      setCommitFilter(false)
    } else {
      setCommitFilter(color)
    }
  }
  let responseFiltered = false
  if (response) {
    responseFiltered = filterMembersByName(
      filterMembersByCommit(response.data, commitFilter),
      selected,
    )
  }

  return (
    <div className={[style.MemberwiseActivityMetrics, ''].join(' ')}>
      <BracketBox animStatus="" boxTitle={'Memberwise Activity Metrics'}>
        <div className={['transparentMultiSelect'].join(' ')}>
          {response && (
            <MultiSelect
              options={response.memberNames}
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
        <WidgetContainer isLoading={isLoading} error={error} data={response}>
          {responseFiltered && (
            <div className="w-100 d-flex flex-column">
              <div className="coloredToggleSwitch">
                <FilterIcon
                  color={'red'}
                  title={'Review Comments'}
                  callback={filterMemberActivityData}
                  commitFilter={commitFilter}
                />
                <FilterIcon
                  color={'blue'}
                  title={'Merged PRs'}
                  callback={filterMemberActivityData}
                  commitFilter={commitFilter}
                />
                <FilterIcon
                  color={'amber'}
                  title={'Total PRs'}
                  callback={filterMemberActivityData}
                  commitFilter={commitFilter}
                />
                <FilterIcon
                  color={'green'}
                  title={'Code Commits'}
                  callback={filterMemberActivityData}
                  commitFilter={commitFilter}
                />
              </div>
              <CommonLineChart
                allData={{
                  data: [...responseFiltered].splice(displayFrom, numberPerDisplay),
                  config: mapConfingResponse(config),
                }}
                customToolTip={CustomToolTip}
                customLegendFnc={CustomLegendSquare}
              />
              <PaginationArrows
                pageNumber={displayFrom}
                noOfItems={numberPerDisplay}
                totalItems={responseFiltered}
                setPagingCallback={setDisplayFrom}
              />
            </div>
          )}
        </WidgetContainer>
      </BracketBox>
    </div>
  )
})

let toolTipName

const CustomToolTip = props => {
  const { active, payload } = props
  if (active && toolTipName && payload) {
    if (toolTipName.includes('sprint1') && payload[0] && payload[0]?.payload?.sprint1) {
      return <ToolTipView sprint={payload[0].payload.sprint1} name={payload[0].payload.Label} />
    } else if (toolTipName.includes('sprint2') && payload[0] && payload[0]?.payload?.sprint2) {
      return <ToolTipView sprint={payload[0].payload.sprint2} name={payload[0].payload.Label} />
    } else {
      return null
    }
  }
  return null
}

const ToolTipView = ({ sprint, name }) => {
  return (
    <div className="customToolTip" data-name={name}>
      <p className="title">{`${sprint['Month Name']}`}</p>
      <ul className="memberMetrics">
        {sprint['Code Commits'] || sprint['Code Commits'] === 0 ? (
          <li className="green">
            <span className="square"></span> {`Code Commits : ${sprint['Code Commits']}`}
          </li>
        ) : null}

        {sprint['Merged PRs'] || sprint['Merged PRs'] === 0 ? (
          <li className="blue">
            <span className="square"></span> {`Merged PRs : ${sprint['Merged PRs']}`}
          </li>
        ) : null}
        {sprint['Total PRs'] || sprint['Total PRs'] === 0 ? (
          <li className="amber">
            <span className="square"></span> {`Total PRs : ${sprint['Total PRs']}`}
          </li>
        ) : null}
        {sprint['Review Comments'] || sprint['Review Comments'] === 0 ? (
          <li className="red">
            <span className="square"></span> {`Review Comments : ${sprint['Review Comments']}`}
          </li>
        ) : null}
      </ul>
    </div>
  )
}

const FilterIcon = ({ color, callback, commitFilter, title }) => {
  return (
    <div
      className={`switch ${color}  ${commitFilter === title ? 'fill' : ''}`}
      onClick={() => {
        callback(title)
      }}
    />
  )
}

const mouseOverFunction = barName => {
  return () => {
    toolTipName = barName
  }
}

const mapConfingResponse = config => {
  config.bar.map(item => {
    item.onMouseOver = mouseOverFunction(item.key)
    return item
  })
  return config
}

MemberwiseActivityMetricsWidget.propTypes = {
  apiEndPointUrl: PropTypes.string,
  data: PropTypes.any,
  callback: PropTypes.func,
  payload: PropTypes.any,
}

CustomToolTip.propTypes = {
  props: PropTypes.any,
  active: PropTypes.any,
  payload: PropTypes.any,
}

ToolTipView.propTypes = {
  sprint: PropTypes.object,
  name: PropTypes.string,
}

FilterIcon.propTypes = {
  color: PropTypes.any,
  callback: PropTypes.any,
  commitFilter: PropTypes.any,
  title: PropTypes.any,
}

export default MemberwiseActivityMetricsWidget
