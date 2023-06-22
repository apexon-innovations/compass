import React from 'react'
import ModernBox from '../ModernBox'
import { render } from '@testing-library/react'

describe('ModernBox', () => {
  it('render component correctly', () => {
    render(
      <ModernBox verticalAlignment="alignTop" animStatus="" boxTitle={'Box Title with Child'}>
        {' '}
        <div>
          <p>Child element</p>
        </div>{' '}
      </ModernBox>,
    )
  })
})
