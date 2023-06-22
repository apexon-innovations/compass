/* eslint-disable */
import React from 'react'
import { render } from '@testing-library/react'
import { projectId, boardId } from '../../../../__tests__/commonTestFunction'
import JiraBoardDropDownWrapper from '../JiraBoardDropDownWrapper'

const props = {
  boardId,
  projectId,
}

describe('JiraBoard DropDown Wrapper', () => {
  it('Snapshot', () => {
    render(<JiraBoardDropDownWrapper {...props} />)
  })
})
