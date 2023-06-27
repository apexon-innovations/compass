import React from 'react'
import CodeHealthDrillDown from '../CodeHealthDrillDown'
import { render, waitFor } from '@testing-library/react'
import { mockProjectData } from '../../Client/__tests__/data'
import { storeProjectClientData } from 'reusable-components/dist/utils/projectDataStoreFunction'

describe('src/screens/ClientCodeHealthDrillDown/CodeHealthDrillDown.js', () => {
  it('should render the <CodeHealthDrillDown/> component correctly ', async () => {
    storeProjectClientData('PROJECT_CLIENT_DATA', mockProjectData.data)
    await waitFor(() => {
      render(<CodeHealthDrillDown params={{ projectId: '607033dc983d0782d078967e' }} />)
    })
  })
})
