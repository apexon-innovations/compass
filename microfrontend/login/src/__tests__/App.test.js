import React from 'react'
import App from '../App'
import { render } from '@testing-library/react'

describe('renders learn react link', () => {
  it('<App/> should render correctly ', () => {
    render(<App />)
  })
})
