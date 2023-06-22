import React from 'react'
import MainLandingPage from '../MainLandingPage'
import { render } from '@testing-library/react'
jest.useFakeTimers()

describe('src/screens/MainLandingPage/MainLandingPage.js', () => {
  beforeAll(() => {
    Storage.prototype.getItem = jest.fn(() => {
      return 'U2FsdGVkX1+tKAYiasbooIfEe4aUEMdwTUOXWEcZ9Ru5umWa/IXdwCiGKSboHho8YxS0Kc5JRoh2VBgTGR1N7Ljc3mXrOUQsHr0qDRmE7Auf4dC4RohjWlEPTmoLs6LEQ+70ovzFHOBb+VznZg8E1xX5IJCPFGMifbDlm9fQYQZcUEZq2Vgk/JPR3T7k7sDfwvMFDfIFFlW/Qv8v3vfjDUKSQbX670pgKOtLSfuko/HP5CNjRUNFgaafDLXnjpmpK4LVNvTvBeSbEFv7tUIS6innFbHbHbTZy0+CEdoIGkw='
    })
  })

  it('should render the <MainLandingPage/> component correctly ', () => {
    render(<MainLandingPage />)
  })
})
