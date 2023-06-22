import React from 'react'
import Strategic from '../Strategic'
import { storeProjectData } from 'reusable-components/dist/utils/projectDataStoreFunction'
import { mockJest } from '__tests__/mockData'
import { mockAllProjectData, mockEmptyBoardProjectData, mockEmptyProjectData, mockProjectData } from '../../Operational/__tests__/data'
import { render, waitFor } from '@testing-library/react'
import { mockStrategicProductData } from './data'

describe('src/screens/Strategic/Strategic.js', () => {

  const unmockedFetch = global.fetch;
  
  afterAll(() => {
    global.fetch = unmockedFetch;
  });
  
  
  it('test component when projectId is not passed', async () => {
    const mockFn = jest.fn();
    storeProjectData(mockProjectData);
    mockJest(200, JSON.stringify(mockAllProjectData));
    window.addEventListener = jest.fn((event, cb) => {
      if (event === 'updateProjectIcon' || event === 'currentProjectData') {
        cb({ detail: mockProjectData })
      }
    });
    await waitFor(() => {
      render(<Strategic params={{ id: '607033dc983d0782d078967e' }} navigate={mockFn} />)
    })
    await waitFor(() => {
      render(<Strategic params={{ id: '607033dc983d0782d078967e' }} navigate={mockFn} />)
    })
    expect(mockFn).toBeCalled();
  })

  it('test getAllProjectApi when projectId doesnot matches', async () => {
    const mockFn = jest.fn();
    storeProjectData(mockProjectData);
    mockJest(200, JSON.stringify(mockStrategicProductData));
    await waitFor(() => {
      render(<Strategic params={{ id: '607033dc983d0782d078967e' }} navigate={mockFn} />)
    })
    await waitFor(() => {
      render(<Strategic params={{ id: '607033dc983d0782d078967e' }} navigate={mockFn} />)
    })
    expect(mockFn).toBeCalled();
  })

  it('test component when projectId is passed', async () => {
    storeProjectData(mockProjectData);
    mockJest(200, JSON.stringify(mockAllProjectData));
    const mockFn = jest.fn();
    await waitFor(() => {
      render(<Strategic params={{ projectId: '607033dc983d0782d078967e' }} navigate={mockFn} />)
    })
    expect(mockFn).not.toBeCalled();
  })

  it('test when projectData doesnot have board data', async () => {
    storeProjectData(mockEmptyBoardProjectData);
    mockJest(403, JSON.stringify(mockAllProjectData));
    const mockFn = jest.fn();
    await waitFor(() => {
      render(<Strategic params={{ projectId: '5e7dac8e215e091c132c8f7f' }} navigate={mockFn} />)
    })
    expect(mockFn).not.toBeCalled();
  })

  it('test when projectData doesnot have projectData', async () => {
    storeProjectData(mockEmptyProjectData);
    mockJest(403, JSON.stringify(mockAllProjectData));
    const mockFn = jest.fn();
    window.addEventListener = jest.fn((event, cb) => {
      if (event === 'updateProjectIcon' || event === 'currentProjectData') {
        cb({ detail: mockProjectData })
      }
    });
    await waitFor(() => {
      render(<Strategic params={{ projectId: '5e7dac8e215e091c132c8f7f' }} navigate={mockFn} />)
    })
    expect(mockFn).not.toBeCalled();
  })

  it('test handleUpdatedProjectData when data doesnot matches', async () => {
    storeProjectData(mockEmptyProjectData);
    mockJest(403, JSON.stringify(mockAllProjectData));
    const mockFn = jest.fn();
    window.addEventListener = jest.fn((event, cb) => {
      if (event === 'currentProjectData') {
        cb({ detail: mockEmptyProjectData })
      }
    });
    await waitFor(() => {
      render(<Strategic params={{ projectId: '5e7dac8e215e091c132c8f7f' }} navigate={mockFn} />)
    })
    expect(mockFn).not.toBeCalled();
  })
})
