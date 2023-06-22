import React from 'react'
import { render } from '@testing-library/react'
import PieHoverChart from '../PieHoverChart'
import { PieChartHoverData } from './data'

describe(`PieHoverChart component`, () => {
  it('PieHoverChart rendered correctly', () => {
    render(<PieHoverChart allData={PieChartHoverData} />)
  })
})
