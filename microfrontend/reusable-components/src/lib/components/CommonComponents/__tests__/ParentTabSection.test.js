import React from 'react'
import { fireEvent, render, screen } from '@testing-library/react'
import ParentTabSection, { CustomTab } from '../ParentTabSection'

describe(`ParentTabSection Box component`, () => {
  it('ParentTabSection rendered correctly', () => {
    render(
      <ParentTabSection defaultActiveKey={0}>
        <CustomTab title="Issues/Complexities">
          <div>This is 1st Tab</div>
        </CustomTab>
        <CustomTab title="Test">
          <div>This is 2nd Tab</div>
        </CustomTab>
      </ParentTabSection>,
    )
    fireEvent.click(screen.getByText('Test'))
    expect(screen.getByText('This is 2nd Tab')).toBeDefined()
  })

  it('ParentTabSection rendered correctly when same tab is clicked twice', () => {
    render(
      <ParentTabSection defaultActiveKey={1} callback={jest.fn()}>
        <CustomTab title="">
          <div>This is 1st Tab</div>
        </CustomTab>
        <CustomTab title="Test">
          <div>This is 2nd Tab</div>
        </CustomTab>
      </ParentTabSection>,
    )
    fireEvent.click(screen.getByText('Test'))
    expect(screen.getByText('This is 2nd Tab')).toBeDefined()
  })

  it('render CustomTab', () => {
    render(
      <CustomTab title="Test">
        <div>Test 1</div>
      </CustomTab>,
    )
  })
})
