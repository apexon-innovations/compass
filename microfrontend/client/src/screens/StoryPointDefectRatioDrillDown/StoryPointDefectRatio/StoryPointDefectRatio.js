import React, { useState } from 'react'
import StaticPopupPage from 'components/StaticPopupPage/StaticPopupPage'
import StoryPointsDefectRatioWidget from 'reusable-components/dist/widgets/StoryPointsDefectRatioWidget/StoryPointsDefectRatioWidget/StoryPointsDefectRatioWidget'
import CustomDropdown from 'reusable-components/dist/components/CommonComponents/CustomDropdown'
import style from '../DefectRatioDrillDown.module.scss'

const StoryPointDefectRatio = () => {
  const sprintSelectDropdown = [
    { displayName: 'Previous 1 Sprints', value: 1 },
    { displayName: 'Previous 5 Sprints', value: 5 },
    { displayName: 'Previous 10 Sprints', value: 10 },
    { displayName: 'Previous 15 Sprints', value: 15 },
  ]

  const [selectedSprint, setSelectedSprint] = useState(sprintSelectDropdown[0])
  const baseUrl = process.env.REACT_APP_API_END_POINT
  return (
    <StaticPopupPage redirectUrl={'/client/overview'}>
      <div className="titleArea">
        <h2 className="title">Story Point Defect Ratio box</h2>
        <div className="controlSpace">
          <CustomDropdown
            items={sprintSelectDropdown}
            selectedOption={selectedSprint.displayName}
            onSelectCallback={selectData => {
              setSelectedSprint(sprintSelectDropdown[selectData])
            }}
            bordered={true}
          />
        </div>
      </div>
      <div className={[style.collapsed].join(' ')}>
        <StoryPointsDefectRatioWidget
          apiEndPointUrl={`${baseUrl}/client-dashboard-service/projects/consolidatedStoryPointDefectRatio?iscProjectIds=all&sprintCount=${selectedSprint.value}`}
          detailsUrl={'/client/client-defect-ratio-graphs'}
          zoomEnable={false}
        />
      </div>
    </StaticPopupPage>
  )
}

export default StoryPointDefectRatio
