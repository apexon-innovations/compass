import React from 'react'
import { Row, Col } from 'react-bootstrap'
import PropTypes from 'prop-types'
import PRsMgmtPieChartWidget from '../PRsMgmtPieChartWidget/PRsMgmtPieChartWidget'
import PRsManagementWidget from '../PRsManagementWidget/PRsManagementWidget'
import SubmitterMetricsWidget from '../SubmitterMetricsWidget/SubmitterMetricsWidget'
import SprintIdWidgetContainer from '../../components/WidgetContainer/SprintIdWidgetContainer'
import ParentTabSection from '../../components/CommonComponents/ParentTabSection'
import { CustomTab } from '../../components/CommonComponents/ParentTabSection'
import BracketBox from '../../components/CommonComponents/BracketBox/BracketBox'
import style from './PRmgmtWidget.module.scss'

const PRmgmtWidget = React.memo(props => {
  return (
    <div className={style.PRmgmtSumitterMatrics}>
      <BracketBox verticalAlignment="alignTop" animStatus="" boxTitle={''}>
        <ParentTabSection defaultActiveKey={0}>
          <CustomTab title="PRs Management">
            <div className={style.prsManagement}>
              <Row className="w-100 ml-0 h-100 mr-0">
                <Col md={5}>
                  <SprintIdWidgetContainer sprintId={props.repoId}>
                    <PRsMgmtPieChartWidget
                      key={props.repoId}
                      apiEndPointUrl={props.apiEndPointUrlOfPRPieChart}
                    />
                  </SprintIdWidgetContainer>
                </Col>
                <Col md={7}>
                  <SprintIdWidgetContainer sprintId={props.repoId}>
                    <PRsManagementWidget
                      key={props.repoId}
                      apiEndPointUrl={props.apiEndPointUrlOfPRMgmt}
                    />
                  </SprintIdWidgetContainer>
                </Col>
              </Row>
            </div>
          </CustomTab>
          <CustomTab title="Submitter Metrics">
            <SprintIdWidgetContainer sprintId={props.repoId}>
              <SubmitterMetricsWidget
                key={props.repoId}
                apiEndPointUrl={props.apiEndPointUrlOfSubmitterMetrics}
              />
            </SprintIdWidgetContainer>
          </CustomTab>
        </ParentTabSection>
      </BracketBox>
    </div>
  )
})

PRmgmtWidget.propTypes = {
  apiEndPointUrlOfPRPieChart: PropTypes.string,
  apiEndPointUrlOfPRMgmt: PropTypes.string,
  apiEndPointUrlOfSubmitterMetrics: PropTypes.string,
  repoId: PropTypes.string,
}

export default PRmgmtWidget
