import {
  getColorBasedOnCode,
  colorCalculationReverse,
  colorCalculation,
  convertProjectDataToDropDData,
} from '../commonFunction'

describe(`Get Color Based On Code`, () => {
  it('Get Color Based On Code Return Green', () => {
    const colorObj = getColorBasedOnCode('G')
    expect(colorObj).toEqual({ name: 'GREEN', color: 'green', label: 'Green' })
  })

  it('Get Color Based On Code Return Red', () => {
    const colorObj = getColorBasedOnCode('R')
    expect(colorObj).toEqual({ name: 'RED', color: 'red', label: 'Red' })
  })
})

describe(`Color Calculation`, () => {
  it('Color Calculation Return Green', () => {
    const colorObj = colorCalculation(20)
    expect(colorObj).toEqual({ className: 'green', color: '#24ff00' })
  })

  it('Color Calculation Return Amber', () => {
    const colorObj = colorCalculation(50)
    expect(colorObj).toEqual({ className: 'amber', color: '#ffb245' })
  })

  it('Color Calculation Return Red', () => {
    const colorObj = colorCalculation(99)
    expect(colorObj).toEqual({ className: 'red', color: '#e00e2c' })
  })
})

describe(`Color Calculation Reverse`, () => {
  it('Color Calculation ReverseReturn Green', () => {
    const colorObj = colorCalculationReverse(90)
    expect(colorObj).toEqual({ className: 'green', color: '#24ff00' })
  })

  it('Color Calculation ReverseReturn Amber', () => {
    const colorObj = colorCalculationReverse(50)
    expect(colorObj).toEqual({ className: 'amber', color: '#ffb245' })
  })

  it('Color Calculation ReverseReturn Red', () => {
    const colorObj = colorCalculationReverse(20)
    expect(colorObj).toEqual({ className: 'red', color: '#e00e2c' })
  })
})

describe(`Convert P Data To DD Data`, () => {
  it('Convert P Data To DD Data empty', () => {
    const result = convertProjectDataToDropDData(false)
    expect(result).toEqual(false)
  })

  it('Convert P Data To DD Data empty', () => {
    const result = convertProjectDataToDropDData([{ name: 'ProjectName', id: 'projectID' }])
    expect(result).toEqual([{ label: 'ProjectName', value: 'projectID' }])
  })
})
