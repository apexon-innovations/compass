import React from 'react'
import { render } from '@testing-library/react'
import { projectId, baseUrl, repoId } from '../../../../__tests__/commonTestFunction'
import ReviewerCollaborationWidget from '../ReviewerCollaborationWidget'

const props = {
  repoId: repoId,
  apiEndPointUrlOfReviewerMetrics: `${baseUrl}/strategy-service/project/repo/reviewer/metrics?repoIds=${repoId.join(
    ',',
  )}&dayCount=7&iscProjectId=${projectId}`,
  apiEndPointUrlOfCollaborativeMetrics: `${baseUrl}/strategy-service/project/repo/collaboration/metrics?repoIds=${repoId.join(
    ',',
  )}&dayCount=7&iscProjectId=${projectId}`,
}

describe('ReviewerCollaborationWidget', () => {
  it('render component', () => {
    render(<ReviewerCollaborationWidget {...props} />)
  })
})
