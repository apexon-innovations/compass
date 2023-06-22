import React from 'react'
import { render, waitFor, cleanup } from '@testing-library/react'
import { baseUrl, projectId, boardId } from '../../../../__tests__/commonTestFunction'
import ProjectSliderWidget from '../ProjectSliderWidget'
import data from './data'
import { mockJest } from '../../../../__tests__/mockData'

const props = {
  apiEndPointUrl: `${baseUrl}/psr-service/project/${projectId}/sprint/bugs/status?boardId=${boardId}`,
}

describe('ProjectSliderWidget', () => {
  const unmockedFetch = global.fetch

  afterEach(() => {
    cleanup()
    global.fetch = unmockedFetch
  })

  it('render component', async () => {
    mockJest(200, JSON.stringify(data))
    await waitFor(() => {
      render(<ProjectSliderWidget {...props} />)
    })
  })
})
