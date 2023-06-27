import React, { PureComponent } from 'react'
import { Row, Col } from 'react-bootstrap'
import { withRouter } from 'reusable-components/dist/components/withRouter/withRouter';
import { Scrollbars } from 'react-custom-scrollbars'
import { Tooltip as ReactTooltip } from 'react-tooltip'
import { getStoredData } from 'reusable-components/dist/utils/projectDataStoreFunction'
import { IC_INFO } from 'reusable-components/dist/const/imgConst'
import StaticPopupPage from '../../components/StaticPopupPage/StaticPopupPage'
import DefectAgeingWidget from 'reusable-components/dist/widgets/DefectAgeingWidget/DefectAgeingWidget'
import DefectAgeingProjectWiseWidget from 'reusable-components/dist/widgets/DefectAgeingProjectWiseWidget/DefectAgeingProjectWiseWidget'
import ModernBox from 'reusable-components/dist/components/CommonComponents/ModernBox/ModernBox'

class DefectAgeingDrillDown extends PureComponent {
  constructor(props) {
    super(props)
    const projectData = getStoredData('PROJECT_CLIENT_DATA')
    this.state = {
      projectReRenderKey: 1,
      projectData: projectData || {},
    }
  }

  renderProjectWiseGraph() {
    const colLeft = []
    const colRight = []
    const baseUrl = process.env.REACT_APP_API_END_POINT

    const { projectData } = this.state

    for (let i = 0; i < projectData.length; i++) {
      if (i % 2 === 0) {
        colLeft.push(
          <DefectAgeingProjectWiseWidget
            apiEndPointUrl={`${baseUrl}/client-dashboard-service/projects/defects/ageing/stats?iscProjectId=${projectData[i].id}`}
            showHideEnable={true}
            needBracketBox={true}
            title={projectData[i].name}
          />,
        )
      } else {
        colRight.push(
          <DefectAgeingProjectWiseWidget
            apiEndPointUrl={`${baseUrl}/client-dashboard-service/projects/defects/ageing/stats?iscProjectId=${projectData[i].id}`}
            showHideEnable={true}
            needBracketBox={true}
            title={projectData[i].name}
          />,
        )
      }
    }

    return (
      <Row className="ml-0 mr-0">
        <Col className="pl-0">{colLeft}</Col>
        <Col className="pl-0">{colRight}</Col>
      </Row>
    )
  }

  render() {
    const baseUrl = process.env.REACT_APP_API_END_POINT
    const projectIds = this.state.projectData.map(item => {
      return item.id
    })
    return (
      <div>
        <StaticPopupPage>
          <div className="titleArea">
            <h2 className="title">
              Defect Ageing Trends
              <div className="info" to="" data-tooltip-place="bottom" data-tip data-tooltip-id="info1">
                <img src={IC_INFO} title="" alt="" />
              </div>
              <ReactTooltip
                className="simpleTooltip"
                id="info1"
                border={true}
                borderColor="#35445f"
                arrowColor="#040a23"
              >
                <div className="infoContent">
                  <p>Defect Ageing Trends view</p>
                </div>
              </ReactTooltip>
            </h2>
          </div>
          <Scrollbars autoHeight autoHeightMax={`calc(85vh - 100px)`}>
            <ModernBox boxTitle={'Consolidate View'}>
              <div className="w-100 d-flex flex-column">
                <DefectAgeingWidget
                  apiEndPointUrl={`${baseUrl}/client-dashboard-service/projects/defects/ageing?iscProjectIds=`}
                  projectIds={projectIds}
                  zoomEnable={false}
                  bracketEnable={false}
                  isFilter={true}
                  areaChart={true}
                />
              </div>
            </ModernBox>
            <div className="gap30" />
            <div className="graphHolder">{this.renderProjectWiseGraph()}</div>
          </Scrollbars>
        </StaticPopupPage>
      </div>
    )
  }
}

export default withRouter(DefectAgeingDrillDown)
