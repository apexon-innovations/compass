/* eslint-disable testing-library/no-unnecessary-act */
// eslint rule is disable as we have to use act() in unit testing
import { renderHook, cleanup, act, waitFor } from '@testing-library/react'
import * as apiWrapper from '../useWidgetAPIWrapper'
import { responseData } from './data'

describe('test custom hooks', () => {
  let baseURL
  const unmockedFetch = global.fetch
  beforeEach(() => {
    baseURL = process.env.REACT_APP_API_END_POINT
  })
  afterEach(() => {
    cleanup()
    global.fetch = unmockedFetch
  })

  test('required parameter testing', () => {
    const hookData = {
      requestType: 'GET',
      requiredParams: [{ apiEndPointUrl: '' }, { requestType: '' }],
    }
    const { result } = renderHook(() => apiWrapper.default(hookData))
    expect(result.current.response).toBe(false)
  })

  test('required parameter testing having errrorMessage', () => {
    const hookData = {
      requestType: 'GET',
      requiredParams: [{ apiEndPointUrl: '' }, { requestType: '' }],
      errorMessage: 'Please pass apiendpoint',
    }
    const { result } = renderHook(() => apiWrapper.default(hookData))
    expect(result.current.response).toBe(false)
  })

  test('need to pass api end point testing', () => {
    const hookData = {
      requestType: 'GET',
      data: [],
      requiredParams: [{ apiEndPointUrl: `${baseURL}/products/1` }, { requestType: 'GET' }],
    }
    const { result } = renderHook(() => apiWrapper.default(hookData))
    expect(result.current.response).toBe(false)
  })

  test('apiCall testing in custom Hook', async () => {
    let data
    const hookData = {
      requestType: 'GET',
      apiEndPointUrl: `${baseURL}/products/1`,
      requiredParams: [{ apiEndPointUrl: `${baseURL}/products/1` }, { requestType: 'GET' }],
      callback: jest.fn(),
    }
    global.fetch = () =>
      Promise.resolve({
        status: 200,
        text: () => Promise.resolve(JSON.stringify(responseData)),
      })
    await act(() => {
      const { result } = renderHook(() => apiWrapper.default(hookData))
      data = result
    })
    await waitFor(() => {
      expect(data.current.response).toStrictEqual(responseData)
    })
  })

  test('apiCall testing in custom Hook when responseMapFunction is passed', async () => {
    let data
    const hookData = {
      requestType: 'GET',
      apiEndPointUrl: `${baseURL}/products/1`,
      requiredParams: [{ apiEndPointUrl: `${baseURL}/products/1` }, { requestType: 'GET' }],
      responseMapFunction: jest.fn().mockReturnValue(responseData),
    }
    global.fetch = () =>
      Promise.resolve({
        status: 200,
        text: () => Promise.resolve(JSON.stringify(responseData)),
      })
    await act(() => {
      const { result } = renderHook(() => apiWrapper.default(hookData))
      data = result
    })
    await waitFor(() => {
      expect(data.current.response).toStrictEqual(responseData)
    })
  })

  test('apiCall testing in custom Hook when responseMapFunction and paramToPassInMapFunction is passed', async () => {
    let data
    const hookData = {
      requestType: 'GET',
      apiEndPointUrl: `${baseURL}/products/1`,
      requiredParams: [{ apiEndPointUrl: `${baseURL}/products/1` }, { requestType: 'GET' }],
      responseMapFunction: jest.fn().mockReturnValue(responseData),
      paramToPassInMapFunction: {},
    }
    global.fetch = () =>
      Promise.resolve({
        status: 200,
        text: () => Promise.resolve(JSON.stringify(responseData)),
      })
    await act(() => {
      const { result } = renderHook(() => apiWrapper.default(hookData))
      data = result
    })
    await waitFor(() => {
      expect(data.current.response).toStrictEqual(responseData)
    })
  })

  test('useEffect else testing', () => {
    const hookData = {
      requestType: 'GET',
      apiEndPointUrl: `${baseURL}/products/1`,
      data: responseData.product,
      requiredParams: [{ apiEndPointUrl: `${baseURL}/products/1` }, { requestType: 'GET' }],
    }
    const { result } = renderHook(() => apiWrapper.default(hookData))
    expect(result.current.response).toStrictEqual(responseData.product)
  })

  test('apiCall testing in custom Hook without data having error 401', async () => {
    let data
    const hookData = {
      requestType: 'GET',
      apiEndPointUrl: `${baseURL}/products/1`,
      requiredParams: [{ apiEndPointUrl: `${baseURL}/products/1` }, { requestType: 'GET' }],
    }
    global.fetch = () =>
      Promise.resolve({
        status: 401,
        text: () => Promise.resolve(JSON.stringify(responseData.apiError)),
      })
    await act(() => {
      const { result } = renderHook(() => apiWrapper.default(hookData))
      data = result
    })
    expect(data.current.response).toBe(false)
  })

  test('apiCall testing in custom Hook without data having error 403', async () => {
    let data
    const hookData = {
      requestType: 'GET',
      apiEndPointUrl: `${baseURL}/products/1`,
      requiredParams: [{ apiEndPointUrl: `${baseURL}/products/1` }, { requestType: 'GET' }],
    }
    global.fetch = () =>
      Promise.resolve({
        status: 403,
        text: () => Promise.resolve(JSON.stringify(responseData.apiError)),
      })
    await act(() => {
      const { result } = renderHook(() => apiWrapper.default(hookData))
      data = result
    })
    expect(data.current.response).toBe(false)
  })
})
