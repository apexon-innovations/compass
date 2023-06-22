import React from 'react'
import { render } from '@testing-library/react'
import SecurityComplianceScoreWidget from '../SecurityComplianceScoreWidget'

const props = {
  repositoryName: 'Repository Name',
}

describe('SecurityComplianceScoreWidget', () => {
  it('render component', () => {
    render(<SecurityComplianceScoreWidget {...props} />)
  })
})
