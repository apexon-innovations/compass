import React, { PureComponent } from 'react'
import { Row, Col } from 'react-bootstrap'
import StaticPopupPage from 'components/StaticPopupPage/StaticPopupPage'
import { Scrollbars } from 'react-custom-scrollbars'
import { getStoredData } from 'reusable-components/dist/utils/projectDataStoreFunction'
import ProjectCompletionTrendsWidget from 'reusable-components/dist/widgets/ProjectCompletionTrendsWidget/ProjectCompletionTrendsWidget'
import CustomDropdown from 'reusable-components/dist/components/CommonComponents/CustomDropdown'

const pointCountDropdownOption = [
  { displayName: 'Points', value: 'Points' },
  { displayName: 'Count', value: 'Count' },
]

const monthOptions = [
  { displayName: 'Last Five Sprint', sprintCount: 5 },
  { displayName: 'Last Ten Sprint', sprintCount: 10 },
  { displayName: 'Last Fifteen Sprint', sprintCount: 15 },
]

class CompletionTrendsDrillDown extends PureComponent {
  constructor(props) {
    super(props)
    const projectData = getStoredData('PROJECT_CLIENT_DATA')
    this.state = {
      selectedPointCount: pointCountDropdownOption[0],
      selectedSprint: monthOptions[0],
      projectReRenderKey: 1,
      projectData: projectData || {},
    }
    this.renderProjectWiseGraph = this.renderProjectWiseGraph.bind(this)
  }

  renderProjectWiseGraph() {
    const baseUrl = process.env.REACT_APP_API_END_POINT

    const colLeft = []
    const colRight = []
    const { projectData, selectedPointCount, selectedSprint } = this.state

    for (let i = 0; i < projectData.length; i++) {
      if (i % 2 === 0) {
        colLeft.push(
          <ProjectCompletionTrendsWidget
            key={`first-${i}-${selectedSprint.sprintCount}`}
            apiEndPointUrl={`${baseUrl}/client-dashboard-service/projects/productCompletionTrend`}
            showHideButton={true}
            isFilter={false}
            zoomEnable={false}
            boxTitle={[projectData[i].name]}
            selectedPointCount={selectedPointCount.displayName}
            sprintCount={selectedSprint.sprintCount}
            projectId={projectData[i].id}
          />,
        )
      } else {
        colRight.push(
          <ProjectCompletionTrendsWidget
            key={`first-${i}-${selectedSprint.sprintCount}`}
            apiEndPointUrl={`${baseUrl}/client-dashboard-service/projects/productCompletionTrend`}
            showHideButton={true}
            isFilter={false}
            zoomEnable={false}
            boxTitle={[projectData[i].name]}
            selectedPointCount={selectedPointCount.displayName}
            sprintCount={selectedSprint.sprintCount}
            projectId={projectData[i].id}
          />,
        )
      }
    }

    return (
      <Row className="ml-0 mr-0">
        <Col md={6} sm={6} xs={12}>
          {colLeft}
        </Col>
        <Col md={6} sm={6} xs={12}>
          {colRight}
        </Col>
      </Row>
    )
  }

  render() {
    const baseUrl = process.env.REACT_APP_API_END_POINT
    return (
      <StaticPopupPage>
        <div className="titleArea">
          <h2 className="title">Project Completion Trends</h2>
          <div className="controlSpace">
            <CustomDropdown
              items={monthOptions}
              selectedOption={this.state.selectedSprint.displayName}
              onSelectCallback={selectData => {
                this.setState({
                  selectedSprint: monthOptions[selectData],
                })
              }}
              alignRight={true}
            />
            <CustomDropdown
              items={pointCountDropdownOption}
              selectedOption={this.state.selectedPointCount.displayName}
              onSelectCallback={selectData => {
                this.setState({
                  selectedPointCount: pointCountDropdownOption[selectData],
                })
              }}
              alignRight={true}
            />
          </div>
        </div>
        <Scrollbars autoHeight autoHeightMax={`calc(85vh - 110px)`}>
          <div className="graphHolderWithBg">
            <ProjectCompletionTrendsWidget
              key={`second-${this.state.selectedSprint.sprintCount}`}
              apiEndPointUrl={`${baseUrl}/client-dashboard-service/projects/productCompletionTrend`}
              selectedPointCount={this.state.selectedPointCount.displayName}
              isFilter={true}
              zoomEnable={false}
              bracketBox={false}
              sprintCount={this.state.selectedSprint.sprintCount}
            />
          </div>
          <div className="gap50" />
          <div className="graphHolder">{this.renderProjectWiseGraph()}</div>
        </Scrollbars>
      </StaticPopupPage>
    )
  }
}

CompletionTrendsDrillDown.propTypes = {}

export default CompletionTrendsDrillDown
