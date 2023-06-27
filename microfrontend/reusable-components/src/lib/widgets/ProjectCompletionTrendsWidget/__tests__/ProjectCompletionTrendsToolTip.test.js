/* eslint-disable */
import React from 'react'
import { BrowserRouter as Router } from 'react-router-dom'
import { render, unmountComponentAtNode } from 'react-dom'
import { act } from 'react-dom/test-utils'
import { sleep, projectId, baseUrl } from '../../../../__tests__/commonTestFunction'
import ProjectCompletionTrendsToolTip from '../ProjectCompletionTrendsToolTip'
import { toolTipData, toolTipData1 } from './data'

const props = { ...toolTipData }

describe(`ProjectCompletionTrendsToolTip`, () => {
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
      render(
        <Router>
          <ProjectCompletionTrendsToolTip {...props} />
        </Router>,
        container,
      )
    })
    await act(() => sleep(500))
    expect(container).toMatchSnapshot()
  })

  it(`Snapshot No Data`, async () => {
    act(() => {
      render(
        <Router>
          <ProjectCompletionTrendsToolTip />
        </Router>,
        container,
      )
    })
    await act(() => sleep(500))
    expect(container).toMatchSnapshot()
  })

  it(`Snapshot No Line Index`, async () => {
    act(() => {
      render(
        <Router>
          <ProjectCompletionTrendsToolTip {...toolTipData1} />
        </Router>,
        container,
      )
    })
    await act(() => sleep(500))
    expect(container).toMatchSnapshot()
  })
})
