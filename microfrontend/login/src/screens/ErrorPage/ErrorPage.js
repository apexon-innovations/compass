import React from 'react'
import style from './ErrorPage.module.scss'
import { Container, Row, Col } from 'react-bootstrap'
import PropTypes from 'prop-types'
import { withRouter } from 'reusable-components/dist/components/withRouter/withRouter'

function ErrorPage(props) {
  // eslint-disable-next-line no-unused-vars
  setTimeout(() => {
    props.navigate('/login')
  }, 2000)

  return (
    <div className={style.loginPage}>
      <Container fluid>
        <Row>
          <Col md={6} xs={12} className="p-0">
            <div>You are not authorized to view this page..!</div>
          </Col>
        </Row>
      </Container>
    </div>
  )
}

ErrorPage.propTypes = {
  navigate: PropTypes.any,
}

export default  withRouter(ErrorPage)
