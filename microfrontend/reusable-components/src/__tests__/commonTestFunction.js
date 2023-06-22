/* eslint-disable */
export const jestMockFunction = data => {
  return () => {
    const mockSuccessResponse = JSON.stringify({
      status: 200,
      data: data.data,
    })
    const mockJsonPromise = Promise.resolve(mockSuccessResponse) // 2
    const mockFetchPromise = Promise.resolve({
      text: () => mockJsonPromise,
      status: 200,
      headers: {},
    })
    jest.spyOn(global, 'fetch').mockImplementation(() => {
      return mockFetchPromise
    })
  }
}

export const sleep = ms => {
  return new Promise(resolve => setTimeout(resolve, ms))
}

export const projectId = 123123

export const boardId = 1

export const timeDuration = 7

export const repoId = ['F6edsBx7', 'Q5eaFVx9']

export const selectedProjects = [123123, 2323332]

export const baseUrl = process.env.REACT_APP_API_END_POINT
