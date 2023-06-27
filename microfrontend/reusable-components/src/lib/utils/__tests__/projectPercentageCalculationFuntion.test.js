/* eslint-disable */
import {
  getProjectsOverallHealth,
  calculateProjectPercentage,
  calculatePercentageAndHeight,
} from '../projectPercentageCalculationFuntion'
import projectData from './projectData'

describe(`Get Projects Overall Health`, () => {
  it('Get Projects Overall Health With Project Data', () => {
    const colorObj = getProjectsOverallHealth(projectData)
    expect(colorObj).toEqual({ red: 4, amber: 3, green: 1, grey: 2 })
  })

  it('Calculate Project Percentage', () => {
    const colorObj = calculateProjectPercentage(5, 5, 5, 5)
    expect(colorObj).toEqual({
      redPercentage: '25.0',
      amberPercentage: '25.0',
      greenPercentage: '25.0',
      greyPercentage: '25.0',
    })
  })

  it('Calculate Percentage And Height', () => {
    const colorObj = calculatePercentageAndHeight(5, 5, 5, 5)
    expect(colorObj).toEqual({
      amberData: {
        color: 'amber',
        heightValue: 65,
        percentage: '25.0',
        value: 5,
      },
      greenData: {
        color: 'green',
        heightValue: 65,
        percentage: '25.0',
        value: 5,
      },
      greyData: {
        color: 'grey',
        heightValue: 65,
        percentage: '25.0',
        value: 5,
      },
      redData: {
        color: 'red',
        heightValue: 65,
        percentage: '25.0',
        value: 5,
      },
    })
  })
})
