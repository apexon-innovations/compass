import React, { useState } from 'react'
import PropTypes from 'prop-types'
import { Scrollbars } from 'react-custom-scrollbars'
import WidgetContainer from '../../../components/WidgetContainer/WidgetContainer'
import useWidgetAPIWrapper from '../../../components/WidgetContainer/useWidgetAPIWrapper'
import style from './MemberInformationWidget.module.scss'

const jiraResponseMap = response => {
  return response && response.data ? response.data.member : {}
}

const filterByTicketStatusData = (response, key) => {
  let data = [...response]
  data = data.filter(item => {
    return item.status === key ? true : false
  })
  return data
}

const MemberJiraInformation = React.memo(({ apiEndPointUrl, data, callback }) => {
  const { response, error, isLoading } = useWidgetAPIWrapper({
    apiEndPointUrl,
    data,
    callback,
    responseMapFunction: jiraResponseMap,
  })
  const [statusFilter, setStatusFilter] = useState('')

  let taskList = false
  if (response) {
    taskList =
      statusFilter === '' ? response.tasks : filterByTicketStatusData(response.tasks, statusFilter)
  }

  return (
    <WidgetContainer isLoading={isLoading} data={response} error={error}>
      <div className={style.workDetails}>
        {response && response.status && (
          <div className={style.taskStatus}>
            <StatusBox
              value={response.status.todo}
              label={'To Do'}
              callback={setStatusFilter}
              filterValue={'To Do'}
              statusFilter={statusFilter}
            />
            <StatusBox
              value={response.status.inProgress}
              label={'In-Progress'}
              callback={setStatusFilter}
              filterValue={'In Progress'}
              statusFilter={statusFilter}
            />
            <StatusBox
              value={response.status.completed}
              label={'Completed'}
              callback={setStatusFilter}
              filterValue={'Completed'}
              statusFilter={statusFilter}
            />
          </div>
        )}
        <div className={style.taskComments}>
          {taskList && taskList.length > 0 ? (
            <Scrollbars autoHeight autoHeightMax={100}>
              <ol>
                {taskList.map((item, index) => {
                  return (
                    <li key={index}>
                      <a href={item.url} rel="noopener noreferrer" target="_blank">
                        {item.number} {item.name}
                      </a>
                    </li>
                  )
                })}
              </ol>
            </Scrollbars>
          ) : (
            <div className={style.noData}>Sorry, No Record Available.</div>
          )}
        </div>
      </div>
      {response.status && (
        <div className={style.totalSummary}>
          <div className={[style.status, style.red].join(' ')}>
            <div className={style.number}>
              <span className={style.span}>{response.status.availableHours}</span>
              <div className={style.highlight}></div>
            </div>
            <div className={style.label}>Total Hours Left</div>
          </div>
          <div className={style.status}>
            <div className={style.number}>
              <span className={style.span}>{response.status.averageStoryPoints}</span>
              <div className={style.highlight}></div>
            </div>
            <div className={style.label}>Average Story Point</div>
          </div>
          <div className={style.status}>
            <div className={style.number}>
              <span className={style.span}>{response.status.spilledOver || 0}</span>
              <div className={style.highlight}></div>
            </div>
            <div className={style.label}>Spill Over</div>
          </div>
          <div className={[style.status, 'blurAll d-none'].join(' ')}>
            <div className={style.number}>
              <span className={style.span}>RnR</span>
              <div className={style.highlight}></div>
            </div>
            <div className={style.label}>Nominate</div>
          </div>
        </div>
      )}
    </WidgetContainer>
  )
})

const StatusBox = ({ value, label, callback, statusFilter, filterValue }) => {
  return (
    <div
      className={[style.status, statusFilter === filterValue ? style.active : ''].join(' ')}
      onClick={() => {
        statusFilter === filterValue ? callback('') : callback(filterValue)
      }}
    >
      <div className={style.number}>
        <span className={style.span}>{value}</span>
      </div>
      <div className={style.label}>{label}</div>
    </div>
  )
}

MemberJiraInformation.propTypes = {
  apiEndPointUrl: PropTypes.string,
  data: PropTypes.any,
  callback: PropTypes.func,
}

StatusBox.propTypes = {
  value: PropTypes.string,
  label: PropTypes.any,
  callback: PropTypes.any,
  statusFilter: PropTypes.string,
  filterValue: PropTypes.string,
}

export default MemberJiraInformation
