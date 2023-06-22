import React from 'react'
import PageNotFound from '../PageNotFound'
import { render } from '@testing-library/react'

describe('PageNotFound component', () => {
  it('PageNotFound rendered correctly', () => {
    render(<PageNotFound />)
  })
})
