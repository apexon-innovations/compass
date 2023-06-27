import React from 'react'
import { render } from '@testing-library/react'
import DefectTrendsDrillDown from '../DefectTrendsDrillDown'
import { storeProjectClientData } from 'reusable-components/dist/utils/projectDataStoreFunction'
import { mockProjectData } from '../../Client/__tests__/data'

describe('src/screens/DefectTrendsDrillDown/DefectTrendsDrillDown.js', () => {
  it('should render the <DefectTrendsDrillDown/> component correctly ', () => {
    storeProjectClientData('PROJECT_CLIENT_DATA', mockProjectData.data)
    render(<DefectTrendsDrillDown />)
  })
})
