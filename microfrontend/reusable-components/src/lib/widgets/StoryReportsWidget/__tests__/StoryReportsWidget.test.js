import React from 'react'
import { render, waitFor, cleanup } from '@testing-library/react'
import { baseUrl } from '../../../../__tests__/commonTestFunction'
import StoryReportsWidget from '../StoryReportsWidget'
import data from './data'
import { mockJest } from '../../../../__tests__/mockData'

const props = {
  apiEndPointUrl: `${baseUrl}/psr-service/project/1232123/sprint/leave`,
}

describe('StoryReportsWidget', () => {
  const unmockedFetch = global.fetch

  afterEach(() => {
    cleanup()
    global.fetch = unmockedFetch
  })

  it('render component', async () => {
    mockJest(200, JSON.stringify(data))
    await waitFor(() => {
      render(<StoryReportsWidget {...props} />)
    })
  })
})
