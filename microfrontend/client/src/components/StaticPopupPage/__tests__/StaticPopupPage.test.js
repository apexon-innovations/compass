import React from 'react'
import { render, screen, fireEvent } from '@testing-library/react'
import StaticPopupPage from '../StaticPopupPage'


describe('src/components/StaticPopupPage/StaticPopupPage.js', () => {
  it('test onClick when redirectUrl is taken from default argument', () => {
    const mockFn = jest.fn();
    render(
      <StaticPopupPage navigate={mockFn} />
    )
    fireEvent.click(screen.getByRole('img'));
    expect(mockFn).toBeCalledWith('/client/overview')
  })

  it('test onClick when redirectUrl is passed null', () => {
    const mockFn = jest.fn();
    render(
      <StaticPopupPage navigate={mockFn} redirectUrl={null} />
    )
    fireEvent.click(screen.getByRole('img'));
    expect(mockFn).toBeCalledWith('/client/overview')
  })
})
