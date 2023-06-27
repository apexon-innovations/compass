import React from 'react'
import { render } from '@testing-library/react'
import ErrorMessage from '../ErrorMessage'
describe(`Error Message Box component`, () => {
  it('Error Message rendered correctly', () => {
    render(<ErrorMessage error={{ message: 'test', errorCode: 403, isMsgPassed: true }} />)
  })

  it('Error Message rendered correctly error code 404', () => {
    render(<ErrorMessage error={{ message: 'test', errorCode: 404, isMsgPassed: true }} />)
  })

  it('Error Message rendered correctly other error code', () => {
    render(<ErrorMessage error={{ errorCode: 201, isMsgPassed: true }} />)
  })
})
