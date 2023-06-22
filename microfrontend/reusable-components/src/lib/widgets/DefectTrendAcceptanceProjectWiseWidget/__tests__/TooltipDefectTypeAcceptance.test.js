import React from 'react'
import { render } from '@testing-library/react'
import TooltipDefectTypeAcceptance from '../TooltipDefectTypeAcceptance'
import { toolTipData } from './data'

const props = { ...toolTipData }

describe('TooltipDefectTypeAcceptance', () => {
  it('render component', () => {
    render(<TooltipDefectTypeAcceptance {...props} />)
  })

  it('render component with no data', () => {
    render(<TooltipDefectTypeAcceptance />)
  })
})
