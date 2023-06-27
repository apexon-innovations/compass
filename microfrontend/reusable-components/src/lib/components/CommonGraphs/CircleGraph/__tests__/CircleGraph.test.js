import React from 'react'
import { render } from '@testing-library/react'
import CircleGraph from '../CircleGraph'
import data, { mockConfigData } from './data'

describe('CircleGraph component', () => {
  it('CircleGraph rendered correctly when props is passed', () => {
    render(<CircleGraph config={data} />)
  })

  it('CircleGraph rendered correctly when props is not passed', () => {
    render(<CircleGraph config={mockConfigData} />)
  })
})
