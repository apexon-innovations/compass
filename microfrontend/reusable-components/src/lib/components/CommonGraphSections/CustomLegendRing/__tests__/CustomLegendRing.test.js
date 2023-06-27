import React from 'react'
import CustomLegendRingData from './data'
import CustomLegendRing from '../CustomLegendRing'
import { render } from '@testing-library/react'

describe('CustomLegendRing', () => {
  it('render component', () => {
    render(<CustomLegendRing payload={CustomLegendRingData} />)
  })
})
