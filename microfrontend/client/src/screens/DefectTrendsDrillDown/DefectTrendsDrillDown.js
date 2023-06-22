import React, { PureComponent } from 'react'
import { Row, Col } from 'react-bootstrap'
import { Tooltip as ReactTooltip } from 'react-tooltip'
import MultiSelect from '@khanacademy/react-multi-select'
import { Scrollbars } from 'react-custom-scrollbars'
import { getStoredData } from 'reusable-components/dist/utils/projectDataStoreFunction'
import ParentTabSection from 'reusable-components/dist/components/CommonComponents/ParentTabSection'
import { CustomTab } from 'reusable-components/dist/components/CommonComponents/ParentTabSection'
import { IC_INFO } from 'reusable-components/dist/const/imgConst'
import StaticPopupPage from '../../components/StaticPopupPage/StaticPopupPage'
import DefectTrendTypeWidget from 'reusable-components/dist/widgets/DefectTrendTypeWidget/DefectTrendTypeWidget'
import DefectTrendSeverityWidget from 'reusable-components/dist/widgets/DefectTrendSeverityWidget/DefectTrendSeverityWidget'
import DefectTrendsAcceptanceWidget from 'reusable-components/dist/widgets/DefectTrendsAcceptanceWidget/DefectTrendsAcceptanceWidget'
import DefectTrendTypeProjectWiseWidget from 'reusable-components/dist/widgets/DefectTrendTypeProjectWiseWidget/DefectTrendTypeProjectWiseWidget'
import DefectTrendSeverityProjectWiseWidget from 'reusable-components/dist/widgets/DefectTrendSeverityProjectWiseWidget/DefectTrendSeverityProjectWiseWidget'
import DefectTrendAcceptanceProjectWiseWidget from 'reusable-components/dist/widgets/DefectTrendAcceptanceProjectWiseWidget/DefectTrendAcceptanceProjectWiseWidget'
import CustomDropdown from 'reusable-components/dist/components/CommonComponents/CustomDropdown'
import style from './DefectTrendsDrillDown.module.scss'

const monthOptions = [
  { displayName: 'Last Three Month', monthCount: 3 },
  { displayName: 'Last Six Month', monthCount: 6 },
  { displayName: 'Last One Year', monthCount: 12 },
]

class DefectTrendsDrillDown extends PureComponent {
  constructor(props) {
    super(props)
    const projectData = getStoredData('PROJECT_CLIENT_DATA')

    const projectIds = projectData.map(item => {
      return item.id
    })
    this.state = {
      selectedPointCount: '',
      projectReRenderKey: 1,
      projectOptions: projectData.map(item => {
        return { label: item.name, value: item.id }
      }),
      projectData: projectIds || {},
      selectedMonth: monthOptions[0],
      selectedProject: [],
    }
    this.renderProjectWiseGraph = this.renderProjectWiseGraph.bind(this)
  }

  renderProjectWiseGraph({ Widget, apiEndPoint }) {
    const colLeft = []
    const colRight = []
    const { projectData, selectedMonth, selectedProject, projectOptions } = this.state
    const selectedProjects = selectedProject.length > 0 ? selectedProject : projectData
    for (let i = 0; i < selectedProjects.length; i++) {
      if (i % 2 === 0) {
        colLeft.push(
          <Widget
            apiEndPointUrl={`${apiEndPoint}?iscProjectId=${selectedProjects[i]}&month=${selectedMonth.monthCount}`}
            title={projectOptions.find(item => item.value === selectedProjects[i]).label}
            showHideEnable={true}
          />,
        )
      } else {
        colRight.push(
          <Widget
            apiEndPointUrl={`${apiEndPoint}?iscProjectId=${selectedProjects[i]}&month=${selectedMonth.monthCount}`}
            title={projectOptions.find(item => item.value === selectedProjects[i]).label}
            showHideEnable={true}
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
    const selectedProjects =
      this.state.selectedProject.length > 0 ? this.state.selectedProject : this.state.projectData

    return (
      <StaticPopupPage>
        <div className="titleArea">
          <h2 className="title">
            Consolidated Defect Snapshots
            <div
              className="info"
              data-tooltip-place="bottom"
              data-tooltip-id="DefectTrendsDrilldown"
            >
              <img src={IC_INFO} title="" alt="" />
            </div>
            <ReactTooltip
              className="simpleTooltip"
              id="DefectTrendsDrilldown"
              border={true}
              borderColor="#35445f"
              arrowColor="#040a23"
            >
              <div className="infoContent">
                <p>
                  A consolidated defect snapshot, which depicts the product defect status by type
                  and severity, with the project-wise defects trend for raised, closed, and open for
                  the selected time duration.
                </p>
              </div>
            </ReactTooltip>
          </h2>
          <div className="controlSpace">
            <div className={['bordered', 'transparentMultiSelect', 'ml-4'].join(' ')}>
              <MultiSelect
                options={this.state.projectOptions}
                selected={this.state.selectedProject}
                onSelectedChanged={selected => {
                  this.setState({ selectedProject: selected })
                }}
                valueRenderer={selected => {
                  return selected.length === 0 ? 'Select Project' : `(${selected.length})`
                }}
                overrideStrings={{
                  selectSomeItems: 'Select Project',
                  allItemsAreSelected: 'All Project are Selected',
                  selectAll: 'Select All Project',
                  search: 'Search Project',
                }}
              />
            </div>

            <CustomDropdown
              alignRight={true}
              items={monthOptions}
              selectedOption={this.state.selectedMonth.displayName}
              onSelectCallback={index => {
                this.setState({ selectedMonth: monthOptions[index] })
              }}
              bordered={true}
            />
          </div>
        </div>
        <Scrollbars autoHeight autoHeightMax={`calc(85vh - 100px)`}>
          <div className={(style.defectTrendsDrillDown, style.CodeHealthDrillDownTabs)}>
            <ParentTabSection defaultActiveKey={0}>
              <CustomTab title="Defect Type">
                <div className="tabsContainer">
                  <DefectTrendTypeWidget
                    key={`second`}
                    apiEndPointUrl={`${baseUrl}/client-dashboard-service/projects/defectTrends/type?iscProjectIds=${selectedProjects.join(
                      ',',
                    )}`}
                  />
                </div>
                <div className="graphHolder">
                  {this.renderProjectWiseGraph({
                    Widget: DefectTrendTypeProjectWiseWidget,
                    apiEndPoint: `${baseUrl}/client-dashboard-service/projects/defectTrends/type/stats`,
                  })}
                </div>
              </CustomTab>
              <CustomTab title="Defect Severity">
                <DefectTrendSeverityWidget
                  key={`second-`}
                  apiEndPointUrl={`${baseUrl}/client-dashboard-service/projects/defectTrends/severity?iscProjectIds=${selectedProjects.join(
                    ',',
                  )}`}
                />
                <div className="graphHolder">
                  {this.renderProjectWiseGraph({
                    Widget: DefectTrendSeverityProjectWiseWidget,
                    apiEndPoint: `${baseUrl}/client-dashboard-service/projects/defectTrends/severity/stats`,
                  })}
                </div>
              </CustomTab>
              <CustomTab title="Defect Acceptance">
                <DefectTrendsAcceptanceWidget
                  apiEndPointUrl={`${baseUrl}/client-dashboard-service/projects/defectTrends/acceptance?iscProjectIds=${selectedProjects.join(
                    ',',
                  )}`}
                />
                <div className="graphHolder">
                  {this.renderProjectWiseGraph({
                    Widget: DefectTrendAcceptanceProjectWiseWidget,
                    apiEndPoint: `${baseUrl}/client-dashboard-service/projects/defectTrends/acceptance/stats`,
                  })}
                </div>
              </CustomTab>
            </ParentTabSection>
          </div>
        </Scrollbars>
      </StaticPopupPage>
    )
  }
}

export default DefectTrendsDrillDown
