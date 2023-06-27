import React from 'react'
import { render, waitFor, cleanup } from '@testing-library/react'
import { baseUrl } from '../../../../__tests__/commonTestFunction'
import CodeHealthSnapshotWidget from '../CodeHealthSnapshotWidget'
import { mockJest } from '../../../../__tests__/mockData'
import data from './data'

describe('CodeHealthSnapshotWidget', () => {
  const unmockedFetch = global.fetch

  afterEach(() => {
    cleanup()
    global.fetch = unmockedFetch
  })

  it('render component', async () => {
    mockJest(200, JSON.stringify(data))
    await waitFor(() => {
      render(
        <CodeHealthSnapshotWidget
          apiEndPointUrl={`${baseUrl}/psr-service/project/1232123/sprint/leave`}
          navigate={jest.fn()}
        />,
      )
    })
  })
})
