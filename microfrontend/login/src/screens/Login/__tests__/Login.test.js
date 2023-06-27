import React from 'react'
import Login from '../Login'
import { render } from '@testing-library/react'

describe('src/screens/Login/Login.js', () => {
  it('should render the <MainLandingPage/> component correctly ', () => {
    render(<Login />)
  })
})
