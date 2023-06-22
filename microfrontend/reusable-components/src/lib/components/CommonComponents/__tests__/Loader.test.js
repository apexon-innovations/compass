import React from 'react'
import Loader from '../Loader'
import { render } from '@testing-library/react'

describe(`Loader Component`, () => {
  it('Loader rendered correctly', () => {
    render(<Loader />)
  })
})
