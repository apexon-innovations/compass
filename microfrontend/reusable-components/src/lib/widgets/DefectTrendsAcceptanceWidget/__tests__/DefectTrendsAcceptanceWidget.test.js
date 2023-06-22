import React from 'react'
import { render, waitFor, cleanup } from '@testing-library/react'
import { baseUrl, selectedProjects } from '../../../../__tests__/commonTestFunction'
import DefectTrendsAcceptanceWidget from '../DefectTrendsAcceptanceWidget'
import data from './data'
import { mockJest } from '../../../../__tests__/mockData'

const props = {
  apiEndPointUrl: `${baseUrl}/client-dashboard-service/projects/defectTrends/acceptance?iscProjectIds=${selectedProjects.join(
    ',',
  )}`,
}

describe('DefectTrendsAcceptanceWidget', () => {
  const unmockedFetch = global.fetch

  afterEach(() => {
    cleanup()
    global.fetch = unmockedFetch
  })

  it('render component', async () => {
    mockJest(200, JSON.stringify(data))
    await waitFor(() => {
      render(<DefectTrendsAcceptanceWidget {...props} />)
    })
  })
})
