import React from 'react'
import NoDataAvailable from '../NoDataAvailable'
import { render } from '@testing-library/react'

describe(`NoDataAvailable Box component`, () => {
  it('NoDataAvailable rendered correctly', () => {
    render(<NoDataAvailable />)
  })
})
