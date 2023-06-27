import React, { Component } from 'react'
import { Container } from 'react-bootstrap'
import CodeHealthSnapshotWidget from 'reusable-components/dist/widgets/CodeHealthSnapshotWidget/CodeHealthSnapshotWidget'
import style from './ClientBusinessDashboard.module.scss'

class ClientBusinessDashboard extends Component {
  render() {
    const baseUrl = process.env.REACT_APP_API_END_POINT
    return (
      <div className={[style.clientBusinessDashboard].join(' ')}>
        <Container className="container1400 animated fadeInUp">
          <CodeHealthSnapshotWidget
            apiEndPointUrl={`${baseUrl}/client-dashboard-service/projects/codeHealth`}
          />
        </Container>
      </div>
    )
  }
}

export default ClientBusinessDashboard
