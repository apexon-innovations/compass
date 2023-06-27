import React, { useRef } from 'react'
import PropTypes from 'prop-types'
import { Table } from 'react-bootstrap'
import { Scrollbars } from 'react-custom-scrollbars'
import ModalContainer from '../../components/ModalContainer/ModalContainer'
import MemberInformationWidget from './MemberInformationWidget/MemberInformationWidget'
import WidgetContainer from '../../components/WidgetContainer/WidgetContainer'
import useWidgetAPIWrapper from '../../components/WidgetContainer/useWidgetAPIWrapper'

import { ModernBox } from '../../components'

// Style
import style from './MemberStatusWidget.module.scss'

const responseMap = response => {
  return response && response.data ? response.data.members : []
}

const MemberStatusWidget = React.memo(
  ({
    apiEndPointUrl,
    data,
    callback,
    memberJiraInfoApiEndPoint,
    memberNestInfoApiEndPoint,
    boardId,
  }) => {
    let { response, error, isLoading } = useWidgetAPIWrapper({
      apiEndPointUrl,
      data,
      callback,
      responseMapFunction: responseMap,
    })
    const modelRef = useRef()

    if (error && Object.keys(error).length === 0 && response.length === 0) {
      error = {
        message: 'Sorry, No Members Data Available.',
        errorCode: 404,
        isMsgPassed: true,
      }
    }

    return (
      <div className={style.memberStatusBox}>
        <ModernBox animStatus="" boxTitle={'Member Status'}>
          <WidgetContainer isLoading={isLoading} data={response} error={error}>
            <Scrollbars autoHeight autoHeightMax={180}>
              <Table responsive borderless className={style.memberStatus}>
                <thead>
                  <tr>
                    <th className={style.firstColumn}>Members ({response.length})</th>
                    <th>To Dos</th>
                    <th>In-Progress</th>
                    <th>Completed</th>
                    <th>Available Hours</th>
                  </tr>
                </thead>
                <tbody>
                  {response.length > 0 &&
                    response.map((item, index) => {
                      return (
                        <tr key={index}>
                          <td
                            className={style.firstColumn}
                            onClick={() => {
                              modelRef.current.handleModelToggle(item)
                            }}
                          >
                            {item.name}
                          </td>
                          <td>{item.todo}</td>
                          <td>{item.inProgress}</td>
                          <td className="blue">{item.completed}</td>
                          <td className="red">{item.availableHours}</td>
                        </tr>
                      )
                    })}
                </tbody>
              </Table>
            </Scrollbars>
          </WidgetContainer>
        </ModernBox>

        <ModalContainer ref={modelRef}>
          <MemberInformationWidget
            memberJiraInfoApiEndPoint={memberJiraInfoApiEndPoint}
            memberNestInfoApiEndPoint={memberNestInfoApiEndPoint}
            boardId={boardId}
          />
        </ModalContainer>
      </div>
    )
  },
)

MemberStatusWidget.propTypes = {
  apiEndPointUrl: PropTypes.string,
  memberJiraInfoApiEndPoint: PropTypes.string,
  memberNestInfoApiEndPoint: PropTypes.string,
  data: PropTypes.any,
  callback: PropTypes.func,
  popUpData: PropTypes.object,
  boardId: PropTypes.string,
}

export default MemberStatusWidget
