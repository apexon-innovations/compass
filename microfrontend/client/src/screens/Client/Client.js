import React, { PureComponent } from 'react'
import { Container, Row, Col } from 'react-bootstrap'
import BracketBox from 'reusable-components/dist/components/CommonComponents/BracketBox/BracketBox'
import StoryPointsDelTrendWidget from 'reusable-components/dist/widgets/StoryPointsDelTrendWidget/StoryPointsDelTrendWidget'
import StoryPointsDefectRatioWidget from 'reusable-components/dist/widgets/StoryPointsDefectRatioWidget/StoryPointsDefectRatioWidget/StoryPointsDefectRatioWidget'
import VelocityTrendsWidget from 'reusable-components/dist/widgets/VelocityTrendsWidget/VelocityTrendsWidget'
import ProjectCompletionTrendsWidget from 'reusable-components/dist/widgets/ProjectCompletionTrendsWidget/ProjectCompletionTrendsWidget'
import DefectTrendsWidget from 'reusable-components/dist/widgets/DefectTrendsWidget/DefectTrendsWidget'
import ProductHealthOverviewSunBurst from 'reusable-components/dist/widgets/ProductHealthOverviewSunBurst/ProductHealthOverviewSunBurst'
import DefectAgeingWidget from 'reusable-components/dist/widgets/DefectAgeingWidget/DefectAgeingWidget'
import ParentTabSection from 'reusable-components/dist/components/CommonComponents/ParentTabSection'
import Loader from 'reusable-components/dist/components/Loader/Loader'
import { CustomTab } from 'reusable-components/dist/components/CommonComponents/ParentTabSection'
import { getStoredData } from 'reusable-components/dist/utils/projectDataStoreFunction'
import style from './Client.module.scss'
import { allProjectAPIWrapper } from '../../utils/commonFunction'

class Client extends PureComponent {
  constructor(props) {
    super(props)

    this.state = {
      projectData: getStoredData('PROJECT_CLIENT_DATA'),
      isLoading: true,
    }
  }

  componentDidMount() {
    if (this.state.projectData.length === 0) {
      allProjectAPIWrapper().then(() => {
        this.setState({ isLoading: false, projectData: getStoredData('PROJECT_CLIENT_DATA') })
      })
    } else {
      this.setState({ isLoading: false })
    }
  }

  render() {
    const baseUrl = process.env.REACT_APP_API_END_POINT

    if (this.state.isLoading) {
      return (
        <div className={[style.clientScreen].join(' ')}>
          <Container className="container1400">
            <Loader placement={'fixed'} />
          </Container>
        </div>
      )
    }

    return (
      <div className={[style.clientScreen].join(' ')}>
        <Container className="container1400">
          <Row>
            <Col className="col" md={6} sm={6} xs={12}>
              <ProductHealthOverviewSunBurst
                apiEndPointUrl={`${baseUrl}/client-dashboard-service/projects/productHealthOverview?iscProjectIds=all`}
                zoomEnable={false}
                bracketEnable={true}
              />
            </Col>
            <Col className="col" md={6} sm={6} xs={12}>
              <BracketBox verticalAlignment="alignTop" animStatus="" boxTitle={''}>
                <ParentTabSection>
                  <CustomTab title="Project Completion Trend">
                    <ProjectCompletionTrendsWidget
                      apiEndPointUrl={`${baseUrl}/client-dashboard-service/projects/productCompletionTrend`}
                      zoomEnable={true}
                      selectedPointCount={'Points'}
                      toolTipCustom={true}
                      bracketBox={false}
                      boxTitle={['Project Completion Trends']}
                    />
                  </CustomTab>
                  <CustomTab title="Velocity Trend">
                    <VelocityTrendsWidget
                      selectedPointCount={'Points'}
                      selectedSprint={5}
                      apiEndPointUrl={`${baseUrl}/client-dashboard-service/projects`}
                      zoomEnable={true}
                    />
                  </CustomTab>
                </ParentTabSection>
              </BracketBox>
            </Col>
          </Row>
          <Row>
            <Col className="col" md={6} sm={6} xs={12}>
              <StoryPointsDelTrendWidget
                selectedSprint={0}
                apiEndPointUrl={`${baseUrl}/client-dashboard-service/projects/consolidatedStoryPointDeliveryTrends`}
                zoomEnable={true}
                selectedPointCount={'Points'}
                toolTipCustom={true}
                projectDetailsLinkUrl={'/client/client-delivery-trends/'}
                viewType={'mainView'}
              />
            </Col>
            <Col className="col" md={6} sm={6} xs={12}>
              <StoryPointsDefectRatioWidget
                apiEndPointUrl={`${baseUrl}/client-dashboard-service/projects/consolidatedStoryPointDefectRatio?iscProjectIds=all&sprintCount=5`}
                zoomEnable={true}
              />
            </Col>
          </Row>
          <Row>
            <Col className="col" md={6} sm={6} xs={12}>
              <DefectTrendsWidget
                apiEndPointUrl={`${baseUrl}/client-dashboard-service/projects/defectTrends/type?iscProjectIds=all`}
                zoomEnable={true}
                bracketEnable={true}
              />
            </Col>
            <Col className="col" md={6} sm={6} xs={12}>
              <DefectAgeingWidget
                apiEndPointUrl={`${baseUrl}/client-dashboard-service/projects/defects/ageing?iscProjectIds=`}
                zoomEnable={true}
                projectIds={'all'}
                bracketEnable={true}
              />
            </Col>
          </Row>
        </Container>
      </div>
    )
  }
}

export default Client
