import React from 'react'
import BackArrow from '../BackArrow'
import { fireEvent, render, screen } from '@testing-library/react'

describe('BackArrow', () => {
  it('Test component when back arrow is click but onClick function is not passed', async () => {
    const mockFn = jest.fn()
    const { container } = render(
      <BackArrow
        title={'Compass'}
        redirectURL={'/client/client-delivery-trends'}
        navigate={mockFn}
      />,
    )
    fireEvent.click(container.getElementsByTagName('div')[4])
    expect(mockFn).toBeCalledWith('/client/client-delivery-trends')
  })

  it('Test component when back arrow is click and onClick function is  passed', async () => {
    const mockFn = jest.fn()
    const { container } = render(
      <BackArrow
        title={'Compass'}
        redirectURL={'/client/client-delivery-trends'}
        onClick={mockFn}
      />,
    )
    fireEvent.click(container.getElementsByTagName('div')[4])
    expect(mockFn).toBeCalled()
  })
})
