import React from 'react'
import PropTypes from 'prop-types'
import { ProgressBar } from 'react-bootstrap'
import WidgetContainer from '../../components/WidgetContainer/WidgetContainer'
import useWidgetAPIWrapper from '../../components/WidgetContainer/useWidgetAPIWrapper'
import style from './ProjectRatingCompliance.module.scss'

const responseMap = response => {
  if (response && response.data) {
    return response.data
  }
  return false
}

const returnCompStatusColorClass = complianceScore => {
  if (complianceScore < 9) {
    return 'c10'
  } else if (complianceScore === 100) {
    return 'c90'
  } else {
    let complianceScoreNumber = complianceScore.toString()[0]
    return `c${complianceScoreNumber}0`
  }
}

const ProjectRatingCompliance = React.memo(({ apiEndPointUrl, data, callback }) => {
  const { response, error, isLoading } = useWidgetAPIWrapper({
    apiEndPointUrl,
    data,
    callback,
    responseMapFunction: responseMap,
  })
  return (
    <div className={[style.projectRating, ' animated fadeInUp'].join(' ')}>
      <WidgetContainer isLoading={isLoading} data={response} error={error} errorMessage={false}>
        {response && response.compliance ? (
          <div className={[style.ratingBox, style.amber].join(' ')}>
            <div className={style.progressTitle}>Compliance</div>
            <ProgressBar now={response.compliance} />
            <div className={style.projectProgressBar}>
              <div
                className={[style.division].join(' ')}
                style={{ marginLeft: `${response.compliance}%` }}
              >
                <div
                  className={[
                    style.number,
                    style[returnCompStatusColorClass(response.compliance)],
                  ].join(' ')}
                >{`${response.compliance.toFixed(2)} %`}</div>
              </div>
            </div>
          </div>
        ) : (
          ''
        )}
      </WidgetContainer>
    </div>
  )
})

ProjectRatingCompliance.propTypes = {
  apiEndPointUrl: PropTypes.string,
  data: PropTypes.any,
  callback: PropTypes.func,
}

export default ProjectRatingCompliance
