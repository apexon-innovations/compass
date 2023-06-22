import React from 'react'
import { render } from '@testing-library/react'
import SliderWrapper from '../SliderWrapper'

describe('SliderWrapper', () => {
  it('render component', () => {
    render(
      <SliderWrapper title="Slider Title">
        <div>Slide 1</div>
        <div>Slide 2</div>{' '}
      </SliderWrapper>,
    )
  })
})
