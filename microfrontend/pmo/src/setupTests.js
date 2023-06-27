window.matchMedia =
  window.matchMedia ||
  function() {
    return {
      matches: false,
      addListener: function() {},
      removeListener: function() {},
    }
  }

  process.env = Object.assign(process.env, { REACT_APP_API_END_POINT: 'testURL'});


jest.mock('reusable-components/dist/components/withRouter/withRouter', () => {
  return {
    withRouter: (Component) => (props) => <Component {...props} />,
  }
}
);

jest.mock('reusable-components/dist/widgets/StoryPointsDelvsAccWidget/StoryPointsDelvsAccWidget', () => (Component) => (props) => <Component {...props} />)

jest.mock('reusable-components/dist/widgets/StoryReportsWidget/StoryReportsWidget', () => (Component) => (props) => <Component {...props} />)

jest.mock('reusable-components/dist/widgets/TeamVelocityWidget/TeamVelocityWidget', () => (Component) => (props) => <Component {...props} />)

jest.mock('reusable-components/dist/widgets/NPSWidget/NPSWidget', () => (Component) => (props) => <Component {...props} />)

jest.mock('reusable-components/dist/widgets/ProjectProgressWidget/ProjectProgressWidget', () => (Component) => (props) => <Component {...props} />)

jest.mock('reusable-components/dist/widgets/SayDoRatioWidget/SayDoRatioWidget', () => (Component) => (props) => <Component {...props} />)

jest.mock('reusable-components/dist/widgets/SayDoRatioMemberWiseWidget/SayDoRatioMemberWiseWidget', () => (Component) => (props) => <Component {...props} />)

jest.mock('reusable-components/dist/widgets/SprintBurnDownChartWidget/SprintBurnDownChartWidget', () => (Component) => (props) => <Component {...props} />)

jest.mock('reusable-components/dist/components/ProjectRating/ProjectRating', () => (Component) => (props) => <Component {...props} />)

jest.mock('reusable-components/dist/components/CommonComponents/ParentTabSection', () => (Component) => (props) => <Component {...props} />)

jest.mock('reusable-components/dist/components/CommonComponents/BracketBox/BracketBox', () => (Component) => (props) => <Component {...props} />)

jest.mock('reusable-components/dist/widgets/SprintProgressWidget/SprintProgressWidget', () => (Component) => (props) => <Component {...props} />)

jest.mock('reusable-components/dist/widgets/TaskStatusWidget/TaskStatusWidget', () => (Component) => (props) => <Component {...props} />)

jest.mock('reusable-components/dist/widgets/DefectsCountWidget/DefectsCountWidget', () => (Component) => (props) => <Component {...props} />)

jest.mock('reusable-components/dist/widgets/LoggedVsAcceptedWidget/LoggedVsAcceptedWidget', () => (Component) => (props) => <Component {...props} />)

jest.mock('reusable-components/dist/widgets/BlockersWidget/BlockersWidget', () => (Component) => (props) => <Component {...props} />)

jest.mock('reusable-components/dist/widgets/MemberStatusWidget/MemberStatusWidget', () => (Component) => (props) => <Component {...props} />)

jest.mock('reusable-components/dist/widgets/EffortVarianceWidget/EffortVarianceWidget', () => (Component) => (props) => <Component {...props} />)

jest.mock('reusable-components/dist/widgets/ComplianceWidget/ComplianceWidget', () => (Component) => (props) => <Component {...props} />)

jest.mock('reusable-components/dist/widgets/CalendarViewWidget/CalendarViewWidget', () => (Component) => (props) => <Component {...props} />)

jest.mock('reusable-components/dist/widgets/PRmgmtWidget/PRmgmtWidget', () => (Component) => (props) => <Component {...props} />)

jest.mock('reusable-components/dist/widgets/ProjectRatingComplianceWidget/ProjectRatingComplianceWidget', () => (Component) => (props) => <Component {...props} />)

jest.mock('reusable-components/dist/widgets/QualityMetricsWidget/QualityMetricsWidget', () => (Component) => (props) => <Component {...props} />)

jest.mock('reusable-components/dist/widgets/RepositoryInspectorWidget/RepositoryInspectorWidget', () => (Component) => (props) => <Component {...props} />)

jest.mock('reusable-components/dist/widgets/MemberwiseProductiveCodeWidget/MemberwiseProductiveCodeWidget', () => (Component) => (props) => <Component {...props} />)

jest.mock('reusable-components/dist/widgets/MemberwiseActivityMetricsWidget/MemberwiseActivityMetricsWidget', () => (Component) => (props) => <Component {...props} />)

jest.mock('reusable-components/dist/widgets/ViolationTechWiseWidget/ViolationTechWiseWidget', () => (Component) => (props) => <Component {...props} />)

jest.mock('reusable-components/dist/widgets/ScoreWidget/ScoreWidget', () => (Component) => (props) => <Component {...props} />)

jest.mock('reusable-components/dist/widgets/SecurityScoreWidget/SecurityScoreWidget', () => (Component) => (props) => <Component {...props} />)

jest.mock('reusable-components/dist/widgets/RiskWidget/RiskWidget', () => (Component) => (props) => <Component {...props} />)

jest.mock('reusable-components/dist/widgets/ReviewerCollaborationWidget/ReviewerCollaborationWidget', () => (Component) => (props) => <Component {...props} />)

jest.mock('reusable-components/dist/widgets/ProjectSliderWidget/ProjectSliderWidget', () => (Component) => (props) => <Component {...props} />)
