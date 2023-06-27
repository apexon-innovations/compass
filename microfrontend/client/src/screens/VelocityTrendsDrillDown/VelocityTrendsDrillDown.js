import React, { PureComponent } from 'react'
import { Row, Col } from 'react-bootstrap'
import { Scrollbars } from 'react-custom-scrollbars'
import VelocityTrendsWidget from 'reusable-components/dist/widgets/VelocityTrendsWidget/VelocityTrendsWidget'
import VelocityTrendsProjectWidget from 'reusable-components/dist/widgets/VelocityTrendsWidget/VelocityTrendsProjectWidget'
import CustomDropdown from 'reusable-components/dist/components/CommonComponents/CustomDropdown'
import { getStoredData } from 'reusable-components/dist/utils/projectDataStoreFunction'
import StaticPopupPage from 'components/StaticPopupPage/StaticPopupPage'

const pointCountDropdownOption = [
  { displayName: 'Count', value: 'count' },
  { displayName: 'Points', value: 'points' },
]

const sprintSelectDropdown = [
  { displayName: 'Previous 5', value: 5 },
  { displayName: 'Previous 10', value: 10 },
  { displayName: 'Previous 15', value: 15 },
  { displayName: 'Previous 20', value: 20 },
  { displayName: 'Previous 25', value: 25 },
]

class VelocityTrendsDrillDown extends PureComponent {
  constructor(props) {
    super(props)
    const projectData = getStoredData('PROJECT_CLIENT_DATA')
    this.state = {
      selectedPointCount: pointCountDropdownOption[1],
      selectedSprint: sprintSelectDropdown[0],
      projectReRenderKey: 1,
      projectData: projectData || {},
    }
  }

  renderProjectWiseGraph() {
    const colLeft = []
    const colRight = []
    const { projectData, selectedPointCount } = this.state

    for (let i = 0; i < projectData.length; i++) {
      if (i % 2 === 0) {
        colLeft.push(
          <VelocityTrendsProjectWidget
            selectedPointCount={selectedPointCount}
            apiEndPointUrl={`/client-dashboard-service/projects/velocityTrends?iscProjectIds=${projectData[i].id}&sprintCount=${this.state.selectedSprint.value}`}
          />,
        )
      } else {
        colRight.push(
          <VelocityTrendsProjectWidget
            selectedPointCount={selectedPointCount}
            apiEndPointUrl={`/client-dashboard-service/projects/velocityTrends?iscProjectIds=${projectData[i].id}&sprintCount=${this.state.selectedSprint.value}`}
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
    return (
      <StaticPopupPage>
        <div className="titleArea">
          <h2 className="title">Velocity Trends</h2>
          <div className="controlSpace">
            <CustomDropdown
              items={sprintSelectDropdown}
              selectedOption={this.state.selectedSprint.displayName}
              onSelectCallback={selectData => {
                this.setState({
                  selectedSprint: sprintSelectDropdown[selectData],
                })
              }}
              alignRight="alignRight"
              bordered={true}
            />
            <CustomDropdown
              items={pointCountDropdownOption}
              selectedOption={this.state.selectedPointCount.displayName}
              onSelectCallback={selectData => {
                this.setState({
                  selectedPointCount: pointCountDropdownOption[selectData],
                })
              }}
              alignRight="alignRight"
              bordered={true}
            />
          </div>
        </div>
        <Scrollbars autoHeight autoHeightMax={`calc(85vh - 110px)`}>
          <div className="graphHolderWithBg">
            <VelocityTrendsWidget
              key={this.state.selectedSprint.value}
              selectedPointCount={this.state.selectedPointCount.displayName}
              selectedSprint={this.state.selectedSprint.value}
              apiEndPointUrl={`${baseUrl}/client-dashboard-service/projects`}
              isFilter={true}
              zoomEnable={false}
            />
          </div>
          <div className="gap30" />
          <div className="graphHolder">{this.renderProjectWiseGraph()}</div>
        </Scrollbars>
      </StaticPopupPage>
    )
  }
}

export default VelocityTrendsDrillDown
