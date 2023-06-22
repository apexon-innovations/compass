import React from 'react'
import ArrowComponent from '../ArrowComponent'
import { fireEvent, render, screen, waitFor } from '@testing-library/react'

describe(`ArrowComponent Box component`, () => {
  it('ArrowComponent rendered correctly when selectedValue is not passed', async () => {
    const { container } = render(<ArrowComponent callback={jest.fn()} />)
    fireEvent.click(container.getElementsByTagName('div')[4])
    expect(screen.getByText('Current Sprint')).toBeDefined()
  })

  it('ArrowComponent rendered correctly when selectedValue is more than 4', () => {
    const { container } = render(<ArrowComponent callback={jest.fn()} selectedValue="1" />)
    fireEvent.click(container.getElementsByTagName('div')[8])
    expect(screen.getByText('Prev Sprint 1')).toBeDefined()
  })
})
