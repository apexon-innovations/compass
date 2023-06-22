import React from 'react'
import { render } from '@testing-library/react'
import TooltipCodeHealthSnapshotWidget from '../TooltipCodeHealthSnapshotWidget'
import { toolTipData } from './data'

const props = { ...toolTipData }

describe('TooltipCodeHealthSnapshotWidget', () => {
  it('render component', () => {
    render(<TooltipCodeHealthSnapshotWidget {...props} />)
  })

  it('render component with no Data', () => {
    render(<TooltipCodeHealthSnapshotWidget />)
  })
})
