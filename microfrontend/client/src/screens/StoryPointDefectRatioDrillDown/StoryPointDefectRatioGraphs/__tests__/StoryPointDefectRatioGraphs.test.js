import React from 'react'
import StoryPointDefectRatioGraphs from '../StoryPointDefectRatioGraphs'
import { render } from '@testing-library/react'
import { storeProjectClientData } from 'reusable-components/dist/utils/projectDataStoreFunction'
import { mockProjectData } from '../../../Client/__tests__/data'

describe('src/screens/StoryPointDefectRatioDrillDown/StoryPointDefectRatioGraphs/StoryPointDefectRatioGraphs.js', () => {
  it('should render the <StoryPointDefectRatioGraphs/> component correctly ', () => {
    storeProjectClientData('PROJECT_CLIENT_DATA', mockProjectData.data)
    render(<StoryPointDefectRatioGraphs params={{ projectId: 12345 }} />)
  })
})
