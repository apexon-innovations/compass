import React from 'react'
import { render, waitFor, cleanup } from '@testing-library/react'
import { baseUrl } from '../../../../__tests__/commonTestFunction'
import VelocityTrendsWidget from '../VelocityTrendsWidget'
import data from './data'
import { mockJest } from '../../../../__tests__/mockData'

const props = {
  apiEndPointUrl: `${baseUrl}/client-dashboard-service/projects`,
  selectedPointCount: 'Point',
  showHideButton: true,
  idealLine: false,
  boxTitle: true,
  backLink: true,
  colorCodeIndex: 1,
}

describe('VelocityTrendsWidget', () => {
  const unmockedFetch = global.fetch

  afterEach(() => {
    cleanup()
    global.fetch = unmockedFetch
  })

  afterAll(() => {
    global.getStoredData = () => {
      return [
        {
          boards: 123,
          currentSprintId: 12,
          currentSprintName: 123,
          id: 21123,
          jiraProjectId: 123,
          name: 'RxSense PBM Admin',
          colorCode: '#sdfsdf',
        },
        {
          boards: 123,
          currentSprintId: 12,
          currentSprintName: 123,
          id: 21123,
          jiraProjectId: 123,
          name: 'RxSense-DWH',
          colorCode: '#123SDF',
        },
      ]
    }
  })

  it('render component', async () => {
    mockJest(200, JSON.stringify(data))
    await waitFor(() => {
      render(<VelocityTrendsWidget {...props} />)
    })
  })
})
