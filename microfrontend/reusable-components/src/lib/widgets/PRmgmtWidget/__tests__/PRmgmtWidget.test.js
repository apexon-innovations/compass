import React from 'react'
import { render, waitFor, cleanup } from '@testing-library/react'
import { baseUrl, repoId } from '../../../../__tests__/commonTestFunction'
import PRmgmtWidget from '../PRmgmtWidget'

const props = {
  repoId: repoId,
  apiEndPointUrlOfSubmitterMetrics: `${baseUrl}/strategy-service/project/repo/submitter/metrics?repoIds=${repoId}`,
  apiEndPointUrlOfPRPieChart: `${baseUrl}/strategy-service/project/repo/pullrequests/summary?repoIds=${repoId}`,
  apiEndPointUrlOfPRMgmt: `${baseUrl}/strategy-service/project/repo/pullrequests/journey?repoIds=${repoId}`,
}

describe('PRmgmtWidget', () => {
  it('render component', async () => {
    await waitFor(() => {
      render(<PRmgmtWidget {...props} />)
    })
  })
})
