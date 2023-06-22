import React from 'react'
import Overview from '../Overview'
import { mockAllProjectData, mockEmptyBoardProjectData, mockEmptyProjectData, mockProjectData } from '../../Operational/__tests__/data'
import { render, waitFor } from '@testing-library/react'
import { storeProjectData } from 'reusable-components/dist/utils/projectDataStoreFunction'
import { mockJest } from '__tests__/mockData'

describe('src/screens/Overview/Overview.js', () => {
  
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
        cb({ detail: mockProjectData }, { projectId: '607033dc983d0782d078967e' }, () => { })
      }
    });
    await waitFor(() => {
      render(<Overview params={{ id: '607033dc983d0782d078967e' }} navigate={mockFn} />)
    })
    await waitFor(() => {
      render(<Overview params={{ id: '607033dc983d0782d078967e' }} navigate={mockFn} />)
    })
    expect(mockFn).toBeCalled();
  })

  it('test component when getProjectsPsrOverallHealthDetails api is not success', async () => {
    const mockFn = jest.fn();
    storeProjectData(mockProjectData);
    mockJest(403);
    window.addEventListener = jest.fn((event, cb) => {
      if (event === 'updateProjectIcon' || event === 'currentProjectData') {
        cb({ detail: mockProjectData }, { projectId: '607033dc983d0782d078967e' }, () => { })
      }
    });
    await waitFor(() => {
      render(<Overview params={{ id: '607033dc983d0782d078967e' }} navigate={mockFn} />)
    })
    await waitFor(() => {
      render(<Overview params={{ id: '607033dc983d0782d078967e' }} navigate={mockFn} />)
    })
    expect(mockFn).not.toBeCalled();
  })

  it('test component when projectId is passed', async () => {
    storeProjectData(mockProjectData);
    mockJest(200, JSON.stringify(mockAllProjectData));
    const mockFn = jest.fn();
    await waitFor(() => {
      render(<Overview params={{ projectId: '607033dc983d0782d078967e' }} navigate={mockFn} />)
    })
    expect(mockFn).not.toBeCalled();
  })

  it('test when projectData doesnot have board data', async () => {
    storeProjectData(mockEmptyBoardProjectData);
    mockJest(403, JSON.stringify(mockAllProjectData));
    const mockFn = jest.fn();
    await waitFor(() => {
      render(<Overview params={{ projectId: '5e7dac8e215e091c132c8f7f' }} navigate={mockFn} />)
    })
    expect(mockFn).not.toBeCalled();
  })

  it('test when projectData doesnot have projectData', async () => {
    storeProjectData(mockEmptyProjectData);
    mockJest(403, JSON.stringify(mockAllProjectData));
    const mockFn = jest.fn();
    window.addEventListener = jest.fn((event, cb) => {
      if (event === 'updateProjectIcon' || event === 'currentProjectData') {
        cb({ detail: mockProjectData }, { projectId: '' }, () => { })
      }
    });
    await waitFor(() => {
      render(<Overview params={{ projectId: '5e7dac8e215e091c132c8f7f' }} navigate={mockFn} />)
    })
    expect(mockFn).not.toBeCalled();
  })
})
