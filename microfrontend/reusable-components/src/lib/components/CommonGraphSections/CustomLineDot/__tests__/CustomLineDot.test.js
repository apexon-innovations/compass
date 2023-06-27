import React from 'react'
import CustomLineDot from '../CustomLineDot'
import { render } from '@testing-library/react'

describe('CustomLineDot', () => {
  it('render component', () => {
    render(<CustomLineDot cx={10} cy={6} stroke={'#fff000'} />)
  })
})
