import React from 'react'
import PropTypes from 'prop-types'
import ParentTabSection from '../../components/CommonComponents/ParentTabSection'
import { CustomTab } from '../../components/CommonComponents/ParentTabSection'
import BracketBox from '../../components/CommonComponents/BracketBox/BracketBox'
import SprintIdWidgetContainer from '../../components/WidgetContainer/SprintIdWidgetContainer'
import style from './ReviewerCollaborationWidget.module.scss'
import ReviewerMetricsWidget from '../ReviewerMetricsWidget/ReviewerMetricsWidget'
import CollaborationMetricsWidget from '../CollaborationMetricsWidget/CollaborationMetricsWidget'

const ReviewerCollaborationWidget = ({
  apiEndPointUrlOfReviewerMetrics,
  apiEndPointUrlOfCollaborativeMetrics,
  repoId,
}) => {
  return (
    <div className={style.reviewMetricsAndCollabMetrics}>
      <BracketBox verticalAlignment="alignTop" animStatus="" boxTitle={''}>
        <ParentTabSection defaultActiveKey={0}>
          <CustomTab title="Reviewer Metrics">
            <SprintIdWidgetContainer sprintId={repoId}>
              <ReviewerMetricsWidget
                key={repoId}
                apiEndPointUrl={apiEndPointUrlOfReviewerMetrics}
              />
            </SprintIdWidgetContainer>
          </CustomTab>
          <CustomTab title="Collaboration Metrics">
            <SprintIdWidgetContainer sprintId={repoId}>
              <CollaborationMetricsWidget
                key={repoId}
                apiEndPointUrl={apiEndPointUrlOfCollaborativeMetrics}
              />
            </SprintIdWidgetContainer>
          </CustomTab>
        </ParentTabSection>
      </BracketBox>
    </div>
  )
}

ReviewerCollaborationWidget.propTypes = {
  apiEndPointUrlOfReviewerMetrics: PropTypes.string,
  apiEndPointUrlOfCollaborativeMetrics: PropTypes.string,
  repoId: PropTypes.any,
}

export default ReviewerCollaborationWidget
