import React from 'react'
import { render } from '@testing-library/react'
import NPSWidget from '../NPSWidget'
import { RecentResponse, LastFiveResponse } from './data'
import { mockJest } from '../../../../__tests__/mockData'

describe('NPSWidget', () => {
  it('render component with Recent response', async () => {
    mockJest(200, JSON.stringify(RecentResponse))
    render(<NPSWidget />)
  })

  it('render component with Last Five response', async () => {
    mockJest(200, JSON.stringify(LastFiveResponse))
    render(<NPSWidget />)
  })
})
