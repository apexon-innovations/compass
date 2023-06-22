import React, { useState } from 'react'
import PropTypes from 'prop-types'
import { Row, Col } from 'react-bootstrap'
import { Scrollbars } from 'react-custom-scrollbars'
import { Tooltip as ReactTooltip } from 'react-tooltip'
import MultiSelect from '@khanacademy/react-multi-select'
import StaticPopupPage from '../../components/StaticPopupPage/StaticPopupPage'
import { getStoredData } from 'reusable-components/dist/utils/projectDataStoreFunction'
import { IC_INFO } from 'reusable-components/dist/const/imgConst'
import { withRouter } from 'reusable-components/dist/components/withRouter/withRouter'
import SecurityComplianceScoreWidget from 'reusable-components/dist/widgets/SecurityComplianceScoreWidget/SecurityComplianceScoreWidget'
import CodeSmellAndIssuesClientWidget from 'reusable-components/dist/widgets/CodeSmellAndIssuesClientWidget/CodeSmellAndIssuesClientWidget'
import CodeTechnicalDebtClientWidget from 'reusable-components/dist/widgets/CodeTechnicalDebtClientWidget/CodeTechnicalDebtClientWidget'
import CodeScoreClientWidget from 'reusable-components/dist/widgets/CodeScoreClientWidget/CodeScoreClientWidget'
import CodeViolationClientWidget from 'reusable-components/dist/widgets/CodeViolationClientWidget/CodeViolationClientWidget'
import CodeMetricsClientWidget from 'reusable-components/dist/widgets/CodeMetricsClientWidget/CodeMetricsClientWidget'
import PaginationArrows from 'reusable-components/dist/components/CommonComponents/PaginationArrows/PaginationArrows'
import CustomDropdown from 'reusable-components/dist/components/CommonComponents/CustomDropdown'
import ParentTabSection from 'reusable-components/dist/components/CommonComponents/ParentTabSection'
import { CustomTab } from 'reusable-components/dist/components/CommonComponents/ParentTabSection'
import style from './CodeHealthDrillDown.module.scss'

const monthOptions = [
  { displayName: 'Last Three Month', monthCount: 3 },
  { displayName: 'Last Six Month', monthCount: 6 },
  { displayName: 'Last One Year', monthCount: 12 },
]

const CodeHealthDrillDown = props => {
  const projectData = getStoredData('PROJECT_CLIENT_DATA')
  const dataProject = projectData.filter(item => {
    return item.id === props.params.projectId
  })
  const baseUrl = process.env.REACT_APP_API_END_POINT
  const dropDownRepo = []
  const defaultSelectedRepoIds = []

  if (dataProject && dataProject[0] && dataProject[0].repoIds) {
    dataProject[0].repoIds.map(item => {
      dropDownRepo.push({ label: item.repoName, value: item.repoId, data: item })
      defaultSelectedRepoIds.push(item.repoId)
      return item
    })
  }

  const [dropDownRepoValue, setDropDownRepoValue] = useState([])
  const [selectedMonth, setSelectedMonth] = useState(monthOptions[0])

  const [displayFromFifth, setDisplayFromFifth] = useState(0)
  const [displayFromSixth, setDisplayFromSixth] = useState(0)

  const numberPerDisplay = 3
  const repoIds = dropDownRepoValue.length > 0 ? dropDownRepoValue : defaultSelectedRepoIds

  const resetPagination = () => {
    setDisplayFromFifth(0)
    setDisplayFromSixth(0)
  }

  const getRepoName = id => {
    return dropDownRepo.find(item => item.value === id)
  }

  return (
    <StaticPopupPage redirectUrl="/client/business-overview">
      {dataProject && dataProject[0] && (
        <React.Fragment>
          <div className="titleArea">
            <div className={style.codeHealthTitle}>
              <h2 className="title">
                {dataProject[0].name}
                <div className="info" to="" data-tooltip-place="bottom" data-tip data-tooltip-id="info1" >
                  <img src={IC_INFO} title="" alt="" />
                </div>
                <ReactTooltip
                  className="simpleTooltip"
                  id="info1"
                  border={true}
                  borderColor="#35445f"
                  arrowColor="#040a23"
                >
                  <p>{dataProject[0].name}</p>
                </ReactTooltip>
              </h2>
              <div className={[style.completionTrendsdropdown, 'controlSpace'].join(' ')}>
                <div className={['bordered transparentMultiSelect'].join(' ')}>
                  <MultiSelect
                    options={dropDownRepo}
                    selected={dropDownRepoValue}
                    onSelectedChanged={selected => {
                      selected.sort()
                      setDropDownRepoValue(selected)
                      resetPagination()
                    }}
                    valueRenderer={selected => {
                      return selected.length === 0 ? 'Select Repository' : `(${selected.length})`
                    }}
                    overrideStrings={{
                      selectSomeItems: 'Select Repository',
                      allItemsAreSelected: 'All Repository are Selected',
                      selectAll: 'Select All Repository',
                      search: 'Search Repository',
                    }}
                    alignRight={true}
                    bordered={true}
                  />
                </div>
                <CustomDropdown
                  items={monthOptions}
                  selectedOption={selectedMonth.displayName}
                  onSelectCallback={index => {
                    setSelectedMonth(monthOptions[index])
                    resetPagination()
                  }}
                  alignRight={true}
                  bordered={true}
                />
              </div>
            </div>
          </div>
          <Scrollbars autoHeight autoHeightMax={`calc(85vh - 100px)`}>
            <div className={style.codeHealthDashboard}>
              <SectionBox
                key={`code-mertic-${repoIds.join('')}-${selectedMonth.monthCount}`}
                repoIds={repoIds}
                numberPerDisplay={numberPerDisplay}
                title={'Code Metric'}
                Widget={CodeMetricsClientWidget}
                apiEndPointUrl={`${baseUrl}/client-dashboard-service/projects/codeHealth/metrics`}
                projectId={dataProject[0].id}
                monthCount={selectedMonth.monthCount}
                getRepoName={getRepoName}
                requiredParams={'isScmAvailable'}
                errorMessage={'Does not have access to Bitbucket for this repository.'}
              />
              <SectionBox
                key={`code-violation-${repoIds.join('')}-${selectedMonth.monthCount}`}
                repoIds={repoIds}
                numberPerDisplay={numberPerDisplay}
                title={'Code Violations Overview'}
                Widget={CodeViolationClientWidget}
                apiEndPointUrl={`${baseUrl}/client-dashboard-service/projects/codeHealth/violations`}
                projectId={dataProject[0].id}
                monthCount={selectedMonth.monthCount}
                getRepoName={getRepoName}
                requiredParams={'isSonarAvailable'}
                errorMessage={'Does not have access to Sonar for this repository.'}
              />
              <SectionBox
                key={`score-${repoIds.join('')}-${selectedMonth.monthCount}`}
                repoIds={repoIds}
                numberPerDisplay={numberPerDisplay}
                title={'Score'}
                Widget={CodeScoreClientWidget}
                apiEndPointUrl={`${baseUrl}/client-dashboard-service/projects/codeHealth/score`}
                projectId={dataProject[0].id}
                monthCount={selectedMonth.monthCount}
                getRepoName={getRepoName}
                requiredParams={'isSonarAvailable'}
                errorMessage={'Does not have access to Sonar for this repository.'}
              />
              <div className={[style.CodeHealthDrillDownTabs, style.sectionArea].join(' ')}>
                <ParentTabSection defaultActiveKey={0}>
                  <CustomTab title="Technical Debt Overview">
                    <SectionBox
                      key={`technical-debt-over-${repoIds.join('')}-${selectedMonth.monthCount}`}
                      repoIds={repoIds}
                      numberPerDisplay={numberPerDisplay}
                      Widget={CodeTechnicalDebtClientWidget}
                      apiEndPointUrl={`${baseUrl}/client-dashboard-service/projects/codeHealth/techDebt`}
                      projectId={dataProject[0].id}
                      monthCount={selectedMonth.monthCount}
                      getRepoName={getRepoName}
                      requiredParams={'isSonarAvailable'}
                      errorMessage={'Does not have access to Sonar for this repository.'}
                    />
                  </CustomTab>
                  <CustomTab title="Code Smell">
                    <React.Fragment>
                      <Row>
                        {repoIds &&
                          [...repoIds]
                            .splice(displayFromFifth, numberPerDisplay)
                            .map((repoId, index) => {
                              const repoDetails = getRepoName(repoId).data
                              return (
                                <Col className="cols" md={4} sm={4} xs={4} key={`${index}-smell`}>
                                  <CodeSmellAndIssuesClientWidget
                                    key={`${index}-smell`}
                                    apiEndPointUrl={`${baseUrl}/client-dashboard-service/projects/codeHealth/techDebt?monthCount=${selectedMonth.monthCount}&iscProjectId=${dataProject[0].id}&repoId=${repoId}`}
                                    lineName={['Coverage', 'Duplication']}
                                    repositoryName={repoDetails.repoName}
                                    requiredParams={[
                                      { isSonarAvailable: repoDetails['isSonarAvailable'] },
                                    ]}
                                    errorMessage={
                                      'Does not have access to Sonar for this repository.'
                                    }
                                  />
                                </Col>
                              )
                            })}
                      </Row>
                      <PaginationArrows
                        pageNumber={displayFromFifth}
                        noOfItems={numberPerDisplay}
                        totalItems={[...repoIds]}
                        setPagingCallback={setDisplayFromFifth}
                      />
                    </React.Fragment>
                  </CustomTab>
                  <CustomTab title="Issues/Complexities">
                    <React.Fragment>
                      <Row>
                        {repoIds &&
                          [...repoIds]
                            .splice(displayFromSixth, numberPerDisplay)
                            .map((repoId, index) => {
                              const repoDetails = getRepoName(repoId).data
                              return (
                                <Col
                                  className="cols"
                                  md={4}
                                  sm={4}
                                  xs={4}
                                  key={`${index}-complexity`}
                                >
                                  <CodeSmellAndIssuesClientWidget
                                    key={`${index}-complexity`}
                                    apiEndPointUrl={`${baseUrl}/client-dashboard-service/projects/codeHealth/techDebt?monthCount=${selectedMonth.monthCount}&iscProjectId=${dataProject[0].id}&repoId=${repoId}`}
                                    lineName={['Issues', 'Complexity']}
                                    repositoryName={repoDetails.repoName}
                                    requiredParams={[
                                      { isSonarAvailable: repoDetails['isSonarAvailable'] },
                                    ]}
                                    errorMessage={
                                      'Does not have access to Sonar for this repository.'
                                    }
                                  />
                                </Col>
                              )
                            })}
                      </Row>
                      <PaginationArrows
                        pageNumber={displayFromSixth}
                        noOfItems={numberPerDisplay}
                        totalItems={[...repoIds]}
                        setPagingCallback={setDisplayFromSixth}
                      />
                    </React.Fragment>
                  </CustomTab>
                </ParentTabSection>
              </div>

              <SectionBox
                key={`security-compliance-score-${repoIds.join('')}-${selectedMonth.monthCount}`}
                repoIds={repoIds}
                numberPerDisplay={numberPerDisplay}
                title={'Security Compliance Score'}
                Widget={SecurityComplianceScoreWidget}
                apiEndPointUrl={`${baseUrl}/client-dashboard-service/projects/codeHealth/score`}
                projectId={dataProject[0].id}
                monthCount={selectedMonth.monthCount}
                getRepoName={getRepoName}
                requiredParams={'isSonarAvailable'}
                errorMessage={'Does not have access to Sonar for this repository.'}
              />
            </div>
          </Scrollbars>
        </React.Fragment>
      )}
    </StaticPopupPage>
  )
}

const SectionBox = ({
  repoIds,
  numberPerDisplay,
  title,
  Widget,
  apiEndPointUrl,
  projectId,
  monthCount,
  getRepoName,
  requiredParams,
  errorMessage,
}) => {
  const [displayFromFirst, setDisplayFromFirst] = useState(0)
  return (
    <div className={title ? style.sectionArea : ''}>
      {title ? (
        <div className="titleArea">
          <h2 className="title">{title}</h2>
        </div>
      ) : (
        ''
      )}
      <Row>
        {repoIds &&
          [...repoIds].splice(displayFromFirst, numberPerDisplay).map(repoId => {
            const repoDetails = getRepoName(repoId).data
            return (
              <Col className="cols" md={4} sm={4} xs={4} key={`${repoId}-metrics`}>
                <Widget
                  key={`${repoId}-metrics`}
                  apiEndPointUrl={`${apiEndPointUrl}?monthCount=${monthCount}&repoId=${repoId}&iscProjectId=${projectId}`}
                  repositoryName={repoDetails.repoName}
                  requiredParams={[{ [requiredParams]: repoDetails[requiredParams] }]}
                  errorMessage={errorMessage}
                />
              </Col>
            )
          })}
      </Row>
      <PaginationArrows
        pageNumber={displayFromFirst}
        noOfItems={numberPerDisplay}
        totalItems={[...repoIds]}
        setPagingCallback={setDisplayFromFirst}
      />
    </div>
  )
}

SectionBox.propTypes = {
  repoIds: PropTypes.any,
  numberPerDisplay: PropTypes.number,
  title: PropTypes.string,
  Widget: PropTypes.any,
  apiEndPointUrl: PropTypes.string,
  projectId: [PropTypes.string, PropTypes.number],
  monthCount: PropTypes.number,
  getRepoName: PropTypes.func,
  requiredParams: PropTypes.any,
  errorMessage: PropTypes.string,
}

CodeHealthDrillDown.propTypes = {
  params: PropTypes.any,
}

export default withRouter(CodeHealthDrillDown)
