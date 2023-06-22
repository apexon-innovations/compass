import React from 'react'
import ModalContainer from '../ModalContainer'
import { render } from '@testing-library/react'

describe('ModalContainer', () => {
  it('render component', () => {
    render(
      <ModalContainer>
        <div>Model Data</div>
      </ModalContainer>,
    )
  })
})
