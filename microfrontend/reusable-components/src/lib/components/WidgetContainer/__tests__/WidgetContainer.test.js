import React from 'react'
import { render, screen } from '@testing-library/react'
import WidgetContainer from '../WidgetContainer'

describe('widget container test', () => {
  test('isLoading is false', () => {
    const props = {
      children: (
        <div>
          <p>DANVOUY Womens T Shirt Casual Cotton Short</p>
        </div>
      ),
      isLoading: false,
      data: {
        title: 'DANVOUY Womens T Shirt Casual Cotton Short',
        price: 12.99,
        description:
          '95%Cotton,5%Spandex, Features: Casual, Short Sleeve, Letter Print,V-Neck,Fashion Tees, The fabric is soft and has some stretch., Occasion: Casual/Office/Beach/School/Home/Street. Season: Spring,Summer,Autumn,Winter.',
        category: 'women clothing',
        image: 'https://fakestoreapi.com/img/61pHAEJ4NML._AC_UX679_.jpg',
      },
    }
    render(<WidgetContainer {...props} />)
    expect(screen.getByText('DANVOUY Womens T Shirt Casual Cotton Short')).toBeTruthy()
  })

  test('isLoading is true', () => {
    const props = {
      isLoading: true,
    }
    render(<WidgetContainer {...props} />)
  })

  test('isLoading is true and loadingComponent is passed', () => {
    const props = {
      isLoading: true,
      loadingComponent: <div>Loading</div>,
    }
    render(<WidgetContainer {...props} />)
    expect(screen.getByText('Loading')).toBeTruthy()
  })

  test('error test when errorClass is true', () => {
    const props = {
      error: { message: 'Test Error Message', errorCode: 403, isMsgPassed: true },
      data: {},
      errorMessage: true,
      errorClass: true,
    }
    render(<WidgetContainer {...props} />)
    expect(screen.getByText('Test Error Message')).toBeTruthy()
  })

  test('error test when errorClass is false', () => {
    const props = {
      error: { message: 'Test Error Message', errorCode: 403, isMsgPassed: true },
      data: {},
      errorMessage: true,
    }
    render(<WidgetContainer {...props} />)
    expect(screen.getByText('Test Error Message')).toBeTruthy()
  })

  test('error test for errorMessage to be false', () => {
    const props = {
      error: { message: 'Test Error Message', errorCode: 403, isMsgPassed: true },
      data: {},
      errorMessage: false,
    }
    render(<WidgetContainer {...props} />)
    expect(screen.queryByText('Test Error Message')).toBeFalsy()
  })
})
