import React, { PureComponent } from 'react'
import { Container, Row, Col } from 'react-bootstrap'
import { LOGO_INFOSTRETCH } from '../../const/imgConst.js'

// Style
import style from './Footer.module.scss'

class Footer extends PureComponent {
  render() {
    return (
      <div className={style.footer}>
        <Container fluid>
          <Row>
            <Col className={style.leftSide}>
              &copy; {new Date().getFullYear()} Infostretch Corporation. All rights reserved.
            </Col>
            <Col className={style.rightSide}>
              <div className={style.poweredBy}>
                <div className={style.text}>Powered by:</div>
                <div className={style.img}>
                  <img src={LOGO_INFOSTRETCH} title="" alt="" />
                </div>
              </div>
            </Col>
          </Row>
        </Container>
      </div>
    )
  }
}

export default Footer
