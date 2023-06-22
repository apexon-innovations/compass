import React from 'react'
import PropTypes from 'prop-types'
import { Container, Row, Col } from 'react-bootstrap'

import ProjectProgressWidget from 'reusable-components/dist/widgets/ProjectProgressWidget/ProjectProgressWidget'
import SprintProgressWidget from 'reusable-components/dist/widgets/SprintProgressWidget/SprintProgressWidget'
import TaskStatusWidget from 'reusable-components/dist/widgets/TaskStatusWidget/TaskStatusWidget'
import DefectsCountWidget from 'reusable-components/dist/widgets/DefectsCountWidget/DefectsCountWidget'
import LoggedVsAcceptedWidget from 'reusable-components/dist/widgets/LoggedVsAcceptedWidget/LoggedVsAcceptedWidget'
import BlockersWidget from 'reusable-components/dist/widgets/BlockersWidget/BlockersWidget'
import MemberStatusWidget from 'reusable-components/dist/widgets/MemberStatusWidget/MemberStatusWidget'
import EffortVarianceWidget from 'reusable-components/dist/widgets/EffortVarianceWidget/EffortVarianceWidget'
import CalendarViewWidget from 'reusable-components/dist/widgets/CalendarViewWidget/CalendarViewWidget'
import ComplianceWidget from 'reusable-components/dist/widgets/ComplianceWidget/ComplianceWidget'

import Loader from 'reusable-components/dist/components/Loader/Loader'
import BracketBox from 'reusable-components/dist/components/CommonComponents/BracketBox/BracketBox'

import { getStoredProjectData } from 'reusable-components/dist/utils/projectDataStoreFunction'

import style from './Overview.module.scss'
import { withRouter } from 'reusable-components/dist/components/withRouter/withRouter'

class Overview extends React.PureComponent {
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

  componentWillUnmount() {
    window.removeEventListener('updateProjectIcon', this.handleProjectIconUpload)
    window.removeEventListener('currentProjectData', this.handleUpdatedProjectData)
  }

  componentDidMount() {
    this.projectDetailsAPICall()
    window.addEventListener('updateProjectIcon', this.handleProjectIconUpload)
    window.addEventListener('currentProjectData', this.handleUpdatedProjectData)
  }

  render() {
    const baseUrl = process.env.REACT_APP_API_END_POINT
    const projectId = this.props.params.projectId
    const { isLoading, boardId } = this.state
    return isLoading ? (
      <Loader />
    ) : (
      <div className={[style.overviewScreen].join(' ')}>
        <Container className="container1400">
          <Row>
            <Col className="col" md={5} sm={6} xs={12}>
              <TaskStatusWidget
                apiEndPointUrl={`${baseUrl}/psr-service/project/${projectId}/sprint/status?boardId=${boardId}`}
              />
            </Col>
            <Col className="col" md={5} sm={6} xs={12}>
              <MemberStatusWidget
                apiEndPointUrl={`${baseUrl}/psr-service/project/${projectId}/sprint/member/status?boardId=${boardId}`}
                memberJiraInfoApiEndPoint={`${baseUrl}/psr-service/project/${projectId}/sprint/member`}
                boardId={boardId}
                memberNestInfoApiEndPoint={`${baseUrl}/psr-service/project/${projectId}/member`}
              />
            </Col>
            <Col className="col" md={2} sm={6} xs={12}>
              <BlockersWidget
                apiEndPointUrl={`${baseUrl}/psr-service/project/${projectId}/sprint/blockers?boardId=${boardId}`}
              />
            </Col>
          </Row>
          <Row>
            <Col className="col" md={3} sm={6} xs={12}>
              <EffortVarianceWidget
                apiEndPointUrl={`${baseUrl}/psr-service/project/${projectId}/sprint/ev?boardId=${boardId}`}
              />
            </Col>
            <Col className="col" md={6} sm={6} xs={12}>
              <div className="w-100 flex-column">
                <ProjectProgressWidget
                  key={this.state.projectReRenderKey}
                  popUpOpen={this.state.popUpOpen}
                  apiEndPointUrl={`${baseUrl}/psr-service/project/${projectId}`}
                  fileUploaderUrl={`${baseUrl}/psr-service/project/${projectId}/logo`}
                  boardId={boardId}
                  projectId={projectId}
                  apiEndPointUrlJiraBoardId={`${baseUrl}/psr-service/project/${projectId}/jiraBoards`}
                />
                <SprintProgressWidget
                  apiEndPointUrl={`${baseUrl}/psr-service/project/${projectId}/sprint/overview?boardId=${boardId}`}
                />
              </div>
            </Col>
            <Col className="col" md={3} sm={6} xs={12}>
              <ComplianceWidget apiEndPointUrl={`${baseUrl}/psr-service/project/${projectId}/compliance`} />
            </Col>
          </Row>
          <Row>
            <Col className="col" md={7} sm={6} xs={12}>
              <BracketBox animStatus="" boxTitle={'Defects'}>
                <Container fluid>
                  <Row>
                    <DefectsCountWidget
                      apiEndPointUrl={`${baseUrl}/psr-service/project/${projectId}/sprint/bugs?boardId=${boardId}`}
                    />
                    <LoggedVsAcceptedWidget
                      apiEndPointUrl={`${baseUrl}/psr-service/project/${projectId}/sprint/bugs/status?boardId=${boardId}`}
                    />
                  </Row>
                </Container>
              </BracketBox>
            </Col>
            <Col className="col" md={5} sm={6} xs={12}>
              <CalendarViewWidget
                apiEndPointUrl={`${baseUrl}/psr-service/project/${projectId}/sprint/leaves?boardId=${boardId}`}
              />
            </Col>
          </Row>
        </Container>
      </div>
    )
  }
}

Overview.propTypes = {
  params: PropTypes.object,
}

export default withRouter(Overview);
