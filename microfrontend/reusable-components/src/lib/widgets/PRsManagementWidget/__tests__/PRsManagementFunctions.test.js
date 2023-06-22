import { getSprintStatusColor, getFilterData } from '../PRsManagementFunctions'
import { prManagementFunctionData, prManagementFunctionDataOutput } from './data'

describe(`PRsManagementFunctions`, () => {
  it(`Get Sprint Status Color Return Green`, () => {
    const response = getSprintStatusColor({ state: 'merged' })
    expect(response).toEqual('green')
  })

  it(`Get Sprint Status Color Return Red`, () => {
    const response = getSprintStatusColor({ state: 'declined' })
    expect(response).toEqual('red')
  })

  it(`Get Sprint Status Color Return Amber`, () => {
    const response = getSprintStatusColor({ state: 'unattended' })
    expect(response).toEqual('amber')
  })

  it(`Get Sprint Status Color Return Blue`, () => {
    const response = getSprintStatusColor({ state: '' })
    expect(response).toEqual('blue')
  })

  it(`Get Filter Data Of Merged`, () => {
    const response = getFilterData(prManagementFunctionData, ['green'])
    expect(response).toEqual(prManagementFunctionDataOutput)
  })

  it(`Get Filter Data Of Unattend`, () => {
    const response = getFilterData(prManagementFunctionData, ['blue'])
    expect(response).toEqual([])
  })
})
