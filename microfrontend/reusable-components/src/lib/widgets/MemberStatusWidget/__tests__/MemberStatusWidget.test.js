import React from 'react'
import { render, waitFor, cleanup } from '@testing-library/react'
import { baseUrl, projectId } from '../../../../__tests__/commonTestFunction'
import MemberStatusWidget from '../MemberStatusWidget'
import data from './data'
import { mockJest } from '../../../../__tests__/mockData'

const props = {
  apiEndPointUrl: `${baseUrl}/psr-service/project/${projectId}/compliance`,
  memberJiraInfoApiEndPoint: `${baseUrl}/psr-service/project/${projectId}/compliance`,
  memberNestInfoApiEndPoint: `${baseUrl}/psr-service/project/${projectId}/compliance`,
  boardId: 'x12sd',
}

describe('MemberStatusWidget', () => {
  const unmockedFetch = global.fetch

  afterEach(() => {
    cleanup()
    global.fetch = unmockedFetch
  })

  it('render component', async () => {
    mockJest(200, JSON.stringify(data))
    await waitFor(() => {
      render(<MemberStatusWidget {...props} />)
    })
  })
})
