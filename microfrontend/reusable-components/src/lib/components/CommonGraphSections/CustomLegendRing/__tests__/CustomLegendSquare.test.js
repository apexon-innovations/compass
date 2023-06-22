import React from 'react'
import CustomLegendSquare from '../CustomLegendSquare'
import { render } from '@testing-library/react'
import CustomLegendRingData from './data'

describe('CustomLegendSquare', () => {
  it('render component', () => {
    render(<CustomLegendSquare payload={CustomLegendRingData} />)
  })
})
