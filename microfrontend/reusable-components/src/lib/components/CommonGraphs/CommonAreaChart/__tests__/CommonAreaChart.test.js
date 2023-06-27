import React from 'react'
import { render } from '@testing-library/react'
import CommonAreaChart from '../CommonAreaChart'
import data, { mockConfigData } from './data'

describe(`CommonAreaChart component`, () => {
  it('CommonAreaChart rendered correctly', () => {
    render(<CommonAreaChart allData={data} />)
  })

  it('CommonAreaChart rendered correctly with different data', () => {
    render(<CommonAreaChart allData={mockConfigData} />)
  })
})
