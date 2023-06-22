import React, { PureComponent } from 'react'
import { Row, Col } from 'react-bootstrap'
import StaticPopupPage from 'components/StaticPopupPage/StaticPopupPage'
import { Scrollbars } from 'react-custom-scrollbars'
import { getStoredData } from 'reusable-components/dist/utils/projectDataStoreFunction'
import StoryPointsDelTrendWidget from 'reusable-components/dist/widgets/StoryPointsDelTrendWidget/StoryPointsDelTrendWidget'
import StoryPointsDelTrendProjectWidget from 'reusable-components/dist/widgets/StoryPointsDelTrendWidget/StoryPointsDelTrendProjectWidget'
import CustomDropdown from 'reusable-components/dist/components/CommonComponents/CustomDropdown'

const pointCountDropdownOption = [
  { displayName: 'Points', value: 'Points' },
  { displayName: 'Count', value: 'Count' },
]

class DeliveryTrendsDrillDown extends PureComponent {
  constructor(props) {
    super(props)
    const projectData = getStoredData('PROJECT_CLIENT_DATA')
    this.state = {
      selectedPointCount: pointCountDropdownOption[0],
      projectReRenderKey: 1,
      projectData: projectData || {},
    }
    this.renderProjectWiseGraph = this.renderProjectWiseGraph.bind(this)
  }

  renderProjectWiseGraph() {
    const baseUrl = process.env.REACT_APP_API_END_POINT

    const colLeft = []
    const colRight = []
    const { projectData, selectedPointCount } = this.state

    for (let i = 0; i < projectData.length; i++) {
      if (i % 2 === 0) {
        colLeft.push(
          <StoryPointsDelTrendProjectWidget
            key={projectData[i].jiraProjectId}
            selectedPointCount={selectedPointCount.value}
            apiEndPointUrl={`${baseUrl}/client-dashboard-service/projects/storyPointDeliveryTrends?iscProjectId=${projectData[i].id}&sprintCount=0,1,2,3,4`}
          />,
        )
      } else {
        colRight.push(
          <StoryPointsDelTrendProjectWidget
            key={projectData[i].jiraProjectId}
            selectedPointCount={selectedPointCount.value}
            apiEndPointUrl={`${baseUrl}/client-dashboard-service/projects/storyPointDeliveryTrends?iscProjectId=${projectData[i].id}&sprintCount=0,1,2,3,4`}
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
          <h2 className="title">Story Point Delivery Trends</h2>
          <div className="controlSpace">
            <CustomDropdown
              items={pointCountDropdownOption}
              selectedOption={this.state.selectedPointCount.displayName}
              onSelectCallback={selectData => {
                this.setState({
                  selectedPointCount: pointCountDropdownOption[selectData],
                })
              }}
              alignRight={true}
              bordered={true}
            />
          </div>
        </div>
        <Scrollbars autoHeight autoHeightMax={`calc(85vh - 110px)`}>
          <div className="graphHolderWithBg">
            <StoryPointsDelTrendWidget
              selectedSprint={0}
              apiEndPointUrl={`${baseUrl}/client-dashboard-service/projects/consolidatedStoryPointDeliveryTrends`}
              selectedPointCount={this.state.selectedPointCount.value}
              zoomEnable={false}
              isFilter={true}
              projectDetailsLinkUrl={'/client/client-delivery-trends/'}
              viewType={'drillDown'}
            />
          </div>
          <div className="gap50" />
          <div className="graphHolder">{this.renderProjectWiseGraph()}</div>
        </Scrollbars>
      </StaticPopupPage>
    )
  }
}

export default DeliveryTrendsDrillDown
