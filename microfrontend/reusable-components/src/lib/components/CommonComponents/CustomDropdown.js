import React from 'react'
import { Dropdown, DropdownButton } from 'react-bootstrap'
import PropTypes from 'prop-types'
import { Scrollbars } from 'react-custom-scrollbars'

const CustomDropdown = React.memo(
  ({ type, alignRight, items, selectedOption, onSelectCallback, bordered }) => {
    return (
      <div
        className={
          bordered ? ['bordered', 'transparentDropdownBox'].join(' ') : 'transparentDropdownBox'
        }
      >
        <DropdownButton
          className={type || ''}
          alignRight={alignRight ? alignRight : false}
          id="dropdown-basic-button"
          title={selectedOption ? selectedOption : '  '}
          onSelect={onSelectCallback ? onSelectCallback : () => {}}
        >
          <Scrollbars autoHeight autoHeightMax={`140px`}>
            {items.map((item, key) => (
              <Dropdown.Item key={key.toString()} eventKey={key.toString()}>
                {typeof item === 'object' ? item.displayName : item}
              </Dropdown.Item>
            ))}
          </Scrollbars>
        </DropdownButton>
      </div>
    )
  },
)

CustomDropdown.propTypes = {
  items: PropTypes.any,
  alignRight: PropTypes.any,
  selectedOption: PropTypes.any,
  onSelectCallback: PropTypes.any,
  type: PropTypes.string,
  bordered: PropTypes.bool,
}

export default CustomDropdown
