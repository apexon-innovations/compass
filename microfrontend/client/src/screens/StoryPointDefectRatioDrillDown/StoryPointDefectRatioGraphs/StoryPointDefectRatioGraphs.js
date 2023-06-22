import React, { PureComponent } from 'react'
import PropTypes from 'prop-types'
import { Row, Col } from 'react-bootstrap'
import { withRouter } from 'reusable-components/dist/components/withRouter/withRouter';import { Scrollbars } from 'react-custom-scrollbars'
import StaticPopupPage from 'components/StaticPopupPage/StaticPopupPage'
import { IC_BACK_ARROW } from 'reusable-components/dist/const/imgConst'
import { getStoredData } from 'reusable-components/dist/utils/projectDataStoreFunction'
import StoryPointsDefectRatioGraphWidget from 'reusable-components/dist/widgets/StoryPointsDefectRatioWidget/StoryPointsDefectRatioGraphWidget/StoryPointsDefectRatioGraphWidget'
import CustomDropdown from 'reusable-components/dist/components/CommonComponents/CustomDropdown'
import style from '../DefectRatioDrillDown.module.scss'

const sprintSelectDropdown = [
  { displayName: 'Previous 5 Sprints', value: 5 },
  { displayName: 'Previous 10 Sprints', value: 10 },
  { displayName: 'Previous 15 Sprints', value: 15 },
]

class StoryPointDefectRatioGraphs extends PureComponent {
  constructor(props) {
    super(props)
    const projectData = getStoredData('PROJECT_CLIENT_DATA')
    this.state = {
      selectedSprint: sprintSelectDropdown[0],
      projectData: projectData || {},
      projectId: this.props.params.projectId || 0,
    }
  }

  renderProjectWiseGraph() {
    const colLeft = []
    const colRight = []
    const { projectData, projectId, selectedSprint } = this.state
    let count = 0
    const baseUrl = process.env.REACT_APP_API_END_POINT
    for (let i = 0; i < projectData.length; i++) {
      if (projectData[i].id !== projectId) {
        if (count % 2 === 0) {
          colLeft.push(
            <StoryPointsDefectRatioGraphWidget
              apiEndPointUrl={`${baseUrl}/client-dashboard-service/projects/storyPointDefectRatio?iscProjectId=${projectData[i].id}&sprintCount=${selectedSprint.value}`}
              showHideEnable={true}
              needBracketBox={true}
              title={projectData[i].name}
            />,
          )
        } else {
          colRight.push(
            <StoryPointsDefectRatioGraphWidget
              apiEndPointUrl={`${baseUrl}/client-dashboard-service/projects/storyPointDefectRatio?iscProjectId=${projectData[i].id}&sprintCount=${selectedSprint.value}`}
              showHideEnable={true}
              needBracketBox={true}
              title={projectData[i].name}
            />,
          )
        }
        count++
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
    const { projectId, selectedSprint } = this.state
    return (
      <StaticPopupPage redirectUrl={'/client/client-defect-ratio'}>
        <div className="titleArea">
          <h2 className="title">Story Point Defect Ratio graph</h2>
          <div className="controlSpace">
            <CustomDropdown
              items={sprintSelectDropdown}
              selectedOption={selectedSprint.displayName}
              onSelectCallback={selectData => {
                this.setState({
                  selectedSprint: sprintSelectDropdown[selectData],
                })
              }}
              alignRight={true}
              bordered={true}
            />
          </div>
        </div>
        <Scrollbars autoHeight autoHeightMax={`calc(85vh - 110px)`}>
          <div className={[style.collapsibleBox, style.collapsed].join(' ')}>
            <div className={style.summaryView}>
              <div
                className={style.arrow}
                onClick={() => {
                  this.props.navigate('/client/client-defect-ratio')
                }}
              >
                <img src={IC_BACK_ARROW} title="" alt="" />
              </div>
              <div className={style.title}>All Projects Defect Ratio</div>
            </div>
          </div>
          <div className="graphHolderWithBg">
            <StoryPointsDefectRatioGraphWidget
              apiEndPointUrl={`${baseUrl}/client-dashboard-service/projects/storyPointDefectRatio?iscProjectId=${projectId}&sprintCount=${selectedSprint.value}`}
              showHideEnable={false}
              needBracketBox={false}
            />
          </div>
          <div className="gap30" />
          <div className="graphHolder">{this.renderProjectWiseGraph()}</div>
        </Scrollbars>
      </StaticPopupPage>
    )
  }
}

StoryPointDefectRatioGraphs.propTypes = {
  navigate: PropTypes.any,
  params: PropTypes.any,
}

export default withRouter(StoryPointDefectRatioGraphs)
