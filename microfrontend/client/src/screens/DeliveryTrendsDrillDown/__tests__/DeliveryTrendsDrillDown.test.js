import React from 'react'
import DeliveryTrendsDrillDown from '../DeliveryTrendsDrillDown'
import { render } from '@testing-library/react'
import { storeProjectClientData } from 'reusable-components/dist/utils/projectDataStoreFunction'
import { mockProjectData } from '../../Client/__tests__/data'

describe('src/screens/DeliveryTrendsDrillDown/DeliveryTrendsDrillDown.js', () => {
  it('should render the <DeliveryTrendsDrillDown/> component correctly ', () => {
    storeProjectClientData('PROJECT_CLIENT_DATA', mockProjectData.data)
    render(<DeliveryTrendsDrillDown />)
  })
})
