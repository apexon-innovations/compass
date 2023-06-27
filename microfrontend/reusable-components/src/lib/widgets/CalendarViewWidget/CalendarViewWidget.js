import React from 'react'
import PropTypes from 'prop-types'
import dayjs from 'dayjs'
import { Tooltip as ReactTooltip } from 'react-tooltip'
import { Scrollbars } from 'react-custom-scrollbars'
import advancedFormat from 'dayjs/plugin/advancedFormat'
import utc from 'dayjs/plugin/utc'
import ModernBox from '../../components/CommonComponents/ModernBox/ModernBox'
import WidgetContainer from '../../components/WidgetContainer/WidgetContainer'
import useWidgetAPIWrapper from '../../components/WidgetContainer/useWidgetAPIWrapper'
import style from './CalendarView.module.scss'

dayjs.extend(advancedFormat)
dayjs.extend(utc)

const responseMapFunction = response => {
  const mapObj = {
    hoursLostForLeaves: 0,
    maxHourLostPerDay: 0,
    publicHoliday: '',
    hoursLostForHoliday: 0,
    calenderObj: {
      Mo: [],
      Tu: [],
      We: [],
      Th: [],
      Fr: [],
      Sa: [],
      Su: [],
    },
  }
  response.data.leaveCalendar.map((item, index) => {
    if (
      index === 0 &&
      dayjs(item.date)
        .utc()
        .format('dd') !== 'Mo'
    ) {
      for (const [key] of Object.entries(mapObj.calenderObj)) {
        if (
          key ===
          dayjs(item.date)
            .utc()
            .format('dd')
        ) {
          break
        } else {
          mapObj.calenderObj[key].push({})
        }
      }
    }
    mapObj.calenderObj[
      dayjs(item.date)
        .utc()
        .format('dd')
    ].push(item)

    mapObj.hoursLostForLeaves += item.memberLeaveHours
    mapObj.hoursLostForHoliday += item.publicHolidayHours
    if (item.isPublicHoliday) {
      mapObj.publicHoliday += `${mapObj.publicHoliday ? ', ' : ''} ${dayjs(item.date)
        .utc()
        .format('Do')}`
    }
    if (mapObj.maxHourLostPerDay < item.memberLeaveHours) {
      mapObj.maxHourLostPerDay = item.memberLeaveHours
    }
    return item
  })

  return mapObj
}

const CalendarView = React.memo(({ apiEndPointUrl, data, callback }) => {
  const { response, error, isLoading } = useWidgetAPIWrapper({
    apiEndPointUrl,
    data,
    callback,
    responseMapFunction: responseMapFunction,
  })
  return (
    <WidgetContainer isLoading={isLoading} data={response} error={error}>
      <div className="w-100">
        <div className="d-flex flex-column">
          <div className={style.customCalendar}>
            {response &&
              response.calenderObj &&
              Object.keys(response.calenderObj).map(key => {
                return (
                  <div className={style.col} key={key}>
                    <div className={style.head}>{key.charAt(0)}</div>
                    {response.calenderObj[key].map((dataValue, index) => {
                      return (
                        <React.Fragment key={index}>
                          {dataValue.isPublicHoliday || key === 'Sa' || key === 'Su' ? (
                            <div className={[style.data, style.red].join(' ')}>
                              <div
                                className={style.box}
                                data-tip
                                data-tooltip-id={`toolTip-${index}${key}`}
                              >
                                {dataValue.date
                                  ? dayjs(dataValue.date)
                                      .utc()
                                      .format('D')
                                  : ''}
                              </div>
                            </div>
                          ) : (
                            <div className={[style.data].join(' ')}>
                              {dataValue.membersOnLeave && dataValue.membersOnLeave.length > 0 && (
                                <ReactTooltip
                                  className="customThemeToolTip"
                                  id={`toolTip-${index}${key}`}
                                  border={true}
                                  borderColor="rgb(124, 180, 235)"
                                  arrowColor="rgba(2, 7, 27, 1)"
                                  delayHide={300}
                                  delayUpdate={300}
                                >
                                  <Scrollbars autoHeight autoHeightMax={120}>
                                    <h3>On Leave: {dataValue.membersOnLeave.length}</h3>
                                    <ul>
                                      {dataValue.membersOnLeave.map((item, memberIndex) => {
                                        return (
                                          <li key={memberIndex}>
                                            <div className="label">{item.split('(')[0]}</div>
                                            <div className="value">{`(${item.split('(')[1]}`}</div>
                                          </li>
                                        )
                                      })}
                                    </ul>
                                  </Scrollbars>
                                </ReactTooltip>
                              )}
                              <div
                                className={style.box}
                                data-tip
                                data-for={`toolTip-${index}${key}`}
                              >
                                {dataValue.date
                                  ? dayjs(dataValue.date)
                                      .utc()
                                      .format('D')
                                  : ''}
                                {dataValue.isMemberLeave ? (
                                  <React.Fragment>
                                    {dataValue.memberLeaveHours === response.maxHourLostPerDay ? (
                                      <span className={[style.dot, style.red].join(' ')} />
                                    ) : (
                                      <span className={[style.dot, style.amber].join(' ')} />
                                    )}
                                  </React.Fragment>
                                ) : (
                                  ''
                                )}
                              </div>
                            </div>
                          )}
                        </React.Fragment>
                      )
                    })}
                  </div>
                )
              })}
          </div>
          <div className={style.calHoursLost}>
            <div className={[style.callData, style.calHoursHoliday].join(' ')}>
              <div className={style.number}>{response.hoursLostForHoliday}</div>
              <div className={style.text}>
                hours lost for holiday on{' '}
                <span className={style.red}>{response.publicHoliday}</span>
              </div>
            </div>
            <div className={[style.callData, style.calHoursLeave].join(' ')}>
              <div className={style.number}>{response.hoursLostForLeaves}</div>
              <div className={style.text}>hours lost for leaves</div>
            </div>
          </div>
        </div>
      </div>
    </WidgetContainer>
  )
})

const CalendarViewWidget = React.memo(({ apiEndPointUrl, data, callback }) => {
  return (
    <ModernBox animStatus="" boxTitle={'Sprint Calendar View'}>
      <CalendarView apiEndPointUrl={apiEndPointUrl} data={data} callback={callback} />
    </ModernBox>
  )
})

const propsTypesObj = {
  apiEndPointUrl: PropTypes.string,
  data: PropTypes.any,
  callback: PropTypes.func,
}

CalendarViewWidget.propTypes = propsTypesObj
CalendarView.propTypes = propsTypesObj

export default CalendarViewWidget
