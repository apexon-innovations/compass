import React from 'react'
import DashboardSwitchForTwo from '../DashboardSwitchForTwo'
import { render, screen, fireEvent } from '@testing-library/react'

describe('src/components/DashboardSwitchForTwo/DashboardSwitchForTwo.js', () => {
  it('test onPageChange when Overview is clicked ', () => {
    const mockFn = jest.fn();
    render(
      <DashboardSwitchForTwo navigate={mockFn} basePath='overview' projectId='12312' />,
    )
    fireEvent.click(screen.getByText('Operational'));
    expect(mockFn).toHaveBeenCalledWith("/client/overview");
  })

  it('test onPageChange when Business is clicked and basePath is business-overview', () => {
    const mockFn = jest.fn();
    render(
      <DashboardSwitchForTwo navigate={mockFn} basePath='business-overview' projectId='12312' />,
    )
    fireEvent.click(screen.getByText('Business'));
    expect(mockFn).toHaveBeenCalledWith("/client/business-overview");
  })

  it('test onPageChange when Business is clicked and basePath is not business-overview and overview', () => {
    const mockFn = jest.fn();
    render(
      <DashboardSwitchForTwo navigate={mockFn} basePath='business-overview1' projectId='12312' />,
    )
    fireEvent.click(screen.getByText('Business'));
    expect(mockFn).toHaveBeenCalledWith("/client/business-overview");
  })
})
