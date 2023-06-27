import React from 'react'
import DashboardSwitch from '../DashboardSwitch'
import { fireEvent, render, screen } from '@testing-library/react'
jest.useFakeTimers()

describe('src/components/DashboardSwitch/DashboardSwitch.js', () => {
  it('should click on current link', () => {
    const mockFn = jest.fn();
    render(
      <DashboardSwitch projectId={'123123'} basePath={'operational'} navigate={mockFn} />
    )
    fireEvent.click(screen.getAllByText('Operational')[0]);
    expect(mockFn).not.toBeCalled();
  })

  it('should click on 2nd link', async() => {
    const mockFn = jest.fn();
    render(
      <DashboardSwitch projectId={'123123'} basePath={'strategic'} navigate={mockFn} />
    )
    fireEvent.click(screen.getAllByText('Overview')[0]);
    jest.advanceTimersByTime(1000)
    expect(mockFn).toBeCalledWith('/pmo/overview/123123');
  })

  it('test when basePath is different', async() => {
    const mockFn = jest.fn();
    render(
      <DashboardSwitch projectId={'123123'} basePath={'strategic1'} navigate={mockFn} />
    )
    fireEvent.click(screen.getAllByText('Overview')[0]);
    expect(mockFn).not.toBeCalled();
  })

  it('test when projectId is not passed', async() => {
    const mockFn = jest.fn();
    render(
      <DashboardSwitch basePath={'strategic'} navigate={mockFn} />
    )
    expect(mockFn).not.toBeCalled();
  })
})
