import React, { PureComponent } from 'react'
import { Container, Row, Col } from 'react-bootstrap'
import PropTypes from 'prop-types'

import PRmgmtWidget from 'reusable-components/dist/widgets/PRmgmtWidget/PRmgmtWidget'
import ProjectRatingComplianceWidget from 'reusable-components/dist/widgets/ProjectRatingComplianceWidget/ProjectRatingComplianceWidget'
import QualityMetricsWidget from 'reusable-components/dist/widgets/QualityMetricsWidget/QualityMetricsWidget'
import RepositoryInspectorWidget from 'reusable-components/dist/widgets/RepositoryInspectorWidget/RepositoryInspectorWidget'
import MemberwiseProductiveCodeWidget from 'reusable-components/dist/widgets/MemberwiseProductiveCodeWidget/MemberwiseProductiveCodeWidget'
import MemberwiseActivityMetricsWidget from 'reusable-components/dist/widgets/MemberwiseActivityMetricsWidget/MemberwiseActivityMetricsWidget'
import ProjectProgressWidget from 'reusable-components/dist/widgets/ProjectProgressWidget/ProjectProgressWidget'
import ViolationTechWiseWidget from 'reusable-components/dist/widgets/ViolationTechWiseWidget/ViolationTechWiseWidget'
import ScoreWidget from 'reusable-components/dist/widgets/ScoreWidget/ScoreWidget'
import SecurityScoreWidget from 'reusable-components/dist/widgets/SecurityScoreWidget/SecurityScoreWidget'
import RiskWidget from 'reusable-components/dist/widgets/RiskWidget/RiskWidget'
import ReviewerCollaborationWidget from 'reusable-components/dist/widgets/ReviewerCollaborationWidget/ReviewerCollaborationWidget'

import Loader from 'reusable-components/dist/components/Loader/Loader'

import { getStoredProjectData } from 'reusable-components/dist/utils/projectDataStoreFunction'
import { getAllProjectApi } from '../../utils/commonFunction'

// Style
import style from './Strategic.module.scss'
import { withRouter } from 'reusable-components/dist/components/withRouter/withRouter'

class Strategic extends PureComponent {
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
      repoId: projectBoardIdExist && projectBoardIdExist.selectedRepo ? projectBoardIdExist.selectedRepo : [],
      timeDuration: projectBoardIdExist && projectBoardIdExist.timeDuration ? projectBoardIdExist.timeDuration : '',
    }
    this.handleProjectIconUpload = this.handleProjectIconUpload.bind(this)
    this.projectDetailsAPICall = this.projectDetailsAPICall.bind(this)
    this.handleUpdatedProjectData = this.handleUpdatedProjectData.bind(this)
    this.updateState = this.updateState.bind(this)
  }

  projectDetailsAPICall() {
    if (!this.props?.params?.projectId) {
      this.updateState({ isLoading: true })
      getAllProjectApi(this.state, this.props, this.updateState)
    } else {
      this.updateState({ isLoading: false })
    }
  }

  updateState(obj) {
    this.setState(obj)
  }

  handleUpdatedProjectData(obj) {
    const projectData = obj.detail
    let updateState
    if (this.state.projectId) {
      updateState = projectData.projects.find(project => project.projectId === this.state.projectId)
    } else if (projectData.projectId) {
      updateState = projectData.projects.find(project => project.projectId === projectData.projectId)
    }

    const updateObject = {}

    if (updateState && updateState.projectId && this.state.projectId !== updateObject.projectId) {
      updateObject['projectId'] = updateState.projectId
    }

    if (updateState && updateState.selectedRepo) {
      updateObject['repoId'] = updateState.selectedRepo
    }

    if (updateState && updateState.timeDuration) {
      updateObject['timeDuration'] = updateState.timeDuration
    }

    if (Object.keys(updateObject).length > 0) {
      this.setState({ ...updateObject, isLoading: false })
    }
  }

  handleProjectIconUpload() {
    this.setState({
      projectReRenderKey: Date.now(),
      popUpOpen: true,
    })
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
    const { repoId, timeDuration } = this.state

    return this.state.isLoading ? (
      <Loader />
    ) : (
      <div className={[style.strategicScreen].join(' ')}>
        <Container className="container1400">
          <Row>
            <Col md={6} className={style.cols}>
              <PRmgmtWidget
                key={repoId}
                repoId={repoId}
                apiEndPointUrlOfSubmitterMetrics={`${baseUrl}/strategy-service/project/repo/submitter/metrics?repoIds=${repoId.join(
                  ',',
                )}&dayCount=${timeDuration}&iscProjectId=${projectId}`}
                apiEndPointUrlOfPRPieChart={`${baseUrl}/strategy-service/project/repo/pullrequests/summary?repoIds=${repoId.join(
                  ',',
                )}&dayCount=${timeDuration}&iscProjectId=${projectId}`}
                apiEndPointUrlOfPRMgmt={`${baseUrl}/strategy-service/project/repo/pullrequests/journey?repoIds=${repoId.join(
                  ',',
                )}&dayCount=${timeDuration}&iscProjectId=${projectId}`}
              />
            </Col>
            <Col md={6} className={style.cols}>
              <ReviewerCollaborationWidget
                key={repoId}
                repoId={repoId}
                apiEndPointUrlOfReviewerMetrics={`${baseUrl}/strategy-service/project/repo/reviewer/metrics?repoIds=${repoId.join(
                  ',',
                )}&dayCount=${timeDuration}&iscProjectId=${projectId}`}
                apiEndPointUrlOfCollaborativeMetrics={`${baseUrl}/strategy-service/project/repo/collaboration/metrics?repoIds=${repoId.join(
                  ',',
                )}&dayCount=${timeDuration}&iscProjectId=${projectId}`}
              />
            </Col>
          </Row>
          <Row>
            <Col md={4} className={style.cols}>
              <QualityMetricsWidget
                key={repoId}
                apiEndPointUrl={`${baseUrl}/strategy-service/project/repo/quality/summary?repoIds=${repoId.join(
                  ',',
                )}&iscProjectId=${projectId}`}
              />
            </Col>
            <Col md={4} className={[style.cols, 'flex-column justify-content-center'].join(' ')}>
              <ProjectProgressWidget
                key={this.state.projectReRenderKey}
                popUpOpen={this.state.popUpOpen}
                repoId={repoId}
                timeDuration={timeDuration}
                projectId={projectId}
                apiEndPointUrl={`${baseUrl}/psr-service/project/${projectId}`}
                fileUploaderUrl={`${baseUrl}/psr-service/project/${projectId}/logo`}
                additionalClass={'innerPageDropdown'}
              />
              <ProjectRatingComplianceWidget
                apiEndPointUrl={`${baseUrl}/strategy-service/project/repo/compliance/analysis?repoIds=${repoId.join(
                  ',',
                )}&iscProjectId=${projectId}`}
              />
            </Col>
            <Col md={4} className={style.cols}>
              <RepositoryInspectorWidget
                key={repoId}
                apiEndPointUrl={`${baseUrl}/strategy-service/project/repo/techdebt/metrics?repoIds=${repoId.join(
                  ',',
                )}&iscProjectId=${projectId}`}
              />
            </Col>
          </Row>
          <Row>
            <Col md={5} className={style.cols}>
              <MemberwiseProductiveCodeWidget
                key={repoId}
                timeDuration={timeDuration}
                apiEndPointUrl={`${baseUrl}/strategy-service/project/repo/code/metrics?repoIds=${repoId.join(
                  ',',
                )}&dayCount=${timeDuration}&iscProjectId=${projectId}`}
              />
            </Col>
            <Col md={7} className={style.cols}>
              <MemberwiseActivityMetricsWidget
                key={repoId}
                apiEndPointUrl={`${baseUrl}/strategy-service/project/repo/activity/metrics?repoIds=${repoId.join(
                  ',',
                )}&dayCount=${timeDuration}&iscProjectId=${projectId}`}
              />
            </Col>
          </Row>
          <Row>
            <Col md={4} className={style.cols}>
              <ViolationTechWiseWidget
                key={repoId}
                apiEndPointUrl={`${baseUrl}/strategy-service/project/repo/violation/summary?repoIds=${repoId.join(
                  ',',
                )}&iscProjectId=${projectId}`}
              />
            </Col>
            <Col md={4} className={style.cols}>
              <ScoreWidget
                key={repoId}
                apiEndPointUrl={`${baseUrl}/strategy-service/project/repo/quality/measurements?repoIds=${repoId.join(
                  ',',
                )}&dayCount=${timeDuration}&iscProjectId=${projectId}`}
              />
            </Col>
            <Col md={4} className={style.cols}>
              <RiskWidget
                key={repoId}
                apiEndPointUrl={`${baseUrl}/strategy-service/project/repo/risk/measurements?repoIds=${repoId.join(
                  ',',
                )}&dayCount=${timeDuration}&iscProjectId=${projectId}`}
              />
            </Col>
          </Row>
          <Row>
            <Col>
              <SecurityScoreWidget data={{ data: 'dummy' }} />
            </Col>
          </Row>
        </Container>
      </div>
    )
  }
}

Strategic.propTypes = {
  params: PropTypes.any,
}

export default withRouter(Strategic)
