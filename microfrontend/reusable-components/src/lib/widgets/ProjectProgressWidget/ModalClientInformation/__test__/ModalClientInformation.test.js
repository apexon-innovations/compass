/* eslint-disable */
import React from 'react'
import { render, unmountComponentAtNode } from 'react-dom'
import { act } from 'react-dom/test-utils'
import { sleep } from '../../../../../__tests__/commonTestFunction'
import ModalClientInformation from '../ModalClientInformation'
import data from './data'
const props = { ...data }

describe(`ModalClientInformation`, () => {
  let container = null

  beforeEach(() => {
    // set up a DOM element as a render target
    container = document.createElement('div')
    document.body.appendChild(container)
  })

  afterEach(() => {
    // cleanup on exiting
    unmountComponentAtNode(container)
    container.remove()
    container = null
  })

  it(`Snapshot`, async () => {
    act(() => {
      render(<ModalClientInformation {...props} />, container)
    })
    await act(() => sleep(500))
    expect(container).toMatchSnapshot()
  })
})
