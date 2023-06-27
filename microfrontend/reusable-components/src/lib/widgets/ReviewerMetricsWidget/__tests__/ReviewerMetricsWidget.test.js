import React from 'react'
import { render, waitFor, cleanup } from '@testing-library/react'
import { baseUrl, projectId, repoId, timeDuration } from '../../../../__tests__/commonTestFunction'
import ReviewerMetricsWidget from '../ReviewerMetricsWidget'
import data from './data'
import { mockJest } from '../../../../__tests__/mockData'

const props = {
  apiEndPointUrl: `${baseUrl}/strategy-service/project/repo/reviewer/metrics?repoIds=${repoId.join(
    ',',
  )}&dayCount=${timeDuration}&iscProjectId=${projectId}`,
}

describe('ReviewerMetricsWidget', () => {
  const unmockedFetch = global.fetch

  afterEach(() => {
    cleanup()
    global.fetch = unmockedFetch
  })

  it('render component', async () => {
    mockJest(200, JSON.stringify(data))
    await waitFor(() => {
      render(<ReviewerMetricsWidget {...props} />)
    })
  })
})
