import React from 'react'
import LogoAnimation from '../LogoAnimation'
import { render } from '@testing-library/react'

jest.useFakeTimers()
describe('<LogoAnimation/>', () => {
  it('should render the <LogoAnimation/> component correctly ', () => {
    document.body.innerHTML =
      '<div>' +
      '  <div id="logoTextColor" >Hello</div>' +
      '  <div id="landingLogoAnimation" >Hello</div>' +
      '  <div class="animationText" >Hello</div>' +
      '  <div id="animationLogo" >Hello</div>' +
      '  <div><stop></stop><stop></stop></div>' +
      '  <div><path></path><path></path></div>' +
      '  <button id="button" />' +
      '</div>'
    render(<LogoAnimation />)
    jest.advanceTimersByTime(9000)
  })
})
