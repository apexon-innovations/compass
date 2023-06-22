import React from 'react'
import { render, waitFor, cleanup } from '@testing-library/react'
import { baseUrl, projectId, repoId } from '../../../../__tests__/commonTestFunction'
import RepositoryInspectorWidget from '../RepositoryInspectorWidget'
import data from './data'
import { mockJest } from '../../../../__tests__/mockData'

const props = {
  apiEndPointUrl: `${baseUrl}/strategy-service/project/repo/techdebt/metrics?repoIds=${repoId.join(
    ',',
  )}&iscProjectId=${projectId}`,
}

describe('RepositoryInspectorWidget', () => {
  const unmockedFetch = global.fetch

  afterEach(() => {
    cleanup()
    global.fetch = unmockedFetch
  })

  it('render component', async () => {
    mockJest(200, JSON.stringify(data))
    await waitFor(() => {
      render(<RepositoryInspectorWidget {...props} />)
    })
  })
})
