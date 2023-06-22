/* eslint-disable */
import React from 'react'
import { render } from '@testing-library/react'
import CommonBarChart from '../CommonBarChart'
import data, { mockConfigData } from './data'

describe(`CommonBarChart component`, () => {
  it('CommonBarChart rendered correctly', () => {
    render(<CommonBarChart allData={data} />)
  })

  it('CommonBarChart rendered correctly with different data', () => {
    render(<CommonBarChart allData={mockConfigData} />)
  })
})
