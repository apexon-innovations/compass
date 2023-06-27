/* eslint-disable */
import { emailValidation, urlValidation, checkEmpty } from '../validation'

describe(`All Validations `, () => {
  it('Email Validation True', () => {
    const colorObj = emailValidation('demo@apexon.com')
    expect(colorObj).toEqual(true)
  })

  it('Email Validation False', () => {
    const colorObj = emailValidation('demo.apexon')
    expect(colorObj).toEqual(false)
  })

  it('URL Validation True', () => {
    const colorObj = urlValidation('https://www.compass.com')
    expect(colorObj).toEqual(true)
  })

  it('URL Validation False', () => {
    const colorObj = urlValidation('compass')
    expect(colorObj).toEqual(false)
  })

  it('Check Empty Validation True', () => {
    const colorObj = checkEmpty('')
    expect(colorObj).toEqual(true)
  })

  it('Check Empty Validation False', () => {
    const colorObj = checkEmpty('compass')
    expect(colorObj).toEqual(false)
  })
})
