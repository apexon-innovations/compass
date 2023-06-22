import React, { useState } from 'react'
import PropTypes from 'prop-types'
import { Tabs, Tab } from 'react-bootstrap'
import { Scrollbars } from 'react-custom-scrollbars'
import dayjs from 'dayjs'
import utc from 'dayjs/plugin/utc'

// Style
import style from './ComplianceWidget.module.scss'

dayjs.extend(utc)

const StatusCircle = ({ label, value, onClickCallBack, activeStatus, filterStatus }) => {
  return (
    <div
      className={[style.status, label === activeStatus ? style.active : ''].join(' ')}
      onClick={() => {
        onClickCallBack(label, filterStatus)
      }}
    >
      <div className={style.number}>
        <span className={style.span}>{value}</span>
      </div>
      <div className={style.label}>{label}</div>
    </div>
  )
}

const ModalBoxSection = ({ data }) => {
  return (
    <div className={style.taskCommentsWrapper}>
      {data.length > 0 ? (
        <ol>
          {' '}
          {data.map((item, index) => {
            return (
              <li key={index}>
                <div className={style.data}>
                  <a href={item.url} rel="noopener noreferrer" target="_blank">
                    {item.number} {item.name} ({item.type})
                  </a>
                </div>
                {item.ncDueDate ? (
                  <div className={style.dueDate}>
                    Due:{' '}
                    {dayjs(item.ncDueDate)
                      .utc()
                      .format('MM/DD/YYYY')}
                  </div>
                ) : (
                  ''
                )}
              </li>
            )
          })}{' '}
        </ol>
      ) : (
        <div className={style.noData}>Sorry, No Record Available.</div>
      )}
    </div>
  )
}

const filterTicketsByStatus = (data, filterStatus) => {
  const filterData = data.filter(item => {
    return filterStatus.indexOf(item.status) > -1
  })
  return filterData
}

const ModalComplianceBox = React.memo(({ response }) => {
  const [filterResponse, setFilterResponse] = useState({
    tickets: response.tickets,
    observations: response.observations,
    activeStatus: false,
  })
  const callBackSetActiveStatus = (status, filterStatus) => {
    if (filterResponse.activeStatus === status) {
      setFilterResponse({
        tickets: response.tickets,
        observations: response.observations,
        activeStatus: false,
      })
    } else {
      setFilterResponse({
        tickets: filterTicketsByStatus(response.tickets, filterStatus),
        observations: filterTicketsByStatus(response.observations, filterStatus),
        activeStatus: status,
      })
    }
  }

  return (
    <div className={style.modalComplianceBox}>
      {response && (
        <Tabs
          className="blackNWhite"
          defaultActiveKey="tabCompTickets"
          onSelect={() => callBackSetActiveStatus(filterResponse.activeStatus)}
        >
          <Tab eventKey="tabCompTickets" title="Tickets">
            <div className={style.workDetails}>
              <div className={style.taskStatus}>
                <StatusCircle
                  label={'To-Dos'}
                  value={response.complianceSummary.openTickets}
                  onClickCallBack={callBackSetActiveStatus}
                  activeStatus={filterResponse.activeStatus}
                  filterStatus={['Open', 'Reopened']}
                />
                <StatusCircle
                  label={'In-Progress'}
                  value={filterTicketsByStatus(response.tickets, ['In Progress']).length}
                  onClickCallBack={callBackSetActiveStatus}
                  activeStatus={filterResponse.activeStatus}
                  filterStatus={['In Progress']}
                />
                <StatusCircle
                  label={'Completed'}
                  value={response.complianceSummary.closedTickets}
                  onClickCallBack={callBackSetActiveStatus}
                  activeStatus={filterResponse.activeStatus}
                  filterStatus={['Closed', 'Resolved']}
                />
              </div>
              <div className={style.taskComments}>
                <Scrollbars autoHeight autoHeightMax={100}>
                  <ModalBoxSection data={filterResponse.tickets} />
                </Scrollbars>
              </div>
            </div>
          </Tab>
          <Tab eventKey="tabCompObservations" title="Observations">
            <div className={style.workDetails}>
              <div className={style.taskStatus}>
                <StatusCircle
                  label={'Open'}
                  value={response.complianceSummary.openObservations}
                  onClickCallBack={callBackSetActiveStatus}
                  activeStatus={filterResponse.activeStatus}
                  filterStatus={['Open', 'Reopened']}
                />
                <StatusCircle
                  label={'Addressed'}
                  value={response.complianceSummary.closedObservations}
                  onClickCallBack={callBackSetActiveStatus}
                  activeStatus={filterResponse.activeStatus}
                  filterStatus={['Closed', 'Resolved']}
                />
              </div>
              <div className={style.taskComments}>
                <Scrollbars autoHeight autoHeightMax={100}>
                  <ModalBoxSection data={filterResponse.observations} />
                </Scrollbars>
              </div>
            </div>
          </Tab>
        </Tabs>
      )}
    </div>
  )
})

StatusCircle.propTypes = {
  label: PropTypes.string,
  value: PropTypes.number,
  activeStatus: PropTypes.any,
  onClickCallBack: PropTypes.func,
  filterStatus: PropTypes.array,
}

ModalBoxSection.propTypes = {
  data: PropTypes.any,
}

ModalComplianceBox.propTypes = {
  response: PropTypes.any,
}
export default ModalComplianceBox
