import React from 'react'
import { render } from '@testing-library/react'
import CommonLineChart from '../CommonLineChart'
import data from './data'

describe(`CommonLineChart component`, () => {
  it('CommonLineChart rendered correctly', () => {
    render(
      <CommonLineChart
        allData={data}
        customTickFnc={jest.fn()}
        customLegendFnc={jest.fn()}
        customToolTip={true}
      />,
    )
  })
})
