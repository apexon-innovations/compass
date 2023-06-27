import React from 'react'
import { render } from '@testing-library/react'
import BillingBox from '../BillingBox'

describe(`Billing Box component`, () => {
  it('Billing Box rendered correctly', () => {
    render(<BillingBox />)
  })
})
