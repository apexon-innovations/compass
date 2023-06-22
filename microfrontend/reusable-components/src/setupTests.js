// jest-dom adds custom jest matchers for asserting on DOM nodes.
// allows you to do things like:
// expect(element).toHaveTextContent(/react/i)
// learn more: https://github.com/testing-library/jest-dom
import '@testing-library/jest-dom'

window.matchMedia =
  window.matchMedia ||
  function() {
    return {
      matches: false,
      addListener: function() {},
      removeListener: function() {},
    }
  }
global.console = {
  // eslint-disable-next-line no-console
  log: console.log, // console.log are ignored in tests

  // Keep native behaviour for other methods, use those to print out things in your own tests, not `console.log`
  error: jest.fn(),
  warn: jest.fn(),
  // eslint-disable-next-line no-console
  info: console.info,
  // eslint-disable-next-line no-console
  debug: console.debug,
}

if (!SVGElement.prototype.getTotalLength) {
  SVGElement.prototype.getTotalLength = () => 1
}

class ResizeObserver {
  observe() {}
  unobserve() {}
  disconnect() {}
}

window.ResizeObserver = ResizeObserver

jest.mock('../src/lib/components/withRouter/withRouter', () => {
  return {
    withRouter: Component => props => <Component {...props} />,
  }
})
