import { render } from '@testing-library/react'
import CustomDropDown from '../CustomDropdown'

describe('CustomDropDown', () => {
  test('render component correctly', () => {
    const props = {
      type: 'test',
      alignRight: true,
      items: [
        {
          displayName: 'test1',
        },
        {
          displayName: 'test2',
        },
      ],
      selectedOption: 'test1',
      onSelectCallback: jest.fn(),
      bordered: true,
    }
    render(<CustomDropDown {...props} />)
  })

  test('render component correctly with different props', () => {
    const props = {
      items: ['test1', 'test2'],
    }
    render(<CustomDropDown {...props} />)
  })
})
