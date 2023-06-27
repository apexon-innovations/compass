import { render, screen } from '@testing-library/react'
import SprintIdWidgetContainer from '../SprintIdWidgetContainer'

describe('SprintIdWidgetContainer', () => {
  test('render component without props', () => {
    render(<SprintIdWidgetContainer />)
  })

  test('render component without props', () => {
    render(<SprintIdWidgetContainer children={<div>Sprint Id WidgetContainer</div>} sprintId="1" />)
    expect(screen.getByText('Sprint Id WidgetContainer')).toBeDefined()
  })

  test('render component without props', () => {
    render(
      <SprintIdWidgetContainer children={<div>Sprint Id WidgetContainer</div>} sprintId="none" />,
    )
    expect(screen.getByText('Sorry, No Data Available.')).toBeDefined()
  })
})
