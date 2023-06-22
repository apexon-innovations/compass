import React from 'react'
import { render } from '@testing-library/react'
import TooltipDefectAgeing from '../TooltipDefectAgeing'
import { toolTipData } from './data'

const props = { ...toolTipData }

describe('TooltipDefectAgeing', () => {
  it('render component', () => {
    render(<TooltipDefectAgeing {...props} />)
  })

  it('render component with no data', () => {
    render(<TooltipDefectAgeing />)
  })
})
