import React from 'react'
import ClientBusinessDashboard from '../ClientBusinessDashboard'
import { render } from '@testing-library/react'

describe('src/screens/ClientBusinessDashboard/ClientBusinessDashboard.js', () => {
  it('should render the <ClientBusinessDashboard/> component correctly ', () => {
    render(<ClientBusinessDashboard />)
  })
})
