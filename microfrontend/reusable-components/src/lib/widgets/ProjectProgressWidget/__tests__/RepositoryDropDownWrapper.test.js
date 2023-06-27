import React from 'react'
import { render } from '@testing-library/react'
import { repoId, projectId, timeDuration } from '../../../../__tests__/commonTestFunction'
import RepositoryDropDownWrapper from '../RepositoryDropDownWrapper'

const props = {
  repoId,
  projectId,
  timeDuration,
}

describe('Repository DropDown Wrapper', () => {
  it('Snapshot', () => {
    render(<RepositoryDropDownWrapper {...props} />)
  })
})
