import React from 'react'
import { render } from '@testing-library/react'
import TooltipDefectTrendType from '../TooltipDefectTrendType'
import { toolTipData } from './data'

const props = { ...toolTipData }

describe('TooltipDefectTrendType', () => {
  it('render component', () => {
    render(<TooltipDefectTrendType {...props} />)
  })

  it('render component with no data', () => {
    render(<TooltipDefectTrendType />)
  })
})
