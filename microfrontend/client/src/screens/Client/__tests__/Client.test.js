import React from 'react'
import Client from '../Client'
import { render, screen, waitFor } from '@testing-library/react'
import { mockJest } from '__tests__/mockData'
import { mockProjectData } from './data'


describe('<Operational/>', () => {
  it('should check the presence of child components ', async () => {
    mockJest(200, JSON.stringify(mockProjectData));

    await waitFor(() => {
      render(<Client />); // when isLoading is true
    })

    await waitFor(() => {
      render(<Client />); // when isLoading is false
      expect(screen.queryByText('Project Completion Trend')).toBeDefined();
    })
  })
})
