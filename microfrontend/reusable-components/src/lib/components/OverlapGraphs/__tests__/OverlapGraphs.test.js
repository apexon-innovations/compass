import React from 'react'
import { render } from '@testing-library/react'
import OverlapGraphs from '../OverlapGraphs'
import data from './data'

describe('OverlapGraphs', () => {
  it('render component', () => {
    render(<OverlapGraphs graphData={data} />)
  })
})
