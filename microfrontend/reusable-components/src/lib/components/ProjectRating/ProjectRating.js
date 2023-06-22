import React, { useRef } from 'react'
import { ProgressBar } from 'react-bootstrap'
import ModalContainer from '../ModalContainer/ModalContainer'
import MemberMoodGraph from '../MemberMoodGraph/MemberMoodGraph'
import style from './ProjectRating.module.scss'

const ProjectRating = React.memo(() => {
  const modelRef = useRef()
  const progressValue = '3.6'
  const progressLocation = progressValue * 2 * 10 + '%'

  return (
    <div>
      <div className={[style.projectRating, 'd-none animated fadeInUp', 'blurAll'].join(' ')}>
        <div
          className={[style.ratingBox, style.amber].join(' ')}
          onClick={() => {
            modelRef.current.handleModelToggle()
          }}
        >
          <div className={style.progressTitle}>Members Mood</div>
          <ProgressBar now={100} />
          <div className={style.projectProgressBar}>
            <div
              className={[style.division, style.amber].join(' ')}
              style={{ marginLeft: progressLocation }}
            >
              <div className={style.number}>{progressValue}</div>
            </div>
          </div>
        </div>
      </div>
      <ModalContainer size="big" ref={modelRef}>
        <MemberMoodGraph />
      </ModalContainer>
    </div>
  )
})

export default ProjectRating
