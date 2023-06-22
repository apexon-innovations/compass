import React from 'react'
import { render } from '@testing-library/react'
import CompletionTrendsDrillDown from '../CompletionTrendsDrillDown'
import { mockProjectData } from '../../Client/__tests__/data'
import { storeProjectClientData } from 'reusable-components/dist/utils/projectDataStoreFunction'

describe('src/screens/CompletionTrendsDrillDown/CompletionTrendsDrillDown.js', () => {
  it('should render the <CompletionTrendsDrillDown/> component correctly ', () => {
    storeProjectClientData('PROJECT_CLIENT_DATA', mockProjectData.data)
    render(<CompletionTrendsDrillDown />)
  })

})
