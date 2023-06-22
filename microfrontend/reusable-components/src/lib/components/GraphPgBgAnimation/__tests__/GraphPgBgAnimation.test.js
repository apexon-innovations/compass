import React from 'react'
import GraphPgBgAnimation from '../GraphPgBgAnimation'
import { render } from '@testing-library/react'

describe('GraphPgBgAnimation', () => {
  it('render component correctly', () => {
    render(<GraphPgBgAnimation themeColor={'#000fff'} />)
  })
})
