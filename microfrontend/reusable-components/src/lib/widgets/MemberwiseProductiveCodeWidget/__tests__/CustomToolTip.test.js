import React from 'react'
import { render } from '@testing-library/react'
import CustomToolTip from '../CustomToolTip'
import { toolTipData } from './data'

const props = { ...toolTipData }

describe('CustomToolTip', () => {
  it('render component', () => {
    render(<CustomToolTip {...props} />)
  })

  it('render component when no data', () => {
    render(<CustomToolTip />)
  })
})
