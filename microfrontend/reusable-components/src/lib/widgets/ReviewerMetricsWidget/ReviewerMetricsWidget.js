import React, { Fragment, useState } from 'react'
import PropTypes from 'prop-types'
import { filterData } from '../../utils/sortingFunction'
import dataMapper from '../../utils/dataMapper'
import WidgetContainer from '../../components/WidgetContainer/WidgetContainer'
import useWidgetAPIWrapper from '../../components/WidgetContainer/useWidgetAPIWrapper'
import CustomDropdown from '../../components/CommonComponents/CustomDropdown'
import CommonLineChart from '../../components/CommonGraphs/CommonLineChart/CommonLineChart'
import PaginationArrows from '../../components/CommonComponents/PaginationArrows/PaginationArrows'
import config from './ReviewerMetricsWidgetConfig.json'
import { filterByColorData, mapConfingWithResponse } from './ReviewerMetricsFunctions'
import style from './ReviewerMetricsWidget.module.scss'

const CustomToolTip = ({ active, payload }) => {
  if (active) {
    return (
      payload &&
      payload[0] && (
        <div className="customToolTip">
          <p className="title">{`${payload[0].payload.Name}`}</p>
          <ul className="reviewList">
            <li
              style={{ color: payload[0].color }}
            >{`Total PR : ${payload[0].payload['Total PRs']}`}</li>
            <li
              style={{ color: payload[0].color }}
            >{`Total Hours : ${payload[0].payload['Total Review Time']}`}</li>
          </ul>
        </div>
      )
    )
  }
  return null
}

const CommonColorFilterBox = ({ callback, colorName, sortOption }) => {
  return (
    <div
      onClick={() => {
        callback(colorName)
      }}
      className={[`switch ${colorName}`, sortOption === colorName ? 'fill' : ''].join(' ')}
    ></div>
  )
}

const responseMap = response => {
  if (response) {
    response.data.reviewers = dataMapper(response.data.reviewers, {
      '[].totalPrs': '[].Total PRs',
      '[].name': '[].Name',
      '[].totalReviewTime': '[].Total Review Time',
    })
    response.data.reviewers = filterData(response.data.reviewers, 'Total PRs')
    response.data.averagePrTime = Number(response.data.averagePrTime.replace(' Hours', '').trim())
    response.data.reviewers.map(item => {
      let totalReviewTime = Number(item['Total Review Time'].replace(' Hours', '').trim())
      if (totalReviewTime >= response.data.averagePrTime) {
        item['red'] = totalReviewTime
      } else if (totalReviewTime < response.data.averagePrTime) {
        item['green'] = totalReviewTime
      }
      item['Total Review Time'] = totalReviewTime
      return item
    })
    return response.data
  } else {
    return false
  }
}

const ReviewerMetricsWidget = ({ apiEndPointUrl, data, callback }) => {
  const dropdownOption = [
    { displayName: 'Most Review', key: 'Total PRs' },
    { displayName: 'Resource Wise Review', key: 'Name' },
  ]

  const { response, error, isLoading } = useWidgetAPIWrapper({
    apiEndPointUrl,
    data,
    callback,
    responseMapFunction: responseMap,
  })

  const [selectedOption, setSelectedOption] = useState(0)
  const [sortOption, setSortOption] = useState('')
  const [displayFrom, setDisplayFrom] = useState(0)
  const numberPerDisplay = 10
  const selectOptionName = dropdownOption[selectedOption].displayName
  let reviewerData = false
  if (response) {
    reviewerData = {}
    reviewerData.reviewers =
      sortOption !== '' ? filterByColorData(response.reviewers, sortOption) : response.reviewers
    reviewerData.reviewers = filterData(reviewerData.reviewers, dropdownOption[selectedOption])
  }

  const setSortingOptionOnClick = color => {
    sortOption !== color ? setSortOption(color) : setSortOption('')
  }

  return (
    <Fragment>
      <CustomDropdown
        items={dropdownOption}
        selectedOption={selectOptionName}
        onSelectCallback={setSelectedOption}
      />
      <WidgetContainer isLoading={isLoading} error={error} data={response}>
        {reviewerData && (
          <div className={style.reviewMetrics}>
            <div className="coloredToggleSwitch" key={sortOption}>
              <CommonColorFilterBox
                callback={setSortingOptionOnClick}
                colorName={'red'}
                sortOption={sortOption}
              />
              <CommonColorFilterBox
                callback={setSortingOptionOnClick}
                colorName={'green'}
                sortOption={sortOption}
              />
            </div>
            <CommonLineChart
              allData={{
                config: mapConfingWithResponse(response.averagePrTime, config),
                data: [...reviewerData.reviewers].splice(displayFrom, numberPerDisplay),
              }}
              customToolTip={CustomToolTip}
            />
            <PaginationArrows
              pageNumber={displayFrom}
              noOfItems={numberPerDisplay}
              totalItems={reviewerData.reviewers}
              setPagingCallback={setDisplayFrom}
            />
          </div>
        )}
      </WidgetContainer>
    </Fragment>
  )
}

ReviewerMetricsWidget.propTypes = {
  apiEndPointUrl: PropTypes.string,
  data: PropTypes.any,
  callback: PropTypes.func,
}

CustomToolTip.propTypes = {
  payload: PropTypes.any,
  active: PropTypes.any,
}

CommonColorFilterBox.propTypes = {
  callback: PropTypes.func,
  colorName: PropTypes.string,
  sortOption: PropTypes.string,
}

export default ReviewerMetricsWidget
