import React, { PureComponent } from 'react'
import PropTypes from 'prop-types'
import { Container, Row, Col } from 'react-bootstrap'

import StoryPointsDelvsAccWidget from 'reusable-components/dist/widgets/StoryPointsDelvsAccWidget/StoryPointsDelvsAccWidget'
import StoryReportsWidget from 'reusable-components/dist/widgets/StoryReportsWidget/StoryReportsWidget'
import TeamVelocityWidget from 'reusable-components/dist/widgets/TeamVelocityWidget/TeamVelocityWidget'
import NPSWidget from 'reusable-components/dist/widgets/NPSWidget/NPSWidget'
import ProjectProgressWidget from 'reusable-components/dist/widgets/ProjectProgressWidget/ProjectProgressWidget'
import SayDoRatioWidget from 'reusable-components/dist/widgets/SayDoRatioWidget/SayDoRatioWidget'
import SayDoRatioMemberWiseWidget from 'reusable-components/dist/widgets/SayDoRatioMemberWiseWidget/SayDoRatioMemberWiseWidget'
import SprintBurnDownChartWidget from 'reusable-components/dist/widgets/SprintBurnDownChartWidget/SprintBurnDownChartWidget'

import BillingBox from 'reusable-components/dist/components/BillingBox/BillingBox'
import ProjectRating from 'reusable-components/dist/components/ProjectRating/ProjectRating'
import Loader from 'reusable-components/dist/components/Loader/Loader'
import ParentTabSection from 'reusable-components/dist/components/CommonComponents/ParentTabSection'
import { CustomTab } from 'reusable-components/dist/components/CommonComponents/ParentTabSection'
import BracketBox from 'reusable-components/dist/components/CommonComponents/BracketBox/BracketBox'

import { getStoredProjectData } from 'reusable-components/dist/utils/projectDataStoreFunction'

// Style
import style from './Operational.module.scss'
import { withRouter } from 'reusable-components/dist/components/withRouter/withRouter'

class Operational extends PureComponent {
  constructor(props) {
    super(props)
    const projectData = getStoredProjectData()
    const projectBoardIdExist =
      projectData.projects && projectData.projects.length > 0
        ? projectData.projects.find(project => project.projectId === projectData.projectId)
        : false
    this.state = {
      isLoading: true,
      projectReRenderKey: 1,
      popUpOpen: false,
      sprintId:
        projectBoardIdExist && projectBoardIdExist.selectedBoard ? projectBoardIdExist.selectedBoard.sprintId : 'none',
      projectId: projectData.projectId || '',
      boardId:
        projectBoardIdExist && projectBoardIdExist.selectedBoard ? projectBoardIdExist.selectedBoard.boardId : '',
    }
    this.handleProjectIconUpload = this.handleProjectIconUpload.bind(this)
    this.handleUpdatedProjectData = this.handleUpdatedProjectData.bind(this)
    this.projectDetailsAPICall = this.projectDetailsAPICall.bind(this)
    this.updateState = this.updateState.bind(this)
  }

  projectDetailsAPICall() {
    if (!this.props?.params?.projectId) {
      this.updateState({ isLoading: true })
      import('../../utils/commonFunction').then(({ getAllProjectApi }) => {
        getAllProjectApi(this.state, this.props, this.updateState)
      })
    } else {
      this.updateState({ isLoading: false })
    }
  }

  handleProjectIconUpload() {
    this.setState({
      projectReRenderKey: Date.now(),
      popUpOpen: true,
    })
  }

  handleUpdatedProjectData(obj) {
    import('../../utils/commonFunction').then(({ updateProjectData }) => {
      updateProjectData(obj, this.state, this.updateState)
    })
  }

  updateState(obj) {
    this.setState(obj)
  }

  componentDidMount() {
    this.projectDetailsAPICall()
    window.addEventListener('updateProjectIcon', this.handleProjectIconUpload)
    window.addEventListener('currentProjectData', this.handleUpdatedProjectData)
  }

  componentWillUnmount() {
    window.removeEventListener('updateProjectIcon', this.handleProjectIconUpload)
    window.removeEventListener('currentProjectData', this.handleUpdatedProjectData)
  }

  render() {
    const baseUrl = process.env.REACT_APP_API_END_POINT
    const projectId = this.props.params.projectId
    const { isLoading, boardId } = this.state
    return isLoading ? (
      <Loader />
    ) : (
      <div className={[style.operationalScreen].join(' ')}>
        <Container className="container1400">
          <Row>
            <Col md={6} className={[style.cols, 'd-flex'].join(' ')}>
              <StoryReportsWidget
                apiEndPointUrl={`${baseUrl}/psr-service/project/${projectId}/sprint/story/report?boardId=${boardId}`}
              />
            </Col>
            <Col md={6} className={style.cols}>
              <StoryPointsDelvsAccWidget
                apiEndPointUrl={`${baseUrl}/psr-service/project/${projectId}/sprint/accepted/delivered?boardId=${boardId}`}
              />
            </Col>
          </Row>
          <Row>
            <Col md={4} className={style.cols}>
              <NPSWidget apiEndPointUrl={`${baseUrl}/psr-service/project/${projectId}/nps`} projectId={projectId} />
            </Col>
            <Col md={4} className={[style.cols, 'flex-column justify-content-center'].join(' ')}>
              <ProjectProgressWidget
                key={this.state.projectReRenderKey}
                popUpOpen={this.state.popUpOpen}
                apiEndPointUrl={`${baseUrl}/psr-service/project/${projectId}`}
                fileUploaderUrl={`${baseUrl}/psr-service/project/${projectId}/logo`}
                projectId={projectId}
                boardId={boardId}
                apiEndPointUrlJiraBoardId={`${baseUrl}/psr-service/project/${projectId}/jiraBoards`}
                additionalClass={'innerPageDropdown'}
              />
              <ProjectRating />
            </Col>
            <Col md={4} className={style.cols}>
              <BillingBox />
            </Col>
          </Row>
          <Row>
            <Col md={6} className={style.cols}>
              <BracketBox verticalAlignment="alignTop" animStatus="" boxTitle={''}>
                <ParentTabSection>
                  <CustomTab title="Sprint Burn Down Chart">
                    <SprintBurnDownChartWidget
                      apiEndPointUrl={`${baseUrl}/psr-service/project/${projectId}/sprint/burndown?boardId=${boardId}`}
                    />
                  </CustomTab>
                  <CustomTab title="Team Velocity">
                    <TeamVelocityWidget
                      data={{
                        key: 'dummy-value',
                      }}
                    />
                  </CustomTab>
                </ParentTabSection>
              </BracketBox>
            </Col>
            <Col md={6} className={style.cols}>
              <BracketBox verticalAlignment="alignTop" animStatus="" boxTitle={''}>
                <ParentTabSection>
                  <CustomTab title="Say - Do Ratio">
                    <SayDoRatioWidget
                      apiEndPointUrl={`${baseUrl}/psr-service/project/${projectId}/sprint/saydo?boardId=${boardId}`}
                    />
                  </CustomTab>
                  <CustomTab title="Say - Do Ratio, Member wise">
                    <SayDoRatioMemberWiseWidget
                      data={{
                        key: 'dummy-value',
                      }}
                    />
                  </CustomTab>
                </ParentTabSection>
              </BracketBox>
            </Col>
          </Row>
        </Container>
      </div>
    )
  }
}

Operational.propTypes = {
  params: PropTypes.object,
}
export default withRouter(Operational)
