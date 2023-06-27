import React from 'react'
import App from '../App'
import { render } from '@testing-library/react'

jest.mock('reusable-components/dist/components/withRouter/withRouter', () => {
  return {
    withRouter: (Component) => (props) => {
      const prop = { ...props, location: { pathname: '/client/business-overview' } }
      return <Component {...prop} />
    },
  }
}
);
describe('renders learn react link', () => {
  it('<App/> should render correctly ', () => {
    render(<App />)
  })
})
