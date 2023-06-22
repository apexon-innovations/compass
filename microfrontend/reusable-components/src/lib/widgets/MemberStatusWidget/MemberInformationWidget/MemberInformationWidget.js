import React from 'react'
import PropTypes from 'prop-types'
import { Row, Col } from 'react-bootstrap'

import MemberNestInformation from './MemberNestInformation'
import MemberJiraInformation from './MemberJiraInformation'

// Style
import style from './MemberInformationWidget.module.scss'

const MemberInformationWidget = React.memo(
  ({ memberJiraInfoApiEndPoint, memberNestInfoApiEndPoint, popUpData, boardId }) => {
    return (
      <div className={style.memberInfo}>
        <Row className="ml-0 mr-0">
          <Col md={4} sm={5} className="d-flex pl-0">
            <MemberNestInformation
              apiEndPointUrl={
                popUpData.memberId !== ''
                  ? `${memberNestInfoApiEndPoint}/${popUpData.memberId}/details`
                  : false
              }
              data={popUpData.memberId === '' ? popUpData : ''}
            />
          </Col>
          <Col md={8} sm={7} className="pr-0">
            <MemberJiraInformation
              apiEndPointUrl={`${memberJiraInfoApiEndPoint}/${popUpData.accountId}/status?boardId=${boardId}`}
            />
          </Col>
        </Row>
      </div>
    )
  },
)

MemberInformationWidget.propTypes = {
  memberJiraInfoApiEndPoint: PropTypes.string,
  memberNestInfoApiEndPoint: PropTypes.string,
  popUpData: PropTypes.object,
  boardId: PropTypes.string,
}

export default MemberInformationWidget
