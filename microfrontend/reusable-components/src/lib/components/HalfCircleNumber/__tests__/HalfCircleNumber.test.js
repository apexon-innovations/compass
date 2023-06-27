import React from 'react'
import HalfCircleNumber from '../HalfCircleNumber'
import { render } from '@testing-library/react'

describe('HalfCircleNumber', () => {
  it('render component correctly', async () => {
    render(
      <HalfCircleNumber className="alignTop" value={50}>
        {' '}
        <div>
          <p>Child element</p>
        </div>{' '}
      </HalfCircleNumber>,
    )
  })
})
