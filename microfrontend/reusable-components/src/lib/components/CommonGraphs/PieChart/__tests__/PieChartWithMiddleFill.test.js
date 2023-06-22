import React from 'react'
import { render } from '@testing-library/react'
import PieChartWithMiddleFill from '../PieChartWithMiddleFill'
import PieChartWithMiddleFillData from './data'

describe(`PieChartWithMiddleFill component`, () => {
  it('PieChartWithMiddleFill rendered correctly', () => {
    render(<PieChartWithMiddleFill allData={PieChartWithMiddleFillData} />)
  })
})
