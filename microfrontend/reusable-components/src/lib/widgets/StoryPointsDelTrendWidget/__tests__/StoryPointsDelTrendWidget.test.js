import React from 'react'
import { render, waitFor, cleanup } from '@testing-library/react'
import { baseUrl, projectId } from '../../../../__tests__/commonTestFunction'
import StoryPointsDelTrendWidget from '../StoryPointsDelTrendWidget'
import data from './data'
import { mockJest } from '../../../../__tests__/mockData'

const props = {
  apiEndPointUrl: `${baseUrl}/psr-service/project/${projectId}/sprint/member/status`,
  zoomEnable: true,
  isFilter: true,
  selectedPointCount: 'Point',
  projectDetailsLinkUrl: '/test',
  viewType: 'main',
}

describe('StoryPointsDelTrendWidget', () => {
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
      render(<StoryPointsDelTrendWidget {...props} />)
    })
  })
})
