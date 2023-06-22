import React from 'react'
import { render } from '@testing-library/react'
import ToolTipStoryPoint from '../ToolTipStoryPoint'
import { toolTipData, toolTipDataIndexFalse } from './data'

const props = { ...toolTipData }

describe('ToolTipStoryPoint', () => {
  it('Snapshot', () => {
    render(<ToolTipStoryPoint {...props} />)
  })

  it('Snapshot Index False', () => {
    render(<ToolTipStoryPoint {...toolTipDataIndexFalse} />)
  })

  it('Snapshot No data', () => {
    render(<ToolTipStoryPoint />)
  })
})
