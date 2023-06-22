/* eslint-disable */
import React from 'react'
import { render, unmountComponentAtNode } from 'react-dom'
import { act } from 'react-dom/test-utils'
import { sleep } from '../../../../__tests__/commonTestFunction'
import ModalComplianceBox from '../ModalComplianceBox'
import { modelComplianceData, noModelComplianceData } from './data'
const props = { ...modelComplianceData }

describe(`ModalComplianceBox`, () => {
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
      render(<ModalComplianceBox {...{ response: modelComplianceData }} />, container)
    })
    await act(() => sleep(500))
    expect(container).toMatchSnapshot()
  })

  it(`Snapshot No data for project`, async () => {
    act(() => {
      render(<ModalComplianceBox {...{ response: noModelComplianceData }} />, container)
    })
    await act(() => sleep(500))
    expect(container).toMatchSnapshot()
  })
})
