import React from 'react'
import DefectAgeingDrillDown from '../DefectAgeingDrillDown'
import { render } from '@testing-library/react'
import { storeProjectClientData } from 'reusable-components/dist/utils/projectDataStoreFunction'
import { mockProjectData } from '../../Client/__tests__/data'

describe('src/screens/DefectAgeingDrillDown/DefectAgeingDrillDown.js', () => {
  it('should render the <DefectAgeingDrillDown/> component correctly ', () => {
    storeProjectClientData('PROJECT_CLIENT_DATA', mockProjectData.data)
    render(
        <DefectAgeingDrillDown />
    )
  })
})
