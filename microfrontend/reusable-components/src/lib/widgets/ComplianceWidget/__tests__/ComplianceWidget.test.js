import React from 'react'
import { render, waitFor, cleanup } from '@testing-library/react'
import { baseUrl, projectId } from '../../../../__tests__/commonTestFunction'
import ComplianceWidget from '../ComplianceWidget'
import data from './data'
import { mockJest } from '../../../../__tests__/mockData'

describe('ComplianceWidget', () => {
  const unmockedFetch = global.fetch

  afterEach(() => {
    cleanup()
    global.fetch = unmockedFetch
  })

  it('render component', async () => {
    mockJest(200, JSON.stringify(data))
    await waitFor(() => {
      render(
        <ComplianceWidget
          apiEndPointUrl={`${baseUrl}/psr-service/project/${projectId}/compliance`}
          navigate={jest.fn()}
        />,
      )
    })
  })
})
