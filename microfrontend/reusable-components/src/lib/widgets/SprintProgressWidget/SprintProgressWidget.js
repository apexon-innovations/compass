import React from 'react'
import PropTypes from 'prop-types'
import { ProgressBar } from 'react-bootstrap'
import dayjs from 'dayjs'
import utc from 'dayjs/plugin/utc'
import { Tooltip as ReactTooltip } from 'react-tooltip'
import WidgetContainer from '../../components/WidgetContainer/WidgetContainer'
import useWidgetAPIWrapper from '../../components/WidgetContainer/useWidgetAPIWrapper'
import style from './SprintProgress.module.scss'

dayjs.extend(utc)

const responseMapFunction = response => {
  return response ? response.data : response
}

const SprintProgressWidget = ({ apiEndPointUrl, data, callback }) => {
  const { response, error, isLoading } = useWidgetAPIWrapper({
    apiEndPointUrl,
    data,
    callback,
    responseMapFunction,
  })

  const countPercentage = (totalDay, daysLeft) => {
    return 100 - (100 * daysLeft) / (totalDay + 1)
  }

  const renderEmptyDay = daysLeft => {
    const tempData = []
    for (let i = 0; i < daysLeft; i++) {
      tempData.push(
        <div className={style.division} key={`renderEmptyDay-${i}`} data-tip data-tooltip-id={i} />,
      )
    }
    return tempData
  }

  return (
    <div className={style.sprintProgress}>
      <WidgetContainer isLoading={isLoading} error={error} data={response} errorMessage={false}>
        {response && Object.keys(response).length > 0 && (
          <React.Fragment>
            <ProgressBar
              now={countPercentage(
                dayjs(
                  dayjs(response.sprint.endDate)
                    .utc()
                    .format('MM/DD/YYYY'),
                ).diff(
                  dayjs(
                    dayjs(response.sprint.startDate)
                      .utc()
                      .format('MM/DD/YYYY'),
                  ),
                  'day',
                  true,
                ),
                dayjs(
                  dayjs(response.sprint.endDate)
                    .utc()
                    .format('MM/DD/YYYY'),
                ).diff(
                  dayjs(
                    dayjs()
                      .utc()
                      .format('MM/DD/YYYY'),
                  ),
                  'day',
                  true,
                ),
              )}
            />
            <div className={style.sprintProgressBar}>
              {response &&
                response.sprint &&
                response.sprint.dailyStatus.map((item, key) => {
                  return (
                    <React.Fragment key={key}>
                      {dayjs().diff(dayjs(item.date), 'hour') >= 0 && (
                        <React.Fragment>
                          <ReactTooltip
                            className="customThemeToolTip"
                            id={`key-${key}`}
                            border={true}
                            borderColor="rgb(124, 180, 235)"
                            arrowColor="rgba(2, 7, 27, 1)"
                          >
                            <h3>
                              Date:{' '}
                              {dayjs(item.date)
                                .utc()
                                .format('MM/DD')}
                            </h3>
                            <ul>
                              <li>
                                <div className="label">To Dos:</div>{' '}
                                <div className="value">{item.todo}</div>
                              </li>
                              <li>
                                <div className="label">In-Progress:</div>{' '}
                                <div className="value">{item.inprogress}</div>
                              </li>
                              <li>
                                <div className="label">Completed:</div>{' '}
                                <div className="value">{item.completed}</div>
                              </li>
                              <li>
                                <div className="label">Remaining:</div>{' '}
                                <div className="value">{item.remaining}</div>
                              </li>
                            </ul>
                          </ReactTooltip>
                          <div className={style.division} data-tip data-tooltip-id={`key-${key}`} />
                        </React.Fragment>
                      )}
                    </React.Fragment>
                  )
                })}
              {renderEmptyDay(
                dayjs(
                  dayjs(response.sprint.endDate)
                    .utc()
                    .format('MM/DD/YYYY'),
                ).diff(
                  dayjs(
                    dayjs()
                      .utc()
                      .format('MM/DD/YYYY'),
                  ),
                  'day',
                  true,
                ),
              )}
            </div>
            <div className={style.progressLegends}>
              <div className={style.startPoint}>
                {dayjs(response.sprint.startDate)
                  .utc()
                  .format('MM/DD')}
              </div>
              <div className={style.middlePoint}>
                <div className={style.daysLeft}>{response.sprint.name} </div>
                <div className={style.hoursLeft}>
                  ({response.sprint.daysLeft} days left / {response.sprint.hoursLeft} hours)
                </div>
              </div>
              <div className={style.endPoint}>
                {dayjs(response.sprint.endDate)
                  .utc()
                  .format('MM/DD')}
              </div>
            </div>
          </React.Fragment>
        )}
      </WidgetContainer>
    </div>
  )
}

SprintProgressWidget.propTypes = {
  apiEndPointUrl: PropTypes.string,
  data: PropTypes.any,
  callback: PropTypes.func,
}

export default SprintProgressWidget
