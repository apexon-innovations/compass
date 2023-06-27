import React from 'react'
import DeliveryTrendsProjectWiseDrillDown from '../DeliveryTrendsProjectWiseDrillDown'
import { render } from '@testing-library/react'

describe('src/screens/DeliveryTrendsDrillDown/DeliveryTrendsProjectWiseDrillDown.js', () => {
  it('should render the <DeliveryTrendsProjectWiseDrillDown/> component correctly ', () => {
    render(
      <DeliveryTrendsProjectWiseDrillDown params={{ projectId: 10003 }} />,
    )
  })
})
