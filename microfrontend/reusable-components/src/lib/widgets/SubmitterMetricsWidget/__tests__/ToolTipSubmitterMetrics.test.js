import React from 'react'
import { render } from '@testing-library/react'
import ToolTipSubmitterMetrics from '../ToolTipSubmitterMetrics'
import { toolTipData } from './data'

const props = { ...toolTipData }

describe('ToolTipSubmitterMetrics', () => {
  it('render component', () => {
    render(<ToolTipSubmitterMetrics {...props} />)
  })

  it('render component with no data', () => {
    render(<ToolTipSubmitterMetrics />)
  })
})
