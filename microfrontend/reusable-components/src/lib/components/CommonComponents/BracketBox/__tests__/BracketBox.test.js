import React from 'react'
import BracketBox from '../BracketBox'
import { render } from '@testing-library/react'

describe('BracketBox', () => {
  it('render component when needBracketBox is true', () => {
    render(
      <BracketBox
        verticalAlignment="alignTop"
        animStatus=""
        boxTitle="Box Title"
        toolTipId="123"
        infoEnable
      />,
    )
  })

  it('render component when needBracketBox is false', () => {
    render(
      <BracketBox
        verticalAlignment="alignTop"
        animStatus=""
        boxTitle={'Box Title with Child'}
        needBracketBox={false}
      >
        {' '}
        <div>
          <p>Child element</p>
        </div>{' '}
      </BracketBox>,
    )
  })
})
