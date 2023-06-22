import React from 'react'
import { render, waitFor, cleanup } from '@testing-library/react'
import { baseUrl } from '../../../../__tests__/commonTestFunction'
import { mockJest } from '../../../../__tests__/mockData'
import BlockersWidget from '../BlockersWidget'
import data from './data'

describe('BlockersWidget', () => {
  const unmockedFetch = global.fetch

  afterEach(() => {
    cleanup()
    global.fetch = unmockedFetch
  })

  it('render component', async () => {
    mockJest(200, JSON.stringify(data))
    await waitFor(() => {
      render(
        <BlockersWidget
          apiEndPointUrl={`${baseUrl}/psr-service/project/1232123/sprint/blockers`}
        />,
      )
    })
  })
})
