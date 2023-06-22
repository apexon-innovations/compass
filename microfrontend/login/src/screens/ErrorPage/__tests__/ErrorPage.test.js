import React from 'react'
import ErrorPage from '../ErrorPage'
import { render } from '@testing-library/react'

jest.useFakeTimers()
describe('src/screens/ErrorPage/ErrorPage.js', () => {
  it('test navigation', () => {
    const mockFn = jest.fn();
    render(<ErrorPage navigate={mockFn} />)
    jest.advanceTimersByTime(3000)
    expect(mockFn).toBeCalledWith('/login')
  })
})
