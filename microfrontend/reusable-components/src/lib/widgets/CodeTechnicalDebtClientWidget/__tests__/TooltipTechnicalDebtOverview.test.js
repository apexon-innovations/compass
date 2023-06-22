/* eslint-disable */
import React from 'react'
import { render, unmountComponentAtNode } from 'react-dom'
import { act } from 'react-dom/test-utils'
import { sleep } from '../../../../__tests__/commonTestFunction'
import TooltipTechnicalDebtOverview from '../TooltipTechnicalDebtOverview'
import { toolTipData } from './data'

const props = { ...toolTipData }

describe(`TooltipTechnicalDebtOverview`, () => {
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
      render(<TooltipTechnicalDebtOverview {...props} />, container)
    })
    await act(() => sleep(500))
    expect(container).toMatchSnapshot()
  })

  it(`Snapshot No data`, async () => {
    act(() => {
      render(<TooltipTechnicalDebtOverview />, container)
    })
    await act(() => sleep(500))
    expect(container).toMatchSnapshot()
  })
})
