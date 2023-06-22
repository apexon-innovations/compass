import React from 'react'
import { render, waitFor, cleanup } from '@testing-library/react'
import { baseUrl } from '../../../../__tests__/commonTestFunction'
import CalendarViewWidget from '../CalendarViewWidget'
import data from './data'
import { mockJest } from '../../../../__tests__/mockData'

describe('CalendarViewWidget', () => {
  const unmockedFetch = global.fetch

  afterEach(() => {
    cleanup()
    global.fetch = unmockedFetch
  })

  it('Snapshot', async () => {
    mockJest(200, JSON.stringify(data))
    await waitFor(() => {
      render(
        <CalendarViewWidget
          apiEndPointUrl={`${baseUrl}/psr-service/project/1232123/sprint/leave`}
          sprintId="123"
        />,
      )
    })
  })
})
