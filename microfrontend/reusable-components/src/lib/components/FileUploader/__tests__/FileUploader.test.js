import React from 'react'
import FileUploader from '../FileUploader'
import { render } from '@testing-library/react'

describe('FileUploader', () => {
  it('render component', async () => {
    render(
      <FileUploader
        onCompleted={() => {}}
        onError={() => {}}
        name={'Name'}
        base64={false}
        accept={
          '.xls,.xlsx, application/vnd.openxmlformats-officedocument.spreadsheetml.sheet,application/vnd.ms-excel'
        }
      />,
    )
  })
})
