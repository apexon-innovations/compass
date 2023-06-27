import React from 'react'
import PaginationArrows from '../PaginationArrows'
import { fireEvent, render, screen } from '@testing-library/react'

describe('PaginationArrows', () => {
  it('render component to test previous arrow', () => {
    const mockFn = jest.fn()
    render(
      <PaginationArrows
        pageNumber={1}
        noOfItems={5}
        totalItems={[1, 2, 3, 4, 5, 6, 7, 8, 9, 10]}
        setPagingCallback={mockFn}
      />,
    )
    fireEvent.click(screen.getAllByRole('img')[0])
    expect(mockFn).toBeCalledWith(-4)
  })

  it('render component to test next arrow', () => {
    const mockFn = jest.fn()
    render(
      <PaginationArrows
        pageNumber={1}
        noOfItems={5}
        totalItems={[1, 2, 3, 4, 5, 6, 7, 8, 9, 10]}
        setPagingCallback={mockFn}
      />,
    )
    fireEvent.click(screen.getAllByRole('img')[1])
    expect(mockFn).toBeCalledWith(6)
  })

  it('render component when both the arrows are disabled', () => {
    const mockFn = jest.fn()
    render(
      <PaginationArrows
        pageNumber={0}
        noOfItems={5}
        totalItems={[1, 2, 3, 4]}
        setPagingCallback={mockFn}
      />,
    )
    fireEvent.click(screen.getAllByRole('img')[1])
    expect(mockFn).not.toBeCalledWith(6)
  })
})
