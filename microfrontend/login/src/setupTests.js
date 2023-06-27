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

  process.env = Object.assign(process.env, { REACT_APP_API_END_POINT: 'http://testURL/'});


jest.mock('reusable-components/dist/components/withRouter/withRouter', () => {
  return {
    withRouter: (Component) => (props) => <Component {...props} />,
  }
}
);

jest.mock('reusable-components/dist/components/Footer/Footer', () => (Component) => (props) => <Component {...props} />)
