import React from 'react'
import App from '../App'
import { render } from '@testing-library/react'

jest.mock('reusable-components/dist/components/withRouter/withRouter', () => {
  return {
    withRouter: (Component) => (props) => {
      const prop = { ...props, location: { pathname: '/pmo/123456' } }
      return <Component {...prop} />
    },
  }
}
);
describe('App', () => {
  it('<App/> should render correctly ', () => {
    render(<App />)
  })
})
