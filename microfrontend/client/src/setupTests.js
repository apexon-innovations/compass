// jest-dom adds custom jest matchers for asserting on DOM nodes.
// allows you to do things like:
// expect(element).toHaveTextContent(/react/i)
// learn more: https://github.com/testing-library/jest-dom
import '@testing-library/jest-dom';

window.matchMedia =
  window.matchMedia ||
  function () {
    return {
      matches: false,
      addListener: function () { },
      removeListener: function () { },
    }
  }

process.env = Object.assign(process.env, { REACT_APP_API_END_POINT: 'testURL'});


jest.mock('reusable-components/dist/components/withRouter/withRouter', () => {
  return {
    withRouter: (Component) => (props) => <Component {...props} />,
  }
}
);

jest.mock('reusable-components/dist/widgets/ProductHealthOverviewSunBurst/ProductHealthOverviewSunBurst', () => (Component) => (props) => <Component {...props} />)

jest.mock('reusable-components/dist/widgets/ProjectCompletionTrendsWidget/ProjectCompletionTrendsWidget', () => (Component) => (props) => <Component {...props} />)

jest.mock('reusable-components/dist/widgets/StoryPointsDelTrendWidget/StoryPointsDelTrendWidget', () => (Component) => (props) => <Component {...props} />)

jest.mock('reusable-components/dist/widgets/StoryPointsDefectRatioWidget/StoryPointsDefectRatioWidget/StoryPointsDefectRatioWidget', () => (Component) => (props) => <Component {...props} />)

jest.mock('reusable-components/dist/widgets/VelocityTrendsWidget/VelocityTrendsWidget', () => (Component) => (props) => <Component {...props} />)

jest.mock('reusable-components/dist/widgets/DefectTrendsWidget/DefectTrendsWidget', () => (Component) => (props) => <Component {...props} />)

jest.mock('reusable-components/dist/widgets/DefectAgeingWidget/DefectAgeingWidget', () => (Component) => (props) => <Component {...props} />)

jest.mock('reusable-components/dist/components/CommonComponents/ParentTabSection', () => (Component) => (props) => <Component {...props} />)

jest.mock('reusable-components/dist/widgets/CodeHealthSnapshotWidget/CodeHealthSnapshotWidget', () => (Component) => (props) => <Component {...props} />)

jest.mock('reusable-components/dist/widgets/SecurityComplianceScoreWidget/SecurityComplianceScoreWidget', () => (Component) => (props) => <Component {...props} />)

jest.mock('reusable-components/dist/widgets/CodeSmellAndIssuesClientWidget/CodeSmellAndIssuesClientWidget', () => (Component) => (props) => <Component {...props} />)

jest.mock('reusable-components/dist/widgets/CodeTechnicalDebtClientWidget/CodeTechnicalDebtClientWidget', () => (Component) => (props) => <Component {...props} />)

jest.mock('reusable-components/dist/widgets/CodeScoreClientWidget/CodeScoreClientWidget', () => (Component) => (props) => <Component {...props} />)

jest.mock('reusable-components/dist/widgets/CodeViolationClientWidget/CodeViolationClientWidget', () => (Component) => (props) => <Component {...props} />)

jest.mock('reusable-components/dist/widgets/CodeMetricsClientWidget/CodeMetricsClientWidget', () => (Component) => (props) => <Component {...props} />)

jest.mock('reusable-components/dist/components/CommonComponents/CustomDropdown', () => (Component) => (props) => <Component {...props} />)

jest.mock('reusable-components/dist/components/CommonComponents/ModernBox/ModernBox', () => (Component) => (props) => <Component {...props} />)

jest.mock('reusable-components/dist/widgets/DefectAgeingProjectWiseWidget/DefectAgeingProjectWiseWidget', () => (Component) => (props) => <Component {...props} />)

jest.mock('reusable-components/dist/widgets/StoryPointsDelTrendWidget/StoryPointsDelTrendProjectWidget', () => (Component) => (props) => <Component {...props} />)

jest.mock('reusable-components/dist/widgets/StoryPointsDelTrendWidget/StoryPointsDelTrendProjectSprintWidget', () => (Component) => (props) => <Component {...props} />)

jest.mock('reusable-components/dist/components/CommonGraphSections/CustomLegend/CustomLegend', () => (Component) => (props) => <Component {...props} />)

jest.mock('reusable-components/dist/components/CommonGraphs/CommonLineChart/CommonLineChart', () => (Component) => (props) => <Component {...props} />)

jest.mock('reusable-components/dist/widgets/StoryPointsDefectRatioWidget/StoryPointsDefectRatioGraphWidget/StoryPointsDefectRatioGraphWidget', () => (Component) => (props) => <Component {...props} />)

jest.mock('reusable-components/dist/widgets/VelocityTrendsWidget/VelocityTrendsProjectWidget', () => (Component) => (props) => <Component {...props} />)
