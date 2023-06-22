import React from 'react'
import SquareBox from '../SquareBox'
import { render } from '@testing-library/react'

describe('SquareBox', () => {
  it('render component correctly', () => {
    render(
      <SquareBox boxTitle={'SquareBox Title with Child'}>
        {' '}
        <div>
          <p>Child element</p>
        </div>{' '}
      </SquareBox>,
    )
  })
})
