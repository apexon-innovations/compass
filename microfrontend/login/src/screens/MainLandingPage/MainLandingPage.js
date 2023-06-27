import React from 'react'
import PropTypes from 'prop-types'
import { Row, Col, Container } from 'react-bootstrap'
import { ReactSVG } from 'react-svg'
import { LandingPageLinks } from './MainLandingPageFunctions'
import BracketBox from 'reusable-components/dist/components/CommonComponents/BracketBox/BracketBox'
import {
  getUserRoles,
  arraysEqual,
} from 'reusable-components/dist/utils/commonFunction'

import style from './MainLandingPage.module.scss'

const GetSection = ({ title, link, access }) => {
  const role = getUserRoles()
  return arraysEqual(role, access) ? (
    <Col className="d-flex" xl={4} lg={4} md={6} sm={12} xs={12}>
      <BracketBox animStatus="" boxTitleColor="" boxTitle={''}>
        <div
          className={[style.redirectBox, 'text-center'].join(' ')}
          id={`link-role-${link.linkTitle}`}
          title={[link.linkTitle + ' - ' + title]}
          onClick={() => {
            window.location = link.linkUrl
          }}
        >
          <div className={style.iconBox}>
            <ReactSVG src={`${link.icon.default}`} />
          </div>
          <div className={style.categoryName}>{link.linkTitle}</div>
          <div className={style.departmentName}>{title}</div>
        </div>
      </BracketBox>
    </Col>
  ) : (
    ''
  )
}

GetSection.propTypes = {
  title: PropTypes.string,
  link: PropTypes.any,
  icon: PropTypes.any,
  access: PropTypes.any,
}

const MainLandingPage = () => {
  return (
    <div className={['commonPage', style.mainLandingPage].join(' ')}>
      <Container>
        <div className={style.channelButtons}>
          <Row className="justify-content-center">
            {LandingPageLinks().map((item, key) => {
              return (
                <GetSection key={key} title={item.title} access={item.access} link={item.link} />
              )
            })}
          </Row>
        </div>
      </Container>
    </div>
  )
}

export default MainLandingPage
