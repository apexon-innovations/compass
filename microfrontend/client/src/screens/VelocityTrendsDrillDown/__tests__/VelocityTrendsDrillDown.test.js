import React from 'react'
import VelocityTrendsDrillDown from '../VelocityTrendsDrillDown'
import { render } from '@testing-library/react'
import { storeProjectClientData } from 'reusable-components/dist/utils/projectDataStoreFunction'
import { mockProjectData } from '../../Client/__tests__/data'

describe('src/screens/VelocityTrendsDrillDown/VelocityTrendsDrillDown.js', () => {
  it('should render the <VelocityTrendsDrillDown/> component correctly ', () => {
    storeProjectClientData('PROJECT_CLIENT_DATA', mockProjectData.data)
    render(<VelocityTrendsDrillDown />)
  })
})
