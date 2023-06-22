import React from 'react'
import { render } from '@testing-library/react'
import customLegendData from './data'
import CustomLegend from '../CustomLegend'

describe('CustomLegend', () => {
  it('render component with Callback', () => {
    render(<CustomLegend payload={customLegendData} activeLegendName="test" callback={() => {}} />)
  })
})
