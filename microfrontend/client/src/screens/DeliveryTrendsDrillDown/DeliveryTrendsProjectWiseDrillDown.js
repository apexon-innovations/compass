import React, { PureComponent } from 'react'
import PropTypes from 'prop-types'
import { Row, Col } from 'react-bootstrap'
import MultiSelect from '@khanacademy/react-multi-select'
import StaticPopupPage from 'components/StaticPopupPage/StaticPopupPage'
import StoryPointsDelTrendProjectSprintWidget from 'reusable-components/dist/widgets/StoryPointsDelTrendWidget/StoryPointsDelTrendProjectSprintWidget'
import CustomDropdown from 'reusable-components/dist/components/CommonComponents/CustomDropdown'

import style from './DeliveryTrendsProjectWiseDrillDown.module.scss'
import { withRouter } from 'reusable-components/dist/components/withRouter/withRouter'

const pointCountDropdownOption = [
  { displayName: 'Points', value: 'Points' },
  { displayName: 'Count', value: 'Count' },
]
const sprintDropdownOption = [
  { label: 'Current Sprint', value: 0 },
  { label: 'Prev Sprint 1', value: 1 },
  { label: 'Prev Sprint 2', value: 2 },
  { label: 'Prev Sprint 3', value: 3 },
  { label: 'Prev Sprint 4', value: 4 },
]
class DeliveryTrendsProjectWiseDrillDown extends PureComponent {
  constructor(props) {
    super(props)
    this.state = {
      selectedPointCount: pointCountDropdownOption[0],
      projectReRenderKey: 1,
      selectedOption: [],
      selectedValues: [],
    }
    this.renderProjectWiseGraph = this.renderProjectWiseGraph.bind(this)
  }

  renderProjectWiseGraph() {
    const baseUrl = process.env.REACT_APP_API_END_POINT

    const colLeft = []
    const colRight = []
    const { selectedPointCount } = this.state
    for (let i = 0; i < sprintDropdownOption.length - 1; i++) {
      if (i % 2 === 0) {
        colLeft.push(
          <StoryPointsDelTrendProjectSprintWidget
            key={sprintDropdownOption[i] + i}
            selectedPointCount={selectedPointCount.value}
            apiEndPointUrl={`${baseUrl}/client-dashboard-service/projects/consolidatedStoryPointDeliveryTrends?iscProjectIds=${
              this.props.params.projectId
            }&sprintNumber=${i + 1}`}
            showHideButton={true}
            idealLine={true}
            backLink={false}
            boxTitle={[`Prev Sprint ${i + 1}`]}
            colorCodeIndex={i + 1}
          />,
        )
      } else {
        colRight.push(
          <StoryPointsDelTrendProjectSprintWidget
            key={sprintDropdownOption[i] + i}
            selectedPointCount={selectedPointCount.value}
            apiEndPointUrl={`${baseUrl}/client-dashboard-service/projects/consolidatedStoryPointDeliveryTrends?iscProjectIds=${
              this.props.params.projectId
            }&sprintNumber=${i + 1}`}
            showHideButton={true}
            idealLine={true}
            backLink={false}
            boxTitle={[`Prev Sprint ${i + 1}`]}
            colorCodeIndex={i + 1}
          />,
        )
      }
    }

    return (
      <Row className="ml-0 mr-0">
        <Col className="pl-0" key={'colLeft'}>
          {colLeft}
        </Col>
        <Col className="pl-0" key={'colRight'}>
          {colRight}
        </Col>
      </Row>
    )
  }

  render() {
    const baseUrl = process.env.REACT_APP_API_END_POINT
    let redirectUrl = '/client/overview'
    if (this.props?.location?.state?.viewType === 'drillDown') {
      redirectUrl = '/client/client-delivery-trends'
    }
    return (
      <StaticPopupPage redirectUrl={redirectUrl}>
        <div className="titleArea">
          <h2 className="title">Story Point Delivery Trends</h2>
          <div className="controlSpace">
            <div className={['transparentMultiSelect', style.multiSelect].join(' ')}>
              <MultiSelect
                key={this.state.selectedOption.length}
                options={sprintDropdownOption}
                selected={this.state.selectedOption}
                onSelectedChanged={selected => {
                  let selectedValues = []
                  selected.sort()
                  sprintDropdownOption.map((item, index) => {
                    if (selected.indexOf(index) > -1) selectedValues.push(item.label)
                    return item
                  })

                  this.setState({
                    selectedOption: selected,
                    selectedValues: selectedValues,
                  })
                }}
                valueRenderer={selected => {
                  return selected.length === 0 ? 'Select Sprint' : `(${selected.length})`
                }}
                overrideStrings={{
                  selectSomeItems: 'Select Sprint',
                  allItemsAreSelected: 'All Sprints are Selected',
                  selectAll: 'Select All Sprints',
                  search: 'Search Sprints',
                }}
                alignRight={true}
              />
            </div>
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
        <div className="graphHolderWithBg">
          {this.state.selectedOption.length === 0 ? (
            <StoryPointsDelTrendProjectSprintWidget
              key={this.props.params.projectId + this.state.selectedOption.length}
              selectedPointCount={this.state.selectedPointCount.value}
              apiEndPointUrl={`${baseUrl}/client-dashboard-service/projects/consolidatedStoryPointDeliveryTrends?iscProjectIds=${this.props.params.projectId}&sprintNumber=0`}
              showHideButton={false}
              idealLine={true}
              backLink={true}
              boxTitle={[`Current Sprint`]}
              colorCodeIndex={6}
            />
          ) : (
            <StoryPointsDelTrendProjectSprintWidget
              key={this.props.params.projectId + this.state.selectedOption.length}
              selectedPointCount={this.state.selectedPointCount.value}
              // apiEndPointUrl={`${baseUrl}/client-dashboard-service/projects/${
              //   this.props.params.projectId
              //   }/sprint/${
              //   this.state.selectedOption.length > 0 ? this.state.selectedOption.toString() : 0
              //   }/storyPointDeliveryTrends`}
              apiEndPointUrl={`${baseUrl}/client-dashboard-service/projects/storyPointDeliveryTrends/?iscProjectId=${
                this.props.params.projectId
              }&sprintCount=${
                this.state.selectedOption.length > 0 ? this.state.selectedOption.toString() : 0
              }`}
              showHideButton={false}
              boxTitle={this.state.selectedValues}
              idealLine={false}
              backLink={true}
            />
          )}
        </div>
        <div className="gap50" />
        <div className="graphHolder">{this.renderProjectWiseGraph()}</div>
      </StaticPopupPage>
    )
  }
}

DeliveryTrendsProjectWiseDrillDown.propTypes = {
  params: PropTypes.any,
  location: PropTypes.any,
}

export default withRouter(DeliveryTrendsProjectWiseDrillDown)
