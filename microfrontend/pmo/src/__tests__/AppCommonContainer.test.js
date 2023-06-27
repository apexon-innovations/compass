import React from 'react'
import AppCommonContainer from '../AppCommonContainer'
import { render } from '@testing-library/react'

describe('App', () => {
  it('<App/> should render correctly ', () => {
    window.addEventListener = jest.fn((event, cb) => {
      if (event === 'updateProjectIcon') {
        cb()
      }
    });
    render(
        <AppCommonContainer location={{ pathname: '/pmo/operational/5e7dbe36215e091c132c8f88' }} />
    )
  })
})
