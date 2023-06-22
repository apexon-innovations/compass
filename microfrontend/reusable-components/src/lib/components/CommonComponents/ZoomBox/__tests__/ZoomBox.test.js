import React from 'react'
import ZoomBox from '../ZoomBox'
import { fireEvent, render, screen } from '@testing-library/react'

describe('ZoomBox', () => {
  it('render component correctly when zoomEnable is true and onClick function is not passed', async () => {
    const mockFn = jest.fn()
    render(
      <ZoomBox zoomEnable={true} redirectURL={'/client/defect-ageing'} navigate={mockFn}>
        {' '}
        <div>
          <p>Child element</p>
        </div>{' '}
      </ZoomBox>,
    )
    fireEvent.click(screen.getByRole('img'))
    expect(mockFn).toBeCalledWith('/client/defect-ageing')
  })

  it('render component correctly when zoomEnable is true and onClick function is  passed', async () => {
    const mockFn = jest.fn()
    render(
      <ZoomBox zoomEnable={true} redirectURL={'/client/defect-ageing'} onClick={mockFn}>
        {' '}
        <div>
          <p>Child element</p>
        </div>{' '}
      </ZoomBox>,
    )
    fireEvent.click(screen.getByRole('img'))
    expect(mockFn).toBeCalled()
  })

  it('render component correctly when zoomEnable is false', async () => {
    render(
      <ZoomBox zoomEnable={false}>
        {' '}
        <div>
          <p>Child element</p>
        </div>{' '}
      </ZoomBox>,
    )
  })
})
