import React from 'react'
import { render, waitFor, cleanup } from '@testing-library/react'
import { baseUrl, projectId } from '../../../../__tests__/commonTestFunction'
import ProductHealthOverviewSunBurst from '../ProductHealthOverviewSunBurst'
import data from './data'
import { mockJest } from '../../../../__tests__/mockData'

const props = {
  apiEndPointUrl: `${baseUrl}/psr-service/project/${projectId}/sprint/bugs`,
}

describe('ProductHealthOverviewSunBurst', () => {
  const unmockedFetch = global.fetch

  afterEach(() => {
    cleanup()
    global.fetch = unmockedFetch
  })

  it('render component', async () => {
    mockJest(200, JSON.stringify(data))
    await waitFor(() => {
      render(<ProductHealthOverviewSunBurst {...props} />)
    })
  })
})
