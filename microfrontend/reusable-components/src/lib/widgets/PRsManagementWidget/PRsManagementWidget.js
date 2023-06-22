import React, { useState } from 'react'
import PropTypes from 'prop-types'
import { Row, Col } from 'react-bootstrap'
import { Tooltip as ReactTooltip } from 'react-tooltip'
import { Scrollbars } from 'react-custom-scrollbars'
import dayjs from 'dayjs'
import advancedFormat from 'dayjs/plugin/advancedFormat'
import utc from 'dayjs/plugin/utc'
import { getPercentage } from '../../utils/projectPercentageCalculationFuntion'
import { getSprintStatusColor, getFilterData, responseMap } from './PRsManagementFunctions'
import ErrorMessage from '../../components/CommonComponents/ErrorMessage'
import WidgetContainer from '../../components/WidgetContainer/WidgetContainer'
import useWidgetAPIWrapper from '../../components/WidgetContainer/useWidgetAPIWrapper'
import PaginationArrows from '../../components/CommonComponents/PaginationArrows/PaginationArrows'
import style from './PRsManagement.module.scss'

dayjs.extend(advancedFormat)
dayjs.extend(utc)

const numberPRDisplay = 6

const RenderReactTollTip = props => {
  return (
    <ReactTooltip
      className={[style[props.toolTipClassName], 'customToolTip'].join(' ')}
      id={props.id}
      borderColor="#3c4d6d"
      arrowColor="#3c4d6d"
      effect="solid"
      delayHide={300}
      delayUpdate={300}
      offset={{ top: props.offset || 0 }}
    >
      {props.children}
    </ReactTooltip>
  )
}

const PRsManagementWidget = React.memo(({ apiEndPointUrl, data, callback }) => {
  const { response, error, isLoading } = useWidgetAPIWrapper({
    apiEndPointUrl,
    data,
    callback,
    responseMapFunction: responseMap,
  })
  let filteredResponse = false
  const [displayFrom, setDisplayFrom] = useState(0)
  const [filterKey, setFilterKey] = useState([])

  const renderPRCommit = (commitData, journeyDuration, createdDateTime) => {
    return (
      <React.Fragment>
        <div
          className={style.prCodeCommit}
          style={{
            left: `${getPercentage(journeyDuration, commitData.time - createdDateTime)}%`,
          }}
          data-tip
          data-tooltip-id={`commit-${commitData.time}`}
        />
        <RenderReactTollTip
          id={`commit-${commitData.time}`}
          toolTipClassName={'prCodeCommitTooltip'}
        >
          <p className="mb-0">Code updated on</p>
          <p className="mb-3">
            {dayjs(commitData.time)
              .utc()
              .format('MM/DD hh:mm:ss')}
          </p>
        </RenderReactTollTip>
      </React.Fragment>
    )
  }

  const renderPRComment = (commentData, journeyDuration, createdDateTime) => {
    return (
      <React.Fragment>
        <div
          className={[style.prComment, style.theme1].join(' ')}
          style={{
            left: `${getPercentage(journeyDuration, commentData.time - createdDateTime)}%`,
          }}
          data-tip
          data-tooltip-id={`comment-${commentData.time}`}
        />
        <RenderReactTollTip
          id={`comment-${commentData.time}`}
          toolTipClassName={'prCommentTooltip'}
        >
          <Scrollbars autoHeight autoHeightMax={120}>
            <div className={style.commentDetails}>
              <div className={style.nameBox}>
                <div className={style.name}>{commentData.by}</div>
                <div className={style.date}>
                  {dayjs(commentData.time)
                    .utc()
                    .format('MM/DD hh:mm:ss')}
                </div>
              </div>
              <div className={style.comments}>{commentData.message}</div>
            </div>
          </Scrollbars>
        </RenderReactTollTip>
      </React.Fragment>
    )
  }

  const renderBoxData = (title, value) => {
    return (
      <div className={style.dataBox}>
        <div className={style.title}>{title}</div>
        <div className={style.value}>{value}</div>
      </div>
    )
  }

  const renderPRProgressBar = (item, index) => {
    const journeyDurationInMS =
      (item.mergedDateTime ||
        item.declinedDateTime ||
        dayjs()
          .utc()
          .format('x')) - item.createdDateTime
    return (
      <div className={[style.box, style[getSprintStatusColor(item)]].join(' ')}>
        <div
          className={style.prNumber}
          data-tip
          data-tooltip-id={`${item.number}${index}-prNameToolTip`}
        >
          #{item.number}
        </div>

        <RenderReactTollTip
          id={`${item.number}${index}-prNameToolTip`}
          toolTipClassName={'prNumberTooltip'}
          offset={-10}
        >
          <Scrollbars autoHeight autoHeightMax={120}>
            <Row>
              <Col md={12} className="pl-0">
                <div className={style.mainBox}>
                  <div className={style.dataBox}>
                    <div className={[style.value, style[getSprintStatusColor(item)]].join(' ')}>
                      {item.commitLog}
                    </div>
                  </div>
                </div>
              </Col>
            </Row>
          </Scrollbars>
        </RenderReactTollTip>

        <div className={style.prProgressArea}>
          <div className={style.prProgress}>
            <div className={style.track}>
              <div
                className={style.prSubmission}
                style={{ left: '0' }}
                data-tip
                data-tooltip-id={`${item.id}-${index}-prSubmission`}
              />
              <RenderReactTollTip
                id={`${item.id}-${index}-prSubmission`}
                toolTipClassName={'prNumberTooltip'}
                offset={-10}
              >
                <Scrollbars autoHeight autoHeightMax={120}>
                  <Row>
                    <Col md={5} className="pl-0">
                      <div className={style.mainBox}>
                        <div className={style.dataBox}>
                          <div className={style.title}>Submitter</div>
                          <div className={[style.value, style.amber].join(' ')}>
                            {item.author.name}
                          </div>
                        </div>
                        {renderBoxData(
                          'Date',
                          dayjs(item.createdDateTime)
                            .utc()
                            .format('MM/DD hh:mm:ss'),
                        )}
                        {renderBoxData('Source', item.sourceBranch)}
                        {renderBoxData('Destination', item.destinationBranch)}
                      </div>
                    </Col>
                    <Col md={7} className="pl-0 pr-0">
                      <div className={style.dataBox}>
                        <div className={style.title}>Reviewer ({item.reviewers.length || 0})</div>
                        {item.reviewers ? (
                          item.reviewers.map((reviewer, index) => {
                            return (
                              <div key={index} className={[style.value, style.green].join(' ')}>
                                {reviewer.name}
                              </div>
                            )
                          })
                        ) : (
                          <div className={[style.value, style.red].join(' ')}>
                            No Reviewer For this PR{' '}
                          </div>
                        )}
                      </div>
                    </Col>
                  </Row>
                </Scrollbars>
              </RenderReactTollTip>

              {item.commits &&
                item.commits.map((commits, index) => {
                  return (
                    <React.Fragment key={index}>
                      {item.createdDateTime < commits.time &&
                      commits.time < item.createdDateTime + journeyDurationInMS
                        ? renderPRCommit(commits, journeyDurationInMS, item.createdDateTime)
                        : ''}
                    </React.Fragment>
                  )
                })}

              {item.comments &&
                item.comments.map((comment, index) => {
                  return (
                    <React.Fragment key={index}>
                      {item.createdDateTime < comment.time &&
                      comment.time < item.createdDateTime + journeyDurationInMS
                        ? renderPRComment(comment, journeyDurationInMS, item.createdDateTime)
                        : ''}
                    </React.Fragment>
                  )
                })}
              {item.state === 'merged' || item.state === 'declined' ? (
                <React.Fragment>
                  <div
                    className={item.state === 'declined' ? style.prCodeDeclined : style.prCodeMerge}
                    data-tooltip-place="left"
                    data-tip
                    data-tooltip-id={`${item.id}-${index}-merged`}
                    style={{ left: '100%' }}
                  />
                  <RenderReactTollTip
                    id={`${item.id}-${index}-merged`}
                    toolTipClassName={'prCodeMergeTooltip'}
                  >
                    <p>
                      {item.mergedDateTime
                        ? 'Merged on'
                        : item.declinedDateTime
                        ? 'Declined On'
                        : 'Open'}
                    </p>
                    {item.mergedDateTime || item.declinedDateTime ? (
                      <p className="mb-0">
                        {dayjs(item.mergedDateTime || item.declinedDateTime)
                          .utc()
                          .format('MM/DD hh:mm:ss')}
                      </p>
                    ) : (
                      ''
                    )}
                  </RenderReactTollTip>
                </React.Fragment>
              ) : (
                ''
              )}
            </div>
          </div>
          <div className={style.totalTime}>{item.journeyDuration}</div>
        </div>
      </div>
    )
  }

  const filterIcon = color => {
    let isExist = filterKey.indexOf(color)
    return (
      <div
        className={`switch ${color}  ${isExist > -1 ? 'fill' : ''}`}
        onClick={() => {
          setDisplayFrom(0)
          if (isExist > -1) {
            let updateFilter = [...filterKey]
            updateFilter.splice(isExist, 1)

            setFilterKey(updateFilter)
          } else {
            let updateFilter = [...filterKey, color]
            setFilterKey(updateFilter)
          }
        }}
      />
    )
  }

  if (response) {
    filteredResponse = filterKey.length > 0 ? getFilterData(response, filterKey) : response
  }

  return (
    <WidgetContainer isLoading={isLoading} data={response} error={error}>
      <div className="coloredToggleSwitch">
        {filterIcon('red')}
        {filterIcon('blue')}
        {filterIcon('green')}
      </div>
      <div className={style.prTimelineBox}>
        <div className={style.timelineBox}>
          {response && (
            <React.Fragment>
              {filteredResponse && filteredResponse.length > 0 ? (
                <React.Fragment>
                  {filteredResponse
                    .slice(displayFrom, displayFrom + numberPRDisplay)
                    .map((mapData, index) => {
                      return (
                        <React.Fragment key={index}>
                          {renderPRProgressBar(mapData, index)}
                        </React.Fragment>
                      )
                    })}
                </React.Fragment>
              ) : (
                <div className={[style.fixNoDataIssue, style.red].join(' ')}>
                  <ErrorMessage
                    error={{
                      errorCode: 404,
                      isMsgPassed: true,
                      message: 'No data available...!',
                    }}
                  />
                </div>
              )}
            </React.Fragment>
          )}
        </div>
        <PaginationArrows
          pageNumber={displayFrom}
          noOfItems={numberPRDisplay}
          totalItems={filteredResponse}
          setPagingCallback={setDisplayFrom}
        />
      </div>
    </WidgetContainer>
  )
})

PRsManagementWidget.propTypes = {
  apiEndPointUrl: PropTypes.string,
  sprintId: PropTypes.string,
  data: PropTypes.any,
  callback: PropTypes.func,
}

RenderReactTollTip.propTypes = {
  toolTipClassName: PropTypes.string,
  id: PropTypes.string,
  offset: PropTypes.any,
  children: PropTypes.any,
}

export default PRsManagementWidget
